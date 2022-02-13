package pt4.flotsblancs.router;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import pt4.flotsblancs.components.NavBar;
import pt4.flotsblancs.components.WindowBar;
import pt4.flotsblancs.scenes.BaseScene;

public class RootScene extends BorderPane {

    private BaseScene currentScene;

    private boolean navBarIsActive;


    private BorderPane sceneContainer;
    private NavBar navBar;
    private WindowBar windowBar;


    public RootScene() {
        initBackground();
        loadStyleSheets();

        navBar = new NavBar();
        sceneContainer = createSceneContainer();
        windowBar = new WindowBar();

        sceneContainer.setTop(windowBar);
        setLeft(navBar);
        setCenter(sceneContainer);
        this.navBarIsActive = true;
    }

    private void loadStyleSheets() {
        String baseUrl = "file:src/main/java/pt4/flotsblancs/stylesheets/";
        getStylesheets().add(baseUrl + "index.css");
        getStylesheets().add(baseUrl + "navBar.css");
        getStylesheets().add(baseUrl + "windowBar.css");
    }

    private void initBackground() {
        BackgroundFill fill = new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY);
        setBackground(new Background(fill));
    }

    private BorderPane createSceneContainer() {
        var container = new BorderPane();
        var fill = new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY);
        container.setBackground(new Background(fill));
        return container;
    }

    public void changeCurrentScene(BaseScene baseScene) {
        // Si la nouvelle page n'a pas besoin de la barre de navigation
        if (baseScene.showNavBar() == false && this.navBarIsActive) {
            // On efface la navbar
            this.getChildren().remove(navBar);
            this.navBarIsActive = false;
        } else {
            // On remet la navbar
            setLeft(navBar);
            this.navBarIsActive = true;
        }

        this.currentScene = baseScene;
        sceneContainer.setCenter(this.currentScene);
        windowBar.setTitle(this.currentScene.getName());
    }
}
