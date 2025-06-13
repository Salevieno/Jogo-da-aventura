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
	private Point topLeft ;
	private String name ;
	private boolean isActive ;
	private boolean isSelected ;
	private final String hoverMessage ;
	private final Align alignment ;
	private final Dimension size ;
	private final Image image ;
	private final Image selectedImage ;
	private final ButtonFunction action ;
	
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 17) ;
	private static final Color textColor = Game.palette[0] ;
	private static final Color selectedTextColor = Game.palette[18] ;
	
	private static List<GameButton> all = new ArrayList<>() ;
		
	public GameButton(Point pos, Align alignment, String name, Image image, Image selectedImage, ButtonFunction action)
	{
		this.name = name ;
		this.image = image ;
		this.selectedImage = selectedImage ;
		this.hoverMessage = null ;
		this.isActive = true ;
		this.isSelected = false ;
		this.size = image != null ? Util.getSize(image) : new Dimension(100, 50) ;
		this.alignment = alignment ;
		this.topLeft = Util.getTopLeft(pos, alignment, size) ;
		this.action = action ;
		all.add(this) ;
	}
	
	public GameButton(Point pos, Align alignment, Image image, Image selectedImage, ButtonFunction action)
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

	public static boolean anyIsHovered(Point mousePos) { return all.stream().filter(button -> button.isActive() && button.ishovered(mousePos)).findAny().isPresent() ;}
	public static void updateSelected(Point mousePos)
	{
		for (GameButton button : all)
		{
			if (!button.isActive || !button.ishovered(mousePos)) { continue ;}

			all.forEach(GameButton::unSelect) ;
			button.select() ;
			return ;
		}
	}
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
	public static void actWhenClicked(Point mousePos, String action)
	{
		if (action == null) { return ;}

		for (int i = 0 ; i <= all.size() - 1; i += 1)
		{
			GameButton button = all.get(i) ;

			if (!button.isActive || !button.isSelected || (!button.isClicked(mousePos, action) && !action.equals("Enter"))) { continue ;}

			button.act() ;
			return ;
		}
	}
	public Point getCenter() {return Util.Translate(topLeft, size.width / 2, size.height / 2) ;}	
	public boolean ishovered(Point mousePos) { return Util.isInside(mousePos, topLeft, size) ;}
	public boolean isSelected() { return isSelected ;}
	public boolean isClicked(Point mousePos, String action)
	{
		if (mousePos == null) { return false ;}
		if (action == null) { return false ;}
		
		return ishovered(mousePos) & action.equals("LeftClick") ;
	}
	
	public void activate() { isActive = true ;}
	public void deactivate() { isActive = false ;}
	public void select() { isSelected = true ;}
	public void unSelect() { isSelected = false ;}
	public void activateAndSelect() { activate() ; select() ;}

	// TODO remover
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
		
		Image imageDisplayed = isSelected ? selectedImage : image ;
		
		if (imageDisplayed == null)
		{
			displayGeneralButton() ;
			return ;
		}
		
		GamePanel.DP.drawImage(imageDisplayed, topLeft, angle, Scale.unit, Align.topLeft) ;

		if (!displayText) { return ;}
		if (name == null) { return ;}
		
		Point textPos = isSelected ? Util.Translate(getCenter(), 0, 3) : getCenter() ;
		GamePanel.DP.drawText(textPos, Align.center, 0, name, font, textColor) ;
	}
	
	public void displayHoverMessage()
	{
		if (hoverMessage != null)
		{
			GamePanel.DP.drawGradRoundRect(new Point(topLeft.x + 20, topLeft.y - 10), alignment, size, 5, Game.palette[1], Game.palette[2], Game.palette[0], true) ;
			Draw.fitText(new Point(topLeft.x + 20, topLeft.y - 10), 14, alignment, hoverMessage, new Font(Game.MainFontName, Font.BOLD, 12), 20, Color.blue) ;
		}
	}

	
	@Override
	public String toString() {
		return "GameButton name:" + name + "";
	}


}