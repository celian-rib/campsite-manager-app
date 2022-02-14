package pt4.flotsblancs.scenes;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.UserStore;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;

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
        setSpacing(70);

        // Création des élèments de cette page
        Label label = new Label(this.getName());

        TextField tFId = new TextField();
        tFId.setPromptText("Identifiant");
        tFId.setMaxWidth(200);

        PasswordField pFMdp = new PasswordField();
        pFMdp.setPromptText("Mot de passe");
        pFMdp.setMaxWidth(200);

        MFXButton bValider = new MFXButton("Valider", 150, 30);
        bValider.setButtonType(ButtonType.RAISED);
        bValider.setOnAction(e -> {
            if(UserStore.log(tFId.getText(), pFMdp.getText())){
                Router.goToScreen(Routes.HOME);
                System.out.println("bravo "+ tFId );
                Router.showToast(ToastType.SUCCESS, "Bonjour " + UserStore.getUserInstance().getName());
            } else {
                System.out.println("t'es NUL "+ tFId );
                Router.showToast(ToastType.ERROR, "Identifiant ou mot de passe incorrect.");
            }
        });
        
        MFXButton bRetour = new MFXButton("Retour");
        bRetour.setButtonType(ButtonType.RAISED);
        bRetour.setOnAction(e -> Router.goToScreen(Routes.HOME));

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        getChildren().addAll(label, tFId, pFMdp, bValider, bRetour);
    }
}
