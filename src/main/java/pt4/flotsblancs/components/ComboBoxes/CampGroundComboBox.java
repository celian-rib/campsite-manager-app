package pt4.flotsblancs.components.ComboBoxes;

import java.sql.SQLException;

import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class CampGroundComboBox extends RefreshableComboBox<CampGround> {

    private final Reservation reservation;
    private final Problem problem;

    public CampGroundComboBox(Reservation reservation) throws SQLException {
        this.reservation = reservation;
        this.problem = null;

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
            try {
                reservation.setCampground(newValue);
            } catch (ConstraintException e) {
                Router.showToast(ToastType.WARNING, e.getMessage());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public CampGroundComboBox(Problem problem) throws SQLException {
        this.problem = problem;
        this.reservation = null;

        setFloatingText("Emplacement");
        setFloatMode(FloatMode.INLINE);
        // TODO afficher que les emplacements dispos sur les dates de la résa
        getItems().addAll(Database.getInstance().getCampgroundDao().queryForAll());
        setMinWidth(180);
        setAnimated(false);
        // refresh();
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            // TODO Check si l'emplacmeent est disponibles sur les dates de la résa
            problem.setCampground(newValue);
        });
    }

    public void addListener(ChangeListener<? super CampGround> listener) {
        valueProperty().addListener(listener);
    }

    public void refresh() {
        if (reservation != null)
            selectItem(reservation.getCampground());
        else
            selectItem(problem.getCampground());
    }

}
