package utilities;

public class Scale
{
	public final static Scale unit = new Scale(1, 1) ;
	
	public double x ;
	public double y ;
	
	public Scale(double x, double y)
	{
		this.x = x ;
		this.y = y ;
	}

	@Override
	public String toString() {
		return "Scale [x=" + x + ", y=" + y + "]";
	}	
}
