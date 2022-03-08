package pt4.flotsblancs.database.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Equipment {
    TENT("Tente"), CAMPINGCAR("Campingcar"), TENT_AND_CAMPINGCAR("Campingcar et tente"), MOBILHOME(
            "Mobilhome");

    @Getter
    private String name;

    public boolean isCompatibleWithCampEquipment(Equipment other) {
        // Rework complet vis à vis de tests
        if (this == CAMPINGCAR)
            return other == CAMPINGCAR || other == TENT_AND_CAMPINGCAR;
        if (this == MOBILHOME)
            return other == MOBILHOME;
        if (this == TENT)
            return other == TENT || other == TENT_AND_CAMPINGCAR;
        if (this == TENT_AND_CAMPINGCAR)
            return other == TENT_AND_CAMPINGCAR;
        return false;
    }

    public String getChar() {
        switch (this) {
            case TENT_AND_CAMPINGCAR:
                return "TC";
            case TENT:
                return "T";
            case CAMPINGCAR:
                return "C";
            default:
                return "M";
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
