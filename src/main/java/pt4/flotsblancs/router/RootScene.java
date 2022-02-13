package pt4.flotsblancs.router;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.BaseScene;
import pt4.flotsblancs.scenes.toast.ToastBuilder;
import pt4.flotsblancs.scenes.toast.ToastType;

public class RootScene extends Group {

    private BaseScene currentScene;
    private boolean navBarIsActive;

    public RootScene() {
        startNavBar();
    }

    public void changeCurrentScene(BaseScene baseScene) {
        // Si la nouvelle page n'a pas besoin de la barre de navigation
        if (baseScene.showNavBar() == false) {
            // On efface tous les enfant
            this.getChildren().clear();
            this.navBarIsActive = false;
        } else {
            // Sinon on retire juste la scene actuelle (Et on garde la navbar)
            this.getChildren().remove(this.currentScene);
            if (this.navBarIsActive == false)
                // Si la scène que l'on va afficher veut la navbar mais qu'elle n'est pas déjà
                // affichée
                startNavBar();
        }

        this.currentScene = baseScene;
        this.getChildren().add(this.currentScene); // On ajoute la nouvelle page
    }

    private MFXButton button(String content, int sizex, int sizey) {
        MFXButton button = new MFXButton(content, sizex, sizey);
        String style = "-fx-background-color: #5fc9fa; -fx-text-fill: #ffffff; -fx-font-size: 20";
        button.setStyle(style);
        button.setButtonType(ButtonType.RAISED);
        return button;
    }

    public void startNavBar() {
        this.navBarIsActive = true;
        var hbox = new HBox(8);

        MFXButton btn1 = button("Clients", 200, 40);
        btn1.setOnAction(event -> Router.goToScreen(Routes.CLIENTS));

        MFXButton btn2 = button("Réservations", 200, 40);
        btn2.setOnAction(event -> Router.goToScreen(Routes.RESERVATIONS));

        MFXButton btn3 = button("Login", 200, 40);
        btn3.setOnAction(event -> Router.goToScreen(Routes.LOGIN));

        MFXButton btn4 = button("Accueil", 200, 40);
        btn4.setOnAction(event -> Router.goToScreen(Routes.HOME));
        
        
        //Boutons de test à supprimer toast
        
        MFXButton warning = new MFXButton("Warning");
        warning.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.WARNING,this,"Message de warning",3000,800));
        
        MFXButton error = new MFXButton("Error");
        error.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.ERROR,this,"Message d'erreur !",3000,800));
        
        MFXButton info = new MFXButton("Info");
        info.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.INFO,this,"Message d'information",3000,800));
        
        MFXButton success = new MFXButton("Success");
        success.setOnAction((arg0) -> ToastBuilder.createToast(ToastType.SUCCESS,this,"Message de succès :)",3000,800));
        

        hbox.getChildren().addAll(btn1, btn2, btn3, btn4, warning, error, info, success);
        // Ajout de la navbar
        this.getChildren().add(hbox);
    }
}
