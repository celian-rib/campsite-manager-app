package pt4.flotsblancs;

import java.sql.SQLException;
import com.github.javafaker.Faker;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;

public class Generator {
    public static void main(String[] args) throws SQLException {
        Database.getInstance(); // Initialisation connexion BD

        var f = new Faker();

        for(int i = 0; i < 15; i++) 
        {
            var c = new Client();
            c.setName(f.name().name());
            c.setFirstName(f.name().firstName());
            c.setPhone(f.phoneNumber().toString());
            c.setAddresse(f.address().toString());
            Database.getInstance().getClientsDao().create(c);
            System.out.println(c);
        }

    }
}
