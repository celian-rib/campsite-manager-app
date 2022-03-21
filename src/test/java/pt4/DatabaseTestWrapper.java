package pt4;

import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.ConstraintException;
import java.sql.SQLException;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.Service;
import pt4.flotsblancs.utils.DateUtils;

/**
 * Cette classe ne contient pas de test mais est un wrapper pour les classes de tests
 * 
 * Etendre cette classe permet d'avoir à disposition des objets de test qui sont créés au début des
 * tests puis supprimés à la fin.
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class DatabaseTestWrapper {

    protected User user;
    protected CampGround campground;
    protected Client client;
    protected Reservation reservation;

    private void log(String message) {
        System.out.println("[ " + getClass().getSimpleName() + " ] " + message);
    }

    @BeforeAll
    public void initTestData() throws SQLException, ConstraintException {
        System.out.println("\n--------------[ " + getClass().getSimpleName() + " ]--------------");
        createTestUser();
        createTestCampground();
        createTestClient();
        createTestReservation(client);
    }

    @BeforeEach
    public void showTestInfos(TestInfo info) {
        String methodName = info.getTestMethod().orElseThrow().getName();
        System.out.println("------------- Test " + methodName + " ------------");
    }

    private void createTestUser() throws SQLException {
        log("Création utilisateur de test");
        user = new User();
        user.setName("TestName");
        user.setFirstName("TestFirstName");
        user.setAdmin(true);
        user.setLogin("test_user_123456789");
        user.setPassword(User.sha256("test_password"));
        var existing = Database.getInstance().getUsersDao().queryForMatching(user);
        if (existing.size() > 0) {
            log(" -- Recyclage utilisateur");
            user = existing.get(0);
        } else {
            Database.getInstance().getUsersDao().create(user);
        }
    }

    private void createTestCampground() throws SQLException {
        log("Création emplacement de test");
        campground = new CampGround();
        campground.setDescription("Belle vue sur les poubelles");
        campground.setPricePerDays(28);
        campground.setSurface(34.5f);
        campground.setAllowedEquipments(Equipment.MOBILHOME);
        campground.setProvidedServices(Service.WATER_AND_ELECTRICITY);
        var existing = Database.getInstance().getCampgroundDao().queryForMatching(campground);
        if (existing.size() > 0) {
            log(" -- Recyclage emplacement");
            campground = existing.get(0);
        } else {
            Database.getInstance().getCampgroundDao().create(campground);
            Database.getInstance().getCampgroundDao().refresh(campground);
        }
    }

    private void createTestClient() throws SQLException {
        log("Création client de test");
        client = new Client();
        client.setAddresse("15 rue Naudet, Gradignan, 33170");
        client.setFirstName("test_firstname");
        client.setName("test_name");
        client.setPhone("+33 07 69 66 65 41");
        client.setPreferences("Camping car avec famille");
        client.setEmail("email.tropbien@email.com");
        var existing = Database.getInstance().getClientsDao().queryForMatching(client);
        if (existing.size() > 0) {
            log(" -- Recyclage client");
            client = existing.get(0);
        } else {
            Database.getInstance().getClientsDao().create(client);
            Database.getInstance().getClientsDao().refresh(client);
        }
    }

    private void createTestReservation(Client client) throws SQLException {
        log("Création réservation de test");
        reservation = new Reservation();
        reservation.setClient(client);
        var startDate = DateUtils.plusDays(new Date(), 2000);
        try {
            reservation.setEquipments(Equipment.MOBILHOME);
            reservation.setSelectedServices(Service.WATER_AND_ELECTRICITY);
            reservation.setStartDate(startDate);
            reservation.setEndDate(DateUtils.plusDays(reservation.getStartDate(), 5));
            reservation.setCampground(campground);
        } catch (ConstraintException e) {
            // Si la réservation de test est déjà dans la bd des contraintes vont sauter

            log(" -- Recyclage reservation par violation des contraintes");
            System.out.println(e.getMessage());
            var existing = Database.getInstance().getReservationDao().queryForEq("client_id",
                    client.getId());
            reservation = existing.get(0);
            Database.getInstance().getReservationDao().refresh(reservation);
            return;
        }

        var existing =
                Database.getInstance().getReservationDao().queryForEq("client_id", client.getId());
        if (existing.size() > 0) {
            log(" -- Recyclage reservation");
            reservation = existing.get(0);
            Database.getInstance().getReservationDao().refresh(reservation);
        } else {
            Database.getInstance().getReservationDao().create(reservation);
            Database.getInstance().getReservationDao().refresh(reservation);
        }
        Database.getInstance().getReservationDao().refresh(reservation);
    }

    @AfterAll
    public void deleteTestData() throws SQLException {
        if (user != null) {
            log("Supression utilisateur de test");
            Database.getInstance().getUsersDao().delete(user);
        }
        if (client != null) {
            log("Supression client de test");
            Database.getInstance().getClientsDao().delete(client);
        }
        if (reservation != null) {
            log("Supression réservation de test");
            Database.getInstance().getReservationDao().delete(reservation);
        }
        if (campground != null) {
            log("Supression emplacement de test");
            Database.getInstance().getCampgroundDao().delete(campground);
        }
        System.out.println("------------------------------------------------\n");
    }
}
