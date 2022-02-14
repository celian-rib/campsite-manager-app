package pt4.flotsblancs.scenes.utils;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class TransitionBuilder 
{
	public static FadeTransition fadeTransition(float fromAlpha, float toAlpha, int durationMillis, Pane pane)
	{
		FadeTransition transition = new FadeTransition(Duration.millis(durationMillis), pane);
		transition.setFromValue(fromAlpha);
		transition.setToValue(toAlpha);
		transition.setInterpolator(Interpolator.EASE_BOTH);
		
		return transition;
	}
}


