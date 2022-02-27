package pt4.flotsblancs.components;

import java.sql.SQLException;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;

public class CampGroundComboBox extends MFXComboBox<CampGround> {
    public CampGroundComboBox(Reservation reservation) throws SQLException {
        setFloatingText("Emplacement");
        setFloatMode(FloatMode.INLINE);
        // TODO afficher que les emplacements dispos sur les dates de la résa
        getItems().addAll(Database.getInstance().getCampgroundDao().queryForAll());
        setMinWidth(180);
        setAnimated(false);
        selectItem(reservation.getCampground());
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            // TODO Check si l'emplacmeent est disponibles sur les dates de la résa
            reservation.setCampground(newValue);
        });
    }

    public void addListener(ChangeListener<? super CampGround> listener) {
        valueProperty().addListener(listener);
    }
}
