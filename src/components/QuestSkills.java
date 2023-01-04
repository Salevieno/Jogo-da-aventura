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
			case "forest":
			{
				return forestMap ;
			}
			case "cave":
			{
				return caveMap ;
			}
			case "island":
			{
				return islandMap ;
			}
			case "volcano":
			{
				return volcanoMap ;
			}
			case "snowland":
			{
				return snowlandMap ;
			}
			case "special":
			{
				return null ;
			}
			default: 
				return null ;
		}
	}
}
