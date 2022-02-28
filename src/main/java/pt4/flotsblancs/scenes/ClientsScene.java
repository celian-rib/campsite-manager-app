package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.scenes.items.ItemScene;

public class ClientsScene extends ItemScene<Client> {

    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;
    private Client client;

    @Override
    public String getName() {
        return "Réservations";
    }

    @Override
    protected Region createContainer(Client client) {
        this.client = client;
        var container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createName());    
        container.getChildren().add(new VBoxSpacer());  
        container.getChildren().add(createAdrPhone());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(new MFXTextField());
        container.getChildren().add(new VBoxSpacer());

        return container;
    }

    @Override
    protected List<Client> queryAll() throws SQLException {
        return Database.getInstance().getClientsDao().queryForAll();
    }

    //mfxTextField BRA BABBLING

    private HBox createName(){
        var textFieldName = new MFXTextField(client.getName());
        var textFieldFName = new MFXTextField(client.getFirstName());
        HBox nameContainer = new HBox(CONTENT_SPACING);
        nameContainer.getChildren().add(textFieldFName);
        nameContainer.getChildren().add(textFieldName);
        return nameContainer;
    }

    private HBox createAdrPhone(){
        var textFieldAdr = new MFXTextField(client.getAddresse());
        var textFieldPhone = new MFXTextField(client.getPhone());
        HBox  AdrPhoneContainer = new HBox(CONTENT_SPACING);
        AdrPhoneContainer.getChildren().add(textFieldAdr);
        AdrPhoneContainer.getChildren().add(textFieldPhone);
        return AdrPhoneContainer;
    }

}
