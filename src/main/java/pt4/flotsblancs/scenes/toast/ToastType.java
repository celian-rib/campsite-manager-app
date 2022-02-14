package pt4.flotsblancs.scenes.toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import io.github.palexdev.materialfx.controls.MFXButton;
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
			img.heightProperty();
			img.widthProperty();
			return img;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
}

/*
 * 
 *         MFXButton warning = new MFXButton("Warning");
        warning.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.WARNING,g,"Message de warning",3000,800));
        
        MFXButton error = new MFXButton("Error");
        error.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.ERROR,g,"Message d'erreur !",3000,800));
        
        MFXButton info = new MFXButton("Info");
        info.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.INFO,g,"Message d'information",3000,800));
        
        MFXButton success = new MFXButton("Success");
        success.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.SUCCESS,g,"Message de succès :)",3000,800));
 * 
 * */
