package pt4.flotsblancs;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt4.flotsblancs.orm.Database;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;

public class App extends Application {

	public static void main(String[] args) throws SQLException  {
		Database.getInstance(); // Initialisation connexion BD
		launch(args); // Lancement JFX
	}

	@Override
	public void start(Stage primaryStage) throws SQLException {
		Router.initialize( // Création routeur
				Routes.HOME, // Route par défaut
				primaryStage, // Fenêtre contenant le routeur
				1200, 700);

		// Affichage de la fenêtre
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
