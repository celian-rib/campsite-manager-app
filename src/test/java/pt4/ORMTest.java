package pt4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Reservation;

public class ORMTest extends DatabaseTest {

    @Test
    public void testClientReservationWithCampground() throws Exception {
        // Récupération du client
        var testClient = Database.getInstance().getClientsDao().queryForSameId(client);

        // On regarde si le client a bien sa réservation
        Reservation clientReservation = (Reservation) testClient.getReservations().toArray()[0];
        assertEquals(clientReservation, reservation);

        // On regarde si la réservation du client possède bien son emplacement
        var reservationCampground = clientReservation.getCampground();
        assertEquals(reservationCampground, campground);
    }
}
