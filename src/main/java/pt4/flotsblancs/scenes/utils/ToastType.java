package pt4.flotsblancs.scenes.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public enum ToastType 
{
	
	//Chemin relatif vers les resources
	ERROR("src/main/resources/ToastIcons/error.png"),
	SUCCESS("src/main/resources/ToastIcons/success.png"),
	INFO("src/main/resources/ToastIcons/info.png"),
	WARNING("src/main/resources/ToastIcons/warning.png");
	
	private String location;
	
	ToastType(String location)
	{
		this.location = location;
	}
	
	/** Permet de récupérer l'icône associé à un toast
	 * 
	 * @return icon : L'icône du toast défini dans l'enum
	 */
	public Image getIcon()
	{
		FileInputStream inputstream;
		try {
			inputstream = new FileInputStream(location);
			Image img = new Image(inputstream);
			img.heightProperty();
			img.widthProperty();
			return img;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
