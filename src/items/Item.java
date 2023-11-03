package items;

import java.awt.Image;

import utilities.UtilS;

public abstract class Item
{
	protected int id ;
	protected String name ;
	protected String description ;
	protected Image image ;
	protected int price ;
	protected double dropChance ;
	

    public static Image slot = UtilS.loadImage("itemSlot.png") ;
	
	public Item(int id, String name, String description, Image image, int price, double dropChance)
	{
		this.id = id ;
		this.name = name ;
		this.description = description ;
		this.image = image ;
		this.price = price ;
		this.dropChance = dropChance ;
	}

	public int getId() {return id ;}
	public String getName() {return name ;}
	public String getDescription() {return description ;}
	public Image getImage() {return image ;}
	public int getPrice() {return price ;}
	public double getDropChance() {return dropChance ;}
	
	@Override
	public String toString()
	{
		return "Item," + id + "," + name;
	}
}
