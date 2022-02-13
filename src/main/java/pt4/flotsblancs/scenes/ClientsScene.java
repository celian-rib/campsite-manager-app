package pt4.flotsblancs.scenes;

import javafx.scene.control.Label;

public class ClientsScene extends BaseScene {

    @Override
    public String getName() {
        return "Clients";
    }

    @Override
    public void start() {
        // Création des élèments de cette page
        Label label = new Label(this.getName());
        label.setLayoutX(100);
        label.setLayoutY(100);

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        addAll(label);
    }

}
