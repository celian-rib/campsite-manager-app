package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Stock;
import pt4.flotsblancs.router.IScene;

public class StocksScene extends VBox implements IScene {

    public String getName() {
        return "Stocks";
    }

    public boolean showNavBar() {
        return true;
    }

    public void onFocus() {
        
    }

    protected List<Stock> queryAll() throws SQLException {
        return Database.getInstance().getStockDao().queryForAll();
    }


    public void onUnfocus() {

    }

    public void start() {
        setAlignment(Pos.CENTER);
        displayTableView();
    }

    public void displayTableView(){
        TableView<Stock> table = new TableView<Stock>();
        
        table.setEditable(true);

        TableColumn<Stock, String> itemCol = new TableColumn<Stock, String>("Objet");

        TableColumn<Stock, Integer> quantityCol = new TableColumn<Stock, Integer>("Quantité");

        TableColumn<Stock, String> storageLocCol = new TableColumn<Stock, String>("Emplacement");

        table.getColumns().addAll(itemCol, quantityCol, storageLocCol);

        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        storageLocCol.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));

        try {
            table.getItems().addAll(queryAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.getChildren().add(table);
    }
}
