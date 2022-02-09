package pt4.flotsblancs.screens.toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public enum ToastType 
{
	
	ERROR("src/main/resources/ToastIcons/error.png"),
	SUCCESS("src/main/resources/ToastIcons/success.png"),
	INFO("src/main/resources/ToastIcons/info.png"),
	WARNING("src/main/resources/ToastIcons/warning.png");
	
	private String location;
	
	ToastType(String location)
	{
		this.location = location;
	}
	
	public Image getIcon()
	{
		FileInputStream inputstream;
		try {
			inputstream = new FileInputStream(location);
			Image img = new Image(inputstream);
			return img;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
