package pt4.flotsblancs.database.daos;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;

public class CampgroundDAO extends BaseDaoImpl<CampGround, String> {

    public CampgroundDAO(JdbcPooledConnectionSource conn, Class<CampGround> class1) throws SQLException {
        super(conn, class1);
    }

    public List<CampGround> getAvailablesCampgrounds(Date start, Date end) throws SQLException {
        Long startDate = start.toInstant().toEpochMilli() / 1000; // TODO faire une méthode dans DateUtils
        Long endDate = end.toInstant().toEpochMilli() / 1000;

        //TODO rework complet prcq ça ne marche pas
        var dbr = Database.getInstance().getReservationDao();

        List<Reservation> reservationsOnDates = dbr.query((dbr.queryBuilder()
                .where()
                // Attention emillien j'ai changé les < > par rapport à ton code, 
                // prend ton code si tu rework ici :)
                .raw(startDate + " > UNIX_TIMESTAMP(start_date)")
                .and()
                .raw(endDate + " < UNIX_TIMESTAMP(start_date)")
                .or()
                .raw(startDate + " > UNIX_TIMESTAMP(end_date)")
                .and()
                .raw(endDate + " < UNIX_TIMESTAMP(end_date)")
                .prepare()));
        List<CampGround> camps = reservationsOnDates.stream().map(r -> r.getCampground()).collect(Collectors.toList());

        return queryForAll().stream().filter(c -> !camps.contains(c)).collect(Collectors.toList());
    }
}
