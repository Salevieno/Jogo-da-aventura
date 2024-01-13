package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Draw;
import graphics.DrawPrimitives;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public abstract class SpellsBar
{	
	private static final int maxNumberRows ;
	private static final Font titlefont ;
	private static final Font font ;
	private static final String title ;
	private static final Color textColor ;

	private static final Image image ;
	private static final Image slotImage ;
	private static final Image slotImageNoMP ;
	private static final Image cooldownImage ;	
	private static final Dimension size ;
	private static final Dimension slotSize ;
	private static final Point barPos ;
	private static final Point titlePos ;
	
	private static List<Spell> spells ;
	private static int nRows ;
	private static int nCols ;
	
	static
	{
		maxNumberRows = 8 ;
		titlefont = new Font("SansSerif", Font.BOLD, 10) ;
		font = new Font("SansSerif", Font.BOLD, 9) ;
		title = Game.allText.get(TextCategories.spellsBar)[0] ;
		textColor = Game.colorPalette[3] ;

		image = UtilS.loadImage("\\SideBar\\" + "SpellsBar.png") ;
		slotImage = UtilS.loadImage("\\SideBar\\" + "Slot.png") ;
		slotImageNoMP = UtilS.loadImage("\\SideBar\\" + "SlotNoMP.png") ;
		cooldownImage = UtilS.loadImage("\\SideBar\\" + "Cooldown.png") ;
		size = UtilG.getSize(image) ;
		slotSize = UtilG.getSize(slotImage) ;
		barPos = new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70) ;
		titlePos = new Point(barPos.x + size.width / 2, barPos.y - size.height + 2) ;
	}
	
	public static void updateSpells(List<Spell> newSpells)
	{
		spells = newSpells ;
		Point grid = UtilG.calcGrid(spells.size(), maxNumberRows) ;
		nRows = grid.x ;
		nCols = grid.y ;
	}
	
	public static void displayCooldown(Point slotCenter, Spell spell, DrawPrimitives DP)
	{

		if (spell.getCooldownCounter().finished()) { return ;}
		
		Dimension imgSize = UtilG.getSize(cooldownImage) ;
		Scale scale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
		Point imgPos = new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2);
		DP.drawImage(cooldownImage, imgPos, Draw.stdAngle, scale, Align.bottomLeft) ;
	
	}
	
	public static void display(int userMP, List<Spell> spells, Point mousePos, DrawPrimitives DP)
	{

		Point offset = new Point(slotSize.width / 2, slotSize.height / 2 + 15) ;
		int sx = (int) UtilG.spacing(size.width, nCols, slotSize.width, 0) ;
		int sy = (int) UtilG.spacing(size.height, nRows, slotSize.height, 15) ;
		double angle = Draw.stdAngle ;	
		
		DP.drawImage(image, barPos, Align.bottomLeft) ;
		
		DP.drawText(titlePos, Align.topCenter, angle, title, titlefont, Game.colorPalette[5]) ;
		
		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			Spell spell = spells.get(i) ;
			if (spell.getLevel() <= 0) { continue ;}
			
			int row = i % nRows ;
			int col = i / nRows ;
			Point slotCenter = UtilG.Translate(barPos, offset.x + col * sx, - size.height + offset.y + row * sy) ;
			Image image = spell.getMpCost() < userMP ? slotImage : slotImageNoMP ;
			DP.drawImage(image, slotCenter, Align.center) ;
			DP.drawText(slotCenter, Align.center, angle, Player.SpellKeys.get(i), font, textColor) ;
			
			displayCooldown(slotCenter, spell, DP) ;

			Point slotTopLeft = UtilG.getTopLeft(slotCenter, Align.center, slotSize) ;
			if (!UtilG.isInside(mousePos, slotTopLeft, slotSize)) { continue ;}
			
			Point textPos = new Point(slotCenter.x - slotSize.width - 10, slotCenter.y) ;
			DP.drawText(textPos, Align.centerRight, angle, spell.getName(), titlefont, textColor) ;
		
		}
	}
}