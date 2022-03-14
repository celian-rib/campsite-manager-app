package pt4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.Service;
import pt4.flotsblancs.utils.DateUtils;

@TestInstance(Lifecycle.PER_CLASS)
public class ReservationTest {

    private Reservation createReservation(String start, String end)
            throws SQLException, ConstraintException {

        var camp = new CampGround();
        camp.setDescription("Belle vue sur les poubelles");
        camp.setPricePerDays(28);
        camp.setSurface(34.5f);
        camp.setAllowedEquipments(Equipment.MOBILHOME);
        camp.setProvidedServices(Service.WATER_AND_ELECTRICITY);
        Database.getInstance().getCampgroundDao().create(camp);

        var client = new Client();
        client.setAddresse("15 rue Naudet, Gradignan, 33170");
        client.setFirstName("test_firstname");
        client.setName("test_name");
        client.setPhone("+33 07 69 66 65 41");
        client.setPreferences("Camping car avec famille");
        Database.getInstance().getClientsDao().create(client);
        Database.getInstance().getClientsDao().refresh(client);
        System.out.println("Création client de test");

        System.out.println("Création réservation de test");
        var resa = new Reservation();
        resa.setClient(client);
        resa.setEquipments(Equipment.MOBILHOME);
        resa.setSelectedServices(Service.WATER_AND_ELECTRICITY);
        resa.setCampground(camp);

        resa.setStartDate(DateUtils.fromLocale(LocalDate.parse(start)));
        resa.setEndDate(DateUtils.fromLocale(LocalDate.parse(end)));

        Database.getInstance().getReservationDao().create(resa);
        Database.getInstance().getReservationDao().refresh(resa);
        System.out.println("Création réservation de test");

        return resa;
    }

    private Reservation tResa;

    @BeforeAll
    public void setup() throws SQLException, ConstraintException {
        tResa = createReservation("3000-09-02", "3000-09-20");
        System.out.println("Date début réservation de test : " + tResa.getStartDate());
        System.out.println("Date fin réservation de test : " + tResa.getEndDate());
    }

    private void testDates(String start, String end, boolean expected) throws SQLException {
        var startDate = DateUtils.fromLocale(LocalDate.parse(start));
        var endDate = DateUtils.fromLocale(LocalDate.parse(end));
        System.out.println("Test reservation date : " + start + " -> " + end);
        var camps = Database.getInstance().getCampgroundDao().getAvailablesCampgrounds(startDate,
                endDate, -1);
        assertEquals(expected, camps.contains(tResa.getCampground()));
    }

    @Test
    public void testGetAvailablesCampgrounds() throws SQLException {
        // Range qui débute après tResa
        // tResa ->@@@@|--------------------|@@@@@@@@@@@@
        // test ->@@@@@@@@@@@@@@@@@@@@@@@@@@@@@|-----|@@
        testDates("3000-09-25", "3000-09-26", true);

        // Range qui avant après tResa
        // tResa ->@@@@@@@@@@@@|--------------------|@@@
        // test ->@@@|-----|@@@@@@@@@@@@@@@@@@@@@@@@@@@
        testDates("3000-08-25", "3000-08-26", true);

        // Range qui durant tResa et fini après tResa
        // tResa ->@@@@@|--------------------|@@@@@@@@@@@
        // test ->@@@@@@@@@@@@@|-----------------------|
        testDates("3000-09-10", "3000-09-25", false);

        // Range qui débute durant tResa et fini avant la fin de tResa
        // tResa ->@@@@@|--------------------|@@@@@@@@@@@
        // test ->@@@@@@@@@@|-----------|@@@@@@@@@@@@@@@
        testDates("3000-09-03", "3000-09-10", false);

        // Range qui débute pile à la fin de tResa
        // tResa ->@@@@|--------------------|@@@@@@@@@@@@
        // test ->@@@@@@@@@@@@@@@@@@@@@@@@@|------|@@@@@
        testDates("3000-09-20", "3000-10-10", true);

        // Range qui termine pile au début de tResa
        // tResa ->@@@@@@@@@@@|--------------------|@@@@@
        // test ->@@@@|------|@@@@@@@@@@@@@@@@@@@@@@@@@@
        testDates("3000-08-10", "3000-09-02", true);
    }

    @AfterAll
    public void delete() throws SQLException {
        Database.getInstance().getClientsDao().delete(tResa.getClient());
        Database.getInstance().getReservationDao().delete(tResa);
    }
}
