package pt4.flotsblancs.database.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pt4.flotsblancs.database.model.types.ProblemStatus;
import pt4.flotsblancs.scenes.items.Item;

@EqualsAndHashCode
@NoArgsConstructor
@DatabaseTable(tableName = "problems")
public class Problem implements Item {

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
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
    @DatabaseField(columnName = "last_update_date")
    private Date lastUpdateDate;

    @Getter
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Client client;

    @Getter
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private CampGround campground;

    @Getter
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Reservation reservation;

    public void setDescription(String description) {
        this.description = description;
        this.lastUpdateDate = new Date();
    }

    public void setClient(Client client) {
        this.client = client;
        this.lastUpdateDate = new Date();
    }

    public void setCampground(CampGround campGround) {
        this.campground = campGround;
        this.lastUpdateDate = new Date();
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        this.lastUpdateDate = new Date();
    }

    public void setStatus(ProblemStatus newStatus) {
        this.status = newStatus;
        this.endDate = newStatus == ProblemStatus.SOLVED ? new Date() : null;
        this.lastUpdateDate = new Date();
    }

    public void setStartDate(Date date) {
        this.startDate = date;
        this.lastUpdateDate = new Date();
    }

    @Override
    public String toString() {
        return "Problème #" + id + "  " + description;
    }

    @Override
    public String getDisplayName() {
        if (client != null) {
            String returnMsg = "[Pb client] " + client;
            if (returnMsg.length() > 30)
                return returnMsg.substring(0, 30) + " [...]";
            return returnMsg;
        } else {
            return "Problème sans client";
        }
    }
}
