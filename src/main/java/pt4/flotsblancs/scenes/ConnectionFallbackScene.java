package pt4.flotsblancs.scenes;

import org.kordamp.ikonli.javafx.FontIcon;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ConnectionFallbackScene extends VBox implements IScene {

    @Override
    public boolean showNavBar() {
        return false;
    }

    @Override
    public void start() {
        setSpacing(40);
        setAlignment(Pos.CENTER);

        FontIcon handIcon = new FontIcon("fas-wifi:100");
        handIcon.setIconColor(Color.GREY);

        VBox labels = new VBox(5);
        labels.setAlignment(Pos.CENTER);
        Label errorLabel = new Label("Erreur de connexion...");
        errorLabel.setFont(new Font(30));
        Label checkLabel = new Label("Vérifiez votre connexion internet");
        labels.getChildren().addAll(errorLabel, checkLabel);

        MFXButton refreshBtn = new MFXButton("Rafraîchir");
        refreshBtn.setButtonType(ButtonType.RAISED);

        refreshBtn.setOnAction(e -> {
            System.out.println("Trying to reconnect");
            try {
                if(Database.getInstance().isConnected()) {
                    Router.goToScreen(Routes.HOME);
                } else {
                    throw new Exception("getInstance error");
                }
            } catch (Exception ex) {
                System.err.println(ex);
                Router.showToast(ToastType.ERROR, "Impossible de se connecter");
            }
        });
        
        getChildren().addAll(handIcon, labels, refreshBtn);
    }
}
