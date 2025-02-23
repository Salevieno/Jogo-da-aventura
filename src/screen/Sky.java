package screen ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;

import graphics.Align;
import graphics2.Draw;
import main.Game;
import utilities.GameTimer;
import utilities.Util;
import utilities.UtilS;

public class Sky 
{
	public static GameTimer dayCounter ;
	private static SkyComponent[] clouds;
	private static SkyComponent[] stars ;
	private static Color color ;

	public static final int height ;
	public static final Dimension size ;
	public static final Image cloudImage1 ;
	public static final Image cloudImage2 ;
	public static final Image cloudImage3 ;
	private static final Image[] starImages ;
	
	static
	{
		dayCounter = new GameTimer(600) ;
		height = (int)(0.2 * Game.getScreen().getSize().height) ;
		size = new Dimension(Game.getScreen().getSize().width - 40, height) ;
		cloudImage1 = UtilS.loadImage("\\Sky\\" + "Cloud1.png") ;
		cloudImage2 = UtilS.loadImage("\\Sky\\" + "Cloud2.png") ;
		cloudImage3 = UtilS.loadImage("\\Sky\\" + "Cloud3.png") ;
		starImages = new Image[] {
				UtilS.loadImage("\\Sky\\" + "Star1.png") ,
				UtilS.loadImage("\\Sky\\" + "Star2.png") ,
				UtilS.loadImage("\\Sky\\" + "Star3.png") ,
				UtilS.loadImage("\\Sky\\" + "Star4.png") ,
				UtilS.loadImage("\\Sky\\" + "Star5.png") ,
				UtilS.loadImage("\\Sky\\" + "Star6.png") ,
				UtilS.loadImage("\\Sky\\" + "Star7.png")
		} ;
	}
	
	public Sky ()
	{
		dayCounter.start() ;
		updateSkyColor() ;
    	
    	clouds = new SkyComponent[5] ;
		for (int c = 0 ; c <= clouds.length - 1 ; c += 1)
		{
			Image image = randomCloudImage() ;
			Point initPos = Game.getScreen().pos(Math.random(), 0) ;
			initPos.y += 2 + (int) ((height - image.getHeight(null)) * Math.random()) ;
			Point speed = new Point((int) (1 + 2 * Math.random()), 0) ;
	    	clouds[c] = new SkyComponent(image, initPos, speed) ;
		}
		
    	stars = new SkyComponent[50] ;
		for (int s = 0 ; s <= stars.length - 1 ; s += 1)
		{
			Point pos = Game.getScreen().pos(Math.random(), 0.01 + 0.18 * Math.random()) ;
			Image image = randomStarImage() ;
			stars[s] = new SkyComponent(image, pos, new Point(0, 0)) ;
		}
	}
	
	public static double dayTimeRate() { return dayCounter.rate() <= 0.5 ? dayCounter.rate() + 0.5 : dayCounter.rate() - 0.5 ;}
	
	private Image randomCloudImage()
	{
		int cloudNumber = Util.randomInt(1, 3) ;
		return cloudNumber == 1 ? cloudImage1 : (cloudNumber == 2 ? cloudImage2 : cloudImage3) ;
	}
	
	private Image randomStarImage()
	{
		int starNumber = Util.randomInt(0, starImages.length - 1) ;
		for (int i = 0; i <= starImages.length  - 1; i += 1)
		{
			if (i != starNumber) { continue ;}
			
			return starImages[i] ;
		}
		
		return null ;
	}
	
	public boolean isDay() { return (0.25 <= dayTimeRate() & dayTimeRate() <= 0.75) ;}
	
	private void resetCloudMovement(SkyComponent cloud)
	{
		Point originPos = new Point(-cloud.getImage().getWidth(null), cloud.getPos().y) ;
		cloud.setPos(originPos) ;
		cloud.setCounter(0) ;
	}
	
	private boolean passedScreen(int x) { return Game.getScreen().mapSize().width <= x ;}
	
	private void displayDaySky()
	{
		for (SkyComponent cloud : clouds)
		{
			cloud.move() ;
			if (passedScreen(cloud.getPos().x))
			{
				resetCloudMovement(cloud) ;
			}
			double alpha = -16 * Math.pow(dayTimeRate(), 2) + 16 * dayTimeRate() - 3 ;
			cloud.display(Draw.stdAngle, alpha) ;
		}
	}
	
	private void displayNightSky()
	{
		double alpha = -16 * Math.pow(dayTimeRate() - 0.5, 2) + 16 * Math.abs(dayTimeRate() - 0.5) - 3 ;
		
		for (SkyComponent star : stars)
		{
			star.display(Draw.stdAngle, alpha) ;
		}
	}
	
	private void updateSkyColor()
	{
		double mult = 1 - 1.8 * Math.abs(dayTimeRate() - 0.5) ;
		int red = Math.max(0, Math.min((int)(Game.palette[21].getRed() * mult), 255)) ;
		int green = Math.max(0, Math.min((int)(Game.palette[21].getGreen() * mult), 255)) ;
		int blue = Math.max(0, Math.min((int)(Game.palette[21].getBlue() * mult), 255)) ;
		color = new Color(red, green, blue) ;
	}
	
	public void display()
	{
		updateSkyColor() ;
		Game.DP.drawRect(new Point(0, height), Align.bottomLeft, size, color, null) ;
		
		if (isDay())
		{
			displayDaySky() ;
			
			return ;
		}
		
		displayNightSky() ;
		
	}

}
