package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Service;

public class ServiceComboBox extends MFXComboBox<Service> {

    public ServiceComboBox(Reservation reservation) {
        setFloatingText("Services demandés");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(Service.values());
        setMinWidth(180);
        setAnimated(false);
        selectItem(reservation.getSelectedServices());
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            reservation.setSelectedServices(newValue);
        });
    }

    public ServiceComboBox(CampGround campground) {
        setFloatingText("Services fournis");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(Service.values());
        setMinWidth(180);
        selectItem(campground.getProvidedServices());
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            campground.setProvidedServices(newValue);
        });
    }

    public void addListener(ChangeListener<? super Service> listener) {
        valueProperty().addListener(listener);
    }
}
