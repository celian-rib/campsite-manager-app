package pt4.flotsblancs;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.*;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.breakpoints.BreakPointManager;
import pt4.flotsblancs.scenes.utils.WindowManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.scene.image.Image;
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
			User.logIn("test", "test");
			defaultRoute = Routes.valueOf(dotenv.get("DEFAULT_ROUTE"));
		}

		try {
			Database.getInstance(); // Initialisation connexion BD
		} catch (Exception e) {
			System.err.println(e);
			defaultRoute = Routes.CONN_FALLBACK;
		}

		Router.initialize(defaultRoute, primaryStage, 1300, 750);

        BreakPointManager.init(primaryStage);
		new WindowManager(primaryStage, 20);

		primaryStage.setMinWidth(950);
		primaryStage.setMinHeight(700);

		// Affichage de la fenêtre
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setResizable(true);
		
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/app_logo.png")));
		
		primaryStage.show();

		
	}
}
