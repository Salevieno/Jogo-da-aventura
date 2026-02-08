package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.Path;
import screen.SideBar;
import utilities.Util;


public abstract class SpellsBar
{	
	private static final int maxNumberRows ;
	private static final Font largefont ;
	private static final Color textColor ;

	private static final Point barPos ;
	private static final Image barImage ;
	private static final Dimension barSize ;
	private static final Image slotImageNoMP ;
	private static final Dimension slotSize ;
	private static final Point slotsOffset ;
	private static final Image cooldownImage ;
	
	private static List<Spell> spells ;
	private static int nRows ;
	private static int nCols ;
	
	static
	{
		maxNumberRows = 8 ;
		largefont = new Font("SansSerif", Font.BOLD, 14) ;
		textColor = Game.palette[4] ;

		barPos = new Point(Game.getScreen().mapSize().width + 2, HotKeysBar.topLeft().y - SideBar.sy) ;
		barImage = Game.loadImage(Path.SIDEBAR_IMG + "SpellsBar.png") ;
		barSize = Util.getSize(barImage) ;
		slotImageNoMP = Game.loadImage(Path.SIDEBAR_IMG + "SlotNoMP.png") ;
		slotSize = Util.getSize(SideBar.slotImage) ;
		slotsOffset = new Point(3, 4) ;
		cooldownImage = Game.loadImage(Path.SIDEBAR_IMG + "Cooldown.png") ;
	}
	
	public static void updateSpells(List<Spell> newSpells)
	{
		spells = newSpells ;
		nCols = Util.calcGridNumberColumns(spells.size(), maxNumberRows) ;
		nRows = maxNumberRows ;
	}
	
	private static void displayCooldown(Point slotCenter, Spell spell)
	{

		if (spell.getCooldownCounter().hasFinished()) { return ;}
		
		Dimension imgSize = Util.getSize(cooldownImage) ;
		Scale scale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
		Point imgPos = new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2);
		GamePanel.DP.drawImage(cooldownImage, imgPos, Draw.stdAngle, scale, Align.bottomLeft) ;
	
	}

	private static void displaySpellName(int slotCenterY, String spellName)
	{
		Point textPos = new Point(barPos.x - 30, slotCenterY) ;
		Draw.bufferedText(textPos, Align.centerRight, 0.0, spellName, largefont, textColor, Game.palette[3], 2) ;
	}
	
	public static void display(int userMP, Point mousePos)
	{
		int sx = (int) Util.spacing(barSize.width, nCols, slotSize.width, slotsOffset.x) ;
		int sy = (int) Util.spacing(barSize.height, nRows, slotSize.height, slotsOffset.y) ;

		GamePanel.DP.drawImage(barImage, barPos, Align.bottomLeft) ;

		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			Spell spell = spells.get(i) ;
			if (spell.getLevel() <= 0) { continue ;}
			
			int row = i / nCols ;
			int col = i % nCols ;
			Point slotCenter = Util.translate(barPos, slotsOffset.x + slotSize.width / 2 + col * sx, - barSize.height + slotSize.height / 2 + slotsOffset.y + row * sy) ;
			Point slotTopLeft = UtilAlignment.getTopLeft(slotCenter, Align.center, slotSize) ;
			Image image = spell.getMpCost() < userMP ? SideBar.slotImage : slotImageNoMP ;
			GamePanel.DP.drawImage(image, slotCenter, Align.center, 0.8) ;
			GamePanel.DP.drawImage(spell.getImage(), slotCenter, Align.center) ;
			Draw.keyboardKey(slotTopLeft, Player.spellKeys.get(i), Game.palette[0]);
			
			displayCooldown(slotCenter, spell) ;

			if (!Util.isInside(mousePos, slotTopLeft, slotSize)) { continue ;}
			
			displaySpellName(slotCenter.y, spell.getName()) ;		
		}
	}
}