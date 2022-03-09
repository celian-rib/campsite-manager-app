package pt4.flotsblancs.components.ComboBoxes;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;

public class ClientComboBox  extends RefreshableComboBox<Client>
{
	private Problem problem;
	
    public ClientComboBox(Problem problem) {

    	this.problem = problem;
    	
        setFloatingText("Equipements client");
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
        setAnimated(false);
        
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null || newValue == null)
                return;
            problem.setClient(newValue);
        });
    }
	
	@Override
	public void refresh() 
	{
		if(problem.getClient() != null)
			selectItem(problem.getClient());
	}

}
