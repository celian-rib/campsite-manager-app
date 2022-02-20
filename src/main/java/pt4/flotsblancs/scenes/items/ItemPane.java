package pt4.flotsblancs.scenes.items;

import javafx.scene.layout.BorderPane;

public class ItemPane<T extends Item> extends BorderPane
{
	private Item item;
	
	public ItemPane(Item item)
	{
		super();
		this.item = item;
	}
	
	public Item getItem()
	{
		return this.item;
	}
	
}
