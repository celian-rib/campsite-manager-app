package pt4.flotsblancs.scenes.components.ComboBoxes;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.Stats.Period;

public class PeriodComboBox extends RefreshableComboBox<Period> {

    public PeriodComboBox() {
        setFloatingText("Période");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(Period.values());
        setMinWidth(180);
        setAnimated(false);
    }

    @Override
    public void refresh() {
        
    }
    
}
