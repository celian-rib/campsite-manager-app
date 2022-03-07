package pt4.flotsblancs.database.model.types;

public enum Problems 
{
	OPEN_URGENT("Urgent"),
	OPEN("En cours"),
	SOLVED("Résolu");
	
	public String displayName;
	
	Problems(String displayName)
	{
		this.displayName = displayName;
	}
	
	public static Problems getFromString(String name)
	{
		switch(name)
		{
			case "SOLVED":
				return SOLVED;
			case "OPEN_URGENT":
				return OPEN_URGENT;
			default:
				return OPEN;
		}
	}
}
