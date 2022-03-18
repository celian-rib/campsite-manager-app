package pt4;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.Test;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.utils.DateUtils;

public class CampgroundTest extends DatabaseTest {

    @Test
    public void testIsAvailable() throws SQLException {
        Date start = DateUtils.minusDays(reservation.getStartDate(), 10);
        Date end = DateUtils.plusDays(reservation.getEndDate(), 10);
        System.out.println("Test camp available start -> " + start);
        System.out.println("Test camp available end -> " + end);

        // L'emplacement est dispo si on exclu sa réservation
        var available = Database.getInstance().getCampgroundDao()
                .isAvailable(reservation, campground, start, end);
        assertTrue(available);
        
        // Si on exclu pas sa réservation alors il n'est pas considéré comme disponible
        available = Database.getInstance().getCampgroundDao()
                .isAvailable(campground, start, end);
        assertFalse(available);
    }

    @Test
    public void testGetAvailableCampgrounds() throws SQLException {
        Date start = DateUtils.minusDays(reservation.getStartDate(), 10);
        Date end = DateUtils.plusDays(reservation.getEndDate(), 10);
        System.out.println("Test camp available start -> " + start);
        System.out.println("Test camp available end -> " + end);

        // L'emplacement est dispo si on exclu sa réservation
        var available = Database.getInstance().getCampgroundDao()
                .getAvailablesCampgrounds(start, end, reservation.getId());
        assertTrue(available.contains(campground));
        
        // Si on exclu pas sa réservation alors il n'est pas considéré comme disponible
        available = Database.getInstance().getCampgroundDao()
                .getAvailablesCampgrounds(start, end, -1);
        assertFalse(available.contains(campground));
    }
}
