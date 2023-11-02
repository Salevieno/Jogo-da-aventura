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

public class SpellsBar
{	
	private static final Font titlefont = new Font("SansSerif", Font.BOLD, 10) ;
	private static final Font font = new Font("SansSerif", Font.BOLD, 9) ;
	private static final String title = Game.allText.get(TextCategories.spellsBar)[0] ;
	
	private static final Image image = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "SpellsBar.png") ;
	private static final Image slotImage = UtilG.loadImage(Game.ImagesPath + "\\SideBar\\" + "Slot.png") ;
	
	public static void display(int currentMP, List<Spell> activeSpells, Point mousePos, DrawingOnPanel DP)
	{
		Point barPos = new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70) ;
		Dimension barSize = new Dimension(image.getWidth(null), image.getHeight(null)) ;
		Dimension slotSize = new Dimension(slotImage.getWidth(null), slotImage.getHeight(null)) ;
		
		int nCols = Math.max(activeSpells.size() / 11 + 1, 1) ;
		int nRows = activeSpells.size() / nCols + 1 ;
		int sx = (int) UtilG.spacing(barSize.width, nCols, slotSize.width, 2) ;
		int sy = (int) UtilG.spacing(barSize.height - titlefont.getSize(), nRows, slotSize.height, 1) ;
		double angle = DrawingOnPanel.stdAngle ;	
		Color textColor = Game.colorPalette[3] ;
		DP.DrawImage(image, barPos, Align.bottomLeft) ;
		
		Point titlePos = new Point(barPos.x + barSize.width / 2, barPos.y - barSize.height + 2) ;
		DP.DrawText(titlePos, Align.topCenter, angle, title, titlefont, Game.colorPalette[5]) ;
		
		List<String> Keys = Player.SpellKeys ;
		Dimension imgSize = new Dimension(Spell.cooldownImage.getWidth(null), Spell.cooldownImage.getHeight(null)) ;
		for (int i = 0 ; i <= activeSpells.size() - 1 ; ++i)
		{
			Spell spell = activeSpells.get(i) ;
			if (0 < spell.getLevel())
			{
				int row = i % nRows ;
				int col = i / nRows ;
				Point slotCenter = UtilG.Translate(barPos, slotSize.width / 2 + col * sx + 2, - barSize.height + slotSize.height / 2 + row * sy + titlefont.getSize() + 1) ;
				Image image = currentMP < spell.getMpCost() ? slotImage : slotImage ;
				DP.DrawImage(image, slotCenter, Align.center) ;
				DP.DrawText(slotCenter, Align.center, angle, Keys.get(i), font, textColor) ;
				
				// display cooldown
				if (!spell.getCooldownCounter().finished())
				{
					Scale Imscale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
					Point cooldownImagePos = new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2);
					DP.DrawImage(Spell.cooldownImage, cooldownImagePos, angle, Imscale, Align.bottomLeft) ;
				}
				
				if (!UtilG.isInside(mousePos, new Point(slotCenter.x - imgSize.width / 2, slotCenter.y - imgSize.height / 2), imgSize)) { continue ;}
				
				DP.DrawText(new Point(slotCenter.x - slotSize.width - 10, slotCenter.y), Align.centerRight, angle, spell.getName(), titlefont, textColor) ;
			}
		}
	}
}