package pt4.flotsblancs.database.model;

import pt4.flotsblancs.database.model.types.*;

import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.PriceUtils;
import pt4.flotsblancs.scenes.utils.StatusColors;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

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
    private int pricePerDays;

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

    @Getter
    @ForeignCollectionField(eager = false)
    private Collection<Problem> problems;

    @Override
    public String toString() {
        return "#" + id + "   " + PriceUtils.priceToString(pricePerDays) + "€/j  " + allowedEquipments.getChar();
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
    public boolean isForeignCorrect() {
        return true;
    }

    @Override
    public Color getStatusColor() {
        return StatusColors.GREEN;
    }

    @Override
    public int compareTo(Item o) {
        var other = (CampGround)o;
        return (int)(other.surface - this.surface);
    }
}
