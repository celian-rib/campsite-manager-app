package pt4.flotsblancs.scenes.components.ComboBoxes;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class EquipmentComboBox extends RefreshableComboBox<Equipment> {

    private final Reservation reservation;
    private final CampGround campground;

    public EquipmentComboBox(Reservation reservation) {
        this.campground = null;
        this.reservation = reservation;

        setFloatingText("Equipements client");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null || newValue == null)
                return;
            try {
                reservation.setEquipments(newValue);
            } catch (ConstraintException e) {
                Router.showToast(ToastType.WARNING, e.getMessage());
            }
        });
    }

    public EquipmentComboBox(CampGround campground) {
        this.campground = campground;
        this.reservation = null;

        setFloatingText("Equipements autorisés");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null || newValue == null)
                return;
            campground.setAllowedEquipments(newValue);
        });
    }

    public void refresh() {
        getItems().clear();
        if (this.campground != null) {
            getItems().addAll(Equipment.values());
            selectItem(campground.getAllowedEquipments());
        } else if (this.reservation != null) {
            getItems().addAll(reservation.getCampground().getCompatiblesEquipments());
            selectItem(reservation.getEquipments());
        }
    }
}
