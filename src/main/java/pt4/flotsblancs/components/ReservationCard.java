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
import lombok.Getter;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.utils.DateUtils;

public class ReservationCard extends BorderPane {

    @Getter
    private Reservation reservation;

    private MFXButton createOpenButton() {
        if (reservation == null)
            return null;
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

        if (reservation == null) {
            var noResa = new Label("Aucune réservation en cours");
            noResa.setFont(new Font(15));
            noResa.setTextFill(Color.GRAY);
            container.getChildren().addAll(icon,noResa);
            return container;
        }

        VBox resaInfos = new VBox(2);
        Label name = new Label("Réservation #" + reservation.getId() + " - " + reservation.getClient().getName());
        name.setFont(new Font(15));

        Label dates = new Label("Du " + DateUtils.toFormattedString(reservation.getStartDate()) + " au "
                + DateUtils.toFormattedString(reservation.getEndDate()));
        dates.setFont(new Font(11));
        dates.setTextFill(Color.GRAY);

        resaInfos.getChildren().addAll(name, dates);

        container.getChildren().addAll(icon, resaInfos);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    public ReservationCard(Reservation reservation, int width) {
        this.reservation = reservation;
        setLeft(createClientInfos());
        setRight(createOpenButton());
        setMinWidth(width);
    }
}
