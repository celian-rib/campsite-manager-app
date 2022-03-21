package pt4.flotsblancs.database.model;

import lombok.*;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
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
public class User implements Item {

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
    @DatabaseField(canBeNull = false, columnName = "is_admin")
    private boolean isAdmin;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private String name;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "first_name")
    private String firstName;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "weekly_hours", defaultValue = "35")
    private Integer weeklyHours;

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
        System.out.println("[UserStore] " + message);
    }

    /**
     * Permet d'ajout un log d'action faite par l'utilisateur actuellement connecté (Rien ne se
     * passe si aucun utilisateur n'esta atuellment connecté)
     * 
     * @param type type d'action faite par l'utilisateur
     * @param message message décrivant l'action
     */
    public static void addlog(LogType type, String message) {
        if (User.getConnected() == null)
            return;
        var log = new Log();
        log.setType(type);
        log.setMessage(message);
        try {
            log.setDate(new Date());
            Database.getInstance().getLogDao().refresh(log);
            log.setUser(User.getConnected());
            Database.getInstance().getLogDao().create(log);
        } catch (SQLException e) {
            ExceptionHandler.loadIssue(e);
        }
    }

    @Override
    public String getDisplayName() {
        return getFirstName() + " " + getName();
    }

    @Override
    public String getSearchString() {
        return String.join(";", getFirstName(), getName(), getLogin(), "#"+getId());
    }
}
