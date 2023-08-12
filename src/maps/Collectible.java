package maps;

import java.awt.Point;

import graphics.DrawingOnPanel;
import items.Item;
import main.Game;
import utilities.Align;
import utilities.TimeCounter;

public class Collectible extends Item
{
//	private String name ;
	private int itemID ;
	private int level ;
	private Point pos ;
	private TimeCounter counter ;
//	private Image image ;

//	public static final String[] CollectibleNames = new String[] {"Berry", "Herb", "Wood", "Metal"} ;
//	public static Image BerryImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col0_Berry" + ".png") ;
//	public static Image HerbImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col1_Herb" + ".png") ;
//	public static Image WoodImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col2_Wood" + ".png") ;
//	public static Image MetalImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col3_Metal" + ".png") ;
//	public static Image[] collectibleImages ;
	
	public Collectible(int itemID, int level, Point pos, int delay)
	{
		super(itemID, Game.getAllItems()[itemID].getName(), Game.getAllItems()[itemID].getDescription(), Game.getAllItems()[itemID].getImage(), Game.getAllItems()[itemID].getPrice(), Game.getAllItems()[itemID].getDropChance()) ;
//		name = CollectibleNames[type] ;
		this.itemID = itemID ;
		this.level = level ;
		this.pos = pos ;
		
		counter = new TimeCounter(0, delay) ;
		
//		String path = Game.ImagesPath  + "\\Collect\\";
//	    Image BerryImage = UtilG.loadImage(path + "Col0_" + name + ".png") ;
//	    Image HerbImage = UtilG.loadImage(path + "Col1_" + name + ".png") ;
//	    Image WoodImage = UtilG.loadImage(path + "Col2_" + name + ".png") ;
//	    Image MetalImage = UtilG.loadImage(path + "Col3_" + name + ".png") ;
//		collectibleImages = new Image[] {BerryImage, HerbImage, WoodImage, MetalImage};
		
//		image = collectibleImages[type] ;
	}

	public int getLevel() { return level ; }
	public Point getPos() { return pos ; }
	public TimeCounter getCounter() { return counter ; }
	public Item getItem() {return Game.getAllItems()[itemID] ;}
	
	public int type()
	{
		if (itemID == 220) { return 0 ;}
		
		return itemID % 3 + 1 ;
	}
	
	public void display(DrawingOnPanel DP) {DP.DrawImage(image, pos, Align.center) ;}

	@Override
	public String toString()
	{
//		return "Collectible [level=" + level + ", pos=" + pos + ", counter=" + counter + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "Collectible," + getItem().getName() ;
	}
	
}
