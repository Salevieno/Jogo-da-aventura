package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import attributes.BasicBattleAttribute;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import liveBeings.CreatureType;
import main.Game;
import main.GamePanel;
import main.TextCategories;
import utilities.Util;
import utilities.UtilS;

public class CreatureAttributesWindow extends AttributesWindow
{

	private static final Point windowPos = Game.getScreen().pos(0.4, 0.2) ;
	private static final Image image = UtilS.loadImage("\\Windows\\" + "CreatureAttWindow.png") ;
	
	public CreatureAttributesWindow()
	{
		super(image, 1);
	}

	public void display(CreatureType creatureType)
	{

		double angle = Draw.stdAngle ;
		
		GamePanel.DP.drawImage(image, windowPos, Align.topLeft) ;

		Image userImage = creatureType.getMovingAnimations().idleGif ;
		Point userPos = Util.Translate(windowPos, size.width / 2, 60) ;
		GamePanel.DP.drawImage(userImage, userPos, Align.center) ;

		Font namefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;		
		Color[] colorPalette = Game.palette ;
		Color textColor = colorPalette[0] ;
		
		String[] attText = Game.allText.get(TextCategories.attributes) ;		
		Point namePos = Util.Translate(windowPos, size.width / 2, 14) ;
		Point levelPos = Util.Translate(windowPos, size.width / 2, 30) ;
		GamePanel.DP.drawText(namePos, Align.center, angle, creatureType.getName(), namefont, textColor) ;		
		GamePanel.DP.drawText(levelPos, Align.center, angle, attText[0] + ": " + creatureType.getLevel(), font, colorPalette[6]) ;
		
		
		// attributes
		Point lifePos = Util.Translate(windowPos, 20, border + padding + 37) ;
		Point mpPos = Util.Translate(windowPos, 20, border + padding + 37 + 26) ;
		String lifeText = attText[1] + ": " + Util.Round(creatureType.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + Util.Round(creatureType.getPA().getMp().getCurrentValue(), 1) ;
		GamePanel.DP.drawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[6]) ;
		GamePanel.DP.drawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[5]) ;
				
		BasicBattleAttribute[] attributes = creatureType.getBA().basicAttributes() ;
		Point initialAttPos = Util.Translate(windowPos, border + padding + 34, 124) ;
		for (int i = 0; i <= attIcons.length - 1; i += 1)
		{
			Point attPos = Util.Translate(initialAttPos, 117 * (i / 3), (i % 3) * 22) ;
			String attValue = Util.Round(attributes[i].getBaseValue(), 1) + " + " + Util.Round(attributes[i].getBonus(), 1) + " + " + Util.Round(attributes[i].getTrain(), 1) ;
			
			GamePanel.DP.drawImage(attIcons[i], Util.Translate(attPos, -15, 0), Scale.unit, Align.center) ;
			GamePanel.DP.drawText(attPos, Align.centerLeft, angle, attValue, font, textColor) ;
		}
		Point critPos = Util.Translate(initialAttPos, 0, 71) ;
		String critValue = attText[9] + ": " + Util.Round(100 * creatureType.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.DP.drawImage(critIcon, Util.Translate(initialAttPos, -15, 72), Scale.unit, Align.center) ;
		GamePanel.DP.drawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		
		
		
	}
}
