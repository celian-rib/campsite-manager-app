package pt4.flotsblancs.screens;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import pt4.flotsblancs.router.*;

public class HomeScreen extends GridPane implements IScreen {

	@Override
	public String getName() {
		return "Accueil";
	}

	@Override
    public void start() {
		this.setAlignment(Pos.CENTER);
		this.setVgap(20);
		this.setPadding(new Insets(25, 25, 25, 25));

		MFXButton addClientButton = new MFXButton("Page ajout client", 200, 40);
		addClientButton.setStyle("-fx-background-color: #5fc9fa; -fx-text-fill: #ffffff; -fx-font-size: 20");
		addClientButton.setButtonType(ButtonType.RAISED);
		addClientButton.setOnAction(event -> Router.goToScreen(Routes.ADD_CLIENT));
		this.add(addClientButton, 1, 1);
		
		MFXButton otherScreenButton = new MFXButton("Page jsp", 200, 40);
		otherScreenButton.setStyle("-fx-background-color: #5fc9fa; -fx-text-fill: #ffffff; -fx-font-size: 20");
		otherScreenButton.setButtonType(ButtonType.RAISED);
		otherScreenButton.setOnAction(event -> Router.goToScreen(Routes.LAUNCHING));
		this.add(otherScreenButton, 1, 2);

    }
}
