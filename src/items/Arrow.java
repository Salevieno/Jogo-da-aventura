package items;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import main.Game;
import utilities.UtilG;

public class Arrow extends Item
{
	private int id ;
	private float atkPower ;
	private String elem ;
	
	private static Arrow[] AllArrow ;

	private static Image arrowIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconArrow.png") ;
	
	public Arrow(int id, String Name, String Description, int price, float dropChance, float atkPower, String elem)
	{
		super(Name, Description, arrowIcon, price, dropChance) ;
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
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Arrow.csv") ;
		AllArrow = new Arrow[input.size()] ;
		for (int p = 0; p <= AllArrow.length - 1; p += 1)
		{
			AllArrow[p] = new Arrow(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]), Float.parseFloat(input.get(p)[7]), input.get(p)[8]);
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
