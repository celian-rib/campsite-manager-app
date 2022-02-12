package pt4.flotsblancs.screens;

import javafx.scene.control.Label;
import pt4.flotsblancs.router.BaseScene;

public class ReservationsScene extends BaseScene {

    @Override
    public String getName() {
        return "Réservations";
    }

    @Override
    public void start() {
        super.start(); // Appel au start de BaseScene pour afficher la barre de navigation

        // Création des élèments de cette page
        Label label = new Label(this.getName());
        label.setLayoutX(100);
        label.setLayoutY(100);

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        addAll(label);
    }

}
