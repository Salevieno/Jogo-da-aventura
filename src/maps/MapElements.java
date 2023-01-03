package maps ;

import java.awt.Image ;
import java.awt.Point;

import graphics.DrawingOnPanel;
import utilities.Align;
import utilities.Scale;

public class MapElements
{
	private int id ;			// id of the element
	private String name ;		// name of the element
	private Point pos ;			// topLeft position of the element
	private Image image ;		// image of the element
	private Point[] collider ;	// points in the element with colliders
	
	public MapElements(int id, String Name, Point Pos, Image image)
	{
		this.id = id ;
		this.name = Name ;
		this.pos = Pos ;
		this.image = image ;
		if (Name.equals("ForestTree"))
		{
			collider = new Point[75 - 18] ;
			for (int i = 18 ; i <= 75 - 1 ; i += 1)
			{
				collider[i - 18] = new Point(i, 15) ;
			}
		}
	}

	public int getid() {return id ;}
	public String getName() {return name ;}
	public Point getPos() {return pos ;}
	public Image getImage() {return image ;}
	public Point[] getBlock() {return collider ;}
	public void setid(int I) {id = I ;}
	public void setName(String N) {name = N ;}
	public void setPos(Point P) {pos = P ;}
	public void setImage(Image I) {image = I ;}
	public void setBlock(Point[] B) {collider = B ;}

	public void DrawImage(double angle, DrawingOnPanel DP)
	{
		DP.DrawImage(image, pos, angle, new Scale(1, 1), Align.topLeft) ;
	}
}