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
	public static TimeCounter dayTime ;
	private static SkyComponent[] clouds;
	private static SkyComponent[] stars ;
	private static boolean isDay ;
	private static Color color ;

	public static final int height = (int)(0.2 * Game.getScreen().getSize().height) ;
	public static final Dimension size = new Dimension(Game.getScreen().getSize().width, height) ;
	public static final Image cloudImage1 = UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Cloud1.png") ;
	public static final Image cloudImage2 = UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Cloud2.png") ;
	public static final Image cloudImage3 = UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Cloud3.png") ;
	public static final Image StarImage = UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star.png") ;
	
	public Sky ()
	{
		dayTime = new TimeCounter(Game.DayDuration / 2, Game.DayDuration) ;
		isDay = true ;
		updateSkyColor() ;
    	
    	// initialize clouds
    	clouds = new SkyComponent[5] ;
		for (int c = 0 ; c <= clouds.length - 1 ; c += 1)
		{
			Image image = randomCloudImage() ;
			Point initPos = Game.getScreen().pos(Math.random(), 0) ;
			initPos.y += 2 + (int) ((height - image.getHeight(null)) * Math.random()) ;
			Point speed = new Point((int) (1 + 2 * Math.random()), 0) ;
	    	clouds[c] = new SkyComponent(image, initPos, speed) ;
		}
		
		// initialize stars
    	stars = new SkyComponent[50] ;
		for (int s = 0 ; s <= stars.length - 1 ; s += 1)
		{
			Point pos = Game.getScreen().pos(Math.random(), 0.01 + 0.18 * Math.random()) ;
			stars[s] = new SkyComponent(StarImage, pos, new Point(0, 0)) ;
		}
	}
	
	private Image randomCloudImage()
	{
		int cloudNumber = UtilG.randomIntFromTo(1, 3) ;
		return cloudNumber == 1 ? cloudImage1 : (cloudNumber == 2 ? cloudImage2 : cloudImage3) ;
	}
	
	public void updateIsDay() { isDay = Game.DayDuration / 4 <= dayTime.getCounter() & dayTime.getCounter() <= 3 * Game.DayDuration / 4 ;}

//	public void DrawStar(Point Pos, double size, Color color, DrawingOnPanel DP)
//	{
//		int NumberOfPoints = 8 ;
//		int[][] Coords = new int[NumberOfPoints][2] ;
//		double t = size/10 ;
//		Coords[0] = new int[] {0, (int)(t), (int)(size/2), (int)(t), 0, (int)(-t), (int)(-size/2), (int)(-t)} ;
//		Coords[1] = new int[] {(int)(-size/2), (int)(-t), 0, (int)(t), (int)(size/2), (int)(t), 0, (int)(-t)} ;
//		for (int i = 0 ; i <= NumberOfPoints - 1 ; ++i)
//		{
//			Coords[0][i] += Pos.x ;
//			Coords[1][i] += Pos.y ;
//		}
//		DP.DrawPolygon(Coords[0], Coords[1], DrawingOnPanel.stdStroke, color) ;
//	}
	
	private void resetCloudMovement(SkyComponent cloud)
	{
		Point originPos = new Point(-cloud.getImage().getWidth(null), cloud.getPos().y) ;
		cloud.setPos(originPos) ;
		cloud.setCounter(0) ;
	}
	
	private boolean passedScreen(int x) { return Game.getScreen().getSize().width <= x ;}
	
	private void displayDaySky(DrawingOnPanel DP)
	{
		for (SkyComponent cloud : clouds)
		{
			cloud.move() ;
			if (passedScreen(cloud.getPos().x))
			{
				resetCloudMovement(cloud) ;
			}
			cloud.display(DrawingOnPanel.stdAngle, 1 - dayTime.rate(), DP) ;
		}
	}
	
	private void displayNightSky(DrawingOnPanel DP)
	{
		for (SkyComponent star : stars)
		{
//			DrawStar(star.getPos(), 10, star.getColor()[0], DP) ;
		}
	}
	
	private void updateSkyColor()
	{
		double ColorMult = (1 - 1.8 * Math.abs(dayTime.getCounter() - Game.DayDuration / 2) / Game.DayDuration) ;
		int red = Math.max(0, Math.min(Game.colorPalette[21].getRed(), 255)) ;
		int green = Math.max(0, Math.min((int)(Game.colorPalette[21].getGreen() * ColorMult), 255)) ;
		int blue = Math.max(0, Math.min((int)(Game.colorPalette[21].getBlue() * ColorMult), 255)) ;
		color = new Color(red, green, blue) ;
	}
	
	public void display(DrawingOnPanel DP)
	{
		System.out.println(dayTime.rate());
		updateSkyColor() ;
		DP.DrawRect(new Point(0, height), Align.bottomLeft, size, 1, color, null) ;
		
		if (isDay)
		{
			displayDaySky(DP) ;
			
			return ;
		}
		
		displayNightSky(DP) ;
		
	}

}
