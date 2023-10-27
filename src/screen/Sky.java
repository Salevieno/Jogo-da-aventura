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
	private static final Image[] starImages = new Image[] {
			UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star1.png") ,
			UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star2.png") ,
			UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star3.png") ,
			UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star4.png") ,
			UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star5.png") ,
			UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star6.png") ,
			UtilG.loadImage(Game.ImagesPath  + "\\Sky\\" + "Star7.png")
	} ;
	
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
			Image image = randomStarImage() ;
			stars[s] = new SkyComponent(image, pos, new Point(0, 0)) ;
		}
	}
	
	private Image randomCloudImage()
	{
		int cloudNumber = UtilG.randomIntFromTo(1, 3) ;
		return cloudNumber == 1 ? cloudImage1 : (cloudNumber == 2 ? cloudImage2 : cloudImage3) ;
	}
	
	private Image randomStarImage()
	{
		int starNumber = UtilG.randomIntFromTo(0, starImages.length - 1) ;
		for (int i = 0; i <= starImages.length  - 1; i += 1)
		{
			if (i != starNumber) { continue ;}
			
			return starImages[i] ;
		}
		
		return null ;
	}
	
	public void updateIsDay() { isDay = (0.25 <= dayTime.rate() & dayTime.rate() <= 0.75) ;}
	
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
			double alpha = -16 * Math.pow(dayTime.rate(), 2) + 16 * dayTime.rate() - 3 ;
			cloud.display(DrawingOnPanel.stdAngle, alpha, DP) ;
		}
	}
	
	private void displayNightSky(DrawingOnPanel DP)
	{
		for (SkyComponent star : stars)
		{
			star.display(DrawingOnPanel.stdAngle, 1, DP) ;
		}
	}
	
	private void updateSkyColor()
	{
		double ColorMult = 1 - 1.8 * Math.abs(dayTime.rate() - 0.5) ;
		int red = Math.max(0, Math.min((int)(Game.colorPalette[21].getRed() * ColorMult), 255)) ;
		int green = Math.max(0, Math.min((int)(Game.colorPalette[21].getGreen() * ColorMult), 255)) ;
		int blue = Math.max(0, Math.min((int)(Game.colorPalette[21].getBlue() * ColorMult), 255)) ;
		color = new Color(red, green, blue) ;
	}
	
	public void display(DrawingOnPanel DP)
	{

		updateIsDay() ;
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
