package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXStepper;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.components.ClientCard;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ReservationsScene extends ItemScene<Reservation> {

    private Label dayCount;

    private Label depositPrice;

    private Label totalPrice;

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

    public static boolean isSameDay(Date date1, Date date2) {
        Instant instant1 = date1.toInstant().truncatedTo(ChronoUnit.DAYS);
        Instant instant2 = date2.toInstant().truncatedTo(ChronoUnit.DAYS);
        return instant1.equals(instant2);
    }

    private MFXDatePicker createDatePicker(Date defaultDate, String prompt) {
        MFXDatePicker picker = new MFXDatePicker();
        picker.setPromptText(prompt);
        picker.setValue(toLocale(defaultDate));
        picker.setText(toLocale(defaultDate).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        picker.setFloatingText(prompt);
        picker.setFloatMode(FloatMode.BORDER);
        picker.setDisable(toLocale(defaultDate).isBefore(LocalDate.now()));
        return picker;
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
                Date old = reservation.getEndDate();
                reservation.setStartDate(fromLocale(newDate));
                refreshControls(reservation, !isSameDay(old, fromLocale(newDate)));
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
                Date old = reservation.getEndDate();
                reservation.setEndDate(fromLocale(newDate));
                refreshControls(reservation, !isSameDay(old, fromLocale(newDate)));
            }
        });
    }

    private BorderPane createHeader(Reservation reservation) {
        BorderPane container = new BorderPane();
        Label title = new Label("Réservation #" + reservation.getId());
        title.setFont(new Font(20));
        title.setTextFill(Color.rgb(51, 59, 97));
        container.setLeft(title);
        return container;
    }

    private VBox createPersonCountSlider(Reservation reservation) {
        var container = new VBox(5);

        var label = new Label();
        label.setText(reservation.getNbPersons() + " personnes");

        var slider = new MFXSlider();
        slider.setMin(1);
        slider.setMax(10);
        slider.setValue(reservation.getNbPersons());
        slider.showMajorTicksProperty();
        slider.valueProperty().addListener((obs, oldValue, newValue) -> {
            reservation.setNbPersons(newValue.intValue());
            refreshControls(reservation, true);
        });

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(label, slider);
        return container;
    }

    private HBox createActionsButtonsSlot(Reservation reservation) {
        var container = new HBox(5);

        var bill = new MFXButton("Envoyer facture");
        var cashback = new MFXButton("Faire une remise");
        var cancel = new MFXButton("Annuler réservation");

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(bill, cashback, cancel);
        return container;
    }

    /*
     * ok - Titre
     * 
     * ok - Nom prénom tel + voir fiche client
     * 
     * ok - Nb personnes (modifiable)
     * 
     * - emplacement(modifiable)
     * 
     * - equipement (modifiable)
     * 
     * ok - début (modifiable)
     * 
     * ok - fin (modifiable)
     * 
     * - problèmes liés à la réservations
     * 
     * - ajouter un problème (lien vers page ajout problm)
     * 
     * ok - prix total
     * 
     * ok - prix acompté
     * 
     * - acompte versé oui / non
     * 
     * - réglement total oui / non
     * 
     * - envoyer facture
     * 
     * - faire une remise (geste commercial)
     * 
     * - annuler
     */
    @Override
    protected Region createContainer(Reservation reservation) {
        VBox container = new VBox(10);
        container.setAlignment(Pos.TOP_CENTER);

        var header = createHeader(reservation);

        var clientCard = new ClientCard(reservation.getClient());

        var startDatePicker = createDatePicker(reservation.getStartDate(), "Date de début");
        createStartDateListener(startDatePicker, reservation);

        var endDatePicker = createDatePicker(reservation.getEndDate(), "Date de fin");
        createEndDateListener(endDatePicker, reservation);

        dayCount = new Label();
        depositPrice = new Label();
        totalPrice = new Label();

        var personCountSlider = createPersonCountSlider(reservation);

        var deposit = new MFXCheckbox("Acompte versée");
        deposit.setSelected(reservation.getDepositDate() != null);

        var actionButtons = createActionsButtonsSlot(reservation);

        refreshControls(reservation, false);
        container.getChildren().addAll(header, clientCard, startDatePicker, endDatePicker);
        container.getChildren().addAll(dayCount, depositPrice, totalPrice, personCountSlider);
        container.getChildren().addAll(deposit, actionButtons);
        container.setPadding(new Insets(50));
        return container;
    }

    private void refreshControls(Reservation reservation, boolean updateDatabase) {
        dayCount.setText("Nombre de jours : " + reservation.getDayCount());
        depositPrice.setText("Prix acompte : " + reservation.getDepositPrice());
        totalPrice.setText("Prix total : " + reservation.getTotalPrice());

        if (updateDatabase) {
            try {
                Database.getInstance().getReservationDao().update(reservation);
                Router.showToast(ToastType.SUCCESS, "Réservation mise à jour");
            } catch (SQLException e) {
                Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
                Router.goToScreen(Routes.HOME);
            }
        }
    }

    @Override
    protected List<Reservation> queryAll() throws SQLException {
        return Database.getInstance().getReservationDao().queryForAll();
    }
}
