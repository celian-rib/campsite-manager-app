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

        setTop(logoAndTitle());
        setCenter(navigationButtons());
        setBottom(logOutButton());
    }

    private VBox logoAndTitle() {
        VBox topContainer = new VBox(10);
        ImageView icon = new ImageView();

        try {
            Image img = new Image(new FileInputStream("src/main/resources/logo.png"));
            img.heightProperty();
            img.widthProperty();
            icon.setFitHeight(50);
            icon.setFitWidth(50);
            icon.setImage(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(30, 0, 0, 0));

        Label title = new Label("Les Flots Blancs");
        title.setTextFill(Color.WHITE);
        topContainer.getChildren().addAll(icon, title);
        return topContainer;
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

        MFXButton btn5 = new MFXButton("Déconnexion", 200, 60);
        btn5.setOnAction(event -> Router.goToScreen(Routes.LOGIN));
        btn5.setId("btn-logout");

        bottomButtons.getChildren().addAll(btn5);
        return bottomButtons;
    }
}
