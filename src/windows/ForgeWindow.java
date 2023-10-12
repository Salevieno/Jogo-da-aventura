package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
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
	private String message ;
	private static final List<String> messages = Arrays.asList(
			"Selecione o equipamento",
			"O equipamento já está no nível máximo!",
			"Você precisa de uma runa apropriada!",
			"Você precisa de mais ouro!",
			"Item forjado!",
			"Essa não! A forja não funcionou!") ;
	private static final int NumberItemsPerWindow = 10 ;
	private static final Image windowImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Forge.png") ;
	
	private Point windowPos = Game.getScreen().getPointWithinBorders(0.2, 0.1) ;

	public ForgeWindow()
	{
		super("Forge", windowImage, 1, 1, 1, 1) ;
		itemsForForge = null ;
		message = messages.get(0) ;
		item = 0 ;
	}	

	public void setItemsForForge(List<Equip> itemsForForge)
	{
		this.itemsForForge = itemsForForge ;
		numberItems = itemsForForge.size() ;
	}

	public void updateMessage(int i)
	{
		message = messages.get(i) ;
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
	
	public void act(BagWindow bag, String action)
	{
		if (menu == 0 & Player.actionIsForward(action))
		{
			forge(bag) ;
		}
	}
	
	public Equip selectedEquip() { if (item == -1) { return null ;} return itemsForForge.get(item) ;}
	
	public void forge(BagWindow bag)
	{

		Equip selectedEquip = selectedEquip() ;
		
//		if (selectedEquip == null) { updateMessage() ; return 2 ;}
		
		if (selectedEquip.getForgeLevel() == Equip.maxForgeLevel) { updateMessage(1) ; return ;}

		int runeId = selectedEquip.isSpecial() ? 20 : 0 ;
		runeId += 2 * selectedEquip.getForgeLevel() ;
		runeId += selectedEquip.isWeapon() ? 0 : 1 ;
		Forge rune = Forge.getAll()[runeId] ;

		if (!bag.contains(rune)) { updateMessage(2) ; return ;}

		int forgePrice = 100 + 1000 * selectedEquip.getForgeLevel() ;


		if (!bag.hasEnoughGold(forgePrice)) { updateMessage(3) ; return ;}


		double chanceForge = 1 - 0.08 * selectedEquip.getForgeLevel() ;

		bag.removeGold(forgePrice) ;
		bag.remove(rune, 1) ;


		if (Math.random() <= chanceForge)
		{
			selectedEquip.incForgeLevel() ;
			
			updateMessage(4) ; return ;
		}
		
		selectedEquip.resetForgeLevel() ;
		bag.remove(selectedEquip, 1);
		
		updateMessage(5) ; return ; 


		// TODO overwrite save
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{

		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 16) ;
		Point messagePos = UtilG.Translate(windowPos, size.width / 2, 36) ;
		double angle = DrawingOnPanel.stdAngle ;
		List<Equip> itemsOnWindow = NumberItemsPerWindow <= itemsForForge.size() ? itemsForForge.subList(0, NumberItemsPerWindow) : itemsForForge ;
		
		if (itemsOnWindow.size() == 0) { item = -1 ;}
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.colorPalette[2]) ;
		DP.DrawText(messagePos, Align.center, angle, message, stdFont, stdColor) ;
		
		Point itemPos = UtilG.Translate(windowPos, 24, 70) ;
		for (Equip item : itemsOnWindow)
		{
			if (item == null) { continue ;}
			
			Point namePos = UtilG.Translate(itemPos, 14, -6) ;
			
			Color itemColor = this.item == itemsOnWindow.indexOf(item) ? selColor : stdColor ;
			DP.DrawImage(Item.slot, itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawImage(item.getImage(), itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawText(namePos, Align.topLeft, angle, item.getName() + " + " + item.getForgeLevel(), stdFont, itemColor) ;
			itemPos.y += 28 ;
		}
		
	}

}
