package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.components.*;
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

    private Label datesLabel;

    private Reservation reservation;

    private final int INNER_PADDING = 10;

    ChangeListener<? super Object> changeListener = (obs, oldValue, newValue) -> {
        // Check if we need to refresh the page and the database
        if (oldValue == newValue || oldValue == null)
            return;
        refreshPage();
        refreshDatabase();
    };

    private void refreshPage() {
        // Rafraichit tous les labels de la page ayant une valeur calculé
        dayCount.setText("Nombre de jours : " + reservation.getDayCount());
        depositPrice.setText("Prix acompte : " + reservation.getDepositPrice());
        totalPrice.setText("Prix total : " + reservation.getTotalPrice());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd MMM", Locale.FRANCE);
        String startStr = simpleDateFormat.format(reservation.getStartDate());
        String endStr = simpleDateFormat.format(reservation.getEndDate());
        datesLabel.setText(startStr + " - " + endStr);
    }

    private void refreshDatabase() {
        try {
            Database.getInstance().getReservationDao().update(reservation);
            Router.showToast(ToastType.SUCCESS, "Réservation mise à jour");
        } catch (SQLException e) {
            Router.showToast(ToastType.ERROR, "Erreur de mise à jour...");
            Router.goToScreen(Routes.HOME);
        }
    }

    @Override
    public String getName() {
        return "Réservations";
    }

    @Override
    protected Region createContainer(Reservation reservation) {
        this.reservation = reservation;

        VBox container = new VBox();
        container.setPadding(new Insets(50));

        container.getChildren().add(createHeader());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createTopSlot());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createBottomSlot());
        container.getChildren().add(new VBoxSpacer());
        container.getChildren().add(createActionsButtonsSlot());

        refreshPage();
        return container;
    }

    /**
     * @return Conteneur avec les cartes, les equipements et services, les sélections de dates
     */
    private HBox createTopSlot() {
        HBox container = new HBox(10);
        // TODO Responsive padding
        // container.setPadding(new Insets(50));
        container.setPadding(new Insets(INNER_PADDING));

        var cards = cardsContainer();
        var gear = selectedEquipmentAndServicesContainer();
        var dates = datesContainer();

        container.getChildren().add(cards);
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(gear);
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(dates);

        return container;
    }

    /**
     * @return Conteneur avec problèmes et contrôles de paiement
     */
    private HBox createBottomSlot() {
        HBox container = new HBox(10);
        // TODO Responsive padding
        // container.setPadding(new Insets(50));
        container.setPadding(new Insets(INNER_PADDING));

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

    /**
     * @return VBox contenant la carte du client et la carte de l'emplacement associés à cette
     *         réservation
     */
    private VBox cardsContainer() {
        VBox container = new VBox(35);
        container.setPadding(new Insets(10, 0, 0, 0));
        container.setMinWidth(220);
        var clientCard = new ClientCard(reservation.getClient(), 220);
        var campCard = new CampgroundCard(reservation.getCampground(), 220);
        container.getChildren().addAll(clientCard, campCard);
        return container;
    }

    /**
     * @return Conteneur contenant les ComboBox des dates de début de fin de la réservation
     */
    private VBox datesContainer() {
        VBox container = new VBox(30);
        dayCount = new Label();
        var startDatePicker = new ReservationDatePicker(reservation, true);
        startDatePicker.addListener(changeListener);
        var endDatePicker = new ReservationDatePicker(reservation, false);
        endDatePicker.addListener(changeListener);
        container.getChildren().addAll(startDatePicker, endDatePicker, dayCount);
        return container;
    }


    /**
     * @return conteneur contenant les ComboBox de sélection de l'equipement / services / nb
     *         personnes
     */
    private VBox selectedEquipmentAndServicesContainer() {
        VBox container = new VBox(30);

        var personCount = new PersonCountComboBox(reservation);
        personCount.addListener(changeListener);

        var service = new ServiceComboBox(reservation);
        service.addListener(changeListener);

        var equipments = new EquipmentComboBox(reservation);
        equipments.addListener(changeListener);

        container.getChildren().addAll(personCount, service, equipments);
        return container;
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
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

    /**
     * @return Boutons d'actions en bas de la page
     */
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

    /**
     * @param typeName
     * @return ComboBox pour les état de paiement (Versé / Attente)
     */
    private MFXComboBox<String> createPriceComboBox(String typeName) {
        var combo = new MFXComboBox<String>();
        combo.setFloatingText(typeName);
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll("Versé", "En attente");
        combo.setMinWidth(180);
        combo.setAnimated(false);
        return combo;
    }

    /**
     * @param typeName
     * @return ComboBox pour choisir une remise
     */
    private MFXComboBox<String> createCashbackComboBox() {
        var combo = new MFXComboBox<String>();
        combo.setFloatingText("Remise");
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll("Aucune", "10%", "20%", "50%");
        combo.selectIndex(0);
        combo.setMinWidth(180);
        combo.setAnimated(false);
        return combo;
    }

    /**
     * Listener de la ComboBox de l'acompte
     * 
     * Lance la mise à jour de l'interface et de la BD si la valeur change
     * 
     * @param comboBox
     */
    private void createDepositListener(MFXComboBox<String> comboBox) {
        comboBox.valueProperty().addListener((obs, oldPrice, newPrice) -> {
            if (oldPrice == null)
                return;
            if (newPrice.equals("Versé"))
                reservation.setDepositDate(new Date());
            else
                reservation.setDepositDate(null);
            refreshPage();
            refreshDatabase();
        });
    }

    /**
     * Listener de la ComboBox de paiement
     * 
     * Lance la mise à jour de l'interface et de la BD si la valeur change
     * 
     * @param comboBox
     */
    private void createPayementListener(MFXComboBox<String> comboBox) {
        comboBox.valueProperty().addListener((obs, oldPrice, newPrice) -> {
            if (oldPrice == null)
                return;
            if (newPrice.equals("Versé"))
                reservation.setPaymentDate(new Date());
            else
                reservation.setPaymentDate(null);
            refreshPage();
            refreshDatabase();
        });
    }

    @Override
    protected List<Reservation> queryAll() throws SQLException {
        return Database.getInstance().getReservationDao().queryForAll();
    }
}
