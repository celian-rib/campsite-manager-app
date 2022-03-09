package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;


import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Log;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;

public class LogsScene extends VBox implements IScene {

    private TableView<Log> table;

    @Override
    public String getName() {
        return "Logs";
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
        table.getSelectionModel().selectedItemProperty();
    }

    private void displayTableView() {
        table = new TableView<Log>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Log, String> userCol = new TableColumn<Log, String>("Utilisateur");
        TableColumn<Log, String> typeCol = new TableColumn<Log, String>("Catégorie");
        TableColumn<Log, String> messageCol = new TableColumn<Log, String>("Détails");
        TableColumn<Log, String> dateCol = new TableColumn<Log, String>("Date");

        table.getColumns().add(userCol);
        table.getColumns().add(typeCol);
        table.getColumns().add(messageCol);
        table.getColumns().add(dateCol);

        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        userCol.setCellFactory(TextFieldTableCell.<Log>forTableColumn());


        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setCellFactory(TextFieldTableCell.<Log>forTableColumn());
        

        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(TextFieldTableCell.<Log>forTableColumn());

        messageCol.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));
        messageCol.setCellFactory(TextFieldTableCell.<Log>forTableColumn());

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

    private List<Log> queryAll() throws SQLException {
        return Database.getInstance().getLogDao().queryForAll();
    }
}

