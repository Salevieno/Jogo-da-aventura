package components ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class GameButton
{
	private int id ;
	private String name ;
	private Point topLeft ;
	private Align alignment ;
	private Dimension size ;
	private boolean isActive ;
	private String description ;
	private String value ;			// return value for when the icon is clicked
	private Image image ;
	private Image selectedImage ;
	private IconFunction action ;
	
	public static int selectedIconID ;
	public static List<GameButton> allIcons = new ArrayList<>() ;	// isn't it insane to create a list of all items of a class inside the class itself?
	
	public GameButton(Point pos, Align alignment, Image image, Image selectedImage, IconFunction action)
	{
		this.image = image ;
		this.selectedImage = selectedImage ;
		isActive = true ;
		size = UtilG.getSize(image) ;
		this.alignment = alignment ;
		this.topLeft = UtilG.getTopLeft(pos, alignment, size) ;
		this.action = action ;
	}
	
	public GameButton(Point pos, Align alignment, String name, Image image, Image selectedImage, IconFunction action)
	{
		this.name = name ;
		this.image = image ;
		this.selectedImage = selectedImage ;
		isActive = true ;
		size = UtilG.getSize(image) ;
		this.alignment = alignment ;
		this.topLeft = UtilG.getTopLeft(pos, alignment, size) ;
		this.action = action ;
	}
	
	public GameButton(int id, String name, Point pos, String description, Image image, Image selectedImage)
	{
		
		this.id = id ;
		this.name = name ;
		this.topLeft = pos ;
		size = image != null ? UtilG.getSize(image) : new Dimension(10 * name.length(), 30) ;
		isActive = false ;
		this.description = description ;
		this.image = image ;
		this.selectedImage = selectedImage ;
		this.alignment = Align.topLeft ;
		
	}

	public int getid() {return id ;}
	public String getName() {return name ;}
	public Point getTopLeftPos() {return topLeft ;}
	public boolean isActive() { return isActive ;}
	public Image getImage() {return image ;}
	public Image getSelectedImage() {return selectedImage ;}
	public void setTopLeftPos(Point P) {topLeft = P ;}
	
	public static void addToAllIconsList(GameButton icon) { allIcons.add(icon) ;}

	public void act() { action.act() ;}
	
	public Point getCenter() {return UtilG.Translate(topLeft, size.width / 2, size.height / 2) ;}
	
	public void select(Point mousePos)
	{
		if (!isActive) { return ;}

		if (ishovered(mousePos))
		{
			selectedIconID = id ;
			return ;
		}
		
		selectedIconID = -1 ;
	}
	public boolean ishovered(Point mousePos) { return UtilG.isInside(mousePos, topLeft, size) ;}
	public boolean isClicked(Point mousePos, String action)
	{
		if (action == null) { return false ;}
		
		return ishovered(mousePos) & action.equals("LeftClick") ;
	}
	public boolean isselected() { return selectedIconID == id ;}
	
	public void activate() { isActive = true ;}
	public void deactivate() { isActive = false ;}
	public String getValue() { return value ;}
	
	public void display(double angle, boolean displayText, Point mousePos, DrawingOnPanel DP)
	{
		
		if (!isActive) { return ;}
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color textColor = Game.colorPalette[9] ;
		Color selectedTextColor = Game.colorPalette[10] ;
		
		select(mousePos) ;
		if (isselected())	// ishovered(MousePos)
		{
			if (selectedImage != null)
			{
				DP.DrawImage(selectedImage, topLeft, angle, new Scale(1, 1), Align.topLeft) ;
				if (displayText)
				{
					DP.DrawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
				}
			}
			else
			{
				DP.DrawRoundRect(topLeft, Align.topLeft, size, 5, Game.colorPalette[5], Game.colorPalette[6], true) ;
				DP.DrawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
			}
		}
		else
		{
			if (image != null)
			{
				DP.DrawImage(image, topLeft, angle, new Scale(1, 1), Align.topLeft) ;
				if (displayText)
				{
					DP.DrawText(getCenter(), Align.center, 0, name, font, textColor) ;
				}
			}
			else
			{
				DP.DrawRoundRect(topLeft, Align.topLeft, size, 2, Game.colorPalette[5], Game.colorPalette[6], true) ;
				DP.DrawText(getCenter(), Align.center, 0, name, font, textColor) ;
			}
		}
	}
	
	public void displayHoverMessage(DrawingOnPanel DP)
	{
		if (description != null)
		{
			DP.DrawRoundRect(new Point(topLeft.x + 20, topLeft.y - 10), alignment, size, 5, Color.lightGray, Color.gray, true) ;
			DP.DrawFitText(new Point(topLeft.x + 20, topLeft.y - 10), 14, alignment, description, new Font(Game.MainFontName, Font.BOLD, 12), 20, Color.blue) ;
		}
	}

	
	@Override
	public String toString() {
		return "GameButton [name=" + name + "]";
	}


}