package pt4.flotsblancs.database.model;

import lombok.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@NoArgsConstructor
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "stocks")
public class Stock {

    @Getter
    @ToString.Include
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private String item;

    @Setter
    @Getter
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
}
