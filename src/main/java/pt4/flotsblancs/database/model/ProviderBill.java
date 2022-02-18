package pt4.flotsblancs.database.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@DatabaseTable(tableName = "provider_bills")
public class ProviderBill {

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "billing_date")
    private Date billingDate;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "provider_informations")
    private String providerInformations;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "price_amount")
    private int priceAmount;
}
