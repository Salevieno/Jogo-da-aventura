package sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.Align;
import graphics.UtilAlignment;
import items.Item;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;

public abstract class HotKeysBar
{
    private static final int QTD_ITEMS = 3 ;
    private static List<Item> hotItems = Arrays.asList(null, null, null) ;
    private static final List<Point> SLOT_CENTER ;
    private static final List<Point> KEY_TEXT_POS ;
    private static final List<Point> ITEM_NAME_POS ;
    
	private static final Dimension SLOT_SIZE = Util.getSize(SideBar.SLOT_IMAGE) ;
	private static final Font FONT = new Font(Game.getMainFontName(), Font.BOLD, 14) ;
	private static final Color TEXT_COLOR = Palette.colors[0] ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.SIDEBAR_IMG + "HotBar.png") ;
	private static final Image SLOT_TRANSPARENT_IMAGE = ImageLoader.loadImage(Path.SIDEBAR_IMG + "SlotTransparent.png") ;
	private static final Point BAR_POS = new Point(Screen.getMe().mapSize().width + 2, Screen.getMe().getSize().height - SideBar.SY) ;

    static
    {
        SLOT_CENTER = new ArrayList<>(QTD_ITEMS) ;
        KEY_TEXT_POS = new ArrayList<>(QTD_ITEMS) ;
        ITEM_NAME_POS = new ArrayList<>(QTD_ITEMS) ;
        for (int i = 0 ; i <= QTD_ITEMS - 1 ; i += 1)
        {
            SLOT_CENTER.add(Util.translate(topLeft(), 13, 16 + 24 * i)) ;
            KEY_TEXT_POS.add(Util.translate(SLOT_CENTER.get(i), SLOT_SIZE.width / 2 + 6, SLOT_SIZE.height / 2)) ;
            ITEM_NAME_POS.add(Util.translate(SLOT_CENTER.get(i), - SLOT_SIZE.width / 2 - 10, 0)) ;
        }
    }

    public static void addItem(Item item)
    {
        int slotHovered = getSlotHoveredIndex(GamePanel.getMousePos());

        if (slotHovered == -1) { return ;}

        hotItems.set(slotHovered, item) ;
    }

    public static Item getItem(int slot) { return hotItems.get(slot) ;}

	public static Dimension size() { return Util.getSize(IMAGE) ;}
	public static Point topLeft() { return new Point(BAR_POS.x, BAR_POS.y - size().height) ;}

	public static int getSlotHoveredIndex(Point mousePos)
	{
		for (int i = 0 ; i <= QTD_ITEMS - 1 ; i += 1)
		{
			if (Util.isInside(mousePos, UtilAlignment.getTopLeft(SLOT_CENTER.get(i), Align.center, SLOT_SIZE), SLOT_SIZE))
			{
				return i ;
			}
		}
		
		return -1 ;
	}
	
	public static void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(IMAGE, BAR_POS, Align.bottomLeft) ;
		
		for (int i = 0 ; i <= QTD_ITEMS - 1 ; i += 1)
		{
			GamePanel.getDP().drawImage(SLOT_TRANSPARENT_IMAGE, SLOT_CENTER.get(i), Align.center) ;
			GamePanel.getDP().drawText(KEY_TEXT_POS.get(i), Align.bottomLeft, Player.getHotKeys()[i], FONT, TEXT_COLOR) ;
			
			if (hotItems.get(i) == null) { continue ;}

			GamePanel.getDP().drawImage(hotItems.get(i).getImage(), SLOT_CENTER.get(i), Align.center) ;
			
			if (!Util.isInside(mousePos, UtilAlignment.getTopLeft(SLOT_CENTER.get(i), Align.center, SLOT_SIZE), SLOT_SIZE)) { continue ;}

			GamePanel.getDP().drawText(ITEM_NAME_POS.get(i), Align.centerRight, hotItems.get(i).getName(), FONT, TEXT_COLOR) ;
		}
	}
}