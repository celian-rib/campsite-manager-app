package pt4.flotsblancs.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import lombok.Setter;

public class ConfirmButton extends MFXButton {

    private boolean needConfirm;

    @Setter
    private EventHandler<ActionEvent> onConfirmedAction;

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
