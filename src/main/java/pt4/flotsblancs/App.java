package pt4.flotsblancs;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	int screenXsize = 950;// cela sera utile pour centrer les objets
	int screenYsize = 800;

	@Override
	public void start(Stage primaryStage) {
		TextField nameText = new TextField();
		nameText.setText("Ecrire ici");
		nameText.setLayoutX(10);
		nameText.setLayoutY(10);

		TextField idText = new TextField();// une zone pour écrire du texte
		idText.setText("identifiant");// si vous voulez mettre un texte de base dans la zone au lancement
		idText.setPromptText("identifiant");// mettre du texte gris en fond
		idText.setLayoutX(screenXsize / 2);// par défaut vaut 0
		idText.setLayoutY(320);// par défaut vaut 0

		PasswordField passwText = new PasswordField();// une zone de texte qui cache son contenu avec des puces
		passwText.setPromptText("Ecrire ici");// mettre du texte gris en fond
		passwText.setLayoutX(screenXsize / 2);// par défaut vaut 0
		passwText.setLayoutY(360);// par défaut vaut 0

		Button button = new Button();// un bouton
		button.setLayoutX(160);// par défaut vaut 0
		button.setLayoutY(10);// par défaut vaut 0
		button.setText("Super bouton");// le texte du bouton

		Button connexion = new Button();
		connexion.setLayoutX(screenXsize / 2);
		connexion.setLayoutY(400);
		connexion.setText("connexion");
		connexion.setOnAction(new EventHandler<ActionEvent>() {
			// si vous voulez faire un event qui s'éxecute lorsque le bouton est pressé
			@Override
			public void handle(ActionEvent event) {

				Label labelFenêtre2 = new Label("vous êtes connecté");

				StackPane secondaryLayout = new StackPane();// sert à cacher ou montrer des objets
				secondaryLayout.getChildren().add(labelFenêtre2);

				Scene secondPage = new Scene(secondaryLayout, 430, 100);

				// nouvelle fenêtre
				Stage newWindow = new Stage();
				newWindow.setTitle("deuxième page qui marche !");// nom de la page
				newWindow.setScene(secondPage);

				// position par rapport à la fenêtre parente
				newWindow.setX(primaryStage.getX() + 200);
				newWindow.setY(primaryStage.getY() + 100);

				Button exempleStackPane = new Button();//un autre bouton mais je n'ai pas fini
				exempleStackPane.setLayoutX(screenXsize / 2);
				exempleStackPane.setLayoutY(400);
				exempleStackPane.setText("exemple de stackPane");
				exempleStackPane.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						
						
						
						newWindow.show();
					}
				});

				newWindow.show();
			}
		});

		Label greetingLabel = new Label();// mettre une etiquette non modifiable sur la page
		greetingLabel.setLayoutX(10);
		greetingLabel.setLayoutY(40);
		// event si le bouton est pressé, change le texte:
		button.setOnAction(event -> greetingLabel.setText("Hello " + nameText.getText() + "!"));

		Group root = new Group();// il faut entrer les noms des objet créé pour les afficher ci-dessous
		root.getChildren().addAll(nameText, button, greetingLabel, connexion, passwText, idText);

		primaryStage.setTitle("tuto Gaël !");// nom de la fenêtre
		primaryStage.setScene(new Scene(root, screenXsize, screenYsize));// taille de la fenêtre
		primaryStage.show();// affiche la fenêtre(hide pour la cacher mais la laisser tourner, attention à
							// ne pas la perdre)
	}
}