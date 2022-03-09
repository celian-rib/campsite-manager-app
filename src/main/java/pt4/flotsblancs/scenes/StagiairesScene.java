package pt4.flotsblancs.scenes;

import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.scenes.items.ItemScene;

public class StagiairesScene extends ItemScene<User> implements IScene {

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
        Label label = new Label(item.getDisplayName());
        container.getChildren().add(label);
        return container;
    }

    @Override
    protected List<User> queryAll() throws SQLException {
        return Database.getInstance().getUsersDao().queryForAll();
    }
    
}
