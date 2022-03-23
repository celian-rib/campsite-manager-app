package pt4.flotsblancs.scenes.components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FlotsBlancsLogo extends VBox {

    /**
     * Permet de créer une Node contenant le logo et le titre de l'application
     * 
     * @param isDark vrai si le logo et titre doivent être noir
     * @param showTitle vrai si l'on veut afficher le titre
     * @param size taille du logo
     */
    public FlotsBlancsLogo(boolean isDark, boolean showTitle, int size) {
        setSpacing(10);
        ImageView icon = new ImageView();

        InputStream stream = getClass().getResourceAsStream(isDark ? "/logo_dark.png" : "/logo.png");
		
		Image img = new Image(stream);
		img.heightProperty();
		img.widthProperty();
		icon.setFitHeight(size);
		icon.setFitWidth(size);
		icon.setImage(img);

        setAlignment(Pos.CENTER);
        setPadding(new Insets(30, 0, 0, 0));

        Label title = new javafx.scene.control.Label("Les Flots Blancs");
        title.setTextFill(isDark ? Color.BLACK : Color.WHITE);
        getChildren().add(icon);
        if (showTitle)
            getChildren().add(title);
    }
}
