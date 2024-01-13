package components ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Draw;
import graphics.DrawPrimitives;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

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
	private static Color textColor = Game.colorPalette[0] ;
	private static Color selectedTextColor = Game.colorPalette[18] ;
	
	public static int selectedIconID ;
	public static List<GameButton> allButtons = new ArrayList<>() ;	// isn't it insane to create a list of all items of a class inside the class itself?
	
	public GameButton(Point pos, Align alignment, Image image, Image selectedImage, IconFunction action)
	{
		name = "" ;
		this.image = image ;
		this.selectedImage = selectedImage ;
		isActive = true ;
		size = image != null ? UtilG.getSize(image) : new Dimension(100, 50) ;
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
		size = image != null ? UtilG.getSize(image) : new Dimension(100, 50) ;
		this.alignment = alignment ;
		this.topLeft = UtilG.getTopLeft(pos, alignment, size) ;
		this.action = action ;
	}

	public String getName() {return name ;}
	public Point getTopLeftPos() {return topLeft ;}
	public boolean isActive() { return isActive ;}
	public Image getImage() {return image ;}
	public Image getSelectedImage() {return selectedImage ;}
	public void setTopLeftPos(Point P) {topLeft = P ;}
	
	public Point getCenter() {return UtilG.Translate(topLeft, size.width / 2, size.height / 2) ;}
	
	public static void addToAllIconsList(GameButton icon) { allButtons.add(icon) ;}

	public boolean ishovered(Point mousePos) { return UtilG.isInside(mousePos, topLeft, size) ;}
	public boolean isClicked(Point mousePos, String action)
	{
		if (action == null) { return false ;}
		
		return ishovered(mousePos) & action.equals("LeftClick") ;
	}
	
	public void activate() { isActive = true ;}
	public void deactivate() { isActive = false ;}
	public void act() { action.act() ;}
	
	
	public void displayGeneralButton(DrawPrimitives DP)
	{
		DP.drawGradRoundRect(topLeft, Align.topLeft, size, 5, Game.colorPalette[3], Game.colorPalette[3], true) ;
		DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
	}
	
	public void displayHovered(double angle, boolean displayText, DrawPrimitives DP)
	{
		if (selectedImage != null)
		{
			DP.drawImage(selectedImage, topLeft, angle, Scale.unit, Align.topLeft) ;
			if (displayText)
			{
				DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
			}
		}
		else
		{
			DP.drawGradRoundRect(topLeft, Align.topLeft, size, 5, Game.colorPalette[5], Game.colorPalette[6], true) ;
			DP.drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
		}
	}
	
	public void displayNotHovered(double angle, boolean displayText, DrawPrimitives DP)
	{
		if (image != null)
		{
			DP.drawImage(image, topLeft, angle, Scale.unit, Align.topLeft) ;
			
			if (!displayText) { return ;}
			
			DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
			return ;
		}
		
		DP.drawGradRoundRect(topLeft, Align.topLeft, size, 2, Game.colorPalette[5], Game.colorPalette[6], true) ;
		DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
	}
	
	public void display(double angle, boolean displayText, Point mousePos, DrawPrimitives DP)
	{
		
		if (!isActive) { return ;}
		
		Image imageDisplayed = ishovered(mousePos) ? selectedImage : image ;
		
		if (imageDisplayed == null)
		{
			displayGeneralButton(DP) ;
			return ;
		}
		
		DP.drawImage(imageDisplayed, topLeft, angle, Scale.unit, Align.topLeft) ;

		if (!displayText) { return ;}
		if (name == null) { return ;}
		
		DP.drawText(getCenter(), Align.center, 0, name, font, textColor) ;
		
	}
	
	public void displayHoverMessage(DrawPrimitives DP)
	{
		if (description != null)
		{
			DP.drawGradRoundRect(new Point(topLeft.x + 20, topLeft.y - 10), alignment, size, 5, Color.lightGray, Color.gray, true) ;
			Draw.fitText(new Point(topLeft.x + 20, topLeft.y - 10), 14, alignment, description, new Font(Game.MainFontName, Font.BOLD, 12), 20, Color.blue) ;
		}
	}

	
	@Override
	public String toString() {
		return "GameButton [name=" + name + "]";
	}


}