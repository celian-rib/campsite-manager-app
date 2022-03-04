package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ClientsScene extends ItemScene<Client> {

    private Label title;
    private Client client;
    private Label address;
    private Label phone;
    private TextArea preferences;
    private MFXButton submitPreferences;

    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;

    @Override
    public String getName() {
        return "Clients";
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        title = new Label();
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        VBox contactInfosContainer = new VBox();
        address = new Label();
        phone = new Label();
        contactInfosContainer.getChildren().addAll(address, phone);

        container.setLeft(title);
        container.setRight(contactInfosContainer);
        return container;
    }

    /**
     * @return Conteneur avec les preferences, pour le moment
     */
    private HBox createTopSlot() {
        HBox container = new HBox(10);
        container.setPadding(new Insets(INNER_PADDING));
        container.setAlignment(Pos.CENTER_RIGHT);

        VBox problemsBox = new VBox(2);
        problemsBox.setAlignment(Pos.CENTER_LEFT);

        container.getChildren().add(problemsBox);

        preferences = new TextArea();
        submitPreferences = new MFXButton("Valider");
        submitPreferences.setButtonType(ButtonType.RAISED);
        submitPreferences.setBackground(new Background(new BackgroundFill(Color.rgb(51, 59, 97), new CornerRadii(5), Insets.EMPTY)));
        submitPreferences.setTextFill(Color.WHITE);
        createPreferenceListener(submitPreferences);

        problemsBox.getChildren().addAll(preferences, submitPreferences);


        return container;
    }

    @Override
    protected Region createContainer(Client item) {
        this.client = item;
        var container = new VBox(10);
        container.setPadding(new Insets(50));
        
        // pull the clien't data from the database
        try {
            Database.getInstance().getClientsDao().refresh(client);
        } catch (Exception e) {
            log("[ERROR] Could not refresh " + client.getDisplayName() + "'s data.");
        }
        
        BorderPane header = createHeader();
        HBox topSlot = createTopSlot();
        title.setText(client.getDisplayName());
        address.setText(client.getAddresse());
        phone.setText(client.getPhone());
        preferences.setText(client.getPreferences());
        
        
        container.getChildren().addAll(header, topSlot);
        return container;
    }

    @Override
    protected List<Client> queryAll() throws SQLException {
        return Database.getInstance().getClientsDao().queryForAll();
    }

    /**
     * Permet de logger les actions de ClientsScene pour le débug
     * 
     * @param message : Message de débug
     */
    private static void log(String message) {
        System.out.println("[ClientsScene] " + message);
    }

    private void refreshDatabase() {
        try {
            Database.getInstance().getClientsDao().update(client);
            Router.showToast(ToastType.SUCCESS, "Préférences mises à jour");
        } catch (SQLRecoverableException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de connexion");
            Router.goToScreen(Routes.CONN_FALLBACK);
        } catch (SQLException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de chargement des données");
            Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
            Router.goToScreen(Routes.HOME);
        }
    }

    /**
     * Listener du bouton des preferences
     * 
     * Lance la mise à jour de l'interface et de la BD si la valeur change
     * 
     * @param submitBtn
     */
    private void createPreferenceListener(MFXButton submitBtn) {
        submitBtn.setOnAction(event -> {
            if (client.getPreferences() != preferences.getText()) {
                client.setPreferences(preferences.getText());
                refreshDatabase();
            }
        });
    }
}
