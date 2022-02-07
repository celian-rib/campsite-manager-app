package pt4.flotsblancs;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class App extends Application {

	private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/pt4";

	public static void main(String[] args) {

		try {
			JdbcPooledConnectionSource connectionSource =
					new JdbcPooledConnectionSource(DATABASE_URL, "root", "root");

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

		button.setOnAction(event -> greetingLabel.setText("Hello " + nameText.getText() + "!"));

		Group root = new Group();
		root.getChildren().addAll(nameText, button, greetingLabel);

		primaryStage.setTitle("Camping !");
		primaryStage.setScene(new Scene(root, 950, 800));
		primaryStage.show();
	}
}
