package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import javafx.print.Collation;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.Stats;
import pt4.flotsblancs.Stats.Period;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.scenes.components.FlotsBlancsLogo;
import pt4.flotsblancs.scenes.components.InformationCard;
import pt4.flotsblancs.scenes.components.VBoxSpacer;

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
		this.nbIncomingClients = Stats.nbClientToday(false);
		this.nbOutgoingClients = Stats.nbClientToday(true);
		this.nbReservations = Stats.affluence(currentPeriod);
		this.averageProblemTime = Stats.averageProblemTime(currentPeriod);
		this.mostProblematicCampgrounds = Stats.mostProblematicCampground(currentPeriod);
		this.mostRentedCampgrounds = Stats.mostRentedCampground(currentPeriod);
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

		var logo = new FlotsBlancsLogo(true, false, 130);
		
		this.getChildren().add(new VBoxSpacer());
		this.getChildren().add(logo);
		this.getChildren().add(new VBoxSpacer());
		this.getChildren().add(createStatsContainer());
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

	private BorderPane createStatsContainer() {
		var container = new BorderPane();
		var left = new VBox(10);
		// var nbClientStats = new InformationCard("Nombre de clients", "Ce mois-ci", Stats.nbClientToday(false)

		var right = new VBox(10);
		return container;
	}
}
