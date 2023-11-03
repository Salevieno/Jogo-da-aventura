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
import utilities.UtilS;

public class ForgeWindow extends GameWindow
{
	
	private List<Equip> itemsForForge ;
	private String message ;
	// TODO colocar esse texto no json
	private static final List<String> messages = Arrays.asList(
			"Selecione o equipamento",
			"O equipamento já está no nível máximo!",
			"Você precisa de uma runa apropriada!",
			"Você precisa de mais ouro!",
			"Item forjado!",
			"Essa não! A forja não funcionou!") ;
	private static final int NumberItemsPerWindow = 10 ;
	private static final Image windowImage = UtilS.loadImage("\\Windows\\" + "Forge.png") ;
	
	private static final Point windowPos = Game.getScreen().getPointWithinBorders(0.2, 0.1) ;

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

	public void displayMessage(int i)
	{
		message = messages.get(i) ;
		Point pos = UtilG.Translate(windowPos, 0, - 30) ;
		Game.getAnimations().get(12).start(200, new Object[] {pos, message, Game.colorPalette[0]}) ;
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
	
	public int forgePrice(int forgeLevel) { return 30 + 100 * forgeLevel + 30 * forgeLevel * forgeLevel ;}
	
	public Forge reqRune(Equip selectedEquip)
	{
		int runeId = selectedEquip.isSpecial() ? 20 : 0 ;
		runeId += 2 * selectedEquip.getForgeLevel() ;
		runeId += selectedEquip.isWeapon() ? 0 : 1 ;
		return Forge.getAll()[runeId] ;
	}
	
	public void forge(BagWindow bag)
	{

		Equip selectedEquip = selectedEquip() ;
		
		if (selectedEquip == null) { return ;}
		
		if (selectedEquip.getForgeLevel() == Equip.maxForgeLevel) { displayMessage(1) ; return ;}

		Forge rune = reqRune(selectedEquip) ;

		if (!bag.contains(rune)) { displayMessage(2) ; return ;}

		int forgeLevel = selectedEquip.getForgeLevel() ; ;
		int forgePrice = forgePrice(forgeLevel) ;


		if (!bag.hasEnoughGold(forgePrice)) { displayMessage(3) ; return ;}


		double chanceForge = 1 - 0.08 * forgeLevel ;

		bag.removeGold(forgePrice) ;
		bag.remove(rune, 1) ;


		if (UtilG.chance(chanceForge))
		{
			selectedEquip.incForgeLevel() ;
			
			displayMessage(4) ; return ;
		}
		
		selectedEquip.resetForgeLevel() ;
		bag.remove(selectedEquip, 1);
		
		displayMessage(5) ; return ; 


		// TODO overwrite save
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{

		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 16) ;
		Point messagePos = UtilG.Translate(windowPos, size.width / 2, 36) ;
		double angle = DrawingOnPanel.stdAngle ;
		List<Equip> itemsOnWindow = NumberItemsPerWindow <= itemsForForge.size() ? itemsForForge.subList(0, NumberItemsPerWindow) : itemsForForge ;
		
		if (itemsOnWindow.size() == 0) { item = -1 ;}
		
		DP.DrawImage(image, windowPos, angle, Scale.unit, Align.topLeft) ;
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.colorPalette[1]) ;
		DP.DrawText(messagePos, Align.center, angle, "Selecione o equipamento", stdFont, stdColor) ;
		
		Point itemPos = UtilG.Translate(windowPos, 24, 70) ;
		for (Equip item : itemsOnWindow)
		{
			if (item == null) { continue ;}
			
			Point namePos = UtilG.Translate(itemPos, 14, -6) ;
			Point runePos = UtilG.Translate(itemPos, 154, -6) ;
			Point coinPos = UtilG.Translate(itemPos, 264, -6) ;
			Point pricePos = UtilG.Translate(itemPos, 274, -6) ;
			
			Color itemColor = this.item == itemsOnWindow.indexOf(item) ? selColor : stdColor ;
			DP.DrawImage(Item.slot, itemPos, angle, Scale.unit, Align.center) ;
			DP.DrawImage(item.getImage(), itemPos, angle, Scale.unit, Align.center) ;
			DP.DrawText(namePos, Align.centerLeft, angle, item.getName() + " + " + item.getForgeLevel(), stdFont, itemColor) ;
			// TODO draw required rune
			DP.DrawText(runePos, Align.centerLeft, angle, reqRune(item).getName(), stdFont, itemColor) ;
			DP.DrawImage(Player.CoinIcon, coinPos, angle, Scale.unit, Align.center) ;
			DP.DrawText(pricePos, Align.centerLeft, angle, String.valueOf(forgePrice(item.getForgeLevel())), stdFont, itemColor) ;
			itemPos.y += 28 ;
		}
		
	}

}
