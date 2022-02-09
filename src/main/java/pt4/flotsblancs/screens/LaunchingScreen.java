package pt4.flotsblancs.screens;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pt4.flotsblancs.router.*;
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
        
        
        Router r = Router.INSTANCE;
        Stage s = r.getStage();
        
        MFXButton warning = new MFXButton("Warning");
        warning.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.WARNING,s,"Message de warning",3000,800));
        
        MFXButton error = new MFXButton("Error");
        error.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.ERROR,s,"Message d'erreur !",3000,800));
        
        MFXButton info = new MFXButton("Info");
        info.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.INFO,s,"Message d'information",3000,800));
        
        MFXButton success = new MFXButton("Success");
        success.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.SUCCESS,s,"Message de succès :)",3000,800));
        
        
        this.getChildren().addAll(helloText, myButton, warning, error, info, success); 
    }
}
