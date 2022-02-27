package pt4.flotsblancs.database.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Equipment {
    TENT("Tente"), 
    CAMPINGCAR("Campingcar"), 
    TENT_AND_CAMPINGCAR("Campingcar et tente"), 
    MOBILHOME("Mobilhome");

    @Getter
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
