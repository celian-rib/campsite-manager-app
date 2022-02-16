package pt4.flotsblancs.components;

import org.kordamp.ikonli.javafx.FontIcon;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;

public class NavBar extends BorderPane {

    private Label userLabel;


    public NavBar() {
        setId("nav-bar");

        setTop(new FlotsBlancsLogo(false, true, 50));
        setCenter(navigationButtons());
        setBottom(logOutButton());
    }

    private MFXButton createNavButton(String content, Routes route, String icon) {
        MFXButton btn = new MFXButton(content, 200, 40);
        btn.setButtonType(ButtonType.FLAT);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(20, 10, 20, 30));
        btn.setOnAction(event -> Router.goToScreen(route));

        FontIcon iconNode = new FontIcon(icon);
        iconNode.setIconColor(Color.WHITE);
        btn.setGraphic(iconNode);
        btn.setGraphicTextGap(20);
        return btn;
    }

    private VBox navigationButtons() {
        VBox centerButtons = new VBox();
        centerButtons.setAlignment(Pos.CENTER);

        centerButtons.getChildren().addAll(
                createNavButton("Accueil", Routes.HOME, "fas-home:24"),
                createNavButton("Clients", Routes.CLIENTS, "far-user:24"),
                createNavButton("Réservations", Routes.RESERVATIONS, "far-calendar-alt:24"),
                createNavButton("Stocks", Routes.RESERVATIONS, "fas-box:22")
        );

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
