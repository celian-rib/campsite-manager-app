package pt4.flotsblancs.database.model;

import pt4.flotsblancs.database.model.types.*;

import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.PTPalette;

import com.j256.ormlite.field.DatabaseField;

import lombok.*;


@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "campgrounds")
public class CampGround implements Item {

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
        if (this.allowedEquipments == Equipment.MOBILHOME)
            return new ArrayList<Service>() {
                {
                    add(Service.WATER_AND_ELECTRICITY);
                }
            };
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

    @Override
    public String getDisplayName() {
        return "Emplacement #" + getId();
    }

    @Override
    public String getSearchString() {
        return String.join(";", getDescription(), getAllowedEquipments().getName(),
                getProvidedServices().getName(), "#" + getId());
    }

    @Override
    public Color getStatusColor() {
        return PTPalette.GREEN;
    }
}
