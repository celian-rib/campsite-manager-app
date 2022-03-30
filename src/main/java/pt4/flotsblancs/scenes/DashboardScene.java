package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.HashMap;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
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
import pt4.flotsblancs.scenes.components.InformationCard;
import pt4.flotsblancs.scenes.components.VBoxSpacer;
import pt4.flotsblancs.scenes.components.ComboBoxes.PeriodComboBox;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.ToastType;

public class DashboardScene extends VBox implements IScene {

    private InformationCard<Integer> nbIncomingClientStats;
    private InformationCard<Integer> nbOutgoingClientStats;
    private InformationCard<Integer> nbReservationsStats;

    private InformationCard<Float> averageProblemTimeStats;
    private InformationCard<HashMap<CampGround, Integer>> mostProblemsStats;
    private InformationCard<HashMap<CampGround, Integer>> mostRentedCampsStats;

    private VBox right;

    private PeriodComboBox periodComboBox;

    // TODO spinner
    private boolean isLoading;

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
        periodComboBox.addUserChangedValueListenerNoCheck((obs, oldV, newV) -> {
            refreshPage();
            Router.showToast(ToastType.SUCCESS,
                    "Statistiques de " + periodComboBox.getSelectedPeriod().toString().toLowerCase() + " affichées");
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
        refreshPage();
    }

    private class LoadStatsTask extends Task<Stats> {
        private Period period;
        private Stats stats;

        public LoadStatsTask(Period period) {
            this.period = period;
        }

        @Override
        protected Stats call() throws Exception {
            System.out.println("Loading stats for " + period.toString());
            stats = new Stats(period);
            return stats;
        }

        protected void succeeded() {
            System.out.println("Stats loaded");
            System.out.println(stats);
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

            isLoading = false;
        };

        protected void failed() {
            ExceptionHandler.loadIssue(new SQLException());
        };
    }

    private void refreshPage() {
        Period currentPeriod = periodComboBox.getSelectedPeriod();
        new Thread(new LoadStatsTask(currentPeriod)).start();
    }

    private HBox createStatsContainer() {
        var container = new HBox();

        var SPACING = 10;

        var left = new VBox(SPACING);

        nbIncomingClientStats = new InformationCard<>(
                "Clients arrivants",
                Routes.CLIENTS,
                Color.rgb(166, 255, 190));

        nbOutgoingClientStats = new InformationCard<>(
                "Clients partants",
                Routes.CLIENTS,
                Color.rgb(197, 226, 252));

        nbReservationsStats = new InformationCard<>(
                "Réservations",
                Routes.RESERVATIONS,
                Color.rgb(234, 166, 255));

        left.getChildren().add(nbIncomingClientStats);
        left.getChildren().add(nbOutgoingClientStats);
        left.getChildren().add(nbReservationsStats);

        right = new VBox(SPACING);

        averageProblemTimeStats = new InformationCard<>(
                "Résolution de problème",
                Routes.PROBLEMS,
                Color.rgb(255, 212, 133));

        mostProblemsStats = new InformationCard<>(
                "Emplacements problématiques",
                Routes.CAMPGROUNDS,
                Color.rgb(255, 188, 166));

        mostRentedCampsStats = new InformationCard<>(
                "Emplacements les plus réservés",
                Routes.CAMPGROUNDS,
                Color.rgb(166, 255, 190));

        right.getChildren().add(averageProblemTimeStats);
        right.getChildren().add(mostProblemsStats);
        right.getChildren().add(mostRentedCampsStats);

        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(left);
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(right);
        container.getChildren().add(new HBoxSpacer());

        return container;
    }
}
