package screen ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;

import javax.swing.ImageIcon;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.AlignmentPoints;
import utilities.TimeCounter;

public class Sky 
{
	public TimeCounter dayTime ;
	public int height ;
	private SkyComponent[] Cloud;
	private SkyComponent[] Star ;
	
	public Sky ()
	{
		dayTime = new TimeCounter(Game.DayDuration / 2, Game.DayDuration) ;
		height = (int)(0.2 * Game.getScreen().getSize().height) ;
    	
    	// initialize clouds
    	Cloud = new SkyComponent[5] ;
		for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
		{
			Image CloudImage = new ImageIcon(Game.ImagesPath + "Cloud" + String.valueOf(1 + (int) (3 * Math.random())) + ".png").getImage() ;
			Point InitialCloudPos = new Point((int)(Math.random() * Game.getScreen().getSize().width), 2 + (int) ((height - CloudImage.getHeight(null)) * Math.random())) ;
			int[] CloudSpeed = new int[] {(int) (1 + 2 * Math.random()), 0} ;
	    	Cloud[c] = new SkyComponent(CloudImage, InitialCloudPos, CloudSpeed, new Color[] {Game.ColorPalette[4]}) ;
		}
		
		// initialize stars
    	Star = new SkyComponent[50] ;
		Image StarImage = new ImageIcon(Game.ImagesPath + "Star.png").getImage() ;
		for (int s = 0 ; s <= Star.length - 1 ; s += 1)
		{
			Point StarPos = new Point((int)(Math.random() * Game.getScreen().getSize().width), (int)(Math.random() * height)) ;
			Color[] StarColor = new Color[] {Game.ColorPalette[(int)((Game.ColorPalette.length - 1)*Math.random())]} ;
	    	Star[s] = new SkyComponent(StarImage, StarPos, new int[2], StarColor) ;
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
		Color skyColor = new Color(Game.ColorPalette[0].getRed(), (int)(Game.ColorPalette[0].getGreen() * ColorMult), (int)(Game.ColorPalette[0].getBlue() * ColorMult)) ;
		
		DP.DrawRect(new Point(0, height), AlignmentPoints.bottomLeft, new Dimension(Game.getScreen().getSize().width, height), 1, skyColor, null) ;
		
		// if it is daylight
		if (Game.DayDuration / 4 <= dayTime.getCounter() & dayTime.getCounter() <= 3 * Game.DayDuration / 4)
		{
			// move and display clouds
			for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
			{
				Point newPos = new Point((int) (Cloud[c].getPos().x + Cloud[c].getSpeed()[0]), (int) Cloud[c].getPos().y) ;
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
