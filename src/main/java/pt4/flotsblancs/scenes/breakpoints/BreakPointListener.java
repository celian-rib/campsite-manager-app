package pt4.flotsblancs.scenes.breakpoints;

public interface BreakPointListener {
    /**
     * Déclencher lorsque la taille de la fenêtre subit un passage sur un point de rupture.
     * 
     * @param oldBp ancien point de rupture
     * @param newBp nouveau point de rupture
     */
    public default void onVerticalBreak(VBreakPoint oldBp, VBreakPoint newBp) {
        // Pas d'implémentation abstraite
    }

    /**
     * Déclencher lorsque la taille de la fenêtre subit un passage sur un point de rupture.
     * 
     * @param oldBp ancien point de rupture
     * @param newBp nouveau point de rupture
     */
    public default void onHorizontalBreak(HBreakPoint oldBp, HBreakPoint newBp) {
        // Pas d'implémentation abstraite
    }
}
