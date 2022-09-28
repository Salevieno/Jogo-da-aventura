package GameComponents ;

public class Screen
{
	private int[] Dimensions ;	// Length, height
	private int[] Borders ;	// Min x, Min y, Max x, Max y
	private int[] Center ;
    public int SkyHeight ;

	public Screen(int[] Dimensions, int[] Borders)
	{
		this.Dimensions = Dimensions ;
		this.Borders = Borders ;
		Center = new int[] {Dimensions[0] / 2, Dimensions[1] / 2} ;
		SkyHeight = (int)(0.2 * Dimensions[1]) ;
	}

	public int[] getDimensions() {return Dimensions ;}
	public int[] getBorders() {return Borders ;}
	public int[] getCenter() {return Center ;}
	public void setDimensions(int[] D) {Dimensions = D ;}
	public void setBorders(int[] B) {Borders = B ;}
	
	public boolean posIsInMap(int[] pos)
	{
		return (Borders[0] < pos[0] & Borders[1] < pos[1] & pos[0] < Borders[2] & pos[1] < Borders[3]) ;
	}
}
