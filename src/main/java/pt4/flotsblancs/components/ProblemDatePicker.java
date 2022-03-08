package pt4.flotsblancs.components;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ProblemDatePicker extends MFXDatePicker {

    private Problem problem;

    private static LocalDate toLocale(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static Date fromLocale(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public ProblemDatePicker(Problem problem, boolean isStartDate) {
        this.problem = problem;

        String text = isStartDate ? "Date de début" : "Date de fin";
        setPromptText(text);
        setFloatingText(text);
        setMaxWidth(180);
        setMinHeight(42);
        setFloatMode(FloatMode.BORDER);
        setAnimated(false);

        Date defaultDate = isStartDate ? problem.getStartDate() : problem.getEndDate();
        if(defaultDate == null)
        	defaultDate = Date.from(Instant.now());
        setValue(toLocale(defaultDate));
        setText(toLocale(defaultDate).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        if (!isStartDate)
            createEndDateListener();
    }

    private void createEndDateListener() {
        valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isAfter(LocalDate.now())) {
                Router.showToast(ToastType.ERROR,
                		"La date donnée est supérieure à la date actuelle");
                setValue(oldDate);
            } else {
            	problem.setEndDate(fromLocale(newDate));
            }
        });
    }

    public void addListener(ChangeListener<? super LocalDate> listener) {
        valueProperty().addListener(listener);
    }
}
