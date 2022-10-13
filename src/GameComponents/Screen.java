package GameComponents ;

import java.awt.Point;

public class Screen
{
	private int[] Dimensions ;	// Length, height
	private int[] Borders ;	// Min x, Min y, Max x, Max y
	private Point Center ;
    public int SkyHeight ;

	public Screen(int[] Dimensions, int[] Borders)
	{
		this.Dimensions = Dimensions ;
		this.Borders = Borders ;
		Center = new Point(Dimensions[0] / 2, Dimensions[1] / 2) ;
		SkyHeight = (int)(0.2 * Dimensions[1]) ;
	}

	public int[] getDimensions() {return Dimensions ;}
	public int[] getBorders() {return Borders ;}
	public Point getCenter() {return Center ;}
	public void setDimensions(int[] D) {Dimensions = D ;}
	public void setBorders(int[] B) {Borders = B ;}
	
	public boolean posIsInMap(Point pos)
	{
		return (Borders[0] < pos.x & Borders[1] < pos.y & pos.x < Borders[2] & pos.y < Borders[3]) ;
	}
}
