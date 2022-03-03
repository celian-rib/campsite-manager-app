package pt4.flotsblancs.components;

import java.util.ArrayList;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ItemList<I extends Item> extends StackPane {

    private ItemScene<I> itemScene;

    private BorderPane borderPane;
    private ScrollPane scrollPane;

    public ItemList(ItemScene<I> itemScene) {
        borderPane = new BorderPane();

        this.itemScene = itemScene;
        scrollPane = createScrollPane();

        TextField searchBar = createSearchBar();
        setMargin(searchBar, new Insets(0, 0, 10, 0));

        BackgroundFill fill = new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY);
        Background background = new Background(fill);

        borderPane.setTop(searchBar);
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(createAddButton());
        borderPane.setPadding(new Insets(10));
        borderPane.setBackground(background);

        BorderPane.setMargin(scrollPane, new Insets(10, 0, 10, 0));

        Shadow shadow = new Shadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setColor(Color.LIGHTGRAY);
        shadow.setRadius(15);

        Pane shadowPane = new Pane();
        shadowPane.setBackground(background);
        shadowPane.setEffect(shadow);

        setBackground(background);
        getChildren().addAll(shadowPane, borderPane);
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

    private HBox createAddButton() {
        var container = new HBox();
        BackgroundFill fill = new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY);
        Background background = new Background(fill);
        container.setBackground(background);
        var btn = new MFXButton("Ajouter");
        // btn.getStyleClass().add("action-button-outlined");
        // btn.setOnAction(e -> {
        //     // TODO linking
        //     Router.showToast(ToastType.WARNING, "LINKING TO DO");
        // });
        btn.setPrefWidth(100);
        container.getChildren().add(btn);
        System.out.println(container.getWidth());
        // btn.setPrefWidth(container.getWidth());
        return container;
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
        ObservableList<ItemPane<I>> itemsListContainer = FXCollections.observableArrayList(listButtons);

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
