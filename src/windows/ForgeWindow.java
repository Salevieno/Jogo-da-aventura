package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.DrawingOnPanel;
import items.Equip;
import items.Forge;
import items.Item;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class ForgeWindow extends GameWindow
{
	private List<Equip> itemsForForge ;
	private static final int NumberItemsPerWindow = 10 ;
	
	public ForgeWindow()
	{
		super("Forge", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Forge.png"), 1, 1, 1, 1) ;
		itemsForForge = null ;
		item = 0 ;
	}
	
	

	public void setItemsForForge(List<Equip> itemsForForge)
	{
		this.itemsForForge = itemsForForge;
	}



	public void navigate(String action)
	{
		if (action == null) { return ;}
		
		if (action.equals(Player.ActionKeys[2]))
		{
			itemUp() ;
		}
		if (action.equals(Player.ActionKeys[0]))
		{
			itemDown() ;
		}
	}
	
	public Equip selectedEquip() { return itemsForForge.get(item) ;}
	
	public void forge(BagWindow bag)
	{

		Equip selectedEquip = selectedEquip() ;
		
		if (selectedEquip.getForgeLevel() == 10) { menu = 2 ; return ;}
		
		int runeId = selectedEquip.isSpecial() ? 20 : 0 ;
		runeId += 2 * selectedEquip.getForgeLevel() ;
		runeId += selectedEquip.isWeapon() ? 0 : 1 ;
		Forge rune = Forge.getAll()[runeId] ;

		if (!bag.contains(rune)) { menu = 3 ; return ;}
		
		int forgePrice = 100 + 1000 * selectedEquip.getForgeLevel() ;


		if (!bag.hasEnoughGold(forgePrice)) { menu = 4 ; return ;}


		double chanceForge = 1 - 0.08 * selectedEquip.getForgeLevel() ;

		bag.removeGold(forgePrice) ;
		bag.Remove(rune, 1) ;


		if (Math.random() <= chanceForge)
		{
			selectedEquip.incForgeLevel() ;
			
			menu = 5 ; 
			return ;
		}
		
		menu = 6 ; 
		selectedEquip.resetForgeLevel() ;
		bag.Remove(selectedEquip, 1);


		// TODO overwrite save
	}
	
	public void display(DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		Point itemPos = UtilG.Translate(windowPos, 30, 70) ;
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 28) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font titleFont = new Font(Game.MainFontName, Font.BOLD, 16) ;
		double angle = DrawingOnPanel.stdAngle ;
		List<Equip> itemsOnWindow = NumberItemsPerWindow <= itemsForForge.size() ? itemsForForge.subList(0, NumberItemsPerWindow) : itemsForForge ;
		
		if (itemsOnWindow.size() == 0) { item = -1 ;}
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.ColorPalette[2]) ;
		for (Equip item : itemsOnWindow)
		{
			if (item == null) { continue ;}
			
			Point namePos = UtilG.Translate(itemPos, 20, -6) ;
			
			Color itemColor = this.item == itemsOnWindow.indexOf(item) ? Game.ColorPalette[6] : Game.ColorPalette[9] ;
			DP.DrawImage(Item.slot, itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawImage(item.getImage(), itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawText(namePos, Align.topLeft, angle, item.getName() + " + " + item.getForgeLevel(), font, itemColor) ;
			itemPos.y += 30 ;
		}
		
	}

}
