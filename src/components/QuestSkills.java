package components;

public enum QuestSkills
{
	forestMap,
	caveMap,
	islandMap,
	volcanoMap,
	snowlandMap,
	dig,
	craftWindow,
	ride,
	dragonAura,
	bestiary;
	
	public static QuestSkills getContinentMap(String continent)
	{
		return switch (continent)
		{
			case "forest" -> forestMap ;
			case "cave" -> caveMap ;
			case "island" -> islandMap ;
			case "volcano" -> volcanoMap ;
			case "snowland" -> snowlandMap ;
			case "special" -> null ;
			default -> null ;
		};
	}
}
