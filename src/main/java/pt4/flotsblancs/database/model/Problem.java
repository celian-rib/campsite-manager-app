package pt4.flotsblancs.database.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.database.model.types.ProblemStatus;
import javafx.scene.paint.Color;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.StatusColors;

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

    public Problem(ProblemStatus status) throws SQLException {
        this.status = status;
        this.startDate = new Date();
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Ajout du nouveau problème #" + id);
        Database.getInstance().getProblemDao().create(this);
        Database.getInstance().getProblemDao().refresh(this);
    }

    public void setDescription(String description) {
        this.description = description;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification de la description du problème " + getDisplayName());
    }

    public void setClient(Client client) {
        this.client = client;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification du client du problème " + getDisplayName());
    }

    public void setCampground(CampGround campGround) {
        this.campground = campGround;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification de l'emplacement du problème " + getDisplayName());
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification de la réservation du problème " + getDisplayName());
    }

    public void setStatus(ProblemStatus newStatus) {
        this.status = newStatus;
        this.endDate = newStatus == ProblemStatus.SOLVED ? new Date() : null;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY,
                "Modification du status à " + newStatus.displayName + " pour le problème " + getDisplayName());
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

    @Override
    public String getSearchString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        var b = new StringBuilder()
                .append(this.id).append(';')
                .append(formatter.format(this.startDate)).append(';');
        if (this.endDate != null)
            b.append(formatter.format(this.endDate)).append(';');
        if (this.client != null)
            b.append(this.client.getFirstName()).append(';')
                    .append(this.client.getName()).append(';');
        if (this.campground != null)
            b.append(this.campground.getId()).append(';');
        if (this.reservation != null)
            b.append(this.reservation.getId()).append(';');
        return b.toString().trim().toLowerCase();
    }

    @Override
    public Color getStatusColor() {
        switch(status) {
            case OPEN:
                return StatusColors.BLUE;
            case OPEN_URGENT:
                return StatusColors.RED;
            default:
                return StatusColors.GREEN;
        }
    }
}
