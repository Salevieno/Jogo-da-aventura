package items;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import maps.GroundTypes;
import utilities.UtilG;

public class GeneralItem extends Item
{
	private int id ;
	
	private static GeneralItem[] AllGeneralItems ;
	public GeneralItem(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "items.png"), price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static GeneralItem[] getAll() {return AllGeneralItems ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_GeneralItem.csv") ;
		AllGeneralItems = new GeneralItem[input.size()] ;
		for (int p = 0; p <= AllGeneralItems.length - 1; p += 1)
		{
			AllGeneralItems[p] = new GeneralItem(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
		}		
	}

	public void use(LiveBeing user)
	{
		
		switch (id)
		{
			case 27: 
			{
				if (!(user instanceof Player) | !user.isTouching(GroundTypes.water))
				{
					return ;
				}
				
				((Player) user).getBag().Remove(this, 1) ;
				((Player) user).getBag().Add(AllGeneralItems[30], 1) ;
			}
			case 28: 
			{
				if (!(user instanceof Player) | !user.isTouching(GroundTypes.water))
				{
					return ;
				}
				
				((Player) user).getBag().Remove(this, 1) ;
				((Player) user).getBag().Add(AllGeneralItems[31], 1) ;
			}
			case 29: 
			{
				if (!(user instanceof Player) | !user.isTouching(GroundTypes.water))
				{
					return ;
				}
				
				((Player) user).getBag().Remove(this, 1) ;
				((Player) user).getBag().Add(AllGeneralItems[32], 1) ;
			}
			case 30: user.getPA().getThirst().incCurrentValue(30) ; return ;
			case 31: user.getPA().getThirst().incCurrentValue(60) ; return ;
			case 32: user.getPA().getThirst().incCurrentValue(100) ; return ;
		}
				
	}
	
	public void printAtt()
	{
		System.out.println("General item id: " + AllGeneralItems[id].getId() +
				"   name: " + AllGeneralItems[id].getName() +
				"   description: " + AllGeneralItems[id].getDescription() +
				"   price: " + AllGeneralItems[id].getPrice() +
				"   drop chance: " + AllGeneralItems[id].getDropChance() + "%");
	}
	
}
