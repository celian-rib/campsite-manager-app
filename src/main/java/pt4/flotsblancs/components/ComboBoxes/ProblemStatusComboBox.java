package pt4.flotsblancs.components.ComboBoxes;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.types.Problems;

public class ProblemStatusComboBox extends RefreshableComboBox<Problems> {

    private Problem problem;

    public ProblemStatusComboBox(Problem problem) {
        this.problem = problem;

        setFloatingText("Status");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(Problems.values());
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null || newValue == null)
                return;
            problem.setStatus(newValue);
        });
    }
    
    @Override
    public void refresh() {
        selectItem(problem.getStatus());
    }
}
