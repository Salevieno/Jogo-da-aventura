package UI ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import graphics.Align;
import main.Game;
import main.GamePanel;
import main.Music;
import main.Palette;
import utilities.Util;

public abstract class GameButton
{
	protected Point topLeft ;
	protected String name ;
	protected Dimension size ;
	protected boolean isActive ;
	protected boolean isSelected ;
	protected final ButtonFunction action ;
	protected final Image image ;
	protected final Image selectedImage ;
	protected final Align alignment ;
	protected final Clip soundEffectOnHover ;
	
	protected static final Font font = new Font(Game.getMainFontName(), Font.BOLD, 17) ;
	protected static final Color textColor = Palette.colors[0] ;
	protected static final Color selectedTextColor = Palette.colors[18] ;
	
	private static List<GameButton> all = new ArrayList<>() ;

	protected GameButton(Point pos, Align alignment, String name, Image image, Image selectedImage, ButtonFunction action, Clip soundEffectOnHover)
	{
		this.name = name ;
		this.image = image ;
		this.selectedImage = selectedImage ;
		this.soundEffectOnHover = soundEffectOnHover ;
		this.isActive = true ;
		this.isSelected = false ;
		this.alignment = alignment ;
		this.action = action ;
		all.add(this) ;
	}


	public Point getTopLeftPos() {return topLeft ;}
	public boolean isActive() { return isActive ;}
	public void setName(String newName) { name = newName ;}

	public static boolean anyIsHovered(Point mousePos) { return all.stream().filter(button -> button.isActive() && button.ishovered(mousePos)).findAny().isPresent() ;}
	public static void updateSelected(Point mousePos)
	{
		for (GameButton button : all)
		{
			if (button.isSelected || !button.isActive || !button.ishovered(mousePos)) { continue ;}

			all.forEach(GameButton::deSelect) ;
			if (button.soundEffectOnHover != null && Game.getSettings().getSoundEffectsAreOn())
			{
				Music.PlayMusic(button.soundEffectOnHover) ;
			}
			button.select() ;
			return ;
		}
	}
	public static void updateMouseCursor(Point mousePos)
	{
		if (GameButton.anyIsHovered(mousePos))
		{
			GamePanel.setCursorToHand() ;
		}
		else
		{
			GamePanel.setCursorToDefault() ;
		}
	}

	public Point getCenter() {return Util.translate(topLeft, size.width / 2, size.height / 2) ;}	
	public static List<GameButton> getAll() { return all ;}
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
	public void deSelect() { isSelected = false ;}
	public void activateAndSelect() { activate() ; select() ;}

	public void act() { action.act() ;}

	public abstract void display(double angle, boolean displayText, Point mousePos, Color textColor, double opacity) ;
	
	public void display(double angle, boolean displayText, Point mousePos)
	{
		display(angle, displayText, mousePos, textColor, 1.0) ;
	}
	
	@Override
	public String toString() {
		return "GameButton name:" + name + "";
	}
}