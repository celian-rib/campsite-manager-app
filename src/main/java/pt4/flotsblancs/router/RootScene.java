package pt4.flotsblancs.router;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt4.flotsblancs.components.NavBar;
import pt4.flotsblancs.components.WindowBar;
import pt4.flotsblancs.scenes.utils.ToastBuilder;
import pt4.flotsblancs.scenes.utils.ToastType;
import pt4.flotsblancs.scenes.utils.TransitionBuilder;

public class RootScene extends StackPane {

    private IScene currentScene;

    private boolean navBarIsActive;

    private BorderPane rootPane;
    private BorderPane toastPane;

    private BorderPane sceneContainer;
    private NavBar navBar;
    private WindowBar windowBar;
    
    


    public RootScene() {
        initBackground();
        loadStyleSheets();

        navBar = new NavBar();
        
        // Initialisation du panneau latéral droit = Scene container
        rootPane = new BorderPane();
        this.getChildren().add(rootPane);
        
        // Initialisation du panneau contenant les toast
        toastPane = new BorderPane();
        toastPane.mouseTransparentProperty().set(true);
        this.getChildren().add(toastPane);
        
        sceneContainer = createSceneContainer();
        windowBar = new WindowBar();

        sceneContainer.setTop(windowBar);
        
        rootPane.setLeft(navBar);
        rootPane.setCenter(sceneContainer);
        
        //setBottom();
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

    public void changeCurrentScene(IScene baseScene) {
        // Si la nouvelle page n'a pas besoin de la barre de navigation
        if (baseScene.showNavBar() == false && this.navBarIsActive) {
            // On efface la navbar
            this.getChildren().remove(navBar);
            this.navBarIsActive = false;
        } else {
            // On remet la navbar
            rootPane.setLeft(navBar);
            this.navBarIsActive = true;
        }
        
        transition(baseScene);
    }
    
    public void transition(IScene baseScene)
    {
        FadeTransition popOut = TransitionBuilder.fadeTransition(1.0f, 0.0f, 250, sceneContainer);
        popOut.play();
        popOut.setOnFinished((ae) -> 
        {
            new Thread(() -> {

            	Platform.runLater(new Runnable() {
            	    @Override
            	    public void run() {
	            		currentScene = baseScene;      
	                    sceneContainer.setCenter((Parent) currentScene);
	                    windowBar.setTitle(currentScene.getName());
            	    }
            	});
            	FadeTransition popIn = TransitionBuilder.fadeTransition(0.0f, 1.0f, 250, sceneContainer);
                popIn.play();            
            }).start();
        });
        popOut.play();
    }
    
    public void showToast(ToastType type, String message)
    {
    	//En français moyenne 350 mots par minute
    	//Un mot environ 5 char
    	//1750 caractères à lire par minute, donc le temps =
    	//donc : msg.lenght*60000/1750 = temps à montrer le toast:
    	// 60 000 car en mili secondes :)
    	showToast(type,message,message.length()*60000/1750,1000);
    }
    
    public void showToast(ToastType type, String message, int durationMillis, int fadeinoutMillis)
    {
    	//Deux borderpane pour placer en bas à droite sinon impossible
    	BorderPane toast = ToastBuilder.createToast(ToastType.INFO, message);
    	BorderPane bottom = new BorderPane();
    	
    	//Ecarte le toast des bords pour une meilleure apparence
    	bottom.setPadding(new Insets(15));
    	toastPane.setBottom(bottom);
    	bottom.setRight(toast);
        
    	ToastBuilder.playTransition(toast,bottom,durationMillis,fadeinoutMillis); 	
    }

}
