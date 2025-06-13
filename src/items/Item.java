package items;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import graphics.Align;
import utilities.UtilS;

public abstract class Item
{
	protected final int id ;
	protected final String name ;
	protected final String description ;
	protected final Image image ;
	protected final int price ;
	protected final double dropChance ;
	
	protected static final Image infoMenu = UtilS.loadImage("itemInfoWindow.png") ;
	public static final List<Item> allItems = new ArrayList<>() ;
	

    public static Image slot = UtilS.loadImage("itemSlot.png") ;
	
	public Item(int id, String name, String description, Image image, int price, double dropChance)
	{
		this.id = id ;
		this.name = name ;
		this.description = description ;
		this.image = image ;
		this.price = price ;
		this.dropChance = dropChance ;
//		allItems.add(this);
	}

	public static List<Item> getItems(int[] itemIDs)
	{
		return Arrays.stream(itemIDs).mapToObj(itemID -> allItems.get(itemID)).collect(Collectors.toList());
	}
	
	public int getId() {return id ;}
	public String getName() {return name ;}
	public String getDescription() {return description ;}
	public Image getImage() {return image ;}
	public int getPrice() {return price ;}
	public double getDropChance() {return dropChance ;}
	
	public abstract void displayInfo(Point pos, Align align) ;
	
	public static void load()
	{
//		List<Item> allItems = new ArrayList<>() ;
		for (int i = 0 ; i <= Potion.getAll().length - 1 ; i += 1)
		{
			allItems.add(Potion.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Alchemy.getAll().length - 1 ; i += 1)
		{
			allItems.add(Alchemy.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Forge.getAll().length - 1 ; i += 1)
		{
			allItems.add(Forge.getAll()[i]) ;
		}
		for (int i = 0 ; i <= PetItem.getAll().length - 1 ; i += 1)
		{
			allItems.add(PetItem.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Food.getAll().length - 1 ; i += 1)
		{
			allItems.add(Food.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Arrow.getAll().length - 1 ; i += 1)
		{
			allItems.add(Arrow.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Equip.getAll().length - 1 ; i += 1)
		{
			allItems.add(Equip.getAll()[i]) ;
		}
		for (int i = 0 ; i <= GeneralItem.getAll().length - 1 ; i += 1)
		{
			allItems.add(GeneralItem.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Fab.getAll().length - 1 ; i += 1)
		{
			allItems.add(Fab.getAll()[i]) ;
		}
		for (int i = 0 ; i <= QuestItem.getAll().length - 1 ; i += 1)
		{
			allItems.add(QuestItem.getAll()[i]) ;
		}
	}
	
	@Override
	public String toString()
	{
		return "Item," + id + "," + name;
	}
}
