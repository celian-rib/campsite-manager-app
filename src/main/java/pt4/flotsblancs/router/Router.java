package pt4.flotsblancs.router;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import pt4.flotsblancs.scenes.*;
import pt4.flotsblancs.scenes.utils.ToastType;

public class Router {

    public enum Routes {
        CONN_FALLBACK, CLIENTS, RESERVATIONS, LOGIN, HOME, STOCKS, CAMPGROUNDS, ADMIN
    }

    /**
     * Liste des routes disponibles dans l'application
     */
    public static final HashMap<Routes, IScene> routes = new HashMap<>() {
        {
            put(Routes.CONN_FALLBACK, new ConnectionFallbackScene());
            put(Routes.HOME, new DashboardScene());
            put(Routes.LOGIN, new LoginScene());
            put(Routes.RESERVATIONS, new ReservationsScene());
            put(Routes.CLIENTS, new ClientsScene());
            put(Routes.STOCKS, (IScene) new StocksScene());
            put(Routes.CAMPGROUNDS, new CampgroundsScene());
            put(Routes.ADMIN, new AdminScene());
        }
    };

    /**
     * Route actuelle affichée sur le "Stage" de l'application
     */
    @Getter
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
    @Getter
    private static Stage primaryStage;

    /**
     * @return La scène actuellement affichée par le routeur
     */
    public static IScene getCurrentScene() {
        return routes.get(currentRoute);
    }

    /**
     * Charge le routeur
     * 
     * @param defaultRoute route chargée au lancement du routeur
     * @param _primaryStage stage de l'application (La fenêtre dans laquelle lancer le routeur)
     */
    public static void initialize(Routes defaultRoute, Stage _primaryStage, int width, int height) {
        primaryStage = _primaryStage;

        rootScene = new RootScene();
        routes.values().forEach(page -> page.start());

        goToScreen(defaultRoute);

        var scene = new Scene(rootScene, width, height);
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        log("Initialized");
    }

    /**
     * Permet d'envoyer un signal à la root scène pour délencher l'affichage d'un toast
     * 
     * 
     * @param type
     * @param message
     * @param durationMillis
     * @param fadeinoutMillis
     */
    public static void showToast(ToastType type, String message, int durationMillis,
            int fadeinoutMillis) {
        rootScene.showToast(type, message, durationMillis, fadeinoutMillis);
    }

    /**
     * Permet d'envoyer un signal à la root scène pour délencher l'affichage d'un toast
     * 
     * @param type
     * @param message
     */
    public static void showToast(ToastType type, String message) {
        rootScene.showToast(type, message);
    }

    /**
     * Permet de change la scène actuelle (La page courante)
     * 
     * @param newRoute
     */
    public static void goToScreen(Routes newRoute) {
        if (!routes.containsKey(newRoute)) {
            log("Route not implemented");
            return;
        }

        // On prévient l'ancienne page qu'elle ne va plus être affiché
        if (currentRoute != null)
            routes.get(currentRoute).onUnfocus();

        currentRoute = newRoute;

        // On demande au conteneur de changer sa scène courante (La page qui est chargée)
        rootScene.changeCurrentScene(routes.get(currentRoute));

        // Changement titre de la fenêtre
        primaryStage.setTitle(routes.get(currentRoute).getName());

        // On prévient la nouvelle page qu'elle vient d'être affichée
        routes.get(currentRoute).onFocus();

        log("Switch scene -> " + newRoute);
    }

    /**
     * Permet de logger les actions du routeur pour le débug
     * 
     * @param message : Message de débug
     */
    private static void log(String message) {
        System.out.println("[Router] " + message);
    }
}
