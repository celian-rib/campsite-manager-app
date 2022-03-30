package pt4.flotsblancs.database.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javafx.scene.paint.Color;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.database.model.types.ProblemStatus;
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
    @DatabaseField(canBeNull = false,dataType = DataType.LONG_STRING)
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

    /**
     * crée un problème et lui donne des valeurs par défauts
     * l'action est loggé
     * 
     * @param status
     * @throws SQLException
     */
    public Problem(ProblemStatus status) throws SQLException {
        this.status = status;
        this.startDate = new Date();
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Ajout du nouveau problème #" + id);
        Database.getInstance().getProblemDao().create(this);
        Database.getInstance().getProblemDao().refresh(this);
    }

    /**
     * l'action est loggé
     * 
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification de la description du problème " + getDisplayName());
    }

    /**
     * l'action est loggé
     * 
     * @param client
     */

    public void setClient(Client client) {
        this.client = client;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification du client du problème " + getDisplayName());
    }

    /**
     * l'action est loggé
     * 
     * @param campGround
     */

    public void setCampground(CampGround campGround) {
        this.campground = campGround;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification de l'emplacement du problème " + getDisplayName());
    }

    /**
     * l'action est loggé
     * 
     * @param reservation
     */

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        this.lastUpdateDate = new Date();
        User.addlog(LogType.MODIFY, "Modification de la réservation du problème " + getDisplayName());
    }

    /**
     * l'action est loggé
     * 
     * @param newStatus
     */
    
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
        if(reservation != null)
            return "[R]  " + reservation.getDisplayName();
        if (client != null)
            return "[C]  " + client.getDisplayName();
        if (campground != null)
            return "[E]  " + campground.getDisplayName();
        return "Problème";
    }

    @Override
    public String getSearchString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return String.join(";", ""+this.id,formatter.format(this.startDate),
        this.endDate != null ? formatter.format(this.endDate) : "",
        this.client != null ? this.client.getFirstName() + ";" + this.client.getName() : "",
        this.campground != null ? ""+this.campground.getId() : "",
        this.reservation != null ? ""+this.reservation.getId() : "").trim().toLowerCase();
    }

    @Override
    public boolean isForeignCorrect() {
        return true;
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

    @Override
    public int compareTo(Item o) {
        Problem other = (Problem) o;
        
        return (status.getCompareScale() + getId()) - (other.status.getCompareScale() + getId());
    }
}
