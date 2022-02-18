package pt4.flotsblancs.database.model;

import com.j256.ormlite.table.DatabaseTable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "reservations")
public class Reservation {

    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(columnName = "deposit_date")
    private Date depositDate;
    
    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(canBeNull = false, columnName = "start_date")
    private Date startDate;
    
    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(canBeNull = false, columnName = "end_date")
    private Date endDate;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(foreign = true, canBeNull = false)
    private Client client;
    
    @Getter
    @Setter
    @DatabaseField(foreign = true, canBeNull = false)
    private CampGround campground;
    
    @Getter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Problem> problems;
}
