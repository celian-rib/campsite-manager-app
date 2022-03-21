package pt4.flotsblancs.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.components.ConfirmButton;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class UserScene extends ItemScene<User> {

    private Label title;
    private Label roleLabel;
    private User stagiaire;
    private MFXTextField firstName;
    private MFXTextField lastName;
    private MFXTextField usernameTxtFld;
    private Spinner<Integer> heures;
    private MFXCheckbox isAdmin;
    private MFXButton resetBtn;
    private HBox resetLayout;
    private VBox credentialsBox;
    private MFXTextField newPwdTxtField;

    @Override
    public String getName() {
        return "Personnels";
    }

    @Override
    public void onUnfocus() {
        onContainerUnfocus();
    }

    @Override
    public void onContainerUnfocus() {
        updateDatabase(stagiaire);
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        VBox nameAndRole = new VBox();

        roleLabel = new Label(stagiaire.isAdmin() ? "Administrateur" : "Personnel");

        title = new Label(stagiaire.getDisplayName());
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        nameAndRole.getChildren().addAll(title, roleLabel);



        var stagiaireId = new Label("#" + stagiaire.getId());
        stagiaireId.setFont(new Font(13));
        stagiaireId.setTextFill(Color.DARKGREY);

        container.setLeft(nameAndRole);
        container.setRight(stagiaireId);
        return container;
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createTopSlot() {
        BorderPane container = new BorderPane();


        var namesLabel = new Label("Identité");

        firstName = new MFXTextField(stagiaire.getFirstName());
        lastName = new MFXTextField(stagiaire.getName());

        var names = new HBox(4, firstName, lastName);

        VBox identity = new VBox(4, namesLabel, names);


        var credentialsLabel = new Label("Identifiants");
        usernameTxtFld = new MFXTextField(stagiaire.getLogin());
        resetBtn = new MFXButton("Réinitialiser le mot de passe.");
        resetBtn.getStyleClass().add("action-button-outlined");
        resetBtn.setOnAction(e -> {
            showResetPwdLayout();
        });
        credentialsBox = new VBox(4, credentialsLabel, usernameTxtFld, resetBtn);
        credentialsBox.setAlignment(Pos.CENTER_RIGHT);

        container.setLeft(identity);
        container.setRight(credentialsBox);
        return container;
    }

    private void showResetPwdLayout() {
        newPwdTxtField = new MFXTextField();
        MFXButton validateBtn = new MFXButton("Valider");
        MFXButton cancelBtn = new MFXButton("Annuler");
        validateBtn.setOnAction(e -> handleReset(true));
        cancelBtn.setOnAction(e -> handleReset(false));
        validateBtn.getStyleClass().add("action-button");
        cancelBtn.getStyleClass().add("action-button-outlined");
        resetLayout = new HBox(5, newPwdTxtField, validateBtn, cancelBtn);
        credentialsBox.getChildren().remove(resetBtn);
        credentialsBox.getChildren().add(resetLayout);
    }

    private void handleReset(Boolean confirmed) {
        String newPwd = newPwdTxtField.getText().trim();
        var shown = credentialsBox.getChildren();
        shown.remove(resetLayout);
        shown.add(resetBtn);
        System.out.println(newPwd);
        if (confirmed && !newPwd.isEmpty()) {
            stagiaire.setPassword(User.sha256(newPwd));
            try {
                Database.getInstance().getUsersDao().update(stagiaire);
            } catch (SQLException e) {
                // TODO logging correct
                e.printStackTrace();
            }
        }
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createMiddleSlot() {
        BorderPane container = new BorderPane();


        var heuresLabel = new Label("Heures hebdomadaires");
        SpinnerValueFactory<Integer> vFact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                7 * 24, stagiaire.getWeeklyHours());
        heures = new Spinner<Integer>();
        heures.setEditable(true);
        heures.setValueFactory(vFact);

        var adminLabel = new Label("Admin ?");

        isAdmin = new MFXCheckbox();
        isAdmin.setSelected(stagiaire.isAdmin());
        isAdmin.setDisable(User.getConnected().equals(stagiaire));



        VBox middle = new VBox(adminLabel, isAdmin);


        container.setCenter(middle);
        container.setLeft(heuresLabel);
        container.setRight(heures);
        return container;
    }


    /**
     * @return Footer de la page (Bouton suppression)
     */
    private BorderPane createFooter() {
        BorderPane container = new BorderPane();


        ConfirmButton deleteUserBtn = new ConfirmButton("Supprimer");
        deleteUserBtn.setDisable(User.getConnected().equals(stagiaire));
        deleteUserBtn.setOnConfirmedAction((e) -> {
            try {
                Database.getInstance().getUsersDao().delete(stagiaire);
                super.onItemDelete(stagiaire);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        
        container.setRight(deleteUserBtn);
        return container;
    }



    @Override
    protected Region createContainer(User item) {
        this.stagiaire = item;
        VBox container = new VBox(10);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(50));



        container.getChildren().addAll(createHeader(), createTopSlot(), createMiddleSlot(), createFooter());
        return container;
    }

    private void updateDatabase(User stagiaire) {
        if (stagiaire == null)
            return;
        try {
            if (!stagiaire.getFirstName().equals(firstName.getText().trim())) {

                stagiaire.setFirstName(firstName.getText().trim());
            }
            if (!stagiaire.getName().equals(lastName.getText().trim())) {

                stagiaire.setName(lastName.getText().trim());
            }
            if (!stagiaire.getLogin().equals(usernameTxtFld.getText().trim())) {

                stagiaire.setLogin(usernameTxtFld.getText().trim());
            }
            if (!stagiaire.getWeeklyHours().equals(heures.getValue())) {

                stagiaire.setWeeklyHours(heures.getValue());
            }
            if (!stagiaire.isAdmin() == isAdmin.selectedProperty().get()) {

                stagiaire.setAdmin(isAdmin.selectedProperty().get());
            }
            Database.getInstance().getUsersDao().update(stagiaire);
            Router.showToast(ToastType.SUCCESS, "Stagiaire mis à jour");
        } catch (SQLRecoverableException e) {
            e.printStackTrace();
            Router.showToast(ToastType.ERROR, "Erreur de connexion");
            Router.goToScreen(Routes.CONN_FALLBACK);
        } catch (SQLException e) {
            e.printStackTrace();
            Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
            Router.goToScreen(Routes.HOME);
        }
    }

    @Override
    protected List<User> queryAll() throws SQLException {
        return Database.getInstance().getUsersDao().queryForAll();
    }

}