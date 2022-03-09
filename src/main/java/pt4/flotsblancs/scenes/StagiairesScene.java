package pt4.flotsblancs.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.scenes.items.ItemScene;

public class StagiairesScene extends ItemScene<User> {

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

    @Override
    protected Region createContainer(User item) {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(5));
        
        MFXButton resetBtn = new MFXButton("Reset password.");
        MFXTextField usernameTxtFld = new MFXTextField(item.getLogin());
        MFXTextField firstNameTxtFld = new MFXTextField(item.getFirstName());
        MFXTextField lastNameTxtFld = new MFXTextField(item.getName());
        Label roleLabel = new Label("Rôle : " + (item.isAdmin() ? "Administrateur" : "Stagiaire"));
        
        
        Label titleLabel = new Label("#" + item.getId() + " - " + item.getDisplayName());
        container.getChildren().addAll(titleLabel, roleLabel, firstNameTxtFld, lastNameTxtFld, usernameTxtFld, resetBtn);
        return container;
    }

    @Override
    protected List<User> queryAll() throws SQLException {
        return Database.getInstance().getUsersDao().queryForAll();
    }
    
}
