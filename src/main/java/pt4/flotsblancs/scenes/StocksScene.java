package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Stock;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.ToastType;

public class StocksScene extends VBox implements IScene {

    private TableView<Stock> table;
    private MFXButton addItemBtn;
    private MFXButton deleteItemBtn;

    @Override
    public String getName() {
        return "Stocks";
    }

    @Override
    public boolean showNavBar() {
        return true;
    }

    @Override
    public void onFocus() {
        updateTable();
    }

    @Override
    public void start() {
        setAlignment(Pos.CENTER);
        setSpacing(10);
        displayTableView();
        this.getChildren().add(createActionsButtonsSlot());
        deleteItemBtn.setDisable(true);
        table.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    deleteItemBtn.setDisable(table.getSelectionModel().getSelectedItem() == null);
                });
    }

    private void displayTableView() {
        table = new TableView<Stock>();
        table.setPrefHeight(620);
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Stock, String> itemCol = new TableColumn<Stock, String>("Produit");
        TableColumn<Stock, Integer> quantityCol = new TableColumn<Stock, Integer>("Quantité");
        TableColumn<Stock, String> storageLocCol = new TableColumn<Stock, String>("Emplacement");
        TableColumn<Stock, Integer> alertCol = new TableColumn<Stock, Integer>("Seuil d'alerte");

        table.getColumns().add(itemCol);
        table.getColumns().add(quantityCol);
        table.getColumns().add(storageLocCol);
        table.getColumns().add(alertCol);

        table.setRowFactory(t -> new TableRow<Stock>() {
            @Override
            public void updateItem(Stock item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    if (item.getQuantity() <= item.getQuantityAlertThreshold()) {
                        setStyle("-fx-background-color: #ff7961");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemCol.setCellFactory(TextFieldTableCell.<Stock>forTableColumn());

        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        quantityCol.setCellFactory(col -> {
            TableCell<Stock, Integer> cell = new TableCell<Stock, Integer>();

            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    var graphic = new Spinner<Integer>();
                    SpinnerValueFactory<Integer> valueFactory =
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000000);
                    valueFactory.setValue(cell.getItem());
                    valueFactory.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (oldVal == null)
                            return;
                        var stock = cell.getTableRow().getItem();
                        if(stock == null)
                            return;
                        stock.setQuantity(newVal);
                        // TODO debounce l'update pour ne pas spam la BD
                        updateDatabase(stock);
                    });
                    graphic.setValueFactory(valueFactory);

                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty())
                            .then((Node) null).otherwise(graphic));
                }
            });

            return cell;
        });

        alertCol.setCellValueFactory(new PropertyValueFactory<>("quantityAlertThreshold"));

        alertCol.setCellFactory(col -> {
            TableCell<Stock, Integer> cell = new TableCell<Stock, Integer>();

            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    var graphic = new Spinner<Integer>();
                    SpinnerValueFactory<Integer> valueFactory =
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000000);
                    valueFactory.setValue(cell.getItem());
                    valueFactory.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (oldVal == null)
                            return;
                        var stock = cell.getTableRow().getItem();
                        if(stock == null)
                            return;
                        stock.setQuantityAlertThreshold(newVal);
                        // TODO debounce l'update pour ne pas spam la BD
                        updateDatabase(stock);
                    });
                    graphic.setValueFactory(valueFactory);

                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty())
                            .then((Node) null).otherwise(graphic));
                }
            });

            return cell;
        });

        storageLocCol.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));
        storageLocCol.setCellFactory(TextFieldTableCell.<Stock>forTableColumn());

        itemCol.setOnEditCommit(event -> {
            event.getRowValue().setItem(event.getNewValue());
            updateDatabase(event.getRowValue());
        });

        storageLocCol.setOnEditCommit(event -> {
            event.getRowValue().setStorageLocation(event.getNewValue());
            updateDatabase(event.getRowValue());
        });
        storageLocCol.setOnEditCommit(event -> {
            event.getRowValue().setStorageLocation(event.getNewValue());
            updateDatabase(event.getRowValue());
        });

        this.getChildren().add(table);
    }

    private void updateTable() {
        try {
            table.getItems().clear();
            table.getItems().addAll(queryAll());
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
        }
    }
    
    private void updateDatabase(Stock stock) {
        table.refresh();
        try {
            Database.getInstance().getStockDao().update(stock);
            Router.showToast(ToastType.SUCCESS, "Stock mis à jour");
        } catch (SQLException e) {
            ExceptionHandler.updateIssue(e);
        }
    }

    private HBox createActionsButtonsSlot() {
        var container = new HBox(10);

        deleteItemBtn = new MFXButton("Supprimer un produit");
        addItemBtn = new MFXButton("Ajouter un produit");
        FontIcon icon = new FontIcon("fas-exclamation-triangle:10");
        icon.setIconColor(Color.WHITE);

        addItemBtn.getStyleClass().add("action-button");
        deleteItemBtn.getStyleClass().add("action-button");

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(deleteItemBtn, addItemBtn);

        deleteItemBtn.setOnAction(e -> {
            try {
                Database.getInstance().getStockDao()
                        .delete(table.getSelectionModel().getSelectedItem());
            } catch (SQLException e1) {
                e1.printStackTrace();
                Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
                Router.goToScreen(Routes.HOME);
            }
            updateTable();
        });

        addItemBtn.setOnAction(e -> {
            var s = new Stock();
            s.setItem("Nouveau produit");
            s.setQuantity(0);
            s.setQuantityAlertThreshold(5);
            s.setStorageLocation("Pas d'emplacement");
            try {
                Database.getInstance().getStockDao().create(s);
            } catch (SQLException e1) {
                e1.printStackTrace();
                Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
                Router.goToScreen(Routes.HOME);
            }
            updateTable();
            table.getSelectionModel().selectLast();
            table.scrollTo(table.getSelectionModel().getSelectedItem()); 
        });

        return container;
    }

    private List<Stock> queryAll() throws SQLException {
        return Database.getInstance().getStockDao().queryForAll();
    }
}
