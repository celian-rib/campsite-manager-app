package pt4.flotsblancs;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
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
		buttonBeauGosse.setText("Gros BG");


		button.setOnAction(event -> greetingLabel.setText("Hello " + nameText.getText() + "!"));

		Group root = new Group();
		root.getChildren().addAll(nameText, button, greetingLabel,buttonBeauGosse);

		primaryStage.setTitle("Camping !");
		primaryStage.setScene(new Scene(root, 950, 800));
		primaryStage.show();
	}
}