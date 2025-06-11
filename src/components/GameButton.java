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
import main.GamePanel;
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
	
	private static Font font = new Font(Game.MainFontName, Font.BOLD, 17) ;
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
		allButtons.add(this) ;
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

	public static boolean anyIsHovered(Point mousePos) { return allButtons.stream().filter(button -> button.isActive() && button.ishovered(mousePos)).findAny().isPresent() ;}
	public static void updateMouseCursor(Point mousePos)
	{
		if (GameButton.anyIsHovered(GamePanel.getMousePos()))
		{
			GamePanel.setCursorToHand() ;
		}
		else
		{
			GamePanel.setCursorToDefault() ;
		}
	}
	public Point getCenter() {return Util.Translate(topLeft, size.width / 2, size.height / 2) ;}	
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
		GamePanel.DP.drawRoundRect(topLeft, Align.topLeft, size, 5, Game.palette[3], Game.palette[0], true) ;
		GamePanel.DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
	}
	
	public void displayHovered(double angle, boolean displayText)
	{
		if (selectedImage != null)
		{
			GamePanel.DP.drawImage(selectedImage, topLeft, angle, Scale.unit, Align.topLeft) ;
			if (displayText)
			{
				GamePanel.DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
			}
		}
		else
		{
			GamePanel.DP.drawRoundRect(topLeft, Align.topLeft, size, 5, Game.palette[5], Game.palette[0], true) ;
			GamePanel.DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
		}
	}
	
	public void displayNotHovered(double angle, boolean displayText)
	{
		if (image != null)
		{
			GamePanel.DP.drawImage(image, topLeft, angle, Scale.unit, Align.topLeft) ;
			
			if (!displayText) { return ;}
			
			GamePanel.DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
			return ;
		}
		
		GamePanel.DP.drawRoundRect(topLeft, Align.topLeft, size, 2, Game.palette[5], Game.palette[0], true) ;
		GamePanel.DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
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
		
		GamePanel.DP.drawImage(imageDisplayed, topLeft, angle, Scale.unit, Align.topLeft) ;

		if (!displayText) { return ;}
		if (name == null) { return ;}
		
		Point textPos = ishovered(mousePos) ? Util.Translate(getCenter(), 0, 3) : getCenter() ;
		GamePanel.DP.drawText(textPos, Align.center, 0, name, font, textColor) ;
	}
	
	public void displayHoverMessage()
	{
		if (description != null)
		{
			GamePanel.DP.drawGradRoundRect(new Point(topLeft.x + 20, topLeft.y - 10), alignment, size, 5, Game.palette[1], Game.palette[2], Game.palette[0], true) ;
			Draw.fitText(new Point(topLeft.x + 20, topLeft.y - 10), 14, alignment, description, new Font(Game.MainFontName, Font.BOLD, 12), 20, Color.blue) ;
		}
	}

	
	@Override
	public String toString() {
		return "GameButton name:" + name + "";
	}


}