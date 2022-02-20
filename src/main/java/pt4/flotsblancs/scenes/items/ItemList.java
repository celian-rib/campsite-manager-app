package pt4.flotsblancs.scenes.items;


import java.util.ArrayList;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ItemList<T extends Item> extends BorderPane
{
	private List<T> items;
	private ItemContainer<T> itemContainer;
	
	public ItemList(List<T> arrayList, ItemContainer<T> itemContainer)
	{
		this.items = arrayList;
		this.itemContainer = itemContainer;
		initItems();	
	}
	
	public void applyDefault(BorderPane button,T i)
	{
		button.setPadding(new Insets(6,0,0,0)); //On aligne verticalement car c'est moche sinon
		button.setPrefHeight(30);
		
		Text display = new Text(i.getAdaptedDisplayName());
		display.setFill(Color.rgb(50, 60, 100));
		display.setStyle("-fx-font-weight: bold");
		
		Text id = new Text("#"+i.getID());
		id.setFill(Color.rgb(50, 50, 80));
		id.setStyle("-fx-font-weight: bold");
		
		
		button.setLeft(display);
		button.setRight(id);
	}
	
	public void initItems()
	{
		//TODO: a modifier
		TextField searchBar = new TextField("TEST");
		ScrollPane scrollPane = new ScrollPane();
		BorderPane container = new BorderPane();

	    scrollPane.setFitToHeight(true);
	    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    

		ArrayList<ItemPane<T>> listTitle = new ArrayList<ItemPane<T>>();
		
		for(T i : items)
		{
			ItemPane<T> button = new ItemPane<T>(i);
			applyDefault(button,i);
			listTitle.add(button);
		}
		
		System.out.println("------------"+listTitle.get(0).getItem());
		
	    ListView<ItemPane<T>> listView = new ListView<ItemPane<T>>();
	    ObservableList<ItemPane<T>> itemsView = FXCollections.observableArrayList(listTitle);
	    
	    listView.setFocusTraversable(false);
	    listView.setStyle("-fx-background-insets: 0;-fx-background-insets: 0;    -fx-background-insets: 0;   -fx-padding: 0;");
	    
	    listView.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        @SuppressWarnings("unchecked")
				T i = (T) listView.getSelectionModel().getSelectedItem().getItem();
		        itemContainer.changeItem(i);
		    }
		});
    
	    listView.setItems(itemsView);
	    scrollPane.setContent(listView);
	    
	    container.setTop(searchBar);
		container.setCenter(scrollPane);
		
		this.setLeft(container);
	}
	
	
	public void update()
	{
		
	}
}
