package screen ;

import java.awt.Dimension;
import java.awt.Point;

public class Screen
{
	private Dimension size ;
	private int[] borders ;	// min x, min y, max x, max y
	private Point center ;	// center of the entire screen, including the sky
	private Point mapCenter ;	// center of the walkable map, excluding the sky

	public Screen(Dimension size, int[] borders)
	{
		this.size = size ;
		this.borders = borders ;
	}

	public Dimension getSize() {return size ;}
	public int[] getBorders() {return borders ;}
	public Point getCenter() {return center ;}
	public Point getMapCenter() {return mapCenter ;}
	public void setSize(Dimension D) {size = D ;}
	public void setBorders(int[] B) {borders = B ;}
	public void calcCenter() {center = new Point(size.width / 2, size.height / 2) ;}
	public void setMapCenter() {mapCenter = new Point(size.width / 2, (size.height + borders[1]) / 2) ;}

	public Dimension sizeWithSidebar() {return new Dimension(size.width + SideBar.size.width, size.height + SideBar.size.height) ;}
	
	public boolean posIsWithinBorders(Point pos)
	{
		return (borders[0] < pos.x & borders[1] < pos.y & pos.x < borders[2] & pos.y < borders[3]) ;
	}
	
	public Point pos(double x, double y)
	{
		return new Point((int)(x * size.width), (int)(y * size.height)) ;
	}
	
	public Point getPointWithinBorders(double x, double y)
	{
		return new Point((int)(x * size.width), (int)(borders[1] + y * (size.height - borders[1]))) ;
	}
}
