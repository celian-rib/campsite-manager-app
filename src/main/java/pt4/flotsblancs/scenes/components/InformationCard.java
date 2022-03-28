package pt4.flotsblancs.scenes.components;

import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.database.model.CampGround;

public class InformationCard extends BorderPane {

    public InformationCard(String mainTitle, String subtitle, String data, Color color) {
        this.setPadding(new Insets(20));
        this.setBackground(new Background(new BackgroundFill(color, new CornerRadii(17), null)));
        this.setPrefSize(400D, 60D);

        this.setLeft(createTitlesContainer(mainTitle, subtitle));
        this.setRight(createStaticInfoContainer(data));
    }
    
    public InformationCard(String mainTitle, String subtitle, HashMap<CampGround, Integer> data, Color color) {
        this.setPadding(new Insets(20));
        this.setBackground(new Background(new BackgroundFill(color, new CornerRadii(17), null)));
        this.setPrefSize(400D, 60D);

        this.setLeft(createTitlesContainer(mainTitle, subtitle));
        // this.setRight(createStaticInfoContainer(data));
    }

    private VBox createStaticInfoContainer(String info) {
        Label infoLabel = new Label(info + "");
        infoLabel.setFont(new Font(20));

        VBox infoBox = new VBox();
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getChildren().addAll(infoLabel);
        return infoBox;
    }

    private VBox createTitlesContainer(String title, String subtitle) {
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label mainTitleLabel = new Label(title);
        Label subtitleLabel = new Label(subtitle);

        mainTitleLabel.setFont(new Font(16));
        subtitleLabel.setFont(new Font(13));
        subtitleLabel.setTextFill(Color.GRAY);

        titleBox.getChildren().addAll(mainTitleLabel, subtitleLabel);
        return titleBox;
    }
}
