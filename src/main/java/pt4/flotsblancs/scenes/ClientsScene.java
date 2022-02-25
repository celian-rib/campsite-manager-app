package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.scenes.items.ItemScene;

public class ClientsScene extends ItemScene<Client> {

    @Override
    public String getName() {
        return "Réservations";
    }

    @Override
    protected Region createContainer(Client item) {
        var container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        Label test = new Label(item.getDisplayName());
        container.getChildren().addAll(test);
        return container;
    }

    @Override
    protected List<Client> queryAll() throws SQLException {
        return Database.getInstance().getClientsDao().queryForAll();
    }
}
