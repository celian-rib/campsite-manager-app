package pt4.flotsblancs.screens;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import pt4.flotsblancs.App;
import pt4.flotsblancs.router.*;

public class HomeScreen extends FlowPane implements IScreen {

	@Override
	public String getName() {
		return "Accueil";
	}

	@Override
    public void start() {
        // this.prefWidthProperty().bind(App.getInstance().getRoot().widthProperty());
        TextField nameText = new TextField();
		nameText.setText("Ecrire ici");
		nameText.setLayoutX(10);
		nameText.setLayoutY(10);

		Button button = new Button();
		button.setLayoutX(160);
		button.setLayoutY(10);
		button.setText("Super bouton");

		Label greetingLabel = new Label();
		greetingLabel.setLayoutX(10);
		greetingLabel.setLayoutY(40);

		MFXButton buttonBeauGosse = new MFXButton();
		buttonBeauGosse.setLayoutX(250);
		buttonBeauGosse.setLayoutY(50);
		buttonBeauGosse.setButtonType(ButtonType.RAISED);
		buttonBeauGosse.setText("Retour");


		buttonBeauGosse.setOnAction(event -> Router.goToScreen(Routes.LAUNCHING));
        this.getChildren().addAll(nameText,button,greetingLabel, buttonBeauGosse);
    }
}
