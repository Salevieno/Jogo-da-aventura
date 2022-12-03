package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import GameComponents.Quests;
import Graphics.DrawPrimitives;
import LiveBeings.CreatureTypes;
import Main.Game;
import Utilities.Scale;
import Utilities.UtilG;

public class QuestWindow extends Window
{
	public static Image image = new ImageIcon(Game.ImagesPath + "Quest.png").getImage() ;
	public QuestWindow()
	{
		super(image, 0, 0, 0, 0) ;
	}
	
	public void display(ArrayList<Quests> quests, DrawPrimitives DP)
	{
		Point windowPos = new Point((int)(0.5 * Game.getScreen().getSize().x), (int)(0.55 * Game.getScreen().getSize().y)) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
		float angle = DrawPrimitives.OverallAngle ;
		Color textColor = Game.ColorPalette[0] ;
		numberWindows = quests.size() ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), "Center") ;
		
		// draw quests
		//int ExpTextCat = [26] ;
		//int ItemsTextCat = AllTextCat[27] ;
		int[] reqCreatureTypeID = quests.get(window).getReqCreatures() ;
		int[] ReqItems = quests.get(window).getReqItems() ;
		
		Quests quest = quests.get(window) ;
		Point questPos = UtilG.Translate(windowPos, 0, - image.getHeight(null) / 3) ;
		DP.DrawText(questPos, "Center", angle, quest.getName(), font, textColor) ;
		
		if (reqCreatureTypeID != null)
		{
			//Size size = new Size((int)(0.23*screenSize.x), (int) (0.2*screenSize.y)) ;
			//int sy = size.y / 5 ;
			//Point WindowPos = new Point((int) (0.4*screenSize.x), (int) (0.6*screenSize.y)) ;
			//DrawMenuWindow(WindowPos, size, null, 0, MenuColor[0], ColorPalette[7]) ;
			//DP.DrawText(new Point((int) (WindowPos.x + 0.5 * size.x), (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()))), "Center", OverallAngle, AllText[ExpTextCat][3], font, ColorPalette[0]) ;
			for (int i = 0 ; i <= reqCreatureTypeID.length - 1 ; i += 1)
			{
				CreatureTypes creatureType = Game.getCreatureTypes()[reqCreatureTypeID[i]] ;
				String creatureName = creatureType.getPA().getName() ;
				Point textPos = UtilG.Translate(windowPos, 15, i * font.getSize()) ;
				//Point Pos2 = new Point((int) (WindowPos.x + Utg.TextL("Creature 299: ", font, G)) + creatureTypes[CreatureType].getSize()[0]/2, (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()) + (i + 1.5)*sy)) ;
				DP.DrawText(textPos, "BotLeft", angle, creatureName + ":", font, textColor) ;
				/*DrawCreature(Pos2, new int[] {creatureType.getPA().getSize().x / 2, creatureType.getPA().getSize().y / 2},
						creatureType.getMovingAnimations().idleGif, creatureType.getColor()) ;*/
			}
		}
		/*if (QuestHasItems)
		{
			Size size = new Size((int) (0.16*screenSize.x), (int) (1.6*Utg.TextH(font.getSize())*(ReqItems.length + 1))) ;
			int sy = size.y / (ReqItems.length + 1) ;
			Point WindowPos = new Point((int) (0.2*screenSize.x), (int) (0.6*screenSize.y)) ;
			DrawMenuWindow(WindowPos, size, null, 0, MenuColor[0], ColorPalette[7]) ;
			DP.DrawText(new Point((int) (WindowPos.x + 0.05 * size.x), (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()))), "BotLeft", OverallAngle, AllText[ItemsTextCat][3], font, npc.getColor()) ;
			for (int i = 0 ; i <= ReqItems.length - 1 ; i += 1)
			{
				if (-1 < ReqItems[i])
				{
					String ItemName = items[ReqItems[i]].getName() ;
					Point Pos1 = new Point((int) (WindowPos.x + 0.05 * size.x), (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()) + 0.15 * size.y + i*sy)) ;
					DP.DrawText(Pos1, "BotLeft", OverallAngle, ItemName, font, npc.getColor()) ;
				}
			}
		}*/
	}
}
