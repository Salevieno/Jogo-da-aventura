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
import main.GamePanel;
import main.Path;
import screen.SideBar;
import utilities.Util;
import graphics.UtilAlignment;


public abstract class SpellsBar
{	
	private static final int maxNumberRows ;
	private static final Font largefont ;
	private static final Font font ;
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
		largefont = new Font("SansSerif", Font.BOLD, 14) ;
		font = new Font("SansSerif", Font.BOLD, 12) ;
		textColor = Game.palette[4] ;

		image = Game.loadImage(Path.SIDEBAR_IMG + "SpellsBar.png") ;
		spellIcon = Game.loadImage(Path.SIDEBAR_IMG + "SpellIcon.png") ;
		slotImageNoMP = Game.loadImage(Path.SIDEBAR_IMG + "SlotNoMP.png") ;
		cooldownImage = Game.loadImage(Path.SIDEBAR_IMG + "Cooldown.png") ;
		size = Util.getSize(image) ;
		barPos = new Point(Game.getScreen().mapSize().width + 6, HotKeysBar.topLeft().y - SideBar.sy) ;
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

		if (spell.getCooldownCounter().hasFinished()) { return ;}
		
		Dimension imgSize = Util.getSize(cooldownImage) ;
		Scale scale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
		Point imgPos = new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2);
		GamePanel.DP.drawImage(cooldownImage, imgPos, Draw.stdAngle, scale, Align.bottomLeft) ;
	
	}
	
	public static void display(int userMP, Point mousePos)
	{
		Dimension slotSize = Util.getSize(SideBar.slotImage) ;
		Point offset = new Point(4, 13) ;
		int sx = (int) Util.spacing(size.width, nCols, slotSize.width, offset.x) ;
		int sy = (int) Util.spacing(size.height, nRows, slotSize.height, offset.y) ;
		
		GamePanel.DP.drawImage(image, barPos, Align.bottomLeft) ;
		GamePanel.DP.drawImage(spellIcon, titlePos, Align.topCenter);

		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			Spell spell = spells.get(i) ;
			if (spell.getLevel() <= 0) { continue ;}
			
			int row = i % nRows ;
			int col = i / nRows ;
			Point slotCenter = Util.translate(barPos, offset.x + slotSize.width / 2 + col * sx, - size.height + slotSize.height / 2 + 8 + offset.y + row * sy) ;
			Image image = spell.getMpCost() < userMP ? SideBar.slotImage : slotImageNoMP ;
			GamePanel.DP.drawImage(image, slotCenter, Align.center) ;
			GamePanel.DP.drawText(slotCenter, Align.center, Draw.stdAngle, Player.spellKeys.get(i), font, textColor) ;
			
			displayCooldown(slotCenter, spell) ;

			Point slotTopLeft = UtilAlignment.getTopLeft(slotCenter, Align.center, slotSize) ;

			if (!Util.isInside(mousePos, slotTopLeft, slotSize)) { continue ;}
			
			Point textPos = new Point(slotCenter.x - slotSize.width, slotCenter.y) ;
			Draw.bufferedText(textPos, Align.centerRight, 0.0, spell.getName(), largefont, textColor, Game.palette[3], 2) ;
		
		}
	}
}