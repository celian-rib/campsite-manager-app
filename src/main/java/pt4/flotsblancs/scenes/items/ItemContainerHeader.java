package pt4.flotsblancs.scenes.items;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class ItemContainerHeader extends BorderPane {
    public ItemContainerHeader(String h1, String h2, String h3) {
        var h1Label = new Label(h1);
        h1Label.setFont(new Font(24));
        h1Label.setTextFill(Color.rgb(51, 59, 97));

        VBox rightContainer = new VBox(5);
        
        var h2Label = new Label(h2);
        h2Label.setFont(new Font(17));
        h2Label.setTextFill(Color.GREY);

        var h3Label = new Label();
        h3Label.setFont(new Font(13));
        h3Label.setTextFill(Color.DARKGREY);

        rightContainer.getChildren().addAll(h2Label, h3Label);

        setLeft(h1Label);
        setRight(rightContainer);
    }
}
