package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
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
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.scenes.items.ItemScene;

public class ProblemsAddScene extends ItemScene<Problem>
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

	@Override
	protected Region createContainer(Problem problem) {
		
		this.problem = problem;
		//Il faut une interface pour :
		
		//écrire description
		//Selectionner reservation -> Si valeur on désaffiche les autres ou on remet à null
		//Selectionner campground
		//Selectionner client
		
		//valider

        VBox container = new VBox();
        container.setPadding(new Insets(50));
        container.getChildren().add(createHeader());
        container.getChildren().add(createBottom());

        return container;
				
		//Mettre start à la création, lastupdate à la creation
		
	}
	
	public HBox createHeader()
	{
		HBox box = new HBox();
		try {
			resaCombo = new ReservationComboBox(this.problem);
			campCombo  = new CampGroundComboBox(this.problem);
			clientCombo    = new ClientComboBox(this.problem);
			
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
		
        valider.setOnAction(e-> {
        	System.out.println("Ok bg");
        });
        
        
        description = new TextArea(problem.getDescription());
        
		bottom.setTop(description);
		bottom.setCenter(new VBoxSpacer());
		bottom.setBottom(valider);
		
		return bottom;
	}
	
	public void setCampground(CampGround campground)
	{
		this.campground = campground;
		refreshPage();
	}
	
	public void setClient(Client client)
	{
		this.client = client;
		refreshPage();
	}
	
	public void setReservation(Reservation reservation)
	{
		this.reservation = reservation;
		refreshPage();
	}
	
	public void refreshPage()
	{
		
	}

	@Override
	protected List<Problem> queryAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Router.goToScreen(Routes.PROBLEMES, p);
     * Router.showToast(ToastType.SUCCESS, "Problème ajouté");
	 */
}
