package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.Stats;
import pt4.flotsblancs.Stats.Period;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.components.FlotsBlancsLogo;
import pt4.flotsblancs.scenes.components.HBoxSpacer;
import pt4.flotsblancs.scenes.components.InformationCard;
import pt4.flotsblancs.scenes.components.VBoxSpacer;
import pt4.flotsblancs.scenes.components.ComboBoxes.PeriodComboBox;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.ToastType;

public class DashboardScene extends VBox implements IScene {

    private int nbIncomingClients;
    private int nbOutgoingClients;
    private int nbReservations;
    private float averageProblemTime;

    private HashMap<CampGround, Integer> mostProblematicCampgrounds;
    private HashMap<CampGround, Integer> mostRentedCampgrounds;

    private InformationCard<Integer> nbIncomingClientStats;
    private InformationCard<Integer> nbOutgoingClientStats;
    private InformationCard<Integer> nbReservationsStats;

    private InformationCard<Float> averageProblemTimeStats;

    private PeriodComboBox periodComboBox;

    @Override
    public String getName() {
        return "Accueil";
    }

    /////////////////////////////////////
    // Nb de client qui quittent
    // Nb de client qui arrivent
    // affluence
    // Temps moyen de réolution de problèmes
    // emplacments les plus problématiques
    // emplacements les plus réservés
    /////////////////////////////////////

    @Override
    public void start() {
        setSpacing(40);
        setAlignment(Pos.CENTER);

        var logo = new FlotsBlancsLogo(true, false, 130);
        periodComboBox = new PeriodComboBox();
        periodComboBox.addUserChangedValueListenerNoCheck((obs, oldV, newV) -> {
            refreshPage();
            Router.showToast(ToastType.SUCCESS,
                    "Statistiques de " + periodComboBox.getSelectedPeriod().toString().toLowerCase() + " affichées");
        });

        this.getChildren().add(new VBoxSpacer());
        this.getChildren().add(logo);
        this.getChildren().add(new VBoxSpacer());
        this.getChildren().add(createStatsContainer());
        this.getChildren().add(periodComboBox);
        this.getChildren().add(new VBoxSpacer());
    }

    @Override
    public void onFocus() {
        refreshPage();
    }

    private void refreshPage() {
        try {
            Period currentPeriod = periodComboBox.getSelectedPeriod();

            nbIncomingClientStats.setData(Stats.nbClientToday(false), currentPeriod);
            nbOutgoingClientStats.setData(Stats.nbClientToday(true), currentPeriod);
            nbReservationsStats.setData(Stats.affluence(currentPeriod), currentPeriod);

            var avgPrblm = Math.round(Stats.averageProblemTime(currentPeriod) * 100.0) / 100.0;
            averageProblemTimeStats.setData((float) avgPrblm, "jours", currentPeriod);

            // this.averageProblemTime = Stats.averageProblemTime(currentPeriod);
            // this.mostProblematicCampgrounds =
            // Stats.mostProblematicCampground(currentPeriod);
            // this.mostRentedCampgrounds = Stats.mostRentedCampground(currentPeriod);
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
            return;
        }
    }

    private HBox createStatsContainer() {
        var container = new HBox();

        var left = new VBox(20);

        nbIncomingClientStats = new InformationCard<>(
                "Clients arrivants",
                Color.rgb(166, 255, 190));

        nbOutgoingClientStats = new InformationCard<>(
                "Clients partants",
                Color.rgb(197, 226, 252));

        nbReservationsStats = new InformationCard<>(
                "Réservations",
                Color.rgb(234, 166, 255));

        left.getChildren().add(nbIncomingClientStats);
        left.getChildren().add(nbOutgoingClientStats);
        left.getChildren().add(nbReservationsStats);

        var right = new VBox(10);

        averageProblemTimeStats = new InformationCard<>(
                "Résolution de problème",
                Color.rgb(255, 188, 166));

        right.getChildren().add(averageProblemTimeStats);

        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(left);
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(right);
        container.getChildren().add(new HBoxSpacer());

        return container;
    }
}
