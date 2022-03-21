package pt4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.utils.DateUtils;

public class ReservationTest extends DatabaseTestWrapper {

    @Override
    @BeforeAll
    public void initTestData() throws SQLException, ConstraintException {
        super.initTestData();

        String start = "3000-09-02";
        String end = "3000-09-20";

        try {
            reservation.setEndDate(DateUtils.fromLocale(LocalDate.parse(end)));
            reservation.setStartDate(DateUtils.fromLocale(LocalDate.parse(start)));
            Database.getInstance().getReservationDao().update(reservation);
        } catch (ConstraintException e) {
            var existing = Database.getInstance().getReservationDao().queryForEq("client_id", client);
            reservation = existing.get(0);
        }
    }

    private void testDates(String start, String end, boolean expected) throws SQLException {
        var startDate = DateUtils.fromLocale(LocalDate.parse(start));
        var endDate = DateUtils.fromLocale(LocalDate.parse(end));
        System.out.println("Test reservation date : " + start + " -> " + end);
        var camps = Database.getInstance().getCampgroundDao().getAvailablesCampgrounds(startDate,
                endDate, -1);
        assertEquals(expected, camps.contains(reservation.getCampground()));
    }

    @Test
    public void testGetAvailablesCampgrounds() throws SQLException {
        // Range qui débute après résa de test
        // résa de test ->@@@@|--------------------|@@@@@@@@@@@@
        // test ->@@@@@@@@@@@@@@@@@@@@@@@@@@@@@|-----|@@
        testDates("3000-09-25", "3000-09-26", true);

        // Range qui avant après résa de test
        // résa de test ->@@@@@@@@@@@@|--------------------|@@@
        // test ->@@@|-----|@@@@@@@@@@@@@@@@@@@@@@@@@@@
        testDates("3000-08-25", "3000-08-26", true);

        // Range qui durant résa de test et fini après résa de test
        // résa de test ->@@@@@|--------------------|@@@@@@@@@@@
        // test ->@@@@@@@@@@@@@|-----------------------|
        testDates("3000-09-10", "3000-09-25", false);

        // Range qui débute durant résa de test et fini avant la fin de résa de test
        // résa de test ->@@@@@|--------------------|@@@@@@@@@@@
        // test ->@@@@@@@@@@|-----------|@@@@@@@@@@@@@@@
        testDates("3000-09-03", "3000-09-10", false);

        // Range qui débute pile à la fin de résa de test
        // résa de test ->@@@@|--------------------|@@@@@@@@@@@@
        // test ->@@@@@@@@@@@@@@@@@@@@@@@@@|------|@@@@@
        testDates("3000-09-20", "3000-10-10", true);

        // Range qui termine pile au début de résa de test
        // résa de test ->@@@@@@@@@@@|--------------------|@@@@@
        // test ->@@@@|------|@@@@@@@@@@@@@@@@@@@@@@@@@@
        testDates("3000-08-10", "3000-09-02", true);
    }
}
