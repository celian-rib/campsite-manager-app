package pt4.flotsblancs.scenes;

import javafx.scene.control.Label;

public class LoginScene extends BaseScene {

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public void start() {
        // On efface la barre de navigation sur la page de login;
        this.showNavBar = false;

        // Création des élèments de cette page
        Label label = new Label(this.getName());
        label.setLayoutX(100);
        label.setLayoutY(100);

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        addAll(label);
    }

}
