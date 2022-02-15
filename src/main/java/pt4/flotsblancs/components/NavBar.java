package pt4.flotsblancs.components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;

public class NavBar extends BorderPane {

    private Label userLabel;

    private MFXButton button(String content, int sizex, int sizey) {
        MFXButton button = new MFXButton(content, sizex, sizey);
        button.setButtonType(ButtonType.FLAT);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(20, 10, 20, 30));
        return button;
    }

    public NavBar() {
        setId("nav-bar");

        setTop(new FlotsBlancsLogo(false, true, 50));
        setCenter(navigationButtons());
        setBottom(logOutButton());
    }

    private VBox navigationButtons() {
        VBox centerButtons = new VBox();
        centerButtons.setAlignment(Pos.CENTER);
        MFXButton btn1 = button("Clients", 200, 40);
        btn1.setOnAction(event -> Router.goToScreen(Routes.CLIENTS));

        MFXButton btn2 = button("Réservations", 200, 40);
        btn2.setOnAction(event -> Router.goToScreen(Routes.RESERVATIONS));

        MFXButton btn3 = button("Login", 200, 40);
        btn3.setOnAction(event -> Router.goToScreen(Routes.LOGIN));

        MFXButton btn4 = button("Accueil", 200, 40);
        btn4.setOnAction(event -> Router.goToScreen(Routes.HOME));



        centerButtons.getChildren().addAll(btn1, btn2, btn3, btn4);

        return centerButtons;
    }

    private VBox logOutButton() {
        VBox bottomButtons = new VBox();
        bottomButtons.setAlignment(Pos.CENTER);

        userLabel = new Label();
        userLabel.setTextFill(Color.WHITE);

        MFXButton btn5 = new MFXButton("Déconnexion", 200, 60);
        btn5.setOnAction(event -> {
            User.logOut();
            Router.showToast(ToastType.INFO, "Deconnecté.e");
            Router.goToScreen(Routes.LOGIN);
        });
        btn5.setId("btn-logout");

        bottomButtons.getChildren().addAll(userLabel, btn5);
        return bottomButtons;
    }

    public void update() {
        userLabel.setText(User.isConnected() ? User.getConnected().toString() : "");
    }
}
