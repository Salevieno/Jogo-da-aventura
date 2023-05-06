package maps ;

import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.Collider;
import graphics.DrawingOnPanel;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class MapElements
{
	private int id ;			// id of the element
	private String name ;		// name of the element
	private Point pos ;			// topLeft position of the element
	private Image image ;		// image of the element
	private List<Collider> colliders ;
	// wall, tree, grass, rock, crystal, stalactite, volcano, chest, invisible wall
	public MapElements(int id, String Name, Point Pos, Image image)
	{
		this.id = id ;
		this.name = Name ;
		this.pos = Pos ;
		this.image = image ;
//		if (Name.equals("ForestTree"))
//		{
//			collider = new Point[75 - 18] ;
//			for (int i = 18 ; i <= 75 - 1 ; i += 1)
//			{
//				collider[i - 18] = new Point(i, 15) ;
//			}
//		}
		colliders = new ArrayList<>() ;
		colliders.add(new Collider(pos)) ;
	}

	public int getid() {return id ;}
	public String getName() {return name ;}
	public Point getPos() {return pos ;}
	public Image getImage() {return image ;}
	public List<Collider> getColliders() {return colliders ;}
	public void setid(int I) {id = I ;}
	public void setName(String N) {name = N ;}
	public void setPos(Point P) {pos = P ;}
	public void setImage(Image I) {image = I ;}
	
	private boolean playerIsBehind(Point playerPos) { return UtilG.isInside(playerPos, pos, UtilG.getSize(image)) ;}

	public void display(Point playerPos, DrawingOnPanel DP)
	{
		if (playerIsBehind(playerPos))
		{
			DP.DrawImage(image, pos, DrawingOnPanel.stdAngle, new Scale(1, 1), false, false, Align.topLeft, 0.5) ;
			
			return ;
		}
		
		DP.DrawImage(image, pos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.topLeft) ;
	}
}