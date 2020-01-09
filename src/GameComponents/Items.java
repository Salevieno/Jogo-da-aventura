package GameComponents;

public class Items
{
	private int ID;
	private String Name;
	private int Price;
	private float DropChance;
	
	public Items(int ID, String Name, int Price, float DropChance)
	{
		this.ID = ID;
		this.Name = Name;
		this.Price = Price;
		this.DropChance = DropChance;
	}

	public int getID() {return ID;}
	public String getName() {return Name;}
	public int getPrice() {return Price;}
	public float getDropChance() {return DropChance;}
	public void setID(int id) {ID = id;}
	public void setName(String N) {Name = N;}
	public void setPrice(int P) {Price = P;}
	public void setDropChance(float DC) {DropChance = DC;}
}