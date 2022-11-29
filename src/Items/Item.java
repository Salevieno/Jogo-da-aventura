package Items;

import java.awt.Image;

public class Item
{
	protected String name ;
	protected String description ;
	protected Image image ;
	protected int price ;
	protected float dropChance ;
	
	public Item(String Name, String Description, Image image, int price, float dropChance)
	{
		this.name = Name ;
		this.description = Description ;
		this.image = image ;
		this.price = price ;
		this.dropChance = dropChance ;
	}
	
	public String getName() {return name ;}
	public String getDescription() {return description ;}
	public Image getImage() {return image ;}
	public int getPrice() {return price ;}
	public float getDropChance() {return dropChance ;}
}
