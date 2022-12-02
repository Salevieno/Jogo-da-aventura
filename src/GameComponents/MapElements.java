package GameComponents ;

import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays ;
import Graphics.DrawPrimitives ;
import Utilities.Scale;

public class MapElements
{
	private int id ;
	private String Name ;
	private Point Pos ;
	private Image image ;
	private int[][] block ;
	
	public MapElements(int id, String Name, Point Pos, Image image)
	{
		this.id = id ;
		this.Name = Name ;
		this.Pos = Pos ;
		this.image = image ;
		if (Name.equals("ForestTree"))
		{
			block = new int[75 - 18][] ;
			for (int i = 18 ; i <= 75 - 1 ; i += 1)
			{
				block[i - 18] = new int[] {i, 15} ;
			}
		}
	}

	public int getid() {return id ;}
	public String getName() {return Name ;}
	public Point getPos() {return Pos ;}
	public Image getImage() {return image ;}
	public int[][] getBlock() {return block ;}
	public void setid(int I) {id = I ;}
	public void setName(String N) {Name = N ;}
	public void setPos(Point P) {Pos = P ;}
	public void setImage(Image I) {image = I ;}
	public void setBlock(int[][] B) {block = B ;}

	public void DrawImage(float angle, DrawPrimitives DP)
	{
		DP.DrawImage(image, Pos, angle, new Scale(1, 1), new boolean[] {false, false}, "BotLeft", 1) ;
	}

	public void PrintProperties()
	{
		System.out.println() ;
		System.out.println(" *** icon properties ***") ;
		System.out.println("id: " + id) ;
		System.out.println("name: " + Name) ;
		System.out.println("pos: " + Pos) ;
		System.out.println("image: " + image) ;
		System.out.println("blocks: " + Arrays.deepToString(block)) ;
	}
}