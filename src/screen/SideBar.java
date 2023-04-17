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
	private Set<GameButton> icons ;
		
	public SideBar(Image playerImage, Image petImage)
	{
    	String path = Game.ImagesPath + "\\SideBar\\";
		Point botCenterPos = new Point(Game.getScreen().getSize().width + 20, Game.getScreen().getSize().height - 220) ;
		icons = new HashSet<>() ;
		String[] names = new String[] {"settings", "bag", "quest", "map", "book", "tent"} ; // "player", "pet"
		for (int i = 0; i <= names.length - 1 ; i += 1)
		{
			Point pos = UtilG.Translate(botCenterPos, 0, -10 - 50 * i) ;
			Image image = UtilG.loadImage(path + "Icon" + i + "_" + names[i] + ".png") ;
			Image selImage = UtilG.loadImage(path + "Icon" + i + "_" + names[i] + "Selected.png") ;
			icons.add(new GameButton(i, names[i], pos, "description", image, selImage)) ;
		}
		icons.forEach(GameButton::activate);
	}
	
	public Set<GameButton> getIcons() { return icons ;}
	public void setIcons(Set<GameButton> icons) { this.icons = icons ;}
		
	public void display(Player player, Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		
		Point barPos = new Point(Game.getScreen().getSize().width, Game.getScreen().getSize().height);
		Dimension size = new Dimension(40, Game.getScreen().getSize().height) ;
		double stdAngle = DrawingOnPanel.stdAngle ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] IconKey = new String[] {null, Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7], null, null, null, null} ;
		Color textColor = Game.ColorPalette[7] ;
		
		DP.DrawRect(barPos, Align.bottomLeft, size, 1, Game.ColorPalette[9], null) ;		
		
		icons.forEach(icon -> {
			icon.display(stdAngle, Align.bottomCenter, mousePos, DP) ;
			if (IconKey[icon.getid()] != null)
			{
				DP.DrawText(icon.getPos(), Align.bottomLeft, stdAngle, IconKey[icon.getid()], font, textColor) ;
			}
		});
		
		// TODO which spells to pass to spellsBar
		SpellsBar.display(player.getMp().getCurrentValue(), player.getSpells(), mousePos, DP);
		HotKeysBar.display(player.getHotItems(), mousePos, DP) ;

	}
	
	
}
