package pt4.flotsblancs.scenes.components;

import java.sql.SQLException;
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

    private boolean skipNextListener;

    public ReservationDatePicker(Reservation reservation, boolean isStartDate) {
        this.reservation = reservation;
        this.skipNextListener = false;

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
        addUserChangedValueListener((obs, oldDate, newDate) -> {
            if (oldDate == null || newDate == null || oldDate == newDate)
                return;
            try {
                reservation.setStartDate(DateUtils.fromLocale(newDate));
            } catch (ConstraintException e) {
                Router.showToast(ToastType.ERROR, e.getMessage());
                this.skipNextListener = true;
                setValue(oldDate);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private void createEndDateListener() {
        addUserChangedValueListener((obs, oldDate, newDate) -> {
            if (oldDate == null || newDate == null || oldDate == newDate)
                return;
            try {
                reservation.setEndDate(DateUtils.fromLocale(newDate));
            } catch (ConstraintException e) {
                Router.showToast(ToastType.ERROR, e.getMessage());
                this.skipNextListener = true;
                setValue(oldDate);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void addUserChangedValueListener(ChangeListener<? super LocalDate> listener) {
        var wrapper = new ChangeListener<>() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (oldValue == null || newValue == null || oldValue == newValue)
                    return;
                if (skipNextListener) {
                    skipNextListener = false;
                    return;
                }
                if (isShowing())
                    listener.changed(observable, (LocalDate) oldValue, (LocalDate) newValue);
            };
        };
        valueProperty().addListener(wrapper);
    }
}
