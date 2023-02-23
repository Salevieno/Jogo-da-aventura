package maps;

import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.TimeCounter;
import utilities.UtilG;

public class Collectible
{
	private int type ;
	private String name ;
	private int level ;
	private Point pos ;
	private TimeCounter counter ;
	private Image image ;

	public static final String[] CollectibleNames = new String[] {"Berry", "Herb", "Wood", "Metal"} ;
	public static Image BerryImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col0_Berry" + ".png") ;
	public static Image HerbImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col1_Herb" + ".png") ;
	public static Image WoodImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col2_Wood" + ".png") ;
	public static Image MetalImage = UtilG.loadImage(Game.ImagesPath  + "\\Collect\\" + "Col3_Metal" + ".png") ;
	public static Image[] collectibleImages ;
	
	public Collectible(int type, int level, Point pos, int delay)
	{
		this.type = type ;
		name = CollectibleNames[type] ;
		this.level = level ;
		this.pos = pos ;
		
		counter = new TimeCounter(0, delay) ;
		
//		String path = Game.ImagesPath  + "\\Collect\\";
//	    Image BerryImage = UtilG.loadImage(path + "Col0_" + name + ".png") ;
//	    Image HerbImage = UtilG.loadImage(path + "Col1_" + name + ".png") ;
//	    Image WoodImage = UtilG.loadImage(path + "Col2_" + name + ".png") ;
//	    Image MetalImage = UtilG.loadImage(path + "Col3_" + name + ".png") ;
		collectibleImages = new Image[] {BerryImage, HerbImage, WoodImage, MetalImage};
		
		image = collectibleImages[type] ;
	}

	public int getType() { return type ; }
	public int getLevel() { return level ; }
	public Point getPos() { return pos ; }
	public TimeCounter getCounter() { return counter ; }	
	
	public void display(DrawingOnPanel DP) {DP.DrawImage(image, pos, Align.center) ;}
	
}
