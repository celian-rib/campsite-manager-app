package pt4;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.Problem.ProblemStatus;
import pt4.flotsblancs.database.model.types.Equipment;

@TestInstance(Lifecycle.PER_CLASS)
public class ORMTest {

    private Client testClient;
    private Reservation testReservation;
    private CampGround testReservationCampground;
    private Problem testProblem;

    @BeforeAll
    public void setUp() throws Exception {
        System.out.println("Création emplacement de test");
        testReservationCampground = new CampGround();
        testReservationCampground.setDescription("Belle vue sur les poubelles");
        testReservationCampground.setPricePerDays(28);
        testReservationCampground.setSurface(34.5f);
        testReservationCampground.setAllowedEquipments(Equipment.MOBILHOME);
        Database.getInstance().getCampgroundDao().create(testReservationCampground);

        System.out.println("Création réservation de test");
        testReservation = new Reservation();
        testReservation.setClient(testClient);
        testReservation.setStartDate(new Date());
        testReservation.setEndDate(new Date());
        testReservation.setDepositDate(new Date());
        Database.getInstance().getReservationDao().refresh(testReservation);
        testReservation.setCampground(testReservationCampground);

        System.out.println("Création client de test");
        testClient = new Client();
        testClient.setAddresse("15 rue Naudet, Gradignan, 33170");
        testClient.setFirstName("test_firstname");
        testClient.setName("test_name");
        testClient.setPhone("+33 07 69 66 65 41");
        testClient.setPreferences("Camping car avec famille");
        Database.getInstance().getClientsDao().create(testClient);
        Database.getInstance().getClientsDao().refresh(testClient);

        testClient.getReservations().add(testReservation);

        System.out.println("Création d'un problème de test");
        testProblem = new Problem();
        testProblem.setDescription("Brindille en feu");
        testProblem.setStartDate(new Date());
        testProblem.setStatus(ProblemStatus.OPEN_URGENT);
        testProblem.setCampground(testReservationCampground);
        Database.getInstance().getProblemDao().create(testProblem);
        Database.getInstance().getProblemDao().refresh(testProblem);
    }

    @Test
    public void testClientReservationWithCampground() throws Exception {
        // Récupération du client
        var client = Database.getInstance().getClientsDao().queryForSameId(testClient);

        // On regarde si le client a bien sa réservation
        Reservation clientReservation = (Reservation) client.getReservations().toArray()[0];
        assertEquals(clientReservation, testReservation);

        // On regarde si la réservation du client possède bien son emplacement
        var reservationCampground = clientReservation.getCampground();
        assertEquals(reservationCampground, testReservationCampground);

        // Récupération du problème
        var problem = Database.getInstance().getProblemDao().queryForSameId(testProblem);
        assertEquals(problem, testProblem);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        System.out.println("Supression emplacement de test " + testReservationCampground);
        Database.getInstance().getCampgroundDao().delete(testReservationCampground);
        System.out.println("Supression client de test " + testClient);
        Database.getInstance().getClientsDao().delete(testClient);
        System.out.println("Supression réservation " + testReservation);
        Database.getInstance().getReservationDao().delete(testReservation);
        System.out.println("Supression réservation " + testProblem);
        Database.getInstance().getProblemDao().delete(testProblem);
    }
}
