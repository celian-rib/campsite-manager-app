package pt4.flotsblancs.scenes.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HBoxSpacer extends Region {
    /**
     * Region JavaFX qui s'etend le plus possible horizontalement
     */
    public HBoxSpacer() {
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
