package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;

import components.Quests;
import graphics.DrawingOnPanel;
import items.Item;
import liveBeings.CreatureTypes;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class QuestWindow extends GameWindow
{
	public QuestWindow()
	{
		super(new ImageIcon(Game.ImagesPath + "\\Windows\\" + "Quest.png").getImage(), 0, 0, 0, 0) ;
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
	
	public void display(ArrayList<Quests> quests, DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.3 * Game.getScreen().getSize().width), (int)(0.15 * Game.getScreen().getSize().height)) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		double angle = DrawingOnPanel.stdAngle ;
		Color textColor = Game.ColorPalette[0] ;
		numberWindows = quests.size() ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		// draw quest name
		Quests quest = quests.get(window) ;
		Point questPos = UtilG.Translate(windowPos, 0, - image.getHeight(null) / 3) ;
		DP.DrawText(questPos, Align.center, angle, quest.getName(), font, textColor) ;

		// draw required creatures
		Map<CreatureTypes, Integer> reqCreatureTypes = quests.get(window).getReqCreatures() ;
		if (reqCreatureTypes != null)
		{
			CreatureTypes[] reqCreatureType = new CreatureTypes[0];
			reqCreatureType = reqCreatureTypes.keySet().toArray(reqCreatureType) ;
			for (int i = 0 ; i <= reqCreatureType.length - 1 ; i += 1)
			{
				CreatureTypes creatureType = reqCreatureType[i] ;
				// TODO get creature name
//				String creatureName = creatureType.getName() ;
//				Point textPos = UtilG.Translate(windowPos, 15, 55 + i * font.getSize()) ;
//				DP.DrawText(textPos, Align.bottomLeft, angle, creatureName + ":" + reqCreatureTypes.get(creatureType), font, textColor) ;
//				creatureType.display(textPos, new Scale(1, 1), DP) ;
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
				DP.DrawText(textPos, Align.bottomLeft, angle, item.getName(), font, textColor) ;
				DP.DrawImage(item.getImage(), textPos, Align.topLeft) ;
			}
		}
	}
}
