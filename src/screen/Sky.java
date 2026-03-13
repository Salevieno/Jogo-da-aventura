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
	private static Color topColor ;
	private static Color botColor ;
	private static final Color[] topColors ;
	private static final Color[] botColors ;
	private static final Image cloudImage1 ;
	private static final Image cloudImage2 ;
	private static final Image cloudImage3 ;
	private static final Image[] starImages ;
	public static final int height ;
	
	static
	{
		height = 192 ;
		topColors = new Color[8];
		botColors = new Color[8];
		
		Image skyColors = Game.loadImage(Path.SKY_IMG + "Colors.png") ;
		for (int i = 0; i <= 8 - 1; i += 1)
		{
			topColors[i] = Util.getPixelColor(Util.toBufferedImage(skyColors), new Point(10 * i, 0));
			botColors[i] = Util.getPixelColor(Util.toBufferedImage(skyColors), new Point(10 * i, 10));
		}

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
			Point2D.Double speed = new Point2D.Double((int) (5 + 10 * Math.random()), 0) ;
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
		updateTopColor() ;
		updateBotColor() ;

		for (SkyComponent cloud : clouds)
		{
			cloud.move(dt) ;
			if (passedScreen(cloud.getPos().x))
			{
				resetCloudMovement(cloud) ;
			}
			double opacity = Math.max(0, -16 * Math.pow(Game.dayTimeRate(), 2) + 16 * Game.dayTimeRate() - 3) ;
			cloud.setOpacity(opacity) ;
		}

		for (SkyComponent star : stars)
		{
			double opacity = Math.max(0, -16 * Math.pow(Game.dayTimeRate() - 0.5, 2) + 16 * Math.abs(Game.dayTimeRate() - 0.5) - 3) ;
			star.setOpacity(opacity) ;
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

	private void updateTopColor()
	{
		double timeRate = Game.dayTimeRate() ;

		for (int i = 0; i <= topColors.length - 2; i += 1)
		{
			if (timeRate <= (i + 1) / (double)topColors.length)
			{
				topColor = interpolateColor(topColors[i] , topColors[i + 1], timeRate * topColors.length - i);
				
				return ;
			}
		}

		topColor = interpolateColor(topColors[topColors.length - 1] , topColors[0], timeRate * topColors.length - (topColors.length - 1));
	}

	private void updateBotColor()
	{
		double timeRate = Game.dayTimeRate() ;

		for (int i = 0; i <= botColors.length - 2; i += 1)
		{
			if (timeRate <= (i + 1) / (double)botColors.length)
			{
				botColor = interpolateColor(botColors[i] , botColors[i + 1], timeRate * botColors.length - i);
				
				return ;
			}
		}
		botColor = interpolateColor(botColors[botColors.length - 1] , botColors[0], timeRate * botColors.length - (botColors.length - 1));
	}

	private Color interpolateColor(Color startColor, Color endColor, double timeRate)
	{
		int red = (int)(startColor.getRed() + (endColor.getRed() - startColor.getRed()) * timeRate) ;
		int green = (int)(startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * timeRate) ;
		int blue = (int)(startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * timeRate) ;
		
		return new Color(red, green, blue) ;
	}
	
	public void display()
	{ // new Point(0, height), Align.bottomLeft, size, color, null
		GamePanel.DP.drawGradRoundRect(new Point(0, height), Align.bottomLeft, size, botColor, topColor, false) ;
		
		if (isDay())
		{
			displayDaySky() ;
			
			return ;
		}
		
		displayNightSky() ;		
	}

}
