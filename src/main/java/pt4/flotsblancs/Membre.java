package pt4.flotsblancs;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "membre")
public class Membre
{
	@DatabaseField(generatedId = true)
	private long id;
	
    @DatabaseField(canBeNull = false)
    private String name;
    
    public Membre()
    {
    	
    }
}
