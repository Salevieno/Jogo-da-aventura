package items;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Game;
import utilities.UtilG;

public class Alchemy extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	
	private static Alchemy[] AllAlchemy ;
	public Alchemy(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal)
	{
		super(Name, Description, UtilG.loadImage(Game.ImagesPath + "Col0__Berry.png"), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		
	}
	public int getId() {return id ;}
	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}
	public static Alchemy[] getAll() {return AllAlchemy ;}
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Alchemy.csv") ;
		AllAlchemy = new Alchemy[input.size()] ;
		for (int a = 0; a <= AllAlchemy.length - 1; a += 1)
		{
			AllAlchemy[a] = new Alchemy(Integer.parseInt(input.get(a)[0]), input.get(a)[1], input.get(a)[3], Integer.parseInt(input.get(a)[5]), Float.parseFloat(input.get(a)[6]), Float.parseFloat(input.get(a)[7]), Float.parseFloat(input.get(a)[8]));
		}
	}
	
	public void printAtt()
	{
		System.out.println("alchemy id: " + AllAlchemy[id].getId() +
				"   name: " + AllAlchemy[id].getName() +
				"   description: " + AllAlchemy[id].getDescription() +
				"   price: " + AllAlchemy[id].getPrice() +
				"   drop chance: " + AllAlchemy[id].getDropChance() + "%" + 
				"   life heal: " + 100 * AllAlchemy[id].getLifeHeal() + "%" + 
				"   mp heal: " + 100 * AllAlchemy[id].getMPHeal() + "%");
	}
	
}
