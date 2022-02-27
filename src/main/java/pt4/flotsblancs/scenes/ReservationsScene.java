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
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.Service;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ReservationsScene extends ItemScene<Reservation> {

    private Label dayCount;

    private Label depositPrice;

    private Label totalPrice;

    private Label datesLabel;

    private Reservation reservation;

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

    private void createStartDateListener(MFXDatePicker picker) {
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
                refreshControls(!isSameDay(old, fromLocale(newDate)));
            }
        });
    }

    private void createEndDateListener(MFXDatePicker picker) {
        picker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate.isBefore(toLocale(reservation.getStartDate()))) {
                Router.showToast(ToastType.ERROR,
                        "La date de fin sélectionnée est antérieure à la date de début de la réservation.");
                picker.setValue(oldDate);
            } else {
                Date old = reservation.getEndDate();
                reservation.setEndDate(fromLocale(newDate));
                refreshControls(!isSameDay(old, fromLocale(newDate)));
            }
        });
    }

    private BorderPane createHeader() {
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

    private HBox createActionsButtonsSlot() {
        var container = new HBox(10);

        var bill = new MFXButton("Envoyer facture");
        var cancel = new MFXButton("Annuler la réservation");
        bill.getStyleClass().add("action-button");
        cancel.getStyleClass().add("action-button");

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(bill, cancel);
        return container;
    }

    private MFXComboBox<String> createPriceComboBox(String typeName) {
        var combo = new MFXComboBox<String>();
        combo.setFloatingText(typeName);
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll("Versé", "En attente");
        combo.setMinWidth(180);
        return combo;
    }

    private MFXComboBox<String> createCashbackComboBox() {
        var combo = new MFXComboBox<String>();
        combo.setFloatingText("Remise");
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll("Aucune", "10%", "20%", "50%");
        combo.selectIndex(0);
        combo.setMinWidth(180);
        return combo;
    }

    private void createDepositListener(MFXComboBox<String> comboBox) {
        comboBox.valueProperty().addListener((obs, oldPrice, newPrice) -> {
            if (oldPrice == null)
                return;
            if (newPrice.equals("Versé"))
                reservation.setDepositDate(new Date());
            else
                reservation.setDepositDate(null);
            refreshControls(true);
        });
    }

    private void createPayementListener(MFXComboBox<String> comboBox) {
        comboBox.valueProperty().addListener((obs, oldPrice, newPrice) -> {
            if (oldPrice == null)
                return;
            if (newPrice.equals("Versé"))
                reservation.setPaymentDate(new Date());
            else
                reservation.setPaymentDate(null);
            refreshControls(true);
        });
    }

    private MFXComboBox<Integer> createPersonCountComboBox() {
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
            refreshControls(oldValue.intValue() != newValue.intValue());
        });
        return combo;
    }

    private MFXComboBox<Service> createServiceComboBox() {
        var combo = new MFXComboBox<Service>();
        combo.setFloatingText("Services demandés");
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll(Service.NONE, Service.WATER_ONLY, Service.ELECTRICITY_ONLY,
                Service.WATER_AND_ELECTRICITY);
        combo.setMinWidth(180);
        combo.selectItem(reservation.getSelectedServices());
        combo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            reservation.setSelectedServices(newValue);
            refreshControls(oldValue != newValue);
        });
        return combo;
    }

    private MFXComboBox<Equipment> createEquipmentComboBox() {
        var combo = new MFXComboBox<Equipment>();
        combo.setFloatingText("Equipements client");
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll(Equipment.CAMPINGCAR, Equipment.MOBILHOME, Equipment.TENT,
                Equipment.TENT_AND_CAMPINGCAR);
        combo.setMinWidth(180);
        combo.selectItem(reservation.getEquipments());
        combo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue == null)
                return;
            reservation.setEquipments(newValue);
            refreshControls(oldValue != newValue);
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
     * ok - equipement client (modifiable)
     * 
     * ok - services supplémentaires (raccordement eau / électricité)
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

    private VBox cardsContainer() {
        VBox container = new VBox(50);
        container.setAlignment(Pos.CENTER);
        container.setMinWidth(220);
        var clientCard = new ClientCard(reservation.getClient(), 220);
        var campCard = new CampgroundCard(reservation.getCampground(), 220);
        container.getChildren().addAll(clientCard, campCard);
        return container;
    }

    private VBox datesContainer() {
        VBox container = new VBox(30);
        dayCount = new Label();
        var startDatePicker = createDatePicker(reservation.getStartDate(), "Date de début");
        createStartDateListener(startDatePicker);
        var endDatePicker = createDatePicker(reservation.getEndDate(), "Date de fin");
        createEndDateListener(endDatePicker);
        container.getChildren().addAll(startDatePicker, endDatePicker, dayCount);
        return container;
    }

    private VBox selectedGearContainer() {
        VBox container = new VBox(30);
        var personCount = createPersonCountComboBox();
        var service = createServiceComboBox();
        var equipments = createEquipmentComboBox();
        container.getChildren().addAll(personCount, service, equipments);
        return container;
    }


    private HBox createTopSlot() {
        HBox container = new HBox(10);
        container.setPadding(new Insets(50));

        var cards = cardsContainer();
        var gear = selectedGearContainer();
        var dates = datesContainer();

        container.getChildren().add(cards);
        container.getChildren().add(createHorizontaleSpacer());
        container.getChildren().add(gear);
        container.getChildren().add(createHorizontaleSpacer());
        container.getChildren().add(dates);

        return container;
    }

    private HBox createBottomSlot() {
        HBox container = new HBox(10);
        container.setPadding(new Insets(50));

        depositPrice = new Label();
        totalPrice = new Label();
        container.getChildren().addAll();

        var deposit = createPriceComboBox("Acompte");
        deposit.selectIndex(reservation.getDepositDate() == null ? 1 : 0);
        createDepositListener(deposit);

        var payment = createPriceComboBox("Réglement complet");
        payment.selectIndex(reservation.getPaymentDate() == null ? 1 : 0);
        createPayementListener(payment);

        var cashback = createCashbackComboBox();

        container.getChildren().addAll(depositPrice, totalPrice, deposit, payment, cashback);

        return container;
    }

    @Override
    protected Region createContainer(Reservation reservation) {
        this.reservation = reservation;

        VBox container = new VBox();
        container.setPadding(new Insets(50));

        container.getChildren().add(createHeader());
        container.getChildren().add(createVerticalSpacer());
        container.getChildren().add(createTopSlot());
        container.getChildren().add(createVerticalSpacer());
        container.getChildren().add(createBottomSlot());
        container.getChildren().add(createVerticalSpacer());
        container.getChildren().add(createActionsButtonsSlot());

        refreshControls(false);
        return container;
    }

    private Region createVerticalSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private Region createHorizontaleSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private void refreshControls(boolean updateDatabase) {
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
