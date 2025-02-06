package components ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import main.Game;
import utilities.Util;

public class GameButton
{
	private String name ;
	private Point topLeft ;
	private Align alignment ;
	private Dimension size ;
	private boolean isActive ;
	private String description ;
	private Image image ;
	private Image selectedImage ;
	private IconFunction action ;
	
	private static Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
	private static Color textColor = Game.palette[0] ;
	private static Color selectedTextColor = Game.palette[18] ;
	
	public static int selectedIconID ;
	public static List<GameButton> allButtons = new ArrayList<>() ;
		
	public GameButton(Point pos, Align alignment, String name, Image image, Image selectedImage, IconFunction action)
	{
		this.name = name ;
		this.image = image ;
		this.selectedImage = selectedImage ;
		isActive = true ;
		size = image != null ? Util.getSize(image) : new Dimension(100, 50) ;
		this.alignment = alignment ;
		this.topLeft = Util.getTopLeft(pos, alignment, size) ;
		this.action = action ;
	}
	
	public GameButton(Point pos, Align alignment, Image image, Image selectedImage, IconFunction action)
	{
		this(pos, alignment, "", image, selectedImage, action) ;
	}

	public String getName() {return name ;}
	public Point getTopLeftPos() {return topLeft ;}
	public boolean isActive() { return isActive ;}
	public Image getImage() {return image ;}
	public Image getSelectedImage() {return selectedImage ;}
	public void setName(String newName) { name = newName ;}
	public void setTopLeftPos(Point P) {topLeft = P ;}
	
	public Point getCenter() {return Util.Translate(topLeft, size.width / 2, size.height / 2) ;}
	
	public static void addToAllIconsList(GameButton icon) { allButtons.add(icon) ;}

	public boolean ishovered(Point mousePos) { return Util.isInside(mousePos, topLeft, size) ;}
	public boolean isClicked(Point mousePos, String action)
	{
		if (mousePos == null) { return false ;}
		if (action == null) { return false ;}
		
		return ishovered(mousePos) & action.equals("LeftClick") ;
	}
	
	public void activate() { isActive = true ;}
	public void deactivate() { isActive = false ;}
	public void act() { action.act() ;}
	
	
	public void displayGeneralButton()
	{
		Game.DP.drawRoundRect(topLeft, Align.topLeft, size, 5, Game.palette[3], Game.palette[0], true) ;
		Game.DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
	}
	
	public void displayHovered(double angle, boolean displayText)
	{
		if (selectedImage != null)
		{
			Game.DP.drawImage(selectedImage, topLeft, angle, Scale.unit, Align.topLeft) ;
			if (displayText)
			{
				Game.DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
			}
		}
		else
		{
			Game.DP.drawRoundRect(topLeft, Align.topLeft, size, 5, Game.palette[5], Game.palette[0], true) ;
			Game.DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
		}
		
		Game.setCursorToHand() ;
	}
	
	public void displayNotHovered(double angle, boolean displayText)
	{
		if (image != null)
		{
			Game.DP.drawImage(image, topLeft, angle, Scale.unit, Align.topLeft) ;
			
			if (!displayText) { return ;}
			
			Game.DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
			return ;
		}
		
		Game.DP.drawRoundRect(topLeft, Align.topLeft, size, 2, Game.palette[5], Game.palette[0], true) ;
		Game.DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
	}
	
	public void display(double angle, boolean displayText, Point mousePos)
	{
		
		if (!isActive) { return ;}
		
		Image imageDisplayed = ishovered(mousePos) ? selectedImage : image ;
		
		if (imageDisplayed == null)
		{
			displayGeneralButton() ;
			return ;
		}
		
		Game.DP.drawImage(imageDisplayed, topLeft, angle, Scale.unit, Align.topLeft) ;

		if (!displayText) { return ;}
		if (name == null) { return ;}
		
		Game.DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
		
	}
	
	public void displayHoverMessage()
	{
		if (description != null)
		{
			Game.DP.drawGradRoundRect(new Point(topLeft.x + 20, topLeft.y - 10), alignment, size, 5, Game.palette[1], Game.palette[2], Game.palette[0], true) ;
			Draw.fitText(new Point(topLeft.x + 20, topLeft.y - 10), 14, alignment, description, new Font(Game.MainFontName, Font.BOLD, 12), 20, Color.blue) ;
		}
	}

	
	@Override
	public String toString() {
		return "GameButton [name=" + name + "]";
	}


}