package pt4.flotsblancs.components;

import org.kordamp.ikonli.javafx.FontIcon;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.database.model.Client;

public class ClientCard extends BorderPane {
    public ClientCard(Client client) {
        HBox logoAndName = new HBox(10);

        FontIcon icon = new FontIcon("far-user-circle:30");
        icon.setIconColor(Color.GREY);

        VBox clientInfos = new VBox(2);
        Label name = new Label(client.getFirstName() + " " + client.getName());
        name.setFont(new Font(15));
        Label id = new Label("#" + client.getId() + " - " + client.getPhone());
        id.setFont(new Font(11));
        id.setTextFill(Color.GRAY);
        clientInfos.getChildren().addAll(name, id);

        logoAndName.getChildren().addAll(icon, clientInfos);
        logoAndName.setAlignment(Pos.CENTER);

        MFXButton openBtn = new MFXButton("Voir la fiche client");
        FontIcon openIcon = new FontIcon("far-caret-square-right:15");
        openIcon.setIconColor(Color.GREY);
        openBtn.setGraphic(openIcon);
        openBtn.setGraphicTextGap(10);

        setLeft(logoAndName);
        setRight(openBtn);

        setPadding(new Insets(30));
    }
}
