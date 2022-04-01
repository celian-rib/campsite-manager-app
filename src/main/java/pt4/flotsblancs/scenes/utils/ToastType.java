package pt4.flotsblancs.scenes.utils;

import java.io.InputStream;

import javafx.scene.image.Image;

public enum ToastType {

	// Chemin relatif vers les resources
	ERROR("/ToastIcons/error.png"), SUCCESS("/ToastIcons/success.png"), INFO(
			"/ToastIcons/info.png"), WARNING("/ToastIcons/warning.png");

	private String location;

	ToastType(String location) {
		this.location = location;
	}

	/**
	 * Permet de récupérer l'icône associé à un toast
	 * 
	 * @return icon : L'icône du toast défini dans l'enum
	 */
	public Image getIcon() {
		InputStream inputstream;
		inputstream = getClass().getResourceAsStream(location);
		Image img = new Image(inputstream);
		img.heightProperty();
		img.widthProperty();
		return img;
	}
}
