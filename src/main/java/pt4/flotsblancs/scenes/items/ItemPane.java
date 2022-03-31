package pt4.flotsblancs.scenes.items;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

class ItemPane<I extends Item> extends BorderPane {
    private I item;
    private Circle statusDot;
    private HBox leftContainer;
    private Paint color;

    ItemPane(I item, int maxWith) {
        this.item = item;
        setPadding(new Insets(0));
        setPrefHeight(30);

        leftContainer = new HBox(8);
        leftContainer.setAlignment(Pos.CENTER);

        statusDot = new Circle(4);
        statusDot.setFill(Color.web("#bcc0d1", 0.5));
        
        Text display = new Text(item.getDisplayName());
        display.setFill(Color.rgb(50, 60, 100));
        display.setStyle("-fx-font-weight: bold");

        leftContainer.getChildren().addAll(statusDot, display);
        
        Text id = new Text("#" + item.getId());
        id.setFill(Color.rgb(50, 50, 80));
        id.setStyle("-fx-font-weight: bold");

        setLeft(leftContainer);
        setRight(id);
        setAlignment(id, Pos.CENTER);
        setAlignment(leftContainer, Pos.CENTER);
        setMaxWidth(maxWith);
    }

    /**
     * Met à jour la couleur de l'item pane
     */
    void updateColor() {
        color = item.getStatusColor();
    }

    /**
     * Rend visible le status dot
     */
    void showDots() {
        statusDot.setFill(color);
    }

    /**
     * @return L'item actuellement utilisé par l'item pane
     */
    I getItem() {
        return this.item;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject == null) return false;
        if (!(anObject instanceof ItemPane<?>)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        var other = (ItemPane<I>)anObject;
        if (this == other) {
            return true;
        }
        var i1 = this.getItem();
        var i2 = other.getItem();
        return i1.getClass().equals(i2.getClass()) && i1.getId() == i2.getId();
    }
}
