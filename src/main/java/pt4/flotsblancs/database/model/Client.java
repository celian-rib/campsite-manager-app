package pt4.flotsblancs.database.model;


import java.util.Collection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "clients")
public class Client {

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

}
