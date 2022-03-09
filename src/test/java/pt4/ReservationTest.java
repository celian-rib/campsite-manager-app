package pt4;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.utils.DateUtils;

@TestInstance(Lifecycle.PER_CLASS)
public class ReservationTest {
    
    private List<Client> testClients = new ArrayList<>();
    private List<Reservation> testReservations = new ArrayList<>();
    private int nbGenerated = 0;

    private void createReservation(String start, String end) throws SQLException {
        var client = new Client();
        client.setAddresse("15 rue Naudet, Gradignan, 33170" + nbGenerated);
        client.setFirstName("test_firstname" + nbGenerated);
        client.setName("test_name" + nbGenerated);
        client.setPhone("+33 07 69 66 65 41" + nbGenerated);
        client.setPreferences("Camping car avec famille" + nbGenerated);
        Database.getInstance().getClientsDao().create(client);
        Database.getInstance().getClientsDao().refresh(client);
        System.out.println("Création client de test");
        
        var resa = new Reservation(client);
        resa.setStartDate(DateUtils.fromLocale(LocalDate.parse(start)));
        resa.setEndDate(DateUtils.fromLocale(LocalDate.parse(end)));
        Database.getInstance().getReservationDao().create(resa);
        Database.getInstance().getReservationDao().refresh(resa);
        System.out.println("Création réservation de test");
        
        testClients.add(client);
        testReservations.add(resa);
        nbGenerated ++;
    }

    @BeforeAll
    public void setup() throws SQLException {
        createReservation("2022-10-01", "2022-10-05");
        createReservation("2022-10-01", "2022-10-05");
    }

    @Test
	public void testLogin() throws SQLException {
        var start = DateUtils.fromLocale(LocalDate.parse("2022-09-25"));
        var end = DateUtils.fromLocale(LocalDate.parse("2022-10-02"));
        var camps = Database.getInstance().getCampgroundDao().getAvailablesCampgrounds(start, end, -1);

        assertTrue(camps.contains(testReservations.get(0).getCampground()));
	}

    @AfterAll
    public void delete() throws SQLException {
        for(var r : testReservations) {
            System.out.println("Suppression de " + r);
            Database.getInstance().getReservationDao().delete(r); 
        }
        for(var c : testClients) {
            System.out.println("Suppression de " + c);
            Database.getInstance().getClientsDao().delete(c); 
        }        
    }
}
