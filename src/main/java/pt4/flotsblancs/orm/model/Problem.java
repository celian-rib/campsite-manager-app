package pt4.flotsblancs.orm.model;

import java.util.Date;

import lombok.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "problems")
public class Problem {

    @Getter
    @Setter
    @DatabaseField(generatedId = true, columnName = "problem_id")
    private int problemId;

    @Getter
    @Setter
    @DatabaseField(columnName = "description")
    private String description;

    @Getter
    @DatabaseField(columnName = "status")
    private String status;

    @Getter
    @DatabaseField(columnName = "start_date")
    private Date startDate;

    @Getter
    @Setter
    @DatabaseField(columnName = "end_date")
    private Date endDate;

    @Getter
    @DatabaseField(columnName = "last_update_date")
    private Date lastUpdateDate;

    public Problem() {
        setStatus("todo");
    }

    private void setStatus(String status) {
        // TO DO
        // Changer status
        // + update lastUpdateDate
    }
}
