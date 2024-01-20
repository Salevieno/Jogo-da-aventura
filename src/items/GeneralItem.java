package items;

import java.awt.Image;
import java.util.List;

import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import maps.GroundTypes;
import utilities.Elements;
import utilities.UtilG;
import utilities.UtilS;

public class GeneralItem extends Item
{
	private double power ;
	private Elements elem ;
	
	private static GeneralItem[] AllGeneralItems ;
	
	private static final Image generalItemIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconGenItem.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_GeneralItem.csv") ;
		AllGeneralItems = new GeneralItem[input.size()] ;
		for (int p = 0; p <= AllGeneralItems.length - 1; p += 1)
		{
			int id = Integer.parseInt(input.get(p)[0]) ;
			String name = input.get(p)[1] ;
			String description = input.get(p)[3] ;
			int price = Integer.parseInt(input.get(p)[5]) ;
			double dropChance = Double.parseDouble(input.get(p)[6]) ;
			AllGeneralItems[p] = new GeneralItem(id, name, description, price, dropChance) ;
		}
	}
	
	public boolean isThrowable() { return 0 < power ;}
	
	public GeneralItem(int id, String Name, String Description, int price, double dropChance)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		power = 0 ;
		elem = Elements.neutral ;
	}

	public static GeneralItem[] getAll() {return AllGeneralItems ;}
	
	public double getPower() { return power ;}
	
	public Elements getElem() { return elem ;}
	
	public static Image imageFromID(int id)
	{		
		return generalItemIcon ;		
	}
	
	public void use(LiveBeing user)
	{
		
		switch (id)
		{
//			case 0:
//				if (!(user instanceof Player)) { return ;}
//				if (!((Player) user).isInBattle()) { return ;}
//				
//				Battle.throwItem(user, ((Player) user).getOpponent(), 10, Elements.air) ;
//				((Player) user).getBag().remove(this, 1) ;
//				
//				return ;
//				
			case 27: 
			{
				if (!(user instanceof Player) | !user.isTouching(GroundTypes.water))
				{
					return ;
				}
				
				((Player) user).getBag().remove(this, 1) ;
				((Player) user).getBag().add(AllGeneralItems[30], 1) ;
			}
			case 28: 
			{
				if (!(user instanceof Player) | !user.isTouching(GroundTypes.water))
				{
					return ;
				}
				
				((Player) user).getBag().remove(this, 1) ;
				((Player) user).getBag().add(AllGeneralItems[31], 1) ;
			}
			case 29: 
			{
				if (!(user instanceof Player) | !user.isTouching(GroundTypes.water))
				{
					return ;
				}
				
				((Player) user).getBag().remove(this, 1) ;
				((Player) user).getBag().add(AllGeneralItems[32], 1) ;
			}
			case 30: user.getPA().getThirst().incCurrentValue(30) ; return ; // TODO esses não estão consumindo o item
			case 31: user.getPA().getThirst().incCurrentValue(60) ; return ;
			case 32: user.getPA().getThirst().incCurrentValue(100) ; return ;
		}
				
	}
	
	
	@Override
	public String toString()
	{
		return "GeneralItem," + id + "," + name ;
	}
}
