package pt4.flotsblancs.database.model;

import lombok.*;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.utils.ToastType;
import com.j256.ormlite.table.DatabaseTable;

import java.security.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@DatabaseTable(tableName = "users")
public class User {

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = false)
    private String login;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = false)
    private String password;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = false, columnName = "is_admin")
    private boolean isAdmin;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(uniqueCombo = true, canBeNull = false)
    private String name;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(uniqueCombo = true, canBeNull = false, columnName = "first_name")
    private String firstName;

    @Getter
    private static User connected;

    public static boolean isConnected() {
        return connected != null;
    }

    public static void logOut() {
        if (!isConnected())
            log("/!\\ Impossible de deconnecter si il n\'y a pas d'utilisateur déjà connecté");
        connected = null;
    }

    public static String sha256(final String base) {
        // SOURCE : https://stackoverflow.com/a/11009612/12647299
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean logIn(String id, String mdp) {
        if (isConnected()) {
            log("/!\\ Un utilisateur est déjà connecté");
            return true;
        }
        QueryBuilder<User, String> queryBuilder;
        try {
            queryBuilder = Database.getInstance().getUsersDao().queryBuilder();
            queryBuilder.where().eq("login", id);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            List<User> accountList = Database.getInstance().getUsersDao().query(preparedQuery);

            for (User u : accountList) {
                String s = sha256(mdp);

                if (u.getPassword().equals(s)) {
                    connected = u;
                    return true;
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return firstName + " " + name;
    }

    /**
     * Permet de logger les actions du UserStore pour le débug
     * 
     * @param message : Message de débug
     */
    private static void log(String message) {
        // TODO vrai système de logging
        System.out.println("[UserStore] " + message);
    }

    public void addlog(LogType type, String message) {
        var log = new Log();
        log.setType(type);
        log.setMessage(message);
        log.setUser(this);
        log.setDate(new Date());
        try {
            Database.getInstance().getLogDao().create(log);
            Database.getInstance().getLogDao().refresh(log);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+log);
            Router.showToast(ToastType.ERROR, "Erreur interne (Logging)");
            Router.goToScreen(Routes.CONN_FALLBACK);
        }
    }
}
