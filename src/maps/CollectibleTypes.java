package maps;

import java.awt.Image;

import main.Game;
import main.Path;

public enum CollectibleTypes
{
	berry (100, Game.loadImage(Path.COLLECTABLES_IMG + "Col0_Berry.png")),
	herb (1280, Game.loadImage(Path.COLLECTABLES_IMG + "Col1_Herb.png")),
	wood (1530, Game.loadImage(Path.COLLECTABLES_IMG + "Col2_Wood.png")),
	metal (2560, Game.loadImage(Path.COLLECTABLES_IMG + "Col3_Metal.png"));
	
	double spawnTime ;
	Image image ;
	
	private CollectibleTypes(double spawnTime, Image image)
	{
		this.spawnTime = spawnTime ;
		this.image = image ;
	}
	
	public double getSpawnTime() { return spawnTime ;}
	public Image getImage() { return image ;}
	
//	public Item getItem(FieldMap map)
//	{
//		return new Collectible(220, map.getLevel(), map.randomPosInMap()) ;
//	}
	
}
