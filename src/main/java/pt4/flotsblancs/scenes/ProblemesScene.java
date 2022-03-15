package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.components.CampgroundCard;
import pt4.flotsblancs.components.ClientCard;
import pt4.flotsblancs.components.HBoxSpacer;
import pt4.flotsblancs.components.ReservationCard;
import pt4.flotsblancs.components.VBoxSpacer;
import pt4.flotsblancs.components.ComboBoxes.ProblemStatusComboBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ProblemesScene extends ItemScene<Problem> {

    private final int INNER_PADDING = 20;
    private final int CONTENT_SPACING = 20;

    private Problem problem;

    private CampgroundCard campCard;
    private ReservationCard resaCard;

    private Label startDate;
    private Label endDate;
    private Label lastUpdateDate;

    private TextArea description;

    private boolean descriptionModified;

    private ChangeListener<? super Object> changeListener = (obs, oldValue, newValue) -> {
        if (oldValue == newValue || oldValue == null)
            return;
        refreshPage();
        refreshDatabase();
    };
    
    @Override
    public void onAddButtonClicked()
    {
        try {
        	Router.goToScreen(Routes.PROBLEMS_ADD);
        } catch (Exception e1) {
            e1.printStackTrace();
            Router.showToast(ToastType.ERROR, "Erreur durant l'ajout du problème");
            Router.goToScreen(Routes.CONN_FALLBACK);
        }
    }

    @Override
    public String getName() {
        return "Problèmes";
    }

    @Override
    protected String addButtonText() {
        return "Ajouter un problème";
    }

    @Override
    protected Region createContainer(Problem problem) {
        this.problem = problem;
        this.descriptionModified = false;

        VBox container = new VBox();

        container.setPadding(new Insets(50));

        container.getChildren().add(createHeader());

        container.getChildren().add(createTopSlot());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createBottomSlot());

        refreshPage();

        return container;
    }

    private void refreshPage() {
        // TODO bien formatter les dates
        startDate.setText("Date de début : " + problem.getStartDate().toString());
        endDate.setText("Date de fin : "
                + (problem.getEndDate() == null ? "" : problem.getEndDate().toString()));
        lastUpdateDate.setText("Dernière mise à jour : " + problem.getLastUpdateDate().toString());
    }

    private BorderPane createBottomSlot() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(25));

        description = new TextArea(problem.getDescription());
        description.textProperty().addListener((obs, oldVal, newVal) -> {
            if(oldVal != null && oldVal != newVal)
                this.descriptionModified = true;
        });

        BorderPane spacing = new BorderPane();
        spacing.setPadding(new Insets(10));

        BorderPane comboPane = new BorderPane();
        ProblemStatusComboBox combo = new ProblemStatusComboBox(problem);
        combo.addUserChangedValueListener(changeListener);
        comboPane.setCenter(combo);


        pane.setTop(description);
        pane.setCenter(spacing);
        pane.setBottom(comboPane);

        return pane;
    }

    /**
     * @return Conteneur avec les cartes, les equipements et services, les sélections de dates
     */
    private HBox createTopSlot() {
        HBox container = new HBox(10);
        container.setPadding(new Insets(INNER_PADDING));

        var cards = cardsContainer();
        var dates = createDatesContainer();

        container.getChildren().add(cards);
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(dates);

        return container;
    }

    /**
     * @return VBox contenant la carte du client et la carte de l'emplacement associés à cette
     *         réservation
     */
    private VBox cardsContainer() {
        VBox container = new VBox(CONTENT_SPACING);
        container.setPadding(new Insets(10, 0, 0, 0));
        container.setMinWidth(220);

        if (problem.getClient() != null) {
            var clientCard = new ClientCard(problem.getClient(), 220);
            container.getChildren().addAll(clientCard);
        }
        if (problem.getCampground() != null) {
            campCard = new CampgroundCard(problem.getCampground(), 220);
            container.getChildren().addAll(campCard);
        }

        if (problem.getReservation() != null) {
            resaCard = new ReservationCard(problem.getReservation(), 220);
            container.getChildren().addAll(resaCard);
        }

        return container;
    }

    /**
     * @return Conteneur contenant les ComboBox des dates de début de fin de la réservation
     */
    private VBox createDatesContainer() {
        VBox container = new VBox(CONTENT_SPACING);

        startDate = new Label();
        endDate = new Label();
        lastUpdateDate = new Label();

        container.getChildren().addAll(startDate, endDate, lastUpdateDate);
        return container;
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createHeader() {

        BorderPane container = new BorderPane();

        Label title = new Label("Problème  #" + problem.getId());
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        container.setLeft(title);

        return container;
    }

    private void refreshDatabase() {
        try {
            Database.getInstance().getProblemDao().update(problem);
            Router.showToast(ToastType.SUCCESS, "Problème mis à jour");
        } catch (SQLRecoverableException e) {
            e.printStackTrace();
            Router.showToast(ToastType.ERROR, "Erreur de connexion");
            Router.goToScreen(Routes.CONN_FALLBACK);
        } catch (SQLException e) {
            e.printStackTrace();
            Router.showToast(ToastType.ERROR, "Erreur de chargement des données");
            Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
            Router.goToScreen(Routes.HOME);
        }
    }

    @Override
    protected List<Problem> queryAll() throws SQLException {
        return Database.getInstance().getProblemDao().queryForAll();
    }

    @Override
    public void onUnfocus() {
        onContainerUnfocus();
    }
    
    @Override
    public void onContainerUnfocus() {
        if(this.descriptionModified)
            refreshDatabase();
    }
}
