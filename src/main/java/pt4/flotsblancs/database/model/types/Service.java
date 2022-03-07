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
        if (this == NONE)
            return true;
        if (this == WATER_AND_ELECTRICITY)
            return other == WATER_AND_ELECTRICITY;
        if (this == WATER_ONLY)
            return other == WATER_ONLY || other == WATER_AND_ELECTRICITY;
        if (this == ELECTRICITY_ONLY)
            return other == ELECTRICITY_ONLY || other == WATER_AND_ELECTRICITY;
        return false;
    }

    public List<Service> getCompatibles() {
        var list = new ArrayList<Service>();
        for (var v : Service.values())
            if (this.isCompatible(v))
                list.add(v);
        return list;
    }

    @Override
    public String toString() {
        return name;
    }
}
