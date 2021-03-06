package pt4.flotsblancs.database.daos;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;

public class CampgroundDAO extends BaseDaoImpl<CampGround, String> {

    public CampgroundDAO(JdbcPooledConnectionSource conn, Class<CampGround> class1)
            throws SQLException {
        super(conn, class1);
    }

    /**
     * Permet de retourner les emplacements compatibles entre deux dates
     * 
     * @param start
     * @param end
     * @param reservationId -> On mets l'id de la reservation pour éviter de la supprimer de la
     *        liste ce qui pose des soucis d'intégrité, mettre -1 si pas d'id
     * @return Liste de campground
     * @throws SQLException
     */
    public List<CampGround> getAvailablesCampgrounds(Date start, Date end, int reservationId)
            throws SQLException {

        var dbr = Database.getInstance().getReservationDao();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = dateFormat.format(start);
        String endTime = dateFormat.format(end);
        
        List<Reservation> overlappingReservations = dbr.query((dbr.queryBuilder().where()
                .raw("('" + startTime + "' < DATE(start_date) AND '" + endTime + "'"
                        + " > DATE(start_date)) OR ('" + startTime + "'" + " < DATE(end_date) AND '"
                        + endTime + "'" + " > DATE(start_date)) AND canceled = 0")
                .prepare()));
        
        List<Integer> ids = new ArrayList<>();

        // On récupère les ids des réservations incompatibles
        for (Reservation r : overlappingReservations) {
            if (r.getId() != reservationId) {
                ids.add(r.getCampground().getId());
            }
        }

        // On check dans la table des campground qu'il n'y a pas un id incompatible et
        // on l'ajoute
        List<CampGround> campgrounds =
                query((queryBuilder().where().raw("id NOT IN " + getIds(ids)).prepare()));

        return campgrounds;
    }

    private String getIds(List<Integer> ids) {
        String s = "(-1,";
        for (Integer i : ids) {
            s += i + ",";
        }
        s = s.substring(0, s.length() - 1);
        s += ")";

        return s;
    }

    /**
     * Permet de savor si un emplacement est disponible pour une réservation entre 2 dates en donnant une réservation à
     * exclure (La réservation donnée n'est pas prise en compte comme rendant indisponible
     * l'emplacement).
     * 
     * @param reservation reservation de l'emplacement
     * @param camp emplacement a vérifier
     * @param start date de début
     * @param end date de fin
     * @return vrai si l'emplacement est disponible pour la réservation donnée entre les 2 dates
     * @throws SQLException
     */
    public boolean isAvailableForReservation(Reservation reservation, CampGround camp, Date start, Date end)
            throws SQLException {
        // On a besoin de la réservation pour ne pas que l'emplacement soit marqué comme
        // prit par sa
        // réservation initiale
        return getAvailablesCampgrounds(start, end, reservation.getId()).contains(camp);
    }

    /**
     * Permet de savoir si un emplacement est disponible entre 2 dates
     * @param camp emplacement à vérifier
     * @param start date de début (inclue)
     * @param end date de fin (inclue)
     * @return vrai si l'emplacement est disponible dans les dates données
     * @throws SQLException
     */
    public boolean isAvailable(CampGround camp, Date start, Date end) throws SQLException {
        return getAvailablesCampgrounds(start, end, -1).contains(camp);
    }
}
