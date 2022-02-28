package pt4.flotsblancs;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.router.*;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.WindowManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

	public static void main(String[] args) throws SQLException {
		launch(args); // Lancement JFX
	}

	@Override
	public void start(Stage primaryStage) throws SQLException {

		Routes defaultRoute = Routes.LOGIN;

		Dotenv dotenv = Dotenv.load();
		if (dotenv.get("DEFAULT_ROUTE") != null) {
			System.out.println("DEFAULT ROUTE OVERRIDE");
			defaultRoute = Routes.valueOf(dotenv.get("DEFAULT_ROUTE"));
		}

		try {
			Database.getInstance(); // Initialisation connexion BD
		} catch (Exception e) {
			System.err.println(e);
			defaultRoute = Routes.CONN_FALLBACK;
		}

		Router.initialize(defaultRoute, primaryStage, 1400, 850);

		new WindowManager(primaryStage, 20);
		// TODO responsive
		// primaryStage.setMinWidth(1400);
		// primaryStage.setMinHeight(850);

		// Affichage de la fenêtre
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
