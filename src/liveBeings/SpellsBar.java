package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.DrawingOnPanel;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class SpellsBar
{	
	private static final int maxNumberRows ;
	private static final Font titlefont ;
	private static final Font font ;
	private static final String title ;
	private static final Color textColor ;

	private static final Point barPos ;
	private static final Image image ;
	private static final Image slotImage ;
	private static final Image slotImageNoMP ;
	private static final Dimension size ;
	private static final Dimension slotSize ;
	
	static
	{
		maxNumberRows = 8 ;
		titlefont = new Font("SansSerif", Font.BOLD, 10) ;
		font = new Font("SansSerif", Font.BOLD, 9) ;
		title = Game.allText.get(TextCategories.spellsBar)[0] ;
		textColor = Game.colorPalette[3] ;
		
		barPos = new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70) ;
		image = UtilS.loadImage("\\Player\\" + "SpellsBar.png") ;
		slotImage = UtilS.loadImage("\\SideBar\\" + "Slot.png") ;
		slotImageNoMP = UtilS.loadImage("\\SideBar\\" + "SlotNoMP.png") ;
		size = UtilG.getSize(image) ;
		slotSize = UtilG.getSize(slotImage) ;
	}
	
	public static void displayCooldown(Point slotCenter, Spell spell, DrawingOnPanel DP)
	{

		if (spell.getCooldownCounter().finished()) { return ;}
		
		Dimension imgSize = UtilG.getSize(Spell.cooldownImage) ;
		Scale scale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
		Point imgPos = new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2);
		DP.DrawImage(Spell.cooldownImage, imgPos, DrawingOnPanel.stdAngle, scale, Align.bottomLeft) ;
	
	}
	
	public static void display(int userMP, List<Spell> spells, Point mousePos, DrawingOnPanel DP)
	{
		// TODO calcs podem ir pra outra função
		Point grid = UtilG.calcGrid(spells.size(), maxNumberRows) ;
		int nRows = grid.x ;
		int nCols = grid.y ;

		Point offset = new Point(slotSize.width / 2, slotSize.height / 2 + 15) ;
		int sx = (int) UtilG.spacing(size.width, nCols, slotSize.width, 0) ;
		int sy = (int) UtilG.spacing(size.height, nRows, slotSize.height, 15) ;
		double angle = DrawingOnPanel.stdAngle ;	
		
		DP.DrawImage(image, barPos, Align.bottomLeft) ;
		
		Point titlePos = new Point(barPos.x + size.width / 2, barPos.y - size.height + 2) ;
		DP.DrawText(titlePos, Align.topCenter, angle, title, titlefont, Game.colorPalette[5]) ;
		
		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			Spell spell = spells.get(i) ;
			if (spell.getLevel() <= 0) { continue ;}
			
			int row = i % nRows ;
			int col = i / nRows ;
			Point slotCenter = UtilG.Translate(barPos, offset.x + col * sx, - size.height + offset.y + row * sy) ;
			Image image = spell.getMpCost() < userMP ? slotImage : slotImageNoMP ;
			DP.DrawImage(image, slotCenter, Align.center) ;
			DP.DrawText(slotCenter, Align.center, angle, Player.SpellKeys.get(i), font, textColor) ;
			
			displayCooldown(slotCenter, spell, DP) ;

			Point slotTopLeft = UtilG.getTopLeft(slotCenter, Align.center, slotSize) ;
			if (!UtilG.isInside(mousePos, slotTopLeft, slotSize)) { continue ;}
			
			Point textPos = new Point(slotCenter.x - slotSize.width - 10, slotCenter.y) ;
			DP.DrawText(textPos, Align.centerRight, angle, spell.getName(), titlefont, textColor) ;
		
		}
	}
}