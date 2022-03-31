package pt4.flotsblancs.scenes.components.ComboBoxes;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.types.ProblemStatus;

public class ProblemStatusComboBox extends RefreshableComboBox<ProblemStatus> {

    private Problem problem;

    public ProblemStatusComboBox(Problem problem) {
        this.problem = problem;

        setFloatingText("Statut");
        setFloatMode(FloatMode.INLINE);
        getItems().addAll(ProblemStatus.values());
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
