package pt4.flotsblancs.scenes.utils;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;

public class ExceptionHandler {

    /**
     * Permet de gérer une exception de type "update"
     * 
     * Le contenu de l'erreur est affiché à l'utilisateur et l'application pas sur
     * la page de fallback
     * 
     * @param e
     */
    public static void updateIssue(SQLException e) {
        if (e instanceof SQLRecoverableException) {
            connectionIssue((SQLRecoverableException) e);
            return;
        }
        e.printStackTrace();
        Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
        Router.goToScreenDirty(Routes.CONN_FALLBACK);
    }

    /**
     * Permet de gérer une exception de type "errur de connexion"
     * 
     * Le contenu de l'erreur est affiché à l'utilisateur et l'application pas sur
     * la page de fallback
     * 
     * @param e
     */
    public static void connectionIssue(SQLRecoverableException e) {
        e.printStackTrace();
        Router.showToast(ToastType.ERROR, "Erreur de connexion");
        Router.goToScreenDirty(Routes.CONN_FALLBACK);
    }

    /**
     * Permet de gérer une exception de type "errur de chargement"
     * 
     * Le contenu de l'erreur est affiché à l'utilisateur et l'application pas sur
     * la page de fallback
     * 
     * @param e
     */
    public static void loadIssue(SQLException e) {
        if (e instanceof SQLRecoverableException) {
            connectionIssue((SQLRecoverableException) e);
            return;
        }
        e.printStackTrace();
        Router.showToast(ToastType.ERROR, "Erreur de chargement");
        Router.goToScreenDirty(Routes.CONN_FALLBACK);
    }
}
