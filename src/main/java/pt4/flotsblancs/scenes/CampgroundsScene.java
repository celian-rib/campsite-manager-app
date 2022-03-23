package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.scenes.items.ItemScene;

public class CampgroundsScene extends ItemScene<Client> {

    @Override
    public String getName() {
        return "Emplacements";
    }

    @Override
    public boolean showNavBar() {
        return true;
    }

    @Override
    public void onFocus() {

    }

    @Override
    public void onUnfocus() {
        onContainerUnfocus();
    }

    //@Override - Décommenter si CampgroundsScene extends ItemScene
    public void onContainerUnfocus() {
        //refreshDatabase();
    }

    @Override
    public void start() {
        var container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        // Création des élèments de cette page
        Label label = new Label(this.getName());

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        getChildren().addAll(label);
    }

    @Override
    protected Region createContainer(Client item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<Client> queryAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
