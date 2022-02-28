package pt4.flotsblancs.database.model;

import com.j256.ormlite.table.DatabaseTable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pt4.flotsblancs.database.model.types.*;
import pt4.flotsblancs.scenes.items.Item;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "reservations")
public class Reservation implements Item {

    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @Setter
    @ToString.Include
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = false, columnName = "nb_persons")
    private int nbPersons;

    @Getter
    @Setter
    @ToString.Include
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = true, columnName = "cash_back")
    private CashBack cashBack;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(columnName = "deposit_date")
    private Date depositDate;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(columnName = "payment_date")
    private Date paymentDate;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(canBeNull = false, columnName = "start_date")
    private Date startDate;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(canBeNull = false, columnName = "end_date")
    private Date endDate;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, defaultValue = "false")
    private Boolean canceled;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, defaultValue = "NONE", columnName = "selected_services")
    private Service selectedServices;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, columnName = "equipments")
    private Equipment equipments;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private Client client;

    @Getter
    @Setter
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private CampGround campground;

    @Getter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Problem> problems;

    @Override
    public String getDisplayName() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM");
        if (canceled)
            return "[Annulée] " + client.getName();
        return format.format(startDate) + "-" + format.format(endDate) + " " + client.getName();
    }

    public int getDayCount() {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) Math.ceil(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) + 1;
    }

    public float getTotalPrice() {
        var dayCount = getDayCount();
        var rawPrice = campground.getPricePerDays() * nbPersons * dayCount;
        var withService = rawPrice + selectedServices.getPricePerDay() * dayCount;
        return withService * cashBack.getReduction();
    }

    public float getDepositPrice() {
        return getTotalPrice() * 0.3f; // Acompte de 30%
    }
}
