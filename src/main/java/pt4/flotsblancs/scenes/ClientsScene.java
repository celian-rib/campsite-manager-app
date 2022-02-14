package pt4.flotsblancs.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.router.IScene;

public class ClientsScene extends VBox implements IScene {

    @Override
    public String getName() {
        return "Clients";
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
        
    }

    @Override
    public void start() {
        setAlignment(Pos.CENTER);

        // Création des élèments de cette page
        Label label = new Label(this.getName());

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        getChildren().addAll(label);
    }
}
