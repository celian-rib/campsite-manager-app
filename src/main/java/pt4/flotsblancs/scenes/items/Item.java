package pt4.flotsblancs.scenes.items;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Item
{
	public String getDisplayName();
	
	public String getID();
	
	public default String getAdaptedDisplayName()
	{
		if(getDisplayName().length()>30)
			return getDisplayName().substring(0,30)+"[...]";
		return getDisplayName();
	}
	
	public static ArrayList<Item> getItems() throws SQLException {
		return null;
	}
}
