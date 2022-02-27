package pt4.flotsblancs.database.model;

import pt4.flotsblancs.database.model.types.*;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import lombok.*;


@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "campgrounds")
public class CampGround {

    @Getter
    @DatabaseField(generatedId = true)
    @EqualsAndHashCode.Include
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
    @DatabaseField(canBeNull = false, columnName = "provided_services")
    private Service providedServices;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "allowed_equipments")
    private Equipment allowedEquipments;

    @Override
    public String toString() {
        return "#" + id + "   " + pricePerDays + "€/j  " + allowedEquipments.getChar();
    }
}
