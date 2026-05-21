package screen ;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

import graphics.Align;
import main.GameFrame;
import main.GamePanel;
import main.Palette;

public class Screen
{
	private final Dimension size ;
	private final Sky sky ;
	private final int[] borders ;	// min x, min y, max x, max y
	private final Point center ;	// center of the entire screen, including the sky
	private final Point mapCenter ;	// center of the walkable map
	private Point2D.Double scale ;
	
	private static Screen screen ;
	private static final int borderOffset = 20 ;

	private Screen(Dimension size, boolean fullscreen)
	{
		this.sky = new Sky(size.width) ;
		this.size = size ;
		this.borders = new int[] { borderOffset, Sky.getHeight() + borderOffset, size.width - 60 - borderOffset, size.height - borderOffset } ;
		this.center = new Point(size.width / 2, size.height / 2) ;
		this.mapCenter = new Point((size.width - 60) / 2, (size.height + Sky.getHeight()) / 2) ;
		this.scale = calcScale(fullscreen) ;
	}

	public static void create(Dimension size, boolean fullScreen)
	{
		if (screen != null) { return ;}

		screen = new Screen(size, fullScreen) ;
	}

	public static Screen getMe() { return screen ;}
	public static int getBorderOffset() { return borderOffset ;}

	public Dimension getSize() {return size ;}
	public int[] getBorders() {return borders ;}
	public Point getCenter() {return center ;}
	public Point getMapCenter() {return mapCenter ;}
	public Point2D.Double getScale() {return scale ;}
	public Dimension mapSize() { return new Dimension(size.width - 60, size.height - borders[1]) ;}
	
	
	private static Point2D.Double calcScale(boolean fullScreen)
	{
		if (!fullScreen) { return new Point2D.Double(1.0, 1.0) ;}
		
		Dimension laptopScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double scaleX = (double) laptopScreenSize.width / GameFrame.getWindowsize().width ;
        double scaleY = (double) laptopScreenSize.height /  GameFrame.getWindowsize().height ;
        double aspectRatio = GameFrame.getWindowsize().width / GameFrame.getWindowsize().height ;
        double currentRatio = (double) laptopScreenSize.width / laptopScreenSize.height;

        if (aspectRatio < currentRatio)
        {
            scaleX = scaleY ;
        }
        else
        {
            scaleY = scaleX ;
        }
        
        return new Point2D.Double(scaleX, scaleY) ;
	}
	
	public void updateScale(boolean fullScreen) { scale = calcScale(fullScreen) ;}
	
	public boolean posIsWithinBorders(Point2D.Double pos)
	{
		return (borders[0] < pos.x & borders[1] < pos.y & pos.x < borders[2] & pos.y < borders[3]) ;
	}
	
	public Point pos(double x, double y)
	{
		return new Point((int)(x * size.width), (int)(y * size.height)) ;
	}
	
	public Point posInMap(double x, double y)
	{
		return new Point((int)(x * mapSize().width), (int)(y * mapSize().height)) ;
	}
	
	public Point getPointWithinBorders(double x, double y)
	{
		return new Point((int)(x * mapSize().width), (int)(borders[1] + y * mapSize().height)) ;
	}

	public void displayBorders()
	{
		Point botLeft = new Point(borders[0], borders[3]) ;
		GamePanel.getDP().drawRect(botLeft, Align.bottomLeft, new Dimension(borders[2] - borders[0], borders[3] - borders[1]), null, Palette.colors[1]) ;
	}

	public void updateSky(double dt)
	{
		sky.update(dt) ;
	}
	
	public void displaySky()
	{
		sky.display() ;
	}
}
