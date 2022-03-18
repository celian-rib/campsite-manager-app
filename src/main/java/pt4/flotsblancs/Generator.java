package pt4.flotsblancs;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;
import com.github.javafaker.Pokemon;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.Stock;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.database.model.types.CashBack;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.ProblemStatus;
import pt4.flotsblancs.database.model.types.Service;

public class Generator {
    public static void main(String[] args) throws SQLException, ConstraintException {
        Database.getInstance(); // Initialisation connexion BD

        var f = new Faker();

        generateAdmin();
        
        generateStocks(f, 100);
        generateClients(f, 50);
        generateCampGrounds(f, 100);
        generateReservations(f, 20);
        generateProblemsResa(f, 5);
        generateProblemsCg(f, 3);
        generateProblemsClient(f, 3);
    }

    private static int rdmNbrBtwn(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    private static void generateAdmin() throws SQLException {
        var u = new User();
        u.setAdmin(true);
        u.setFirstName("polp");
        u.setName("ogba");
        u.setPassword(User.sha256("test"));
        u.setLogin("test");
        var existing = Database.getInstance().getUsersDao().queryForMatching(u);
        if (existing.size() == 0) {
            Database.getInstance().getUsersDao().create(u);
        }
    }

    private static void generateReservations(Faker f, int nbr) throws SQLException, ConstraintException {
        List<CampGround> CGlist = Database.getInstance().getCampgroundDao().queryForAll();
        List<Client> ClientsList = Database.getInstance().getClientsDao().queryForAll();
        for (int i = 0; i < nbr; i++) {
            var resa = new Reservation();

            resa.setCampground(CGlist.get(rdmNbrBtwn(0, CGlist.size())));
            resa.setClient(ClientsList.get(rdmNbrBtwn(0, ClientsList.size())));
            resa.setNbPersons(rdmNbrBtwn(1, 5));

            var cashbacks = CashBack.values();
            resa.setCashBack(cashbacks[rdmNbrBtwn(0, cashbacks.length)]);

            resa.setDepositDate(rdmNbrBtwn(0, 10) > 5 ? f.date().past(50, TimeUnit.DAYS) : null);
            resa.setStartDate(f.date().future(200, TimeUnit.DAYS, new java.util.Date()));
            resa.setEndDate(f.date().future(30, TimeUnit.DAYS, resa.getStartDate()));

            var equipments = resa.getCampground().getCompatiblesEquipments();
            resa.setEquipments(equipments.get(rdmNbrBtwn(0, equipments.size())));
            
            var services = resa.getCampground().getCompatiblesServices();
            resa.setSelectedServices(services.get(rdmNbrBtwn(0, services.size())));

            Database.getInstance().getReservationDao().create(resa);
            System.out.println(resa);
        }
    }

    private static void generateCampGrounds(Faker f, int nbr) throws SQLException {
        for (int i = 0; i < nbr; i++) {
            var cg = new CampGround();
            cg.setDescription(f.country().name());
            cg.setPricePerDays(f.number().randomDigitNotZero());
            cg.setSurface(f.number().randomDigitNotZero());
            cg.setAllowedEquipments(Equipment.values()[rdmNbrBtwn(0, Equipment.values().length)]);
            if (cg.getAllowedEquipments() == Equipment.MOBILHOME)
                cg.setProvidedServices(Service.WATER_AND_ELECTRICITY);
            else
                cg.setProvidedServices(Service.values()[rdmNbrBtwn(0, Service.values().length)]);
            try {
                Database.getInstance().getCampgroundDao().create(cg);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            System.out.println(cg);
        }
    }

    private static void generateClients(Faker f, int nbr) throws SQLException {
        for (int i = 0; i < nbr; i++) {
            var c = new Client();
            c.setAddresse(f.address().fullAddress());
            c.setPhone(f.phoneNumber().cellPhone().toString());
            String hp = f.harryPotter().spell();
            c.setName((hp.split(" ").length > 1) ? hp.split(" ")[1] : hp.split(" ")[0]);
            c.setFirstName(f.dragonBall().character().split(" ")[0]);
            c.setEmail(f.internet().emailAddress());
            try {
                Database.getInstance().getClientsDao().create(c);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void generateStocks(Faker f, int nbr) throws SQLException {
        for (int i = 0; i < nbr; i++) {
            var s = new Stock();
            Pokemon p = f.pokemon();
            s.setItem(p.name());
            s.setQuantity(f.number().randomDigit());
            s.setQuantityAlertThreshold(5);
            s.setStorageLocation(p.location());
            Database.getInstance().getStockDao().create(s);
            System.out.println(s.getItem() + " " + s.getQuantity() + " " + s.getStorageLocation());
        }
    }

    private static void generateProblemsResa(Faker f, int nbr) throws SQLException {
        List<Reservation> resaList = Database.getInstance().getReservationDao().queryForAll();
        for (int i = 0; i < nbr; i++) {
            var p = new Problem();
            p.setReservation(resaList.get(rdmNbrBtwn(0, resaList.size())));
            p.setCampground(p.getReservation().getCampground());
            p.setClient(p.getReservation().getClient());
            p.setDescription(f.lorem().sentence().toString());
            p.setStatus(ProblemStatus.values()[rdmNbrBtwn(0, ProblemStatus.values().length)]);

            Database.getInstance().getProblemDao().create(p);
            System.out.println(p);
        }
    }

    private static void generateProblemsClient(Faker f, int nbr) throws SQLException {
        List<Client> ClientsList = Database.getInstance().getClientsDao().queryForAll();
        for (int i = 0; i < nbr; i++) {
            var p = new Problem();
            p.setClient(ClientsList.get(rdmNbrBtwn(0, ClientsList.size())));
            p.setDescription(f.lorem().sentence().toString());
            p.setStatus(ProblemStatus.values()[rdmNbrBtwn(0, ProblemStatus.values().length)]);

            Database.getInstance().getProblemDao().create(p);
            System.out.println(p);
        }

    }

    private static void generateProblemsCg(Faker f, int nbr) throws SQLException {
        List<CampGround> CGlist = Database.getInstance().getCampgroundDao().queryForAll();
        for (int i = 0; i < nbr; i++) {
            var p = new Problem();
            p.setCampground(CGlist.get(rdmNbrBtwn(0, CGlist.size())));
            p.setDescription(f.lorem().sentence().toString());
            p.setStatus(ProblemStatus.values()[rdmNbrBtwn(0, ProblemStatus.values().length)]);

            Database.getInstance().getProblemDao().create(p);
            System.out.println(p);
        }
    }
}
