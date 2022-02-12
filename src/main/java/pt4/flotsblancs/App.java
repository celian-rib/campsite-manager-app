package pt4.flotsblancs;

import pt4.flotsblancs.router.*;
import pt4.flotsblancs.screens.TestScreen;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import pt4.flotsblancs.orm.Database;

public class App extends Application {

	public static void main(String[] args) throws SQLException {
		Database.getInstance(); // Initialisation connexion BD
		launch(args); // Lancement JFX
	}

	@Override
	public void start(Stage primaryStage) throws SQLException {
		// // Création routeur
		Router.initialize(
			Routes.HOME, // Route par défaut
			primaryStage
		);

		// Affichage de la fenêtre
		primaryStage.show();
	}
}
