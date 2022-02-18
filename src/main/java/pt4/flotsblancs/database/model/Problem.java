package pt4.flotsblancs.database.model;

import java.util.Date;

import lombok.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@EqualsAndHashCode
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "problems")
public class Problem {

    public enum ProblemStatus {
        OPEN, OPEN_URGENT, SOLVED
    }

    @Getter
    @ToString.Include
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private String description;

    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private ProblemStatus status = ProblemStatus.OPEN;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "start_date")
    private Date startDate = new Date();

    @Getter
    @DatabaseField(columnName = "end_date")
    private Date endDate;

    @Getter
    @DatabaseField(columnName = "last_update_date")
    private Date lastUpdateDate;

    @Getter
    @Setter
    @DatabaseField(foreign = true)
    private Client client;

    @Getter
    @Setter
    @DatabaseField(foreign = true)
    private CampGround campground;

    @Getter
    @Setter
    @DatabaseField(foreign = true)
    private Reservation reservation;

    public void setStatus(ProblemStatus newStatus) {
        this.status = newStatus;
        if (newStatus == ProblemStatus.SOLVED) {
            this.endDate = new Date();
        } else {
            this.endDate = null;
        }
    }
}
