package pt4.flotsblancs.scenes;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.Stats;
import pt4.flotsblancs.Stats.Period;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.scenes.components.FlotsBlancsLogo;
import pt4.flotsblancs.scenes.components.InformationCard;
import pt4.flotsblancs.scenes.components.VBoxSpacer;

public class DashboardScene extends VBox implements IScene {

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
		return container;
	}
}
