package pt4.flotsblancs.orm.model;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import lombok.*;


@DatabaseTable(tableName = "campgrounds")
public class CampGround {

    @Getter
    @DatabaseField(generatedId = true, columnName = "campground_id")
    private int campgroundId;

    @Getter
    @Setter
    @DatabaseField(columnName = "description")
    private String description;

    @Getter
    @Setter
    @DatabaseField(columnName = "location_description")
    private String lacationDescription;

    @Getter
    @Setter
    @DatabaseField(columnName = "price_per_days")
    private float pricePerDays;

    @Getter
    @Setter
    @DatabaseField(columnName = "surface")
    private float surface;

    @Getter
    @Setter
    @DatabaseField(columnName = "provided_equipments")
    private String providedEquipments;

    public CampGround() {}
}
