package pt4.flotsblancs.scenes.components;

import java.util.LinkedHashMap;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt4.flotsblancs.database.model.User;
import pt4.flotsblancs.router.Router;
import pt4.flotsblancs.router.Router.Routes;
import pt4.flotsblancs.scenes.breakpoints.BreakPointListener;
import pt4.flotsblancs.scenes.breakpoints.BreakPointManager;
import pt4.flotsblancs.scenes.breakpoints.HBreakPoint;
import pt4.flotsblancs.scenes.utils.ToastType;

public class NavBar extends BorderPane implements BreakPointListener {

    private Label userLabel;
    private FlotsBlancsLogo largeLogo;
    private FlotsBlancsLogo smallLogo;
    private MFXButton logoutBtn;

    /**
     * Map avec les boutons de navigation associées à leur route
     */
    private LinkedHashMap<Router.Routes, MFXButton> navButtons;

    public NavBar() {
        // link au css de la navbar
        setId("nav-bar");

        largeLogo = new FlotsBlancsLogo(false, true, 50);
        smallLogo = new FlotsBlancsLogo(false, false, 30);

        setCenter(navigationButtons());
        setBottom(logOutButton());
        BreakPointManager.addListener(this);
    }

    private void addNavButton(Routes route, String icon, int gap) {
        MFXButton btn = new MFXButton(Router.routes.get(route).getName(), 200, 40);
        btn.setButtonType(ButtonType.FLAT);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(20, 10, 20, 30));
        btn.setOnAction(event -> Router.goToScreen(route));
        FontIcon iconNode = new FontIcon(icon);
        iconNode.setIconColor(Color.WHITE);
        btn.setGraphic(iconNode);
        btn.setGraphicTextGap(gap);
        navButtons.put(route, btn);
    }

    private VBox navigationButtons() {
        VBox centerButtons = new VBox();
        centerButtons.setAlignment(Pos.CENTER);

        navButtons = new LinkedHashMap<Routes, MFXButton>();

        addNavButton(Routes.HOME, "fas-home:18", 15);
        addNavButton(Routes.CLIENTS, "far-user:19", 15);
        addNavButton(Routes.RESERVATIONS, "far-calendar-alt:19", 15);
        addNavButton(Routes.STOCKS, "fas-box:16", 15);
        addNavButton(Routes.CAMPGROUNDS, "fas-caravan:16", 11);
        addNavButton(Routes.PROBLEMS, "fas-exclamation-triangle:16", 11);
        addNavButton(Routes.ADMIN, "fas-user-cog:16", 11);
        addNavButton(Routes.LOGS, "far-list-alt", 15);

        centerButtons.getChildren().addAll(navButtons.values());

        return centerButtons;
    }

    private VBox logOutButton() {
        VBox bottomButtons = new VBox();
        bottomButtons.setAlignment(Pos.CENTER);

        userLabel = new Label();
        userLabel.setTextFill(Color.WHITE);

        logoutBtn = new MFXButton("Déconnexion", 200, 60);
        logoutBtn.setOnAction(event -> {
            User.logOut();
            Router.showToast(ToastType.INFO, "Deconnecté.e");
            Router.goToScreen(Routes.LOGIN);
        });
        logoutBtn.setId("btn-logout");

        bottomButtons.getChildren().addAll(userLabel, logoutBtn);
        return bottomButtons;
    }

    /**
     * Met à jour l'affichage de la barre de navigation
     * 
     * - Rafraichit l'affichage de l'utilisateur actuellement connecté
     * 
     * - Met à jour le boutons sélectionné dans la navbar (En fonction de la route actuellement
     * chargée)
     */
    public void update() {
        userLabel.setText(User.isConnected() ? User.getConnected().toString() : "");

        navButtons.forEach((route, button) -> {
            if (Router.getCurrentRoute() == route)
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.178);");
            else
                button.setStyle("-fx-background-color: transparent;");
        });
    }

    @Override
    public void onHorizontalBreak(HBreakPoint oldBp, HBreakPoint newBp) {
        if (newBp.getWidth() <= HBreakPoint.MEDIUM.getWidth()) {
            navButtons.forEach((r, btn) -> {
                setTop(smallLogo);
                btn.setText("         ");
                btn.setTextOverrun(OverrunStyle.CLIP);
                btn.setMaxWidth(50);
                logoutBtn.setMaxWidth(50);
            });
        } else {
            navButtons.forEach((r, btn) -> {
                setTop(largeLogo);
                btn.setText(Router.routes.get(r).getName());
                btn.setMaxWidth(200);
                logoutBtn.setMaxWidth(200);
            });
        }
    }
}
