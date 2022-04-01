package pt4.flotsblancs;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.Stock;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.database.model.types.CashBack;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.ProblemStatus;
import pt4.flotsblancs.database.model.types.Service;

/**
 * Cette classe est indépendante de l'application, elle permet de générer des
 * données
 * fictives et cohérentes dans la base de donnée.
 */
public class Generator {

    private static int nbResaFideleClient = 0;

    public static void main(String[] args) throws SQLException {
        Database.getInstance(); // Initialisation connexion BD

        var f = new Faker();

        generateAdmin();
        generateUsers();
        generateStocks(f);
        generateClients(f, 120);
        generateCampGrounds(f, 100);
        generateReservations(f, 200);
        generateClientFidele(f);
        generateProblemsResa(f, 10);
        generateProblemsCg(f, 10);
        generateProblemsClient(f, 10);
    }

    private static int rdmNbrBtwn(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    private static void generateAdmin() throws SQLException {
        var u = new User();
        u.setAdmin(true);
        u.setFirstName("Paul");
        u.setName("Campo");
        u.setPassword(User.sha256("test"));
        u.setLogin("Campo");
        var existing = Database.getInstance().getUsersDao().queryForMatching(u);
        if (existing.size() == 0) {
            Database.getInstance().getUsersDao().create(u);
        }
    }

    private static void generateUsers() throws SQLException {
        String[] firstNames = { "Anna", "Paul", "Jérémy", "Polp" };
        String[] lastNames = { "Dupont", "Martin", "Delamama", "Ogba" };
        String[] pwds = { "Pilou33", "123456789", "motdepass", "test" };
        String[] logins = { "ADupont", "PMartin", "JDelamama", "POgba" };
        User u;
        for (int i = 0; i < logins.length; i++) {
            u = new User();
            u.setAdmin(false);
            u.setFirstName(firstNames[i]);
            u.setName(lastNames[i]);
            u.setPassword(User.sha256(pwds[i]));
            u.setLogin(logins[i]);
            u.setWeeklyHours(new Random().nextInt(20) + 15);
            var existing = Database.getInstance().getUsersDao().queryBuilder().where().eq("login", logins[i]).query()
                    .size() > 0;
            if (!existing) {
                Database.getInstance().getUsersDao().create(u);
            }
        }
    }

    private static void generateReservations(Faker f, int nbr) throws SQLException {
        List<CampGround> CGlist = Database.getInstance().getCampgroundDao().queryForAll();
        List<Client> ClientsList = Database.getInstance().getClientsDao().queryForAll();
        for (int i = 0; i < nbr; i++) {
            var resa = new Reservation();

            try {
                resa.setCampground(CGlist.get(rdmNbrBtwn(0, CGlist.size())));
                resa.setClient(ClientsList.get(rdmNbrBtwn(0, ClientsList.size())));
                resa.setNbPersons(rdmNbrBtwn(1, 5));

                var cashbacks = CashBack.values();
                resa.setCashBack(cashbacks[rdmNbrBtwn(0, cashbacks.length)]);

                var equipments = resa.getCampground().getCompatiblesEquipments();
                resa.setEquipments(equipments.get(rdmNbrBtwn(0, equipments.size())));

                var services = resa.getCampground().getCompatiblesServices();
                resa.setSelectedServices(services.get(rdmNbrBtwn(0, services.size())));

                if (i > nbr / 2) {
                    // futur
                    resa.setDepositDate(rdmNbrBtwn(0, 10) > 5 ? f.date().past(50, TimeUnit.DAYS) : null);
                    resa.setStartDate(f.date().future(200, TimeUnit.DAYS, new java.util.Date()));
                    resa.setEndDate(f.date().future(30, TimeUnit.DAYS, resa.getStartDate()));

                    if (rdmNbrBtwn(0, 10) > 5) {
                        resa.setDepositDate(new Date());
                        if (rdmNbrBtwn(0, 10) > 5)
                            resa.setPaymentDate(new Date());
                    }
                } else {
                    // passé
                    resa.setStartDate(f.date().past(100, TimeUnit.DAYS, new java.util.Date()));
                    resa.setEndDate(f.date().future(30, TimeUnit.DAYS, resa.getStartDate()));

                    resa.setDepositDate(resa.getStartDate());
                    resa.setPaymentDate(resa.getEndDate());

                }
                Database.getInstance().getReservationDao().create(resa);
                Database.getInstance().getReservationDao().refresh(resa);
                System.out.println(resa);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void generateClientFidele(Faker f) throws SQLException {
        List<CampGround> CGlist = Database.getInstance().getCampgroundDao().queryForAll();
        List<Client> ClientsList = Database.getInstance().getClientsDao().queryForAll();
        while (nbResaFideleClient <= 4) {
            var resa = new Reservation();

            try {
                resa.setCampground(CGlist.get(rdmNbrBtwn(0, CGlist.size())));

                resa.setClient(ClientsList.get(0));

                resa.setNbPersons(rdmNbrBtwn(1, 5));

                var cashbacks = CashBack.values();
                resa.setCashBack(cashbacks[rdmNbrBtwn(0, cashbacks.length)]);

                var equipments = resa.getCampground().getCompatiblesEquipments();
                resa.setEquipments(equipments.get(rdmNbrBtwn(0, equipments.size())));

                var services = resa.getCampground().getCompatiblesServices();
                resa.setSelectedServices(services.get(rdmNbrBtwn(0, services.size())));

                resa.setDepositDate(f.date().past(50, TimeUnit.DAYS));
                resa.setStartDate(f.date().past(100, TimeUnit.DAYS, new java.util.Date()));
                resa.setEndDate(f.date().future(30, TimeUnit.DAYS, resa.getStartDate()));

                if (rdmNbrBtwn(0, 10) > 5) {
                    resa.setDepositDate(new Date());
                    if (rdmNbrBtwn(0, 10) > 5)
                        resa.setPaymentDate(new Date());
                }
                Database.getInstance().getReservationDao().create(resa);
                Database.getInstance().getReservationDao().refresh(resa);
                System.out.println(resa);
                nbResaFideleClient += 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void generateCampGrounds(Faker f, int nbr) throws SQLException {
        for (int i = 0; i < nbr; i++) {
            var cg = new CampGround();
            cg.setDescription(f.lorem().sentence().toString() + f.lorem().sentence().toString()
                    + f.lorem().sentence().toString());
            cg.setPricePerDays(f.number().randomDigitNotZero() * 100);
            cg.setSurface(f.number().randomDigitNotZero() * 10);
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
            try {
                var c = new Client();
                c.setAddresse(f.address().fullAddress());
                c.setPhone("0956674332");
                c.setName(f.name().lastName());
                c.setFirstName(f.name().firstName());
                c.setEmail(f.internet().emailAddress());
                Database.getInstance().getClientsDao().create(c);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void generateStocks(Faker f) throws SQLException {
        String[] item = { "Pansements", "Désinfectant", "Crème piqure moustique", "Bouteille d'eau 1L",
                "Bouteille d'eau 50cL", "Savon biodégradable", "Brosse à dents", "Papier toilette",
                "Parapluie", "Mug Flots Blancs", "T-shirt Flots Blancs", "Casquette Flots Blancs",
                "Pins Flots Blancs", "Magnet Flots Blancs", "Carte touristique", "Guide Touristique" };
        String[] location = { "Etagère 1", "Etagère 2", "Meuble de caisse", "Coffre" };
        Stock s;
        for (int i = 0; i < item.length; i++) {
            s = new Stock();
            s.setItem(item[i]);
            s.setQuantity(f.random().nextInt(500));
            s.setQuantityAlertThreshold(f.number().randomDigitNotZero() * 10);
            s.setStorageLocation(location[f.random().nextInt(location.length)]);
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
