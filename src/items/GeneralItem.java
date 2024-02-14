package items;

import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import graphics.Draw;
import graphics.DrawPrimitives;
import liveBeings.AttackModifiers;
import liveBeings.Buff;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import maps.GroundTypes;
import utilities.Align;
import utilities.Elements;
import utilities.UtilG;
import utilities.UtilS;

public class GeneralItem extends Item
{
	private double power ;
	private AttackModifiers atkMod ;
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
			int power = Integer.parseInt(input.get(p)[7]) ;
			AttackModifiers atkMod = calcAtkMod(id) ;
			Elements elem = Elements.valueOf(input.get(p)[8]) ;
			AllGeneralItems[p] = new GeneralItem(id, name, description, price, dropChance, power, atkMod, elem) ;
		}
	}
	
	private static AttackModifiers calcAtkMod(int id)
	{
		return switch (id)
		{
			case 64 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[] {0.5, 0, 0}, new double[3]) ;
			case 65 -> new AttackModifiers(new double[3], new double[3], new double[] {1, 0, 0}, new double[3], new double[3]) ;
			case 66 -> new AttackModifiers(new double[3], new double[] {0.5, 0, 0}, new double[3], new double[3], new double[3]) ;
			case 67 -> new AttackModifiers(new double[] {0.5, 0, 0}, new double[3], new double[3], new double[3], new double[3]) ;
			case 69 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[3], new double[] {1, 0, 0}) ;
			case 71 -> new AttackModifiers(new double[] {1, 0, 0}, new double[3], new double[3], new double[3], new double[3]) ;
			case 73 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[] {0.8, 0, 0}, new double[3]) ;
			case 75 -> new AttackModifiers(new double[3], new double[] {1, 0, 0}, new double[3], new double[3], new double[3]) ;
			case 78 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[] {1, 0, 0}, new double[3]) ;
			default -> new AttackModifiers() ;
		};
		// TODO item effects for ids 21, 22, 23, 25, 74, 79, 80, 81, 82, 83, 84, 86, 87, 89, 90, 99, 100, 105, 106, 109 & 111
	}
	
	public static List<Item> throwableItems() { return Arrays.asList(AllGeneralItems).stream().filter(item -> 0 < item.power).collect(Collectors.toList()) ;}
	
	public boolean isThrowable() { return 0 < power ;}
	
	public GeneralItem(int id, String Name, String Description, int price, double dropChance, int power, AttackModifiers atkMod, Elements elem)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.power = power ;
		this.atkMod = atkMod ;
		this.elem = elem ;
	}

	public static GeneralItem[] getAll() {return AllGeneralItems ;}
	
	public double getPower() { return power ;}
	public Elements getElem() { return elem ;}
	public AttackModifiers getAtkMod() { return atkMod ;}
	
	public static Image imageFromID(int id)
	{		
		return generalItemIcon ;		
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
			case 30: user.getPA().getThirst().incCurrentValue(30) ; ((Player) user).getBag().remove(this, 1) ; return ;
			case 31: user.getPA().getThirst().incCurrentValue(60) ; ((Player) user).getBag().remove(this, 1) ; return ;
			case 32: user.getPA().getThirst().incCurrentValue(100) ; ((Player) user).getBag().remove(this, 1) ; return ;
		}
				
	}

	public void displayInfo(Point pos, Align align, DrawPrimitives DP)
	{
		Draw.menu(pos, align, UtilG.getSize(infoMenu)) ;
	}
	
	@Override
	public String toString()
	{
		return name ;
	}
}
