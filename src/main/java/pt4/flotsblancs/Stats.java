package pt4.flotsblancs;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;

public class Stats 
{

	@AllArgsConstructor
	public enum Period {
		WEEKLY("Cette semaine"), 
		MONTHLY("Ce mois"), 
		ANNUALY("Cette année"), 
		TWO_YEAR("Deux dernière années"), 
		THREE_YEAR("Trois dernières années");

		@Getter
		private String name;

		@Override
		public String toString() {
			return name;
		}
	}
	
	public static void main(String[] args) throws SQLException
	{
		System.out.println(averageProblemTime(Period.ANNUALY));
	}
	
	public static int nbClientToday(boolean isLeaving) throws SQLException
	{
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		var dbr = Database.getInstance().getReservationDao();
		String nowDateString = dateFormat.format(now);
		
		String value = "start_date";
		if(isLeaving)
			value = "end_date";
		
		List<Reservation> reservations = dbr.query((dbr.queryBuilder().where()
                .raw("'"+nowDateString+"' = DATE("+value+")")
                .prepare()));
		
		
		
		return reservations.size();
	}
	
	public static float averageProblemTime(Period period) throws SQLException
	{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		
		Date endDate = c.getTime();
		
		switch(period)
		{
			case WEEKLY:
				c.add(Calendar.DATE, -7);
				break;
			case MONTHLY:
				c.add(Calendar.DATE, -30);
				break;
			case ANNUALY:
				c.add(Calendar.DATE, -365);
				break;
			case TWO_YEAR:
				c.add(Calendar.DATE, -365*2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, -365*3);
				break;
		}
		
		Date startDate = c.getTime();
		
		String endDateString = dateFormat.format(endDate);
		String startDateString = dateFormat.format(startDate);
		
		var dbr = Database.getInstance().getProblemDao();
		
        List<Problem> problems = dbr.query((dbr.queryBuilder().where()
                .raw("('" + startDateString + "' < DATE(start_date) AND '" + endDateString + "'"
                        + " > DATE(start_date)) OR ('" + startDateString + "'" + " < DATE(end_date) AND '"
                        + endDateString + "'" + " > DATE(start_date))")
                .prepare()));
        
        float duration = 0f;
        int pbNb = 0;
        
        for(Problem p : problems)
        {
        	Date start = p.getStartDate();
        	Date end = p.getEndDate();
        	if(start != null & end != null)
        	{
        		
        		Long startSeconds = start.getTime()/1000;
        		Long endSeconds   = end  .getTime()/1000; //Nombre de secondes
        		
        		Long between = endSeconds - startSeconds; //Distance en seconde entre les deux dates
        		
        		duration += between/3600/24; //On divise 3600 -> heures -> 24 -> jours, moyenne en jours
        		pbNb++;
        	}
        }
        
        return duration/pbNb;
        
	}
	
	public static HashMap<CampGround,Integer> mostProblematicCampground(Period period) throws SQLException
	{
		// TODO trier par nombre de problèmes
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		
		Date endDate = c.getTime();
		
		switch(period)
		{
			case WEEKLY:
				c.add(Calendar.DATE, -7);
				break;
			case MONTHLY:
				c.add(Calendar.DATE, -30);
				break;
			case ANNUALY:
				c.add(Calendar.DATE, -365);
				break;
			case TWO_YEAR:
				c.add(Calendar.DATE, -365*2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, -365*3);
				break;
		}
		
		Date startDate = c.getTime();
		
		String endDateString = dateFormat.format(endDate);
		String startDateString = dateFormat.format(startDate);
		
		var dbr = Database.getInstance().getProblemDao();
		
        List<Problem> problems = dbr.query((dbr.queryBuilder().where()
                .raw("('" + startDateString + "' < DATE(start_date) AND '" + endDateString + "'"
                        + " > DATE(start_date)) OR ('" + startDateString + "'" + " < DATE(end_date) AND '"
                        + endDateString + "'" + " > DATE(start_date))")
                .prepare()));
        
        HashMap<CampGround,Integer> worstCamps = new HashMap<CampGround,Integer>();
        
        for(Problem problem : problems)
        {
        	CampGround camp = problem.getCampground();
        	
        	if(camp != null)
        	{
        		if(worstCamps.containsKey(camp))
        		{
        			worstCamps.put(camp, worstCamps.get(camp)+1);
        		} else {
        			worstCamps.put(camp, 1);
        		}
        	}
        }	
        
		return worstCamps;
	}
	
	public static HashMap<CampGround,Integer> mostRentedCampground(Period period) throws SQLException
	{
		// TODO trier par nombre de reservations
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		
		Date endDate = c.getTime();
		
		switch(period)
		{
			case WEEKLY:
				c.add(Calendar.DATE, -7);
				break;
			case MONTHLY:
				c.add(Calendar.DATE, -30);
				break;
			case ANNUALY:
				c.add(Calendar.DATE, -365);
				break;
			case TWO_YEAR:
				c.add(Calendar.DATE, -365*2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, -365*3);
				break;
		}
		
		
		Date startDate = c.getTime();
		
		String endDateString = dateFormat.format(endDate);
		String startDateString = dateFormat.format(startDate);
		
		var dbr = Database.getInstance().getReservationDao();
		
        List<Reservation> reservations = dbr.query((dbr.queryBuilder().where()
                .raw("('" + startDateString + "' < DATE(start_date) AND '" + endDateString + "'"
                        + " > DATE(start_date)) OR ('" + startDateString + "'" + " < DATE(end_date) AND '"
                        + endDateString + "'" + " > DATE(start_date))")
                .prepare()));
        
        HashMap<CampGround,Integer> camps = new HashMap<CampGround,Integer>();
        
        for(Reservation r : reservations)
        {
        	CampGround camp = r.getCampground();
 
        	if(camp != null) {
	        	if(camps.containsKey(camp))
	        	{
	        		camps.put(camp, camps.get(camp)+1);
	        	} else {
	        		camps.put(camp,1);
	        	}
        	}
        }	
		return camps;
	}

	public static int affluence(Period period) throws SQLException
	{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		
		Date startDate = c.getTime();
		
		switch(period)
		{
			case WEEKLY:
				c.add(Calendar.DATE, 7);
				break;
			case MONTHLY:
				c.add(Calendar.DATE, 30);
				break;
			case ANNUALY:
				c.add(Calendar.DATE, 365);
				break;
			case TWO_YEAR:
				c.add(Calendar.DATE, 365*2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, 365*3);
				break;
		}
		
		Date endDate = c.getTime();
		
		String startDateString = dateFormat.format(startDate);
		String endDateString   = dateFormat.format(endDate);
		
		var dbr = Database.getInstance().getReservationDao();
		
        List<Reservation> reservations = dbr.query((dbr.queryBuilder().where()
                .raw("('" + startDateString + "' < DATE(start_date) AND '" + endDateString + "'"
                        + " > DATE(start_date)) OR ('" + startDateString + "'" + " < DATE(end_date) AND '"
                        + endDateString + "'" + " > DATE(start_date))")
                .prepare()));
		
		return reservations.size();
	}
}
