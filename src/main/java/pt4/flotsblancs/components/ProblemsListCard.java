package pt4.flotsblancs.components;

import java.util.Collection;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.effects.DepthLevel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ProblemsListCard extends StackPane {

    public ProblemsListCard(Collection<Problem> problems) {
        var problemsList = new MFXListView<Problem>();
        problemsList.setDepthLevel(DepthLevel.LEVEL0);

        problemsList.getItems().addAll(problems);
        problemsList.setMaxWidth(500);
        problemsList.setPrefWidth(500);
        problemsList.setMinWidth(100);
        problemsList.setMaxHeight(140);

        var noProblem = createNoProblemContainer();
      
        getChildren().addAll(problemsList, noProblem);
        noProblem.setVisible(problems.size() == 0);
    }

    private VBox createNoProblemContainer() {
        var noProblemContainer = new VBox(30);
        var noProblemLabel = new Label("Aucun problème pour cette réservation");
        noProblemContainer.setAlignment(Pos.CENTER);

        var addProblemBtn = new MFXButton("Ajouter un problème");
        addProblemBtn.getStyleClass().add("action-button-outlined");
        addProblemBtn.setOnAction(e -> {
            // TODO linking
            Router.showToast(ToastType.WARNING, "LINKING TO DO");
        });

        noProblemContainer.getChildren().addAll(noProblemLabel, addProblemBtn);
        return noProblemContainer;
    }
}
