package pt4.flotsblancs.router;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt4.flotsblancs.scenes.*;

public class Router {

    public enum Routes {
        CLIENTS, RESERVATIONS, LOGIN, HOME
    }

    /**
     * Liste des routes disponibles dans l'application
     */
    public static final HashMap<Routes, BaseScene> routes = new HashMap<>() {
        {
            put(Routes.HOME, new DashboardScene());
            put(Routes.LOGIN, new LoginScene());
            put(Routes.RESERVATIONS, new ReservationsScene());
            put(Routes.CLIENTS, new ClientsScene());
        }
    };

    /**
     * Route actuelle affichée sur le "Stage" de l'application
     */
    private static Routes currentRoute;

    /**
     * Scene contenant la barre de navigation et l'écran a afficher Cette scène ne change jamais et
     * est le réel conteneur de toute l'application (La racine de l'arbre JFX)
     */
    private static RootScene rootScene;

    /**
     * Conteneur au plus haut niveau de l'application (La fenêtre) Le stage contient la rootScene
     * qui elle contient la barre de navigation et la scene courante (page)
     */
    private static Stage primaryStage;


    /**
     * Charge le routeur
     * 
     * @param defaultRoute route chargée au lancement du routeur
     * @param _primaryStage stage de l'application (La fenêtre dans laquelle lancer le routeur)
     */
    public static void initialize(Routes defaultRoute, Stage _primaryStage, int width, int height) {
        primaryStage = _primaryStage;

        rootScene = new RootScene();
        goToScreen(defaultRoute);

        routes.values().forEach(page -> page.start());
        primaryStage.setScene(new Scene(rootScene, width, height));
        log("Initialized");
    }

    /**
     * Permet de change la scène actuelle (La page courante)
     * 
     * @param newRoute
     */
    public static void goToScreen(Routes newRoute) {
        if (!routes.containsKey(newRoute)) {
            System.err.println("Route not implemented");
            return;
        }
        currentRoute = newRoute;

        // On demande au conteneur de changer sa scène courante (La page qui est chargée)
        rootScene.changeCurrentScene(routes.get(currentRoute));

        // Changement titre de la fenêtre
        primaryStage.setTitle(routes.get(currentRoute).getName());

        log("Switch -> " + newRoute);
    }

    public static Routes getCurrentRoutes() {
        return currentRoute;
    }

    private static void log(String message) {
        System.out.println("[Router] " + message);
    }
}
