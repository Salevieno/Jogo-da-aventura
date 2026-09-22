package windows;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import liveBeings.CreatureType;
import liveBeings.Player;
import main.GamePanel;
import main.Palette;
import screen.Screen;
import utilities.Util;

public class BestiaryWindow extends GameWindow
{
	private final Dimension windowSize ;
    private final int offset ;
    private final Dimension slotSize ;
    private final int sx ;
    private final int sy ;
    private final List<Point> slotTopLeft ;
    private final List<Point> slotCenter ;
	private List<CreatureType> discoveredCreatures ;
    private int numSlotsInPage ;
    private CreatureInfoWindow creatureInfoWindow ;

    private static final int NUM_ROWS = 6 ;
    private static final int NUM_COLS = 6 ;
	
	public BestiaryWindow()
	{// TODO imagem do bestiário
		super("Bestiário", Screen.getMe().pos(0.1, 0.3), null, 0, 0, 0, 0) ;
		this.windowSize = new Dimension(384, 288) ;
        this.offset = 12 ;
        this.slotSize = new Dimension(windowSize.width / (NUM_COLS + 1) - 2 * offset / NUM_COLS, windowSize.height / (NUM_ROWS + 1) - 2 * offset / NUM_ROWS) ;
        this.sx = (int) Util.spacing(windowSize.width, NUM_COLS, slotSize.width, offset) ;
        this.sy = (int) Util.spacing(windowSize.height, NUM_ROWS, slotSize.height, offset) ;
        this.slotTopLeft = new ArrayList<>(NUM_ROWS * NUM_COLS) ;
        this.slotCenter = new ArrayList<>(NUM_ROWS * NUM_COLS) ;
        for (int i = 0 ; i <= NUM_ROWS * NUM_COLS - 1 ; i += 1)
		{
            this.slotTopLeft.add(Util.translate(topLeftPos, (i / NUM_COLS) * sx + offset, (i % NUM_ROWS) * sy + offset)) ;
            this.slotCenter.add(Util.translate(slotTopLeft.get(i), slotSize.width / 2, slotSize.height / 2)) ;
        }
		this.discoveredCreatures = new ArrayList<>() ;
        this.numSlotsInPage = Math.min(discoveredCreatures.size(), NUM_ROWS * NUM_COLS) ;
        this.creatureInfoWindow = new CreatureInfoWindow() ;
	}

    protected void onOpen()
    {
        
    }

    public void act(Player player, Point mousePos)
    {
        
    }

	public void addDiscoveredCreature(CreatureType newCreature)
    {
        discoveredCreatures.add(newCreature) ;
        numSlotsInPage = Math.min(discoveredCreatures.size(), NUM_ROWS * NUM_COLS) ;
    }
	
	public void navigate(String action)
	{
	}
    protected int itemHoveredID(Point mousePos)
    {
        for (int i = 0 ; i <= numSlotsInPage - 1 ; i += 1)
		{
            if (Util.isInside(mousePos, slotTopLeft.get(i), slotSize)) { return i ;}
        }
        return -1 ;
    }

	public void display(Point mousePos)
	{
		// draw window
		GamePanel.getDP().drawGradRoundRect(topLeftPos, Align.topLeft, windowSize, 3, Palette.colors[5], Palette.colors[14], Palette.colors[0], true) ;
		
		if (discoveredCreatures == null) { return ;}
		
		for (int i = 0 ; i <= numSlotsInPage - 1 ; i += 1)
		{
			// draw slots
			GamePanel.getDP().drawGradRoundRect(slotCenter.get(i), Align.center, slotSize, 2, Palette.colors[3], Palette.colors[20], Palette.colors[0], true) ;

			// draw creatures
			CreatureType creatureType = discoveredCreatures.get(i) ;
			double scaleFactor = Math.min((double) (slotSize.width - 10) / creatureType.getSize().width, (double) (slotSize.height - 10) / creatureType.getSize().height) ;
			creatureType.display(slotCenter.get(i), new Scale(scaleFactor, scaleFactor)) ;
		}

		if (discoveredCreatures.isEmpty()) { return ;}
		if (item < 0) { return ;}

        creatureInfoWindow.setCreatureType(discoveredCreatures.get(item)) ;
		creatureInfoWindow.display(mousePos) ;
	}

	protected void onClose() { }
}