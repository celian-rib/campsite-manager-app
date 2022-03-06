package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Service;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ServiceComboBox extends MFXComboBox<Service> {

    private final Reservation reservation;
    private final CampGround campground;

    public ServiceComboBox(Reservation reservation) {
        this.campground = null;
        this.reservation = reservation;

        setFloatingText("Services demandés");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            reservation.setSelectedServices(newValue);
        });
    }

    public ServiceComboBox(CampGround campground) {
        this.campground = campground;
        this.reservation = null;

        setFloatingText("Services fournis");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            campground.setProvidedServices(newValue);
        });
    }

    public void refresh() {
        getItems().clear();
        if (this.campground != null) {
            getItems().addAll(Service.values());
            selectItem(campground.getProvidedServices());
        } else if (this.reservation != null) {
            getItems().addAll(reservation.getCampground().getProvidedServices().getCompatibles());
            try {
                selectItem(reservation.getSelectedServices());
            } catch (Exception e) {
                System.out.println(
                        "Reservation service set to None -> camground not allowing this service");
                Router.showToast(ToastType.ERROR,
                        "Les services demandés par la réservation ne sont pas ou plus disponibles pour l'emplacement");
                reservation.setSelectedServices(Service.NONE);
                selectItem(Service.NONE);
            }
        }
    }

    public void addListener(ChangeListener<? super Service> listener) {
        valueProperty().addListener(listener);
    }
}
