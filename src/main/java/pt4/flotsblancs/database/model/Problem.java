package pt4.flotsblancs.database.model;

import java.util.Date;

import lombok.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@EqualsAndHashCode
@NoArgsConstructor
@DatabaseTable(tableName = "problems")
public class Problem {

    public enum ProblemStatus {
        OPEN, OPEN_URGENT, SOLVED
    }

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private String description;

    @Getter
    @DatabaseField(canBeNull = false)
    private ProblemStatus status = ProblemStatus.OPEN;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "start_date")
    private Date startDate = new Date();

    @Getter
    @DatabaseField(columnName = "end_date")
    private Date endDate;

    @Getter
    @Setter
    @DatabaseField(columnName = "last_update_date")
    private Date lastUpdateDate;

    @Getter
    @Setter
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Client client;

    @Getter
    @Setter
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private CampGround campground;

    @Getter
    @Setter
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Reservation reservation;

    public void setStatus(ProblemStatus newStatus) {
        this.status = newStatus;
        if (newStatus == ProblemStatus.SOLVED) {
            this.endDate = new Date();
            this.lastUpdateDate = this.endDate;
        } else {
            this.endDate = null;
        }
    }

    public void setStartDate(Date date){
        this.startDate =  date;
        this.lastUpdateDate = date;
    }

    @Override
    public String toString() {
        return "Problème #" + id + "  " + description;
    }
}
