package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.router.Router;

public class WindowBar extends BorderPane {

    private MFXButton button(String content) {
        MFXButton button = new MFXButton(content);
        button.setButtonType(ButtonType.RAISED);
        button.setAlignment(Pos.BASELINE_CENTER);
        return button;
    }

    public WindowBar() {
        setId("top-bar");

        var hbox = new HBox(5);

        MFXButton close = button("x");
        close.setId("close-btn");
        close.setRippleColor(Color.TRANSPARENT);

        MFXButton minimize = button("-");
        minimize.setId("minimize-btn");
        minimize.setRippleColor(Color.TRANSPARENT);

        MFXButton fullscreen = button("+");
        fullscreen.setId("fullscreen-btn");
        fullscreen.setRippleColor(Color.TRANSPARENT);

        hbox.getChildren().addAll(fullscreen, minimize, close);
        hbox.setPadding(new Insets(10));

        setRight(hbox);
        toFront();

        fullscreen.setOnAction(e -> {
            Router.getPrimaryStage().setFullScreen(!Router.getPrimaryStage().isFullScreen());
        });

        close.setOnAction(e -> {
            Router.getPrimaryStage().close();
        });

        minimize.setOnAction(e -> {
            Router.getPrimaryStage().setIconified(!Router.getPrimaryStage().isIconified());
        });
    }

    /**
     * Met à jour l'affichage de la barre de fenêtre, Soit le titre affiché au milieu
     * 
     * @param newTitle nouveau titre a afficher
     */
    public void update() {
        if (Router.getCurrentScene().showNavBar())
            setStyle("-fx-background-radius: 0 10px 0 0;");
        else
            setStyle("-fx-background-radius: 10px 10px 0 0;");
    }
}
