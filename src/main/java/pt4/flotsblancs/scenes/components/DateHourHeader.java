package pt4.flotsblancs.scenes.components;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class DateHourHeader extends BorderPane {

    private Label currentPage;

    public DateHourHeader() {
        currentPage = new Label();
        currentPage.setFont(new Font(24));
        currentPage.setTextFill(Color.rgb(51, 59, 97));

        String pattern = "hh:mm:ss MMM dd, yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.FRANCE);

        Label hourLabel = new Label(format.format(new Date()));
        hourLabel.setFont(new Font(20));
        hourLabel.setTextFill(Color.rgb(51, 59, 97));

        setRight(hourLabel);
        setLeft(currentPage);
        setPadding(new Insets(20, 30, 20, 20));

        Timeline clock =
                new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        hourLabel.setText(format.format(new Date()));
                    }
                }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    public void updateCurrentPage(String currentPageName) {
        currentPage.setText(currentPageName);
    }
}
