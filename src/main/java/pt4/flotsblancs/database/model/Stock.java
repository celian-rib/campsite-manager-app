package pt4.flotsblancs.database.model;

import lombok.*;
import pt4.flotsblancs.database.model.types.LogType;
import javafx.scene.paint.Color;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.StatusColors;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@NoArgsConstructor
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "stocks")
public class Stock implements Item {

    @Getter
    @ToString.Include
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private String item;

    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private int quantity;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "quantity_alert_threshold")
    private int quantityAlertThreshold;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "storage_location")
    private String storageLocation;

    public void setItem(String item) {
        User.addlog(LogType.ADD, "Mise à jour du stock " + this.item + " à " + item);
        this.item = item;
    }

    public void setQuantity(int quantity) {
        User.addlog(LogType.ADD, "Mise à jour de la quantité de " + item + " de " + this.quantity + " à " + quantity);
        this.quantity = quantity;
    }

    public void setQuantityAlertThreshold(int quantityAlert) {
        User.addlog(LogType.ADD,
                "Mise à jour de l'alerte de " + item + " de " + this.quantityAlertThreshold + " à " + quantity);
        this.quantityAlertThreshold = quantityAlert;
    }

    public void setStorageLocation(String location) {
        User.addlog(LogType.ADD, "Mise à jour de l'emplacement de stockage de " + item + " de " + this.storageLocation
                + " à " + location);
        this.storageLocation = location;
    }

    @Override
    public String getDisplayName() {
        return item;
    }

    @Override
    public String getSearchString() {
        return String.join(";",this.item,this.storageLocation).trim().toLowerCase();
    }

    @Override
    public boolean isForeignCorrect() {
        return true;
    }
    
    @Override
    public Color getStatusColor() {
        return this.quantity < this.quantityAlertThreshold ? StatusColors.RED : StatusColors.GREEN;
    }

    @Override
    public int compareTo(Item o) {
        Stock other = (Stock)o;
        int val = (this.getStatusColor() == StatusColors.GREEN ? 100 : 1000) + this.getId();
        int otherVal = (other.getStatusColor() == StatusColors.GREEN ? 100 : 1000) + other.getId();
        return val - otherVal;
        

    }

}
