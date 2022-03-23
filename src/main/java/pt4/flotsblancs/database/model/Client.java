package pt4.flotsblancs.database.model;

import java.sql.SQLException;
import java.util.Collection;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.scene.paint.Color;
import lombok.*;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.StatusColors;
import pt4.flotsblancs.scenes.utils.TxtFieldValidation;

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
        this.firstName = "NOUVEAU Prénom";
        this.name = "NOUVEAU Nom";
        this.addresse = "NOUVEAU Adresse";
        this.phone = "00 00 00 00 00";
        this.preferences = null;
        this.email = "jean.dupond@mail.com";
        Database.getInstance().getClientsDao().create(this);
        Database.getInstance().getClientsDao().refresh(this);
        User.addlog(LogType.ADD, "Ajout d'un nouveau client");
    }

    public void setPhone(String phone) throws ConstraintException {
        if (TxtFieldValidation.phoneValidation(phone)) {
            this.phone = phone;
            User.addlog(LogType.MODIFY, "Téléphone du client " + getDisplayName() + " changé pour " + phone);
            return;
        }
        throw new ConstraintException("Téléphone invalide, modification annulée", false);
    }

    public void setAddresse(String addresse) throws ConstraintException {
        if (!TxtFieldValidation.phoneValidation(addresse) && !TxtFieldValidation.emailValidation(addresse)) {
            User.addlog(LogType.MODIFY, "Addresse du client " + getDisplayName() + " changé pour " + addresse);
            this.addresse = addresse;
            return;
        }
        throw new ConstraintException("Adresse invalide, modification annulée", false);
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

    public void setEmail(String email) throws ConstraintException {
        if (TxtFieldValidation.emailValidation(email)) {
            User.addlog(LogType.MODIFY, "Email du client " + getDisplayName() + " changé pour " + email);
            this.email = email;
            return;
        }
        throw new ConstraintException("Email invalide, modification annulée", false);
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
        var hasProblem = problems.stream().anyMatch(p -> p.getEndDate() != null);
        return hasProblem ? StatusColors.RED : StatusColors.BLUE;
    }
}
