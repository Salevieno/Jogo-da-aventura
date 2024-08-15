package items;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import attributes.Attributes;
import graphics.Draw;
import graphics.DrawPrimitives;
import libUtil.Align;
import libUtil.Util;
import liveBeings.AttackModifiers;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import maps.GroundTypes;
import utilities.Elements;
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
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "Item_GeneralItem.csv") ;
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

		// TODO item effects for ids 82, 83, 86, 87, 89, 109 & 111
		((Player) user).getBag().remove(this, 1) ;
		
		switch (id)
		{
			case 21:
				if (!user.isPlayerAlly()) { return ;}
				
				user.getHpCounter().reset() ;
				user.getHpCounter().start() ;
				
				return ;
				
			case 22:
				if (!user.isPlayerAlly()) { return ;}
				
				user.getBA().getStatus().get(Attributes.poison).reset() ;
				
				return ;
				
			case 23:
				if (!user.isPlayerAlly()) { return ;}
				
				user.getPA().getLife().incCurrentValue(20) ;
				return ;
				
			case 27, 28, 29: 
				if (!(user instanceof Player) | !user.isTouching(GroundTypes.water)) { return ;}
				
				((Player) user).getBag().add(AllGeneralItems[id + 3], 1) ;
				return ;
			
			case 30: user.getPA().getThirst().incCurrentValue(30) ;  return ;
			case 31: user.getPA().getThirst().incCurrentValue(60) ;  return ;
			case 32: user.getPA().getThirst().incCurrentValue(100) ;  return ;
			case 74: user.getsDrunk(20) ;  return ;
			case 79:
				user.getAtkSpeedBonusCounter().reset() ;
				user.getAtkSpeedBonusCounter().start() ;
				user.getBattleActionCounter().setDuration(user.getBattleActionCounter().getDuration()/1.2) ;
				return ;
				
			case 81, 84: user.getBA().getStatus().get(Attributes.blood).reset() ; return ;
			case 105: user.getsDrunk(50) ;  return ;
			case 106: user.getsDrunk(150) ;  return ;
		}
				
	}

	public void displayInfo(Point pos, Align align, DrawPrimitives DP)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 9) ;
		Point textPos = Util.Translate(pos, 5 - Util.getSize(infoMenu).width, 10) ;		
		DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, description, font, Game.colorPalette[0]) ;
	}
	
	@Override
	public String toString()
	{
		return name ;
	}
}
