package items;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import attributes.AttributeBonus;
import liveBeings.LiveBeing;
import main.Game;
import utilities.Elements;
import utilities.UtilG;

public class Equip extends Item
{
	private int id ;
	private int forgeLevel ;
	private AttributeBonus attBonus ;
	private Elements elem ;
	
	private static Equip[] allEquips ;
	
	public static Image SwordImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq0_Sword.png") ;
	public static Image StaffImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq1_Staff.png") ;
	public static Image BowImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq2_Bow.png") ;
	public static Image ClawsImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq3_Claws.png") ;
	public static Image DaggerImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq4_Dagger.png") ;
	public static Image ShieldImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq5_Shield.png") ;
	public static Image ArmorImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq6_Armor.png") ;
	public static Image ArrowImage = UtilG.loadImage(Game.ImagesPath + "\\Equips\\" + "Eq7_Arrow.png") ;

	public Equip(int id, String name, String description, int price, float dropChance, int forgeLevel, AttributeBonus attBonus, Elements elem)
	{
		super(name, description, UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "items.png"), price, dropChance) ;
		this.id = id ;
		this.forgeLevel = forgeLevel ;
		this.attBonus = attBonus ;
		this.elem = elem ;
	}

	public int getId() {return id ;}
	public int getForgeLevel() {return forgeLevel ;}
	public Elements getElem() {return elem ;}
	public AttributeBonus getAttributeBonus() {return attBonus ;}
	public static Equip[] getAll() {return allEquips ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Equip.csv") ;
		allEquips = new Equip[input.size()] ;
		for (int p = 0; p <= allEquips.length - 1; p += 1)
		{
			allEquips[p] = new Equip(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]),
					Integer.parseInt(input.get(p)[7]),																																	// forge level
					new AttributeBonus(Integer.parseInt(input.get(p)[8]), Integer.parseInt(input.get(p)[9]),																				// life and mp
					Integer.parseInt(input.get(p)[10]),	Integer.parseInt(input.get(p)[11]), Integer.parseInt(input.get(p)[12]), Integer.parseInt(input.get(p)[13]),									// phyatk magatk phydef magdef
					Integer.parseInt(input.get(p)[14]), Integer.parseInt(input.get(p)[15]),																									// dex and agi
					Float.parseFloat(input.get(p)[16]), Float.parseFloat(input.get(p)[17]),																									// crit atk and def chance
					Float.parseFloat(input.get(p)[18]), Float.parseFloat(input.get(p)[19]), Integer.parseInt(input.get(p)[20]),																	// stun
					Float.parseFloat(input.get(p)[21]), Float.parseFloat(input.get(p)[22]), Integer.parseInt(input.get(p)[23]),																	// block
					Float.parseFloat(input.get(p)[24]), Float.parseFloat(input.get(p)[25]), Integer.parseInt(input.get(p)[26]), Integer.parseInt(input.get(p)[27]), Integer.parseInt(input.get(p)[28]),	// blood
					Float.parseFloat(input.get(p)[29]), Float.parseFloat(input.get(p)[30]), Integer.parseInt(input.get(p)[31]), Integer.parseInt(input.get(p)[32]), Integer.parseInt(input.get(p)[33]),	// poison
					Float.parseFloat(input.get(p)[34]), Float.parseFloat(input.get(p)[35]), Integer.parseInt(input.get(p)[36])),																// silence
					Elements.valueOf(input.get(p)[37]));																																					// elem
		}		
	}

	public void use(LiveBeing user)
	{
//		int NumberOfEquipTypes = 3 ;	// Sword/Staff/Bow/Claws/Dagger, shield, armor/robe (Archers have bow, bandana, and armor)
//		int EquipType = (EquipID + job) % NumberOfEquipTypes ;
//		Equip currentEquip = equips[EquipType] ;
//		if (currentEquip != null)	// Unnequip the current equip
//		{
//			if (user.SetIsFormed(equips))	// if the set was formed, remove the 20% bonus
//			{
//				ApplyEquipsBonus(equips[0], (double)-0.2) ;
//				ApplyEquipsBonus(equips[1], (double)-0.2) ;
//				ApplyEquipsBonus(equips[2], (double)-0.2) ;
//			}
//			equips[EquipType] = null ;
//			user.getElem()[EquipType + 1] = Elements.neutral ;
//			ApplyEquipsBonus(currentEquip, -1) ;
//		}
//		
//		if (currentEquip == null)
//		{
//			equips[EquipType] = Equip.getAll()[EquipID] ;
//			user.getElem()[EquipType + 1] = equips[EquipType].getElem() ;
//			ApplyEquipsBonus(equips[EquipType], 1) ;
//			if (user.SetIsFormed(equips))	// if the set is formed, add the 20% bonus
//			{
//				ApplyEquipsBonus(equips[0], (double)0.2) ;
//				ApplyEquipsBonus(equips[1], (double)0.2) ;
//				ApplyEquipsBonus(equips[2], (double)0.2) ;
//			}
//		}
//			
//		user.getElem()[4] = user.hasSuperElement() ? user.getElem()[1] : Elements.neutral ;
	}
	
	public void printAtt()
	{
		System.out.print("Equip id: " + allEquips[id].getId() +
				"   name: " + allEquips[id].getName() +
				"   description: " + allEquips[id].getDescription() +
				"   price: " + allEquips[id].getPrice() +
				"   drop chance: " + allEquips[id].getDropChance() + "%" + 
				"   forge level: " + allEquips[id].getForgeLevel() + " ") ;
		allEquips[id].getAttributeBonus().printAtt() ;
		System.out.println("   elem: " + allEquips[id].getElem());
	}
	
}
