package Items;

public class Item
{
	protected String Name ;
	protected String Description ;
	protected int price ;
	protected float dropChance ;
	public Item(String Name, String Description, int price, float dropChance)
	{
		this.Name = Name ;
		this.Description = Description ;
		this.price = price ;
		this.dropChance = dropChance ;
	}
	
	public String getName() {return Name ;}
	public String getDescription() {return Description ;}
	public int getPrice() {return price ;}
	public float getDropChance() {return dropChance ;}
}
