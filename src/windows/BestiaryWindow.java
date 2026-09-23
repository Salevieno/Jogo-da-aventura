package windows;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import liveBeings.CreatureType;
import liveBeings.Player;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import screen.Screen;
import utilities.Util;

public class BestiaryWindow extends GameWindow
{
    private final int offset ;
    private final Dimension slotSize ;
    private final int sx ;
    private final int sy ;
    
    private final List<Point> slotCenter ;
    private final Point navigationButtonsPos ;
	private List<CreatureType> discoveredCreatures ;
	private List<CreatureType> creaturesOnPage ;
    private List<Scale> creatureImageScales ;
    private int numSlotsInPage ;
    private CreatureInfoWindow creatureInfoWindow ;

    private static final int NUM_ROWS = 5 ;
    private static final int NUM_COLS = 6 ;
    private static final int QTD_SLOTS_ON_PAGE = NUM_COLS * NUM_ROWS ;
	private static final Image SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BestiarySlot.png") ;
	private static final Image SELECTED_SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BestiarySlotSelected.png") ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Bestiary.png") ;
	
	public BestiaryWindow()
	{
		super("Bestiário", Screen.getMe().pos(0.1, 0.3), IMAGE, 0, 0, 0, 0) ;
        this.offset = 16 ;
        this.navigationButtonsPos = Util.translate(topLeftPos, 0, size.height + 10) ;
        this.slotSize = Util.getSize(SLOT_IMAGE) ;
		Dimension windowSize = Util.getSize(IMAGE) ;
        this.sx = (int) Util.spacing(windowSize.width, NUM_COLS, slotSize.width, offset) ;
        this.sy = (int) Util.spacing(windowSize.height, NUM_ROWS, slotSize.height, offset) ;
        this.slotCenter = new ArrayList<>(QTD_SLOTS_ON_PAGE) ;
        for (int i = 0 ; i <= QTD_SLOTS_ON_PAGE - 1 ; i += 1)
		{
            this.slotCenter.add(Util.translate(topLeftPos, (i / NUM_ROWS) * sx + offset + slotSize.width / 2, (i % NUM_ROWS) * sy + offset + slotSize.height / 2)) ;
        }
		this.discoveredCreatures = new ArrayList<>() ;
        this.creaturesOnPage = new ArrayList<>(QTD_SLOTS_ON_PAGE) ;
        this.creatureImageScales = new ArrayList<>() ;
        this.numSlotsInPage = Math.min(discoveredCreatures.size(), QTD_SLOTS_ON_PAGE) ;
        this.creatureInfoWindow = new CreatureInfoWindow() ;
        this.item = -1 ;
	}

    protected void onOpen()
    {
        
    }

    public void act(Player player, Point mousePos)
    {
        
    }

	public void addDiscoveredCreature(CreatureType newCreatureType)
    {
        if (discoveredCreatures.contains(newCreatureType)) { return ;}

        discoveredCreatures.add(newCreatureType) ;
        double scaleFactor = Math.min((double) (slotSize.width - 6) / newCreatureType.getSize().width, (double) (slotSize.height - 16) / newCreatureType.getSize().height) ;
        creatureImageScales.add(new Scale(scaleFactor, scaleFactor)) ;
        numberPages = (int) Math.ceil((double) discoveredCreatures.size() / (QTD_SLOTS_ON_PAGE)) ;
        updatePage() ;
    }
	
	public void navigate(String action)
	{
		if (action.equals(stdPageDown))
		{
			pageDown() ;
			updatePage() ;
		}
		if (action.equals(stdPageUp))
		{
			pageUp() ;
			updatePage() ;
		}
	}
    protected int itemHoveredID(Point mousePos)
    {
        for (int i = 0 ; i <= numSlotsInPage - 1 ; i += 1)
		{
            if (itemIsHovered(mousePos, slotCenter.get(i), Align.center, slotSize)) { return i ;}
        }
        return -1 ;
    }

    private void updatePage()
    {
        if (discoveredCreatures == null || discoveredCreatures.isEmpty()) { return ;}
        if (discoveredCreatures.size() <= QTD_SLOTS_ON_PAGE)
        {
            creaturesOnPage = discoveredCreatures ;
            numSlotsInPage = Math.min(creaturesOnPage.size(), QTD_SLOTS_ON_PAGE) ;
            return ;
        }

        creaturesOnPage = discoveredCreatures.subList(page * QTD_SLOTS_ON_PAGE, Math.min((page + 1) * QTD_SLOTS_ON_PAGE, discoveredCreatures.size())) ;
        numSlotsInPage = Math.min(creaturesOnPage.size(), QTD_SLOTS_ON_PAGE) ;
        item = 0 ;
    }

	public void display(Point mousePos)
	{
        if (0 <= item && !creaturesOnPage.isEmpty())
        {
            creatureInfoWindow.setCreatureType(creaturesOnPage.get(item)) ;
            creatureInfoWindow.display(mousePos) ;
        }

		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, 1.0) ;

		if (creaturesOnPage == null || creaturesOnPage.isEmpty()) { return ;}

		for (int i = 0 ; i <= creaturesOnPage.size() - 1 ; i += 1)
		{
		    GamePanel.getDP().drawImage(i == item ? SELECTED_SLOT_IMAGE : SLOT_IMAGE, slotCenter.get(i), Scale.unit, Align.center, 1.0) ;
			creaturesOnPage.get(i).display(slotCenter.get(i), creatureImageScales.get(i + page * QTD_SLOTS_ON_PAGE)) ;
		}

        if (2 <= numberPages)
        {
		    drawNavigationButtons(navigationButtonsPos, size.width, SUBTITLE_FONT, page, numberPages, stdOpacity) ;
        }
	}

	protected void onClose() { }
}