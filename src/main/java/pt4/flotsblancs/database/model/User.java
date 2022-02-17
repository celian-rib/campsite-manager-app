package pt4.flotsblancs.database.model;

import lombok.*;

import pt4.flotsblancs.database.Database;
import com.j256.ormlite.table.DatabaseTable;

import java.security.*;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

@DatabaseTable(tableName = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User {

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(generatedId = true, columnName = "user_id")
    private int userId;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(columnName = "login")
    private String login;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @DatabaseField(columnName = "password")
    private String password;

    @Getter
    @Setter
    @DatabaseField(columnName = "is_admin")
    private boolean admin;

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
        if (!isConnected())
            log("/!\\ Impossible de deconnecter si il ny a pas d'utilisateur déjà connecté");
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
}
