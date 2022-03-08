package pt4.flotsblancs.database.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LogType {
    ADD("Ajout"),
    DELETE("Suppression"),
    MODIFY("Modification"),
    SUDO("Administration"); 

    @Getter
    private String name;
}
