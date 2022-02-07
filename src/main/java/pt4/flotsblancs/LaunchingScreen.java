package pt4.flotsblancs;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LaunchingScreen extends VBox implements FBScreen {
    public static AvailableRoutes ROUTE = AvailableRoutes.LAUNCHING;

    private App appInstance;
    
    public LaunchingScreen(App appInstance) {
        super();
        this.appInstance = appInstance;
    }

    public void initialize() {
        this.prefWidthProperty().bind(appInstance.getRoot().widthProperty());
        Text helloText = new Text("Welcome to MyCamping");
        MFXButton myButton = new MFXButton("go to home");
        myButton.setOnAction((arg0) -> App.goToScreen(AvailableRoutes.HOME));
        this.getChildren().addAll(helloText, myButton);
    }
}
