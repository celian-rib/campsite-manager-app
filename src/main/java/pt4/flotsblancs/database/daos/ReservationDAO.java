package pt4.flotsblancs.database.daos;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import pt4.flotsblancs.database.model.Reservation;

public class ReservationDAO extends BaseDaoImpl<Reservation, String> {

    public ReservationDAO(JdbcPooledConnectionSource conn, Class<Reservation> class1) throws SQLException {
        super(conn, class1);
    }
}
