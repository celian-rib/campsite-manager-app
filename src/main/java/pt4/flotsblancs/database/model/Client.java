package pt4.flotsblancs.database.model;

import java.util.Collection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;
import pt4.flotsblancs.scenes.items.Item;

@NoArgsConstructor
@EqualsAndHashCode
@DatabaseTable(tableName = "clients")
public class Client implements Item {

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @DatabaseField(uniqueCombo = true, canBeNull = false)
    private String name;

    @Getter
    @Setter
    @DatabaseField(uniqueCombo = true, canBeNull = false, columnName = "first_name")
    private String firstName;

    @Getter
    @Setter
    @DatabaseField
    private String preferences;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private String addresse;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private String phone;

    @Getter
    @ForeignCollectionField(eager = false)
    private Collection<Problem> problems;

    @Getter
    @Setter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Reservation> reservations;

    public Client(String name, String firstName) {
        this.name = name;
        this.firstName = firstName;
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

}
