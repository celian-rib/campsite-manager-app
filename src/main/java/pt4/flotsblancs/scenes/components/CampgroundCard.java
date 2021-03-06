package pt4.flotsblancs.scenes.components;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import io.github.palexdev.materialfx.controls.MFXButton;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.PriceUtils;

public class CampgroundCard extends BorderPane {

    private CampGround camp;

    private MFXButton createOpenButton() {
        MFXButton openBtn = new MFXButton("");
        var tooltip = new Tooltip("Voir la fiche de l'emplacement");
        tooltip.setShowDelay(new Duration(0));
        openBtn.setTooltip(tooltip);

        FontIcon openIcon = new FontIcon("far-caret-square-right:20");
        openIcon.setIconColor(Color.rgb(51, 59, 97));
        openBtn.setGraphic(openIcon);
        openBtn.setGraphicTextGap(10);

        openBtn.setOnAction(e -> Router.goToScreen(Routes.CAMPGROUNDS, camp));

        return openBtn;
    }

    private HBox createCampgroundInfos() {
        HBox container = new HBox(10);

        FontIcon icon = new FontIcon("fas-campground:25");
        icon.setIconColor(Color.rgb(51, 59, 97));

        VBox clientInfos = new VBox(2);
        Label name = new Label("Emplacement #" + camp.getId());
        name.setFont(new Font(15));

        Label id = new Label("Prix / Jours : " + PriceUtils.priceToString(camp.getPricePerDays()) + "€");
        id.setFont(new Font(11));
        id.setTextFill(Color.GRAY);
        clientInfos.getChildren().addAll(name, id);

        container.getChildren().addAll(icon, clientInfos);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    /**
     * Permet de créer une "Carte emplacement"
     */
    public CampgroundCard(CampGround camp, int width) {
        setMaxWidth(width);
        refresh(camp);
    }

    /**
     * Permet de rafrachir les informations affiché par cette carte, ou de changer
     * l'emplacement actuellement affiché
     */
    public void refresh(CampGround camp) {
        this.camp = camp;
        setLeft(createCampgroundInfos());
        setRight(createOpenButton());
    }
}
