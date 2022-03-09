package pt4.flotsblancs.components.ComboBoxes;

import java.sql.SQLException;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;

public class CampGroundComboBox extends MFXComboBox<CampGround> {

    private final Reservation reservation;

    public CampGroundComboBox(Reservation reservation) throws SQLException {
        this.reservation = reservation;

        setFloatingText("Emplacement");
        setFloatMode(FloatMode.INLINE);

        var dao = Database.getInstance().getCampgroundDao();
        var camps = dao.getAvailablesCampgrounds(reservation.getStartDate(), reservation.getEndDate(),
                reservation.getId());
        getItems().addAll(camps);

        setMinWidth(180);
        setAnimated(false);
        refresh();
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            // TODO Check si l'emplacmeent est disponibles sur les dates de la résa
            // Enfaite non faudra pas le faire la (mais faut le faire)
            reservation.setCampground(newValue);
        });
    }

    public void refresh() {
        selectItem(reservation.getCampground());
    }

    public void addListener(ChangeListener<? super CampGround> listener) {
        valueProperty().addListener(listener);
    }
}
