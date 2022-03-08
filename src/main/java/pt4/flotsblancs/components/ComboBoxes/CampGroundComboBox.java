package pt4.flotsblancs.components.ComboBoxes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.CampGround;
import pt4.flotsblancs.database.model.Reservation;

public class CampGroundComboBox extends MFXComboBox<CampGround> {

    private final Reservation reservation;

    @SuppressWarnings("unchecked")
	public CampGroundComboBox(Reservation reservation) throws SQLException {
        this.reservation = reservation;

        setFloatingText("Emplacement");
        setFloatMode(FloatMode.INLINE);
        // TODO afficher que les emplacements dispos sur les dates de la résa
        
        Long startDate = reservation.getStartDate().toInstant().toEpochMilli()/1000;
        Long endDate   = reservation.getEndDate  ().toInstant().toEpochMilli()/1000;
        
        var dbr = Database.getInstance().getReservationDao();
        var dbc = Database.getInstance().getCampgroundDao();
        
        List<Reservation> emplacementIncompatible = dbr.query(
        		(dbr.queryBuilder()
        		.where()
        		.raw(startDate+" <= UNIX_TIMESTAMP(start_date)")
				.and()
				.raw(endDate+" >= UNIX_TIMESTAMP(start_date)")
				.or()
        		.raw(startDate+" <= UNIX_TIMESTAMP(end_date)")
				.and()
				.raw(endDate+" >= UNIX_TIMESTAMP(end_date)")
				.prepare()));
        
        
        /*
         * SELECT * FROM reservations WHERE start <= UNIX_TIMESTAMP(start_date) AND end >= UNIX_TIMESTAMP(start_date)
							OR   start <= UNIX_TIMESTAMP(end_date) AND end >= UNIX_TIMESTAMP(end_date)
         * 
         */
        
        List<Integer> ids = new ArrayList<>();
        
        //On récupère les ids des réservations incompatibles
        for(Reservation r : emplacementIncompatible)
        {
        	if(r.getId()!=this.reservation.getId()) {
        		ids.add(r.getCampground().getId());
        		System.out.println(this.reservation.getId()+" incompatible avec:"+r.getId());
        	}
        	System.out.println("");
        }

        //On check dans la table des campground qu'il n'y a pas un id incompatible et on l'ajoute
        
        List<CampGround> campgrounds = dbc.query(
        		(dbc.queryBuilder().where().raw("id NOT IN "+getIds(ids)).prepare()
        	));
        
        System.out.println(campgrounds.size());
        
        getItems().addAll(campgrounds);

        
        //db.executeRaw("SELECT * FROM reservations WHERE start_date >= "+endDate+" OR end_date <= "+startDate);
        
        //On selectionne les reservations où:
        //La date de fin X <= date de fin Y
        // OU QUE  date de début X => date début Y
        
        setMinWidth(180);
        setAnimated(false);
        refresh();
        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            // TODO Check si l'emplacmeent est disponibles sur les dates de la résa
            reservation.setCampground(newValue);
        });
    }
    
    public String getIds(List<Integer> ids)
    {
    	String s = "(-1,";
    	for(Integer i : ids)
    	{
    		s+=i+",";
    	}
    	s = s.substring(0, s.length()-1);
    	s += ")";
    	
    	return s;
    }

    public void refresh() {
        selectItem(reservation.getCampground());
    }

    public void addListener(ChangeListener<? super CampGround> listener) {
        valueProperty().addListener(listener);
    }
}
