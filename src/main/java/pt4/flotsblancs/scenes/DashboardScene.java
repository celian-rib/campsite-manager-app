package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.HashMap;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.Stats;
import pt4.flotsblancs.Stats.Period;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.scenes.components.FlotsBlancsLogo;
import pt4.flotsblancs.scenes.components.HBoxSpacer;
import pt4.flotsblancs.scenes.components.InformationCard;
import pt4.flotsblancs.scenes.components.VBoxSpacer;
import pt4.flotsblancs.scenes.components.ComboBoxes.PeriodComboBox;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;

public class DashboardScene extends VBox implements IScene {

    private Period currentPeriod;

    private int nbIncomingClients;
    private int nbOutgoingClients;
    private int nbReservations;
    private float averageProblemTime;

    private HashMap<CampGround, Integer> mostProblematicCampgrounds;
    private HashMap<CampGround, Integer> mostRentedCampgrounds;

    @Override
    public String getName() {
        return "Accueil";
    }

    private void queryStats() throws SQLException {
        this.currentPeriod = Period.ANNUALY;
        this.nbIncomingClients = Stats.nbClientToday(false);
        this.nbOutgoingClients = Stats.nbClientToday(true);
        this.nbReservations = Stats.affluence(currentPeriod);
        this.averageProblemTime = Stats.averageProblemTime(currentPeriod);
        this.mostProblematicCampgrounds = Stats.mostProblematicCampground(currentPeriod);

        mostProblematicCampgrounds.forEach((k, v) -> {
           System.out.println(k); 
           System.out.println(v); 
           System.out.println("----"); 
        });

        this.mostRentedCampgrounds = Stats.mostRentedCampground(currentPeriod);
        // print all the above variables
        System.out.println("nbIncomingClients: " + nbIncomingClients);
        System.out.println("nbOutgoingClients: " + nbOutgoingClients);
        System.out.println("nbReservations: " + nbReservations);
        System.out.println("averageProblemTime: " + averageProblemTime);
        System.out.println("mostProblematicCampgrounds: " + mostProblematicCampgrounds);
        System.out.println("mostRentedCampgrounds: " + mostRentedCampgrounds);
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
        try {
            queryStats();
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
            return;
        }

        setSpacing(40);
        setAlignment(Pos.CENTER);

        var logo = new FlotsBlancsLogo(true, false, 130);

        this.getChildren().add(new VBoxSpacer());
        this.getChildren().add(logo);
        this.getChildren().add(new VBoxSpacer());
        this.getChildren().add(createStatsContainer());
        this.getChildren().add(new PeriodComboBox());
        this.getChildren().add(new VBoxSpacer());

        // try {
        // BorderPane stats = new BorderPane();

        // VBox left = new VBox();
        // VBox right = new VBox();

        // InformationCard c1 = new InformationCard("Résolution des problèmes","Temps
        // moyen, ce mois-ci",Stats.averageProblemTime(Period.MONTHLY)+"
        // jours",Color.rgb(236, 204, 104,0.5f));
        // InformationCard c2 = new InformationCard("Occupation","Ce
        // mois-ci",Stats.affluence(Period.MONTHLY)+" emplacements",Color.rgb(164, 176,
        // 190,0.5f));
        // InformationCard c3 = new
        // InformationCard("TITRE","Sous-titre","information",Color.rgb(255, 155, 155));

        // left.getChildren().addAll(c1,c2);
        // right.getChildren().add(c3);

        // /*left.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0),
        // CornerRadii.EMPTY, Insets.EMPTY)));
        // left.setPrefWidth(300D);
        // left.setPrefHeight(100D);

        // right.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 0),
        // CornerRadii.EMPTY, Insets.EMPTY)));
        // right.setPrefWidth(300D);
        // right.setPrefHeight(100D);*/

        // stats.setLeft(left);
        // stats.setRight(right);

        // this.setTop(null);
        // this.setBottom(stats);
        // } catch(Exception e) {
        // e.printStackTrace();
        // }
    }

    private HBox createStatsContainer() {
        var container = new HBox();

        var left = new VBox(20);

        var nbIncomingClientStats = new InformationCard(
                "Clients arrivants",
                "Ce mois-ci",
                this.nbIncomingClients + "",
                Color.rgb(166, 255, 190));

        var nbOutgoingClientStats = new InformationCard(
                "Clients partants",
                "Ce mois-ci",
                this.nbIncomingClients + "",
                Color.rgb(197, 226, 252));

        var nbReservationsStats = new InformationCard(
                "Réservations",
                "Ce mois-ci",
                this.nbReservations + "",
                Color.rgb(234, 166, 255));

        left.getChildren().add(nbIncomingClientStats);
        left.getChildren().add(nbOutgoingClientStats);
        left.getChildren().add(nbReservationsStats);

        var right = new VBox(10);

        var averageProblemTimeStats = new InformationCard(
                "Résolution de problème",
                "Temps moyen ce mois-ci",
                Math.round(this.averageProblemTime * 100.0) / 100.0 + " jours",
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
