package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.HashMap;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.Stats;
import pt4.flotsblancs.Stats.Period;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.components.FlotsBlancsLogo;
import pt4.flotsblancs.scenes.components.HBoxSpacer;
import pt4.flotsblancs.scenes.components.StatsCard;
import pt4.flotsblancs.scenes.components.VBoxSpacer;
import pt4.flotsblancs.scenes.components.ComboBoxes.PeriodComboBox;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.ToastType;

public class DashboardScene extends VBox implements IScene {

    private StatsCard<Integer> nbIncomingClientStats;
    private StatsCard<Integer> nbOutgoingClientStats;
    private StatsCard<Integer> nbReservationsStats;

    private StatsCard<Float> averageProblemTimeStats;
    private StatsCard<HashMap<CampGround, Integer>> mostProblemsStats;
    private StatsCard<HashMap<CampGround, Integer>> mostRentedCampsStats;

    private VBox right;

    private PeriodComboBox periodComboBox;

    private MFXProgressSpinner spinner;
    private HBox statsContainer;

    private Stats stats;

    @Override
    public String getName() {
        return "Accueil";
    }

    @Override
    public void start() {
        setSpacing(40);
        setAlignment(Pos.CENTER);

        var logo = new FlotsBlancsLogo(true, false, 130);
        periodComboBox = new PeriodComboBox();
        periodComboBox.setMinWidth(200);
        periodComboBox.addUserChangedValueListenerNoCheck((obs, oldV, newV) -> {
            refreshPage(true);
        });

        periodComboBox.selectItem(Period.WEEKLY);

        this.getChildren().add(new VBoxSpacer());
        this.getChildren().add(logo);
        this.getChildren().add(new VBoxSpacer());
        this.getChildren().add(createStatsContainer());
        this.getChildren().add(periodComboBox);
        this.getChildren().add(new VBoxSpacer());
    }

    @Override
    public void onFocus() {
        if (stats == null)
            refreshPage(false);
    }

    private class LoadStatsTask extends Task<Stats> {
        private Period period;
        private boolean showToast;

        public LoadStatsTask(Period period, boolean showToast) {
            this.showToast = showToast;
            this.period = period;
        }

        @Override
        protected Stats call() throws Exception {
            setIsLoading(true);
            System.out.println("Loading stats for " + period.toString());
            stats = new Stats(period);
            return stats;
        }

        protected void succeeded() {
            System.out.println("Stats loaded");
            nbIncomingClientStats.setData(stats.getNbClientIncomming(), Period.TODAY);
            nbOutgoingClientStats.setData(stats.getNbClientOutgoing(), Period.TODAY);
            nbReservationsStats.setData(stats.getNbReservations(), period);

            var avgPrblm = Math.round(stats.getAverageProblemTime() * 100.0) / 100.0;
            averageProblemTimeStats.setData((float) avgPrblm, "jours", period);

            mostProblemsStats.setData(stats.getMostProblematicCamps(), "prblm", period);
            mostRentedCampsStats.setData(stats.getMostRentedCamps(), "résa", period);

            right.getChildren().get(0).setOpacity(0);

            if (!period.isInFuture()) {
                averageProblemTimeStats.setOpacity(1);
                mostProblemsStats.setOpacity(1);
            } else {
                averageProblemTimeStats.setOpacity(0);
                mostProblemsStats.setOpacity(0);
            }

            setIsLoading(false);
            if (showToast)
                Router.showToast(ToastType.SUCCESS,
                        "Statistiques de " + periodComboBox.getSelectedPeriod().toString().toLowerCase()
                                + " affichées");
        };

        protected void failed() {
            ExceptionHandler.loadIssue(new SQLException());
        };
    }

    private void setIsLoading(boolean isLoading) {
        statsContainer.setVisible(!isLoading);
        spinner.setVisible(isLoading);
    }

    private void refreshPage(boolean showToast) {
        Period currentPeriod = periodComboBox.getSelectedPeriod();
        new Thread(new LoadStatsTask(currentPeriod, showToast)).start();
    }

    private StackPane createStatsContainer() {
        var container = new StackPane();

        spinner = new MFXProgressSpinner();
        spinner.setVisible(false);

        statsContainer = new HBox();

        var SPACING = 10;

        var left = new VBox(SPACING);

        mostProblemsStats = new StatsCard<>(
                "Emplacements problématiques",
                Routes.CAMPGROUNDS,
                Color.rgb(255, 188, 166));

        nbOutgoingClientStats = new StatsCard<>(
                "Clients partants",
                Routes.CLIENTS,
                Color.rgb(197, 226, 252));

        nbIncomingClientStats = new StatsCard<>(
                "Clients arrivants",
                Routes.CLIENTS,
                Color.rgb(166, 255, 190));

        left.getChildren().addAll(mostProblemsStats, nbIncomingClientStats, nbOutgoingClientStats);

        right = new VBox(SPACING);

        averageProblemTimeStats = new StatsCard<>(
                "Résolution de problème",
                Routes.PROBLEMS,
                Color.rgb(255, 212, 133));

        nbReservationsStats = new StatsCard<>(
                "Réservations",
                Routes.RESERVATIONS,
                Color.rgb(234, 166, 255));

        mostRentedCampsStats = new StatsCard<>(
                "Emplacements les plus réservés",
                Routes.CAMPGROUNDS,
                Color.rgb(166, 255, 190));

        right.getChildren().addAll(averageProblemTimeStats, nbReservationsStats, mostRentedCampsStats);

        statsContainer.getChildren().add(new HBoxSpacer());
        statsContainer.getChildren().add(left);
        statsContainer.getChildren().add(new HBoxSpacer());
        statsContainer.getChildren().add(right);
        statsContainer.getChildren().add(new HBoxSpacer());

        container.getChildren().addAll(statsContainer, spinner);
        return container;
    }
}
