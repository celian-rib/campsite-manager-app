package pt4;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
    
    private static int nbGenerated = 0;

    private Reservation createReservation(String start, String end) throws SQLException {
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
        
        nbGenerated ++;
        return resa;
    }

    private Reservation r1;
    private Reservation r2;

    @BeforeAll
    public void setup() throws SQLException {
        r1 = createReservation("1111-10-01", "1111-10-05");
        r2 = createReservation("1111-11-23", "1111-12-10"); // TODO check si je crois que le moi est bien le mois :/
    }

    @Test
	public void testLogin() throws SQLException {
        // Dates qui ne sont pas prisent par aucune réservation
        var start = DateUtils.fromLocale(LocalDate.parse("1111-09-25"));
        var end = DateUtils.fromLocale(LocalDate.parse("1111-09-26"));
        var camps = Database.getInstance().getCampgroundDao().getAvailablesCampgrounds(start, end, -1);
        assertTrue(camps.contains(r1.getCampground()));
        assertTrue(camps.contains(r2.getCampground()));
        
        // Dates qui ne sont prisent par r1
        start = DateUtils.fromLocale(LocalDate.parse("1111-09-25"));
        end = DateUtils.fromLocale(LocalDate.parse("1111-10-01"));
        camps = Database.getInstance().getCampgroundDao().getAvailablesCampgrounds(start, end, -1);
        assertTrue(camps.contains(r1.getCampground()));
        assertTrue(camps.contains(r2.getCampground()));
	}

    @AfterAll
    public void delete() throws SQLException {
        Database.getInstance().getClientsDao().delete(r1.getClient()); 
        Database.getInstance().getReservationDao().delete(r1);

        Database.getInstance().getClientsDao().delete(r2.getClient()); 
        Database.getInstance().getReservationDao().delete(r2); 
    }
}
