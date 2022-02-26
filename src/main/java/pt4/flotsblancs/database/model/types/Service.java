package pt4.flotsblancs.database.model.types;

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
}
