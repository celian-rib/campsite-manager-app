package pt4.flotsblancs.orm.model;

import com.j256.ormlite.table.DatabaseTable;

import com.j256.ormlite.field.DatabaseField;

@DatabaseTable(tableName = "reservations")
public class Reservation {

    @DatabaseField(generatedId = true, columnName = "reservation_id")
    private int reservationId;

    @DatabaseField(columnName = "client_id")
    private int clientId;

    public Reservation() {}

}
