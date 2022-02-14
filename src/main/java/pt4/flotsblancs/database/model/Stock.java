package pt4.flotsblancs.database.model;

import lombok.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "stocks")
public class Stock {

    @Getter
    @DatabaseField(generatedId = true, columnName = "stock_id")
    private int stockId;

    @Getter
    @DatabaseField(columnName = "item")
    private String item;

    @Setter
    @Getter
    @DatabaseField(columnName = "quantity")
    private int quantity;

    @Setter
    @Getter
    @DatabaseField(columnName = "quantity_alert_treshold")
    private int quantityAlertThreshold;

    @Setter
    @Getter
    @DatabaseField(columnName = "storage_location")
    private String storageLocation;

    public Stock() {}

}
