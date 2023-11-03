package items;

import java.awt.Image;
import java.util.List;

import attributes.BattleAttributes;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import utilities.UtilG;
import utilities.UtilS;

public class Arrow extends Item
{
	private float atkPower ;
	private String elem ;
	
	private static Arrow[] AllArrow ;

	private static final Image woodArrowIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconWoodArrow.png") ;
	private static final Image strongArrowIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconStrongArrow.png") ;
	private static final Image boltArrowIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconBoltArrow.png") ;
	
	private static final Image ArrowImage = UtilS.loadImage("\\Equips\\" + "Eq7_Arrow.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Arrow.csv") ;
		AllArrow = new Arrow[input.size()] ;
		for (int p = 0; p <= AllArrow.length - 1; p += 1)
		{
			AllArrow[p] = new Arrow(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3],
					Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]),
					Float.parseFloat(input.get(p)[7]), input.get(p)[8]);
		}
	}
	
	public Arrow(int id, String Name, String Description, int price, float dropChance, float atkPower, String elem)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.atkPower = atkPower ;
		this.elem = elem ;
	}

	public float getAtkPower() {return atkPower ;}
	public String getElem() {return elem ;}
	public static Arrow[] getAll() {return AllArrow ;}

	public static Image imageFromID(int id)
	{
		if (id % 3 == 0) { return woodArrowIcon ;}
		if (id % 3 == 1) { return strongArrowIcon ;}
		if (id % 3 == 2) { return boltArrowIcon ;}
		
		return null ;
	}

	public Image fullSizeImage()
	{
		return ArrowImage ;
	}
	
	public void use(LiveBeing user)
	{
		if (!(user instanceof Player)) { return ;}

		Player player = (Player) user ;
		Arrow arrow = Arrow.getAll()[id] ;
		
		if (player.getEquippedArrow() == arrow)
		{
			// unequip
			applyBonus(user.getBA(), arrow, -1) ;
			player.setEquippedArrow(null) ;
			
			return ;
		}
		
		// equip		
		if (!player.getBag().contains(arrow)) { return ;}
		applyBonus(user.getBA(), arrow, 1) ;
		player.setEquippedArrow(arrow) ;
	}
	
	private void applyBonus(BattleAttributes BA, Arrow arrow, double mult)
	{
		BA.getPhyAtk().incBonus(atkPower * mult) ;
	}

	@Override
	public String toString()
	{
//		return "Arrow [atkPower=" + atkPower + ", elem=" + elem + ", id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "Arrow," + id + "," + name;
	}
	
	

//	public void printAtt()
//	{
//		System.out.println("Arrow id: " + AllArrow[id].getId() +
//				"   name: " + AllArrow[id].getName() +
//				"   description: " + AllArrow[id].getDescription() +
//				"   price: " + AllArrow[id].getPrice() +
//				"   drop chance: " + AllArrow[id].getDropChance() + "%" + 
//				"   atk power: " + AllArrow[id].getAtkPower() + 
//				"   elem: " + AllArrow[id].getElem());
//	}
	
}
