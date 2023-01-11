package components ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class GameIcon
{
	private int id ;
	private String Name ;
	private Point topLeftCorner ;
	private Dimension size ;
	private boolean isActive ;
	private String description ;
	private Image image ;
	private Image SelectedImage ;	// Image when selected
	private String value ;	// return value for when the icon is clicked
	
	public static int selectedIconID ;
	public static List<GameIcon> allIcons = new ArrayList<>() ;	// isn't it insane to create a list of all items of a class inside the class itself?
	
	public GameIcon(int id, String Name, Point Pos, String description, Image image, Image SelectedImage)
	{
		this.id = id ;
		this.Name = Name ;
		this.topLeftCorner = Pos ;
		size = new Dimension(10 * Name.length(), 30) ;
		isActive = false ;
		this.description = description ;
		this.image = image ;
		this.SelectedImage = SelectedImage ;

		if (image != null)
		{
			size.width = image.getWidth(null) ;
			size.height = image.getHeight(null) ;
		}

		if (id == 0)
		{
			value = "P" ;	// Language = Portuguese
		}
		if (id == 1)
		{
			value = "E" ;	// Language = English
		}
		if (id == 2)
		{
			value = "N" ;	// Game = new game
		}
		if (id == 3)
		{
			value = "L" ;	// Game = load game
		}
		if (id == 4)
		{
			value = "M" ;	// Sex = male
		}
		if (id == 5)
		{
			value = "F" ;	// Sex = female
		}
		if (id == 6)
		{
			value = "0" ;	// Ousadia = baixo
		}
		if (id == 7)
		{
			value = "1" ;	// Ousadia = médio
		}
		if (id == 8)
		{
			value = "2" ;	// Ousadia = alto
		}
		if (id == 9)
		{
			value = "0" ;	// Classe = cavaleiro
		}
		if (id == 10)
		{
			value = "1" ;	// Classe = mago
		}
		if (id == 11)
		{
			value = "2" ;	// Classe = arqueiro
		}
		if (id == 12)
		{
			value = "3" ;	// Classe = animal
		}
		if (id == 13)
		{
			value = "4" ;	// Classe = ladrão
		}
		description = Name ;
	}

	public int getid() {return id ;}
	public String getName() {return Name ;}
	public Point getPos() {return topLeftCorner ;}
	public boolean getIsActive() { return isActive ;}
	public Image getImage() {return image ;}
	public Image getSelectedImage() {return SelectedImage ;}
	public void setPos(Point P) {topLeftCorner = P ;}
	
	public static void addToAllIconsList(GameIcon icon)
	{
		allIcons.add(icon) ;
	}
	public static void iconIsClicked(Point mousePos)
	{
		for (GameIcon icon : allIcons)
		{
			if (icon.isActive & icon.ishovered(mousePos))
			{
				icon.getValue() ;
			}
		}
	}
	
	public Point getCenter() {return new Point(topLeftCorner.x + size.width / 2, topLeftCorner.y + size.height / 2) ;}
	
	public void select(Point mousePos)
	{
		if (isActive & ishovered(mousePos))
		{
			selectedIconID = id ;
		}
	}
	public boolean ishovered(Point mousePos)
	{
		if (UtilG.isInside(mousePos, topLeftCorner, size))
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	public boolean isselected()
	{
		if (selectedIconID == id)
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
	public String getValue() { return value ;}
	
	
	// Draw methods
	public void display(double angle, Align alignment, Point mousePos, DrawingOnPanel DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 16) ;
		Color textColor = Game.ColorPalette[0] ;
		Color selectedTextColor = Game.ColorPalette[1] ;
		
		select(mousePos) ;
		if (isselected())	// ishovered(MousePos)
		{
			if (SelectedImage != null)
			{
				DP.DrawImage(SelectedImage, topLeftCorner, angle, new Scale(1, 1), alignment) ;
				//DP.DrawText(getCenter(), AlignmentPoints.center, 0, Name, font, selectedTextColor) ;
			}
			else
			{
				DP.DrawRoundRect(topLeftCorner, alignment, size, 5, Game.ColorPalette[5], Game.ColorPalette[6], true) ;
				DP.DrawText(getCenter(), Align.center, 0, Name, font, selectedTextColor) ;
			}
		}
		else
		{
			if (image != null)
			{
				DP.DrawImage(image, topLeftCorner, angle, new Scale(1, 1), alignment) ;
				//DP.DrawText(getCenter(), AlignmentPoints.center, 0, Name, font, textColor) ;
			}
			else
			{
				DP.DrawRoundRect(topLeftCorner, alignment, size, 2, Game.ColorPalette[5], Game.ColorPalette[6], true) ;
				DP.DrawText(getCenter(), Align.center, 0, Name, font, textColor) ;
			}
		}
	}
	public void displayHoverMessage(Align alignment, DrawingOnPanel DP)
	{
		if (description != null)
		{
			DP.DrawRoundRect(new Point(topLeftCorner.x + 20, topLeftCorner.y - 10), alignment, size, 5, Color.lightGray, Color.gray, true) ;
			DP.DrawFitText(new Point(topLeftCorner.x + 20, topLeftCorner.y - 10), 14, alignment, description, new Font(Game.MainFontName, Font.BOLD, 12), 20, Color.blue) ;
		}
	}
}