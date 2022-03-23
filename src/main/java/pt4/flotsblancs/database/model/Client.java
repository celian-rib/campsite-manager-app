package pt4.flotsblancs.database.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.SQLException;
import java.util.Collection;
import javafx.scene.paint.Color;
import lombok.*;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.StatusColors;

@EqualsAndHashCode
@NoArgsConstructor
@DatabaseTable(tableName = "clients")
public class Client implements Item {

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @DatabaseField(canBeNull = false)
    private String name;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "first_name")
    private String firstName;

    @Getter
    @DatabaseField
    private String preferences;

    @Getter
    @DatabaseField(canBeNull = false)
    private String addresse;

    @Getter
    @DatabaseField(canBeNull = false)
    private String phone;

    @Getter
    @DatabaseField(canBeNull = false)
    private String email;

    @Getter
    @ForeignCollectionField(eager = false)
    private Collection<Problem> problems;

    @Getter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Reservation> reservations;

    public Client(String name) throws SQLException {
        this.firstName = "Jean";
        this.name = "Dupond";
        this.addresse = "Adresse";
        this.phone = "00 00 00 00 00";
        this.preferences = null;
        Database.getInstance().getClientsDao().create(this);
        Database.getInstance().getClientsDao().refresh(this);
        User.addlog(LogType.ADD, "Ajout d'un nouveau client");
    }

    public void setPhone(String phone) {
        User.addlog(
                LogType.MODIFY,
                "Téléphone du client " + getDisplayName() + " changé pour " + phone);
        this.phone = phone;
    }

    public void setAddresse(String addresse) {
        User.addlog(
                LogType.MODIFY,
                "Addresse du client " + getDisplayName() + " changé pour " + addresse);
        this.addresse = addresse;
    }

    public void setPreferences(String preferences) {
        User.addlog(
                LogType.MODIFY,
                "Préférences du client " +
                        getDisplayName() +
                        " changé pour " +
                        preferences);
        this.preferences = preferences;
    }

    public void setFirstName(String firstName) {
        User.addlog(
                LogType.MODIFY,
                "Prénom du client " + getDisplayName() + " changé pour " + firstName);
        this.firstName = firstName;
    }

    public void setName(String name) {
        User.addlog(
                LogType.MODIFY,
                "Nom du client " + getDisplayName() + " changé pour " + name);
        this.name = name;
    }

    public void setEmail(String email) {
        User.addlog(
                LogType.MODIFY,
                "Email du client " + getDisplayName() + " changé pour " + email);
        this.email = email;
    }

    public Reservation getOpenReservation() {
        return reservations
                .stream()
                .filter(r -> r.getPaymentDate() == null)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return toString();
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.name;
    }

    @Override
    public String getSearchString() {
        return String
                .join(
                        ";",
                        "" + this.id,
                        this.firstName,
                        this.name,
                        this.addresse,
                        this.phone)
                .trim()
                .toLowerCase();
    }

    @Override
    public boolean isForeignCorrect() {
        return reservations != null && problems != null;
    }

    @Override
    public Color getStatusColor() {
        // TODO -> rouge que si les problèmes sont ouverts
        return problems.size() > 0 ? StatusColors.RED : StatusColors.BLUE;
    }
}
