package pt4.flotsblancs.router;

public interface IScene {
    /**
     * Permet de spécifier si oui ou non la barre de navigation doit être affichée quand cette page
     * est à l'écran
     * 
     * @return true si la navbar doit être affichée
     */
    public default boolean showNavBar() {
        return true;
    }

    /**
     * @return le nom de cette scène (Page). Ce nom sera affiché en haut de l'écran
     */
    public default String getName() {
        return null;
    };

    /**
     * Appelée au moment ou cette scène est chargée (Au lancement de l'app)
     */
    public void start();

    /**
     * Appelée au moment ou cette scène est affichée à l'écran par le routeur
     */
    public default void onFocus() {};

    /**
     * Appelée au moment ou cette scène est retiré de l'écran par le routeur
     */
    public default void onUnfocus() {};

    
}
