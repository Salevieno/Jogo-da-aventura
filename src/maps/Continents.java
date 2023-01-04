package maps;

public enum Continents
{
	forest ("forest"),
	cave ("cave"),
	island ("island"),
	volcano ("volcano"),
	snowland ("snowland"),
	special ("special");
	
	private Continents(String name)
	{
		
	}
	
	public static Continents[] getAll()
	{
		return new Continents[] {forest, cave, island, volcano, snowland, special} ;
	}
}
