package pt4;

import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.ConstraintException;
import java.sql.SQLException;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.Service;

public class TestUtils {

    public static CampGround campground;
    public static Client client;
    public static Reservation reservation;

    public static void initTestData() throws SQLException, ConstraintException {
        createTestCampground();
        createTestClient();
        createTestReservation(client);
    }

    private static void createTestCampground() throws SQLException {
        System.out.println("Création emplacement de test");
        campground = new CampGround();
        campground.setDescription("Belle vue sur les poubelles");
        campground.setPricePerDays(28);
        campground.setSurface(34.5f);
        campground.setAllowedEquipments(Equipment.MOBILHOME);
        campground.setProvidedServices(Service.WATER_AND_ELECTRICITY);
        Database.getInstance().getCampgroundDao().create(campground);
    }

    private static void createTestClient() throws SQLException {
        System.out.println("Création client de test");
        client = new Client();
        client.setAddresse("15 rue Naudet, Gradignan, 33170");
        client.setFirstName("test_firstname");
        client.setName("test_name");
        client.setPhone("+33 07 69 66 65 41");
        client.setPreferences("Camping car avec famille");
        var existing = Database.getInstance().getClientsDao().queryForMatching(client);
        if (existing.size() > 0) {
            client = existing.get(0);
            Database.getInstance().getClientsDao().refresh(client);
        } else {
            Database.getInstance().getClientsDao().create(client);
            Database.getInstance().getClientsDao().refresh(client);
        }
    }

    private static void createTestReservation(Client client)
            throws SQLException, ConstraintException {
        System.out.println("Création réservation de test");
        reservation = new Reservation(client);
        reservation.setCampground(campground);
        Database.getInstance().getReservationDao().refresh(reservation);
    }

    public static void deleteTestData() throws SQLException {
        if (client != null)
            Database.getInstance().getClientsDao().delete(client);
        if (reservation != null)
            Database.getInstance().getReservationDao().delete(reservation);
        if (campground != null)
            Database.getInstance().getCampgroundDao().delete(campground);
    }
}
