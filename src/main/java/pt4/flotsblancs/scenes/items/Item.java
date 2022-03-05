package pt4.flotsblancs.scenes.items;

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
}
