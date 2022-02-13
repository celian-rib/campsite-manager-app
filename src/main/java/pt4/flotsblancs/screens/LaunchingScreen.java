package pt4.flotsblancs.screens;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pt4.flotsblancs.router.IScreen;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Routes;
import pt4.flotsblancs.screens.toast.ToastBuilder;
import pt4.flotsblancs.screens.toast.ToastType;

public class LaunchingScreen extends VBox implements IScreen {

    @Override
    public String getName() {
        return "Lancement";
    }

	@Override
    public void start() {
        // this.prefWidthProperty().bind(App.getInstance().getRoot().widthProperty());
        Text helloText = new Text("Welcome to MyCamping");
        MFXButton myButton = new MFXButton("go to home");
        myButton.setOnAction((arg0) -> Router.goToScreen(Routes.HOME));
        
        
        Group g = new Group();
        
        MFXButton warning = new MFXButton("Warning");
        warning.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.WARNING,g,"Message de warning",3000,800));
        
        MFXButton error = new MFXButton("Error");
        error.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.ERROR,g,"Message d'erreur !",3000,800));
        
        MFXButton info = new MFXButton("Info");
        info.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.INFO,g,"Message d'information",3000,800));
        
        MFXButton success = new MFXButton("Success");
        success.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.SUCCESS,g,"Message de succès :)",3000,800));
        
        
        this.getChildren().addAll(helloText, myButton, warning, error, info, success); 
    }
}
