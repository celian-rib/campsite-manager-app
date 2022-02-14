package pt4.flotsblancs.router;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import pt4.flotsblancs.components.NavBar;
import pt4.flotsblancs.components.WindowBar;
import pt4.flotsblancs.scenes.utils.Toaster;
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
    
    /**
     * Méthode pour charger les resources CSS.
     */
    private void loadStyleSheets() {
        String baseUrl = "file:src/main/java/pt4/flotsblancs/stylesheets/";
        getStylesheets().add(baseUrl + "index.css");
        getStylesheets().add(baseUrl + "navBar.css");
        getStylesheets().add(baseUrl + "windowBar.css");
    }

    /**
     * Permet de définir la couleur de fond de la fenêtre
     */
    private void initBackground() {
        BackgroundFill fill = new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY);
        setBackground(new Background(fill));
    }

    /**
     * Crée le container qui est séparé de la navbar
     * @return BorderPane : le containeur en question
     */
    private BorderPane createSceneContainer() {
        var container = new BorderPane();
        var fill = new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY);
        container.setBackground(new Background(fill));
        return container;
    }

    /**
     * Permet de changer la scène courante
     * 
     * Si la scène possède un attribut navBar à false et que la navbar avant 
     * transition était présente, alors on retire la navbar (Qui est chargée 
     * pour toute la durée de l'application) de l'affichage.
     * 
     * Au contraire, si la scène possède son attribut navBar à true et que 
     * la navBar n'était pas affichée alors, on va l'afficher
     * 
     * @param baseScene , La nouvelle scène à afficher
     */
    public void changeCurrentScene(IScene baseScene) {
        // Si la nouvelle page n'a pas besoin de la barre de navigation
        if (baseScene.showNavBar() == false && this.navBarIsActive) {
            // On efface la navbar
            rootPane.getChildren().remove(navBar);
            this.navBarIsActive = false;
        } else {
            // On remet la navbar
            rootPane.setLeft(navBar);
            this.navBarIsActive = true;
        }
        
        transition(baseScene);
    }
    
    /**
     * Permet de créer une transition lisse entre deux pages
     *
     * On passe en paramètre la nouvelle scène vers laquelle on va faire 
     * une transition. Et comme on ne change pas la scène courante jusqu'à 
     * la fin de la transition de disparaition de celle-ci, on peut agir
     * sur le sceneContainer qui n'est pas modifié avant la fin de la première transition.
     * 
     * Donc il y a deux transitions, une pour faire disparaître la page courante, et la suivante,
     * celle pour faire apparaître la nouvelle. Après cette dernière transition c'est là
     * que la scène est changée.
     *
     * @param baseScene : La nouvelle scène vers laquelle on va transitionner
     */
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
    
    /*
     * Permet de montrer un toast en calculant automatiquement le temps pendant lequel il sera affiché
     * (à partir du nombre moyen de mots lu par minute par un français)
     * @param type : Type du toast à montrer
     * @param message : Message que le toast contiendra
     * @see showToast(ToastType type, String message, int durationMillis, int fadeinoutMillis)
     */
    public void showToast(ToastType type, String message)
    {
    	//En français moyenne 350 mots par minute
    	//Un mot environ 5 char
    	//1750 caractères à lire par minute, donc le temps =
    	//donc : msg.lenght*60000/1750 = temps à montrer le toast:
    	// 60 000 car en mili secondes :)
    	showToast(type,message,message.length()*60000/1750,1000);
    }
    
    /**
     * Permet de montrer un toast avec le message entrée et la durée 
     * définie dans les paramètres
     * 
     * @param type : Type du toast à montrer
     * @param message : Message que le toast contiendra
     * @param durationMillis : Temps pendant lequel le toast sera affiché 
     * @param fadeinoutMillis : Durée de la transition d'apparition / disparaition
     */
    public void showToast(ToastType type, String message, int durationMillis, int fadeinoutMillis)
    {
    	//Deux borderpane pour placer en bas à droite sinon impossible
    	BorderPane toast = Toaster.createToast(ToastType.INFO, message);
    	BorderPane bottom = new BorderPane();
    	
    	//Ecarte le toast des bords pour une meilleure apparence
    	bottom.setPadding(new Insets(15));
    	toastPane.setBottom(bottom);
    	bottom.setRight(toast);
        
    	Toaster.playTransition(toast,bottom,durationMillis,fadeinoutMillis); 	
    }

}
