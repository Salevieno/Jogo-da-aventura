package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import attributes.BasicBattleAttribute;
import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import liveBeings.CreatureType;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class CreatureAttributesWindow extends AttributesWindow
{
	private final Point topLeftPos ;
	public CreatureAttributesWindow()
	{
		super(ImageLoader.loadImage(Path.WINDOWS_IMG + "CreatureAttWindow.png"), 1);
		this.topLeftPos = Screen.getMe().pos(0.4, 0.2) ;
	}

	public void display(CreatureType creatureType)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Align.topLeft) ;

		SpriteAnimation userImage = creatureType.getMovingAnimations().spriteIdle ;
		Point userPos = Util.translate(topLeftPos, size.width / 2, 60) ;
		userImage.display(GamePanel.getDP(), userPos, Align.center) ;

		Font namefont = new Font(Game.getMainFontName(), Font.BOLD, 13) ;
		Font font = new Font(Game.getMainFontName(), Font.BOLD, 11) ;		
		Color[] colorPalette = Palette.colors ;
		Color textColor = colorPalette[0] ;
		
		String[] attText = Game.getAllText().get(TextCategories.attributes) ;		
		Point namePos = Util.translate(topLeftPos, size.width / 2, 14) ;
		Point levelPos = Util.translate(topLeftPos, size.width / 2, 30) ;
		GamePanel.getDP().drawText(namePos, Align.center, creatureType.getName(), namefont, textColor) ;		
		GamePanel.getDP().drawText(levelPos, Align.center, attText[0] + ": " + creatureType.getLevel(), font, colorPalette[6]) ;
		
		
		// attributes
		Point lifePos = Util.translate(topLeftPos, 20, BORDER + PADDING + 37) ;
		Point mpPos = Util.translate(topLeftPos, 20, BORDER + PADDING + 37 + 26) ;
		String lifeText = attText[1] + ": " + Util.round(creatureType.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + Util.round(creatureType.getPA().getMp().getCurrentValue(), 1) ;
		GamePanel.getDP().drawText(lifePos, Align.centerLeft, lifeText, font, colorPalette[6]) ;
		GamePanel.getDP().drawText(mpPos, Align.centerLeft, mpText, font, colorPalette[5]) ;
				
		BasicBattleAttribute[] attributes = creatureType.getBA().basicAttributes() ;
		Point initialAttPos = Util.translate(topLeftPos, BORDER + PADDING + 34, 124) ;
		for (int i = 0; i <= ATT_ICONS.length - 1; i += 1)
		{
			Point attPos = Util.translate(initialAttPos, 117 * (i / 3), (i % 3) * 22) ;
			String attValue = Util.round(attributes[i].getBaseValue(), 1) + " + " + Util.round(attributes[i].getBonus(), 1) + " + " + Util.round(attributes[i].getTrain(), 1) ;
			
			GamePanel.getDP().drawImage(ATT_ICONS[i], Util.translate(attPos, -15, 0), Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(attPos, Align.centerLeft, attValue, font, textColor) ;
		}
		Point critPos = Util.translate(initialAttPos, 0, 71) ;
		String critValue = attText[9] + ": " + Util.round(100 * creatureType.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.getDP().drawImage(CRIT_ICON, Util.translate(initialAttPos, -15, 72), Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(critPos, Align.centerLeft, critValue, font, colorPalette[6]) ;
	}
}
