package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import main.Game;
import main.TextCategories;
import screen.SideBar;
import utilities.Util;
import utilities.UtilS;

public abstract class SpellsBar
{	
	private static final int maxNumberRows ;
	private static final Font titlefont ;
	private static final Font font ;
	private static final String title ;
	private static final Color textColor ;

	private static final Image image ;
	private static final Image spellIcon ;
	private static final Image slotImageNoMP ;
	private static final Image cooldownImage ;	
	private static final Dimension size ;
	private static final Point barPos ;
	private static final Point titlePos ;
	
	private static List<Spell> spells ;
	private static int nRows ;
	private static int nCols ;
	
	static
	{
		maxNumberRows = 8 ;
		titlefont = new Font("SansSerif", Font.BOLD, 9) ;
		font = new Font("SansSerif", Font.BOLD, 9) ;
		title = Game.allText.get(TextCategories.spellsBar)[0] ;
		textColor = Game.palette[19] ;

		image = UtilS.loadImage("\\SideBar\\" + "SpellsBar.png") ;
		spellIcon = UtilS.loadImage("\\SideBar\\" + "SpellIcon.png") ;
		slotImageNoMP = UtilS.loadImage("\\SideBar\\" + "SlotNoMP.png") ;
		cooldownImage = UtilS.loadImage("\\SideBar\\" + "Cooldown.png") ;
		size = Util.getSize(image) ;
		barPos = new Point(Game.getScreen().getSize().width + 2, Game.getScreen().getSize().height - 80) ;
		titlePos = new Point(barPos.x + size.width / 2, barPos.y - size.height + 2) ;
	}
	
	public static void updateSpells(List<Spell> newSpells)
	{
		spells = newSpells ;
		nCols = Util.calcGridNumberColumns(spells.size(), maxNumberRows) ;
		nRows = maxNumberRows ;
	}
	
	public static void displayCooldown(Point slotCenter, Spell spell)
	{

		if (spell.getCooldownCounter().finished()) { return ;}
		
		Dimension imgSize = Util.getSize(cooldownImage) ;
		Scale scale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
		Point imgPos = new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2);
		Game.DP.drawImage(cooldownImage, imgPos, Draw.stdAngle, scale, Align.bottomLeft) ;
	
	}
	
	public static void display(int userMP, Point mousePos)
	{
		Dimension slotSize = Util.getSize(SideBar.slotImage) ;
		Point offset = new Point(4, 7) ;
		int sx = (int) Util.spacing(size.width, nCols, slotSize.width, offset.x) ;
		int sy = (int) Util.spacing(size.height, nRows, slotSize.height, offset.y) ;
		
		Game.DP.drawImage(image, barPos, Align.bottomLeft) ;		
		Game.DP.drawImage(spellIcon, titlePos, Align.topCenter);

		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			Spell spell = spells.get(i) ;
			if (spell.getLevel() <= 0) { continue ;}
			
			int row = i % nRows ;
			int col = i / nRows ;
			Point slotCenter = Util.Translate(barPos, offset.x + slotSize.width / 2 + col * sx, - size.height + slotSize.height / 2 + 8 + offset.y + row * sy) ;
			Image image = spell.getMpCost() < userMP ? SideBar.slotImage : slotImageNoMP ;
			Game.DP.drawImage(image, slotCenter, Align.center) ;
			Game.DP.drawText(slotCenter, Align.center, Draw.stdAngle, Player.SpellKeys.get(i), font, textColor) ;
			
			displayCooldown(slotCenter, spell) ;

			Point slotTopLeft = Util.getTopLeft(slotCenter, Align.center, slotSize) ;
			if (!Util.isInside(mousePos, slotTopLeft, slotSize)) { continue ;}
			
			Point textPos = new Point(slotCenter.x - slotSize.width, slotCenter.y) ;
			Draw.bufferedText(textPos, Align.centerRight, spell.getName(), titlefont, textColor) ;
		
		}
	}
}