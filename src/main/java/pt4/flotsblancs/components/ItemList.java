package pt4.flotsblancs.components;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.items.ItemScene;

public class ItemList<I extends Item> extends BorderPane {

    private ItemScene<I> itemScene;
    private ScrollPane scrollPane;

    public ItemList(ItemScene<I> itemScene) {
        this.itemScene = itemScene;
        scrollPane = createScrollPane();

        TextField searchBar = createSearchBar();
        setMargin(searchBar, new Insets(0, 0, 10, 0));

        setTop(searchBar);
        setCenter(scrollPane);
    }

    private ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    private TextField createSearchBar() {
        TextField searchBar = new TextField();
        searchBar.setPromptText("Rechercher");
        return searchBar;
    }

    private ItemPane<I> createListButton(I item) {
        ItemPane<I> button = new ItemPane<I>(item);
        button.setPadding(new Insets(6, 0, 0, 0)); // On aligne verticalement car c'est moche sinon
        button.setPrefHeight(30);

        Text display = new Text(item.getDisplayName());
        display.setFill(Color.rgb(50, 60, 100));
        display.setStyle("-fx-font-weight: bold");

        Text id = new Text("#" + item.getId());
        id.setFill(Color.rgb(50, 50, 80));
        id.setStyle("-fx-font-weight: bold");

        button.setLeft(display);
        button.setRight(id);
        return button;
    }

    public void updateItems(List<I> items) {
        ArrayList<ItemPane<I>> listButtons = new ArrayList<ItemPane<I>>();

        for (I i : items)
            listButtons.add(createListButton(i));

        ListView<ItemPane<I>> listView = new ListView<ItemPane<I>>();
        ObservableList<ItemPane<I>> itemsListContainer =
                FXCollections.observableArrayList(listButtons);

        listView.setFocusTraversable(false);
        listView.setStyle(
                "-fx-background-insets: 0;-fx-background-insets: 0;    -fx-background-insets: 0;   -fx-padding: 0;");

        listView.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ItemPane<I> selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    itemScene.updateContainer(selected.getItem());
                }
            }
        });

        listView.setItems(itemsListContainer);
        scrollPane.setContent(listView);
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
    }
}
