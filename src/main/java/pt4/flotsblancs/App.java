package pt4.flotsblancs;

import java.sql.SQLException;

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

	public static void main(String[] args) throws SQLException {
		
		JdbcPooledConnectionSource connectionSource
		 = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/test","route","09071998");
		
		TableUtils.createTable(connectionSource, Membre.class);
		
		
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