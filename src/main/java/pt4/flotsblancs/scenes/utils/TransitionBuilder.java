package pt4.flotsblancs.scenes.utils;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class TransitionBuilder {

	/**
	 * Permet de générer un objet FadeTransition appliqué à un Pane passé en paramètre, il suffit
	 * d'appeler sur l'objet retourner .play() pour lancer la transition de fase
	 * 
	 * @param fromAlpha : Transparence de départ, de 0 (invisible) à 1 (complètement visible)
	 * @param toAlpha : Transparence d'arrivée, de 0 (invisible) à 1 (complètement visible)
	 * @param durationMillis : Durée de la transition
	 * @param pane : Panneau auquel appliquer la transition
	 * @return transition : Objet sur lequel on peut appeler la méthode play() pour lancer
	 *         l'animation
	 */
	public static FadeTransition fadeTransition(float fromAlpha, float toAlpha, int durationMillis,
			Pane pane) {
		FadeTransition transition = new FadeTransition(Duration.millis(durationMillis), pane);
		transition.setFromValue(fromAlpha);
		transition.setToValue(toAlpha);
		transition.setInterpolator(Interpolator.EASE_BOTH);

		return transition;
	}
}


