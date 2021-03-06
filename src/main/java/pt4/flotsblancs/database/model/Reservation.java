package pt4.flotsblancs.database.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import javafx.scene.paint.Color;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.types.CashBack;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.database.model.types.Service;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.StatusColors;
import pt4.flotsblancs.utils.DateUtils;

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

    @Getter
    @EqualsAndHashCode.Include
    @DatabaseField(canBeNull = true, columnName = "cash_back")
    private CashBack cashBack;

    @Getter
    @DatabaseField(columnName = "deposit_date")
    private Date depositDate;

    @Getter
    @DatabaseField(columnName = "payment_date")
    private Date paymentDate;

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

    @Getter
    @DatabaseField(canBeNull = false, defaultValue = "NONE", columnName = "selected_services")
    private Service selectedServices;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "equipments")
    private Equipment equipments;

    @Getter
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private Client client;

    @Getter
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private CampGround campground;

    @Getter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Problem> problems;

    @Getter
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private byte[] bill;

    public Reservation() {
        this.canceled = false;
    }

    /**
     * Permet de cr??er une r??servation dite "vide" ?? partir d'un client
     * 
     * Des donn??es valides et coh??rente seront donn??es ?? la r??servation.
     * 
     * @param client client a assigner ?? cette r??servation
     * @throws SQLException erreur technique de cr??ation
     * @throws ConstraintException la cr??ation ?? subit des modfications par effet de bords ?? cause
     *         des ses contraintes entre ??quipements / services / emplacement / dates
     */
    public Reservation(Client client) throws SQLException, ConstraintException {
        this.client = client;

        int plusDay = 0;
        List<CampGround> availablesCamps = new ArrayList<CampGround>();

        do {
            // Date par d??faut : de aujourd'hui ?? ajd + 5jours
            this.startDate = DateUtils.plusDays(new Date(), plusDay);
            this.endDate = DateUtils.plusDays(startDate, 5);

            availablesCamps = Database.getInstance().getCampgroundDao()
                    .getAvailablesCampgrounds(startDate, endDate, -1);
            plusDay++;
        } while (availablesCamps.size() == 0);


        this.nbPersons = 1;
        this.cashBack = CashBack.NONE;
        this.campground = availablesCamps.get(0);
        this.equipments = campground.getAllowedEquipments();
        this.selectedServices = campground.getProvidedServices();
        this.canceled = false;

        Database.getInstance().getReservationDao().create(this);
        Database.getInstance().getReservationDao().refresh(this);
        User.addlog(LogType.ADD, "Cr??ation de la r??servation #" + id);
    }

    /**
     * Change l'emplacement actuel de la r??servation tout en respectant les contraintes sur les
     * ??quipements et les services demand??s (Ces derniers peuvent changer par effet de bord)
     * 
     * @param camp nouvel emplacement de la r??servation
     * @throws ConstraintException
     * @throws SQLException
     */
    public void setCampground(CampGround camp) throws ConstraintException, SQLException {
        // G??re le cas ou la r??servation n'est pas encore bien construite
        if (this.startDate == null || this.endDate == null) {
            this.campground = camp;
        } else {
            if (!Database.getInstance().getCampgroundDao().isAvailableForReservation(this, camp,
                    startDate, endDate)) {
                throw new ConstraintException(
                        "Cet emplacement n'est pas disponibles sur les dates de la r??servation",
                        false);
            }
        }

        this.campground = camp;
        User.addlog(LogType.MODIFY, "Emplacement de la r??servation #" + id + " chang?? ?? "
                + campground.getDisplayName());

        // Gestion des contraintes equipements et services
        // ATTENTION -> changer l'emplacement prend la priorit?? en terme de contrainte
        // et change
        // donc servies et equipement si ils ne sont pas compatibles
        ConstraintException exceptionHandler = null;

        try {
            checkEquipmentsConstraints();
        } catch (ConstraintException e) {
            // On ne relance pas l'exception tout de suite, il faut avant v??rifier les
            // services
            exceptionHandler = e;
        }

        try {
            checkServicesConstraint();
        } catch (Exception e) {
            if (exceptionHandler == null)
                throw e; // Seul les services ont bloqu??
            else
                throw new ConstraintException(
                        "Les services et equipement demand??s ont ??t?? modifi??s pour correspondre au nouvel emplacement",
                        true);
        }

        if (exceptionHandler != null) // Seul les equipement ont bloqu??
            throw exceptionHandler;
    }

    /**
     * Permet de changer les ??quipements demand??s par la r??servation en conservant les contraintes
     * impos??es par l'emplacement
     * 
     * @param equipment
     * @throws ConstraintException
     */
    public void setEquipments(Equipment equipment) throws ConstraintException {
        User.addlog(LogType.MODIFY,
                "Equipements de la r??servation #" + id + " chang??s ?? " + equipment.getName());
        this.equipments = equipment;
        checkEquipmentsConstraints();
    }

    /**
     * Permet de changer les services demand??s par la r??servation en conservant les contraintes
     * impos??es par l'emplacement
     * 
     * @param service
     * @throws ConstraintException
     */
    public void setSelectedServices(Service service) throws ConstraintException {
        User.addlog(LogType.MODIFY,
                "Services de la r??servation #" + id + " chang??s ?? " + service.getName());
        this.selectedServices = service;
        checkServicesConstraint();
    }

    /**
     * V??rifie l'int??grit?? des contrainte entre les equipement demand??s par la r??servation et son
     * emplacement
     * 
     * En cas de non compatibilit?? l'??quipement sera modifi?? pour r??pondre ?? la contrainte
     * 
     * @throws ConstraintException
     */
    private void checkEquipmentsConstraints() throws ConstraintException {
        // G??re le cas ou la r??servation n'est pas encore bien construite
        if (this.equipments == null || campground == null)
            return;
        if (!equipments.isCompatibleWithCampEquipment(campground.getAllowedEquipments())) {
            equipments = campground.getAllowedEquipments();
            User.addlog(LogType.MODIFY,
                    "Equipements de la r??servation #" + id + " chang??s ?? " + equipments.getName());
            throw new ConstraintException(
                    "Equipements de la r??servation modifi??s pour correspondre ?? l'emplacement selectionn??",
                    true);
        }
    }

    /**
     * V??rifie l'int??grit?? des contrainte entre les services demand??s par la r??servation et son
     * emplacement
     * 
     * En cas de non compatibilit?? le service sera modifi?? pour r??pondre ?? la contrainte
     * 
     * @throws ConstraintException
     */
    private void checkServicesConstraint() throws ConstraintException {
        // G??re le cas ou la r??servation n'est pas encore bien construite
        if (this.selectedServices == null || campground == null)
            return;

        String err =
                "Services modifi??s pour correspondre aux services propos??s par l'emplacement selectionn??";
        // Cas ou l'emplacement est un mobilhome, on force eau et ??lectricit??
        if (campground.getAllowedEquipments() == Equipment.MOBILHOME
                && selectedServices != Service.WATER_AND_ELECTRICITY) {
            selectedServices = Service.WATER_AND_ELECTRICITY;
            User.addlog(LogType.MODIFY, "Services de la r??servation #" + id + " chang??s ?? "
                    + selectedServices.getName());
            throw new ConstraintException(err, true);
        }

        // Cas ou le service s??lectionn?? n'est pas disponible sur l'emplacement
        if (!selectedServices.isCompatibleWithCampService(campground.getProvidedServices())) {
            selectedServices = campground.getProvidedServices();
            User.addlog(LogType.MODIFY, "Services de la r??servation #" + id + " chang??s ?? "
                    + selectedServices.getName());
            throw new ConstraintException(err, true);
        }
    }

    /**
     * @param newStartDate
     * @throws ConstraintException
     * @throws SQLException
     */

    /**
     * V??rifie si les nouvelles dates de d??but de s??jour s??lectionn??es sont valides l'action est
     * logg??
     * 
     * @param newStartDate
     * @throws ConstraintException
     * @throws SQLException
     */

    public void setStartDate(Date newStartDate) throws ConstraintException, SQLException {
        // G??re le cas ou la r??servation n'est pas encore bien construite
        if (this.endDate == null || this.campground == null) {
            this.startDate = newStartDate;
            return;
        }

        var campDao = Database.getInstance().getCampgroundDao();
        if (!campDao.isAvailableForReservation(this, this.campground, newStartDate, endDate)) {
            throw new ConstraintException(
                    "L'emplacement s??lectionn?? n'est pas disponibles avec cette date de d??but",
                    false);
        }

        if (DateUtils.isInPast(newStartDate)) {
            throw new ConstraintException(
                    "La date de d??but s??lectionn??e est ant??rieure ?? la date actuelle", false);
        }

        if (DateUtils.isAfter(newStartDate, endDate)) {
            throw new ConstraintException(
                    "La date de d??but s??lectionn??e est ult??rieure ?? la date de fin", false);
        }
        User.addlog(LogType.MODIFY,
                "Date de d??but de la r??servation #" + id + " chang?? ?? " + newStartDate);
        this.startDate = newStartDate;
    }

    /**
     * V??rifie si les nouvelles dates de fin de s??jour s??lectionn??es sont valides l'action est logg??
     * 
     * @param newEndDate
     * @throws ConstraintException
     * @throws SQLException
     */

    public void setEndDate(Date newEndDate) throws ConstraintException, SQLException {
        // G??re le cas ou la r??servation n'est pas encore bien construite
        if (this.startDate == null || this.campground == null) {
            this.endDate = newEndDate;
            return;
        }

        var campDao = Database.getInstance().getCampgroundDao();
        if (!campDao.isAvailableForReservation(this, this.campground, startDate, newEndDate)) {
            throw new ConstraintException(
                    "L'emplacement s??lectionn?? n'est pas disponibles avec cette date de fin",
                    false);
        }

        if (DateUtils.toLocale(newEndDate).isBefore(DateUtils.toLocale(startDate))) {
            throw new ConstraintException(
                    "La date de fin s??lectionn??e est ant??rieure ?? la date de d??but de la r??servation.",
                    false);
        }
        User.addlog(LogType.MODIFY,
                "Date de fin de la r??servation #" + id + " chang?? ?? " + newEndDate);
        this.endDate = newEndDate;
    }

    /**
     * @return Nombres de jours de la r??servation
     */
    public int getDayCount() {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) Math.ceil(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) + 1;
    }

    /**
     * @return Prix total de la r??servation
     */
    public int getTotalPrice() {
        var dayCount = getDayCount();
        int rawPrice = campground.getPricePerDays() * nbPersons * dayCount;

        var withService = rawPrice + selectedServices.getPricePerDay() * dayCount;

        var i = (int) Math.floor(withService * cashBack.getReduction());
        return i;

    }

    /**
     * @return Prix d'acompte de la r??servation
     */
    public int getDepositPrice() {
        var i = (int) Math.floor((getTotalPrice() * 0.3f) * cashBack.getReduction());// Acompte de
                                                                                     // 30%
        return i;
    }

    /**
     * @return Prix de la r??servation restant a payer
     */
    public int getToPayPrice() {
        if (getDepositDate() == null)
            return getTotalPrice();
        if (getPaymentDate() == null)
            return getTotalPrice() - getDepositPrice();
        return 0;
    }

    /**
     * @return vrai si cette r??servation est dans le pass?? (Soit sa date de fin est pass??e)
     */
    public boolean isInPast() {
        return new Date().compareTo(this.getEndDate()) >= 0;
    }

    @Override
    public String getSearchString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return String
                .join(";", "" + this.id, formatter.format(this.startDate),
                        this.client.getFirstName(), this.client.getName(), this.client.getPhone())
                .trim().toLowerCase();
    }

    @Override
    public String getDisplayName() {
        if (startDate == null || endDate == null || client == null)
            return "Reservation " + getId();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");
        var prefix = canceled ? "[Annul??e] " : "";
        return prefix + format.format(startDate) + "-" + format.format(endDate) + " "
                + client.getName();
    }

    /**
     * l'action est logg??
     * 
     * @param nbP
     */
    public void setNbPersons(int nbP) {
        User.addlog(LogType.MODIFY,
                "Nombre de personnes de la r??servation #" + id + " chang?? pour " + nbP);
        this.nbPersons = nbP;
    }

    /**
     * l'action est logg??
     * 
     * @param date
     */

    public void setPaymentDate(Date date) {
        if (date == null) {
            User.addlog(LogType.DELETE, "Paiement annul?? pour la r??servation #" + id);
        } else {
            User.addlog(LogType.ADD, "Paiement effectu?? pour la r??servation #" + id);
        }
        this.paymentDate = date;
    }

    /**
     * l'action est logg??
     * 
     * @param client
     */

    public void setClient(Client client) {
        this.client = client;
        User.addlog(LogType.MODIFY,
                "Client de la r??servation #" + id + " chang?? pour " + this.client.getDisplayName());
    }

    /**
     * l'action est logg??
     * 
     * @param date
     */

    public void setDepositDate(Date date) {
        if (date == null) {
            User.addlog(LogType.DELETE, "Accompte annul?? pour la r??servation #" + id);
        } else {
            User.addlog(LogType.ADD, "Accompte vers?? pour la r??servation #" + id);
        }
        this.depositDate = date;
    }

    /**
     * l'action est logg??
     * 
     * @param cb
     */

    public void setCashBack(CashBack cb) {
        User.addlog(LogType.MODIFY, "Remise de la r??servation #" + id + " chang?? pour " + cb);
        this.cashBack = cb;
    }

    /**
     * l'action est logg??
     * 
     * @param canceled
     */

    public void setCanceled(boolean canceled) {
        User.addlog(LogType.DELETE, "R??servation #" + id + " annul??e");
        this.canceled = canceled;
    }

    /**
     * l'action est logg??
     * 
     * @param fileData
     */

    public void setBill(byte[] fileData) {
        User.addlog(LogType.ADD, "G??n??ration de la facture de la r??servation #" + id);
        this.bill = fileData;
    }

    @Override
    public boolean isForeignCorrect() {
        return client != null && campground != null && problems != null;
    }

    @Override
    public Color getStatusColor() {
        if (canceled)
            return StatusColors.BLACK;
        if (isInPast())
            return StatusColors.RED;
        if (depositDate != null)
            return paymentDate != null ? StatusColors.GREEN : StatusColors.BLUE;
        return StatusColors.YELLOW;
    }

    /**
     * D??finit l'??chelle de comparaison d'une r??servation selon son ??tat
     * 
     * @return
     */

    private int getCompareScale() {
        if (isInPast() || canceled)
            return -10000; // Reservation pass??e ou annul??e
        if (depositDate == null && paymentDate == null)
            return 1000; // Non pay??, pas d'accompte
        if (paymentDate == null)
            return 100; // accompte pay??
        return 10; // pay??
    }

    @Override
    public int compareTo(Item o) {
        Reservation other = (Reservation) o;
        return (other.getCompareScale() - getCompareScale())
                + this.getStartDate().compareTo(other.getStartDate());
    }
}
