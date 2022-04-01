package pt4.flotsblancs;

import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
public class Stats {
	@Getter
	private int nbClientIncomming;
	@Getter
	private int nbClientOutgoing;
	@Getter
	private int nbReservations;

	@Getter
	private float averageProblemTime;
	@Getter
	private HashMap<CampGround, Integer> mostProblematicCamps;
	@Getter
	private HashMap<CampGround, Integer> mostRentedCamps;

	@AllArgsConstructor
	public enum Period {
		TODAY("Aujourd'hui"),
		WEEKLY("Semaine précédente"),
		MONTHLY("Le mois dernier"),
		ANNUALY("Dernière année"),
		TWO_YEAR("Deux dernière années"),
		THREE_YEAR("Trois dernières années"),
		NEXT_WEEK("Prochaine semaine"),
		NEXT_MONTH("Mois prochain"),
		NEXT_YEAR("Prochaine année"),
		NEXT_TWO_YEAR("Deux prochaines années"),
		NEXT_THREE_YEAR("Trois prochaines années");

		@Getter
		private String name;

		@Override
		public String toString() {
			return name;
		}

		/**
		 * Retourne vrai si la période sélectionnée est dans le futur.
		 */
		public boolean isInFuture() {
			return (this == NEXT_WEEK || this == NEXT_MONTH || this == NEXT_YEAR
					|| this == NEXT_TWO_YEAR || this == NEXT_THREE_YEAR);
		}
	}

	/**
	 * Permet de générer l'ensemble des statistiques sur une période donnée
	 */
	public Stats(Period period) throws SQLException {
		this.nbClientIncomming = nbClientToday(false);
		this.nbClientOutgoing = nbClientToday(true);
		this.nbReservations = nbReservations(period);
		this.averageProblemTime = averageProblemTime(period);
		this.mostProblematicCamps = mostProblematicCampground(period);
		this.mostRentedCamps = mostRentedCampground(period);
	}

	private int nbClientToday(boolean isLeaving) throws SQLException {
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		var dbr = Database.getInstance().getReservationDao();
		String nowDateString = dateFormat.format(now);

		String value = "start_date";
		if (isLeaving)
			value = "end_date";

		List<Reservation> reservations = dbr.query((dbr.queryBuilder().where()
				.raw("'" + nowDateString + "' = DATE(" + value + ")").prepare()));

		return reservations.size();
	}

	private float averageProblemTime(Period period) throws SQLException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		Date endDate = c.getTime();

		switch (period) {
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
				c.add(Calendar.DATE, -365 * 2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, -365 * 3);
				break;
			case NEXT_WEEK:
				c.add(Calendar.DATE, 7);
				break;
			case NEXT_MONTH:
				c.add(Calendar.DATE, 30);
				break;
			case NEXT_YEAR:
				c.add(Calendar.DATE, 365);
				break;
			case NEXT_TWO_YEAR:
				c.add(Calendar.DATE, 365 * 2);
				break;
			case NEXT_THREE_YEAR:
				c.add(Calendar.DATE, 365 * 3);
				break;
			default:
				break;
		}

		Date startDate = c.getTime();

		if (startDate.after(endDate)) {
			Date switcher = startDate;
			startDate = endDate;
			endDate = switcher;
		}

		String endDateString = dateFormat.format(endDate);
		String startDateString = dateFormat.format(startDate);

		var dbr = Database.getInstance().getProblemDao();

		List<Problem> problems = dbr.query((dbr.queryBuilder().where()
				.raw("('" + startDateString + "' <= DATE(start_date) AND '" + endDateString + "'"
						+ " >= DATE(start_date)) OR ('" + startDateString + "'"
						+ " <= DATE(end_date) AND '" + endDateString + "'"
						+ " >= DATE(start_date))")
				.prepare()));

		float duration = 0f;
		int pbNb = 0;

		for (Problem p : problems) {
			Date start = p.getStartDate();
			Date end = p.getEndDate();
			if (start != null & end != null) {

				Long startSeconds = start.getTime() / 1000;
				Long endSeconds = end.getTime() / 1000; // Nombre de secondes

				Long between = endSeconds - startSeconds; // Distance en seconde entre les deux
															// dates

				duration += between / 3600 / 24; // On divise 3600 -> heures -> 24 -> jours, moyenne
													// en jours
				pbNb++;
			}
		}

		return duration / pbNb;

	}

	private HashMap<CampGround, Integer> mostProblematicCampground(Period period)
			throws SQLException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		Date endDate = c.getTime();

		switch (period) {
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
				c.add(Calendar.DATE, -365 * 2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, -365 * 3);
				break;
			case NEXT_WEEK:
				c.add(Calendar.DATE, 7);
				break;
			case NEXT_MONTH:
				c.add(Calendar.DATE, 30);
				break;
			case NEXT_YEAR:
				c.add(Calendar.DATE, 365);
				break;
			case NEXT_TWO_YEAR:
				c.add(Calendar.DATE, 365 * 2);
				break;
			case NEXT_THREE_YEAR:
				c.add(Calendar.DATE, 365 * 3);
				break;
			default:
				break;
		}

		Date startDate = c.getTime();

		if (startDate.after(endDate)) {
			Date switcher = startDate;
			startDate = endDate;
			endDate = switcher;
		}

		String endDateString = dateFormat.format(endDate);
		String startDateString = dateFormat.format(startDate);

		var dbr = Database.getInstance().getProblemDao();

		List<Problem> problems = dbr.query((dbr.queryBuilder().where()
				.raw("('" + startDateString + "' <= DATE(start_date) AND '" + endDateString + "'"
						+ " >= DATE(start_date)) OR ('" + startDateString + "'"
						+ " <= DATE(end_date) AND '" + endDateString + "'"
						+ " >= DATE(start_date))")
				.prepare()));

		HashMap<CampGround, Integer> worstCamps = new HashMap<CampGround, Integer>();

		for (Problem problem : problems) {
			CampGround camp = problem.getCampground();

			if (camp != null) {
				if (worstCamps.containsKey(camp)) {
					worstCamps.put(camp, worstCamps.get(camp) + 1);
				} else {
					worstCamps.put(camp, 1);
				}
			}
		}

		return worstCamps;
	}

	private HashMap<CampGround, Integer> mostRentedCampground(Period period) throws SQLException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		Date endDate = c.getTime();

		switch (period) {
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
				c.add(Calendar.DATE, -365 * 2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, -365 * 3);
				break;
			case NEXT_WEEK:
				c.add(Calendar.DATE, 7);
				break;
			case NEXT_MONTH:
				c.add(Calendar.DATE, 30);
				break;
			case NEXT_YEAR:
				c.add(Calendar.DATE, 365);
				break;
			case NEXT_TWO_YEAR:
				c.add(Calendar.DATE, 365 * 2);
				break;
			case NEXT_THREE_YEAR:
				c.add(Calendar.DATE, 365 * 3);
				break;
			default:
				break;
		}

		Date startDate = c.getTime();

		if (startDate.after(endDate)) {
			Date switcher = startDate;
			startDate = endDate;
			endDate = switcher;
		}

		String endDateString = dateFormat.format(endDate);
		String startDateString = dateFormat.format(startDate);

		var dbr = Database.getInstance().getReservationDao();

		List<Reservation> reservations = dbr.query(
				(dbr.queryBuilder().where()
						.raw("DATE(start_date) >= '" + startDateString
								+ "' AND DATE(start_date) <= '" + endDateString + "'")
						.prepare()));

		HashMap<CampGround, Integer> camps = new HashMap<CampGround, Integer>();

		for (Reservation r : reservations) {
			CampGround camp = r.getCampground();

			if (camp != null) {
				if (camps.containsKey(camp)) {
					camps.put(camp, camps.get(camp) + 1);
				} else {
					camps.put(camp, 1);
				}
			}
		}
		return camps;
	}

	private int nbReservations(Period period) throws SQLException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();

		Date startDate = c.getTime();

		switch (period) {
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
				c.add(Calendar.DATE, -365 * 2);
				break;
			case THREE_YEAR:
				c.add(Calendar.DATE, -365 * 3);
				break;
			case NEXT_WEEK:
				c.add(Calendar.DATE, 7);
				break;
			case NEXT_MONTH:
				c.add(Calendar.DATE, 30);
				break;
			case NEXT_YEAR:
				c.add(Calendar.DATE, 365);
				break;
			case NEXT_TWO_YEAR:
				c.add(Calendar.DATE, 365 * 2);
				break;
			case NEXT_THREE_YEAR:
				c.add(Calendar.DATE, 365 * 3);
				break;
			default:
				break;
		}

		Date endDate = c.getTime();

		if (startDate.after(endDate)) {
			Date switcher = startDate;
			startDate = endDate;
			endDate = switcher;
		}

		String startDateString = dateFormat.format(startDate);
		String endDateString = dateFormat.format(endDate);

		var dbr = Database.getInstance().getReservationDao();

		List<Reservation> reservations = dbr.query((dbr.queryBuilder().where()
				.raw("('" + startDateString + "' <= DATE(start_date) AND '" + endDateString + "'"
						+ " >= DATE(start_date)) OR ('" + startDateString + "'"
						+ " <= DATE(end_date) AND '" + endDateString + "'"
						+ " >= DATE(start_date))")
				.prepare()));

		return reservations.size();
	}
}
