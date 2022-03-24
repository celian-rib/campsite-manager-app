package pt4.flotsblancs.database.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pt4.flotsblancs.scenes.utils.PriceUtils;

@AllArgsConstructor
public enum Service {
    NONE("Aucun", 000),
    WATER_ONLY("Eau", 200),
    ELECTRICITY_ONLY("Electricité", 200),
    WATER_AND_ELECTRICITY("Eau et électricité", 400);

    @Getter
    private String name;

    @Getter
    private int pricePerDay;

    /**
     * Permet de savoir si ce service est compatible avec un service 
     * 
     * @param other service avec lequel se comparer
     * @return
     */
    public boolean isCompatibleWithCampService(Service other) {
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
        return name + " " + PriceUtils.priceToString(pricePerDay)+ "€/j";
    }
}
