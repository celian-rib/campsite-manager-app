package pt4.flotsblancs.components;

import java.util.ArrayList;
import java.util.Collection;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.effects.DepthLevel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ProblemsListCard extends StackPane {

    private final MFXListView<Problem> problemsList;
    private final Label noProblemLabel;

    public ProblemsListCard(Collection<Problem> problems) {
        problemsList = new MFXListView<Problem>();
        noProblemLabel = new Label("Aucun problème pour cette réservation");
        problemsList.getItems().addAll(problems);
        setup();
    }
    
    public ProblemsListCard(Client client) {
        problemsList = new MFXListView<Problem>();
        noProblemLabel = new Label("Aucun problème lié à ce client");
        ArrayList<Problem> problems = new ArrayList<Problem>();
        client.getReservations().forEach(r -> problems.addAll(r.getProblems()));
        problemsList.getItems().addAll(problems);
        setup();
    }

    private void setup() {
        problemsList.setDepthLevel(DepthLevel.LEVEL0);
        problemsList.setMaxWidth(500);
        problemsList.setPrefWidth(400);
        problemsList.setMinWidth(100);
        problemsList.setMaxHeight(140);

        var noProblem = createNoProblemContainer();
        getChildren().addAll(problemsList, noProblem);
        noProblem.setVisible(problemsList.getItems().size() == 0);
    }

    private VBox createNoProblemContainer() {
        var noProblemContainer = new VBox(30);
        noProblemContainer.setAlignment(Pos.CENTER);

        var addProblemBtn = new MFXButton("Ajouter un problème");
        addProblemBtn.getStyleClass().add("action-button-outlined");
        addProblemBtn.setOnAction(e -> {
            // TODO linking
            Router.showToast(ToastType.WARNING, "LINKING TO DO");
        });

        noProblemContainer.setMaxWidth(200);
        noProblemContainer.getChildren().addAll(noProblemLabel, addProblemBtn);
        return noProblemContainer;
    }
}
