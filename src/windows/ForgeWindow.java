package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import components.GameButton;
import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import items.Equip;
import items.Forge;
import items.GeneralItem;
import items.Item;
import liveBeings.Player;
import main.Game;
import main.TextCategories;
import utilities.Util;
import utilities.UtilS;

public class ForgeWindow extends GameWindow
{
	
	private List<Equip> itemsForForge ;
	private String message ;
	private GameButton forgeButton ;
	private BagWindow bag ;
	private static final List<String> messages ;
	
	private static final int NumberItemsPerWindow = 10 ;
	private static final Point windowPos = Game.getScreen().getPointWithinBorders(0.2, 0.05) ;
	private static final Image windowImage = UtilS.loadImage("\\Windows\\" + "Forge.png") ;
	

	static
	{
		messages = Arrays.asList(Game.allText.get(TextCategories.forgeWindowMessages)) ;
	}
	
	public ForgeWindow()
	{
		super("Forge", windowPos, windowImage, 1, 1, 1, 1) ;
		bag = null ;
		forgeButton = new GameButton(new Point(200, 300), Align.topLeft, null, null, () -> {forge() ;}) ;
		itemsForForge = null ;
		message = messages.get(0) ;
		item = 0 ;
	}	

	public void setItemsForForge(List<Equip> itemsForForge)
	{
		this.itemsForForge = itemsForForge ;
		numberItems = itemsForForge.size() ;
	}
	
	public void setBag(BagWindow bag) { this.bag = bag ;}

	public void displayMessage(int i)
	{
		message = messages.get(i) ;
		Point pos = Util.Translate(windowPos, 0, - 30) ;
		Animation.start(AnimationTypes.message, new Object[] {pos, message, Game.palette[0]}) ;
	}
	
	public void navigate(String action)
	{
		if (action == null) { return ;}
		
		if (action.equals(stdMenuDown))
		{
			itemUp() ;
		}
		if (action.equals(stdMenuUp))
		{
			itemDown() ;
		}
	}
	
	public void act(String action)
	{
		if (menu == 0 & actionIsForward(action))
		{
			forge() ;
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
	
	private double forgeChanceBonus(Equip selectedEquip, BagWindow bag)
	{
		int runeId = selectedEquip.isSpecial() ? 20 : 0 ;
		runeId += 2 * selectedEquip.getForgeLevel() ;
		runeId += selectedEquip.isWeapon() ? 0 : 1 ;
		int runeType = Forge.typeFromID(runeId) ;
		
		Item bonusItem = switch (runeType)
		{
			case 0 -> GeneralItem.getAll()[80] ;
			case 1 -> GeneralItem.getAll()[99] ;
			case 2 -> GeneralItem.getAll()[90] ;
			case 3 -> GeneralItem.getAll()[100] ;
			default -> null ;
		};
		
		if (bonusItem == null || !bag.contains(bonusItem)) { return 0.0 ;}
		
		bag.remove(bonusItem, 1) ;
		return 0.1 ;
	}
	
	public void forge()
	{
		
		Equip selectedEquip = selectedEquip() ;
		
		if (selectedEquip == null) { return ;}
		
		if (selectedEquip.getForgeLevel() == Equip.maxForgeLevel) { displayMessage(1) ; return ;}

		Forge rune = reqRune(selectedEquip) ;

		if (!bag.contains(rune)) { displayMessage(2) ; return ;}

		int forgeLevel = selectedEquip.getForgeLevel() ;
		int forgePrice = forgePrice(forgeLevel) ;


		if (!bag.hasEnoughGold(forgePrice)) { displayMessage(3) ; return ;}

		double chanceBonus = forgeChanceBonus(selectedEquip, bag) ;
		double chanceForge = 1 - 0.08 * forgeLevel + chanceBonus ;

		bag.removeGold(forgePrice) ;
		bag.remove(rune, 1) ;


		if (Util.chance(chanceForge))
		{
			selectedEquip.incForgeLevel() ;
			
			displayMessage(4) ; return ;
		}
		
		selectedEquip.resetForgeLevel() ;
		bag.remove(selectedEquip, 1);
		
		if (-1 < Game.getSlotLoaded())
		{
			Game.getPlayer().save(Game.getSlotLoaded()) ;
		}
		displayMessage(5) ; return ; 
		
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{

		Point titlePos = Util.Translate(windowPos, size.width / 2, 16) ;
		Point messagePos = Util.Translate(windowPos, size.width / 2, 36) ;
		double angle = Draw.stdAngle ;
		List<Equip> itemsOnWindow = NumberItemsPerWindow <= itemsForForge.size() ? itemsForForge.subList(0, NumberItemsPerWindow) : itemsForForge ;
		
		if (itemsOnWindow.size() == 0) { item = -1 ;}
		
		DP.drawImage(image, windowPos, angle, Scale.unit, Align.topLeft, stdOpacity) ;
		
		DP.drawText(titlePos, Align.center, angle, name, titleFont, Game.palette[1]) ;
		DP.drawText(messagePos, Align.center, angle, messages.get(0), stdFont, stdColor) ;
		
		Point itemPos = Util.Translate(windowPos, 24, 70) ;
		
		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
		{			
			if (itemsOnWindow.get(i) == null) { continue ;}
			
			Point namePos = Util.Translate(itemPos, 14, 0) ;
			Point runePos = Util.Translate(itemPos, 160, 0) ;
			Point pricePos = Util.Translate(itemPos, 185, 0) ;
			Point coinPos = Util.Translate(itemPos, 210, 0) ;
			
			checkMouseSelection(mousePos, namePos, Align.centerLeft, new Dimension(200, 10), i) ;
			
			Equip equip = itemsOnWindow.get(i) ;
			Color itemColor = this.item == itemsOnWindow.indexOf(equip) ? selColor : stdColor ;
			DP.drawImage(Item.slot, itemPos, angle, Scale.unit, Align.center) ;
			DP.drawImage(equip.getImage(), itemPos, angle, Scale.unit, Align.center) ;
			DP.drawText(namePos, Align.centerLeft, angle, equip.getName() + " + " + equip.getForgeLevel(), stdFont, itemColor) ;
			DP.drawImage(reqRune(equip).getImage(), runePos, Align.center) ;
			
			if (Util.isInside(mousePos, Util.getTopLeft(runePos, Align.center, Util.getSize(Item.slot)), Util.getSize(Item.slot)))
			{
				Point runeNamePos = Util.Translate(runePos, -Item.slot.getWidth(null) / 2, -Item.slot.getHeight(null) / 2 - 5) ;
				DP.drawText(runeNamePos, Align.centerLeft, angle, reqRune(equip).getName(), stdFont, stdColor) ;
			}
			
			DP.drawImage(Player.CoinIcon, coinPos, angle, Scale.unit, Align.center) ;
			DP.drawText(pricePos, Align.centerLeft, angle, String.valueOf(forgePrice(equip.getForgeLevel())), stdFont, itemColor) ;
			itemPos.y += 28 ;
		}
		
//		forgeButton.display(angle, false, mousePos, DP) ;
		
	}

}
