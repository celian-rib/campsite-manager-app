package pt4.flotsblancs;

import pt4.flotsblancs.router.*;
import pt4.flotsblancs.router.Router.Routes;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt4.flotsblancs.orm.Database;

public class App extends Application {

	public static void main(String[] args) throws SQLException {
		Database.getInstance(); // Initialisation connexion BD
		launch(args); // Lancement JFX
	}

	@Override
	public void start(Stage primaryStage) throws SQLException {
		Router.initialize( // Création routeur
				Routes.HOME, // Route par défaut
				primaryStage, // Fenêtre contenant le routeur
				1200, 700);

		// GO ON SECOND SCREEN
		javafx.geometry.Rectangle2D bounds =
				javafx.stage.Screen.getScreens().get(1).getVisualBounds();
		primaryStage.setX(bounds.getMinX() + 100);
		primaryStage.setY(bounds.getMinY() + 100);
		// GO ON SECOND SCREEN

		// Affichage de la fenêtre
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
