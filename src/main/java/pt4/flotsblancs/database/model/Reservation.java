package pt4.flotsblancs.database.model;

import com.j256.ormlite.table.DatabaseTable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pt4.flotsblancs.database.model.types.*;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.ToastType;

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
    @DatabaseField(canBeNull = false, defaultValue = "NONE", columnName = "selected_services")
    private Service selectedServices;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "equipments")
    private Equipment equipments;

    @Getter
    @Setter
    @ToString.Include
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private Client client;

    @Getter
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private CampGround campground;

    @Getter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Problem> problems;

    /**
     * Change l'emplacement actuel de la réservation tout en respectant les contraintes sur les
     * équipements et les services demandés (Ces derniers peuvent changer par effet de bord)
     * 
     * @param camp nouvel emplacement de la réservation
     */
    public void setCampground(CampGround camp) {
        this.campground = camp;
        checkEquipmentsConstraints();
        checkServicesConstraint();
    }

    /**
     * Permet de changer les équipements demandés par la réservation en conservant les contraintes
     * imposées par l'emplacement
     * 
     * @param equipment
     */
    public void setEquipments(Equipment equipment) {
        this.equipments = equipment;
        checkEquipmentsConstraints();
    }

    /**
     * Permet de changer les services demandés par la réservation en conservant les contraintes
     * imposées par l'emplacement
     * 
     * @param service
     */
    public void setSelectedServices(Service service) {
        this.selectedServices = service;
        checkServicesConstraint();
    }

    /**
     * Vérifie l'intégrité des contrainte entre les equipement demandés par la réservation et son
     * emplacement
     * 
     * En cas de non compatibilité l'équipement sera modifié pour répondre à la contrainte
     */
    private void checkEquipmentsConstraints() {
        if (!equipments.isCompatible(campground.getAllowedEquipments())) {
            equipments = campground.getAllowedEquipments();
            Router.showToast(ToastType.WARNING,
                    "Equipement de la réservation modifiés pour correspondre à l'emplacement selectionné");
        }
    }

    /**
     * Vérifie l'intégrité des contrainte entre les services demandés par la réservation et son
     * emplacement
     * 
     * En cas de non compatibilité le service sera modifié pour répondre à la contrainte
     */
    private void checkServicesConstraint() {
        if (this.selectedServices == null)
            return;
        if (campground.getAllowedEquipments() == Equipment.MOBILHOME) {
            selectedServices = Service.WATER_AND_ELECTRICITY;
            Router.showToast(ToastType.WARNING,
                    "Services de la réservation modifiés pour correspondre à aux services proposés par l'emplacement selectionné");
        }
        if (!selectedServices.isCompatibleWithCampService(campground.getProvidedServices())) {
            selectedServices = campground.getProvidedServices();
            Router.showToast(ToastType.WARNING,
                    "Services de la réservation modifiés pour correspondre à aux services proposés par l'emplacement selectionné");
        }
    }

    /**
     * @return Nombres de jours de la réservation
     */
    public int getDayCount() {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) Math.ceil(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) + 1;
    }

    /**
     * @return Prix total de la réservation
     */
    public float getTotalPrice() {
        var dayCount = getDayCount();
        var rawPrice = campground.getPricePerDays() * nbPersons * dayCount;
        var withService = rawPrice + selectedServices.getPricePerDay() * dayCount;
        return withService * cashBack.getReduction();
    }

    /**
     * @return Prix d'acompte de la réservation
     */
    public float getDepositPrice() {
        return getTotalPrice() * 0.3f; // Acompte de 30%
    }

    @Override
    public String getDisplayName() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM");
        if (canceled)
            return "[Annulée] " + client.getName();
        return format.format(startDate) + "-" + format.format(endDate) + " " + client.getName();
    }
}
