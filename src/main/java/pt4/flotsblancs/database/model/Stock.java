package pt4.flotsblancs.database.model;

import lombok.*;
import pt4.flotsblancs.scenes.items.Item;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@NoArgsConstructor
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "stocks")
public class Stock implements Item{

    @Getter
    @ToString.Include
    @DatabaseField(generatedId = true)
    private int id;

    @Setter
    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private String item;

    @Setter
    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private int quantity;

    @Setter
    @Getter
    @DatabaseField(canBeNull = false, columnName = "quantity_alert_threshold")
    private int quantityAlertThreshold;

    @Setter
    @Getter
    @DatabaseField(canBeNull = false, columnName = "storage_location")
    private String storageLocation;

    @Override
    public String getDisplayName() {
        return item;
    }

    @Override
    public String getSearchString() {
        return new StringBuilder()
            .append(this.id).append(';')
            .append(this.item).append(';')
            .append(this.storageLocation).append(';')
            .toString().trim().toLowerCase();
    }
}
