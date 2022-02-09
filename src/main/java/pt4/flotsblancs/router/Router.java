package pt4.flotsblancs.router;

import java.util.HashMap;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt4.flotsblancs.screens.AddClientScreen;
import pt4.flotsblancs.screens.HomeScreen;
import pt4.flotsblancs.screens.LaunchingScreen;
import pt4.flotsblancs.screens.toast.ToastBuilder;
import pt4.flotsblancs.screens.toast.ToastType;

public class Router {

    public final HashMap<Routes, IScreen> routes;
    private Routes currentRoute;
    private Stage primaryStage;

    private Scene rootJFXScene;
    private IScreen currentScene;

    private static Router INSTANCE;

    public Router(Routes defaultRoute, Stage primaryStage) {
        Router.INSTANCE = this;
        this.primaryStage = primaryStage;

        this.routes = new HashMap<>();
        this.routes.put(Routes.HOME, new HomeScreen());
        this.routes.put(Routes.LAUNCHING, new LaunchingScreen());
        this.routes.put(Routes.ADD_CLIENT, new AddClientScreen());

        goToScreen(defaultRoute);

        this.rootJFXScene = new Scene((Parent) this.currentScene, 800, 700);

        routes.values().forEach(page -> page.start());
        primaryStage.setScene(this.rootJFXScene);
    }

    public static void goToScreen(Routes newRoute) {
        INSTANCE.loadScreen(newRoute);
    }

    public void loadScreen(Routes newRoute) {
        if (!routes.containsKey(newRoute)) {
            System.err.println("Route not implemented");
            return;
        }

        this.currentRoute = newRoute;
        this.currentScene = routes.get(newRoute);

        primaryStage.setTitle(this.currentScene.getName());
     
        
        

        if (this.rootJFXScene != null) {
            this.rootJFXScene.setRoot((Parent) this.currentScene);
            ToastBuilder.createToast(ToastType.ERROR,primaryStage,"Le compte n'a pas été reconnu dans la base de données ! Veuillez contacter un administrateur !",3000,800);
        }
    }

    public Routes getCurrentRoutes() {
        return this.currentRoute;
    }
}
