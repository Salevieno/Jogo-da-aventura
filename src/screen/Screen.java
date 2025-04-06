package screen ;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

import main.GameFrame;

public class Screen
{
	private Dimension size ;
	private int[] borders ;	// min x, min y, max x, max y
	private Point center ;	// center of the entire screen, including the sky
	private Point mapCenter ;	// center of the walkable map
	private Point2D.Double scale ;

	public Screen(Dimension size, int[] borders)
	{
		this.size = size ;
		this.borders = borders ;
		this.scale = calcScale() ;
		calcCenter() ;
	}

	public Dimension getSize() {return size ;}
	public int[] getBorders() {return borders ;}
	public Point getCenter() {return center ;}
	public Point getMapCenter() {return mapCenter ;}
	public Point2D.Double getScale() {return scale ;}
	public void setSize(Dimension D) {size = D ;}
	public void setBorders(int[] B) {borders = B ;}
	public void calcCenter() {center = new Point(size.width / 2, size.height / 2) ;}
	public void setMapCenter() {mapCenter = new Point((size.width - 40) / 2, (size.height + borders[1]) / 2) ;}
	public Dimension mapSize() { return new Dimension(size.width - 40, size.height - borders[1]) ;}
	
	
	private static Point2D.Double calcScale()
	{
		System.out.println("Calc scale");

		if (!GameFrame.fullScreen) { return new Point2D.Double(1.0, 1.0) ;}
		
		Dimension laptopScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double scaleX = (double) laptopScreenSize.width / GameFrame.getWindowsize().width ;
        double scaleY = (double) laptopScreenSize.height /  GameFrame.getWindowsize().height ;
        double aspectRatio = GameFrame.getWindowsize().width / GameFrame.getWindowsize().height ;
        double currentRatio = (double) laptopScreenSize.width / laptopScreenSize.height;

		System.out.println(laptopScreenSize);
		System.out.println(new Point2D.Double(scaleX, scaleY));
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
	
	public void updateScale() { scale = calcScale() ;}
	
	public boolean posIsWithinBorders(Point pos)
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
}
