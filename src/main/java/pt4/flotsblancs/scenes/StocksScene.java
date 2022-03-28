package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Stock;

import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.components.ConfirmButton;
import pt4.flotsblancs.scenes.components.HBoxSpacer;
import pt4.flotsblancs.scenes.components.PromptedTextField;
import pt4.flotsblancs.scenes.components.VBoxSpacer;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.ToastType;

public class StocksScene extends ItemScene<Stock> {

    private Label title;

    private Stock stock;

    private MFXTextField itemInput;
    private MFXTextField storageLocationInput;

    private Spinner<Integer> quantitySpinner;
    private Spinner<Integer> quantityAlertSpinner;

    private MFXButton saveButton;

    private ChangeListener<? super Object> changeListener = (obs, oldVal, newVal) -> {
        if (oldVal == null || newVal == null || oldVal == newVal)
            return;
        saveButton.setDisable(false);
    };

    @Override
    public String getName() {
        return "Stocks";
    }

    @Override
    protected String addButtonText() {
        return "Ajouter un nouveau stock";
    }

    @Override
    protected void onAddButtonClicked() {
        var newStock = new Stock();
        newStock.setItem("Nouveau produit");
        newStock.setQuantity(0);
        newStock.setQuantityAlertThreshold(5);
        newStock.setStorageLocation("Pas d'emplacement");
        try {
            Database.getInstance().getStockDao().create(newStock);
            Router.showToast(ToastType.SUCCESS, "Nouveau stock créé");
            Router.goToScreen(Routes.STOCKS, newStock);
        } catch (SQLException e1) {
            ExceptionHandler.loadIssue(e1);
        }
    }

    private void updateDatabase(Stock stock) {
        try {
            if (!stock.getItem().equals(itemInput.getText()))
                stock.setItem(itemInput.getText());

            if (!stock.getStorageLocation().equals(storageLocationInput.getText()))
                stock.setStorageLocation(storageLocationInput.getText());

            if (stock.getQuantity() != quantitySpinner.getValue())
                stock.setQuantity(quantitySpinner.getValue());

            if (stock.getQuantityAlertThreshold() != quantityAlertSpinner.getValue())
                stock.setQuantityAlertThreshold(quantityAlertSpinner.getValue());

            Database.getInstance().getStockDao().update(stock);
            Router.showToast(ToastType.SUCCESS, "Stock mis à jour");
        } catch (SQLException e) {
            ExceptionHandler.updateIssue(e);
        }
    }

    private void refreshPage() {
        title.setText(stock.getItem() + "  #" + stock.getId());
    }

    @Override
    protected Region createContainer(Stock item) {
        this.stock = item;
        var container = new VBox();
        container.setPadding(new Insets(50));

        container.getChildren().add(createHeader());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createMainContainer());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createActionsButtonsSlot());

        refreshPage();
        return container;
    }

    private HBox createActionsButtonsSlot() {
        var container = new HBox(10);

        saveButton = new MFXButton("Sauvegarder");
        saveButton.getStyleClass().add("action-button");
        saveButton.setDisable(true);

        ConfirmButton deleteStockButton = new ConfirmButton("Supprimer");
        deleteStockButton.getStyleClass().add("action-button-outlined");
        deleteStockButton.setOnConfirmedAction((e) -> {
            try {
                Router.showToast(ToastType.INFO, "Stock supprimé");
                Database.getInstance().getStockDao().delete(stock);
                super.onItemDelete(stock);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(deleteStockButton, saveButton);

        saveButton.setOnAction(e -> {
            updateDatabase(stock);
            updateItemList();
            saveButton.setDisable(true);
        });

        return container;
    }

    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        title = new Label();
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        container.setLeft(title);
        return container;
    }

    private HBox createMainContainer() {
        HBox container = new HBox();

        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(createTextInfosContainer());
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(createQuantitysContainer());
        container.getChildren().add(new HBoxSpacer());
        return container;
    }

    private VBox createTextInfosContainer() {
        VBox container = new VBox(30);

        itemInput = new PromptedTextField(stock.getItem(), "Nom du produit");
        storageLocationInput =
                new PromptedTextField(stock.getStorageLocation(), "Emplacement de stockage");

        itemInput.textProperty().addListener(changeListener);
        storageLocationInput.textProperty().addListener(changeListener);

        container.getChildren().add(itemInput);
        container.getChildren().add(storageLocationInput);

        return container;
    }

    private VBox createQuantitysContainer() {
        VBox container = new VBox(30);

        VBox quantityContainer = new VBox(5);
        Label prompt = new Label("Quantité actuellement en stock");
        SpinnerValueFactory<Integer> vFact =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, stock.getQuantity());
        quantitySpinner = new Spinner<Integer>();
        quantitySpinner.setEditable(true);
        quantitySpinner.setValueFactory(vFact);
        quantitySpinner.valueProperty().addListener(changeListener);
        quantityContainer.getChildren().addAll(prompt, quantitySpinner);

        VBox quantityAlertContainer = new VBox(5);
        Label promptAlert = new Label("Seuil d'alerte quantité");
        SpinnerValueFactory<Integer> vFactAlert =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000,
                        stock.getQuantityAlertThreshold());
        quantityAlertSpinner = new Spinner<Integer>();
        quantityAlertSpinner.setEditable(true);
        quantityAlertSpinner.setValueFactory(vFactAlert);
        quantityAlertSpinner.valueProperty().addListener(changeListener);
        quantityContainer.getChildren().addAll(promptAlert, quantityAlertSpinner);

        container.getChildren().add(quantityContainer);
        container.getChildren().add(quantityAlertContainer);
        return container;
    }

    @Override
    protected List<Stock> queryAll() throws SQLException {
        return Database.getInstance().getStockDao().queryForAll();
    }
}
