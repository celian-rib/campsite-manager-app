package pt4.flotsblancs.database.model;

import lombok.*;
import pt4.flotsblancs.database.Database;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.SQLException;
import java.util.List;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

@DatabaseTable(tableName = "users")
public class User {

    @Getter
    @Setter
    @DatabaseField(generatedId = true, columnName = "user_id")
    private int userId;

    @Getter
    @Setter
    @DatabaseField(columnName = "login")
    private String login;

    @Getter
    @Setter
    @DatabaseField(columnName = "password")
    private String password;

    @Getter
    @Setter
    @DatabaseField(columnName = "is_admin")
    private int admin;

    @Getter
    @Setter
    @DatabaseField(columnName = "name")
    private String name;

    @Getter
    @Setter
    @DatabaseField(columnName = "first_name")
    private String firstName;

    private static User connected;

    public User() {} // Constructeur vide pour ORMLite

    public static User getConnected() {
        return connected;
    }

    public static boolean isConnected() {
        return connected != null;
    }

    public static void logOut() {
        if (!isConnected()) {
            log("/!\\ Impossible de deconnecter si il ny a pas d'utilisateur déjà connecté");
            return;
        }
        connected = null;
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
                if (u.getPassword().equals(mdp)) {
                    connected = u;
                    return true;
                }
            }
        } catch (SQLException e1) {
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
}
