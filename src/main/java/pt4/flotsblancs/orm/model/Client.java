package pt4.flotsblancs.orm.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;



@DatabaseTable(tableName = "clients")
public class Client {

    @Getter
    @DatabaseField(generatedId = true, columnName = "client_id")
    private int clientId;

    @Getter
    @Setter
    @DatabaseField(columnName = "name")
    private String name;

    @Getter
    @Setter
    @DatabaseField(columnName = "first_name")
    private String firstName;

    @Getter
    @Setter
    @DatabaseField(columnName = "preferences")
    private String preferences;

    @Getter
    @Setter
    @DatabaseField(columnName = "address")
    private String addresse;

    @Getter
    @Setter
    @DatabaseField(columnName = "phone")
    private int phone;

    public Client() {}

    public Client(String name, String firstName) {
        this.name = name;
        this.firstName = firstName;
    }
}
