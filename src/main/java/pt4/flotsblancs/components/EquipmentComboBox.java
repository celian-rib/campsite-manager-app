package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class EquipmentComboBox extends MFXComboBox<Equipment> {

    private final Reservation reservation;

    public EquipmentComboBox(Reservation reservation) {
        this.reservation = reservation;
        setup();
        setFloatingText("Equipements client");

        getItems().addAll(Equipment.values());
        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            if (!reservation.getCampground().getAllowedEquipments().isCompatible(newValue)) {
                Router.showToast(ToastType.ERROR,
                        "L'emplacement de cette réservation n'accepte pas ce type d'équipement");
                selectItem(reservation.getCampground().getAllowedEquipments());
                return;
            }
            reservation.setEquipments(newValue);
        });
    }

    public void refresh() {
        if (reservation != null)
            selectItem(reservation.getEquipments());
    }

    public EquipmentComboBox(CampGround campground) {
        this.reservation = null;
        setup();
        setFloatingText("Equipements autorisés");

        getItems().addAll(Equipment.values());
        selectItem(campground.getAllowedEquipments());

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            campground.setAllowedEquipments(newValue);
        });
    }

    private void setup() {
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);
    }

    public void addListener(ChangeListener<? super Equipment> listener) {
        valueProperty().addListener(listener);
    }
}
