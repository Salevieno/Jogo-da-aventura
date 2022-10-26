package SkyComponents ;

import java.awt.Color ;
import java.awt.Image ;
import java.awt.Point;

import javax.swing.ImageIcon;

import GameComponents.Screen;
import GameComponents.Size;
import Graphics.DrawPrimitives ;
import Main.Game;

public class Sky 
{
	public int dayTime ;
	public int height ;
	private SkyComponent[] Cloud;
	private SkyComponent[] Star ;
	
	public Sky ()
	{
		dayTime = Game.DayDuration / 2 ;
		height = (int)(0.2 * Game.getScreen().getSize().x) ;
    	Cloud = new SkyComponent[5] ;
    	Star = new SkyComponent[50] ;
    	Screen screen = Game.getScreen() ;
		for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
		{
			Image CloudImage = new ImageIcon(Game.ImagesPath + "Cloud" + String.valueOf(1 + (int) (3 * Math.random())) + ".png").getImage() ;
			Point InitialCloudPos = new Point((int)(Math.random() * screen.getSize().x), 2 + (int) ((height - CloudImage.getHeight(null)) * Math.random())) ;
			float[] CloudSpeed = new float[] {(float)(1 + 1.5 * Math.random()), 0} ;
	    	Cloud[c] = new SkyComponent(CloudImage, InitialCloudPos, CloudSpeed, new Color[] {Game.ColorPalette[4]}) ;
		}
		Image StarImage = new ImageIcon(Game.ImagesPath + "Star.png").getImage() ;
		for (int s = 0 ; s <= Star.length - 1 ; s += 1)
		{
			Point StarPos = new Point((int)(Math.random() * screen.getSize().x), (int)(Math.random() * height)) ;
			Color[] StarColor = new Color[] {Game.ColorPalette[(int)((Game.ColorPalette.length - 1)*Math.random())]} ;
	    	Star[s] = new SkyComponent(StarImage, StarPos, new float[2], StarColor) ;
		}	
	}

	public void incDayTime()
	{
		dayTime += 1 ;
		if (dayTime % Game.DayDuration == 0)
		{
			dayTime = 0 ;
		}
	}
	public void DrawStar(Point Pos, float size, Color color, DrawPrimitives DP)
	{
		int NumberOfPoints = 8 ;
		int[][] Coords = new int[NumberOfPoints][2] ;
		float t = size/10 ;
		Coords[0] = new int[] {0, (int)(t), (int)(size/2), (int)(t), 0, (int)(-t), (int)(-size/2), (int)(-t)} ;
		Coords[1] = new int[] {(int)(-size/2), (int)(-t), 0, (int)(t), (int)(size/2), (int)(t), 0, (int)(-t)} ;
		for (int i = 0 ; i <= NumberOfPoints - 1 ; ++i)
		{
			Coords[0][i] += Pos.x ;
			Coords[1][i] += Pos.y ;
		}
		DP.DrawPolygon(Coords[0], Coords[1], DrawPrimitives.StdThickness, color) ;
	}
	
	public void display(DrawPrimitives DP)
	{
		int DayDuration = Game.DayDuration ;
    	Screen screen = Game.getScreen() ;
		float ColorMult = (1 - (float)(1.8 * Math.abs(dayTime - DayDuration / 2)) / DayDuration) ;		
		DP.DrawRect(new Point(0, height), "BotLeft", new Size(screen.getSize().x, height), 1,
				new Color(Game.ColorPalette[0].getRed(), (int)(Game.ColorPalette[0].getGreen()*ColorMult), (int)(Game.ColorPalette[0].getBlue()*ColorMult)), Game.ColorPalette[9], false) ;
		
		if (DayDuration / 4 <= dayTime & dayTime <= 3 * DayDuration / 4)
		{
			for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
			{
				Cloud[c].setPos(new Point((int) (Cloud[c].getPos().x + Cloud[c].getSpeed()[0]), (int) Cloud[c].getPos().y)) ;
				if (screen.getSize().x <= Cloud[c].getPos().x)
				{
					Cloud[c].getPos().x = -Cloud[c].getImage().getWidth(null) ;
					Cloud[c].setCounter(0) ;
					Cloud[c].setPos(new Point((int) (Cloud[c].getPos().x + Cloud[c].getSpeed()[0]), (int) Cloud[c].getPos().y)) ;
				}
				Cloud[c].display(DrawPrimitives.OverallAngle, DP) ;
			}
		}
		else
		{
			for (int s = 0 ; s <= 50 - 1 ; s += 1)
			{
				DrawStar(Star[s].getPos(), 10, Star[s].getColor()[0], DP) ;
			}
		}
	}

}
