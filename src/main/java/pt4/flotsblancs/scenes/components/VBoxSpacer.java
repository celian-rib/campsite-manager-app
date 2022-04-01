package pt4.flotsblancs.scenes.components;

import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class VBoxSpacer extends Region {
    /**
     * Region JavaFX qui s'etend le plus possible verticalement
     */
    public VBoxSpacer() {
        VBox.setVgrow(this, Priority.ALWAYS);
    }
}
