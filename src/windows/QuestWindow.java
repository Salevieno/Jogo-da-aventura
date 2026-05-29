package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.List;
import java.util.Map;

import components.Quest;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import items.Item;
import liveBeings.CreatureType;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class QuestWindow extends GameWindow
{
	private List<Quest> quests ;
	private BagWindow bag ; // TODO remover bag, registrar itens já coletados e atualizar ao abrir a janela
	
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Quest.png") ;

	public QuestWindow()
	{
		super("Quest", Screen.getMe().pos(0.3, 0.1), IMAGE, 0, 0, 0, 0) ;
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
	
	public void displayReqCreatures(Point sectionPos, Quest quest)
	{
		
		Map<Integer, Integer> reqCreatureTypeIDs = quests.get(window).getReqCreatures() ;
		
		if (reqCreatureTypeIDs == null) { return ;}
		if (reqCreatureTypeIDs.isEmpty()) { return ;}
		
		double angle = Draw.stdAngle ;
		Color textColor = Palette.colors[0] ;
		
		List<CreatureType> reqCreatureType = reqCreatureTypeIDs.keySet().stream().map(typeID -> CreatureType.getAll().get(typeID)).toList() ;
		GamePanel.getDP().drawText(sectionPos, Align.center, angle, "Criaturas necessárias", TITLE_FONT, textColor) ;
		GamePanel.getDP().drawLine(Util.translate(sectionPos, -60, 20), Util.translate(sectionPos, 60, 20), 1, textColor) ;
		
		Point creaturePos = Util.translate(sectionPos, -60, 20) ;
		for (CreatureType creatureType : reqCreatureType)
		{
			String creatureName = creatureType.getName() ;
			creaturePos.y += creatureType.getSize().height / 2 + 6 ;
			Point textPos = Util.translate(creaturePos, 25, 0) ;
			int numberReq = reqCreatureTypeIDs.get(creatureType.getID()) ;
			int numberCounter = quest.getCounter().get(creatureType.getID()) ;
			creatureType.display(creaturePos, Scale.unit) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, angle, creatureName + " : " + numberCounter + " / " + numberReq, TITLE_FONT, textColor) ;
			creaturePos.y += creatureType.getSize().height / 2 ;
		}	
		
	}
	
	public void displayReqItems(Point sectionPos)
	{
		
		Map<Integer, Integer> reqItemIDs = quests.get(window).getReqItemIDs() ;
		
		if (reqItemIDs == null) { return ;}
		if (reqItemIDs.isEmpty()) { return ;}

		double angle = Draw.stdAngle ;
		Color textColor = Palette.colors[0] ;
		
		List<Item> reqItem = reqItemIDs.keySet().stream().map(id -> Item.getAllItems().get(id)).toList() ;
		GamePanel.getDP().drawText(sectionPos, Align.center, angle, "Itens necessários", TITLE_FONT, textColor) ;
		GamePanel.getDP().drawLine(Util.translate(sectionPos, -60, 20), Util.translate(sectionPos, 60, 20), 1, textColor) ;
		
		Point circlePos = Util.translate(sectionPos, -80, 20) ;
		Point itemPos = Util.translate(circlePos, 13, 0) ;
		for (Item item : reqItem)
		{
			itemPos.y += TITLE_FONT.getSize() + 4 ;
			circlePos.y += TITLE_FONT.getSize() + 4 ;
			Point textPos = Util.translate(itemPos, 15, 0) ;
			GamePanel.getDP().drawCircle(circlePos, 10, 0, bag.contains(item) ? Palette.colors[3] : Palette.colors[6], null) ;
			GamePanel.getDP().drawImage(item.getImage(), itemPos, Align.center) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, angle, item.getName(), TITLE_FONT, textColor) ;
		}
		
	}
	
	public void display(Point mousePos)
	{
		
		double angle = Draw.stdAngle ;

		GamePanel.getDP().drawImage(image, topLeftPos, angle, Scale.unit, Align.topLeft, stdOpacity) ;

		if (quests.size() <= 0) { return ;}
		
		Quest quest = quests.get(window) ;
		Point questPos = Util.translate(topLeftPos, image.getWidth(null) / 2, 30) ;
		GamePanel.getDP().drawText(questPos, Align.center, angle, quest.getName(), TITLE_FONT, Palette.colors[8]) ;
		
		displayReqCreatures(Util.translate(topLeftPos, size.width / 2 , 60), quest) ;
		displayReqItems(Util.translate(topLeftPos, size.width / 2, 260)) ;
		
		Draw.windowArrows(Util.translate(topLeftPos, 0, size.height + 10), size.width, window, numberWindows, stdOpacity) ;
		
	}
}
