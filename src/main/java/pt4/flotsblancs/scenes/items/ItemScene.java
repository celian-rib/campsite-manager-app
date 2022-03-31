package pt4.flotsblancs.scenes.items;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import pt4.flotsblancs.scenes.breakpoints.BreakPointListener;
import pt4.flotsblancs.scenes.breakpoints.BreakPointManager;
import pt4.flotsblancs.scenes.breakpoints.HBreakPoint;
import pt4.flotsblancs.scenes.components.EmptyItemContainer;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.Timer;

public abstract class ItemScene<I extends Item> extends BorderPane
        implements IItemScene<I>, BreakPointListener {

    private ItemList<I> itemList;
    private final static Timer TIMER = new Timer();

    /**
     * Permet de créer le conteneur affichant l'item actuellement sélectionné
     * 
     * @param item item sélectionné qui doit être affiché
     * @return une Node gérant l'affichage de l'item
     */
    protected abstract Region createContainer(I item);

    /**
     * @return l'ensembles des Items
     * @throws SQLException
     */
    protected abstract List<I> queryAll() throws SQLException;

    /**
     * @return Permet d'indiquer le texte du bouton d'ajout
     */
    protected String addButtonText() {
        return "Ajouter";
    }

    /**
     * Appelée au moment ou le bouton ajouter
     */
    protected void onAddButtonClicked() {
        System.out.println("Bouton d'ajout pas implémenté sur la page " + getName());
    };

    protected void onContainerUnfocus() {
        // Peut être implémentée par les enfants
    };

    /**
     * Met en place le BorderPane contenant à gauche la liste des items et à droite
     * l'item
     * selectionné
     */
    @Override
    public void start() {
        itemList = new ItemList<I>(this);

        BreakPointManager.addListener(this);
        setLeft(itemList);
        updateContainer(null);
    }

    @Override
    public void onFocus() {
        updateItemList();
    }

    protected void updateItemList() {
        itemList.setIsLoading(true);
        final Task<List<I>> updateListTask = new Task<List<I>>() {
            @Override
            protected java.util.List<I> call() throws SQLException {
                List<I> allItems;
                allItems = queryAll().stream().filter(i -> i.isForeignCorrect()).sorted().collect(Collectors.toList());
                    Platform.runLater(() -> itemList.updateItems(allItems));
                    Platform.runLater(() -> itemList.getListButtons().forEach(b -> b.displayDot()));
                    
                    System.out.println("UPDATED");
                    return allItems;
            };

            @Override
            protected void succeeded() {
                super.succeeded();
                itemList.setIsLoading(false);
            };

            @Override
            protected void failed() {
                super.failed();
                getException().printStackTrace();
                ExceptionHandler.loadIssue(new SQLException());
            };
        };
        
        new Thread(updateListTask).start();
    }

    /**
     * Met à jour le conteneur droit de la page, affichant les informations de
     * l'item sélectionné
     * 
     * @param item item selectionné qui doit être affiché
     */
    void updateContainer(I item) {
        if (item != null) {
            onContainerUnfocus();
        }
        // Stack pane pour pouvoir créer une ombre derrière le conteneur
        StackPane stack = new StackPane();

        Shadow shadow = new Shadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setColor(Color.LIGHTGRAY);
        shadow.setRadius(15);

        BackgroundFill fill = new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY);
        Background background = new Background(fill);

        Pane shadowPane = new Pane();
        shadowPane.setBackground(background);
        shadowPane.setEffect(shadow);

        Region container = item == null ? new EmptyItemContainer() : createContainer(item);
        container.setBackground(background);

        stack.getChildren().addAll(shadowPane, container);

        setCenter((Parent) stack);
    }

    protected void onItemDelete(Item i) {
        updateContainer(null);
        updateItemList();
    }

    @Override
    public void onHorizontalBreak(HBreakPoint oldBp, HBreakPoint newBp) {
        if (newBp.getWidth() <= HBreakPoint.LARGE.getWidth()) {
            setPadding(new Insets(5));
            BorderPane.setMargin(itemList, new Insets(0, 20, 0, 0));
        } else {
            setPadding(new Insets(50));
            BorderPane.setMargin(itemList, new Insets(0, 40, 0, 0));
        }
    }

    @Override
    public void selectItem(I item) {
        itemList.selectItem(item);
    }
}
