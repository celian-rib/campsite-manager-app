package pt4.flotsblancs.scenes.components;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;

public class PromptedTextField extends MFXTextField {
    public PromptedTextField(String text, String prompt) {
        setText(text);
        setFloatingText(prompt);
        setFloatMode(FloatMode.INLINE);
        setMinWidth(180);
    }

    /**
     * @return le text de l'input non null (String vide dans le pire des cas)
     */
    public String getTextSafely() {
        if(super.getText() == null)
            return "";
        return super.getText();
    }
}
