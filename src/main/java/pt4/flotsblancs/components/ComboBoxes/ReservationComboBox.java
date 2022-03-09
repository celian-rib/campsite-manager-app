package pt4.flotsblancs.components.ComboBoxes;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;

public class ReservationComboBox  extends RefreshableComboBox<Reservation>
{
	private Problem problem;
	
    public ReservationComboBox(Problem problem) {

    	this.problem = problem;
    	
        setFloatingText("Equipements client");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);
        
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null || newValue == null)
                return;
            problem.setReservation(newValue);
        });
    }
	
	@Override
	public void refresh() 
	{
		if(problem.getReservation() != null)
			selectItem(problem.getReservation());
	}
}
