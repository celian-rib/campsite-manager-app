package pt4.flotsblancs.screens;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pt4.flotsblancs.router.*;

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
        this.getChildren().addAll(helloText, myButton);
    }
}
