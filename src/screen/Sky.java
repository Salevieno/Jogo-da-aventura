package screen ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.TimeCounter;
import utilities.UtilG;

public class Sky 
{
	public TimeCounter dayTime ;
//	public int height ;
	private SkyComponent[] Cloud;
	private SkyComponent[] Star ;
	
	public final int height = (int)(0.2 * Game.getScreen().getSize().height) ;
	
	public Sky ()
	{
		dayTime = new TimeCounter(Game.DayDuration / 2, Game.DayDuration) ;
		
		String path = Game.ImagesPath  + "\\Sky\\";
    	
    	// initialize clouds
    	Cloud = new SkyComponent[5] ;
		for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
		{
			Image CloudImage = UtilG.loadImage(path + "Cloud" + String.valueOf(1 + (int) (3 * Math.random())) + ".png") ;
			Point InitialCloudPos = new Point((int)(Math.random() * Game.getScreen().getSize().width), 2 + (int) ((height - CloudImage.getHeight(null)) * Math.random())) ;
			Point CloudSpeed = new Point((int) (1 + 2 * Math.random()), 0) ;
	    	Cloud[c] = new SkyComponent(CloudImage, InitialCloudPos, CloudSpeed, new Color[] {Game.colorPalette[4]}) ;
		}
		
		// initialize stars
    	Star = new SkyComponent[50] ;
		Image StarImage = UtilG.loadImage(path + "Star.png") ;
		for (int s = 0 ; s <= Star.length - 1 ; s += 1)
		{
			Point StarPos = new Point((int)(Math.random() * Game.getScreen().getSize().width), (int)(Math.random() * height)) ;
			Color[] StarColor = new Color[] {Game.colorPalette[(int)((Game.colorPalette.length - 1)*Math.random())]} ;
	    	Star[s] = new SkyComponent(StarImage, StarPos, new Point(0, 0), StarColor) ;
		}	
	}

	public void DrawStar(Point Pos, double size, Color color, DrawingOnPanel DP)
	{
		int NumberOfPoints = 8 ;
		int[][] Coords = new int[NumberOfPoints][2] ;
		double t = size/10 ;
		Coords[0] = new int[] {0, (int)(t), (int)(size/2), (int)(t), 0, (int)(-t), (int)(-size/2), (int)(-t)} ;
		Coords[1] = new int[] {(int)(-size/2), (int)(-t), 0, (int)(t), (int)(size/2), (int)(t), 0, (int)(-t)} ;
		for (int i = 0 ; i <= NumberOfPoints - 1 ; ++i)
		{
			Coords[0][i] += Pos.x ;
			Coords[1][i] += Pos.y ;
		}
		DP.DrawPolygon(Coords[0], Coords[1], DrawingOnPanel.stdStroke, color) ;
	}
	
	public void display(DrawingOnPanel DP)
	{
		double ColorMult = (1 - 1.8 * Math.abs(dayTime.getCounter() - Game.DayDuration / 2) / Game.DayDuration) ;
		int red = Math.max(0, Math.min(Game.colorPalette[21].getRed(), 255)) ;
		int green = Math.max(0, Math.min((int)(Game.colorPalette[21].getGreen() * ColorMult), 255)) ;
		int blue = Math.max(0, Math.min((int)(Game.colorPalette[21].getBlue() * ColorMult), 255)) ;
		Color skyColor = new Color(red, green, blue) ;
		
		DP.DrawRect(new Point(0, height), Align.bottomLeft, new Dimension(Game.getScreen().getSize().width, height), 1, skyColor, null) ;
		
		// if it is daylight
		if (Game.DayDuration / 4 <= dayTime.getCounter() & dayTime.getCounter() <= 3 * Game.DayDuration / 4)
		{
			// move and display clouds
			for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
			{
				Point newPos = new Point((int) (Cloud[c].getPos().x + Cloud[c].getSpeed().x), (int) Cloud[c].getPos().y + Cloud[c].getSpeed().y) ;
				Cloud[c].setPos(newPos) ;

				// if the cloud has passed the screen, reset its position
				if (Game.getScreen().getSize().width <= Cloud[c].getPos().x)
				{
					Point originPos = new Point(-Cloud[c].getImage().getWidth(null), Cloud[c].getPos().y) ;
					Cloud[c].setPos(originPos) ;
					Cloud[c].setCounter(0) ;
				}
				Cloud[c].display(DrawingOnPanel.stdAngle, DP) ;
			}
		}
		else
		{
			// if it is night, draw stars
			for (int s = 0 ; s <= 50 - 1 ; s += 1)
			{
				DrawStar(Star[s].getPos(), 10, Star[s].getColor()[0], DP) ;
			}
		}
	}

}
