package windows;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.DrawingOnPanel;
import items.Item;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class CraftWindow extends GameWindow
{
	private static int NumberItemsPerWindow = 10 ;
	private List<Item> ItemsForCrafting ;
	private Image image ;

	public CraftWindow(List<Item> ItemsForCrafting)
	{
		super("Cria��o", null, 1, 1, NumberItemsPerWindow, ItemsForCrafting.size() / NumberItemsPerWindow + 1) ;
		image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Craft.png") ;
		this.ItemsForCrafting = ItemsForCrafting ;
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
	
	public void display(Point MousePos, DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.5*Game.getScreen().getSize().width), (int)(0.3*Game.getScreen().getSize().height)) ;
		Point itemPos = UtilG.Translate(windowPos, 30, 30) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		List<Item> ItemsInWindow = ItemsForCrafting.subList(0, NumberItemsPerWindow) ;
		
		DP.DrawImage(image, windowPos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.topLeft) ;
		for (Item item : ItemsInWindow)
		{
			DP.DrawImage(item.getImage(), itemPos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.centerLeft) ;
			DP.DrawText(UtilG.Translate(itemPos, 20, 0), Align.centerLeft, DrawingOnPanel.stdAngle, item.getName(), font, Game.ColorPalette[8]) ;
			itemPos.y += 20 ;
		}
	}
}
