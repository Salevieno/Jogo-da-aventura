package maps;

import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;

public class Collectible
{
	private int type ;
	private String name ;
	private int level ;
	private Point pos ;
	private int counter ;
	private int delay ;
	private Image image ;

	public static final String[] CollectibleNames = new String[] {"Berry", "Herb", "Wood", "Metal"} ;
	public static Image[] collectibleImages ;
	
	public Collectible(int type, int level, Point pos, int counter, int delay)
	{
		this.type = type ;
		this.level = level ;
		this.pos = pos ;
		this.counter = counter ;
		this.delay = delay ;		

		name = CollectibleNames[type] ;
		
		String path = Game.ImagesPath  + "\\Collect\\";
	    Image BerryImage = new ImageIcon(path + "Col0_" + name + ".png").getImage() ;
	    Image HerbImage = new ImageIcon(path + "Col1_" + name + ".png").getImage() ;
	    Image WoodImage = new ImageIcon(path + "Col2_" + name + ".png").getImage() ;
	    Image MetalImage = new ImageIcon(path + "Col3_" + name + ".png").getImage() ;
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
	public boolean collectingIsOver() {return (counter == 0) ;}
	
	public void display(DrawingOnPanel DP) {DP.DrawImage(image, pos, Align.center) ;}
}