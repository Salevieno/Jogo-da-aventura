package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import animations.MessageAnimation;
import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import items.Equip;
import items.Forge;
import items.GeneralItem;
import items.Item;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class ForgeWindow extends GameWindow
{	
	private List<Equip> itemsForForge ;
	private String message ;
	private BagWindow bag ;

	private static final int QTD_ITEMS_ON_WINDOW = 10 ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Forge.png") ;
	private static final List<String> MESSAGES = Arrays.asList(Game.getAllText().get(TextCategories.forgeWindowMessages)) ;	

	public ForgeWindow()
	{
		super("Forge", Screen.getMe().getPointWithinBorders(0.2, 0.05), IMAGE, 1, 1, 1, 1) ;
		bag = null ;
		// forgeButton = new GameButton(new Point(200, 300), Align.topLeft, null, null, () -> {forge() ;}) ;
		itemsForForge = new ArrayList<>() ;
		message = MESSAGES.get(0) ;
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
		message = MESSAGES.get(i) ;
		Point pos = Util.translate(topLeftPos, 0, - 30) ;
		MessageAnimation.start(pos, message, Palette.colors[0]) ;
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
		
		if (selectedEquip.isAtMaxForgeLevel()) { displayMessage(1) ; return ;}

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
		
		if (-1 < Game.getSaveSlotInUse())
		{
			Game.getPlayer().save(Game.getSaveSlotInUse()) ;
		}
		displayMessage(5) ; return ; 
		
	}
	
	public void display(Point mousePos)
	{

		Point titlePos = Util.translate(topLeftPos, size.width / 2, 16) ;
		Point messagePos = Util.translate(topLeftPos, size.width / 2, 36) ;
		List<Equip> itemsOnWindow = QTD_ITEMS_ON_WINDOW <= itemsForForge.size() ? itemsForForge.subList(0, QTD_ITEMS_ON_WINDOW) : itemsForForge ;
		
		if (itemsOnWindow.size() == 0) { item = -1 ;}
		
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;
		
		GamePanel.getDP().drawText(titlePos, Align.center, name, TITLE_FONT, Palette.colors[1]) ;
		GamePanel.getDP().drawText(messagePos, Align.center, MESSAGES.get(0), STD_FONT, STD_COLOR) ;
		
		Point itemPos = Util.translate(topLeftPos, 24, 70) ;
		
		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
		{			
			if (itemsOnWindow.get(i) == null) { continue ;}
			
			Point namePos = Util.translate(itemPos, 14, 0) ;
			Point runePos = Util.translate(itemPos, 160, 0) ;
			Point pricePos = Util.translate(itemPos, 185, 0) ;
			Point coinPos = Util.translate(itemPos, 210, 0) ;
			
			checkMouseSelection(mousePos, namePos, Align.centerLeft, new Dimension(200, 10), i) ;
			
			Equip equip = itemsOnWindow.get(i) ;
			Color itemColor = this.item == itemsOnWindow.indexOf(equip) ? SELECTED_COLOR : STD_COLOR ;
			GamePanel.getDP().drawImage(Item.getSlotImage(), itemPos, Scale.unit, Align.center) ;
			GamePanel.getDP().drawImage(equip.getImage(), itemPos, Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(namePos, Align.centerLeft, equip.getName() + " + " + equip.getForgeLevel(), STD_FONT, itemColor) ;
			GamePanel.getDP().drawImage(reqRune(equip).getImage(), runePos, Align.center) ;
			
			if (Util.isInside(mousePos, UtilAlignment.getTopLeft(runePos, Align.center, Util.getSize(Item.getSlotImage())), Util.getSize(Item.getSlotImage())))
			{
				Point runeNamePos = Util.translate(runePos, -Item.getSlotImage().getWidth(null) / 2, -Item.getSlotImage().getHeight(null) / 2 - 5) ;
				GamePanel.getDP().drawText(runeNamePos, Align.centerLeft, reqRune(equip).getName(), STD_FONT, STD_COLOR) ;
			}
			
			GamePanel.getDP().drawImage(Player.getCoinImg(), coinPos, Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(pricePos, Align.centerLeft, String.valueOf(forgePrice(equip.getForgeLevel())), STD_FONT, itemColor) ;
			itemPos.y += 28 ;
		}
		
//		forgeButton.display(angle, false, mousePos) ;
		
	}

}
