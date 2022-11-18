package Maps;

import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import Graphics.DrawPrimitives;
import Main.Game;

public class Collectible
{
	private int type ;
	private String name ;
	private int level ;
	private Point pos ;
	private int counter ;
	private int delay ;
	private Image image ;

	public static String[] CollectibleNames ;
	public static Image[] collectibleImages ;
	
	public Collectible(int type, int level, Point pos, int counter, int delay)
	{
		this.type = type ;
		this.level = level ;
		this.pos = pos ;
		this.counter = counter ;
		this.delay = delay ;
		
		CollectibleNames = new String[] {"Berry", "Herb", "Wood", "Metal"} ;

		name = CollectibleNames[type] ;
		
	    Image BerryImage = new ImageIcon(Game.ImagesPath + "Col0_Berry.png").getImage() ;
	    Image HerbImage = new ImageIcon(Game.ImagesPath + "Col1_Herb.png").getImage() ;
	    Image WoodImage = new ImageIcon(Game.ImagesPath + "Col2_Wood.png").getImage() ;
	    Image MetalImage = new ImageIcon(Game.ImagesPath + "Col3_Metal.png").getImage() ;
		collectibleImages = new Image[] {BerryImage, HerbImage, WoodImage, MetalImage};
		
		image = collectibleImages[type] ;
	}

	public int getType() { return type ; }
	public int getLevel() { return level ; }
	public Point getPos() { return pos ; }
	public int getCounter() { return counter ; }
	public int getDelay() { return delay ; }
	
	public void incCounter() { counter = (counter + 1) % delay ;}
	public void resetCounter() { counter = 0 ;}
	
	public void display(DrawPrimitives DP) {DP.DrawImage(image, pos, "Center") ;}
}
