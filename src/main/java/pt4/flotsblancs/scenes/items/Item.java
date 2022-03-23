package pt4.flotsblancs.scenes.items;

import javafx.scene.paint.Color;

public interface Item extends Comparable<Item> {
	/**
	 * 
	 * @return Nom d'affichage condensé complet de l'item (Utilisé pour l'affichage
	 *         à l'intérieur de
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
	 *         parmis les données de l'item
	 */
	public String getSearchString();

	/**
	 * @return vrai si cette instance d'Item est correctement initialisé par
	 *         l'ORM
	 */
	public boolean isForeignCorrect();

	/**
	 * @return Couleur qui définit l'état de l'item
	 */
	public Color getStatusColor();
}
