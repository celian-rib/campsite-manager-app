package pt4.flotsblancs.scenes.breakpoints;

import java.util.HashSet;
import javafx.stage.Stage;
import lombok.Getter;

public class BreakPointManager {

    /**
     * Liste des élèments ayant besoin d'écouter les événèments de changement de
     * taille de la
     * fenêtre
     */
    private final static HashSet<BreakPointListener> listeners = new HashSet<BreakPointListener>();

    /**
     * Permet d'inscrire un élèment aux changements de taille de la fenêtre
     * 
     * @param listener
     */
    public static void addListener(BreakPointListener listener) {
        listeners.add(listener);
    }

    /**
     * Permet de supprimer un élèment de la liste des listener de changements de
     * taille de la
     * fenêtre
     * 
     * @param listener
     */
    public static void removeListener(BreakPointListener listener) {
        listeners.remove(listener);
    }

    @Getter
    private static VBreakPoint currentVerticalBreakPoint;

    @Getter
    private static HBreakPoint currentHorizontalBreakPoint;

    /**
     * Stage actuel de l'app, donnée à l'initialisation
     */
    private static Stage stage;

    /**
     * Permet d'initialiser le système de gestion des breakpoints
     * 
     * @param stage
     */
    public static void init(Stage stage) {
        BreakPointManager.stage = stage;
        setupPropertiesListener();
    }

    private static void setupPropertiesListener() {
        // Gestion Width (Horizontal)
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            handleWidthProperty(oldVal.intValue(), newVal.intValue());
        });

        // Gestion height (Vertical)
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            handleHeightPorperty(oldVal.intValue(), newVal.intValue());
        });

        stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
            handleWidthProperty((int) stage.getWidth(), (int) stage.getWidth());
            handleHeightPorperty((int) stage.getWidth(), (int) stage.getWidth());
        });
    }

    private static void handleWidthProperty(int oldVal, int newVal) {
        var old = currentHorizontalBreakPoint;
        if (newVal < HBreakPoint.SMALL.getWidth())
            currentHorizontalBreakPoint = HBreakPoint.SMALL;
        else if (newVal < HBreakPoint.MEDIUM.getWidth())
            currentHorizontalBreakPoint = HBreakPoint.MEDIUM;
        else if (newVal < HBreakPoint.LARGE.getWidth())
            currentHorizontalBreakPoint = HBreakPoint.LARGE;
        else
            currentHorizontalBreakPoint = HBreakPoint.NONE;
        if (currentHorizontalBreakPoint != old)
            notifyAllHorizontalBp(old);
    }

    private static void handleHeightPorperty(int oldVal, int newVal) {
        var old = currentVerticalBreakPoint;
        if (newVal < VBreakPoint.SMALL.getHeight())
            currentVerticalBreakPoint = VBreakPoint.SMALL;
        else if (newVal < VBreakPoint.MEDIUM.getHeight())
            currentVerticalBreakPoint = VBreakPoint.MEDIUM;
        else if (newVal < VBreakPoint.LARGE.getHeight())
            currentVerticalBreakPoint = VBreakPoint.LARGE;
        else
            currentVerticalBreakPoint = VBreakPoint.NONE;
        if (currentVerticalBreakPoint != old)
            notifyAllVerticalBp(old);
    }

    private static void notifyAllHorizontalBp(HBreakPoint old) {
        listeners.forEach(l -> l.onHorizontalBreak(old, currentHorizontalBreakPoint));
    }

    private static void notifyAllVerticalBp(VBreakPoint old) {
        listeners.forEach(l -> l.onVerticalBreak(old, currentVerticalBreakPoint));
    }
}
