package pt4.flotsblancs.components.ComboBoxes;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.model.Reservation;

public class PersonCountComboBox extends MFXComboBox<Integer> {
    public PersonCountComboBox(Reservation reservation) {
        setFloatingText("Nombre de personnes");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        setMinWidth(180);
        setAnimated(false);
        selectItem(reservation.getNbPersons());
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            reservation.setNbPersons(newValue.intValue());
        });
    }

    public void addListener(ChangeListener<? super Integer> listener) {
        valueProperty().addListener(listener);
    }
}
