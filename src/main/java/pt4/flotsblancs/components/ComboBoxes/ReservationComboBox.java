package pt4.flotsblancs.components.ComboBoxes;

import java.sql.SQLException;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;

public class ReservationComboBox extends RefreshableComboBox<Reservation>
{
	private Problem problem;

    public ReservationComboBox(Problem problem) throws SQLException {
        this.problem = problem;

        setFloatingText("Réservations");
        setFloatMode(FloatMode.INLINE);
        // TODO afficher que les emplacements dispos sur les dates de la résa
        getItems().addAll(Database.getInstance().getReservationDao().queryForAll());
        setMinWidth(180);
        setAnimated(false);
        refresh();
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            // TODO Check si l'emplacmeent est disponibles sur les dates de la résa
            problem.setReservation(newValue);
        });
    }
  
    public void refresh() {
    	if(problem.getReservation() != null)
    		selectItem(problem.getReservation());
    }

    public void addListener(ChangeListener<? super Reservation> listener) {
        valueProperty().addListener(listener);
    }
}
