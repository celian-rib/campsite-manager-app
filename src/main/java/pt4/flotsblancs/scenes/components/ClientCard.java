package pt4.flotsblancs.scenes.components;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;

public class ClientCard extends BorderPane {

    private Client client;

    private MFXButton createOpenButton() {
        MFXButton openBtn = new MFXButton("");
        var tooltip = new Tooltip("Voir la fiche du client");
        tooltip.setShowDelay(new Duration(0));
        openBtn.setTooltip(tooltip);

        FontIcon openIcon = new FontIcon("far-caret-square-right:20");
        openIcon.setIconColor(Color.rgb(51, 59, 97));
        openBtn.setGraphic(openIcon);
        openBtn.setGraphicTextGap(10);

        openBtn.setOnAction(e -> Router.goToScreen(Routes.CLIENTS, client));
        return openBtn;
    }

    private HBox createClientInfos() {
        HBox container = new HBox(10);

        FontIcon icon = new FontIcon("far-user-circle:30");
        icon.setIconColor(Color.rgb(51, 59, 97));
        
        if(client.isFrequentClient())
        {
        	icon = new FontIcon("fas-crown:25");
        	icon.setIconColor(Color.rgb(255, 215, 0));
        }
        
        
        

        VBox clientInfos = new VBox(2);
        Label name = new Label(client.getFirstName() + " " + client.getName());
        name.setFont(new Font(15));
        Label id = new Label("#" + client.getId() + " - " + client.getPhone());
        id.setFont(new Font(11));
        id.setTextFill(Color.GRAY);
        clientInfos.getChildren().addAll(name, id);

        container.getChildren().addAll(icon, clientInfos);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    public ClientCard(Client client, int width) {
        this.client = client;
        setLeft(createClientInfos());
        setRight(createOpenButton());
        setMaxWidth(width);
    }
}
