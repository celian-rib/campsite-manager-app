package pt4.flotsblancs.database.model;

import com.j256.ormlite.table.DatabaseTable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.*;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.utils.DateUtils;

import java.sql.SQLException;
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
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = false, columnName = "nb_persons")
    private int nbPersons;

    public void setNbPersons(int nbP) {
        this.nbPersons = nbP;
        User.addlog(LogType.MODIFY, "Nombre de personnes de la réservation " + getDisplayName() + " changé pour " + nbP);
    }

    @Getter
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = true, columnName = "cash_back")
    private CashBack cashBack;

    public void setCashBack(CashBack cb){
        this.cashBack = cb;
        User.addlog(LogType.MODIFY, "Remise de la réservation " + getDisplayName() + " changé pour " +cb);
    }

    @Getter
    @DatabaseField(columnName = "deposit_date")
    private Date depositDate;

    public void setDepositDate(Date date){
        this.depositDate = date;
        User.addlog(LogType.MODIFY, "Date de dépot d'accompte de la réservation " + getDisplayName() + " changé pour " + date);
    }

    @Getter
    @DatabaseField(columnName = "payment_date")
    private Date paymentDate;

    public void setPaymentDate(Date date){
        this.paymentDate = date;
        User.addlog(LogType.MODIFY, "Date de payement de la réservation " + getDisplayName() + " changé pour " + date);
    }

    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false, columnName = "start_date")
    private Date startDate;

    @Getter
    @ToString.Include
    @DatabaseField(canBeNull = false, columnName = "end_date")
    private Date endDate;

    @Getter
    @DatabaseField(canBeNull = false, defaultValue = "false")
    private Boolean canceled;

    public void setCanceled(boolean canceled){
        this.canceled = canceled;
        User.addlog(LogType.DELETE, "Réservation " + getDisplayName() + " annulée");
    }

    @Getter
    @DatabaseField(canBeNull = false, defaultValue = "NONE", columnName = "selected_services")
    private Service selectedServices;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "equipments")
    private Equipment equipments;

    @Getter
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        User.addlog(LogType.MODIFY, "Client de la réservation " + getDisplayName() + " changé pour " + client.getDisplayName());
    }

    @Getter
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private CampGround campground;

    @Getter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Problem> problems;

    /**
     * Permet de créer une réservation dite "vide" à partir d'un client
     * 
     * Des données valides et cohérente seront données à la réservation.
     * 
     * @param client client a assigner à cette réservation
     * @throws SQLException        erreur technique de création
     * @throws ConstraintException la création à subit des modfications par effet de
     *                             bords à cause
     *                             des ses contraintes entre équipements / services
     *                             / emplacement / dates
     */
    public Reservation(Client client) throws SQLException, ConstraintException {
        this.client = client;

        // Date par défaut : de aujourd'hui à ajd + 5jours
        this.startDate = new Date();
        this.endDate = DateUtils.fromLocale(DateUtils.toLocale(new Date()).plusDays(5));

        var availablesCamps = Database.getInstance().getCampgroundDao()
                .getAvailablesCampgrounds(startDate, endDate, -1);

        if (availablesCamps.size() == 0) {
            // TODO gérer ce cas
            // (Attention si il y en a plus de dispo décaler date résa)
        }

        this.campground = availablesCamps.get(0);
        this.equipments = campground.getAllowedEquipments();
        this.selectedServices = campground.getProvidedServices();
        this.nbPersons = 1;
        this.cashBack = CashBack.NONE;

        Database.getInstance().getReservationDao().create(this);
        // Refresh pour setup les relations
        Database.getInstance().getReservationDao().refresh(this);
        User.addlog(LogType.ADD, "Création de la réservation " + getDisplayName());
    }

    /**
     * Change l'emplacement actuel de la réservation tout en respectant les
     * contraintes sur les
     * équipements et les services demandés (Ces derniers peuvent changer par effet
     * de bord)
     * 
     * @param camp nouvel emplacement de la réservation
     * @throws ConstraintException
     * @throws SQLException
     */
    public void setCampground(CampGround camp) throws ConstraintException, SQLException {
        // Gére le cas ou la réservation n'est pas encore bien construite
        if (this.startDate == null || this.endDate == null) {
            this.campground = camp;
        } else {
            if (!Database.getInstance().getCampgroundDao().isAvailable(this, camp, startDate, endDate)) {
                throw new ConstraintException(
                        "Cet emplacement n'est pas disponibles sur les dates de la réservation",
                        false);
            }
        }

        User.addlog(LogType.MODIFY, "Emplacement de la réservation " + getDisplayName() + " changé à " + campground); // TODO mettre displayName de campground
        this.campground = camp;

        // Gestion des contraintes equipements et services
        // ATTENTION -> changer l'emplacement prend la priorité en terme de contrainte
        // et change
        // donc servies et equipement si ils ne sont pas compatibles
        ConstraintException exceptionHandler = null;

        try {
            checkEquipmentsConstraints();
        } catch (ConstraintException e) {
            // On ne relance pas l'exception tout de suite, il faut avant vérifier les
            // services
            exceptionHandler = e;
        }

        try {
            checkServicesConstraint();
        } catch (Exception e) {
            if (exceptionHandler == null)
                throw e; // Seul les services ont bloqué
            else
                throw new ConstraintException(
                        "Les services et equipement demandés ont été modifiés pour correspondre au nouvel emplacement",
                        true);
        }

        if (exceptionHandler != null) // Seul les equipement ont bloqué
            throw exceptionHandler;
    }

    /**
     * Permet de changer les équipements demandés par la réservation en conservant
     * les contraintes
     * imposées par l'emplacement
     * 
     * @param equipment
     * @throws ConstraintException
     */
    public void setEquipments(Equipment equipment) throws ConstraintException {
        this.equipments = equipment;
        User.addlog(LogType.MODIFY, "Equipements de la réservation " + getDisplayName() + " changés à " + equipments.getName());
        checkEquipmentsConstraints();
    }

    /**
     * Permet de changer les services demandés par la réservation en conservant les
     * contraintes
     * imposées par l'emplacement
     * 
     * @param service
     * @throws ConstraintException
     */
    public void setSelectedServices(Service service) throws ConstraintException {
        this.selectedServices = service;
        User.addlog(LogType.MODIFY, "Services de la réservation " + getDisplayName() + " changés à " + selectedServices.getName());
        checkServicesConstraint();
    }

    /**
     * Vérifie l'intégrité des contrainte entre les equipement demandés par la
     * réservation et son
     * emplacement
     * 
     * En cas de non compatibilité l'équipement sera modifié pour répondre à la
     * contrainte
     * 
     * @throws ConstraintException
     */
    private void checkEquipmentsConstraints() throws ConstraintException {
        // Gére le cas ou la réservation n'est pas encore bien construite
        if (this.equipments == null || campground == null)
            return;
        if (!equipments.isCompatibleWithCampEquipment(campground.getAllowedEquipments())) {
            equipments = campground.getAllowedEquipments();
            User.addlog(LogType.MODIFY, "Equipements de la réservation " + getDisplayName() + " changés à " + equipments.getName());
            throw new ConstraintException(
                    "Equipements de la réservation modifiés pour correspondre à l'emplacement selectionné",
                    true);
        }
    }

    /**
     * Vérifie l'intégrité des contrainte entre les services demandés par la
     * réservation et son
     * emplacement
     * 
     * En cas de non compatibilité le service sera modifié pour répondre à la
     * contrainte
     * 
     * @throws ConstraintException
     */
    private void checkServicesConstraint() throws ConstraintException {
        // Gére le cas ou la réservation n'est pas encore bien construite
        if (this.selectedServices == null || campground == null)
            return;

        String err = "Services modifiés pour correspondre aux services proposés par l'emplacement selectionné";
        // Cas ou l'emplacement est un mobilhome, on force eau et électricité
        if (campground.getAllowedEquipments() == Equipment.MOBILHOME
                && selectedServices != Service.WATER_AND_ELECTRICITY) {
            selectedServices = Service.WATER_AND_ELECTRICITY;
            User.addlog(LogType.MODIFY, "Services de la réservation " + getDisplayName() + " changés à " + selectedServices.getName());
            throw new ConstraintException(err, true);
        }

        // Cas ou le service sélectionné n'est pas disponible sur l'emplacement
        if (!selectedServices.isCompatibleWithCampService(campground.getProvidedServices())) {
            selectedServices = campground.getProvidedServices();
            throw new ConstraintException(err, true);
        }
    }

    public void setStartDate(Date newStartDate) throws ConstraintException, SQLException {
        // Gére le cas ou la réservation n'est pas encore bien construite
        if (this.endDate == null) {
            this.startDate = newStartDate;
            return;
        }

        var campDao = Database.getInstance().getCampgroundDao();
        if (!campDao.isAvailable(this, this.campground, newStartDate, endDate)) {
            throw new ConstraintException(
                    "L'emplacement sélectionné n'est pas disponibles avec cette date de début",
                    false);
        }

        if (DateUtils.isInPast(newStartDate)) {
            throw new ConstraintException(
                    "La date de début sélectionnée est antérieur à la date actuelle", false);
        }

        if (DateUtils.isAfter(newStartDate, endDate)) {
            throw new ConstraintException(
                    "La date de début sélectionnée est ultérieure à la date de fin", false);
        }
        User.addlog(LogType.MODIFY, "Date de début de la réservation " + getDisplayName() + " changé à " + newStartDate);
        this.startDate = newStartDate;
    }

    public void setEndDate(Date newEndDate) throws ConstraintException, SQLException {
        // Gére le cas ou la réservation n'est pas encore bien construite
        if (this.startDate == null || this.campground == null) {
            this.endDate = newEndDate;
            return;
        }

        var campDao = Database.getInstance().getCampgroundDao();
        if (!campDao.isAvailable(this, this.campground, startDate, newEndDate)) {
            throw new ConstraintException(
                    "L'emplacement sélectionné n'est pas disponibles avec cette date de fin",
                    false);
        }

        if (DateUtils.toLocale(newEndDate).isBefore(DateUtils.toLocale(startDate))) {
            throw new ConstraintException(
                    "La date de fin sélectionnée est antérieure à la date de début de la réservation.",
                    false);
        }
        User.addlog(LogType.MODIFY, "Date de fin de la réservation " + getDisplayName() + " changé à " + newEndDate);
        this.endDate = newEndDate;
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

    /**
     * @return vrai si cette réservation est dans le passé (Soit sa date de fin est
     *         passée)
     */
    public boolean isInPast() {
        return new Date().compareTo(this.getEndDate()) >= 0;
    }

    @Override
    public String getSearchString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return new StringBuilder().append(this.id).append(';')
                .append(formatter.format(this.startDate)).append(';')
                .append(this.client.getFirstName()).append(';').append(this.client.getName())
                .append(';').append(this.client.getPhone()).append(';').toString().trim()
                .toLowerCase();
    }

    @Override
    public String getDisplayName() {
        // TODO supprimer ça ou mieux gérer l'intégrité de la BD
        // Client peut être null si il a été supprimé, das ce cas l'applic crash de
        // partout (pas que
        // ici)
        var clientStr = client == null ? "Aucun client" : client.getName();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");
        if (canceled)
            return "[Annulée] " + clientStr;
        return format.format(startDate) + "-" + format.format(endDate) + " " + clientStr;
    }
}
