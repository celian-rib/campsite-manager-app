package pt4.flotsblancs.scenes.components;

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

public class InformationCard extends BorderPane {

  public InformationCard(String mainTitle, String subtitle, String info, Color color) {

    this.setPadding(new Insets(25, 25, 25, 25));
    this.setBackground(new Background(new BackgroundFill(color, new CornerRadii(25), null)));
    this.setPrefSize(500D, 90D);

    Label mainTitleLabel = new Label(mainTitle);
    Label subtitleLabel = new Label(subtitle);
    Label infoLabel = new Label(info);

    /*
     * mainTitleLabel.setPadding(new Insets(0,0,0,10));
     * subtitleLabel.setPadding(new Insets(0,0,0,10));
     * infoLabel.setPadding(new Insets(0,0,0,-10));
     */

    mainTitleLabel.setFont(new Font(15));
    subtitleLabel.setFont(new Font(12));
    infoLabel.setFont(new Font(15));

    VBox titleBox = new VBox();
    titleBox.setAlignment(Pos.CENTER);
    titleBox.getChildren().addAll(mainTitleLabel, subtitleLabel);

    VBox infoBox = new VBox();
    infoBox.setAlignment(Pos.CENTER);
    infoBox.getChildren().addAll(infoLabel);

    this.setLeft(titleBox);
    this.setRight(infoBox);
  }
}
