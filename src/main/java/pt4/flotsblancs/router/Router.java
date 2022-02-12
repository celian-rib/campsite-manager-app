package pt4.flotsblancs.router;

import java.util.HashMap;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import pt4.flotsblancs.screens.*;

public class Router {

    public static final HashMap<Routes, BaseScene> routes = new HashMap<>() {
        {
            put(Routes.HOME, new DashboardScene());
            put(Routes.LOGIN, new LoginScene());
            put(Routes.RESERVATIONS, new ReservationsScene());
            put(Routes.CLIENTS, new ClientsScene());
        }
    };

    /**
     * Conteneur au plus haut niveau de l'application contenant ce routeur (La fenêtre)
     */
    private static Stage primaryStage;

    /**
     * Route actuelle affichée sur le "Stage" de l'application
     */
    private static Routes currentRoute;

    private static Scene rootScene;

    private static IScreen currentScene;

    public static void initialize(Routes defaultRoute, Stage _primaryStage) {
        primaryStage = _primaryStage;
        goToScreen(defaultRoute);

        BaseScene baseScene = new DashboardScene();
		rootScene = new Scene(baseScene, 800, 700);

        routes.values().forEach(page -> page.start());
        primaryStage.setScene(rootScene);
        baseScene.start();
        log("Initialized");
    }

    public static void goToScreen(Routes newRoute) {
        if (!routes.containsKey(newRoute)) {
            System.err.println("Route not implemented");
            return;
        }

        log("Switch -> " + newRoute);

        currentRoute = newRoute;
        currentScene = routes.get(newRoute);

        primaryStage.setTitle(currentScene.getName());

        if (rootScene != null)
            rootScene.setRoot((Parent) currentScene);
    }

    public static Routes getCurrentRoutes() {
        return currentRoute;
    }
    
    public static Scene getRootScene() {
        return rootScene;
    }

    private static void log(String message) {
        System.out.println("[Router] " + message);
    }
}
