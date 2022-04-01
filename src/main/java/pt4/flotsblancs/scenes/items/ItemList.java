package pt4.flotsblancs.scenes.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

class ItemList<I extends Item> extends StackPane {

    private final static int CONTENT_WIDTH = 250;
    private String query = "";
    private List<I> initialList;

    private ItemScene<I> itemScene;

    private BorderPane borderPane;
    private MFXScrollPane scrollPane;

    private MFXTextField searchBar;

    private I selectedItem = null;

    private ListView<ItemPane<I>> listView = new ListView<ItemPane<I>>();

    @Getter
    private ArrayList<ItemPane<I>> listButtons = new ArrayList<ItemPane<I>>();

    private HBox addButton;

    private ObservableList<ItemPane<I>> itemsListContainer = FXCollections.observableArrayList();

    /**
     * Permet de créer l'item list associée à l'item scene donnée
     * 
     * @param itemScene
     */
    ItemList(ItemScene<I> itemScene) {
        this.itemScene = itemScene;

        BackgroundFill fill = new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY);
        Background background = new Background(fill);

        Shadow shadow = new Shadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setColor(Color.LIGHTGRAY);
        shadow.setRadius(15);

        Pane shadowPane = new Pane();
        shadowPane.setBackground(background);
        shadowPane.setEffect(shadow);

        scrollPane = createScrollPane();
        searchBar = createSearchBar();
        addSearchListener();
        addButton = createAddButton();

        // Border pane qui contient l'ensemble des élèments
        // ItemList en elle même est une StackPane pour superposer l'ombre avec cette
        // borderpane
        borderPane = new BorderPane();
        borderPane.setTop(searchBar);
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(addButton);
        borderPane.setPadding(new Insets(10));
        borderPane.setBackground(background);
        borderPane.setCenter(scrollPane);

        BorderPane.setMargin(scrollPane, new Insets(10, 0, 10, 0));

        setBackground(background);
        setMargin(searchBar, new Insets(0, 0, 10, 0));
        setIsLoading(true);

        getChildren().addAll(shadowPane, borderPane);
    }

    private void addSearchListener() {
        if (searchBar == null)
            return;
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList(newValue);
        });
    }

    private void filterList(String filter) {
        query = filter.trim().toLowerCase();
        if (query.isEmpty()) {
            updateItems(initialList);
            return;
        }

        List<I> filteredList = initialList.stream().filter((I item) -> {
            var words = query.split(" ");
            return Arrays.stream(words).allMatch(item.getSearchString()::contains);
        }).collect(Collectors.toList());
        updateItems(filteredList, true);
    }

    private ArrayList<ItemPane<I>> createListButtons(List<I> items) {
        ArrayList<ItemPane<I>> liste = new ArrayList<ItemPane<I>>();
        ItemPane<I> ipane;
        for (I i : items) {
            ipane = new ItemPane<I>(i, CONTENT_WIDTH - 15);
            liste.add(ipane);
        }
        return liste;
    }

    private void updateItems(List<I> items, boolean filtered) {
        long start = System.currentTimeMillis();
        log("Starting update");

        if (items.size() == 0)
            return;

        if (!filtered)
            initialList = items;

        long start2 = System.currentTimeMillis();
        listButtons = createListButtons(items);
        long end2 = System.currentTimeMillis();
        log("Time to create list buttons: " + (end2 - start2) + "ms");

        itemsListContainer.clear();
        itemsListContainer.addAll(listButtons);

        listView = new ListView<ItemPane<I>>();
        listView.setFocusTraversable(false);
        listView.setStyle("-fx-background-insets: 0; -fx-background-insets: 0; -fx-padding: 0;");

        listView.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            ItemPane<I> selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getItem() != selectedItem) {
                selectedItem = selected.getItem();
                log("User selected item: " + selectedItem.getDisplayName());
                itemScene.updateContainer(selected.getItem());
            }
        });

        if (selectedItem != null) {
            itemScene.updateContainer(selectedItem);
            selectItem(selectedItem);
        }

        listView.setItems(itemsListContainer);
        scrollPane.setContent(listView);

        long end = System.currentTimeMillis();
        log("Items updated in " + (end - start) + " ms");

        updateDotsColorAsync(listButtons);
    }

    private void updateDotsColorAsync(ArrayList<ItemPane<I>> itemPanes) {
        final Task<ArrayList<ItemPane<I>>> updatesDotsTask = new Task<ArrayList<ItemPane<I>>>() {
            @Override
            protected ArrayList<ItemPane<I>> call() {
                itemPanes.forEach(ip -> ip.updateColor());
                return itemPanes;
            };

            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(() -> itemPanes.forEach(b -> b.showDots()));
            };

            @Override
            protected void failed() {
                super.failed();
                getException().printStackTrace();
            };
        };

        new Thread(updatesDotsTask).start();
    }


    /**
     * Permet de mettre à jour la liste d'Item affichés par cette ItemList
     * 
     * @param items
     */
    void updateItems(List<I> items) {
        updateItems(items, false);
    }

    /**
     * Permet de réinitialiser l'item sélectionné dans la liste
     */
    void clearSelectedItem() {
        selectedItem = null;
    }

    /**
     * Active ou désactive l'animation de chargement de cette itemList
     * 
     * @param isLoading
     */
    void setIsLoading(boolean isLoading) {
        if (isLoading)
            borderPane.setCenter(new MFXProgressSpinner());
        else
            borderPane.setCenter(scrollPane);
    }

    /**
     * Permet de sélectionner un item en particulier
     * 
     * @param item
     */
    void selectItem(I item) {
        log("Selecting item " + item.getDisplayName());
        selectedItem = item;
        var itemPane = new ItemPane<I>(item, CONTENT_WIDTH - 15);
        int index = itemsListContainer.indexOf(itemPane);

        itemScene.updateContainer(item);

        if (index == -1) {
            itemsListContainer.add(itemPane);
            listView.setItems(itemsListContainer);
            listView.getSelectionModel().select(itemPane);
            listView.scrollTo(itemPane);
        } else {
            listView.setItems(itemsListContainer);
            listView.getSelectionModel().clearAndSelect(index);
            listView.getFocusModel().focus(index);
            listView.scrollTo(index);
        }
        log("Selected");
    }

    private MFXScrollPane createScrollPane() {
        MFXScrollPane scrollPane = new MFXScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(CONTENT_WIDTH);
        return scrollPane;
    }

    private MFXTextField createSearchBar() {
        MFXTextField searchBar = new MFXTextField();
        searchBar.setFloatingText("Rechercher");
        searchBar.setFloatMode(FloatMode.BORDER);
        searchBar.setMinWidth(CONTENT_WIDTH);
        var icon = new FontIcon("fas-search:15");
        icon.setIconColor(Color.GREY);
        searchBar.setPadding(new Insets(5, 10, 5, 5));
        searchBar.setTrailingIcon(icon);
        return searchBar;
    }

    private HBox createAddButton() {
        if (itemScene.addButtonText() == null)
            return null;
        var container = new HBox();
        var btn = new MFXButton(itemScene.addButtonText());
        btn.getStyleClass().add("action-button");
        btn.setOnAction(e -> {
            itemScene.onAddButtonClicked();
        });
        container.getChildren().add(btn);
        btn.setMinWidth(CONTENT_WIDTH);
        return container;
    }

    private static void log(String message) {
        System.out.println("[ItemList] " + message);
    }
}
