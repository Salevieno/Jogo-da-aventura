package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import attributes.BasicBattleAttribute;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import liveBeings.Pet;
import main.Game;
import main.GamePanel;
import main.TextCategories;
import utilities.Elements;
import utilities.Util;
import utilities.UtilS;

public class PetAttributesWindow extends AttributesWindow
{
	private Pet pet ;

	Point windowPos = Game.getScreen().pos(0.52, 0.14) ;
	
	private static final Image image = UtilS.loadImage("\\Windows\\" + "PetAttWindow.png") ;
	
	public PetAttributesWindow()
	{
		super(image, 1);
	}

	public void setPet(Pet pet) { this.pet = pet ;}
	
	public void display(Point mousePos)
	{

		double angle = Draw.stdAngle ;
		
		GamePanel.DP.drawImage(image, windowPos, Align.topLeft, stdOpacity) ;

		Image userImage = pet.getMovingAni().idleGif ;
		Point userPos = Util.Translate(windowPos, size.width / 2, 73) ;
		GamePanel.DP.drawImage(userImage, userPos, Align.center) ;

		Font namefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;		
		Color[] colorPalette = Game.palette ;
		Color textColor = colorPalette[0] ;
		
		String[] attText = Game.allText.get(TextCategories.attributes) ;		
		Point namePos = Util.Translate(windowPos, size.width / 2, 18) ;
		Point levelPos = Util.Translate(windowPos, size.width / 2, 38) ;
		GamePanel.DP.drawText(namePos, Align.center, angle, pet.getName(), namefont, textColor) ;		
		GamePanel.DP.drawText(levelPos, Align.center, angle, attText[0] + ": " + pet.getLevel(), font, colorPalette[7]) ;
		
		//	Equips
		if (pet.getEquip() != null)
		{
			Point slotCenter = Util.Translate(windowPos, 222, 72) ;
			Dimension slotSize = new Dimension(51, 51) ;
			GamePanel.DP.drawImage(pet.getEquip().fullSizeImage(), slotCenter, Align.center) ;
			// TODO pegar elemento do equip
			Elements eqElem = pet.getAtkElem() ;
			if (eqElem != null)
			{

				Point elemPos = Util.Translate(slotCenter, slotSize.width - 12, slotSize.height / 2) ;
				GamePanel.DP.drawImage(eqElem.image, elemPos, angle, new Scale(0.12, 0.12), Align.center) ;
			}
		}
		
		
		// super element
//		if (pet.hasSuperElement())
//		{
//			Point superElemPos = Util.Translate(userPos, 0, 35) ;
//			Image superElemImage = pet.getElem()[4].image ;
//			GamePanel.DP.drawImage(superElemImage, superElemPos, angle, new Scale(0.3, 0.3), Align.center) ;
//		}
		
		
		// attributes
		Point lifePos = Util.Translate(windowPos, 20, border + padding + 46) ;
		Point mpPos = Util.Translate(windowPos, 20, border + padding + 46 + 27) ;
		String lifeText = attText[1] + ": " + Util.Round(pet.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + Util.Round(pet.getPA().getMp().getCurrentValue(), 1) ;
		GamePanel.DP.drawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[7]) ;
		GamePanel.DP.drawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[20]) ;
				
		BasicBattleAttribute[] attributes = pet.getBA().basicAttributes() ;
		Point initialAttPos = Util.Translate(windowPos, border + padding + 26, 136) ;
		for (int i = 0; i <= attIcons.length - 1; i += 1)
		{
			Point attPos = Util.Translate(initialAttPos, 134 * (i / 3), (i % 3) * 22) ;
			String attValue = Util.Round(attributes[i].getBaseValue(), 1) + " + " + Util.Round(attributes[i].getBonus(), 1) + " + " + Util.Round(attributes[i].getTrain(), 1) ;
			
			GamePanel.DP.drawImage(attIcons[i], Util.Translate(attPos, -15, 0), Scale.unit, Align.center) ;
			GamePanel.DP.drawText(attPos, Align.centerLeft, angle, attValue, font, textColor) ;
		}
		Point critPos = Util.Translate(initialAttPos, 0, 71) ;
		String critValue = attText[9] + ": " + Util.Round(100 * pet.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.DP.drawImage(critIcon, Util.Translate(initialAttPos, -15, 72), Scale.unit, Align.center) ;
		GamePanel.DP.drawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[7]) ;		
		
	}
}
