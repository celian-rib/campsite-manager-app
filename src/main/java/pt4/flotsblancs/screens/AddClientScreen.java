package pt4.flotsblancs.screens;



import java.sql.SQLException;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pt4.flotsblancs.orm.Client;
import pt4.flotsblancs.orm.Database;
import pt4.flotsblancs.router.IScreen;

public class AddClientScreen extends Group implements IScreen {

	@Override
	public String getName() {
		return "Ajouter un client";
	}

	Label clientsLabel;

    private void refreshClientsList() throws SQLException {
		clientsLabel.setText("");
		for (Client c : Database.getInstance().getClientsDao().queryForAll()) {
			clientsLabel.setText(clientsLabel.getText() + "\n" + c.toString());
        }
    }

	@Override
    public void start() {
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
				System.out.println(e.getMessage());
			}
		});
		try {
			refreshClientsList();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.getChildren().addAll(firstText, nameText, addButton, clientsLabel);
    }

	
}
