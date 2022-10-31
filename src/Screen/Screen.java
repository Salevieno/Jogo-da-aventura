package Screen ;

import java.awt.Point;

import Utilities.Size;

public class Screen
{
	private Size size ;	// Length, height
	private int[] borders ;	// Min x, Min y, Max x, Max y
	private Point center ;	// center of the entire screen, including the sky
	private Point mapCenter ;	// center of the walkable map, excluding the sky

	public Screen(Size size, int[] borders)
	{
		this.size = size ;
		this.borders = borders ;
	}

	public Size getSize() {return size ;}
	public int[] getBorders() {return borders ;}
	public Point getCenter() {return center ;}
	public Point getMapCenter() {return mapCenter ;}
	public void setSize(Size D) {size = D ;}
	public void setBorders(int[] B) {borders = B ;}
	public void setCenter() {center = new Point(size.x / 2, size.y / 2) ;}
	public void setMapCenter() {mapCenter = new Point(size.x / 2, (size.y + borders[1]) / 2) ;}
	
	public boolean posIsInMap(Point pos)
	{
		return (borders[0] < pos.x & borders[1] < pos.y & pos.x < borders[2] & pos.y < borders[3]) ;
	}
}
