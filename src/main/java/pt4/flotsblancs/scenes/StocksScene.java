package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Stock;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;

public class StocksScene extends VBox implements IScene {

    private TableView<Stock> table;

    public String getName() {
        return "Stocks";
    }

    public boolean showNavBar() {
        return true;
    }

    public void onFocus() {
        updateTable();
    }

    protected List<Stock> queryAll() throws SQLException {
        return Database.getInstance().getStockDao().queryForAll();
    }

    public void start() {
        setAlignment(Pos.CENTER);
        displayTableView();
    }

    public void displayTableView() {
        table = new TableView<Stock>();
        table.setEditable(true);

        TableColumn<Stock, String> itemCol = new TableColumn<Stock, String>("Objet");
        TableColumn<Stock, Integer> quantityCol = new TableColumn<Stock, Integer>("Quantité");
        TableColumn<Stock, String> storageLocCol = new TableColumn<Stock, String>("Emplacement");
        TableColumn<Stock, Integer> alertCol = new TableColumn<Stock, Integer>("Seuil d'alerte");

        table.getColumns().add(itemCol);
        table.getColumns().add(quantityCol);
        table.getColumns().add(storageLocCol);
        table.getColumns().add(alertCol);
        

        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemCol.setCellFactory(TextFieldTableCell.<Stock>forTableColumn());

        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        quantityCol.setCellFactory(col -> {
            TableCell<Stock, Integer> cell = new TableCell<Stock, Integer>();

            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    var graphic = new Spinner<Integer>();
                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100000000);
                    valueFactory.setValue(cell.getItem());
                    valueFactory.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if(oldVal == null)
                            return;
                        var stock = cell.getTableRow().getItem();
                        stock.setQuantity(newVal);
                        // TODO debounce l'update pour ne pas spam la BD
                        updateDatabase(stock);
                    });
                    graphic.setValueFactory(valueFactory);

                    cell.graphicProperty()
                            .bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
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
                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100000000);
                    valueFactory.setValue(cell.getItem());
                    valueFactory.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if(oldVal == null)
                            return;
                        var stock = cell.getTableRow().getItem();
                        stock.setQuantityAlertThreshold(newVal);
                        // TODO debounce l'update pour ne pas spam la BD
                        updateDatabase(stock);
                    });
                    graphic.setValueFactory(valueFactory);

                    cell.graphicProperty()
                            .bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
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
        } catch (SQLRecoverableException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de connexion");
            Router.goToScreen(Routes.CONN_FALLBACK);
        } catch (SQLException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de chargement des données");
            Router.goToScreen(Routes.HOME);
        }
    }

    private void updateDatabase(Stock stock) {
        try {
            Database.getInstance().getStockDao().update(stock);
            Router.showToast(ToastType.SUCCESS, "Stock mis à jour");
        } catch (SQLRecoverableException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de connexion");
            Router.goToScreen(Routes.CONN_FALLBACK);
        } catch (SQLException e) {
            System.err.println(e);
            Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
            Router.goToScreen(Routes.HOME);
        }
    }
}
