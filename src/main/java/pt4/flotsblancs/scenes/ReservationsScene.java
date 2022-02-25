package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.scenes.items.ItemScene;

public class ReservationsScene extends ItemScene<Reservation> {

    @Override
    public String getName() {
        return "Réservations";
    }

    @Override
    protected Region createContainer(Reservation item) {
        var container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(new Label(item.getDisplayName()));
        container.getChildren().addAll(new Label("Client : " + item.getClient().getName()));
        return container;
    }

    @Override
    protected List<Reservation> queryAll() throws SQLException {
        return Database.getInstance().getReservationDao().queryForAll();
    }
}
