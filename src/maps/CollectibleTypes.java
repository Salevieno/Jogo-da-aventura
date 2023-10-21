package maps;

import items.Item;

public enum CollectibleTypes
{
	berry (4000),
	herb (128000),
	wood (153600),
	metal (256000);
	
	int spawnTime ;
	
	private CollectibleTypes(int spawnTime)
	{
		this.spawnTime = spawnTime ;
	}
	
	public int getSpawnTime() { return spawnTime ;}
	
	public Item getItem(FieldMap map)
	{
		return new Collectible(220, map.getLevel(), map.randomPosInMap(), spawnTime) ;
	}
}
