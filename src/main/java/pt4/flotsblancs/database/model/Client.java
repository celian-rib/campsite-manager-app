package pt4.flotsblancs.database.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import javafx.scene.paint.Color;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.daos.ReservationDAO;
import pt4.flotsblancs.database.model.types.LogType;
import pt4.flotsblancs.database.model.types.ProblemStatus;
import pt4.flotsblancs.scenes.items.Item;
import pt4.flotsblancs.scenes.utils.StatusColors;
import pt4.flotsblancs.scenes.utils.TxtFieldValidation;

@EqualsAndHashCode
@DatabaseTable(tableName = "clients")
public class Client implements Item {

    @Getter
    @DatabaseField(generatedId = true)
    private int id;

    @Getter
    @DatabaseField(canBeNull = false)
    private String name;

    @Getter
    @DatabaseField(canBeNull = false, columnName = "first_name")
    private String firstName;

    @Getter
    @DatabaseField
    private String preferences;

    @Getter
    @DatabaseField(canBeNull = false)
    private String addresse;

    @Getter
    @DatabaseField(canBeNull = false)
    private String phone;

    @Getter
    @DatabaseField(canBeNull = false)
    private String email;

    @Getter
    @ForeignCollectionField(eager = false)
    private Collection<Problem> problems;

    @Getter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Reservation> reservations;

    @Getter
    @DatabaseField(canBeNull = false)
    private Date creationDate;

    public Client() {
        this.creationDate = new Date();
    }

    public Client(String name) throws SQLException {
        this.creationDate = new Date();
        this.firstName = "NOUVEAU Prénom";
        this.name = "NOUVEAU Nom";
        this.addresse = "NOUVEAU Adresse";
        this.phone = "00 00 00 00 00";
        this.preferences = null;
        this.email = "jean.dupond@mail.com";
        Database.getInstance().getClientsDao().create(this);
        Database.getInstance().getClientsDao().refresh(this);
        User.addlog(LogType.ADD, "Ajout d'un nouveau client");
    }

    public void setPhone(String phone) throws ConstraintException {
        if (TxtFieldValidation.phoneValidation(phone)) {
            this.phone = phone;
            User.addlog(LogType.MODIFY,
                    "Téléphone du client " + getDisplayName() + " changé pour " + phone);
            return;
        }
        throw new ConstraintException("Téléphone invalide, modification annulée", false);
    }

    public void setAddresse(String addresse) throws ConstraintException {
        if (!TxtFieldValidation.phoneValidation(addresse)
                && !TxtFieldValidation.emailValidation(addresse)) {
            User.addlog(LogType.MODIFY,
                    "Addresse du client " + getDisplayName() + " changé pour " + addresse);
            this.addresse = addresse;
            return;
        }
        throw new ConstraintException("Adresse invalide, modification annulée", false);
    }

    public void setPreferences(String preferences) {
        User.addlog(LogType.MODIFY,
                "Préférences du client " + getDisplayName() + " changé pour " + preferences);
        this.preferences = preferences;
    }

    public void setFirstName(String firstName) {
        User.addlog(LogType.MODIFY,
                "Prénom du client " + getDisplayName() + " changé pour " + firstName);
        this.firstName = firstName;
    }

    public void setName(String name) {
        User.addlog(LogType.MODIFY, "Nom du client " + getDisplayName() + " changé pour " + name);
        this.name = name;
    }

    public void setEmail(String email) throws ConstraintException {
        if (TxtFieldValidation.emailValidation(email)) {
            User.addlog(LogType.MODIFY,
                    "Email du client " + getDisplayName() + " changé pour " + email);
            this.email = email;
            return;
        }
        throw new ConstraintException("Email invalide, modification annulée", false);
    }

    public Reservation getOpenReservation() {
        if(!isForeignCorrect())
            return null;
        return reservations.stream().filter(r -> r.getPaymentDate() == null && !r.getCanceled()).findFirst()
                .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return toString();
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.name;
    }

    @Override
    public String getSearchString() {
        return String.join(";", "" + this.id, this.firstName, this.name, this.addresse, this.phone)
                .trim().toLowerCase();
    }

    @Override
    public boolean isForeignCorrect() {
        return reservations != null && problems != null;
    }

    public boolean hasOpenProblem() {
        if (problems.size() == 0)
            return false;
        return problems
                .stream()
                .anyMatch(p -> p.getStatus() == ProblemStatus.OPEN || p.getStatus() == ProblemStatus.OPEN_URGENT);
    }

    @Override
    public Color getStatusColor() {
        return hasOpenProblem() ? StatusColors.RED : StatusColors.BLUE;
    }

    private int getSortScore() {
        int score = 0;
        score -= this.getOpenReservation() != null ? 100 : 0;
        // score -= this.hasOpenProblem() ? 10 : 0;
        System.out.println(this.toString());
        return score;

    }

    public boolean isKing() {
        // TODO bouger ça dans un DAO, et le renommer en isFrequentClient ou autre (mais plus explicite)
        int clientId = this.getId();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        String currentDateString = dateFormat.format(currentDate);

        try {
            ReservationDAO dbr = Database.getInstance().getReservationDao();
            List<Reservation> reservations = dbr.query((dbr.queryBuilder().where()
                    .raw("DATEDIFF('" + currentDateString + "',DATE(end_date))>=0 AND client_id = " + clientId)
                    .prepare()));

            return reservations.size() >= 3;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isKing()
    {
    	
    	int clientId = this.getId();
    	
    	for(int i=0;i<5;i++);
    	System.out.println("");
    	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date currentDate = c.getTime();
		String currentDateString = dateFormat.format(currentDate);
		
    	
    	try {
			ReservationDAO dbr = Database.getInstance().getReservationDao();
			
	        List<Reservation> reservations = dbr.query((dbr.queryBuilder().where()
	                .raw("DATEDIFF('"+currentDateString+"',DATE(end_date))>=0 AND client_id = "+clientId)
	                .prepare()));
	

	        
	        if(reservations.size() >= 3)
	        {
	        	return true;
	        }
	        
	    	for(int i=0;i<5;i++);
	    	System.out.println("");
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    	
    	return false;
    }

    @Override
    public int compareTo(Item o) {
        Client other = (Client) o;
        return name.compareTo(other.getName());
        // le tri par problème est compliqué à implémenter, l'utilisation de stream
        // ralentit l'UI
        // Et j'ai pas réussi à implémenter l'async.

        // int score = getSortScore(), otherScore = other.getSortScore();
        // return (score - otherScore) + name.compareTo(other.getName());
    }

}
