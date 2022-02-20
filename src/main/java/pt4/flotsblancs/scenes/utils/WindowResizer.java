package pt4.flotsblancs.scenes.utils;

import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowResizer {

    private final HashMap<Cursor, EventHandler<MouseEvent>> dragActions = new HashMap<>();
    private final Scene rootScene;

    private double mousePressedSceneX, mousePressedSceneY;
    private double mousePressedScreenX, mousePressedScreenY;
    private double mousePressedStageWidth, mousePressedStageHeight;

    /**
     * Permet de créer une instance de WindowResizer qui se chargera de rendre possible le
     * changement de taille pat l'utilisateur du stage donnée
     * 
     * @param stage stage concerné
     * @param windowEdgeDistance distance avec les bords de la fenêtre sur lequels il devient
     *        possible de changer la taille de la fenêtre
     */
    public WindowResizer(Stage stage, int windowEdgeDistance) {
        this.rootScene = stage.getScene();

        // On stock les infos de position de la souris au moment ou elle est "drag"
        rootScene.setOnMousePressed(event -> {
            mousePressedSceneX = event.getSceneX();
            mousePressedSceneY = event.getSceneY();

            mousePressedScreenX = event.getScreenX();
            mousePressedScreenY = event.getScreenY();

            mousePressedStageWidth = stage.getWidth();
            mousePressedStageHeight = stage.getHeight();
        });

        // Enregistremet des différents actions qui correspondent aux différents types de "drag"
        dragActions.put(Cursor.NW_RESIZE, event -> {
            double width = mousePressedStageWidth - (event.getScreenX() - mousePressedScreenX);
            double height = mousePressedStageHeight - (event.getScreenY() - mousePressedScreenY);
            if (height > stage.getMinHeight()) {
                stage.setY(event.getScreenY() - mousePressedSceneY);
                stage.setHeight(height);
            }
            if (width > stage.getMinWidth()) {
                stage.setX(event.getScreenX() - mousePressedSceneX);
                stage.setWidth(width);
            }
        });

        dragActions.put(Cursor.NE_RESIZE, event -> {
            double width = mousePressedStageWidth - (event.getScreenX() - mousePressedScreenX);
            double height = mousePressedStageHeight + (event.getScreenY() - mousePressedScreenY);
            if (height > stage.getMinHeight())
                stage.setHeight(height);
            if (width > stage.getMinWidth()) {
                stage.setX(event.getScreenX() - mousePressedSceneX);
                stage.setWidth(width);
            }
        });

        dragActions.put(Cursor.SW_RESIZE, event -> {
            double width = mousePressedStageWidth + (event.getScreenX() - mousePressedScreenX);
            double height = mousePressedStageHeight - (event.getScreenY() - mousePressedScreenY);
            if (height > stage.getMinHeight()) {
                stage.setHeight(height);
                stage.setY(event.getScreenY() - mousePressedSceneY);
            }
            if (width > stage.getMinWidth())
                stage.setWidth(width);
        });

        dragActions.put(Cursor.SE_RESIZE, event -> {
            double width = mousePressedStageWidth + (event.getScreenX() - mousePressedScreenX);
            double height = mousePressedStageHeight + (event.getScreenY() - mousePressedScreenY);
            if (height > stage.getMinHeight())
                stage.setHeight(height);
            if (width > stage.getMinWidth())
                stage.setWidth(width);
        });

        dragActions.put(Cursor.E_RESIZE, event -> {
            double width = mousePressedStageWidth - (event.getScreenX() - mousePressedScreenX);
            if (width > stage.getMinWidth()) {
                stage.setX(event.getScreenX() - mousePressedSceneX);
                stage.setWidth(width);
            }
        });

        dragActions.put(Cursor.W_RESIZE, event -> {
            double width = mousePressedStageWidth + (event.getScreenX() - mousePressedScreenX);
            if (width > stage.getMinWidth())
                stage.setWidth(width);
        });

        dragActions.put(Cursor.S_RESIZE, event -> {
            double height = mousePressedStageHeight + (event.getScreenY() - mousePressedScreenY);
            if (height > stage.getMinHeight())
                stage.setHeight(height);
        });

        dragActions.put(Cursor.OPEN_HAND, event -> {
            stage.setX(event.getScreenX() - mousePressedSceneX);
            stage.setY(event.getScreenY() - mousePressedSceneY);
        });

        rootScene.setOnMouseMoved(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            boolean cursorIsInLeftZone = mouseX > 0 && mouseX < windowEdgeDistance;
            boolean cursorIsInRightZone = mouseX < rootScene.getWidth()
                    && mouseX > rootScene.getWidth() - windowEdgeDistance;
            boolean cursorIsInTopZone = mouseY < rootScene.getHeight()
                    && mouseY > rootScene.getHeight() - windowEdgeDistance;
            boolean cursorIsInBottomZone = mouseY > 0 && mouseY < windowEdgeDistance;

            if (cursorIsInLeftZone && cursorIsInBottomZone)
                updateSceneAppearence(Cursor.NW_RESIZE);
            else if (cursorIsInLeftZone && cursorIsInTopZone)
                updateSceneAppearence(Cursor.NE_RESIZE);
            else if (cursorIsInRightZone && cursorIsInBottomZone)
                updateSceneAppearence(Cursor.SW_RESIZE);
            else if (cursorIsInRightZone && cursorIsInTopZone)
                updateSceneAppearence(Cursor.SE_RESIZE);
            else if (cursorIsInLeftZone)
                updateSceneAppearence(Cursor.E_RESIZE);
            else if (cursorIsInRightZone)
                updateSceneAppearence(Cursor.W_RESIZE);
            else if (cursorIsInBottomZone)
                updateSceneAppearence(Cursor.OPEN_HAND);
            else if (cursorIsInTopZone)
                updateSceneAppearence(Cursor.S_RESIZE);
            else
                updateSceneAppearence(Cursor.DEFAULT);
        });
    }

    /**
     * Change le type de curseur et l'action réalisée sur la fenêtre si la souris est "draggée"
     * 
     * @param c curseur actuellemnt utilisé (Le type de curseur donne la direction de resize)
     */
    private void updateSceneAppearence(Cursor c) {
        rootScene.setCursor(c);
        rootScene.setOnMouseDragged(c == Cursor.DEFAULT ? null : dragActions.get(c));
    }
}
