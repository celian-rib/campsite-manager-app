package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.time.Month;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Stock;
import pt4.flotsblancs.router.IScene;

public class StocksScene extends VBox implements IScene {

    TableView<Stock> table = new TableView<Stock>();
    private MFXButton modify;

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
        this.getChildren().add(createActionsButtonsSlot());
    }

    public void displayTableView() {

        table.setEditable(true);

        TableColumn<Stock, String> itemCol = new TableColumn<Stock, String>("Objet");

        TableColumn<Stock, Integer> quantityCol = new TableColumn<Stock, Integer>("Quantité");

        TableColumn<Stock, String> storageLocCol = new TableColumn<Stock, String>("Emplacement");

        table.getColumns().addAll(itemCol, quantityCol, storageLocCol);

        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemCol.setCellFactory(TextFieldTableCell.<Stock>forTableColumn());

        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        // quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new
        // IntegerStringConverter()));

        quantityCol.setCellFactory(col -> {
            TableCell<Stock, Integer> cell = new TableCell<Stock, Integer>();

            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    var graphic = new Spinner<Integer>();
                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100000000);
                    valueFactory.setValue(cell.getItem());
                    graphic.setValueFactory(valueFactory);

                    cell.graphicProperty()
                            .bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
                }
            });

            return cell;
        });

        quantityCol.setOnEditCommit((CellEditEvent<Stock, Integer> event) -> {
            TablePosition<Stock, Integer> pos = event.getTablePosition();

            Integer newGender = event.getNewValue();

            int row = pos.getRow();
            Stock stock = event.getTableView().getItems().get(row);
            System.out.println(stock);            
        });

        storageLocCol.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));
        storageLocCol.setCellFactory(TextFieldTableCell.<Stock>forTableColumn());

        try {
            table.getItems().addAll(queryAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.getChildren().add(table);
    }

    private HBox createActionsButtonsSlot() {
        var container = new HBox(10);

        modify = new MFXButton("Valider les modifications");
        FontIcon icon = new FontIcon("fas-exclamation-triangle:10");
        icon.setIconColor(Color.WHITE);

        modify.getStyleClass().add("action-button");

        modify.setOnAction(e -> {
            System.out.println(table.getSelectionModel().getSelectedItem());
        });

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(modify);
        return container;
    }
}
