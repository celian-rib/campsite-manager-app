package pt4.flotsblancs.orm.model;

import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import com.j256.ormlite.field.DatabaseField;

@DatabaseTable(tableName = "client_problems")
public class ClientProblem {

    @Getter
    @Setter
    @DatabaseField(columnName = "problem_id")
    private int problemId;

    @Getter
    @Setter
    @DatabaseField(columnName = "client_id")
    private int clientId;

    public ClientProblem() {}
}
