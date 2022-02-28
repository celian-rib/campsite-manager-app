package pt4.flotsblancs.components;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ReservationDatePicker extends MFXDatePicker {

    private Reservation reservation;

    private static LocalDate toLocale(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static Date fromLocale(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public ReservationDatePicker(Reservation reservation, boolean isStartDate) {
        this.reservation = reservation;

        String text = isStartDate ? "Date de début" : "Date de fin";
        setPromptText(text);
        setFloatingText(text);
        setMaxWidth(180);
        setMinHeight(42);
        setFloatMode(FloatMode.BORDER);
        setAnimated(false);

        Date defaultDate = isStartDate ? reservation.getStartDate() : reservation.getEndDate();
        setValue(toLocale(defaultDate));
        setText(toLocale(defaultDate).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        setDisable(toLocale(defaultDate).isBefore(LocalDate.now()));

        if (isStartDate)
            createStartDateListener();
        else
            createEndDateListener();
    }

    private void createStartDateListener() {
        valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isBefore(LocalDate.now())) {
                Router.showToast(ToastType.ERROR,
                        "La date de début sélectionnée est antérieur à la date actuelle");
                setValue(oldDate);
            } else if (newDate.isAfter(toLocale(reservation.getEndDate()))) {
                Router.showToast(ToastType.ERROR,
                        "La date de début sélectionnée est ultérieure à la date de fin");
                setValue(oldDate);
            } else {
                reservation.setStartDate(fromLocale(newDate));
            }
        });
    }

    private void createEndDateListener() {
        valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isBefore(toLocale(reservation.getStartDate()))) {
                Router.showToast(ToastType.ERROR,
                        "La date de fin sélectionnée est antérieure à la date de début de la réservation.");
                setValue(oldDate);
            } else {
                reservation.setEndDate(fromLocale(newDate));
            }
        });
    }

    public void addListener(ChangeListener<? super LocalDate> listener) {
        valueProperty().addListener(listener);
    }
}
