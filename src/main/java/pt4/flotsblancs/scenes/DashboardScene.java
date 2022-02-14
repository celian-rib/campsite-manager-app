package pt4.flotsblancs.scenes;

import javafx.scene.text.Font;
import pt4.flotsblancs.router.IScene;
import pt4.flotsblancs.scenes.toast.ToastBuilder;
import pt4.flotsblancs.scenes.toast.ToastType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardScene extends VBox implements IScene {

    @Override
    public String getName() {
        return "Accueil";
    }

    @Override
    public boolean showNavBar() {
        return true;
    }

    @Override
    public void start() {
        setAlignment(Pos.CENTER);
        setSpacing(40);

        Label label = new Label("Demo material FX");
        label.setFont(new Font("Arial", 25));

        MFXProgressSpinner spinner = new MFXProgressSpinner();
        MFXSlider slider = new MFXSlider();

        MFXDatePicker datePicker = new MFXDatePicker();
        datePicker.setPromptText("Sélectionner une date");

        MFXCheckbox checkBox = new MFXCheckbox("Bonsoir");
        MFXToggleButton toggle = new MFXToggleButton("Bonsoir");

        getChildren().addAll(label, slider, datePicker, checkBox, toggle, spinner);
    }
}
