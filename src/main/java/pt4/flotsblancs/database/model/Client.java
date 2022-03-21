package pt4.flotsblancs.database.model;

import java.sql.SQLException;
import java.util.Collection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import javafx.scene.paint.Color;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.PTPalette;

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
        User.addlog(LogType.MODIFY, "Téléphone du client " + getDisplayName() + " changé pour " + phone);
        this.phone = phone;
    }

    public void setAddresse(String addresse) {
        User.addlog(LogType.MODIFY, "Addresse du client " + getDisplayName() + " changé pour " + addresse);
        this.addresse = addresse;
    }

    public void setPreferences(String preferences) {
        User.addlog(LogType.MODIFY, "Préférences du client " + getDisplayName() + " changé pour " + preferences);
        this.preferences = preferences;
    }

    public void setFirstName(String firstName) {
        User.addlog(LogType.MODIFY, "Prénom du client " + getDisplayName() + " changé pour " + firstName);
        this.firstName = firstName;
    }

    public void setName(String name) {
        User.addlog(LogType.MODIFY, "Nom du client " + getDisplayName() + " changé pour " + name);
        this.name = name;
    }
    
    public void setEmail(String email) {
        User.addlog(LogType.MODIFY, "Email du client " + getDisplayName() + " changé pour " + email);
        this.email = email;
    }

    public Reservation getOpenReservation() {
        return reservations.stream().filter(r -> r.getPaymentDate() == null).findFirst()
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
        return new StringBuilder()
                .append(this.id).append(';')
                .append(this.firstName).append(';')
                .append(this.name).append(';')
                .append(this.addresse).append(';')
                .append(this.phone).append(';')
                .toString().trim().toLowerCase();
    }

    @Override
    public Color getStatusColor() {
        return problems.size() > 0 ? PTPalette.RED : PTPalette.BLUE;
    }

}
