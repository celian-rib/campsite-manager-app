package pt4.flotsblancs.scenes.utils;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ToastBuilder 
{
	
	/** Permet de lancer l'affichage d'un toast avec sa durée et son fadein/out time
	 * Il faut lui passer en paramètre deux Panes. Le premier, celui sur lequel le toast sera mis, et un autre,
	 * le container de celui-ci
	 * 
	 * @param toast : Pane qui a été retourné par la fonction createToast 
	 * @param container : Pane sur lequel le toast sera attaché 
	 * @param durationMillis : Durée d'affichage du toast sans compter les fadeinout
	 * @param fadeinoutMillis : Durée de transition pour l'affichage
	 */
	public static void playTransition(Pane toast, Pane container, int durationMillis, int fadeinoutMillis)
	{
        //Fade du toast
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeinoutMillis), container);
        fadeTransition.setFromValue(0.0f);
        fadeTransition.setToValue(1.0f);

        fadeTransition.setOnFinished((ae) -> 
        {
            new Thread(() -> {
                try
                {
                    Thread.sleep(durationMillis);
                } catch (InterruptedException e) { e.printStackTrace(); }
                
	                //Fade du toast
	                FadeTransition fadeTransitionOut = new FadeTransition(Duration.millis(fadeinoutMillis), container);
	                fadeTransitionOut.setFromValue(1.0f);
	                fadeTransitionOut.setToValue(0.0f);
	               
	                fadeTransitionOut.play();
            }).start();
        }); 
        
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        fadeTransition.play();
	}
	
	/** Permet de créer un toast avec le type indiqué et le contenu passé.
	 * 
	 * 
	 * @param type : Type du toast à créer (Warning, info, erreur...)
	 * @param content : Contenu du toast qui sera affiché à l'écran lors du .playTransition
	 * @see ToastBuilder.playTransition(Pane toast, Pane container, int durationMillis, int fadeinoutMillis)
	 * @return BorderPane : Le pane sur lequel le toast est attaché
	 */
    public static BorderPane createToast(ToastType type, String content)
    {
        
        Text text = new Text(content);
        text.setFont(Font.font("Verdana", 15));
        
        String rgbaBackground = "";
        
        BorderPane pane = new BorderPane();
        ImageView icon = new ImageView();
        
        icon.setFitHeight(25);
        icon.setFitWidth(25);
        icon.setImage(type.getIcon());
        
        switch(type)
        {
	        case ERROR:
	        	rgbaBackground = "rgba(248, 215, 218,1)";
	        	text.setFill(Color.rgb(132,32,41));
	        	icon.setEffect(new DropShadow(15, Color.rgb(132,32,41)));
	        	break;
	        case INFO:
	        	rgbaBackground = "rgba(207, 226, 255,1)";
	        	text.setFill(Color.rgb(72,66,152));
	        	icon.setEffect(new DropShadow(15, Color.rgb(72,66,152)));
	        	break;
	        case SUCCESS:
	        	rgbaBackground = "rgba(209, 231, 221,1)";
	        	text.setFill(Color.rgb(15,81,50));
	        	icon.setEffect(new DropShadow(15, Color.rgb(15,81,50)));
	        	break;
	        case WARNING:
	        	rgbaBackground = "rgba(255, 243, 205,1)";
	        	text.setFill(Color.rgb(102,77,0));
	        	icon.setEffect(new DropShadow(15, Color.rgb(102,77,0)));
	        	break;
        }
        StackPane txt = new StackPane(text);
        txt.setPadding(new Insets(0,0,0,10));
        
        pane.setStyle("-fx-background-radius: 5; -fx-background-color: "+rgbaBackground+"; -fx-padding: 15px;");
        pane.setOpacity(1);
        
        StackPane.setAlignment(pane, Pos.BOTTOM_CENTER);
        
        pane.setLeft(icon);
        pane.setCenter(txt);
        
        return pane;
    }
}
