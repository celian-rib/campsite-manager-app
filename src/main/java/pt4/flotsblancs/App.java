package pt4.flotsblancs;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

import io.github.cdimascio.dotenv.Dotenv;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {

		try {
			Dotenv dotenv = Dotenv.load();;

			var connectionSource = new JdbcPooledConnectionSource(dotenv.get("DB_URL"),
					dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"));

			TableUtils.createTableIfNotExists(connectionSource, Membre.class);

			// instantiate the DAO to handle Account with String id
			Dao<Membre, String> accountDao = DaoManager.createDao(connectionSource, Membre.class);

			// Création d'un membre
			Membre membre = new Membre("Michel");
			accountDao.create(membre); // Pour le persist dans la db

			// On veut récupérer michel dans la db (faire une requête)
			Membre fetchedMembre = accountDao.queryForId("Michel");

			System.out.println(fetchedMembre);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// Lancer JFX
		try {
			launch(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
		root.getChildren().addAll(nameText, button, greetingLabel, buttonBeauGosse);

		primaryStage.setTitle("Camping !");
		primaryStage.setScene(new Scene(root, 950, 800));
		primaryStage.show();
	}
}
