package pt4.flotsblancs.components;

import java.time.LocalDate;
import java.util.Date;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;
import pt4.flotsblancs.utils.DateUtils;

public class ReservationDatePicker extends MFXDatePicker {

    private Reservation reservation;

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
            try {
                reservation.setStartDate(DateUtils.fromLocale(newDate));
            } catch (ConstraintException e) {
                Router.showToast(ToastType.ERROR, e.getMessage());
                setValue(oldDate);
            }
        });
    }

    private void createEndDateListener() {
        valueProperty().addListener((obs, oldDate, newDate) -> {
            try {
                reservation.setEndDate(DateUtils.fromLocale(newDate));
            } catch (ConstraintException e) {
                Router.showToast(ToastType.ERROR, e.getMessage());
                setValue(oldDate);
            }
        });
    }

    public void addUserChangedValueListener(ChangeListener<? super LocalDate> listener) {
        var wrapper = new ChangeListener<>() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (oldValue == null || newValue == null || oldValue == newValue)
                    return;
                if (isShowing())
                    listener.changed(observable, (LocalDate) oldValue, (LocalDate) newValue);
            };
        };
        valueProperty().addListener(wrapper);
    }
}
