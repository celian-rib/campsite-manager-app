package pt4.flotsblancs.scenes.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.enums.FloatMode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import pt4.flotsblancs.database.model.Reservation;

public class ItemList<I extends Item> extends StackPane {

    private final static int CONTENT_WIDTH = 250;
    private String query = "";
    private List<I> initialList;

    private ItemScene<I> itemScene;

    private BorderPane borderPane;
    private MFXScrollPane scrollPane;

    private MFXTextField searchBar;

    private ListView<ItemPane<I>> listView = new ListView<ItemPane<I>>();
    private ArrayList<ItemPane<I>> listButtons = new ArrayList<ItemPane<I>>();

    private HBox addButton;

    /**
     * Permet de créer l'item list associée à l'item scene donnée
     * 
     * @param itemScene
     */
    public ItemList(ItemScene<I> itemScene) {
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
        // ItemList en ellse même est une StackPane pour superposer l'ombre avec cette borderpane
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
        ArrayList<ItemPane<I>> listButtons = new ArrayList<ItemPane<I>>();

        for (I i : items)
            listButtons.add(createListButton(i));
        return listButtons;
    }

    private void updateItems(List<I> items, boolean filtered) {
        if (!filtered) {
            boolean isReservation = items.get(0) instanceof Reservation;
            items.sort((i1, i2) -> {
                if (isReservation) {
                    var r1 = (Reservation) i1;
                    var r2 = (Reservation) i2;

                    return r1.getStartDate().compareTo(r2.getStartDate());
                }
                return i1.getId() - i2.getId();
            });
            if (isReservation) {
                ArrayList<I> canceledAndPast = new ArrayList<I>();
                int i = 0;
                while (i < items.size()) {
                    I it = items.get(i);
                    Reservation r = (Reservation) it;
                    if (r.getCanceled() || r.isInPast()) {
                        canceledAndPast.add(it);
                        items.remove(it);
                        continue;
                    }
                    i++;
                }
                items.addAll(canceledAndPast);
            }


            initialList = items;
        }
        listButtons = createListButtons(items);

        ListView<ItemPane<I>> listView = new ListView<ItemPane<I>>();
        ObservableList<ItemPane<I>> itemsListContainer =
                FXCollections.observableArrayList(listButtons);

        listView.setFocusTraversable(false);
        listView.setStyle("-fx-background-insets: 0; -fx-background-insets: 0; -fx-padding: 0;");

        listView.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ItemPane<I> selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null)
                    itemScene.updateContainer(selected.getItem());
            }
        });

        listView.setItems(itemsListContainer);
        scrollPane.setContent(listView);
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
    public void selectItem(I item) {
        listView.getSelectionModel().select(new ItemPane<I>(item));
        itemScene.updateContainer(item);
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
        if(itemScene.addButtonText() == null)
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

    private ItemPane<I> createListButton(I item) {
        ItemPane<I> button = new ItemPane<I>(item);
        // On aligne verticalement car c'est moche sinon
        button.setPadding(new Insets(6, 0, 0, 0));
        button.setPrefHeight(30);

        Text display = new Text(item.getDisplayName());
        display.setFill(Color.rgb(50, 60, 100));
        display.setStyle("-fx-font-weight: bold");

        Text id = new Text("#" + item.getId());
        id.setFill(Color.rgb(50, 50, 80));
        id.setStyle("-fx-font-weight: bold");

        button.setLeft(display);
        button.setRight(id);
        button.setMaxWidth(CONTENT_WIDTH - 15);
        return button;
    }

    private class ItemPane<T extends Item> extends BorderPane {
        private T item;

        public ItemPane(T item) {
            super();
            this.item = item;
        }

        public T getItem() {
            return this.item;
        }

        @Override
        public boolean equals(Object anObject) {
            if (this == anObject) {
                return true;
            }
            return anObject instanceof Item && this.getItem().equals(anObject);
        }
    }
}
