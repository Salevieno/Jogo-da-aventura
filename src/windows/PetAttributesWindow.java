package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import attributes.BasicBattleAttribute;
import graphics.DrawingOnPanel;
import liveBeings.Pet;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;

public class PetAttributesWindow extends AttributesWindow
{
	private Pet pet ;
	
	public PetAttributesWindow()
	{
		super(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PetAttWindow.png"), 1);
	}

	public void setPet(Pet pet) { this.pet = pet ;}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{

		Dimension screenSize = Game.getScreen().getSize() ;
		Point windowPos = new Point((int) (0.5 * screenSize.width), (int)(0.2 * screenSize.height)) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, Align.topLeft) ;

		Image userImage = pet.getMovingAni().idleGif ;
		Point userPos = UtilG.Translate(windowPos, size.width / 2, 60) ;
		DP.DrawImage(userImage, userPos, Align.center) ;

		Font namefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;		
		Color[] colorPalette = Game.colorPalette ;
		Color textColor = colorPalette[21] ;
		
		String[] attText = Game.allText.get(TextCategories.attributes) ;		
		Point namePos = UtilG.Translate(windowPos, size.width / 2, 14) ;
		Point levelPos = UtilG.Translate(windowPos, size.width / 2, 30) ;
		DP.DrawText(namePos, Align.center, angle, pet.getName(), namefont, textColor) ;		
		DP.DrawText(levelPos, Align.center, angle, attText[0] + ": " + pet.getLevel(), font, colorPalette[6]) ;
		
		
		// TODO pet equips
		//	Equips
//		if (pet.equips != null)
//		{
//			int[] eqSlotW = new int[] {51, 51, 51, 8} ;
//			int[] eqSlotH = new int[] {51, 51, 51, 24} ;
//			Point[] eqSlotCenter = new Point[] {UtilG.Translate(windowPos, 66, 110),
//					UtilG.Translate(windowPos, 244, 62),
//					UtilG.Translate(windowPos, 244, 134), 
//					UtilG.Translate(windowPos, 95, 117)} ;
//			for (int eq = 0 ; eq <= equips.length - 1 ; eq += 1)
//			{
//				if (equips[eq] == null) { continue ;}
//				
//				Equip equip = equips[eq] ;
//				Image[] eqImages = new Image[] {Equip.SwordImage, Equip.ShieldImage, Equip.ArmorImage, Equip.ArrowImage} ;
//				Point textPos = UtilG.Translate(eqSlotCenter[eq], -eqSlotW[eq] / 2 - 5, -eqSlotH[eq] / 2 - 2) ;
//
//				DP.DrawImage(eqImages[eq], eqSlotCenter[eq], Align.center) ;
//				if (0 < equip.getForgeLevel())
//				{
//					DP.DrawText(textPos, Align.bottomCenter, angle, equipsText[eq + 1] + " + " + equip.getForgeLevel(), font, textColor) ;					
//				}
//				
//				Elements eqElem = pet.getElem()[eq + 1] ;
//				if (eqElem != null) { continue ;}
//				
//				int elemID = Elements.getID(eqElem) ;
//				Image elemImage = DrawingOnPanel.ElementImages[elemID] ;
//				Point elemPos = UtilG.Translate(eqSlotCenter[eq], eqSlotW[eq] - 12, eqSlotH[eq] / 2) ;
//				DP.DrawImage(elemImage, elemPos, angle, new Scale(0.12, 0.12), Align.center) ;
//			}
//		}
		
		
		// super element
		if (pet.hasSuperElement())
		{
			int elemID = Elements.getID(pet.getElem()[4]) ;
			Point superElemPos = UtilG.Translate(userPos, 0, 35) ;
			Image superElemImage = DrawingOnPanel.ElementImages[elemID] ;
			DP.DrawImage(superElemImage, superElemPos, angle, new Scale(0.3, 0.3), Align.center) ;
		}
		
		
		// attributes
		Point lifePos = UtilG.Translate(windowPos, 20, border + padding + 37) ;
		Point mpPos = UtilG.Translate(windowPos, 20, border + padding + 37 + 26) ;
		String lifeText = attText[1] + ": " + UtilG.Round(pet.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + UtilG.Round(pet.getPA().getMp().getCurrentValue(), 1) ;
		DP.DrawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[6]) ;
		DP.DrawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[5]) ;
				
		BasicBattleAttribute[] attributes = pet.getBA().basicAttributes() ;
		Point initialAttPos = UtilG.Translate(windowPos, border + padding + 34, 124) ;
		for (int i = 0; i <= attributes.length - 1; i += 1)
		{
			Point attPos = UtilG.Translate(initialAttPos, 117 * (i / 3), (i % 3) * 22) ;
			String attValue = UtilG.Round(attributes[i].getBaseValue(), 1) + " + " + UtilG.Round(attributes[i].getBonus(), 1) + " + " + UtilG.Round(attributes[i].getTrain(), 1) ;
			
			DP.DrawImage(attIcons[i], UtilG.Translate(attPos, -15, 0), Scale.unit, Align.center) ;
			DP.DrawText(attPos, Align.centerLeft, angle, attValue, font, textColor) ;
		}
		Point critPos = UtilG.Translate(initialAttPos, 0, 71) ;
		String critValue = attText[9] + ": " + UtilG.Round(100 * pet.getBA().TotalCritAtkChance(), 1) + "%" ;
		DP.DrawImage(critIcon, UtilG.Translate(initialAttPos, -15, 72), Scale.unit, Align.center) ;
		DP.DrawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		
		
	}
}
