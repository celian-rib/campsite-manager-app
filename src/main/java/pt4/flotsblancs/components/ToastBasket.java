package pt4.flotsblancs.components;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ToastBasket extends GridPane
{
	private ArrayList<Pane> toasts;
	private BorderPane bottom;
	
	public ToastBasket(BorderPane toastPane)
	{
		this.toasts = new ArrayList<>();
    	bottom = new BorderPane();
    	bottom.setPadding(new Insets(15));
    	this.setVgap(5);
    	toastPane.setBottom(bottom);
    	bottom.setRight(this);
	}
	
	
	/**
	 * Retourne la taille de la liste des toasts 
	 * @return
	 */
	public int getSize()
	{
		return this.toasts.size();
	}

	
	/**
	 * Permet d'afficher un toast
	 * @param toast : toast à afficher
	 */
	public void display(Pane toast)
	{
		this.add(toast,0,getSize()); 
		this.toasts.add(toast);
	}
	
	/**
	 * Permet de retirer un toast
	 * @param toast : le toast à retirer
	 */
	public void remove(Pane toast)
	{
    	Platform.runLater(new Runnable() {
    	    @Override
    	    public void run() {
    			toasts.remove(toast);
    			getChildren().remove(toast);
    	    	updateToasts();
    	    }   
    	});
	}
	
	
	/** Récupère l'id d'un toast à partir de l'objet toast
	 * 
	 * @param toast
	 * @return
	 */
	public int getId(Pane toast)
	{
		int id = 0;
		for(Pane p : toasts)
		{
			if(p==toast)
				return id;
			id++;
		}
		return id;
	}
	
	//TODO : Fonction pour déplacer proprement les toasts 
	public void updateToasts()
	{
		ArrayList<Pane> substitution = new ArrayList<>();
		
		for(Pane p : toasts)
		{
			substitution.add(p);
			getChildren().remove(p);
		}

		this.toasts.removeAll(toasts);
		
		int i=0;

		for(Pane p : substitution)
		{
			this.toasts.add(p);
			this.add(p,0,i);
			i++;
		}
	}
}
