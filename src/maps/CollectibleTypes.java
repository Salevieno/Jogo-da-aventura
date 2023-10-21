package maps;

import items.Item;

public enum CollectibleTypes
{
	berry (400),
	herb (156),
	wood (250),
	metal (400);
	
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
