package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.components.CampgroundCard;
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

    Label datesLabel;

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

        Label title = new Label("Réservation  #" + reservation.getId());
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        datesLabel = new Label();
        datesLabel.setFont(new Font(20));
        datesLabel.setTextFill(Color.GREY);

        container.setLeft(title);
        container.setRight(datesLabel);
        return container;
    }

    private HBox createActionsButtonsSlot(Reservation reservation) {
        var container = new HBox(10);

        var bill = new MFXButton("Envoyer facture");
        var cancel = new MFXButton("Annuler la réservation");
        bill.getStyleClass().add("action-button");
        cancel.getStyleClass().add("action-button");

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(bill, cancel);
        return container;
    }

    private MFXComboBox<String> createPriceComboBox(Reservation reservation, String typeName) {
        var combo = new MFXComboBox<String>();
        combo.setFloatingText(typeName);
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll("Versé", "En attente");
        combo.setMinWidth(180);
        return combo;
    }

    private void createDepositListener(MFXComboBox<String> comboBox, Reservation reservation) {
        comboBox.valueProperty().addListener((obs, oldPrice, newPrice) -> {
            if (oldPrice == null)
                return;
            if (newPrice.equals("Versé"))
                reservation.setDepositDate(new Date());
            else
                reservation.setDepositDate(null);
            refreshControls(reservation, true);
        });
    }

    private void createPayementListener(MFXComboBox<String> comboBox, Reservation reservation) {
        comboBox.valueProperty().addListener((obs, oldPrice, newPrice) -> {
            if (oldPrice == null)
                return;
            if (newPrice.equals("Versé"))
                reservation.setPaymentDate(new Date());
            else
                reservation.setPaymentDate(null);
            refreshControls(reservation, true);
        });
    }

    private MFXComboBox<Integer> createPersonCountComboBox(Reservation reservation) {
        var combo = new MFXComboBox<Integer>();
        combo.setFloatingText("Nombre de personnes");
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        combo.setMinWidth(180);
        combo.selectItem(reservation.getNbPersons());
        combo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            reservation.setNbPersons(newValue.intValue());
            refreshControls(reservation, oldValue.intValue() != newValue.intValue());
        });
        return combo;
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
     * - equipement client (modifiable)
     * 
     * - services supplémentaires (raccordement eau / électricité)
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
     * ok - acompte versé oui / non
     * 
     * ok - réglement total oui / non
     * 
     * ok - envoyer facture
     * 
     * ok - faire une remise (geste commercial)
     * 
     * ok - annuler
     */
    // TODO -> ranger + ajouter controls manquants + faire le layout
    @Override
    protected Region createContainer(Reservation reservation) {
        VBox container = new VBox();

        var header = createHeader(reservation);
        var clientCard = new ClientCard(reservation.getClient(), 300);

        BorderPane topContainer = new BorderPane();
        topContainer.setTop(header);
        topContainer.setCenter(clientCard);
        BorderPane.setMargin(header, new Insets(0, 0, 20, 0));

        VBox centerContainer = new VBox(20);
        var startDatePicker = createDatePicker(reservation.getStartDate(), "Date de début");
        createStartDateListener(startDatePicker, reservation);

        var endDatePicker = createDatePicker(reservation.getEndDate(), "Date de fin");
        createEndDateListener(endDatePicker, reservation);

        dayCount = new Label();
        depositPrice = new Label();
        totalPrice = new Label();

        var personCountSlider = createPersonCountComboBox(reservation);

        var campCard = new CampgroundCard(reservation.getCampground(), 300);

        var deposit = createPriceComboBox(reservation, "Acompte");
        deposit.selectIndex(reservation.getDepositDate() == null ? 1 : 0);
        createDepositListener(deposit, reservation);

        var payment = createPriceComboBox(reservation, "Réglement complet");
        payment.selectIndex(reservation.getPaymentDate() == null ? 1 : 0);
        createPayementListener(payment, reservation);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.getChildren().addAll(campCard, startDatePicker, endDatePicker, dayCount,
                depositPrice, totalPrice, personCountSlider, deposit, payment);

        var actionButtons = createActionsButtonsSlot(reservation);

        refreshControls(reservation, false);


        // container.setTop(topContainer);
        // container.setCenter(centerContainer);
        // container.setBottom(actionButtons);

        container.getChildren().add(header);
        container.getChildren().add(createSpacer());
        container.getChildren().add(centerContainer);
        container.getChildren().add(createSpacer());
        container.getChildren().add(actionButtons);

        // container.getChildren().addAll(header, clientCard, startDatePicker, endDatePicker);
        // container.getChildren().addAll(dayCount, depositPrice, totalPrice, personCountSlider);
        // container.getChildren().addAll(deposit, payment, actionButtons);
        container.setPadding(new Insets(50));
        return container;
    }

    private Region createSpacer() {
        final Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private void refreshControls(Reservation reservation, boolean updateDatabase) {
        dayCount.setText("Nombre de jours : " + reservation.getDayCount());
        depositPrice.setText("Prix acompte : " + reservation.getDepositPrice());
        totalPrice.setText("Prix total : " + reservation.getTotalPrice());

        String startStr = toLocale(reservation.getStartDate())
                .format(DateTimeFormatter.ofPattern("E dd MMM", Locale.FRANCE));
        String endStr = toLocale(reservation.getEndDate())
                .format(DateTimeFormatter.ofPattern("E dd MMM", Locale.FRANCE));
        datesLabel.setText(startStr + " - " + endStr);

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
