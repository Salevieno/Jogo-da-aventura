package screen ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import main.Game;
import main.GamePanel;
import main.Path;
import utilities.Util;


public class Sky 
{
	private final Dimension size ;
	
	private static List<SkyComponent> clouds;
	private static List<SkyComponent> stars ;
	private static Color color ;
	private static final Image cloudImage1 ;
	private static final Image cloudImage2 ;
	private static final Image cloudImage3 ;
	private static final Image[] starImages ;
	public static final int height ;
	
	static
	{
		height = 192 ;
		cloudImage1 = Game.loadImage(Path.SKY_IMG + "Cloud1.png") ;
		cloudImage2 = Game.loadImage(Path.SKY_IMG + "Cloud2.png") ;
		cloudImage3 = Game.loadImage(Path.SKY_IMG + "Cloud3.png") ;
		starImages = new Image[] {
				Game.loadImage(Path.SKY_IMG + "Star1.png") ,
				Game.loadImage(Path.SKY_IMG + "Star2.png") ,
				Game.loadImage(Path.SKY_IMG + "Star3.png") ,
				Game.loadImage(Path.SKY_IMG + "Star4.png") ,
				Game.loadImage(Path.SKY_IMG + "Star5.png") ,
				Game.loadImage(Path.SKY_IMG + "Star6.png") ,
				Game.loadImage(Path.SKY_IMG + "Star7.png")
		} ;
	}
	
	public Sky (int screenWidth)
	{
		size = new Dimension(screenWidth - 60, height) ;
    	
    	clouds = new ArrayList<>() ;
		for (int c = 0 ; c <= 5 - 1 ; c += 1)
		{
			Image image = randomCloudImage() ;
			Point posAsPoint = Util.randomPos(new Point(), new Dimension(size.width, size.height - image.getHeight(null)), new Dimension(1, 1)) ;
			Point2D.Double pos = new Point2D.Double(posAsPoint.x, posAsPoint.y) ;
			Point2D.Double speed = new Point2D.Double((int) (20 + 100 * Math.random()), 0) ;
	    	clouds.add(new SkyComponent(image, pos, speed)) ;
		}
		
    	stars = new ArrayList<>() ;
		for (int s = 0 ; s <= 50 - 1 ; s += 1)
		{
			Point posAsPoint = Util.randomPos(new Point(), size, new Dimension(1, 1)) ;
			Point2D.Double pos = new Point2D.Double(posAsPoint.x, posAsPoint.y) ;
			Image image = randomStarImage() ;
			stars.add(new SkyComponent(image, pos, new Point2D.Double(0, 0))) ;
		}
	}
	
	
	private static Image randomCloudImage()
	{
		int cloudNumber = Util.randomInt(1, 3) ;
		return cloudNumber == 1 ? cloudImage1 : (cloudNumber == 2 ? cloudImage2 : cloudImage3) ;
	}
	
	private static Image randomStarImage()
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
		Point2D.Double originPos = new Point2D.Double(-cloud.getImage().getWidth(null), cloud.getPos().y) ;
		cloud.setPos(originPos) ;
	}
	
	private static boolean passedScreen(double x) { return Game.getScreen().mapSize().width <= x ;}
	
	private static boolean isDay() { return (0.25 <= Game.dayTimeRate() & Game.dayTimeRate() <= 0.75) ;}
	
	public void update(double dt)
	{
		updateSkyColor() ;

		for (SkyComponent cloud : clouds)
		{
			cloud.move(dt) ;
			if (passedScreen(cloud.getPos().x))
			{
				resetCloudMovement(cloud) ;
			}
			cloud.setOpacity(-16 * Math.pow(Game.dayTimeRate(), 2) + 16 * Game.dayTimeRate() - 3) ;
		}

		for (SkyComponent star : stars)
		{
			star.setOpacity(-16 * Math.pow(Game.dayTimeRate() - 0.5, 2) + 16 * Math.abs(Game.dayTimeRate() - 0.5) - 3) ;
		}
	}

	private void displayDaySky()
	{
		clouds.forEach(SkyComponent::display);
	}
	
	private void displayNightSky()
	{
		stars.forEach(SkyComponent::display);
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
		GamePanel.DP.drawRect(new Point(0, height), Align.bottomLeft, size, color, null) ;
		
		if (isDay())
		{
			displayDaySky() ;
			
			return ;
		}
		
		displayNightSky() ;		
	}

}
