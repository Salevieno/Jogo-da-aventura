package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;

import GameComponents.Quests;
import Graphics.DrawPrimitives;
import Items.Item;
import LiveBeings.CreatureTypes;
import LiveBeings.Player;
import Main.Game;
import Utilities.Scale;
import Utilities.UtilG;

public class QuestWindow extends Window
{
	public QuestWindow()
	{
		super(new ImageIcon(Game.ImagesPath + "Quest.png").getImage(), 0, 0, 0, 0) ;
	}
	
	public void navigate(String action)
	{
		if (action.equals(Player.ActionKeys[3]))
		{
			windowUp() ;
		}
		if (action.equals(Player.ActionKeys[1]))
		{
			windowDown() ;
		}
	}
	
	public void display(ArrayList<Quests> quests, DrawPrimitives DP)
	{
		Point windowPos = new Point((int)(0.3 * Game.getScreen().getSize().x), (int)(0.15 * Game.getScreen().getSize().y)) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		double angle = DrawPrimitives.OverallAngle ;
		Color textColor = Game.ColorPalette[0] ;
		numberWindows = quests.size() ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), "TopLeft") ;
		
		// draw quest name
		Quests quest = quests.get(window) ;
		Point questPos = UtilG.Translate(windowPos, 0, - image.getHeight(null) / 3) ;
		DP.DrawText(questPos, "Center", angle, quest.getName(), font, textColor) ;

		// draw required creatures
		Map<CreatureTypes, Integer> reqCreatureTypes = quests.get(window).getReqCreatures() ;
		if (reqCreatureTypes != null)
		{
			CreatureTypes[] reqCreatureType = new CreatureTypes[0];
			reqCreatureType = reqCreatureTypes.keySet().toArray(reqCreatureType) ;
			for (int i = 0 ; i <= reqCreatureType.length - 1 ; i += 1)
			{
				CreatureTypes creatureType = reqCreatureType[i] ;
				String creatureName = creatureType.getPA().getName() ;
				Point textPos = UtilG.Translate(windowPos, 15, 55 + i * font.getSize()) ;
				DP.DrawText(textPos, "BotLeft", angle, creatureName + ":" + reqCreatureTypes.get(creatureType), font, textColor) ;
				creatureType.display(textPos, new Scale(1, 1), DP) ;
			}
		}
		
		// draw required items
		Map<Item, Integer> reqItems = quests.get(window).getReqItems() ;
		if (reqItems != null)
		{
			Item[] reqItem = new Item[0];
			reqItem = reqItems.keySet().toArray(reqItem) ;
			for (int i = 0 ; i <= reqItem.length - 1 ; i += 1)
			{
				Item item = reqItem[i] ;
				Point textPos = UtilG.Translate(windowPos, 65, 55 + i * font.getSize()) ;
				DP.DrawText(textPos, "BotLeft", angle, item.getName(), font, textColor) ;
				DP.DrawImage(item.getImage(), textPos, "TopLeft") ;
			}
		}
	}
}
