package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import pt4.flotsblancs.components.ReservationCard;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.scenes.items.ItemScene;

public class ClientsScene extends ItemScene<Client> {

    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;
    private Client client;

    @Override
    public String getName() {
        return "Client";
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
        container.getChildren().add(createPreference());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createReservationAndProblems());
        container.getChildren().add(new VBoxSpacer());

        return container;
    }

    @Override
    protected List<Client> queryAll() throws SQLException {
        return Database.getInstance().getClientsDao().queryForAll();
    }

    // mfxTextField BRA BABBLING

    private HBox createName() {
        var textFieldName = new MFXTextField(client.getName());
        var textFieldFName = new MFXTextField(client.getFirstName());
        HBox nameContainer = new HBox(CONTENT_SPACING);
        nameContainer.getChildren().add(textFieldFName);
        nameContainer.getChildren().add(textFieldName);
        return nameContainer;
    }

    private HBox createAdrPhone() {
        var textFieldAdr = new MFXTextField(client.getAddresse());
        var textFieldPhone = new MFXTextField(client.getPhone());
        HBox AdrPhoneContainer = new HBox(CONTENT_SPACING);
        AdrPhoneContainer.getChildren().add(textFieldAdr);
        AdrPhoneContainer.getChildren().add(textFieldPhone);
        return AdrPhoneContainer;
    }

    private HBox createPreference() {
        var textPreference = new MFXTextField(client.getPreferences());
        HBox AdrPreference = new HBox(CONTENT_SPACING);
        AdrPreference.getChildren().add(textPreference);
        return AdrPreference;
    }

    private HBox createReservationAndProblems() {
        HBox AdrReservationAndProblems = new HBox(CONTENT_SPACING);
        int size = client.getReservations().size();
        
        if (size != 0) {
            ReservationCard reservation = new ReservationCard(
                    (Reservation) client.getReservations().toArray()[size - 1], 220); // on met la dernière réservation
            AdrReservationAndProblems.getChildren().add(reservation);
        } else {
            Label reservationLabel = new Label("Ce client n'a pas de reservation");
            AdrReservationAndProblems.getChildren().add(reservationLabel);
        }

        return AdrReservationAndProblems;
    }

}
