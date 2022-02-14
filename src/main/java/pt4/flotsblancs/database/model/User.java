package pt4.flotsblancs.database.model;

import lombok.*;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

@DatabaseTable(tableName = "users")
public class User {

    @Getter
    @Setter
    @DatabaseField(generatedId = true, columnName = "user_id")
    private int userId;

    @Getter
    @Setter
    @DatabaseField(columnName = "login")
    private String login;

    @Getter
    @Setter
    @DatabaseField(columnName = "password")
    private String password;

    @Getter
    @Setter
    @DatabaseField(columnName = "is_admin")
    private int admin;

    @Getter
    @Setter
    @DatabaseField(columnName = "name")
    private String name;

    @Getter
    @Setter
    @DatabaseField(columnName = "first_name")
    private String forstName;

    public User() {}
}
