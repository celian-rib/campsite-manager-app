package pt4.flotsblancs.scenes.components;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EmptyItemContainer extends VBox {

    /**
     * Logo et texte utilisé dans le système ItemScene qui permet d'indique que rine
     * n'est encore sélectionné par l'utilisateur
     */
    public EmptyItemContainer() {
        setSpacing(15);
        setAlignment(Pos.CENTER);

        var label = new Label("Aucun élèment sélectionné");
        label.setFont(new Font(15));
        label.setTextFill(Color.LIGHTGRAY);

        FontIcon icon = new FontIcon("fas-columns:30");
        icon.setIconColor(Color.rgb(51, 59, 97));

        getChildren().addAll(icon, label);
    }
}
