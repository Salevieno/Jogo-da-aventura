package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import components.GameButton;
import graphics.DrawingOnPanel;
import liveBeings.HotKeysBar;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.SpellsBar;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class SideBar
{
	private Set<GameButton> buttons ;
	
	private Point barPos = new Point(Game.getScreen().getSize().width, Game.getScreen().getSize().height);	
	private Dimension size = new Dimension(40, Game.getScreen().getSize().height) ;
		
	public SideBar(Image playerImage, Image petImage)
	{
		
		Point topLeftPos = new Point(Game.getScreen().getSize().width + 20, Game.getScreen().getSize().height - 220) ;
		buttons = new HashSet<>() ;
		String[] names = new String[] {"settings", "bag", "quest", "map", "book", "tent"} ; // "player", "pet"

    	String path = Game.ImagesPath + "\\SideBar\\";
    	int offsetY = 0 ;
		for (int i = 0; i <= names.length - 1 ; i += 1)
		{			
			Image image = UtilG.loadImage(path + "Icon" + i + "_" + names[i] + ".png") ;
			Image selImage = UtilG.loadImage(path + "Icon" + i + "_" + names[i] + "Selected.png") ;
			Point botCenterPos = UtilG.Translate(topLeftPos, 0, - offsetY) ;
			buttons.add(new GameButton(i, names[i], botCenterPos, "", image, selImage)) ;

			offsetY += image.getHeight(null) + 10 ;
		}
		
		buttons.forEach(GameButton::activate);
		
	}
	
	public Set<GameButton> getButtons() { return buttons ;}
	public void setButtons(Set<GameButton> buttons) { this.buttons = buttons ;}
		
	public void display(Player player, Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		
		double stdAngle = DrawingOnPanel.stdAngle ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] IconKey = new String[] {null, Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7], null, null, null, null} ;
		Color textColor = Game.colorPalette[7] ;
		
		DP.DrawRect(barPos, Align.bottomLeft, size, 1, Game.colorPalette[9], null) ;
		
		buttons.forEach(button -> button.display(stdAngle, Align.bottomCenter, false, mousePos, DP)) ;
		buttons.forEach(button -> DP.DrawText(button.getPos(), Align.topLeft, stdAngle, IconKey[button.getid()] != null ? IconKey[button.getid()] : "", font, textColor)) ;
		
		// TODO which spells to pass to spellsBar
		SpellsBar.display(player.getMp().getCurrentValue(), player.getSpells(), mousePos, DP);
		HotKeysBar.display(player.getHotItems(), mousePos, DP) ;

	}
	
	
}
