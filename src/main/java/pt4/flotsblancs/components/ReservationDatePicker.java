package pt4.flotsblancs.components;

import java.time.LocalDate;
import java.util.Date;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;
import pt4.flotsblancs.utils.DateUtils;

public class ReservationDatePicker extends MFXDatePicker {

    private Reservation reservation;

    private LocalDate skipNotif;

    public ReservationDatePicker(Reservation reservation, boolean isStartDate) {
        this.reservation = reservation;
        this.skipNotif = null;

        String text = isStartDate ? "Date de début" : "Date de fin";
        setPromptText(text);
        setFloatingText(text);
        setMaxWidth(180);
        setMinHeight(42);
        setFloatMode(FloatMode.BORDER);
        setAnimated(false);

        Date defaultDate = isStartDate ? reservation.getStartDate() : reservation.getEndDate();
        setValue(DateUtils.toLocale(defaultDate));
        setText(DateUtils.toFormattedString(defaultDate));

        setDisable(DateUtils.toLocale(defaultDate).isBefore(LocalDate.now()));

        if (isStartDate)
            createStartDateListener();
        else
            createEndDateListener();
    }

    private void createStartDateListener() {
        valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isBefore(LocalDate.now())) {
                this.skipNotif = oldDate;
                Router.showToast(ToastType.ERROR,
                "La date de début sélectionnée est antérieur à la date actuelle");
                setValue(oldDate);
            } else if (newDate.isAfter(DateUtils.toLocale(reservation.getEndDate()))) {
                this.skipNotif = oldDate;
                Router.showToast(ToastType.ERROR,
                        "La date de début sélectionnée est ultérieure à la date de fin");
                setValue(oldDate);
            } else {
                reservation.setStartDate(DateUtils.fromLocale(newDate));
            }
        });
    }

    private void createEndDateListener() {
        valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isBefore(DateUtils.toLocale(reservation.getStartDate()))) {
                this.skipNotif = oldDate;
                Router.showToast(ToastType.ERROR,
                        "La date de fin sélectionnée est antérieure à la date de début de la réservation.");
                setValue(oldDate);
            } else {
                reservation.setEndDate(DateUtils.fromLocale(newDate));
            }
        });
    }

    public void addListener(ChangeListener<? super LocalDate> listener) {
        valueProperty().addListener((obs, oldDate, newDate) -> {
            if (this.skipNotif ==  newDate) {
                this.skipNotif = null;
                System.out.println("Skipped");
            } else {
                listener.changed(obs, oldDate, newDate);
                System.out.println("Notified");
            }
        });
    }
}
