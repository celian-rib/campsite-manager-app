package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.enums.FloatMode;

import java.util.function.Consumer;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Equipment;
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
            if (oldValue == null || newValue == null)
                return;
            reservation.setSelectedServices(newValue);
        });

        // setOnCommit(new Consumer<String>() {
        //     @Override
        //     public void accept(String t) {
        //         System.out.println("COMMITED TO " + t);                
        //     };
        // });
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
            if (oldValue == null || newValue == null)
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
            System.out.println("UPDATE");
            // On peut choisir parmis les services donnés par l'emplacement
            System.out.println(reservation.getCampground().getCompatiblesServices());
            getItems().addAll(reservation.getCampground().getCompatiblesServices());
            selectItem(reservation.getSelectedServices());
        }
    }

    public void addListener(ChangeListener<? super Service> listener) {
        // TODO Call ce listener que si l'update vient de l'utilisateur et non d'un
        // effet de bord
        var wrapper = new ChangeListener<>() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println(isHover());
                if (isPressed()) {
                    listener.changed(observable, (Service) oldValue, (Service) newValue);
                }
            };
        };

        valueProperty().addListener(wrapper);
    }
}
