package pt4.flotsblancs.router;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public abstract class BaseScene extends Group implements IScreen {

    private MFXButton button(String content, int sizex, int sizey) {
        MFXButton button = new MFXButton(content, sizex, sizey);
        String style = "-fx-background-color: #5fc9fa; -fx-text-fill: #ffffff; -fx-font-size: 20";
        button.setStyle(style);
        button.setButtonType(ButtonType.RAISED);
        return button;
    }

    public void start() {
        // NavBar
        var hbox = new HBox(8);

        MFXButton btn1 = button("Clients", 200, 40);
        btn1.setOnAction(event -> Router.goToScreen(Routes.CLIENTS));
       
        MFXButton btn2 = button("Réservations", 200, 40);
        btn2.setOnAction(event -> Router.goToScreen(Routes.RESERVATIONS));
        
        MFXButton btn3 = button("Login", 200, 40);
        btn3.setOnAction(event -> Router.goToScreen(Routes.LOGIN));
       
        MFXButton btn4 = button("Accueil", 200, 40);
        btn4.setOnAction(event -> Router.goToScreen(Routes.HOME));

        hbox.getChildren().addAll(btn1, btn2, btn3, btn4);
        // Ajout de la navbar
        addAll(hbox);
    }

    protected void addAll(Node... nodes) {
        this.getChildren().addAll(nodes);
    }
}
