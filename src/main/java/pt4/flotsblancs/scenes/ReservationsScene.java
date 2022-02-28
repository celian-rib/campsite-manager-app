package pt4.flotsblancs.scenes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.kordamp.ikonli.javafx.FontIcon;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt4.flotsblancs.components.*;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.Problem;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.database.model.types.CashBack;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.items.ItemScene;
import pt4.flotsblancs.scenes.utils.ToastType;

public class ReservationsScene extends ItemScene<Reservation> {

    private final int INNER_PADDING = 10;
    private final int CONTENT_SPACING = 20;

    private Label title;

    private Label dayCount;

    private Label depositPrice;

    private Label totalPrice;

    private Label datesLabel;

    private Reservation reservation;

    private CampgroundCard campCard;

    private MFXComboBox<String> depositComboBox;

    private MFXComboBox<String> paymentComboBox;

    private MFXComboBox<CashBack> cashBackComboBox;

    private ReservationDatePicker startDatePicker;

    private ReservationDatePicker endDatePicker;

    private CampGroundComboBox campComboBox;

    private ServiceComboBox serviceComboBox;

    private PersonCountComboBox personCountComboBox;

    private EquipmentComboBox equipmentsComboBox;

    private MFXButton sendBillBtn;

    private ConfirmButton cancelBtn;

    private VBox noProblemContainer;

    private Label noProblemLabel;

    ChangeListener<? super Object> changeListener = (obs, oldValue, newValue) -> {
        // Check if we need to refresh the page and the database
        if (oldValue == newValue || oldValue == null)
            return;
        refreshPage();
        refreshDatabase();
    };

    private void refreshPage() {
        // Rafraichit tous les labels de la page ayant une valeur calculé
        String cancelStr = reservation.getCanceled() ? " (Annulée)" : "";
        title.setText("Réservation  #" + reservation.getId() + cancelStr);

        dayCount.setText(reservation.getDayCount() + " jours");
        depositPrice.setText("Prix acompte : " + reservation.getDepositPrice() + "€");
        totalPrice.setText("Prix total : " + reservation.getTotalPrice() + "€");
        campCard.refresh(reservation.getCampground());
        noProblemContainer.setVisible(reservation.getProblems().size() == 0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd MMM", Locale.FRANCE);
        String startStr = simpleDateFormat.format(reservation.getStartDate());
        String endStr = simpleDateFormat.format(reservation.getEndDate());
        datesLabel.setText(startStr + " - " + endStr);

        equipmentsComboBox.refresh();
        campComboBox.refresh();

        // Active / Desactive les contrôle en fonction de l'état de la réservation
        boolean isDeposited = reservation.getDepositDate() != null;
        boolean isPaid = reservation.getPaymentDate() != null;
        boolean canceled = reservation.getCanceled();
        startDatePicker.setDisable(isDeposited || isPaid || canceled);
        endDatePicker.setDisable(isDeposited || isPaid || canceled);
        campComboBox.setDisable(isDeposited || isPaid || canceled);
        serviceComboBox.setDisable(isDeposited || isPaid || canceled);
        personCountComboBox.setDisable(isDeposited || isPaid || canceled);
        equipmentsComboBox.setDisable(isDeposited || isPaid || canceled);
        cashBackComboBox.setDisable(isPaid || canceled);
        depositComboBox.setDisable(isPaid || canceled);
        paymentComboBox.setDisable(!isDeposited || canceled);
        sendBillBtn.setDisable(!isPaid || canceled);
        cancelBtn.setDisable(isPaid || canceled);
    }

    private void refreshDatabase() {
        try {
            Database.getInstance().getReservationDao().update(reservation);
            Router.showToast(ToastType.SUCCESS, "Réservation mise à jour");
        } catch (SQLException e) { // TODO gérer erreurs de conn en +
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
        container.setAlignment(Pos.BOTTOM_CENTER);

        var problemsContainer = new StackPane();

        var problemsList = new MFXListView<Problem>();
        problemsList.setDepthLevel(DepthLevel.LEVEL0);

        problemsList.getItems().addAll(reservation.getProblems());
        problemsList.setMaxWidth(500);
        problemsList.setPrefWidth(500);
        problemsList.setMinWidth(100);
        problemsList.setMaxHeight(140);

        noProblemContainer = new VBox(30);
        noProblemLabel = new Label("Aucun problème pour cette réservation");
        var addProblemBtn = new MFXButton("Ajouter un problème");
        noProblemContainer.getChildren().addAll(noProblemLabel, addProblemBtn);
        noProblemContainer.setAlignment(Pos.CENTER);
        addProblemBtn.getStyleClass().add("action-button-outlined");
        addProblemBtn.setOnAction(e -> {
            // TODO linking
            Router.showToast(ToastType.WARNING, "LINKING TO DO");
        });

        problemsContainer.getChildren().addAll(problemsList, noProblemContainer);

        container.getChildren().add(createPaymentContainer());
        container.getChildren().add(new HBoxSpacer());
        container.getChildren().add(problemsContainer);
        return container;
    }

    private HBox createPaymentContainer() {
        HBox container = new HBox(CONTENT_SPACING);

        VBox btnContainer = new VBox(CONTENT_SPACING);
        depositComboBox = createPriceComboBox("Acompte");
        depositComboBox.selectIndex(reservation.getDepositDate() == null ? 1 : 0);
        createDepositListener(depositComboBox);

        paymentComboBox = createPriceComboBox("Réglement complet");
        paymentComboBox.selectIndex(reservation.getPaymentDate() == null ? 1 : 0);
        createPayementListener(paymentComboBox);

        cashBackComboBox = createCashbackComboBox();
        createCashbackListener(cashBackComboBox);

        btnContainer.getChildren().addAll(depositComboBox, paymentComboBox, cashBackComboBox);

        VBox labelsContainer = new VBox(CONTENT_SPACING);
        depositPrice = new Label();
        depositPrice.setMinWidth(110);
        depositPrice.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);

        totalPrice = new Label();
        totalPrice.setMinWidth(110);
        totalPrice.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);


        labelsContainer.getChildren().addAll(depositPrice, totalPrice);

        container.getChildren().addAll(btnContainer, labelsContainer);
        return container;
    }

    /**
     * @return VBox contenant la carte du client et la carte de l'emplacement associés à cette
     *         réservation
     */
    private VBox cardsContainer() {
        VBox container = new VBox(CONTENT_SPACING);
        container.setPadding(new Insets(10, 0, 0, 0));
        container.setMinWidth(220);
        var clientCard = new ClientCard(reservation.getClient(), 220);
        campCard = new CampgroundCard(reservation.getCampground(), 220);
        container.getChildren().addAll(clientCard, campCard);
        return container;
    }

    /**
     * @return Conteneur contenant les ComboBox des dates de début de fin de la réservation
     */
    private VBox datesContainer() {
        VBox container = new VBox(CONTENT_SPACING);

        startDatePicker = new ReservationDatePicker(reservation, true);
        startDatePicker.addListener(changeListener);

        endDatePicker = new ReservationDatePicker(reservation, false);
        endDatePicker.addListener(changeListener);

        container.getChildren().addAll(startDatePicker, endDatePicker);

        try {
            // TODO ne pas fetch les emplacements à chaque fois
            campComboBox = new CampGroundComboBox(reservation);
            campComboBox.addListener(changeListener);
            container.getChildren().add(campComboBox);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return container;
    }


    /**
     * @return conteneur contenant les ComboBox de sélection de l'equipement / services / nb
     *         personnes
     */
    private VBox selectedEquipmentAndServicesContainer() {
        VBox container = new VBox(CONTENT_SPACING);

        personCountComboBox = new PersonCountComboBox(reservation);
        personCountComboBox.addListener(changeListener);

        serviceComboBox = new ServiceComboBox(reservation);
        serviceComboBox.addListener(changeListener);

        equipmentsComboBox = new EquipmentComboBox(reservation);
        equipmentsComboBox.addListener(changeListener);

        container.getChildren().addAll(personCountComboBox, serviceComboBox, equipmentsComboBox);
        return container;
    }

    /**
     * @return Header de la page (Numéro de réservations + Label avec dates)
     */
    private BorderPane createHeader() {
        BorderPane container = new BorderPane();

        title = new Label();
        title.setFont(new Font(24));
        title.setTextFill(Color.rgb(51, 59, 97));

        VBox datesInfosContainer = new VBox(5);
        datesLabel = new Label();
        datesLabel.setFont(new Font(17));
        datesLabel.setTextFill(Color.GREY);

        dayCount = new Label();
        dayCount.setFont(new Font(13));
        dayCount.setTextFill(Color.DARKGREY);

        datesInfosContainer.getChildren().addAll(datesLabel, dayCount);

        container.setLeft(title);
        container.setRight(datesInfosContainer);
        return container;
    }

    /**
     * @return Boutons d'actions en bas de la page
     */
    private HBox createActionsButtonsSlot() {
        var container = new HBox(10);

        sendBillBtn = new MFXButton("Envoyer facture");
        cancelBtn = new ConfirmButton("Annuler la réservation");
        FontIcon icon = new FontIcon("fas-exclamation-triangle:10");
        icon.setIconColor(Color.WHITE);
        cancelBtn.setGraphic(icon);
        cancelBtn.setOnConfirmedAction(e -> {
            reservation.setCanceled(true);
            refreshPage();
            refreshDatabase();
        });

        sendBillBtn.getStyleClass().add("action-button");
        cancelBtn.getStyleClass().add("action-button");

        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(sendBillBtn, cancelBtn);
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
    private MFXComboBox<CashBack> createCashbackComboBox() {
        var combo = new MFXComboBox<CashBack>();
        combo.setFloatingText("Remise");
        combo.setFloatMode(FloatMode.INLINE);
        combo.getItems().addAll(CashBack.values());
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

    /**
     * Listener de la ComboBox de paiement
     * 
     * Lance la mise à jour de l'interface et de la BD si la valeur change
     * 
     * @param comboBox
     */
    private void createCashbackListener(MFXComboBox<CashBack> comboBox) {
        comboBox.valueProperty().addListener((obs, oldPrice, newPrice) -> {
            if (oldPrice == null)
                return;
            reservation.setCashBack(newPrice);
            refreshPage();
            refreshDatabase();
        });
    }

    @Override
    protected List<Reservation> queryAll() throws SQLException {
        return Database.getInstance().getReservationDao().queryForAll();
    }
}
