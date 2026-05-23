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
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Sky 
{
	private final Dimension size ;	
	private List<SkyComponent> clouds;
	private List<SkyComponent> stars ;
	private Color topColor ;
	private Color botColor ;

	private static final int HEIGHT ;
	private static final Color[] TOP_COLORS ;
	private static final Color[] BOT_COLORS ;
	private static final Image CLOUD_IMAGE1 ;
	private static final Image CLOUD_IMAGE2 ;
	private static final Image CLOUD_IMAGE3 ;
	private static final Image[] STAR_IMAGES ;
	
	static
	{
		HEIGHT = 192 ;
		TOP_COLORS = new Color[8];
		BOT_COLORS = new Color[8];
		
		Image skyColors = ImageLoader.loadImage(Path.SKY_IMG + "Colors.png") ;
		for (int i = 0; i <= 8 - 1; i += 1)
		{
			TOP_COLORS[i] = Util.getPixelColor(Util.toBufferedImage(skyColors), new Point(10 * i, 0));
			BOT_COLORS[i] = Util.getPixelColor(Util.toBufferedImage(skyColors), new Point(10 * i, 10));
		}

		CLOUD_IMAGE1 = ImageLoader.loadImage(Path.SKY_IMG + "Cloud1.png") ;
		CLOUD_IMAGE2 = ImageLoader.loadImage(Path.SKY_IMG + "Cloud2.png") ;
		CLOUD_IMAGE3 = ImageLoader.loadImage(Path.SKY_IMG + "Cloud3.png") ;
		STAR_IMAGES = new Image[] {
				ImageLoader.loadImage(Path.SKY_IMG + "Star1.png") ,
				ImageLoader.loadImage(Path.SKY_IMG + "Star2.png") ,
				ImageLoader.loadImage(Path.SKY_IMG + "Star3.png") ,
				ImageLoader.loadImage(Path.SKY_IMG + "Star4.png") ,
				ImageLoader.loadImage(Path.SKY_IMG + "Star5.png") ,
				ImageLoader.loadImage(Path.SKY_IMG + "Star6.png") ,
				ImageLoader.loadImage(Path.SKY_IMG + "Star7.png")
		} ;
	}
	
	protected Sky (int width)
	{
		size = new Dimension(width - 60, HEIGHT) ;
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
		return cloudNumber == 1 ? CLOUD_IMAGE1 : (cloudNumber == 2 ? CLOUD_IMAGE2 : CLOUD_IMAGE3) ;
	}
	
	private static Image randomStarImage()
	{
		int starNumber = Util.randomInt(0, STAR_IMAGES.length - 1) ;
		for (int i = 0; i <= STAR_IMAGES.length  - 1; i += 1)
		{
			if (i != starNumber) { continue ;}
			
			return STAR_IMAGES[i] ;
		}
		
		return null ;
	}
	
	
	private void resetCloudMovement(SkyComponent cloud)
	{
		Point2D.Double originPos = new Point2D.Double(-cloud.getImage().getWidth(null), cloud.getPos().y) ;
		cloud.setPos(originPos) ;
	}
	
	private static boolean passedScreen(double x) { return Screen.getMe().mapSize().width <= x ;}
	
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

		for (int i = 0; i <= TOP_COLORS.length - 2; i += 1)
		{
			if (timeRate <= (i + 1) / (double)TOP_COLORS.length)
			{
				topColor = interpolateColor(TOP_COLORS[i] , TOP_COLORS[i + 1], timeRate * TOP_COLORS.length - i);
				
				return ;
			}
		}

		topColor = interpolateColor(TOP_COLORS[TOP_COLORS.length - 1] , TOP_COLORS[0], timeRate * TOP_COLORS.length - (TOP_COLORS.length - 1));
	}

	private void updateBotColor()
	{
		double timeRate = Game.dayTimeRate() ;

		for (int i = 0; i <= BOT_COLORS.length - 2; i += 1)
		{
			if (timeRate <= (i + 1) / (double)BOT_COLORS.length)
			{
				botColor = interpolateColor(BOT_COLORS[i] , BOT_COLORS[i + 1], timeRate * BOT_COLORS.length - i);
				
				return ;
			}
		}
		botColor = interpolateColor(BOT_COLORS[BOT_COLORS.length - 1] , BOT_COLORS[0], timeRate * BOT_COLORS.length - (BOT_COLORS.length - 1));
	}

	private Color interpolateColor(Color startColor, Color endColor, double timeRate)
	{
		int red = (int)(startColor.getRed() + (endColor.getRed() - startColor.getRed()) * timeRate) ;
		int green = (int)(startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * timeRate) ;
		int blue = (int)(startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * timeRate) ;
		
		return new Color(red, green, blue) ;
	}
	
	public void display()
	{ // new Point(0, HEIGHT), Align.bottomLeft, size, color, null
		GamePanel.getDP().drawGradRoundRect(new Point(0, HEIGHT), Align.bottomLeft, size, botColor, topColor, false) ;
		
		if (isDay())
		{
			displayDaySky() ;
			
			return ;
		}
		
		displayNightSky() ;		
	}

	public static int getHeight() { return HEIGHT ;}

}
