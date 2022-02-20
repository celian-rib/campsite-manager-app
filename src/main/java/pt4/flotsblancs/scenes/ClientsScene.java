package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.items.ItemList;
import pt4.flotsblancs.scenes.items.containers.ClientItemContainer;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ClientsScene extends BorderPane implements IScene {

    @Override
    public String getName() {
        return "Clients";
    }

    @Override
    public boolean showNavBar() {
        return true;
    }

    @Override
    public void onFocus() {

    }

    @Override
    public void onUnfocus() {

    }

    @Override
    public void start() {
        try {
            // Création du panel à droite qui affiche les information d'un client en particulié
            ClientItemContainer itemContainer = new ClientItemContainer();

            // Récupération de tous les clients
            // TODO -> ne pas fetch les clients au lancement de l'app mais à l'ouverture de la page ?
            List<Client> clients = Database.getInstance().getClientsDao().queryForAll();

            // Création panel à gauche qui affiche la liste de clients
            ItemList<Client> itemList = new ItemList<Client>(clients, itemContainer);

            // On ajoute les 2 panels à la page
            setLeft(itemList);
            setCenter(itemContainer);

            setMargin(itemList, new Insets(30));
            setPadding(new Insets(30));
        } catch (SQLException e) {
            System.err.println(e);
            // TODO: A change le message
            Router.showToast(ToastType.ERROR, e.getMessage());
        }
    }
}
