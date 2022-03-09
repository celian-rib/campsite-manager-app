package pt4.flotsblancs.components.ComboBoxes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;

public class CampGroundComboBox extends MFXComboBox<CampGround> {

    private final Reservation reservation;

    @SuppressWarnings("unchecked")
	public CampGroundComboBox(Reservation reservation) throws SQLException {
        this.reservation = reservation;

        setFloatingText("Emplacement");
        setFloatMode(FloatMode.INLINE);
        // TODO afficher que les emplacements dispos sur les dates de la résa
        
        getItems().addAll(Database.getInstance().getCampgroundDao().getAvailablesCampgrounds(reservation.getStartDate(),reservation.getEndDate(),reservation.getId()));

        
        //db.executeRaw("SELECT * FROM reservations WHERE start_date >= "+endDate+" OR end_date <= "+startDate);
        
        //On selectionne les reservations où:
        //La date de fin X <= date de fin Y
        // OU QUE  date de début X => date début Y
        
        setMinWidth(180);
        setAnimated(false);
        refresh();
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            // TODO Check si l'emplacmeent est disponibles sur les dates de la résa
            reservation.setCampground(newValue);
        });
    }

    public void refresh() {
        selectItem(reservation.getCampground());
    }

    public void addListener(ChangeListener<? super CampGround> listener) {
        valueProperty().addListener(listener);
    }
}
