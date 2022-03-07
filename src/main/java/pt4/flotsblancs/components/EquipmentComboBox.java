package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.enums.FloatMode;
import java.util.ArrayList;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class EquipmentComboBox extends MFXComboBox<Equipment> {

    private final Reservation reservation;
    private final CampGround campground;
    private ArrayList<ChangeListener<? super Equipment>> listeners;

    public EquipmentComboBox(Reservation reservation) {
        listeners = new ArrayList<>();
        this.campground = null;
        this.reservation = reservation;

        setFloatingText("Equipements client");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        getItems().addAll(Equipment.values());
        refresh();
        selectItem(reservation.getEquipments());


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

    public EquipmentComboBox(CampGround campground) {
        listeners = new ArrayList<>();
        this.campground = campground;
        this.reservation = null;

        setFloatingText("Equipements autorisés");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        getItems().addAll(Equipment.values());
        selectItem(campground.getAllowedEquipments());

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            campground.setAllowedEquipments(newValue);
        });
    }

    public void refresh() {
        // getItems().clear();
        // if (this.campground != null) {
        //     getItems().addAll(Equipment.values());
        //     selectItem(campground.getAllowedEquipments());
        // } else if (this.reservation != null) {
        //     getItems().addAll(reservation.getCampground().getAllowedEquipments().getCompatibles());
        //     // TODO AAAAAAAAAAA

        //     // try {
        //     //     selectItem(reservation.getEquipments());
        //     // } catch (Exception e) {
        //     //     System.out.println(
        //     //             "Reservation equipement set to default -> camground not allowing this equipement");
        //     //     Router.showToast(ToastType.ERROR,
        //     //             "Les équipement demandés par la réservation ne correspondent pas ou plus à l'emplacement, ils ont donc été changés");
        //     //     reservation.setEquipments(reservation.getCampground().getAllowedEquipments());
        //     //     selectItem(reservation.getCampground().getAllowedEquipments());
        //     // }
        // }
    }

    public void addListener(ChangeListener<? super Equipment> listener) {
        listeners.add(listener);
        valueProperty().addListener(listener);
    }
}
