package pt4.flotsblancs.database.model.types;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Service {
    NONE("Aucun", 0), 
    WATER_ONLY("Eau seulement", 2), 
    ELECTRICITY_ONLY("Electricité seulement", 2), 
    WATER_AND_ELECTRICITY("Eau et électricité", 4);

    @Getter
    private String name;

    @Getter
    private int pricePerDay;

    public boolean isCompatible(Service other) {
        if (this == other)
            return true;
        if (this == WATER_AND_ELECTRICITY)
            return other == WATER_AND_ELECTRICITY || other == WATER_ONLY || other == ELECTRICITY_ONLY;
        if (this == WATER_ONLY)
            return other == WATER_AND_ELECTRICITY || other == WATER_ONLY ;
        if (this == ELECTRICITY_ONLY)
            return other == WATER_AND_ELECTRICITY || other == ELECTRICITY_ONLY ;
        return false;
    }

    public List<Service> getCompatibles() {
        var list = new ArrayList<Service>();
        list.add(NONE);
        if (this == WATER_AND_ELECTRICITY) {
            list.add(WATER_AND_ELECTRICITY);
            list.add(WATER_ONLY);
            list.add(ELECTRICITY_ONLY);
        } else if(this == WATER_ONLY) {
            list.add(WATER_ONLY);
        } else if(this == ELECTRICITY_ONLY) {
            list.add(ELECTRICITY_ONLY);
        }
        return list;
    }

    @Override
    public String toString() {
        return name;
    }
}
