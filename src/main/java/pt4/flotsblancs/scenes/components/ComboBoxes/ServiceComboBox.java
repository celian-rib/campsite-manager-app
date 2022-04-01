package pt4.flotsblancs.scenes.components.ComboBoxes;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Service;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ServiceComboBox extends RefreshableComboBox<Service> {

    private final Reservation reservation;
    private final CampGround campground;

    /**
     * Combo box pour selectionner le service d'une réservation
     */
    public ServiceComboBox(Reservation reservation) {
        this.campground = null;
        this.reservation = reservation;

        setFloatingText("Services demandés");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null || newValue == null)
                return;
            try {
                reservation.setSelectedServices(newValue);
            } catch (ConstraintException e) {
                Router.showToast(ToastType.WARNING, e.getMessage());
            }
        });
    }

    /**
     * Combo box pour selectionner le service d'un emplacement
     */
    public ServiceComboBox(CampGround campground) {
        this.campground = campground;
        this.reservation = null;

        setFloatingText("Services fournis");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null || newValue == null)
                return;
            campground.setProvidedServices(newValue);
        });
    }

    @Override
    public void refresh() {
        getItems().clear();
        if (this.campground != null) {
            getItems().addAll(Service.values());
            selectItem(campground.getProvidedServices());
        } else if (this.reservation != null) {
            getItems().addAll(reservation.getCampground().getCompatiblesServices());
            selectItem(reservation.getSelectedServices());
        }
    }
}
