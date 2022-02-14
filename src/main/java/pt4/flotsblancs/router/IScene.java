package pt4.flotsblancs.router;

public interface IScene {
    public boolean showNavBar();

    /**
     * @return le nom de cette scène (Page). Ce nom sera affiché en haut de l'écran
     */
    public String getName();

    /**
     * Appelé au moment ou cette scène est chargée (Au lancement de l'app)
     */
    public abstract void start();

    /**
     * Appelé au moment ou cette scène est affichée à l'écran par le routeur
     */
    public abstract void onFocus();
}
