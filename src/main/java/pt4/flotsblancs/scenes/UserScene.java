package pt4.flotsblancs.scenes;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.function.UnaryOperator;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.converter.IntegerStringConverter;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.components.ConfirmButton;
import pt4.flotsblancs.scenes.components.HBoxSpacer;
import pt4.flotsblancs.scenes.components.PromptedTextField;
import pt4.flotsblancs.scenes.components.VBoxSpacer;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.ToastType;

public class UserScene extends ItemScene<User> {

    private Label title;
    private Label roleLabel;
    private User stagiaire;

    private Spinner<Integer> hoursSpinner;
    private MFXCheckbox isAdmin;

    private PromptedTextField firstName;
    private PromptedTextField lastName;
    private PromptedTextField login;

    private PromptedTextField newPwdTxtField;

    private MFXButton cancelResetBtn;
    private MFXButton validateResetBtn;

    private MFXButton resetBtn;
    private MFXButton saveButton;

    private ChangeListener<? super Object> changeListener = (obs, oldVal, newVal) -> {
        if (oldVal == null || newVal == null || oldVal == newVal)
            return;
        saveButton.setDisable(false);
    };

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
        if (this.saveButton != null)
            if (!saveButton.isDisabled())
                updateDatabase(stagiaire);
    }

    @Override
    protected String addButtonText() {
        return "Ajouter un utilisateur";
    }

    @Override
    protected void onAddButtonClicked() {
        try {
            Router.goToScreen(Routes.USERS, new User("Jean"));
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
        }
    }

    @Override
    protected Region createContainer(User item) {
        this.stagiaire = item;
        VBox container = new VBox(10);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(50));

        container.getChildren().add(createHeader());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createMiddleSlot());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createFooter());

        return container;
    }

    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        String role = stagiaire.isAdmin() ? "Administrateur" : "Personnel";

        VBox nameAndRole = new VBox();

        roleLabel = new Label(role);

        title = new Label(stagiaire.getDisplayName());
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        nameAndRole.getChildren().addAll(title, roleLabel);

        var stagiaireId = new Label("Personnel #" + stagiaire.getId());
        stagiaireId.setFont(new Font(17));
        stagiaireId.setTextFill(Color.GREY);

        container.setLeft(nameAndRole);
        container.setRight(stagiaireId);
        return container;
    }

    private HBox passwordResetButtons() {
        HBox container = new HBox(8);

        cancelResetBtn = new MFXButton("Annuler");
        cancelResetBtn.getStyleClass().add("action-button-outlined");
        cancelResetBtn.setVisible(false);
        cancelResetBtn.setOnAction(e -> {
            newPwdTxtField.setVisible(false);
            resetBtn.setVisible(true);
            cancelResetBtn.setVisible(false);
            validateResetBtn.setVisible(false);
        });

        validateResetBtn = new MFXButton("Valider");
        validateResetBtn.getStyleClass().add("action-button");
        validateResetBtn.setVisible(false);
        validateResetBtn.setOnAction(e -> handlePasswordReset());

        container.getChildren().addAll(cancelResetBtn, new HBoxSpacer(), validateResetBtn);
        return container;
    }

    private VBox createPersonnalInfosContainer() {
        VBox container = new VBox(8);
        container.setAlignment(Pos.CENTER_LEFT);

        firstName = new PromptedTextField(stagiaire.getFirstName(), "Prénom");
        firstName.textProperty().addListener(changeListener);

        lastName = new PromptedTextField(stagiaire.getName(), "Nom");
        lastName.textProperty().addListener(changeListener);

        login = new PromptedTextField(stagiaire.getLogin(), "Identifiant");
        login.textProperty().addListener(changeListener);

        container.getChildren().addAll(firstName, lastName, login);

        newPwdTxtField = new PromptedTextField("", "Nouveau mot de passe");
        newPwdTxtField.setVisible(false);

        container.getChildren().addAll(newPwdTxtField, passwordResetButtons());

        return container;
    }

    private VBox createHoursContainer() {
        var container = new VBox(8);
        var heuresLabel = new Label("Heures hebdomadaires");
        SpinnerValueFactory<Integer> vFact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                7 * 24, stagiaire.getWeeklyHours());
        hoursSpinner = new Spinner<Integer>();
        hoursSpinner.setEditable(true);
        hoursSpinner.setValueFactory(vFact);
        hoursSpinner.valueProperty().addListener(changeListener);

        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };

        TextFormatter<Integer> priceFormatter = new TextFormatter<Integer>(
        new IntegerStringConverter(), stagiaire.getWeeklyHours(), filter);

        hoursSpinner.getEditor().setTextFormatter(priceFormatter);
        vFact.setValue(stagiaire.getWeeklyHours());

        container.getChildren().addAll(heuresLabel, hoursSpinner);
        return container;
    }

    private HBox createAdminSelectionContainer() {
        var container = new HBox(8);
        container.setAlignment(Pos.CENTER_LEFT);
        var adminLabel = new Label("Administrateur");

        isAdmin = new MFXCheckbox();
        isAdmin.setSelected(stagiaire.isAdmin());
        isAdmin.setDisable(User.getConnected().equals(stagiaire));
        isAdmin.selectedProperty().addListener(changeListener);

        container.getChildren().addAll(isAdmin, adminLabel);
        return container;
    }

    private HBox createMiddleSlot() {
        HBox container = new HBox();

        var rightContainer = new VBox(30);

        rightContainer.getChildren().add(createHoursContainer());
        rightContainer.getChildren().add(createAdminSelectionContainer());

        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(createPersonnalInfosContainer());
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(rightContainer);
        container.getChildren().add(new HBoxSpacer());
        return container;
    }

    private HBox createFooter() {
        HBox container = new HBox(8);
        container.setAlignment(Pos.CENTER_RIGHT);

        resetBtn = new MFXButton("Réinitialiser le mot de passe");
        resetBtn.getStyleClass().add("action-button-outlined");
        resetBtn.setOnAction(e -> {
            newPwdTxtField.setVisible(true);
            newPwdTxtField.setText("");
            resetBtn.setVisible(false);
            validateResetBtn.setVisible(true);
            cancelResetBtn.setVisible(true);
        });

        ConfirmButton deleteUserBtn = new ConfirmButton("Supprimer");
        deleteUserBtn.getStyleClass().add("action-button-outlined");
        deleteUserBtn.setDisable(User.getConnected().equals(stagiaire));
        deleteUserBtn.setOnConfirmedAction((e) -> {
            try {
                Database.getInstance().getUsersDao().delete(stagiaire);
                super.onItemDelete(stagiaire);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        saveButton = new MFXButton("Sauvegarder");
        saveButton.getStyleClass().add("action-button");
        saveButton.setDisable(true);
        saveButton.setOnAction(e -> {
            updateDatabase(stagiaire);
            saveButton.setDisable(true);
        });

        container.getChildren().addAll(resetBtn, deleteUserBtn, saveButton);
        return container;
    }

    private void handlePasswordReset() {
        String newPwd = newPwdTxtField.getTextSafely().trim();
        if (newPwd.isEmpty())
            return;
        newPwdTxtField.setVisible(false);
        resetBtn.setVisible(true);
        cancelResetBtn.setVisible(false);
        validateResetBtn.setVisible(false);
        try {
            stagiaire.setPassword(User.sha256(newPwd));
            Database.getInstance().getUsersDao().update(stagiaire);
            Router.showToast(ToastType.SUCCESS, "Mot de passe mis à jour");
        } catch (SQLException e) {
            ExceptionHandler.updateIssue(e);
        }
    }

    private void updateDatabase(User stagiaire) {
        if (stagiaire == null)
            return;
        try {
            if (!stagiaire.getFirstName().equals(firstName.getTextSafely().trim()))
                stagiaire.setFirstName(firstName.getTextSafely().trim());
            if (!stagiaire.getName().equals(lastName.getTextSafely().trim()))
                stagiaire.setName(lastName.getTextSafely().trim());
            if (!stagiaire.getLogin().equals(login.getTextSafely().trim()))
                stagiaire.setLogin(login.getTextSafely().trim());
            if (!stagiaire.getWeeklyHours().equals(hoursSpinner.getValue()))
                stagiaire.setWeeklyHours(hoursSpinner.getValue());
            if (!stagiaire.isAdmin() == isAdmin.selectedProperty().get())
                stagiaire.setAdmin(isAdmin.selectedProperty().get());
            Database.getInstance().getUsersDao().update(stagiaire);
            Router.showToast(ToastType.SUCCESS, "Personnel mis à jour");
            updateItemList();
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
        }
    }

    @Override
    protected List<User> queryAll() throws SQLException {
        return Database.getInstance().getUsersDao().queryForAll();
    }
}
