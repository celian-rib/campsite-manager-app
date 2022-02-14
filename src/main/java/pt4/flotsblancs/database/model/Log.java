package pt4.flotsblancs.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



import lombok.*;

@DatabaseTable(tableName = "logs")
public class Log {

    @Getter
    @Setter
    @DatabaseField(columnName = "id_historique")
    private int id_historique;

    @Getter
    @Setter
    @DatabaseField(columnName = "id_compte")
    private int id_compte;

    @Getter
    @Setter
    @DatabaseField(columnName = "action")
    private String action;

    public Log() {}
}
