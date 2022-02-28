package pt4.flotsblancs.database.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CashBack {
    NONE("Aucune", 1), 
    TWENTY_PERCENT("20%", 0.8f), 
    THIRTY_PERCENT("30%", 0.7f), 
    FOURTY_PERCENT("40%", 0.6f), 
    FIFTY_PERCENT("50%", 0.5f);

    @Getter
    private String name;

    @Getter
    private float reduction;

    @Override
    public String toString() {
        return name;
    }
}
