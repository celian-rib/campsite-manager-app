package pt4.flotsblancs.scenes;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Log;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;

import java.util.Date;
import java.util.List;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

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
        getChildren().add(createTableView());
        table.getSelectionModel().selectedItemProperty();
    }

    private TableView<Log> createTableView() {
        table = new TableView<Log>();
        table.setPrefHeight(620);
        table.setEditable(false);

        TableColumn<Log, User> userCol = new TableColumn<Log, User>("Utilisateur");
        TableColumn<Log, LogType> typeCol = new TableColumn<Log, LogType>("Catégorie");
        TableColumn<Log, String> messageCol = new TableColumn<Log, String>("Détails");
        TableColumn<Log, Date> dateCol = new TableColumn<Log, Date>("Date");

        table.getColumns().add(userCol);
        table.getColumns().add(typeCol);
        table.getColumns().add(messageCol);
        table.getColumns().add(dateCol);

        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        userCol.setCellFactory(column -> {
            TableCell<Log, User> cell = new TableCell<Log, User>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getFirstName() + " " + item.getName());
                    }
                }
            };

            return cell;
        });


        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setCellFactory(column -> {
            TableCell<Log, LogType> cell = new TableCell<Log, LogType>() {
                @Override
                protected void updateItem(LogType item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };

            return cell;
        });

        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(column -> {
            TableCell<Log, Date> cell = new TableCell<Log, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(format.format(item));
                    }
                }
            };

            return cell;
        });

        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageCol.setCellFactory(TextFieldTableCell.<Log>forTableColumn());

        userCol.setResizable(false);
        userCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

        typeCol.setResizable(false);
        typeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

        messageCol.setResizable(false);
        messageCol.prefWidthProperty().bind(table.widthProperty().multiply(0.6));

        dateCol.setResizable(false);
        dateCol.prefWidthProperty().bind(table.widthProperty().multiply(0.2));

        return table;
    }

    private void updateTable() {
        try {
            table.getItems().clear();
            table.getItems().addAll(queryAll());
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
        }
    }

    private List<Log> queryAll() throws SQLException {
        return Database.getInstance().getLogDao().queryForAll();
    }
}

