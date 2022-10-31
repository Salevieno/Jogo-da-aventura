package GameComponents ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays ;

import Graphics.DrawPrimitives ;
import Utilities.Size;
import Utilities.UtilG;

public class Icon
{
	private int id ;
	private String Name ;
	private Point Pos ;		// [x, y]
	private Size size ;	// [width, height]
	public boolean isActive ;
	private String description ;
	private Image image ;
	private Image SelectedImage ;	// Image when selected
	
	public Icon(int id, String Name, Point Pos, String description, Image image, Image SelectedImage)
	{
		this.id = id ;
		this.Name = Name ;
		this.Pos = Pos ;
		size = new Size(10 * Name.length(), 30) ;
		isActive = false ;
		this.description = description ;
		this.image = image ;
		this.SelectedImage = SelectedImage ;
		
		if (image != null)
		{
			size.x = image.getWidth(null) ;
			size.y = image.getHeight(null) ;
		}
	}

	public int getid() {return id ;}
	public String getName() {return Name ;}
	public Point getPos() {return Pos ;}
	public Image getImage() {return image ;}
	public Image getSelectedImage() {return SelectedImage ;}
	public void setid(int I) {id = I ;}
	public void setName(String N) {Name = N ;}
	public void setPos(Point P) {Pos = P ;}
	public void setImage(Image I) {image = I ;}
	public void setSelectedImage(Image S) {SelectedImage = S ;}
	
	public boolean ishovered(Point MousePos)
	{
		if (UtilG.isInside(MousePos, new Point(Pos.x - size.x / 2, Pos.y - size.y / 2), size))
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	public boolean isselected(int selectedButton)
	{
		if (selectedButton == id)
		{
			return true ;
		}
		return false ;
	}
	
	public void activate()
	{
		isActive = true ;
	}
	public void deactivate()
	{
		isActive = false ;
	}
	public Object startaction()
	{
		if (id == 0)
		{
			return "P" ;	// Language = Portuguese
		}
		if (id == 1)
		{
			return "E" ;	// Language = English
		}
		if (id == 2)
		{
			return "N" ;	// Game = new game
		}
		if (id == 3)
		{
			return "L" ;	// Game = load game
		}
		if (id == 4)
		{
			return "M" ;	// Sex = male
		}
		if (id == 5)
		{
			return "F" ;	// Sex = female
		}
		if (id == 6)
		{
			return "0" ;	// Ousadia = baixo
		}
		if (id == 7)
		{
			return "1" ;	// Ousadia = médio
		}
		if (id == 8)
		{
			return "2" ;	// Ousadia = alto
		}
		if (id == 9)
		{
			return "0" ;	// Classe = cavaleiro
		}
		if (id == 10)
		{
			return "1" ;	// Classe = mago
		}
		if (id == 11)
		{
			return "2" ;	// Classe = arqueiro
		}
		if (id == 12)
		{
			return "3" ;	// Classe = animal
		}
		if (id == 13)
		{
			return "4" ;	// Classe = ladrão
		}
		
		return null ;
	}
	
	
	/* Draw methods */
	public void DrawImage(float angle, int selectedButton, Point MousePos, DrawPrimitives DP)
	{
		if (ishovered(MousePos) | isselected(selectedButton))
		{
			if (SelectedImage != null)
			{
				DP.DrawImage(SelectedImage, Pos, angle, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
			}
			else
			{
				DP.DrawRoundRect(Pos, "Center", size, 5, Color.lightGray, Color.gray, true) ;
				DP.DrawText(Pos, "Center", 0, Name, new Font("Scheherazade Bold", Font.BOLD, 28), Color.blue) ;
			}
		}
		else
		{
			if (image != null)
			{
				DP.DrawImage(image, Pos, angle, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
			}
			else
			{
				DP.DrawRoundRect(Pos, "Center", size, 2, Color.lightGray, Color.gray, true) ;
				DP.DrawText(Pos, "Center", 0, Name, new Font("Scheherazade Bold", Font.BOLD, 28), Color.blue) ;
			}
		}
	}
	public void DrawHoverMessage(DrawPrimitives DP)
	{
		if (description != null)
		{
			DP.DrawRoundRect(new Point(Pos.x + 20, Pos.y - 10), "Center", size, 5, Color.lightGray, Color.gray, true) ;
			DP.DrawFitText(new Point(Pos.x + 20, Pos.y - 10), 14, "Center", description, new Font("Scheherazade Bold", Font.BOLD, 12), 20, Color.blue) ;
		}
	}
	
	
	/* Print methods */
	public void PrintProperties()
	{
		System.out.println() ;
		System.out.println(" *** icon properties ***") ;
		System.out.println("id: " + id) ;
		System.out.println("name: " + Name) ;
		System.out.println("pos: " + Pos) ;
		System.out.println("size: " + size) ;
		System.out.println("image: " + image) ;
		System.out.println("selected image: " + SelectedImage) ;
	}
}