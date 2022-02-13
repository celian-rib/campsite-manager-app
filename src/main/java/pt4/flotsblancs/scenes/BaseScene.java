package pt4.flotsblancs.scenes;

import javafx.scene.Group;
import javafx.scene.Node;

public abstract class BaseScene extends Group {

    protected boolean showNavBar = true;

    /**
     * Permet de récupérer le nom de cette scène (Page). Ce nom sera affiché en haut de l'écran
     * 
     * @return
     */
    public abstract String getName();

    /**
     * Appelé au moment ou cette scène est affichée à l'écran par le routeur
     */
    public abstract void start();

    /**
     * Permet d'ajouter un ou plusieurs élèments à ces pages (Ajouter des enfants à ce noeud)
     * 
     * @param nodes
     */
    protected void addAll(Node... nodes) {
        this.getChildren().addAll(nodes);
    }

    /**
     * @return cette page autorise l'affchage de la barre de navigation ou non
     */
    public boolean showNavBar() {
        return this.showNavBar;
    }
}
