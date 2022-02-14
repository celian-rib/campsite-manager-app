package pt4.flotsblancs.scenes.utils;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ToastBuilder 
{
	
	public static void playTransition(BorderPane toast, BorderPane container, int durationMillis, int fadeinoutMillis)
	{
        //Déplacement du haut vers le bas du toast
        final Timeline moveTimeline = new Timeline();
        moveTimeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(container.layoutYProperty(), -100);
        final KeyFrame kf = new KeyFrame(Duration.millis(fadeinoutMillis), kv);
        moveTimeline.getKeyFrames().add(kf);
        
        //Fade du toast
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeinoutMillis), container);
        fadeTransition.setFromValue(0.0f);
        fadeTransition.setToValue(1.0f);
  
        
        //Permet de combiner deux transitions en même temps
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                moveTimeline
        );
        
        
        
        parallelTransition.setOnFinished((ae) -> 
        {
            new Thread(() -> {
                try
                {
                    Thread.sleep(durationMillis);
                } catch (InterruptedException e) { e.printStackTrace(); }
                
	                final Timeline moveTimelineOut = new Timeline();
	                final KeyValue kvOut = new KeyValue(container.layoutYProperty(), 100);
	                final KeyFrame kfOut = new KeyFrame(Duration.millis(fadeinoutMillis), kvOut);
	                moveTimelineOut.getKeyFrames().add(kfOut);
	                
	                //Fade du toast
	                FadeTransition fadeTransitionOut = new FadeTransition(Duration.millis(fadeinoutMillis), container);
	                fadeTransitionOut.setFromValue(1.0f);
	                fadeTransitionOut.setToValue(0.0f);
	                
	                ParallelTransition parallelTransition2 = new ParallelTransition();
	                parallelTransition2.getChildren().addAll(
	                		moveTimelineOut,
	                		fadeTransitionOut
	                );
	                
	             //   parallelTransition2.setOnFinished((aeb) -> stage.close());
	                parallelTransition2.play();
            }).start();
        }); 
        parallelTransition.setInterpolator(Interpolator.EASE_BOTH);

        parallelTransition.play();
	}
	
    public static BorderPane createToast(ToastType type, String content)
    {
        /*Stage stage=new Stage();
     
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);*/
        
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
