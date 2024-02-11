package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;
import java.util.Map;

import components.Quest;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import liveBeings.CreatureType;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class QuestWindow extends GameWindow
{
	private List<Quest> quests ;
	private BagWindow bag ;
	
	private static final Point windowPos ;
	private static final Font font ;
	private static final Image image ;
	
	static
	{
		windowPos = Game.getScreen().pos(0.3, 0.15) ;
		font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		image = UtilS.loadImage("\\Windows\\" + "Quest.png") ;
	}
	
	public QuestWindow()
	{
		super("Quest", windowPos, image, 0, 0, 0, 0) ;
	}
	
	
	public void setQuests(List<Quest> quests) { this.quests = quests ;}
	public void setBag(BagWindow bag) { this.bag = bag ;}


	public void navigate(String action)
	{
		if (action.equals(stdWindowUp))
		{
			windowUp() ;
		}
		if (action.equals(stdWindowDown))
		{
			windowDown() ;
		}
	}
	
	public void displayReqCreatures(Point sectionPos, Quest quest, DrawPrimitives DP)
	{
		
		Map<CreatureType, Integer> reqCreatureTypes = quests.get(window).getReqCreatures() ;
		
		if (reqCreatureTypes == null) { return ;}
		if (reqCreatureTypes.isEmpty()) { return ;}
		
		double angle = Draw.stdAngle ;
		Color textColor = Game.colorPalette[0] ;
		
		CreatureType[] reqCreatureType = new CreatureType[0];
		reqCreatureType = reqCreatureTypes.keySet().toArray(reqCreatureType) ;
		DP.drawText(sectionPos, Align.center, angle, "Criaturas necessárias", font, textColor) ;
		DP.drawLine(UtilG.Translate(sectionPos, -60, 20), UtilG.Translate(sectionPos, 60, 20), 1, textColor) ;
		
		Point creaturePos = UtilG.Translate(sectionPos, 10, 10) ;
		for (CreatureType creatureType : reqCreatureType)
		{
			String creatureName = creatureType.getName() ;
			creaturePos = UtilG.Translate(creaturePos, 0, creatureType.getSize().height + 4) ;
			Point textPos = UtilG.Translate(creaturePos, 25, 0) ;
			int numberReq = reqCreatureTypes.get(creatureType) ;
			int numberCounter = quest.getCounter().get(creatureType) ;
			creatureType.display(creaturePos, Scale.unit, DP) ;
			DP.drawText(textPos, Align.centerLeft, angle, creatureName + " : " + numberCounter + " / " + numberReq, font, textColor) ;
		}	
		
	}
	
	public void displayReqItems(Point sectionPos, DrawPrimitives DP)
	{
		
		Map<Item, Integer> reqItems = quests.get(window).getReqItems() ;
		
		if (reqItems == null) { return ;}
		if (reqItems.isEmpty()) { return ;}

		double angle = Draw.stdAngle ;
		Color textColor = Game.colorPalette[0] ;
		
		Item[] reqItem = new Item[0] ;
		reqItem = reqItems.keySet().toArray(reqItem) ;
		DP.drawText(sectionPos, Align.center, angle, "Itens necessários", font, textColor) ;
		DP.drawLine(UtilG.Translate(sectionPos, -60, 20), UtilG.Translate(sectionPos, 60, 20), 1, textColor) ;
		
		Point circlePos = UtilG.Translate(sectionPos, 10, 20) ;
		Point itemPos = UtilG.Translate(circlePos, 10, 0) ;
		for (Item item : reqItem)
		{
			itemPos = UtilG.Translate(itemPos, 0, font.getSize() + 4) ;
			circlePos = UtilG.Translate(circlePos, 0, font.getSize() + 4) ;
			Point textPos = UtilG.Translate(itemPos, 15, 0) ;
			DP.drawCircle(circlePos, 10, 0, bag.contains(item) ? Game.colorPalette[3] : Game.colorPalette[6], null) ;
			DP.drawImage(item.getImage(), itemPos, Align.center) ;
			DP.drawText(textPos, Align.centerLeft, angle, item.getName(), font, textColor) ;
		}
		
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{		

		numberWindows = quests.size() ;	// TODO quest number windows, set when opening window
		
		double angle = Draw.stdAngle ;

		DP.drawImage(image, windowPos, angle, Scale.unit, Align.topLeft) ;

		if (quests.size() <= 0) { return ;}
		
		Quest quest = quests.get(window) ;
		Point questPos = UtilG.Translate(windowPos, image.getWidth(null) / 2, 30) ;
		DP.drawText(questPos, Align.center, angle, quest.getName() + " " + quest.getID(), font, Game.colorPalette[8]) ;
		
		displayReqCreatures(UtilG.Translate(windowPos, 0 , 60), quest, DP) ;
		displayReqItems(UtilG.Translate(windowPos, 0, 230), DP) ;
		
		Draw.windowArrows(UtilG.Translate(windowPos, size.width / 2, size.height + 5), size.width, window, numberWindows) ;
	}
}
