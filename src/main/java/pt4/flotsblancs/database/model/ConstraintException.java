package pt4.flotsblancs.database.model;

import lombok.Getter;

public class ConstraintException extends Exception {

    /**
     * Permet de savoir si la valeur liée à la contrainte a été modifiée par effet de bord dans le
     * but corriger cette contrainte. Cela peut être utilisé pour gérer les dépendances cycliques
     * entres les contraintes et débloquer certaines situations.
     */
    @Getter
    private final boolean valueHasBeenAltered;

    public ConstraintException(final String message, boolean valueAltered) {
        super(message);
        this.valueHasBeenAltered = valueAltered;
    }
}
