package pt4.flotsblancs;

import java.sql.SQLException;
import java.sql.Date; //ne pas supprimer, cet import EST utilisé
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;
import com.github.javafaker.Pokemon;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.Stock;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.database.model.Problem.ProblemStatus;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.Service;


public class Generator {
    public static void main(String[] args) throws SQLException {
        Database.getInstance(); // Initialisation connexion BD

        var f = new Faker();

        generateAdmin();
        
        generateStocks(f, 10);
        generateClients(f, 50);
        generateCampGrounds(f, 100);
        generateReservations(f, 20);
        generateProblemsResa(f, 5);
        generateProblemsCg(f, 3);
        generateProblemsClient(f, 3);
    }
    
    private static int rdmNbrBtwn(int min, int max){
        Random r = new Random();
        return r.nextInt(max-min) + min;
    }
    
    private static void generateAdmin() throws SQLException{
        var u = new User();
        u.setAdmin(true);
        u.setFirstName("polp");
        u.setName("ogba");
        u.setPassword(User.sha256("test"));
        u.setLogin("test");
        Database.getInstance().getUsersDao().create(u);
    }

    private static void generateReservations(Faker f, int nbr) throws SQLException{
        List<CampGround> CGlist = Database.getInstance().getCampgroundDao().queryForAll();
        List<Client> ClientsList = Database.getInstance().getClientsDao().queryForAll();
        for(int i = 0; i < nbr; i++) {
            var resa = new Reservation();
            resa.setCampground(CGlist.get(rdmNbrBtwn(0,CGlist.size())));
            resa.setClient(ClientsList.get(rdmNbrBtwn(0,ClientsList.size())));
            resa.setNbPersons(rdmNbrBtwn(1, 5));
            resa.setCashBackPercent(rdmNbrBtwn(0, 10) > 8 ? 20 : 0);
            resa.setDepositDate(f.date().past(50, TimeUnit.DAYS));
            resa.setStartDate(f.date().future(200, TimeUnit.DAYS, new java.util.Date()));
            resa.setEndDate(f.date().future(30, TimeUnit.DAYS, resa.getStartDate()));
            // TODO -> random enum selection
            resa.setEquipments(Equipment.TENT);
            Database.getInstance().getReservationDao().create(resa);
            System.out.println(resa);
        }
    }
    
    private static void generateCampGrounds(Faker f, int nbr) throws SQLException{ 
        for(int i = 0; i < nbr; i++) {
            var cg = new CampGround();
            cg.setDescription(f.country().name());
            cg.setPricePerDays(f.number().randomDigitNotZero());
            cg.setSurface(f.number().randomDigitNotZero());
            // TODO -> random enum selection
            cg.setAllowedEquipments(Equipment.TENT_AND_CAMPINGCAR);
            // TODO -> random enum selection
            cg.setProvidedServices(Service.WATER_AND_ELECTRICITY);
            Database.getInstance().getCampgroundDao().create(cg);
            System.out.println(cg);
        }
    }
    
    private static void generateClients(Faker f, int nbr) throws SQLException{
        for(int i = 0; i < nbr; i++) {
            var c = new Client();
            c.setAddresse(f.address().fullAddress());
            c.setPhone(f.phoneNumber().cellPhone().toString());
            String hp = f.harryPotter().character();
            c.setName((hp.split(" ").length>1)?hp.split(" ")[1]:hp.split(" ")[0]);
            c.setFirstName(f.dragonBall().character().split(" ")[0]);
            Database.getInstance().getClientsDao().create(c);
            System.out.println(c);
        }        
    }

    private static void generateStocks(Faker f, int nbr) throws SQLException{
        for(int i = 0; i < nbr; i++) {
            var s = new Stock();
            Pokemon p = f.pokemon();
            s.setItem(p.name());
            s.setQuantity(f.number().randomDigit());
            s.setQuantityAlertThreshold(5);
            s.setStorageLocation(p.location());
            Database.getInstance().getStockDao().create(s);
            System.out.println(s.getItem()+" "+s.getQuantity()+" "+s.getStorageLocation());
        }
    }

    private static void generateProblemsResa(Faker f, int nbr) throws SQLException{
        List<Reservation> resaList = Database.getInstance().getReservationDao().queryForAll();
        for (int i=0; i<nbr; i++){
            var p = new Problem();
            p.setReservation(resaList.get(rdmNbrBtwn(0, resaList.size())));
            p.setCampground(p.getReservation().getCampground());
            p.setClient(p.getReservation().getClient());
            p.setDescription(f.elderScrolls().city());
            p.setStartDate(f.date().between(p.getReservation().getStartDate(), p.getReservation().getEndDate()));
            int rdm = rdmNbrBtwn(1,4);
            switch(rdm){
                case 1 :
                    p.setStatus(ProblemStatus.OPEN);
                    break;
                case 2 : 
                    p.setStatus(ProblemStatus.OPEN_URGENT);
                    break;
                default :
                    p.setStatus(ProblemStatus.SOLVED);
            }
            Database.getInstance().getProblemDao().create(p);
            System.out.println(p);
        }
    }

    private static void generateProblemsClient(Faker f, int nbr) throws SQLException{
        List<Client> ClientsList = Database.getInstance().getClientsDao().queryForAll();
        for(int i = 0; i < nbr; i++) {
            var p = new Problem();
            p.setClient(ClientsList.get(rdmNbrBtwn(0,ClientsList.size())));
            p.setDescription(f.elderScrolls().city());
            p.setStartDate(f.date().past(30, TimeUnit.DAYS));
            int rdm = rdmNbrBtwn(1,4);
            switch(rdm){
                case 1 :
                    p.setStatus(ProblemStatus.OPEN);
                    break;
                case 2 : 
                    p.setStatus(ProblemStatus.OPEN_URGENT);
                    break;
                default :
                    p.setStatus(ProblemStatus.SOLVED);
            }
            Database.getInstance().getProblemDao().create(p);
            System.out.println(p);
        }

    }
    private static void generateProblemsCg(Faker f, int nbr) throws SQLException{
        List<CampGround> CGlist = Database.getInstance().getCampgroundDao().queryForAll();
        for(int i = 0; i < nbr; i++) {
            var p = new Problem();
            p.setCampground(CGlist.get(rdmNbrBtwn(0,CGlist.size())));
            p.setDescription(f.elderScrolls().city());
            p.setStartDate(f.date().past(30, TimeUnit.DAYS));
            int rdm = rdmNbrBtwn(1,4);
            switch(rdm){
                case 1 :
                    p.setStatus(ProblemStatus.OPEN);
                    break;
                case 2 : 
                    p.setStatus(ProblemStatus.OPEN_URGENT);
                    break;
                default :
                    p.setStatus(ProblemStatus.SOLVED);
            }
            Database.getInstance().getProblemDao().create(p);
            System.out.println(p);
        }
    }
}
