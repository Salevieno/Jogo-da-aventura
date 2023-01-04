package components;

public enum QuestSkills
{
	forestMap,
	caveMap,
	islandMap,
	volcanoMap,
	snowlandMap,
	shovel,
	craftWindow,
	ride,
	dragonAura,
	bestiary;
	
	public static QuestSkills getContinentMap(String continent)
	{
		switch (continent)
		{
			case "Floresta":
			{
				return forestMap ;
			}
			case "Caverna":
			{
				return caveMap ;
			}
			case "Ilha":
			{
				return islandMap ;
			}
			case "Vulcão":
			{
				return volcanoMap ;
			}
			case "Terra das neves":
			{
				return snowlandMap ;
			}
			default: 
				return null ;
		}
	}
}
