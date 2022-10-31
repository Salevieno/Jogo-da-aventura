package Items;

import java.io.IOException;

import Main.Game;
import Utilities.UtilG;

public class Arrow extends Item
{
	private int id ;
	private float atkPower ;
	private String elem ;
	
	private static Arrow[] AllArrow ;
	public Arrow(int id, String Name, String Description, int price, float dropChance, float atkPower, String elem)
	{
		super(Name, Description, price, dropChance) ;
		this.id = id ;
		this.atkPower = atkPower ;
		this.elem = elem ;
	}

	public int getId() {return id ;}
	public float getAtkPower() {return atkPower ;}
	public String getElem() {return elem ;}
	public static Arrow[] getAll() {return AllArrow ;}
	
	
	public static void Initialize() throws IOException
	{
		int NumArrow = UtilG.count(Game.CSVPath + "Item_Arrow.csv") ;
		String[][] Input = UtilG.ReadTextFile(Game.CSVPath + "Item_Arrow.csv", NumArrow) ;
		AllArrow = new Arrow[NumArrow] ;
		for (int p = 0; p <= NumArrow - 1; p += 1)
		{
			AllArrow[p] = new Arrow(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]), Float.parseFloat(Input[p][7]), Input[p][8]);
		}		
	}

	public void printAtt()
	{
		System.out.println("Arrow id: " + AllArrow[id].getId() +
				"   name: " + AllArrow[id].getName() +
				"   description: " + AllArrow[id].getDescription() +
				"   price: " + AllArrow[id].getPrice() +
				"   drop chance: " + AllArrow[id].getDropChance() + "%" + 
				"   atk power: " + AllArrow[id].getAtkPower() + 
				"   elem: " + AllArrow[id].getElem());
	}
	
}
