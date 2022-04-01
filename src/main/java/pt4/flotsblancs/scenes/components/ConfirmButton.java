package pt4.flotsblancs.scenes.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import lombok.Setter;

public class ConfirmButton extends MFXButton {

    private boolean needConfirm;

    @Setter
    private EventHandler<ActionEvent> onConfirmedAction;

    /**
     * Permet de créer un bouton ayant un comportement de confirmation
     * 
     * Il faut cliquer 2 fois sur le bouton pour valider l'action
     * 
     * @param text text de base du bouton
     */
    public ConfirmButton(String text) {
        super(text);
        this.needConfirm = true;

        setGraphicTextGap(20);

        setOnAction(e -> {
            var width = getWidth();
            if (needConfirm) {
                setText("Confirmer");
                setMinWidth(width);
            } else {
                setText(text);
                if (onConfirmedAction != null)
                    onConfirmedAction.handle(e);
            }
            needConfirm = !needConfirm;
        });
    }
}
