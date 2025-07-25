package screen ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;

import graphics.Align;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import utilities.Util;
import utilities.UtilS;

public class Sky 
{
	private final Dimension size ;
	
	private static SkyComponent[] clouds;
	private static SkyComponent[] stars ;
	private static Color color ;
	private static final Image cloudImage1 ;
	private static final Image cloudImage2 ;
	private static final Image cloudImage3 ;
	private static final Image[] starImages ;
	public static final int height ;
	
	static
	{
		height = 192 ;
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
	
	public Sky (int screenWidth)
	{
		size = new Dimension(screenWidth - 60, height) ;
    	
    	clouds = new SkyComponent[5] ;
		for (int c = 0 ; c <= clouds.length - 1 ; c += 1)
		{
			Image image = randomCloudImage() ;
			Point initPos = Util.RandomPos(new Point(), new Dimension(size.width, size.height - image.getHeight(null)), new Dimension(1, 1)) ;
			Point speed = new Point((int) (1 + 2 * Math.random()), 0) ;
	    	clouds[c] = new SkyComponent(image, initPos, speed) ;
		}
		
    	stars = new SkyComponent[50] ;
		for (int s = 0 ; s <= stars.length - 1 ; s += 1)
		{
			Point pos = Util.RandomPos(new Point(), size, new Dimension(1, 1)) ;
			Image image = randomStarImage() ;
			stars[s] = new SkyComponent(image, pos, new Point(0, 0)) ;
		}
	}
	
	
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
	
	
	private void resetCloudMovement(SkyComponent cloud)
	{
		Point originPos = new Point(-cloud.getImage().getWidth(null), cloud.getPos().y) ;
		cloud.setPos(originPos) ;
		// TODO
		cloud.setCounter(0) ;
	}
	
	private boolean passedScreen(int x) { return Game.getScreen().mapSize().width <= x ;}
	
	private static boolean isDay() { return (0.25 <= Game.dayTimeRate() & Game.dayTimeRate() <= 0.75) ;}
	
	private void displayDaySky()
	{
		for (SkyComponent cloud : clouds)
		{
			cloud.move() ;
			if (passedScreen(cloud.getPos().x))
			{
				resetCloudMovement(cloud) ;
			}
			double alpha = -16 * Math.pow(Game.dayTimeRate(), 2) + 16 * Game.dayTimeRate() - 3 ;
			cloud.display(Draw.stdAngle, alpha) ;
		}
	}
	
	private void displayNightSky()
	{
		double alpha = -16 * Math.pow(Game.dayTimeRate() - 0.5, 2) + 16 * Math.abs(Game.dayTimeRate() - 0.5) - 3 ;
		
		for (SkyComponent star : stars)
		{
			star.display(Draw.stdAngle, alpha) ;
		}
	}
	
	private void updateSkyColor()
	{
		double mult = 1 - 1.8 * Math.abs(Game.dayTimeRate() - 0.5) ;
		int red = Math.max(0, Math.min((int)(Game.palette[21].getRed() * mult), 255)) ;
		int green = Math.max(0, Math.min((int)(Game.palette[21].getGreen() * mult), 255)) ;
		int blue = Math.max(0, Math.min((int)(Game.palette[21].getBlue() * mult), 255)) ;
		color = new Color(red, green, blue) ;
	}
	
	public void display()
	{
		updateSkyColor() ;
		GamePanel.DP.drawRect(new Point(0, height), Align.bottomLeft, size, color, null) ;
		
		if (isDay())
		{
			displayDaySky() ;
			
			return ;
		}
		
		displayNightSky() ;
		
	}

}
