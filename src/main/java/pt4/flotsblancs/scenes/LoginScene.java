package pt4.flotsblancs.scenes;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;

public class LoginScene extends VBox implements IScene {

    TextField tFId;
    PasswordField pFMdp;

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public boolean showNavBar() {
        return false;
    }

    @Override
    public void onFocus() {
        // On clear les input de login et mdp a l'ouverture de la page
        tFId.setText("");
        pFMdp.setText("");
    }

    @Override
    public void onUnfocus() {

    }

    @Override
    public void start() {
        setAlignment(Pos.CENTER);
        setSpacing(30);

        Label label = new Label(this.getName());

        tFId = new TextField();
        tFId.setPromptText("Identifiant");
        tFId.setMaxWidth(200);

        pFMdp = new PasswordField();
        pFMdp.setPromptText("Mot de passe");
        pFMdp.setMaxWidth(200);

        MFXButton bValider = new MFXButton("Valider", 150, 30);
        bValider.setButtonType(ButtonType.RAISED);
        bValider.setOnAction(e -> {
            if (User.logIn(tFId.getText(), pFMdp.getText())) {
                Router.goToScreen(Routes.HOME);
                Router.showToast(ToastType.SUCCESS,
                        "Connecté.e en tant que " + User.getConnected().toString());
            } else {
                Router.showToast(ToastType.ERROR, "Identifiant ou mot de passe incorrect.");
            }
        });

        MFXButton DEVLOGIN = new MFXButton("FAST LOGIN (DEV)", 150, 30);
        DEVLOGIN.setOnAction(e -> {
            User.logIn("test", "plop");
            Router.goToScreen(Routes.HOME);
        });
        DEVLOGIN.setTextFill(Color.RED);
        getChildren().addAll(label, tFId, pFMdp, bValider, DEVLOGIN);
    }
}
