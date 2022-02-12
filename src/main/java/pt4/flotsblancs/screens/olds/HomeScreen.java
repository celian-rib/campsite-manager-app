package pt4.flotsblancs.screens.olds;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import pt4.flotsblancs.router.*;

public class HomeScreen extends BaseScene {

	@Override
	public String getName() {
		return "Accueil";
	}

	@Override
	public void start() {
		// MenuBar menuBar = new MenuBar();
		// Menu menu1 = new Menu("Menu 1");
		// menuBar.getMenus().add(menu1);

		// GridPane pane = new GridPane();

		// pane.add(menuBar, 0, 0);

		// pane.setAlignment(Pos.CENTER);
		// pane.setVgap(20);
		// pane.setPadding(new Insets(25, 25, 25, 25));

		// MFXButton addClientButton = new MFXButton("Page ajout client", 200, 40);
		// addClientButton.setStyle(
		// 		"-fx-background-color: #5fc9fa; -fx-text-fill: #ffffff; -fx-font-size: 20");
		// addClientButton.setButtonType(ButtonType.RAISED);
		// addClientButton.setOnAction(event -> Router.goToScreen(Routes.ADD_CLIENT));
		// pane.add(addClientButton, 1, 1);

		// MFXButton otherScreenButton = new MFXButton("Page jsp", 200, 40);
		// otherScreenButton.setStyle(
		// 		"-fx-background-color: #5fc9fa; -fx-text-fill: #ffffff; -fx-font-size: 20");
		// otherScreenButton.setButtonType(ButtonType.RAISED);
		// otherScreenButton.setOnAction(event -> Router.goToScreen(Routes.LAUNCHING));
		// pane.add(otherScreenButton, 1, 2);

		// addAll(pane);
	}
}
