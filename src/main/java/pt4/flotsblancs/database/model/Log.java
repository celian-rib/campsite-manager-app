package pt4.flotsblancs.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "logs")
public class Log {

    @Getter
    @ToString.Include
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private User user;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(canBeNull = false)
    private String action;
}
