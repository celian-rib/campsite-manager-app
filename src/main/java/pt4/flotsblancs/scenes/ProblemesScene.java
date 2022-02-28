package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.components.CampGroundComboBox;
import pt4.flotsblancs.components.CampgroundCard;
import pt4.flotsblancs.components.ClientCard;
import pt4.flotsblancs.components.EquipmentComboBox;
import pt4.flotsblancs.components.HBoxSpacer;
import pt4.flotsblancs.components.PersonCountComboBox;
import pt4.flotsblancs.components.ProblemDatePicker;
import pt4.flotsblancs.components.ServiceComboBox;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ProblemesScene extends ItemScene<Problem>
{
	
    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;

    private Label dayCount;

    private Label datesLabel;

    private Problem item;

    private CampgroundCard campCard;

    private ProblemDatePicker startDatePicker;

    private ProblemDatePicker endDatePicker;

    private CampGroundComboBox campComboBox;

    private ServiceComboBox serviceComboBox;

    private PersonCountComboBox personCountComboBox;

    private EquipmentComboBox equipmentsComboBox;

    @Override
    public String getName() {
        return "Problèmes";
    }
    
	@Override
	protected Region createContainer(Problem item) {
        this.item = item;

        VBox container = new VBox();

        container.setPadding(new Insets(50));

        container.getChildren().add(createHeader());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createTopSlot());
        container.getChildren().add(new VBoxSpacer());

        return container;
	}


    /**
     * @return Conteneur avec les cartes, les equipements et services, les sélections de dates
     */
    private HBox createTopSlot() {
        HBox container = new HBox(10);
        // TODO Responsive padding
        // container.setPadding(new Insets(50));
        container.setPadding(new Insets(INNER_PADDING));

        var cards = cardsContainer();
        var gear = selectedEquipmentAndServicesContainer();
        var dates = datesContainer();

        container.getChildren().add(cards);
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(gear);
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(dates);

        return container;
    }

    /**
     * @return VBox contenant la carte du client et la carte de l'emplacement associés à cette
     *         réservation
     */
    private VBox cardsContainer() {
        VBox container = new VBox(CONTENT_SPACING);
        container.setPadding(new Insets(10, 0, 0, 0));
        container.setMinWidth(220);
        var clientCard = new ClientCard(item.getClient(), 220);
        campCard = new CampgroundCard(item.getCampground(), 220);
        container.getChildren().addAll(clientCard, campCard);
        return container;
    }

    /**
     * @return Conteneur contenant les ComboBox des dates de début de fin de la réservation
     */
    private VBox datesContainer() {
        VBox container = new VBox(CONTENT_SPACING);

        startDatePicker = new ProblemDatePicker(item, true);

        endDatePicker = new ProblemDatePicker(item, false);

        container.getChildren().addAll(startDatePicker, endDatePicker);

        /*
         * 
         * ICI AFFICHER LA DATE DE LAST UPDATE EN INMODIFIABLE
         * 
         * 
         */
        try {
            campComboBox = new CampGroundComboBox(item.getReservation());
            container.getChildren().add(campComboBox);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return container;
    }


    /**
     * @return conteneur contenant les ComboBox de sélection de l'equipement / services / nb
     *         personnes
     */
    private VBox selectedEquipmentAndServicesContainer() {
        VBox container = new VBox(CONTENT_SPACING);

        personCountComboBox = new PersonCountComboBox(item.getReservation());

        serviceComboBox = new ServiceComboBox(item.getReservation());

        equipmentsComboBox = new EquipmentComboBox(item.getReservation());

        container.getChildren().addAll(personCountComboBox, serviceComboBox, equipmentsComboBox);
        return container;
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        Label title = new Label("Problème  #" + item.getId());
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        VBox datesInfosContainer = new VBox(5);
        datesLabel = new Label();
        datesLabel.setFont(new Font(17));
        datesLabel.setTextFill(Color.GREY);

        dayCount = new Label();
        dayCount.setFont(new Font(13));
        dayCount.setTextFill(Color.DARKGREY);

        datesInfosContainer.getChildren().addAll(datesLabel, dayCount);

        container.setLeft(title);
        container.setRight(datesInfosContainer);
        return container;
    }

	@Override
	protected List<Problem> queryAll() throws SQLException {
        return Database.getInstance().getProblemDao().queryForAll();
	}
}
