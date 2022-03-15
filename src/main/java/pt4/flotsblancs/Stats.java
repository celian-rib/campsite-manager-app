package pt4.flotsblancs;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;

public class Stats 
{
	


	enum Period {
		WEEKLY, MONTHLY, ANNUALY;
	}
	
	public static void main(String[] args) throws SQLException
	{
//		 Dao<?, String> campDao = Database.getInstance().getCampgroundDao();
//		 Dao<?, String> problemDao = Database.getInstance().getProblemDao();
//		 Dao<?, String> resaDao = Database.getInstance().getReservationDao();
		 
		 int affluence = affluence(Period.ANNUALY);
	}
	
	public static List<CampGround> mostChoosen()
	{
		return null;
	}
	
	public static List<CampGround> mostProblems()
	{
		return null;
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
