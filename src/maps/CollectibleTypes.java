package maps;

import java.awt.Image;

import items.Item;
import utilities.UtilS;

public enum CollectibleTypes
{
	berry (100, UtilS.loadImage("\\Collect\\" + "Col0_Berry.png")),
	herb (1280, UtilS.loadImage("\\Collect\\" + "Col1_Herb.png")),
	wood (1530, UtilS.loadImage("\\Collect\\" + "Col2_Wood.png")),
	metal (2560, UtilS.loadImage("\\Collect\\" + "Col3_Metal.png"));
	
	int spawnTime ;
	Image image ;
	
	private CollectibleTypes(int spawnTime, Image image)
	{
		this.spawnTime = spawnTime ;
		this.image = image ;
	}
	
	public int getSpawnTime() { return spawnTime ;}
	public Image getImage() { return image ;}
	
	public Item getItem(FieldMap map)
	{
		return new Collectible(220, map.getLevel(), map.randomPosInMap(), spawnTime) ;
	}
	
}
