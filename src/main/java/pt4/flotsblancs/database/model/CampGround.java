package pt4.flotsblancs.database.model;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import lombok.*;


@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@DatabaseTable(tableName = "campgrounds")
public class CampGround {

    @Getter
    @DatabaseField(generatedId = true)
    @ToString.Include
    private int id;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private String description;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "price_per_day")
    private float pricePerDays;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private float surface;

    @Getter
    @Setter
    @DatabaseField(columnName = "provided_equipments")
    private String providedEquipments;

}
