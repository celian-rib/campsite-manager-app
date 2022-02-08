package pt4.flotsblancs.orm;

import java.sql.SQLException;
import java.util.HashSet;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import io.github.cdimascio.dotenv.Dotenv;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;

public class Database {
    private static Database instance = null;

    private JdbcPooledConnectionSource conn;

    private Dao<Client, String> clientsDao;

    private Database() throws SQLException {
        // Chargement variables d'environnement
        Dotenv dotenv = Dotenv.load();

        // Lancement de la connexion avec la BD
        this.conn = new JdbcPooledConnectionSource(dotenv.get("DB_URL"), dotenv.get("DB_USER"),
                dotenv.get("DB_PASSWORD"));

        createAllTablesIfNotExists();
        createAllDAOs();
    }



    public static Database getInstance() throws SQLException {
        if (Database.instance == null)
            Database.instance = new Database();
        return instance;
    }

    private void createAllTablesIfNotExists() throws SQLException {
        TableUtils.createTableIfNotExists(conn, Client.class);
    }

    private void createAllDAOs() throws SQLException {
        clientsDao = DaoManager.createDao(conn, Client.class);
    }

    public Dao<Client, String> getClientsDao() {
        return clientsDao;
    }
}
