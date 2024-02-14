package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import attributes.BasicBattleAttribute;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.Equip;
import liveBeings.Pet;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class PetAttributesWindow extends AttributesWindow
{
	private Pet pet ;
	
	public PetAttributesWindow()
	{
		super(UtilS.loadImage("\\Windows\\" + "PetAttWindow.png"), 1);
	}

	public void setPet(Pet pet) { this.pet = pet ;}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{

		Dimension screenSize = Game.getScreen().getSize() ;
		Point windowPos = new Point((int) (0.5 * screenSize.width), (int)(0.2 * screenSize.height)) ;
		double angle = Draw.stdAngle ;
		
		DP.drawImage(image, windowPos, Align.topLeft) ;

		Image userImage = pet.getMovingAni().idleGif ;
		Point userPos = UtilG.Translate(windowPos, size.width / 2, 60) ;
		DP.drawImage(userImage, userPos, Align.center) ;

		Font namefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;		
		Color[] colorPalette = Game.colorPalette ;
		Color textColor = colorPalette[21] ;
		
		String[] attText = Game.allText.get(TextCategories.attributes) ;		
		Point namePos = UtilG.Translate(windowPos, size.width / 2, 14) ;
		Point levelPos = UtilG.Translate(windowPos, size.width / 2, 30) ;
		DP.drawText(namePos, Align.center, angle, pet.getName(), namefont, textColor) ;		
		DP.drawText(levelPos, Align.center, angle, attText[0] + ": " + pet.getLevel(), font, colorPalette[6]) ;
		
		//	Equips
		if (pet.getEquip() != null)
		{
			Point slotCenter = UtilG.Translate(windowPos, 190, 62) ;
			Dimension slotSize = new Dimension(51, 51) ;
			DP.drawImage(Equip.ArmorImage, slotCenter, Align.center) ;
			Elements eqElem = pet.getElem()[0] ;
			if (eqElem != null)
			{

				Point elemPos = UtilG.Translate(slotCenter, slotSize.width - 12, slotSize.height / 2) ;
				DP.drawImage(eqElem.image, elemPos, angle, new Scale(0.12, 0.12), Align.center) ;
			}
		}
		
		
		// super element
		if (pet.hasSuperElement())
		{
			Point superElemPos = UtilG.Translate(userPos, 0, 35) ;
			Image superElemImage = pet.getElem()[4].image ;
			DP.drawImage(superElemImage, superElemPos, angle, new Scale(0.3, 0.3), Align.center) ;
		}
		
		
		// attributes
		Point lifePos = UtilG.Translate(windowPos, 20, border + padding + 37) ;
		Point mpPos = UtilG.Translate(windowPos, 20, border + padding + 37 + 26) ;
		String lifeText = attText[1] + ": " + UtilG.Round(pet.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + UtilG.Round(pet.getPA().getMp().getCurrentValue(), 1) ;
		DP.drawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[6]) ;
		DP.drawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[5]) ;
				
		BasicBattleAttribute[] attributes = pet.getBA().basicAttributes() ;
		Point initialAttPos = UtilG.Translate(windowPos, border + padding + 34, 124) ;
		for (int i = 0; i <= attIcons.length - 1; i += 1)
		{
			Point attPos = UtilG.Translate(initialAttPos, 117 * (i / 3), (i % 3) * 22) ;
			String attValue = UtilG.Round(attributes[i].getBaseValue(), 1) + " + " + UtilG.Round(attributes[i].getBonus(), 1) + " + " + UtilG.Round(attributes[i].getTrain(), 1) ;
			
			DP.drawImage(attIcons[i], UtilG.Translate(attPos, -15, 0), Scale.unit, Align.center) ;
			DP.drawText(attPos, Align.centerLeft, angle, attValue, font, textColor) ;
		}
		Point critPos = UtilG.Translate(initialAttPos, 0, 71) ;
		String critValue = attText[9] + ": " + UtilG.Round(100 * pet.getBA().TotalCritAtkChance(), 1) + "%" ;
		DP.drawImage(critIcon, UtilG.Translate(initialAttPos, -15, 72), Scale.unit, Align.center) ;
		DP.drawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		
		
	}
}
