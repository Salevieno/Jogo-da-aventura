package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import attributes.BasicBattleAttribute;
import graphics.DrawingOnPanel;
import liveBeings.Creature;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class CreatureAttributesWindow extends AttributesWindow
{
	public CreatureAttributesWindow()
	{
		super(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "CreatureAttWindow.png"), 1);
	}

	public void display(Creature creature, DrawingOnPanel DP)
	{

		Dimension screenSize = Game.getScreen().getSize() ;
		Point windowPos = new Point((int) (0.6 * screenSize.width), (int)(0.2 * screenSize.height)) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, Align.topLeft) ;

		Image userImage = creature.getMovingAni().idleGif ;
		Point userPos = UtilG.Translate(windowPos, size.width / 2, 60) ;
		DP.DrawImage(userImage, userPos, Align.center) ;

		Font namefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;		
		Color[] colorPalette = Game.colorPalette ;
		Color textColor = colorPalette[9] ;
		
		String[] attText = Game.allText.get(TextCategories.attributes) ;		
		Point namePos = UtilG.Translate(windowPos, size.width / 2, 14) ;
		Point levelPos = UtilG.Translate(windowPos, size.width / 2, 30) ;
		DP.DrawText(namePos, Align.center, angle, creature.getName(), namefont, textColor) ;		
		DP.DrawText(levelPos, Align.center, angle, attText[0] + ": " + creature.getLevel(), font, colorPalette[6]) ;
		
		
		// attributes
		Point lifePos = UtilG.Translate(windowPos, 20, border + padding + 37) ;
		Point mpPos = UtilG.Translate(windowPos, 20, border + padding + 37 + 26) ;
		String lifeText = attText[1] + ": " + UtilG.Round(creature.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + UtilG.Round(creature.getPA().getMp().getCurrentValue(), 1) ;
		DP.DrawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[6]) ;
		DP.DrawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[5]) ;
				
		BasicBattleAttribute[] attributes = creature.getBA().basicAttributes() ;
		Point initialAttPos = UtilG.Translate(windowPos, border + padding + 34, 124) ;
		for (int i = 0; i <= attributes.length - 1; i += 1)
		{
			Point attPos = UtilG.Translate(initialAttPos, 117 * (i / 3), (i % 3) * 22) ;
			String attValue = UtilG.Round(attributes[i].getBaseValue(), 1) + " + " + UtilG.Round(attributes[i].getBonus(), 1) + " + " + UtilG.Round(attributes[i].getTrain(), 1) ;
			
			DP.DrawImage(attIcons[i], UtilG.Translate(attPos, -15, 0), new Scale(1, 1), Align.center) ;
			DP.DrawText(attPos, Align.centerLeft, angle, attValue, font, textColor) ;
		}
		Point critPos = UtilG.Translate(initialAttPos, 0, 71) ;
		String critValue = attText[9] + ": " + UtilG.Round(100 * creature.getBA().TotalCritAtkChance(), 1) + "%" ;
		DP.DrawImage(critIcon, UtilG.Translate(initialAttPos, -15, 72), new Scale(1, 1), Align.center) ;
		DP.DrawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		
		
		
	}
}
