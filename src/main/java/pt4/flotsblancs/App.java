package pt4.flotsblancs;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.HashMap;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;

public class App extends Application {

	public static final HashMap<AvailableRoutes,FBScreen> routes = new HashMap<AvailableRoutes,FBScreen>();
	private static App INSTANCE;
	private FBScreen currentScene;
	private AvailableRoutes currentRoute;
	private Scene root;
	private static Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		App.INSTANCE = this;
		App.primaryStage = primaryStage;
		createRoutes();
		goToScreen(AvailableRoutes.LAUNCHING);
		this.root = new Scene((Parent)getCurrentScene(), 950, 800);
		routes.values().forEach(page -> page.initialize());
		primaryStage.setScene(this.root);

		primaryStage.setTitle("Camping !");
		
		primaryStage.show();
	}

	private void createRoutes() {
		routes.put(AvailableRoutes.LAUNCHING,new LaunchingScreen(App.INSTANCE));
		routes.put(AvailableRoutes.HOME,new HomeScreen(App.INSTANCE));
	}

	public static void goToScreen(AvailableRoutes newRoute) {
		App.INSTANCE.currentRoute = newRoute;
		App.INSTANCE.currentScene = routes.get(newRoute);
		
		if (App.INSTANCE.root != null) {
			App.INSTANCE.root.setRoot((Parent)App.INSTANCE.currentScene);
		}
	}

	public Scene getRoot() {
		return this.root;
	}

	public static App getInstance() {
		return INSTANCE;
	}

	public FBScreen getCurrentScene() {
		return this.currentScene;
	}
}