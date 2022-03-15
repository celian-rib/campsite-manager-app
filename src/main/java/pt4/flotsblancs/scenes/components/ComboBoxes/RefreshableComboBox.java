package pt4.flotsblancs.scenes.components.ComboBoxes;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class RefreshableComboBox<T> extends MFXComboBox<T> {

    /**
     * Permet de mettre à jour la valeur affiché par de cette ComboBox
     * 
     * L'appel à refresh ne déclenchera pas les userChangedListener
     */
    public abstract void refresh();

    /**
     * Permet d'inscire un listener sur la valeur de la ComboBox qui ecoute UNIQUEMENT les
     * changements de valeurs causés directement par l'utilisateur et non par un rafraichissement ou
     * un effet de bord
     */
    public void addUserChangedValueListener(ChangeListener<? super T> listener) {
        var wrapper = new ChangeListener<>() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (oldValue == null || newValue == null || oldValue == newValue)
                    return;
                if (isShowing())
                    listener.changed(observable, (T) oldValue, (T) newValue);
            };
        };
        valueProperty().addListener(wrapper);
    }
    
    public void addUserChangedValueListenerNoCheck(ChangeListener<? super T> listener) {
        var wrapper = new ChangeListener<>() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (isShowing())
                    listener.changed(observable, (T) oldValue, (T) newValue);
            };
        };
        valueProperty().addListener(wrapper);
    }
}
