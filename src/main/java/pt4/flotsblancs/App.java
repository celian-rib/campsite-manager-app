package pt4.flotsblancs;

import pt4.flotsblancs.orm.*;
import java.sql.SQLException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		// Lancer JFX
		try {
			Database.getInstance(); // Initialisation connexion BD
			launch(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	Label clientsLabel;

	@Override
	public void start(Stage primaryStage) throws SQLException {
		TextField firstText = new TextField();
		firstText.setText("Prénom du client à ajouter");
		firstText.setLayoutX(130);
		firstText.setLayoutY(70);
		TextField nameText = new TextField();
		nameText.setText("Nom du client à ajouter");
		nameText.setLayoutX(130);
		nameText.setLayoutY(100);

		MFXButton addButton = new MFXButton();
		addButton.setButtonType(ButtonType.RAISED);
		addButton.setLayoutX(130);
		addButton.setLayoutY(130);

		clientsLabel = new Label();
		clientsLabel.setLayoutX(130);
		clientsLabel.setLayoutY(200);

		addButton.setOnAction(event -> {
			System.out.println("Ajout du client");
			Client client = new Client(firstText.getText(), nameText.getText());
			try {
				Database.getInstance().getClientsDao().create(client);
				refreshClientsList();
			} catch (SQLException e) {
			}
		});

		Group root = new Group();
		root.getChildren().addAll(firstText, nameText, addButton, clientsLabel);

		primaryStage.setTitle("Camping !");
		primaryStage.setScene(new Scene(root, 950, 800));
		primaryStage.show();

		refreshClientsList();
	}

	private void refreshClientsList() throws SQLException {
		clientsLabel.setText("");
		for (Client c : Database.getInstance().getClientsDao().queryForAll()) {
			clientsLabel.setText(clientsLabel.getText() + "\n" + c.toString());
		}
	}
}
