package pt4.flotsblancs.scenes.items.containers;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.scenes.items.ItemContainer;

public class ClientItemContainer extends VBox implements ItemContainer<Client> {

    private Client client;

    public ClientItemContainer() {
        setAlignment(Pos.CENTER);
        refresh();
    }

    @Override
    public void changeItem(Client newItem) {
        client = newItem;
        refresh();
    }

    private void refresh() {
        this.getChildren().clear();

        if (client == null) {
            Label noClientLabel = new Label("Aucun client sélctionné");
            getChildren().addAll(noClientLabel);
            return;
        }

        Label name = new Label("Nom : " + client.getName());
        Label firstName = new Label("Prénom : " + client.getFirstName());

        this.getChildren().addAll(name, firstName);
    }
}
