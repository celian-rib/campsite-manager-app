package pt4.flotsblancs.scenes;

import javafx.scene.control.Label;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.IScene;

public class StagiairesScene extends VBox implements IScene {

    @Override
    public String getName() {
        return "Stagiaires";
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
        setAlignment(Pos.CENTER);

        // Création des élèments de cette page
        Label label = new Label(this.getName());

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        getChildren().addAll(label);
    }
    
}
