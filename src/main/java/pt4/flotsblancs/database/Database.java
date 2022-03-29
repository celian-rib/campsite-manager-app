package pt4.flotsblancs.database;

import pt4.flotsblancs.database.daos.CampgroundDAO;
import pt4.flotsblancs.database.daos.ReservationDAO;
import pt4.flotsblancs.database.model.*;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

import java.sql.SQLException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

public class Database {

    private static Database instance = null;

    private JdbcPooledConnectionSource conn;

    @Getter
    private Dao<Client, String> clientsDao;

    @Getter
    private Dao<User, String> usersDao;

    @Getter
    private CampgroundDAO campgroundDao;

    @Getter
    private Dao<ProviderBill, String> billDao;

    @Getter
    private Dao<Log, String> logDao;

    @Getter
    private Dao<Problem, String> problemDao;

    @Getter
    private ReservationDAO reservationDao;

    @Getter
    private Dao<Stock, String> stockDao;

    private Database() throws SQLException {
        Logger.setGlobalLogLevel(Level.ERROR);

        // Chargement variables d'environnement
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String passwd = dotenv.get("DB_PASSWORD");

        // Lancement de la connexion avec la BD
        this.conn = new JdbcPooledConnectionSource(url, user, passwd);

        createAllTablesIfNotExists();
        createAllDAOs();
    }

    public static Database getInstance() throws SQLException {
        if (Database.instance == null)
            Database.instance = new Database();
        return instance;
    }

    public boolean isConnected() {
        if (this.conn == null)
            return false;
        String pingQuery = this.conn.getDatabaseType().getPingStatement();
        DatabaseConnection dbConn;
        try {
            dbConn = conn.getReadOnlyConnection(null);
            dbConn.executeStatement(pingQuery, 1);
            conn.releaseConnection(dbConn);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void createAllTablesIfNotExists() throws SQLException {
        TableUtils.createTableIfNotExists(conn, Client.class);
        TableUtils.createTableIfNotExists(conn, User.class);
        TableUtils.createTableIfNotExists(conn, CampGround.class);
        TableUtils.createTableIfNotExists(conn, ProviderBill.class);
        TableUtils.createTableIfNotExists(conn, Log.class);
        TableUtils.createTableIfNotExists(conn, Problem.class);
        TableUtils.createTableIfNotExists(conn, Reservation.class);
        TableUtils.createTableIfNotExists(conn, Stock.class);
    }

    private void createAllDAOs() throws SQLException {
        clientsDao = DaoManager.createDao(conn, Client.class);
        usersDao = DaoManager.createDao(conn, User.class);
        campgroundDao = new CampgroundDAO(conn, CampGround.class);
        billDao = DaoManager.createDao(conn, ProviderBill.class);
        logDao = DaoManager.createDao(conn, Log.class);
        problemDao = DaoManager.createDao(conn, Problem.class);
        reservationDao = new ReservationDAO(conn, Reservation.class);
        stockDao = DaoManager.createDao(conn, Stock.class);
    }
}
