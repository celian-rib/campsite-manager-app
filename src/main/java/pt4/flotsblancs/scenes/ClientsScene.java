package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import pt4.flotsblancs.components.ProblemsListCard;
import pt4.flotsblancs.components.ReservationCard;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ClientsScene extends ItemScene<Client> {

    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;
    private Client client;
    private MFXTextField textFieldName;
    private MFXTextField textFieldFName;
    private MFXTextField textFieldAdr;
    private MFXTextField textFieldPhone;
    private MFXTextField textPreference;
    private MFXButton updateButton;
    private MFXButton addReservationButton;

    @Override
    public String getName() {
        return "Client";
    }

    @Override
    protected Region createContainer(Client client) {
        this.client = client;
        var container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(INNER_PADDING));

        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createName());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createAdrPhone());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createPreference());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createReservationAndProblems());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createActionsButtonsSlot());
        container.getChildren().add(new VBoxSpacer());
        return container;
    }

    @Override
    protected List<Client> queryAll() throws SQLException {
        return Database.getInstance().getClientsDao().queryForAll();
    }

    private HBox createName() {
        textFieldName = new MFXTextField(client.getName());
        textFieldFName = new MFXTextField(client.getFirstName());
        HBox nameContainer = new HBox(CONTENT_SPACING);
        nameContainer.setAlignment(Pos.CENTER);
        nameContainer.getChildren().add(textFieldFName);
        nameContainer.getChildren().add(textFieldName);
        return nameContainer;
    }

    private HBox createAdrPhone() {
        textFieldAdr = new MFXTextField(client.getAddresse());
        textFieldPhone = new MFXTextField(client.getPhone());
        HBox AdrPhoneContainer = new HBox(CONTENT_SPACING);
        AdrPhoneContainer.setAlignment(Pos.CENTER);
        AdrPhoneContainer.getChildren().add(textFieldAdr);
        AdrPhoneContainer.getChildren().add(textFieldPhone);
        return AdrPhoneContainer;
    }

    private HBox createPreference() {
        textPreference = new MFXTextField(client.getPreferences());
        HBox AdrPreference = new HBox(CONTENT_SPACING);
        AdrPreference.setAlignment(Pos.CENTER);
        AdrPreference.getChildren().add(textPreference);
        return AdrPreference;
    }

    private HBox createReservationAndProblems() {
        HBox AdrReservationAndProblems = new HBox(CONTENT_SPACING);
        AdrReservationAndProblems.setAlignment(Pos.CENTER);
        int size = client.getReservations().size();

        if (size != 0) {

            ReservationCard reservationCard = new ReservationCard(
                    (Reservation) client.getReservations().toArray()[size - 1], 220); // on met la dernière réservation
            AdrReservationAndProblems.getChildren().add(reservationCard);

            int nbr_prblm = reservationCard.getReservation().getProblems().size();

            if (nbr_prblm != 0) {
                ProblemsListCard problemsContainer = new ProblemsListCard(
                        reservationCard.getReservation().getProblems());
                AdrReservationAndProblems.getChildren().add(problemsContainer);
            } else {
                Label problemLabel = new Label("il n'y a aucun problème !");
                AdrReservationAndProblems.getChildren().add(problemLabel);
            }

        } else {
            Label reservationLabel = new Label("Ce client n'a pas de reservation");
            AdrReservationAndProblems.getChildren().add(reservationLabel);
            Label problemLabel = new Label("il n'y a aucun problème !");
            AdrReservationAndProblems.getChildren().add(problemLabel);
        }
        return AdrReservationAndProblems;
    }

    private void updateDatabase(Client client) {
        try {
            client.setFirstName(textFieldFName.getText());
            client.setName(textFieldName.getText());
            client.setAddresse(textFieldAdr.getText());
            client.setPhone(textFieldPhone.getText());
            client.setPreferences(textPreference.getText());
            Database.getInstance().getClientsDao().update(client);
            Router.showToast(ToastType.SUCCESS, "Client mis à jour");
            User.getConnected().addlog(LogType.MODIFY, "Mise à jour du client "+textFieldFName+" "+textFieldName);
        } catch (SQLRecoverableException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de connexion");
            Router.goToScreen(Routes.CONN_FALLBACK);
        } catch (SQLException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
            Router.goToScreen(Routes.HOME);
        }
    }

    @Override
    public void onUnfocus() {
        // TODO remplacer par onContainerUnfocus
    }

    private HBox createActionsButtonsSlot() {
        var container = new HBox(10);

        updateButton = new MFXButton("Mettre à jour");
        updateButton.getStyleClass().add("action-button");
        
        // TODO afficher que si le client n'a pas de réservation en cours
        addReservationButton = new MFXButton("Créer une réservation");
        addReservationButton.getStyleClass().add("action-button");

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(updateButton, addReservationButton);

        updateButton.setOnAction(e -> {
            updateDatabase(client);
        });

        addReservationButton.setOnAction(e -> {
           try {
                Router.goToScreen(Routes.RESERVATIONS, new Reservation(client));
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        return container;
    }

}
