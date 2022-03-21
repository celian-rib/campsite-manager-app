package pt4.flotsblancs.database.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;
import pt4.flotsblancs.database.model.types.LogType;

@NoArgsConstructor
@EqualsAndHashCode
@DatabaseTable(tableName = "logs")
public class Log {

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private User user;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private String message;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private Date date;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false)
    private LogType type;

    @Override
    public String toString() {
        return user +" "+message+" "+date+" "+type;
    }
}
