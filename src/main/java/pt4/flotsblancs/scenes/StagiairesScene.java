package pt4.flotsblancs.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.sql.SQLException;
import java.util.List;



import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.scenes.items.ItemScene;

public class StagiairesScene extends ItemScene<User> {

    private Label title;
    private Label roleLabel;
    private User stagiaire;
    private MFXTextField firstName;
    private MFXTextField lastName;

    @Override
    public String getName() {
        return "Stagiaires";
    }

    @Override
    public boolean showNavBar() {
        return true;
    }

    @Override
    public void onUnfocus() {
        onContainerUnfocus();
    }

    @Override
    public void onContainerUnfocus() {
        //refreshDatabase();
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        VBox nameAndRole = new VBox();

        roleLabel = new Label(stagiaire.isAdmin() ? "Administrateur" : "Stagiaire");

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
        MFXTextField usernameTxtFld = new MFXTextField(stagiaire.getLogin());
        MFXButton resetBtn = new MFXButton("Reset password.");
        resetBtn.getStyleClass().add("action-button");
        VBox credentials = new VBox(4, credentialsLabel, usernameTxtFld, resetBtn);
        credentials.setAlignment(Pos.CENTER_RIGHT);

        container.setLeft(identity);
        container.setRight(credentials);
        return container;
    }

     /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createMiddleSlot() {
        BorderPane container = new BorderPane();

        
        var heuresLabel = new Label("Heures hebdomadaires");
        SpinnerValueFactory<Integer> vFact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 7*24, stagiaire.getWeeklyHours());
        Spinner<Integer> heures = new Spinner<Integer>();
        heures.setEditable(true);
        heures.setValueFactory(vFact);

        

        container.setLeft(heuresLabel);
        container.setRight(heures);
        return container;
    }

    

    @Override
    protected Region createContainer(User item) {
        this.stagiaire = item;
        VBox container = new VBox(10);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(50));
        
        
        
        
        container.getChildren().addAll(createHeader(), createTopSlot(), createMiddleSlot());
        return container;
    }

    @Override
    protected List<User> queryAll() throws SQLException {
        return Database.getInstance().getUsersDao().queryForAll();
    }
    
}
