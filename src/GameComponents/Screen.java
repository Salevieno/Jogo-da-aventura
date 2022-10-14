package GameComponents ;

import java.awt.Point;

public class Screen
{
	private Size size ;	// Length, height
	private int[] borders ;	// Min x, Min y, Max x, Max y
	private Point center ;
    private int skyHeight ;

	public Screen(Size Size, int[] Borders)
	{
		this.size = Size ;
		this.borders = Borders ;
		center = new Point(Size.x / 2, Size.y / 2) ;
		skyHeight = (int)(0.2 * Size.x) ;
	}

	public Size getSize() {return size ;}
	public int[] getBorders() {return borders ;}
	public Point getCenter() {return center ;}
	public int getSkyHeight() {return skyHeight ;}
	public void setSize(Size D) {size = D ;}
	public void setBorders(int[] B) {borders = B ;}
	
	public boolean posIsInMap(Point pos)
	{
		return (borders[0] < pos.x & borders[1] < pos.y & pos.x < borders[2] & pos.y < borders[3]) ;
	}
}
