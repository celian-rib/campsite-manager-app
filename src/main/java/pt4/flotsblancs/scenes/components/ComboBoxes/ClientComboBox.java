package pt4.flotsblancs.scenes.components.ComboBoxes;

import java.sql.SQLException;

import io.github.palexdev.materialfx.enums.FloatMode;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Problem;

public class ClientComboBox extends RefreshableComboBox<Client> {
    private Problem problem;

    /**
     * Combo box permettant de sélectionner un client pour un problème donné
     * 
     * @param problem
     * @throws SQLException
     */
    public ClientComboBox(Problem problem) throws SQLException {
        this.problem = problem;

        setFloatingText("Clients");
        setFloatMode(FloatMode.INLINE);

        getItems().addAll(Database.getInstance().getClientsDao().queryForAll());
        setMinWidth(180);
        setAnimated(false);

        refresh();

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            problem.setClient(newValue);
        });
    }

    @Override
    public void refresh() {
        if (problem.getClient() != null)
            selectItem(problem.getClient());
    }
}
