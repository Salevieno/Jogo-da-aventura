package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.DrawingOnPanel;
import main.Game;
import screen.Screen;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class SpellsBar
{	
	
	private static final Image image = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "SpellsBar.png") ;
	public static final Image slotImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSlot.png") ;
	
	public static void display(int currentMP, List<Spell> spells, List<Spell> ActiveSpells, Point mousePos, Color BGcolor, Color TextColor, DrawingOnPanel DP)
	{
		Font Titlefont = new Font("SansSerif", Font.BOLD, 10) ;
		Font font = new Font("SansSerif", Font.BOLD, 9) ;
		Screen screen = Game.getScreen() ;
		Color[] colorPalette = Game.ColorPalette ;
		double OverallAngle = DrawingOnPanel.stdAngle ;
		Point Pos = new Point(screen.getSize().width + 1, (int) (0.99 * screen.getSize().height) - 70) ;
		//List<Spell> ActiveSpells = player.GetActiveSpells() ;
		Dimension size = new Dimension(36, 130) ;
		int slotW = slotImage.getWidth(null), slotH = slotImage.getHeight(null) ;	// Bar size
		int Ncols = Math.max(ActiveSpells.size() / 11 + 1, 1) ;
		int Nrows = ActiveSpells.size() / Ncols + 1 ;
		int Sx = (int) UtilG.spacing(size.width, Ncols, slotW, 3), Sy = (int) UtilG.spacing(size.height, Nrows, slotH, 5) ;		
		List<String> Keys = Player.SpellKeys ;
		
		//Color BGcolor = Player.ClassColors[player.getJob()] ;
		//Color TextColor = player.getColor() ;
		
//		DP.DrawRoundRect(Pos, Align.bottomLeft, size, 1, colorPalette[7], BGcolor, true) ;
		DP.DrawImage(image, Pos, Align.bottomLeft) ;
		DP.DrawText(new Point(Pos.x + size.width / 2, Pos.y - size.height + 3), Align.topCenter, OverallAngle, Game.allText.get("Barra de habilidades")[0], Titlefont, colorPalette[5]) ;
		for (int i = 0 ; i <= ActiveSpells.size() - 1 ; ++i)
		{
			Spell spell = spells.get(i) ;
			if (0 < spell.getLevel())
			{
				Point slotCenter = new Point(Pos.x + slotW / 2 + (i / Nrows) * Sx + 3, Pos.y - size.height + slotH / 2 + (i % Nrows) * Sy + Titlefont.getSize() + 5) ;
				if (currentMP < spell.getMpCost())
				{
					DP.DrawImage(slotImage, slotCenter, Align.center) ;
				}
				else
				{
					DP.DrawImage(slotImage, slotCenter, Align.center) ;
				}
				DP.DrawText(slotCenter, Align.center, OverallAngle, Keys.get(i), font, TextColor) ;
				Dimension imgSize = new Dimension(Spell.cooldownImage.getWidth(null), Spell.cooldownImage.getHeight(null)) ;
				if (!spell.getCooldownCounter().finished())
				{
					Scale Imscale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
					DP.DrawImage(Spell.cooldownImage, new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2), OverallAngle, Imscale, Align.bottomLeft) ;
				}
				if (UtilG.isInside(mousePos, new Point(slotCenter.x - imgSize.width / 2, slotCenter.y - imgSize.height / 2), imgSize))
				{
					DP.DrawText(new Point(slotCenter.x - imgSize.width - 10, slotCenter.y), Align.centerRight, OverallAngle, spell.getName(), Titlefont, TextColor) ;
				}
			}
		}
	}
}
