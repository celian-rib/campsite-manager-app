package pt4.flotsblancs.database.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Service {
    NONE("Aucun", 0),
    WATER_ONLY("Eau", 2),
    ELECTRICITY_ONLY("Electricité", 2),
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

    @Override
    public String toString() {
        return name + " " + pricePerDay + "€/j";
    }
}
