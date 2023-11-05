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
	private int id ;
	private String name ;
	private Point topLeft ;
	private Image image ;
	private List<Collider> colliders ;

	public MapElements(int id, String name, Point pos, Image image)
	{
		this.id = id ;
		this.name = name ;
		this.topLeft = pos ;
		this.image = image ;
		addColliders(name) ;
	}

	public int getid() {return id ;}
	public String getName() {return name ;}
	public Point getPos() {return topLeft ;}
	public Image getImage() {return image ;}
	public List<Collider> getColliders() {return colliders ;}
	public void setid(int I) {id = I ;}
	public void setName(String N) {name = N ;}
	public void setPos(Point P) {topLeft = P ;}
	public void setImage(Image I) {image = I ;}
	
	private boolean playerIsBehind(Point playerPos) { return UtilG.isInside(playerPos, topLeft, UtilG.getSize(image)) ;}

	private void addColliders(String name)
	{

		colliders = new ArrayList<>() ;
		colliders.add(new Collider(topLeft)) ;
		switch(name)
		{
			case "":
			default: return ;
		}

//		if (name.equals("ForestTree"))
//		{
//			collider = new Point[75 - 18] ;
//			for (int i = 18 ; i <= 75 - 1 ; i += 1)
//			{
//				collider[i - 18] = new Point(i, 15) ;
//			}
//		}
	}
	
	public void display(Point playerPos, DrawingOnPanel DP)
	{
		if (!playerIsBehind(playerPos))
		{
			DP.DrawImage(image, topLeft, DrawingOnPanel.stdAngle, Scale.unit, Align.topLeft) ;
			
			return ;
		}
		
		DP.DrawImage(image, topLeft, DrawingOnPanel.stdAngle, Scale.unit, false, false, Align.topLeft, 0.5) ;
		
	}
}