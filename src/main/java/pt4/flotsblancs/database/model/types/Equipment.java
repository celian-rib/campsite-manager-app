package pt4.flotsblancs.database.model.types;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Equipment {
    TENT("Tente"), CAMPINGCAR("Campingcar"), TENT_AND_CAMPINGCAR("Campingcar et tente"), MOBILHOME(
            "Mobilhome");

    @Getter
    private String name;

    public boolean isCompatible(Equipment other) {
        // Rework complet vis à vis de tests
        if (this == other)
            return true;
        if (this == Equipment.TENT_AND_CAMPINGCAR || this == TENT || this == CAMPINGCAR)
            return other == TENT_AND_CAMPINGCAR || other == TENT || other == CAMPINGCAR;
        return false;
    }

    // TODO bouger ça dans campground comme service
    public List<Equipment> getCompatibles() {
        var list = new ArrayList<Equipment>();
        if (this == Equipment.TENT_AND_CAMPINGCAR) {
            list.add(Equipment.TENT_AND_CAMPINGCAR);
            list.add(Equipment.TENT);
            list.add(Equipment.CAMPINGCAR);
        } else {
            list.add(this);
        }
        return list;
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
