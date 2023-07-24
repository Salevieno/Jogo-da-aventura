package items;

import java.awt.Image;

import main.Game;
import utilities.UtilG;

public abstract class Item
{
	protected int id ;
	protected String name ;
	protected String description ;
	protected Image image ;
	protected int price ;
	protected double dropChance ;
	

    public static Image slot = UtilG.loadImage(Game.ImagesPath + "itemSlot.png") ;
	
	public Item(int id, String Name, String Description, Image image, int price, double dropChance)
	{
		this.id = id ;
		this.name = Name ;
		this.description = Description ;
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
	
}
