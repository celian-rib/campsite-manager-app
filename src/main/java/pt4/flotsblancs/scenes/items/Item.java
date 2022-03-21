package pt4.flotsblancs.scenes.items;

import javafx.scene.paint.Color;

public interface Item {
	/**
	 * 
	 * @return Nom d'affichage condensé complet de l'item (Utilisé pour l'affichage à l'intérieur de
	 *         l'ItemList
	 */
	public String getDisplayName();

	/**
	 * 
	 * @return identifiant de l'item
	 */
	public int getId();

	/**
	 * 
	 * @return Une chaîne de caractère permettant de rechercher
	 * parmis les données de l'item
	 */
	public String getSearchString();

	/**
	 * 
	 * @return Une Paint javafx qui définit l'état de l'item
	 */
	public Color getStatusColor();
}
