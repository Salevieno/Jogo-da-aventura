package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		super("Quest", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Quest.png"), 0, 0, 0, 0) ;
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
	
	public void display(List<Quests> quests, BagWindow bag, DrawingOnPanel DP)
	{		
		if (quests.size() <= 0) { return ;}

		numberWindows = quests.size() ;
		
		Point windowPos = new Point((int)(0.3 * Game.getScreen().getSize().width), (int)(0.15 * Game.getScreen().getSize().height)) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		double angle = DrawingOnPanel.stdAngle ;	
		Quests quest = quests.get(window) ;
		Point questPos = UtilG.Translate(windowPos, image.getWidth(null) / 2, 30) ;

		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		DP.DrawText(questPos, Align.center, angle, quest.getName(), font, Game.ColorPalette[6]) ;

		// draw required creatures
		Map<CreatureTypes, Integer> reqCreatureTypes = quests.get(window).getReqCreatures() ;
		if (reqCreatureTypes != null)
		{
			CreatureTypes[] reqCreatureType = new CreatureTypes[0];
			reqCreatureType = reqCreatureTypes.keySet().toArray(reqCreatureType) ;
			Point creaturesSectionPos = UtilG.Translate(windowPos, size.width / 2 , 60) ;
			DP.DrawText(creaturesSectionPos, Align.center, angle, "Criaturas necessárias", font, Game.ColorPalette[9]) ;
			DP.DrawLine(UtilG.Translate(creaturesSectionPos, -60, 20), UtilG.Translate(creaturesSectionPos, 60, 20), 1, Game.ColorPalette[9]) ;
			Point creaturePos = UtilG.Translate(windowPos, 50, 80) ;
			for (int i = 0 ; i <= reqCreatureType.length - 1 ; i += 1)
			{
				String creatureName = reqCreatureType[i].getName() ;
				creaturePos = UtilG.Translate(creaturePos, 0, reqCreatureType[i].getSize().height + 4) ;
				Point textPos = UtilG.Translate(creaturePos, 25, 0) ;
				DP.DrawText(textPos, Align.centerLeft, angle, creatureName + " : " + quest.getCounter().get(reqCreatureType[i]) + " / " + reqCreatureTypes.get(reqCreatureType[i]), font, Game.ColorPalette[9]) ;
				reqCreatureType[i].display(creaturePos, new Scale(1, 1), DP) ;
			}
		}
		
		// draw required items
		Map<Item, Integer> reqItems = quests.get(window).getReqItems() ;
		
		if (reqItems == null) { return ;}
		
		Item[] reqItem = new Item[0];
		reqItem = reqItems.keySet().toArray(reqItem) ;
		Point itemsSectionPos = UtilG.Translate(windowPos, size.width / 2, 180) ;
		DP.DrawText(itemsSectionPos, Align.center, angle, "Itens necessários", font, Game.ColorPalette[9]) ;
		DP.DrawLine(UtilG.Translate(itemsSectionPos, -60, 20), UtilG.Translate(itemsSectionPos, 60, 20), 1, Game.ColorPalette[9]) ;
		Point circlePos = UtilG.Translate(windowPos, 50, 200) ;
		Point itemPos = UtilG.Translate(circlePos, 10, 0) ;
		for (Item item : reqItem)
		{
			itemPos = UtilG.Translate(itemPos, 0, font.getSize() + 4) ;
			circlePos = UtilG.Translate(circlePos, 0, font.getSize() + 4) ;
			Point textPos = UtilG.Translate(itemPos, 15, 0) ;
			DP.DrawCircle(circlePos, 10, 0, bag.contains(item) ? Game.ColorPalette[3] : Game.ColorPalette[6], null) ;
			DP.DrawImage(item.getImage(), itemPos, Align.center) ;
			DP.DrawText(textPos, Align.centerLeft, angle, item.getName(), font, Game.ColorPalette[9]) ;
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, 0, size.height), size.width, window, numberWindows) ;
	}
}
