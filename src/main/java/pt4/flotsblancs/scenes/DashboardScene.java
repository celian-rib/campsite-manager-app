package pt4.flotsblancs.scenes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.router.IScene;

public class DashboardScene extends BorderPane implements IScene {

    @Override
    public String getName() {
        return "Accueil";
    }

    @Override
    public boolean showNavBar() {
        return true;
    }

    @Override
    public void onFocus() {
        
    }

    @Override
    public void onUnfocus() {
        
    }

    @Override
    public void start() 
    {
    	
    	BorderPane stats = new BorderPane();
    	
    	VBox left = new VBox();
    	VBox right = new VBox();
    	
    	Node title = new Label("bonjour");
    	 
    	
    	left.getChildren().add(title);
    	
    	stats.setLeft(left);
    	stats.setRight(right);

        this.setTop(null);
        this.setBottom(stats);
    }
}
