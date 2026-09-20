package windows;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.Quest;
import graphics.Align;
import graphics.Scale;
import items.Item;
import liveBeings.CreatureType;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class QuestWindow extends GameWindow
{
	private final Point questPos ;
    private final Point reqCreaturesTitlePos ;
    private final Point reqItemsTitlePos ;
    private final Point navigationButtonsPos ;
	private final List<Point> creaturesPos ;
	private final List<Point> creaturesTextPos ;
	private final List<Point> itemsTextPos ;
    private final List<Point> circlePos ;
    private final List<Point> itemPos ;
	private List<Quest> quests ;
    private Quest quest ;
	private List<CreatureType> reqCreatureTypes ;
	private List<Item> reqItems ;
	private BagWindow bag ;
	
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Quest.png") ;

	public QuestWindow()
	{
		super("Quest", Screen.getMe().pos(0.36, 0.14), IMAGE, 0, 0, 0, 0) ;
	    this.questPos = Util.translate(topLeftPos, image.getWidth(null) / 2, 30) ;
        this.reqCreaturesTitlePos = Util.translate(topLeftPos, size.width / 2, 128) ;
        this.reqItemsTitlePos = Util.translate(topLeftPos, size.width / 2, 368) ;
        this.navigationButtonsPos = Util.translate(topLeftPos, 0, size.height + 10) ;
        this.creaturesPos = new ArrayList<>(3) ;
        this.creaturesTextPos = new ArrayList<>(3) ;
        for (int i = 0 ; i <= 3 - 1 ; i += 1)
        {
            this.creaturesPos.add(Util.translate(topLeftPos, 64, 112 + (i + 1) * 66)) ;
            this.creaturesTextPos.add(Util.translate(topLeftPos, 96, 112 + (i + 1) * 66)) ;
        }
        this.itemsTextPos = new ArrayList<>(10) ;
        this.circlePos = new ArrayList<>(10) ;
        this.itemPos = new ArrayList<>(10) ;
        for (int i = 0 ; i <= 10 - 1 ; i += 1)
        {
            this.itemsTextPos.add(Util.translate(topLeftPos, 96, 400 + (i + 1) * 20)) ;
            this.circlePos.add(Util.translate(topLeftPos, 64, 400 + (i + 1) * 20)) ;
            this.itemPos.add(Util.translate(topLeftPos, 80, 400 + (i + 1) * 20)) ;
        }
	}

    protected void onOpen()
    {
        this.quests = Game.getPlayer().getQuests() ;

        if (quests == null) { return ;}
        if (quests.isEmpty()) { return ;}

        this.numberPages = quests.size() ;
        this.bag = Game.getPlayer().getBag() ;
        updatePage() ;
    }

    public void act(Player player, Point mousePos)
    {
        
    }

	public void navigate(String action)
	{
		if (action.equals(stdPageUp))
		{
			pageUp() ;
            updatePage() ;
		}
		if (action.equals(stdPageDown))
		{
			pageDown() ;
            updatePage() ;
		}
	}
    protected int itemHoveredID(Point mousePos)
    {
        return -1 ;
    }

    public void updateSelectedItemOnHover(Point mousePos)
    {
    }
	
    private void updatePage()
    {
		if (quests.size() <= page) { return ;}

        this.quest = quests.get(page) ;
		this.reqCreatureTypes = quest.getReqCreatureIDs().keySet().stream().map(typeID -> CreatureType.getAll().get(typeID)).toList() ;
		this.reqItems = quest.getReqItemIDs().keySet().stream().map(id -> Item.getAllItems().get(id)).toList() ;
    }

	public void displayReqCreatures()
	{
		if (reqCreatureTypes == null) { return ;}
		if (reqCreatureTypes.isEmpty()) { return ;}
		
		GamePanel.getDP().drawText(reqCreaturesTitlePos, Align.center, "Criaturas necessárias", TITLE_FONT, Palette.colors[0]) ;
		
		for (int i = 0 ; i <= reqCreatureTypes.size() - 1; i += 1)
		{
            CreatureType creatureType = reqCreatureTypes.get(i) ;
			int qtdReq = quest.getReqCreatureIDs().get(creatureType.getID()) ;
			int counter = quest.getCounter().get(creatureType.getID()) ;
            String text = creatureType.getName() + " : " + counter + " / " + qtdReq ;

			creatureType.display(creaturesPos.get(i), Scale.unit) ;
			GamePanel.getDP().drawText(creaturesTextPos.get(i), Align.centerLeft, text, TITLE_FONT, Palette.colors[0]) ;
		}
	}
	
	public void displayReqItems()
	{
		if (reqItems == null) { return ;}
		if (reqItems.isEmpty()) { return ;}

		GamePanel.getDP().drawText(reqItemsTitlePos, Align.center, "Itens necessários", TITLE_FONT, Palette.colors[0]) ;
		
		for (int i = 0 ; i <= reqItems.size() - 1; i += 1)
		{
			Item item = reqItems.get(i) ;
			GamePanel.getDP().drawCircle(circlePos.get(i), 10, 0, bag.contains(item) ? Palette.colors[3] : Palette.colors[6], null) ;
			GamePanel.getDP().drawImage(item.getImage(), itemPos.get(i), Align.center) ;
			GamePanel.getDP().drawText(itemsTextPos.get(i), Align.centerLeft, item.getName(), TITLE_FONT, Palette.colors[0]) ;
		}
	}
	
	public void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;
		
        if (quest == null) { return ;}

		GamePanel.getDP().drawText(questPos, Align.center, quest.getName(), TITLE_FONT, Palette.colors[8]) ;
		
		displayReqCreatures() ;
		displayReqItems() ;
		
		drawNavigationButtons(navigationButtonsPos, size.width, SUBTITLE_FONT, page, numberPages, stdOpacity) ;
	}

    protected void onClose() { }
}