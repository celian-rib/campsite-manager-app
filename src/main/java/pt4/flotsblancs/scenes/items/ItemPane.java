package pt4.flotsblancs.scenes.items;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

class ItemPane<I extends Item> extends BorderPane {
    private I item;

    public ItemPane(I item, int maxWith) {
        this.item = item;
        // On aligne verticalement car c'est moche sinon
        setPadding(new Insets(0));
        setPrefHeight(30);

        HBox leftContainer = new HBox(8);
        leftContainer.setAlignment(Pos.CENTER);

        Circle statusDot = new Circle(4);
        statusDot.setFill(item.getStatusColor());
        
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

    public I getItem() {
        return this.item;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        return anObject instanceof Item && this.getItem().equals(anObject);
    }
}
