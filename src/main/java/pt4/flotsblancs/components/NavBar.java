package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;

public class NavBar extends BorderPane {

    private MFXButton button(String content, int sizex, int sizey) {
        MFXButton button = new MFXButton(content, sizex, sizey);
        button.setButtonType(ButtonType.FLAT);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(20, 10, 20, 30));
        return button;
    }

    public NavBar() {
        setId("nav-bar");

        VBox centerButtons = new VBox();
        centerButtons.setAlignment(Pos.CENTER);

        VBox bottomButtons = new VBox();
        bottomButtons.setAlignment(Pos.CENTER);

        MFXButton btn1 = button("Clients", 200, 40);
        btn1.setOnAction(event -> Router.goToScreen(Routes.CLIENTS));

        MFXButton btn2 = button("Réservations", 200, 40);
        btn2.setOnAction(event -> Router.goToScreen(Routes.RESERVATIONS));

        MFXButton btn3 = button("Login", 200, 40);
        btn3.setOnAction(event -> Router.goToScreen(Routes.LOGIN));

        MFXButton btn4 = button("Accueil", 200, 40);
        btn4.setOnAction(event -> Router.goToScreen(Routes.HOME));

        MFXButton btn5 = new MFXButton("Déconnexion", 200, 60);
        btn5.setOnAction(event -> Router.goToScreen(Routes.LOGIN));
        btn5.setId("btn-logout");

        setCenter(centerButtons);
        setBottom(bottomButtons);
        centerButtons.getChildren().addAll(btn1, btn2, btn3, btn4);
        bottomButtons.getChildren().addAll(btn5);
    }
}
