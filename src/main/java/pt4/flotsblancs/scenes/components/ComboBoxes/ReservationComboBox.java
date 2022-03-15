package pt4.flotsblancs.scenes.components.ComboBoxes;

import java.sql.SQLException;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;

public class ReservationComboBox extends RefreshableComboBox<Reservation> {
    private Problem problem;

    public ReservationComboBox(Problem problem) throws SQLException {
        this.problem = problem;

        setFloatingText("Réservations");
        setFloatMode(FloatMode.INLINE);

        getItems().addAll(Database.getInstance().getReservationDao().queryForAll());

        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            problem.setReservation(newValue);
        });
    }

    public void refresh() {
        if (problem.getReservation() != null)
            selectItem(problem.getReservation());
    }
}
