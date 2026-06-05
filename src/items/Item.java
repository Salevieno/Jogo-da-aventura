package items;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import graphics.Align;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;


public abstract class Item
{
	protected final int id ;
	protected String name ;
	protected String description ;
	protected final Image image ;
	protected final int price ;
	protected final double dropChance ;
	
	protected static final Image INFO_MENU_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "itemInfoWindow.png") ;
    private static final Image SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "itemSlot.png") ;
	private static final List<Item> ALL = new ArrayList<>() ;
	
	public Item(int id, String name, String description, Image image, int price, double dropChance)
	{
		this.id = id ;
		this.name = name ;
		this.description = description ;
		this.image = image ;
		this.price = price ;
		this.dropChance = dropChance ;
		ALL.add(this) ;
	}

	public static List<Item> getItems(int[] itemIDs)
	{
		return Arrays.stream(itemIDs).mapToObj(itemID -> ALL.get(itemID)).collect(Collectors.toList());
	}
	
	public int getId() { return id ;}
	public String getName() { return name ;}
	public String getDescription() { return description ;}
	public Image getImage() { return image ;}
	public int getPrice() { return price ;}
	public double getDropChance() { return dropChance ;}
	public static Image getSlotImage() { return SLOT_IMAGE ;}
	public static List<Item> getAllItems() { return ALL ;}
	public void setName(String name) { this.name = name ;}
	public void setDescription(String description) { this.description = description ;}

	protected static void drawMenu(Point pos, Align align, Dimension size)
	{
		GamePanel.getDP().drawRoundRect(pos, align, size, 1, Palette.colors[3], Palette.colors[0], true);
	}
	
	public abstract void displayInfo(Point pos, Align align) ;
	
	public static void load(String language)
	{
		ItemsData.createPotions() ;
		ItemsData.createAlchemy() ;
		ItemsData.createForge() ;
		ItemsData.createPetItem() ;
		ItemsData.createFood() ;
		ItemsData.createArrows() ;
		ItemsData.createEquips() ;
		ItemsData.createGeneralItems() ;
		ItemsData.createFab() ;
		ItemsData.createQuest() ;
		Potion.updateText(language) ;
		Alchemy.updateText(language) ;
		Forge.updateText(language) ;
		PetItem.updateText(language) ;
		Food.updateText(language) ;
		Arrow.updateText(language);
		Equip.updateText(language) ;
		GeneralItem.updateText(language) ;
		Fab.updateText(language) ;
		QuestItem.updateText(language) ;
	}
	
	@Override
	public String toString()
	{
		return "Item," + id + "," + name;
	}
}
