package pt4.flotsblancs.screens.olds;



import java.sql.SQLException;
import java.util.List;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pt4.flotsblancs.orm.model.Client;
import pt4.flotsblancs.orm.Database;
import pt4.flotsblancs.router.BaseScene;
import pt4.flotsblancs.router.IScreen;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Routes;

public class AddClientScreen extends BaseScene {

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
		super.start(); // Affiche barre de navigation

		QueryBuilder<Client, String> queryBuilder;
		try {
			queryBuilder = Database.getInstance().getClientsDao().queryBuilder();
			queryBuilder.where().eq("first_name", "Emilien");
			PreparedQuery<Client> preparedQuery = queryBuilder.prepare();
			List<Client> accountList = Database.getInstance().getClientsDao().query(preparedQuery);
	
			for (Client c : accountList) {
				System.out.println(c.toString());
				System.out.println("-------");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

		MFXButton myButton = new MFXButton("go to home");
        myButton.setOnAction((arg0) -> Router.goToScreen(Routes.HOME));

		this.getChildren().addAll(firstText, nameText, addButton, clientsLabel, myButton);
    }

	
}
