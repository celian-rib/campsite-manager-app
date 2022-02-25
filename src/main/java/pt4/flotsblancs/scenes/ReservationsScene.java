package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt4.flotsblancs.components.ClientCard;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ReservationsScene extends ItemScene<Reservation> {

    @Override
    public String getName() {
        return "Réservations";
    }

    private static LocalDate toLocale(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static Date fromLocale(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private VBox createDatePicker(Date defaultDate, String prompt) {
        VBox container = new VBox(3);
        container.setMaxWidth(190);

        MFXDatePicker picker = new MFXDatePicker();
        picker.setPromptText(prompt);
        picker.setValue(toLocale(defaultDate));
        picker.setText(toLocale(defaultDate).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        Label label = new Label(prompt);
        container.getChildren().addAll(label, picker);
        return container;
    }

    private void createStartDateListener(MFXDatePicker picker, Reservation reservation) {
        picker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isBefore(LocalDate.now())) {
                Router.showToast(ToastType.ERROR,
                        "La date de début sélectionnée est antérieur à la date actuelle");
                picker.setValue(oldDate);
            } else if (newDate.isAfter(toLocale(reservation.getEndDate()))) {
                Router.showToast(ToastType.ERROR,
                        "La date de début sélectionnée est ultérieure à la date de fin");
                picker.setValue(oldDate);
            } else {
                try {
                    reservation.setStartDate(fromLocale(newDate));
                    Database.getInstance().getReservationDao().update(reservation);
                    Router.showToast(ToastType.SUCCESS, "Date de début mise à jour");
                } catch (SQLException e) {
                    Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
                    Router.goToScreen(Routes.HOME);
                }
            }
        });
    }

    private void createEndDateListener(MFXDatePicker picker, Reservation reservation) {
        picker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isBefore(toLocale(reservation.getStartDate()))) {
                Router.showToast(ToastType.ERROR,
                        "La date de fin sélectionnée est antérieure à la date de début de la réservation.");
                picker.setValue(oldDate);
            } else {
                try {
                    reservation.setEndDate(fromLocale(newDate));
                    Database.getInstance().getReservationDao().update(reservation);
                    Router.showToast(ToastType.SUCCESS, "Date de fin mise à jour");
                } catch (SQLException e) {
                    Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
                    Router.goToScreen(Routes.HOME);
                }
            }
        });
    }

    @Override
    protected Region createContainer(Reservation reservation) {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        var clientCard = new ClientCard(reservation.getClient());

        var startDatePicker = createDatePicker(reservation.getStartDate(), "Date de début");
        createStartDateListener((MFXDatePicker) startDatePicker.getChildren().get(1), reservation);

        var endDatePicker = createDatePicker(reservation.getEndDate(), "Date de fin");
        createEndDateListener((MFXDatePicker) endDatePicker.getChildren().get(1), reservation);

        container.getChildren().addAll(clientCard, startDatePicker, endDatePicker);
        container.setPadding(new Insets(30));
        return container;
    }

    @Override
    protected List<Reservation> queryAll() throws SQLException {
        return Database.getInstance().getReservationDao().queryForAll();
    }
}
