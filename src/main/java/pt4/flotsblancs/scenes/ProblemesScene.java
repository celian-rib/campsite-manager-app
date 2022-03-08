package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.time.LocalDate;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.components.CampgroundCard;
import pt4.flotsblancs.components.ClientCard;
import pt4.flotsblancs.components.HBoxSpacer;
import pt4.flotsblancs.components.ProblemDatePicker;
import pt4.flotsblancs.components.ReservationCard;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.components.ComboBoxes.ProblemStatusComboBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.types.Problems;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;


/*
 * 
 * Resa card
 * 
 */

public class ProblemesScene extends ItemScene<Problem>
{
	
    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;

    private Problem item;

    private CampgroundCard campCard;
    
    private ReservationCard resaCard;

    private ProblemDatePicker startDatePicker;

    private ProblemDatePicker endDatePicker;
    
    private TextArea description;
    
    private Problems status;
    
    private boolean firstLaunchToast = true;
    
    public void initLaunch()
    {
    	this.firstLaunchToast = true;
    }



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

        container.getChildren().add(createTopSlot());
        container.getChildren().add(new VBoxSpacer());
        
        container.getChildren().add(createBottomSlot());

        return container;
	}
	
	private BorderPane createBottomSlot()
	{
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(25));
		
		description = new TextArea(item.getDescription());
		description.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
	            item.setDescription(newValue);
	        }
	    });
		
		BorderPane spacing = new BorderPane();
		spacing.setPadding(new Insets(10));
		
		BorderPane comboPane  = new BorderPane();
		ProblemStatusComboBox combo = new ProblemStatusComboBox(item);
        combo.addUserChangedValueListener((obs, newVal, oldVal) -> {
            if (oldVal == newVal || oldVal == null)
            return;
            refreshPage();
            refreshDatabase(true);
        });
		comboPane.setCenter(combo);

		
		pane.setTop(description);
		pane.setCenter(spacing);
		pane.setBottom(comboPane);

		return pane;
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
        var dates = datesContainer();

        container.getChildren().add(cards);
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
        
        if(item.getClient() != null) {
	        var clientCard = new ClientCard(item.getClient(), 220);
	        container.getChildren().addAll(clientCard);
        }
        if(item.getCampground() != null) {
	        campCard = new CampgroundCard(item.getCampground(), 220);
	        container.getChildren().addAll(campCard);
        }
        
        if(item.getReservation() != null)
        {
	        resaCard = new ReservationCard(item.getReservation(), 220);
	        container.getChildren().addAll(resaCard);
        }

        return container;
    }

    /**
     * @return Conteneur contenant les ComboBox des dates de début de fin de la réservation
     */
    private VBox datesContainer() {
        VBox container = new VBox(CONTENT_SPACING);

        startDatePicker = new ProblemDatePicker(item, true);

        endDatePicker = new ProblemDatePicker(item, false);
        
        startDatePicker.addListener(changeListener);
        startDatePicker.setEditable(false);
        startDatePicker.setDisable(true);
        
        endDatePicker.addListener(changeListener);
        
        
        if(item.getStatus() != Problems.SOLVED) {
        	endDatePicker.setEditable(false);
        	endDatePicker.setDisable(true);
        	endDatePicker.updateCurrentDate();
        } else {
        	endDatePicker.setEditable(true);
        	endDatePicker.setDisable(false);
        }

        container.getChildren().addAll(startDatePicker, endDatePicker);

        /*
         * 
         * ICI AFFICHER LA DATE DE LAST UPDATE EN INMODIFIABLE
         * 
         * 
         */
        
           
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

        container.setLeft(title);

        return container;
    }
    
    ChangeListener<? super Object> changeListener = (obs, oldValue, newValue) -> {
        // Check if we need to refresh the page and the database
        if (oldValue == newValue || oldValue == null)
            return;
        refreshPage();
        refreshDatabase(true);
    };

    
    private void refreshPage() {
    	
    	boolean isSolved = item.getStatus() == Problems.SOLVED;
    	endDatePicker.setDisable(!isSolved);
    	
    	if(item.getCampground() != null & campCard != null)
    		campCard.refresh(item.getCampground());

        // Active / Desactive les contrôle en fonction de l'état du probleme
        
    }

    public void refreshDatabase(boolean showToast) {
        try {
            Database.getInstance().getProblemDao().update(item);
            if(showToast)
            	
            	Router.showToast(ToastType.SUCCESS, "Problème mis à jour");
        } catch (SQLRecoverableException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de connexion");
            Router.goToScreen(Routes.CONN_FALLBACK);
        } catch (SQLException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de chargement des données");
            Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
            Router.goToScreen(Routes.HOME);
        }
    }

	@Override
	protected List<Problem> queryAll() throws SQLException {
        return Database.getInstance().getProblemDao().queryForAll();
	}
}
