package components;

public enum SpellTypes
{
	passive ("passive"),
	offensive ("offensive"),
	support ("support");
	
	private SpellTypes(String name)
	{
		
	}
	
	public static SpellTypes getByName(String name)
	{
		// TODO forma melhor de pegar Enum pelo nome
		switch (name)
		{
			case "passive": return passive ;
			case "offensive": return offensive ;
			case "support": return support ;
			default: return null ;
		}
	}
}
