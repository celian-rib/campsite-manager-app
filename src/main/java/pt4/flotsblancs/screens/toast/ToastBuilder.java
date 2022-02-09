package pt4.flotsblancs.screens.toast;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ToastBuilder 
{
    public static void createToast(ToastType type,Stage parentStage, String content, int milliSecDuration, int milliSecFadeInOut)
    {
        Stage stage=new Stage();
        
        stage.initOwner(parentStage);
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        
        Text text = new Text(content);
        text.setFont(Font.font("Verdana", 15));
        
        String rgbaBackground = "";
        
        switch(type)
        {
	        case ERROR:
	        	rgbaBackground = "rgba(248, 215, 218,1)";
	        	text.setFill(Color.rgb(132,32,41));
	        	break;
	        case INFO:
	        	break;
	        case SUCCESS:
	        	break;
	        case WARNING:
	        	break;
        }


        StackPane txt = new StackPane(text);
        txt.setStyle("-fx-background-radius: 5; -fx-background-color: "+rgbaBackground+"; -fx-padding: 15px;");
        txt.setOpacity(1);
        
        txt.setTranslateX(parentStage.getX()*2-(Screen.getPrimary().getBounds().getWidth()-parentStage.getWidth()));
        txt.setTranslateY(parentStage.getY()+parentStage.getHeight()-30);


        
        StackPane.setAlignment(txt, Pos.BOTTOM_CENTER);
        							  

        Scene scene = new Scene(txt);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        
        stage.show();
        
        /* CODE DE L ANIMATION DU TOAST */
        
        //Déplacement du haut vers le bas du toast
        final Timeline moveTimeline = new Timeline();
        moveTimeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(txt.layoutYProperty(), -100);
        final KeyFrame kf = new KeyFrame(Duration.millis(milliSecFadeInOut), kv);
        moveTimeline.getKeyFrames().add(kf);
        
        //Fade du toast
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(milliSecFadeInOut), txt);
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
                    Thread.sleep(milliSecDuration);
                } catch (InterruptedException e) { e.printStackTrace(); }
                
	                final Timeline moveTimelineOut = new Timeline();
	                final KeyValue kvOut = new KeyValue(txt.layoutYProperty(), 100);
	                final KeyFrame kfOut = new KeyFrame(Duration.millis(milliSecFadeInOut), kvOut);
	                moveTimelineOut.getKeyFrames().add(kfOut);
	                
	                //Fade du toast
	                FadeTransition fadeTransitionOut = new FadeTransition(Duration.millis(milliSecFadeInOut), txt);
	                fadeTransitionOut.setFromValue(1.0f);
	                fadeTransitionOut.setToValue(0.0f);
	                
	                ParallelTransition parallelTransition2 = new ParallelTransition();
	                parallelTransition2.getChildren().addAll(
	                		moveTimelineOut,
	                		fadeTransitionOut
	                );
	                
	                parallelTransition2.setOnFinished((aeb) -> stage.close());
	                
	                parallelTransition2.play();
                
            }).start();
        }); 

        parallelTransition.setInterpolator(Interpolator.EASE_BOTH);
        parallelTransition.play();
        
        /* FIN ANIMATION DU TOAST */
        

    }
    
    //background error F8D7DA
    //fill error 842029
    
}
