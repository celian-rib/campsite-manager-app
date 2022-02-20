package pt4.flotsblancs.scenes.items;

public interface ItemContainer<T extends Item>
{	
	public void changeItem(T newItem);
}
