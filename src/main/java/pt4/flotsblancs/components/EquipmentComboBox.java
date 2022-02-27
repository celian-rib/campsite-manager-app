package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Equipment;

public class EquipmentComboBox extends MFXComboBox<Equipment> {

    public EquipmentComboBox(Reservation reservation) {
        setFloatingText("Equipements client");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(Equipment.values());
        setMinWidth(180);
        setAnimated(false);
        selectItem(reservation.getEquipments());
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            reservation.setEquipments(newValue);
        });
    }

    public EquipmentComboBox(CampGround campground) {
        setFloatingText("Equipements autorisés");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(Equipment.values());
        setMinWidth(180);
        selectItem(campground.getAllowedEquipments());
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            campground.setAllowedEquipments(newValue);
        });
    }

    public void addListener(ChangeListener<? super Equipment> listener) {
        valueProperty().addListener(listener);
    }
}
