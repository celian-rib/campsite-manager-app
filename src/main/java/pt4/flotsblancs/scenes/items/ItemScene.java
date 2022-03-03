package pt4.flotsblancs.scenes.items;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
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
import pt4.flotsblancs.components.ItemList;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.breakpoints.BreakPointListener;
import pt4.flotsblancs.scenes.breakpoints.BreakPointManager;
import pt4.flotsblancs.scenes.breakpoints.HBreakPoint;
import pt4.flotsblancs.scenes.utils.ToastType;

public abstract class ItemScene<I extends Item> extends BorderPane
        implements IScene, BreakPointListener {

    private ItemList<I> itemList;
    private MFXProgressSpinner loading;

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
     * Met en place le BorderPane contenant à gauche la liste des items et à droite l'item
     * selectionné
     */
    @Override
    public void start() {
        itemList = new ItemList<I>(this);
        loading = new MFXProgressSpinner();
        setLeft(loading);
        setCenter(null);

        BorderPane.setMargin(itemList, new Insets(0, 40, 0, 0));
        BreakPointManager.addListener(this);
    }

    @Override
    public void onFocus() {
        final Task<List<I>> updateListTask = new Task<List<I>>() {
            protected java.util.List<I> call() throws Exception {
                var allItems = queryAll();
                Platform.runLater(() -> {
                    try {
                        // Mise à jour de la liste
                        itemList.updateItems(allItems);
                        setLeft(itemList);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                });
                return allItems;
            };

            protected void succeeded() {
                
            };

            protected void failed() {};

            
        };
        
        new Thread(updateListTask).start();

        // catch (SQLRecoverableException e) {
        //     System.err.println(e);
        //     Router.showToast(ToastType.ERROR, "Erreur de connexion");
        //     Router.goToScreen(Routes.CONN_FALLBACK);
        // } catch (SQLException e) {
        //     System.err.println(e);
        //     Router.showToast(ToastType.ERROR, "Erreur de chargement des données");
        // } 
        
    }

    /**
     * Met à jour le conteneur droit de la page, affichant les informations de l'item sélectionné
     * 
     * @param item item selectionné qui doit être affiché
     */
    public void updateContainer(I item) {
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

        Region container = createContainer(item);
        container.setBackground(background);

        stack.getChildren().addAll(shadowPane, container);

        setCenter((Parent) stack);
    }

    @Override
    public void onHorizontalBreak(HBreakPoint oldBp, HBreakPoint newBp) {
        if (newBp.getWidth() <= HBreakPoint.LARGE.getWidth()) {
            setPadding(new Insets(5));
        } else {
            setPadding(new Insets(50));
        }
    }
}
