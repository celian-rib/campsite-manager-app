package pt4.flotsblancs.scenes;

import com.github.javafaker.Stock;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.router.IScene;

public class StocksScene extends VBox {

    public String getName() {
        return "Stocks";
    }

    public boolean showNavBar() {
        return true;
    }

    public void onFocus() {
        TableView<Stock> table = new TableView<Stock>();

      // Create column UserName (Data type of String).
      TableColumn<Stock, String> itemCol //
              = new TableColumn<Stock, String>("Objet");

      // Create column Email (Data type of String).
      TableColumn<Stock, Integer> quantityCol//
              = new TableColumn<Stock, Integer>("Quantité");

      // Create column FullName (Data type of String).
      TableColumn<Stock, String> storageLocCol//
              = new TableColumn<Stock, String>("Emplacement");

      // Active Column
      TableColumn<Stock, Boolean> activeCol//
              = new TableColumn<Stock, Boolean>("Active");

      table.getColumns().addAll(itemCol, quantityCol, storageLocCol, activeCol);

      this.getChildren().add(table);
    }

    public void onUnfocus() {

    }

    public void start() {
        setAlignment(Pos.CENTER);

        // Création des élèments de cette page
        Label label = new Label(this.getName());

        // On ajoute tous les élèments de cette page comme enfant de BaseScene
        // Ils seront grace à cela affichés.
        getChildren().addAll(label);
    }
}
