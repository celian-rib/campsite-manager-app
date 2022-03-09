package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import pt4.flotsblancs.components.HBoxSpacer;
import pt4.flotsblancs.components.ProblemsListCard;
import pt4.flotsblancs.components.ReservationCard;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.breakpoints.BreakPointManager;
import pt4.flotsblancs.scenes.breakpoints.HBreakPoint;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ClientsScene extends ItemScene<Client> {

    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;

    private Client client;

    private Label title;

    private MFXTextField name;
    private MFXTextField firstName;
    private MFXTextField adresse;
    private MFXTextField phone;
    private MFXTextField preferences;

    private MFXButton saveButton;
    private MFXButton addReservationButton;

    private ChangeListener<? super Object> changeListener = (obs, oldVal, newVal) -> {
        if (oldVal == null || newVal == null || oldVal == newVal)
            return;
        saveButton.setDisable(false);
    };

    @Override
    public String getName() {
        return "Client";
    }

    @Override
    protected Region createContainer(Client client) {
        this.client = client;

        var container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        container.getChildren().add(createHeader());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createTopSLot());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createBottomSlot());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createActionsButtonsSlot());

        refreshPage();

        return container;
    }

    private void refreshPage() {
        title.setText(client.getDisplayName());
        addReservationButton.setDisable(client.getOpenReservation() != null);
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        title = new Label();
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        var clientId = new Label("Client #" + client.getId());
        clientId.setFont(new Font(13));
        clientId.setTextFill(Color.DARKGREY);

        container.setLeft(title);
        container.setRight(clientId);
        return container;
    }

    private HBox createTopSLot() {
        HBox container = new HBox();
        container.setPadding(new Insets(INNER_PADDING));
        container.setAlignment(Pos.TOP_CENTER);

        container.getChildren().add(createCardsContainer());
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(createNameFirstNameContainer());
        return container;
    }

    private BorderPane createBottomSlot() {
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(INNER_PADDING));

        container.setLeft(createInfosContainer());
        container.setRight(new ProblemsListCard(client));
        return container;
    }

    private MFXTextField createTextField(String text, String prompt) {
        var textField = new MFXTextField();
        textField.setText(text);
        textField.setFloatingText(prompt);
        textField.setFloatMode(FloatMode.INLINE);
        textField.setMinWidth(180);
        return textField;
    }

    private VBox createCardsContainer() {
        VBox container = new VBox(CONTENT_SPACING);
        container.setAlignment(Pos.TOP_LEFT);

        var card = new ReservationCard(client.getOpenReservation(), 250);

        var clientSince = new Label("Client depuis :    TODO");
        clientSince.setFont(new Font(15));
        clientSince.setTextFill(Color.GRAY);
        
        var nbReservations = new Label("Nombre de réservations : " + client.getReservations().size());
        nbReservations.setFont(new Font(15));
        nbReservations.setTextFill(Color.GRAY);
        container.getChildren().addAll(card, clientSince, nbReservations);
        return container;
    }

    private VBox createNameFirstNameContainer() {
        VBox container = new VBox(CONTENT_SPACING);
        container.setAlignment(Pos.CENTER);

        name = createTextField(client.getName(), "Nom");
        name.textProperty().addListener(changeListener);
        firstName = createTextField(client.getFirstName(), "Prénom");
        firstName.textProperty().addListener(changeListener);


        container.getChildren().addAll(name, firstName);
        return container;
    }

    private VBox createInfosContainer() {
        VBox container = new VBox(CONTENT_SPACING);
        container.setAlignment(Pos.BASELINE_LEFT);

        phone = createTextField(client.getPhone(), "Téléphone");

        boolean isReduced = isReducedSize(BreakPointManager.getCurrentHorizontalBreakPoint());

        adresse = createTextField(client.getAddresse(), "Adresse");
        adresse.setMinWidth(isReduced ? 180 : 350);
        adresse.textProperty().addListener(changeListener);

        preferences = createTextField(client.getPreferences(), "Préférences");
        preferences.setMinWidth(isReduced ? 180 : 350);
        preferences.textProperty().addListener(changeListener);

        container.getChildren().addAll(phone, adresse, preferences);
        return container;
    }

    private HBox createActionsButtonsSlot() {
        var container = new HBox(10);

        saveButton = new MFXButton("Sauvegarder");
        saveButton.getStyleClass().add("action-button");
        saveButton.setDisable(true);

        addReservationButton = new MFXButton("Créer une réservation");
        addReservationButton.getStyleClass().add("action-button");

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(saveButton, addReservationButton);

        saveButton.setOnAction(e -> {
            updateDatabase(client);
            saveButton.setDisable(true);
        });

        addReservationButton.setOnAction(e -> {
            try {
                Router.goToScreen(Routes.RESERVATIONS, new Reservation(client));
                Router.showToast(ToastType.SUCCESS, "Réservation ajoutée");
            } catch (SQLException e1) {
                System.err.println(e);
                Router.showToast(ToastType.ERROR, "Erreur durant l'ajout de la réservation");
                Router.goToScreen(Routes.CONN_FALLBACK);
            }
        });

        return container;
    }

    private void updateDatabase(Client client) {
        if (client == null)
            return;
        try {
            client.setFirstName(firstName.getText());
            client.setName(name.getText());
            client.setAddresse(adresse.getText());
            client.setPhone(phone.getText());
            client.setPreferences(preferences.getText());
            Database.getInstance().getClientsDao().update(client);
            Router.showToast(ToastType.SUCCESS, "Client mis à jour");
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
        updateDatabase(client);
    }

    @Override
    protected List<Client> queryAll() throws SQLException {
        return Database.getInstance().getClientsDao().queryForAll();
    }

    private boolean isReducedSize(HBreakPoint currentBp) {
        return currentBp.getWidth() <= HBreakPoint.LARGE.getWidth();
    }

    @Override
    public void onHorizontalBreak(HBreakPoint oldBp, HBreakPoint newBp) {
        super.onHorizontalBreak(oldBp, newBp); // Implémentation de ItemScene

        if (adresse == null || preferences == null)
            return;

        if (isReducedSize(newBp)) {
            adresse.setMinWidth(180);
            preferences.setMinWidth(180);
        } else {
            adresse.setMinWidth(350);
            preferences.setMinWidth(350);
        }
    }
}
