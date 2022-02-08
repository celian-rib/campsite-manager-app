package pt4.flotsblancs.orm.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;


@DatabaseTable(tableName = "bills")
public class Bill {

    @Getter
    @DatabaseField(generatedId = true, columnName = "bill_id")
    private int billId;

    @Getter
    @DatabaseField(columnName = "facturation_date")
    private Date facturationDate;

    @Getter
    @Setter
    @DatabaseField(columnName = "provider_informations")
    private String providerInformations;

    @Getter
    @Setter
    @DatabaseField(columnName = "price_amount")
    private int priceAmount;

    public Bill() {}

}
