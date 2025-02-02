package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;
import java.util.Map;

import components.Quest;
import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics2.Draw;
import items.Item;
import liveBeings.CreatureType;
import main.Game;
import utilities.Util;
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
		windowPos = Game.getScreen().pos(0.3, 0.1) ;
		font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		image = UtilS.loadImage("\\Windows\\" + "Quest.png") ;
	}
	
	public QuestWindow()
	{
		super("Quest", windowPos, image, 0, 0, 0, 0) ;
	}
	
	
	public void setQuests(List<Quest> quests) { this.quests = quests ; numberWindows = quests.size() ;}
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
		Color textColor = Game.palette[0] ;
		
		CreatureType[] reqCreatureType = new CreatureType[0];
		reqCreatureType = reqCreatureTypes.keySet().toArray(reqCreatureType) ;
		DP.drawText(sectionPos, Align.center, angle, "Criaturas necessárias", font, textColor) ;
		DP.drawLine(Util.Translate(sectionPos, -60, 20), Util.Translate(sectionPos, 60, 20), 1, textColor) ;
		
		Point creaturePos = Util.Translate(sectionPos, -60, 20) ;
		for (CreatureType creatureType : reqCreatureType)
		{
			String creatureName = creatureType.getName() ;
			creaturePos.y += creatureType.getSize().height / 2 + 6 ;
			Point textPos = Util.Translate(creaturePos, 25, 0) ;
			int numberReq = reqCreatureTypes.get(creatureType) ;
			int numberCounter = quest.getCounter().get(creatureType) ;
			creatureType.display(creaturePos, Scale.unit, DP) ;
			DP.drawText(textPos, Align.centerLeft, angle, creatureName + " : " + numberCounter + " / " + numberReq, font, textColor) ;
			creaturePos.y += creatureType.getSize().height / 2 ;
		}	
		
	}
	
	public void displayReqItems(Point sectionPos, DrawPrimitives DP)
	{
		
		Map<Item, Integer> reqItems = quests.get(window).getReqItems() ;
		
		if (reqItems == null) { return ;}
		if (reqItems.isEmpty()) { return ;}

		double angle = Draw.stdAngle ;
		Color textColor = Game.palette[0] ;
		
		Item[] reqItem = new Item[0] ;
		reqItem = reqItems.keySet().toArray(reqItem) ;
		DP.drawText(sectionPos, Align.center, angle, "Itens necessários", font, textColor) ;
		DP.drawLine(Util.Translate(sectionPos, -60, 20), Util.Translate(sectionPos, 60, 20), 1, textColor) ;
		
		Point circlePos = Util.Translate(sectionPos, -80, 20) ;
		Point itemPos = Util.Translate(circlePos, 13, 0) ;
		for (Item item : reqItem)
		{
			itemPos.y += font.getSize() + 4 ;
			circlePos.y += font.getSize() + 4 ;
			Point textPos = Util.Translate(itemPos, 15, 0) ;
			DP.drawCircle(circlePos, 10, 0, bag.contains(item) ? Game.palette[3] : Game.palette[6], null) ;
			DP.drawImage(item.getImage(), itemPos, Align.center) ;
			DP.drawText(textPos, Align.centerLeft, angle, item.getName(), font, textColor) ;
		}
		
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		
		double angle = Draw.stdAngle ;

		DP.drawImage(image, windowPos, angle, Scale.unit, Align.topLeft, stdOpacity) ;

		if (quests.size() <= 0) { return ;}
		
		Quest quest = quests.get(window) ;
		Point questPos = Util.Translate(windowPos, image.getWidth(null) / 2, 30) ;
		DP.drawText(questPos, Align.center, angle, quest.getName(), font, Game.palette[8]) ;
		
		displayReqCreatures(Util.Translate(windowPos, size.width / 2 , 60), quest, DP) ;
		displayReqItems(Util.Translate(windowPos, size.width / 2, 260), DP) ;
		
		Draw.windowArrows(Util.Translate(windowPos, 0, size.height + 10), size.width, window, numberWindows, stdOpacity) ;
		
	}
}
