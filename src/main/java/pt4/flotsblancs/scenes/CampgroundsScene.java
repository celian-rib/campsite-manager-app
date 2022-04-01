package pt4.flotsblancs.scenes;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.scenes.components.HBoxSpacer;
import pt4.flotsblancs.scenes.components.ProblemsListCard;
import pt4.flotsblancs.scenes.components.VBoxSpacer;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.PriceUtils;

import java.sql.SQLException;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CampgroundsScene extends ItemScene<CampGround> {

    private CampGround campground;

    @Override
    public String getName() {
        return "Emplacements";
    }

    @Override
    protected String addButtonText() {
        return null;
    }

    private Region createHeader() {
        var container = new HBox();

        var campgroundID = new Label("Emplacement #" + campground.getId());
        campgroundID.setFont(new Font(20));
        container.getChildren().add(campgroundID);

        return container;
    }

    @Override
    protected Region createContainer(CampGround item) {
        this.campground = item;
        var container = new VBox(10);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);

        // Création des élèments de cette page
        Label label = new Label(this.getName());
        container.getChildren().add(label);
        container.getChildren().add(createHeader());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createTopSLot());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createBottomSlot());
        container.getChildren().add(new VBoxSpacer());

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        getChildren().addAll(label);
        return container;
    }

    private Region createTopSLot() {
        var container = new VBox(10);

        var campgroundDesc = new Label("Description: " + campground.getDescription());
        campgroundDesc.setAlignment(Pos.CENTER_LEFT);
        campgroundDesc.setWrapText(true);
        container.getChildren().add(campgroundDesc);

        var campgroundEquipmentAllowed = new Label("Equipement possible : " + campground.getAllowedEquipments());
        container.getChildren().add(campgroundEquipmentAllowed);

        var campgroundProvidedServices = new Label("Services : " + campground.getProvidedServices());
        container.getChildren().add(campgroundProvidedServices);

        var campgroundSurface = new Label("Surface : " + campground.getSurface() + "Mètres carré");
        container.getChildren().add(campgroundSurface);

        return container;
    }

    private Region createBottomSlot() {
        var container = new HBox();

        var campgroundPricePerDay = new Label("prix par jour: " + PriceUtils.priceToString(campground.getPricePerDays()) + "€");
        container.getChildren().add(campgroundPricePerDay);

        container.getChildren().add(new HBoxSpacer());

        var campgroundProblems = new ProblemsListCard(campground);
        container.getChildren().add(campgroundProblems);

        return container;
    }

    @Override
    protected List<CampGround> queryAll() throws SQLException {
        return Database.getInstance().getCampgroundDao().queryForAll();
    }
}
