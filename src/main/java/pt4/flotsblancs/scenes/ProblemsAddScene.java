package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.components.ComboBoxes.CampGroundComboBox;
import pt4.flotsblancs.components.ComboBoxes.ClientComboBox;
import pt4.flotsblancs.components.ComboBoxes.ReservationComboBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ProblemsAddScene extends Region implements IScene
{

	private Problem problem;
	
	private Reservation reservation;
	private CampGround campground;
	private Client client;
	
	private ReservationComboBox resaCombo;
	private CampGroundComboBox campCombo;
	private ClientComboBox clientCombo;
	
	private MFXButton valider;
	
	private TextArea description;
	
	private Object changed;
	
	private boolean needClear;
	
    private ChangeListener<? super Object> changeListener = (obs, oldValue, newValue) -> {
        refreshPage();
    };
    
	@Override
	public void start() {
		
		this.problem = new Problem();
		
        VBox container = new VBox();
        container.setPadding(new Insets(50));
        container.getChildren().add(createHeader());
        container.getChildren().add(createBottom());

        this.getChildren().add(container);
	}
	
	public HBox createHeader()
	{
		HBox box = new HBox();
		try {
			resaCombo      = new ReservationComboBox(this.problem);
			campCombo      = new CampGroundComboBox(this.problem);
			clientCombo    = new ClientComboBox(this.problem);
			
			resaCombo.addUserChangedValueListenerNoCheck(changeListener);
			resaCombo.setOnAction(e -> {
				changed = resaCombo;
			});
			
			campCombo.addUserChangedValueListenerNoCheck(changeListener);
			campCombo.setOnAction(e -> {
				changed = campCombo;
			});
			
			clientCombo.addUserChangedValueListenerNoCheck(changeListener);
			clientCombo.setOnAction(e -> {
				changed = clientCombo;
			});
			
			box.getChildren().addAll(resaCombo,campCombo,clientCombo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return box;
	}
	
	public BorderPane createBottom()
	{
		
		BorderPane bottom = new BorderPane();
		
		valider = new MFXButton("Sauvegarder");
        valider.getStyleClass().add("action-button");
        
        MFXButton clear = new MFXButton("Réinitialiser");
        this.needClear = false;
        clear.getStyleClass().add("action-button");
        clear.setOnAction(e -> {
        	needClear = true;
        	this.campCombo.clearSelection();
        	this.clientCombo.clearSelection();
        	this.resaCombo.clearSelection();
        	
        	refreshPage();
        });
		
        valider.setOnAction(e-> {
	        try {
	          this.problem.setDescription(""+this.description.getText());
	          
	          if(this.client != null)
	        	  this.problem.setClient(this.client);
	          if(this.reservation != null)
	        	  this.problem.setReservation(this.reservation);
	          if(this.campground != null)
	        	  this.problem.setCampground(this.campground);	  
	          
	          
	          if(this.client == null && this.reservation == null && this.campground == null)
	          {
	        	  Router.showToast(ToastType.ERROR, "Aucun selecteur indiqué");
	          } else {
	          
		          Database.getInstance().getProblemDao().createOrUpdate(this.problem);
		       	  Router.goToScreen(Routes.PROBLEMES);
		          Router.showToast(ToastType.SUCCESS, "Problème ajouté");
	          }
	        } catch(Exception ex) {
	        	ex.printStackTrace();
	        }
        });
        
        
        description = new TextArea(problem.getDescription());
        
		bottom.setTop(description);
		bottom.setCenter(new VBoxSpacer());
		
		HBox buttons = new HBox();
		
		buttons.getChildren().addAll(valider,clear);
		
		bottom.setBottom(buttons);
		
		return bottom;
	}
	
	public void setCampground(CampGround campground)
	{
		this.campground = campground;
	}
	
	public void setClient(Client client)
	{
		this.client = client;
	}
	
	public void setReservation(Reservation reservation)
	{
		this.reservation = reservation;
	}
	
	public void refreshPage()
	{
		if(this.resaCombo.getSelectedItem() != null && changed instanceof ReservationComboBox)
		{
			changed = null;
			Reservation resa = resaCombo.getSelectedItem();
			
			if(resa.getCampground() != null) {
				this.campCombo.selectItem(resa.getCampground());
				this.campground = resa.getCampground();
			} else {
				this.campCombo.clearSelection();
				this.campground = null;
			}
			
			if(resa.getClient() != null) {
				
				List<Client> clients = this.clientCombo.getItems();
				
				for(Client c : clients)
				{
					Client c2 = resa.getClient();
					
					if(c.getAddresse().equalsIgnoreCase(c2.getAddresse()) && c.getName().equalsIgnoreCase(c2.getName()))
					{
						this.clientCombo.selectItem(c);
						this.client = c;
						break;
					}
				}
				
			} else {
				this.client = null;
				this.clientCombo.clearSelection();
			}
			
			this.reservation = resa;
			
		} else if(this.campCombo.getSelectedItem() != null && changed instanceof CampGroundComboBox) {
			changed = null;
			this.resaCombo.clearSelection();
			this.reservation = null;
			
			this.clientCombo.clearSelection();
			this.client = null;
			
			this.campground = this.campCombo.getSelectedItem();
		} else if(this.clientCombo.getSelectedItem() != null && changed instanceof ClientComboBox) {
			changed = null;
			this.campCombo.clearSelection();
			this.campground = null;
			
			this.resaCombo.clearSelection();
			this.reservation = null;
			
			this.client = this.clientCombo.getSelectedItem();
		} else if(needClear){
			needClear = false;
			changed = null;
			this.client = null;
			this.campground = null;
			this.reservation = null;
		}	
	}
}
