package pt4.flotsblancs.scenes;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;

public class LoginScene extends VBox implements IScene {

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public boolean showNavBar() {
        return false;
    }

    @Override
    public void start() {
        setAlignment(Pos.CENTER);
        setSpacing(100);

        // Création des élèments de cette page
        Label label = new Label(this.getName());

        MFXButton btn = new MFXButton("Retour");
        btn.setButtonType(ButtonType.RAISED);
        btn.setOnAction(e -> Router.goToScreen(Routes.HOME));

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        getChildren().addAll(label, btn);
    }
}
