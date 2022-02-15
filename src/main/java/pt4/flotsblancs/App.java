package pt4.flotsblancs;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.router.*;
import pt4.flotsblancs.router.Router.Routes;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

	public static void main(String[] args) throws SQLException {
		Database.getInstance(); // Initialisation connexion BD
		launch(args); // Lancement JFX
	}

	@Override
	public void start(Stage primaryStage) throws SQLException {
		Router.initialize( // Création routeur
				Routes.LOGIN, // Route par défaut
				primaryStage, // Fenêtre contenant le routeur
				1200, 700);

		// Préparation et affichage de la fenêtre
		setupStage(primaryStage);
	}

	private void setupStage(Stage primaryStage) {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setResizable(true);
		// primaryStage.getIcons().add(new Image("file:app_logo.png"));
		primaryStage.getIcons().add(new Image("https://teteamodeler.ouest-france.fr/media/cache/thumb_800/poule-explication-tte-modeler-du-mot-poule.jpeg"));
		// .setDockIconImage(new Image("https://teteamodeler.ouest-france.fr/media/cache/thumb_800/poule-explication-tte-modeler-du-mot-poule.jpeg"));
		org.eaw
		// .setDockIconImage(new ImageIcon("file:app_logo.png").getImage());
		primaryStage.show();
	}
}
