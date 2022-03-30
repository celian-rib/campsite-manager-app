package pt4.flotsblancs.scenes.utils;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;

public class ExceptionHandler {
    public static void updateIssue(SQLException e) {
        if(e instanceof SQLRecoverableException) {
            connectionIssue((SQLRecoverableException) e);
            return;
        }
        e.printStackTrace();
        Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
        Router.goToScreenDirty(Routes.CONN_FALLBACK);
    }

    public static void connectionIssue(SQLRecoverableException e) {
        e.printStackTrace();
        Router.showToast(ToastType.ERROR, "Erreur de connexion");
        Router.goToScreenDirty(Routes.CONN_FALLBACK);
    }
    
    public static void loadIssue(SQLException e) {
        if(e instanceof SQLRecoverableException) {
            connectionIssue((SQLRecoverableException) e);
            return;
        }
        e.printStackTrace();
        Router.showToast(ToastType.ERROR, "Erreur de chargement");
        Router.goToScreenDirty(Routes.CONN_FALLBACK);
    }
}
