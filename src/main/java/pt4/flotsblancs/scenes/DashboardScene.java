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

    private void refreshPage() {
        try {
            Period currentPeriod = periodComboBox.getSelectedPeriod();

            nbIncomingClientStats.setData(Stats.nbClientToday(false), Period.TODAY);
            nbOutgoingClientStats.setData(Stats.nbClientToday(true), Period.TODAY);
            nbReservationsStats.setData(Stats.affluence(currentPeriod), currentPeriod);

            var avgPrblm = Math.round(Stats.averageProblemTime(currentPeriod) * 100.0) / 100.0;
            averageProblemTimeStats.setData((float) avgPrblm, "jours", currentPeriod);

            mostProblemsStats.setData(Stats.mostProblematicCampground(currentPeriod), "prblm", currentPeriod);
            mostRentedCampsStats.setData(Stats.mostRentedCampground(currentPeriod), "résa", currentPeriod);
            
            right.getChildren().get(0).setOpacity(0);
            
            
            if(!Period.future(currentPeriod)) {
            	averageProblemTimeStats.setOpacity(1);
            	mostProblemsStats.setOpacity(1);
            } else {
            	averageProblemTimeStats.setOpacity(0);
            	mostProblemsStats.setOpacity(0);
            }
            
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
            return;
        }
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
