package pt4.flotsblancs.database.model;

import pt4.flotsblancs.database.model.types.*;

import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * @return Tous les services possibles pour cet emplacement
     */
    public List<Service> getCompatiblesServices() {
        var list = new ArrayList<Service>();
        for (var v : Service.values())
            if (v.isCompatibleWithCampService(providedServices))
                list.add(v);
        return list;
    }
    
    /**
     * @return Tous les services possibles pour cet emplacement
     */
    public List<Equipment> getCompatiblesEquipments() {
        var list = new ArrayList<Equipment>();
        for (var v : Equipment.values())
            if (v.isCompatibleWithCampEquipment(allowedEquipments))
                list.add(v);
        return list;
    }
}
