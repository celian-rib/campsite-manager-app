package pt4.flotsblancs.scenes.components.ComboBoxes;

import io.github.palexdev.materialfx.beans.Alignment;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import pt4.flotsblancs.Stats.Period;

public class PeriodComboBox extends RefreshableComboBox<Period> {

    /**
     * Combo box pour sélectionner une période
     */
    public PeriodComboBox() {
        setFloatingText("Période");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(Period.NEXT_THREE_YEAR, Period.NEXT_TWO_YEAR, Period.NEXT_YEAR,
                Period.NEXT_MONTH, Period.NEXT_WEEK, Period.WEEKLY, Period.MONTHLY, Period.ANNUALY,
                Period.TWO_YEAR, Period.THREE_YEAR);
        setMinWidth(180);
        setAnimated(false);
        selectItem(Period.NEXT_WEEK);
        setPopupAlignment(Alignment.of(HPos.CENTER, VPos.TOP));
    }

    public Period getSelectedPeriod() {
        return this.getSelectedItem();
    }

    @Override
    public void refresh() {}

}
