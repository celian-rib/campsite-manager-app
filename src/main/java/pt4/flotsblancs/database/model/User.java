package pt4.flotsblancs.database.model;

import lombok.*;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.ExceptionHandler;
import pt4.flotsblancs.scenes.utils.StatusColors;

import com.j256.ormlite.table.DatabaseTable;

import javafx.scene.paint.Color;

import java.security.*;
import java.sql.SQLException;
import java.text.Collator;
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

    /**
     * crée un nouvel utilisateur et lui donne des valeurs par défaut
     * l'action est loggé
     * 
     * @param name
     * @throws SQLException
     */
    public User(String name) throws SQLException {
        this.firstName = "NOUVEAU Prénom";
        this.name = "NOUVEAU Nom";
        this.isAdmin = false;
        this.login = "NOUVEAU login";
        this.password = User.sha256("flots-blancs");
        this.weeklyHours = 35;
        Database.getInstance().getUsersDao().create(this);
        Database.getInstance().getUsersDao().refresh(this);
        User.addlog(LogType.ADD, "Ajout d'un nouvel utilisateur");
    }

    /**
     * vérifie si un utilisateur est connecté
     * 
     * @return
     */

    public static boolean isConnected() {
        return connected != null;
    }

    /**
     * déconnecte un utilisateur si il y en a un
     */

    public static void logOut() {
        if (!isConnected())
            log("/!\\ Impossible de deconnecter si il n\'y a pas d'utilisateur déjà connecté");
        connected = null;
    }


    /**
     * renvoie la version haché d'un mot de passe 
     * 
     * @param base
     * @return
     */

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

    /**
     * connecte un utilisateur si les identifiants sont correctes
     * 
     * @param id
     * @param mdp
     * @return
     */

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

    @Override
    public boolean isForeignCorrect() {
        return true;
    }

    @Override
    public Color getStatusColor() {
        return this.isAdmin ? StatusColors.RED : StatusColors.BLUE;
    }

    @Override
    public int compareTo(Item o) {

        Collator c = Collator.getInstance(java.util.Locale.FRANCE);
        c.setStrength(Collator.PRIMARY);
        User other = (User)o;
        int val = c.compare(this.getDisplayName(),other.getDisplayName());
        return val;
    }
}
