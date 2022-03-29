package pt4.flotsblancs.scenes.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.effects.DepthLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.Item;

// TODO afficher le status du problème en plus (circle)
public class ProblemsListCard extends StackPane {

    private final MFXListView<Problem> problemsList;
    private final Label noProblemLabel;

    private final Item item;

    public ProblemsListCard(Reservation reservation) {
        this.item = reservation;

        problemsList = new MFXListView<Problem>();
        noProblemLabel = new Label("Aucun problème pour cette réservation");
        problemsList.getItems().addAll(reservation.getProblems());
        setup();
    }

    public ProblemsListCard(Client client) {
        this.item = client;

        problemsList = new MFXListView<Problem>();
        noProblemLabel = new Label("Aucun problème lié à ce client");

        problemsList.getItems().addAll(client.getOpenProblems());

        setup();
    }

    public ProblemsListCard(CampGround campGround) {
        this.item = campGround;

        problemsList = new MFXListView<Problem>();
        noProblemLabel = new Label("Aucun problème pour cet emplacement");
        problemsList.getItems().addAll(campGround.getProblems());
        setup();
    }

    private void setup() {
        problemsList.setDepthLevel(DepthLevel.LEVEL0);
        problemsList.setMaxWidth(500);
        problemsList.setPrefWidth(400);
        problemsList.setMinWidth(100);
        problemsList.setMaxHeight(140);

        problemsList.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            // Récupération de l'item selectionné très peu conventionelle, voir thread
            // github de mfx :
            // https://github.com/palexdev/MaterialFX/discussions/159
            var listValues = problemsList.getSelectionModel().getSelection().values();
            if (listValues.size() == 0)
                return;
            ObservableList<Problem> namesList = FXCollections.observableArrayList(listValues);
            Router.goToScreen(Routes.PROBLEMS, namesList.listIterator().next());
        });

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
            Router.goToScreen(Routes.PROBLEMS_ADD, item);
        });

        noProblemContainer.setMaxWidth(200);
        noProblemContainer.getChildren().addAll(noProblemLabel, addProblemBtn);
        return noProblemContainer;
    }
}
