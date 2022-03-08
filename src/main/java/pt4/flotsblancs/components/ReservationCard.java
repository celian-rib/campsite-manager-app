package pt4.flotsblancs.components;

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
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;

public class ReservationCard extends BorderPane {

    private Reservation reservation;

    private MFXButton createOpenButton() {
        MFXButton openBtn = new MFXButton("");
        var tooltip = new Tooltip("Voir la reservation du client");
        tooltip.setShowDelay(new Duration(0));
        openBtn.setTooltip(tooltip);

        FontIcon openIcon = new FontIcon("far-caret-square-right:20");
        openIcon.setIconColor(Color.rgb(51, 59, 97));
        openBtn.setGraphic(openIcon);
        openBtn.setGraphicTextGap(10);

        openBtn.setOnAction(e -> Router.goToScreen(Routes.RESERVATIONS, reservation));
        return openBtn;
    }

    private HBox createClientInfos() {
        HBox container = new HBox(10);

        FontIcon icon = new FontIcon("far-calendar-alt:19");
        icon.setIconColor(Color.rgb(51, 59, 97));

        VBox clientInfos = new VBox(2);
        Label name = new Label(reservation.getId() + " " + reservation.getCampground());
        name.setFont(new Font(15));
        Label id = new Label("#" + reservation.getStartDate() + " - " + reservation.getEndDate());
        id.setFont(new Font(11));
        id.setTextFill(Color.GRAY);
        clientInfos.getChildren().addAll(name, id);

        container.getChildren().addAll(icon, clientInfos);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    public ReservationCard(Reservation reservation, int width) {
        this.reservation = reservation;
        setLeft(createClientInfos());
        setRight(createOpenButton());
        setMaxWidth(width);
    }
}
