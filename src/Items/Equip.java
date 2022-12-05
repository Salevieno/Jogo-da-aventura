package Items;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import LiveBeings.AttributeBonus;
import Main.Game;
import Utilities.UtilG;

public class Equip extends Item
{
	private int id ;
	private int forgeLevel ;
	private AttributeBonus attBonus ;
	private String elem ;
	
	private static Equip[] AllEquip ;
	
	public static Image SwordImage = new ImageIcon(Game.ImagesPath + "Eq0_Sword.png").getImage() ;
	public static Image StaffImage = new ImageIcon(Game.ImagesPath + "Eq1_Staff.png").getImage() ;
	public static Image BowImage = new ImageIcon(Game.ImagesPath + "Eq2_Bow.png").getImage() ;
	public static Image ClawsImage = new ImageIcon(Game.ImagesPath + "Eq3_Claws.png").getImage() ;
	public static Image DaggerImage = new ImageIcon(Game.ImagesPath + "Eq4_Dagger.png").getImage() ;
	public static Image ShieldImage = new ImageIcon(Game.ImagesPath + "Eq5_Shield.png").getImage() ;
	public static Image ArmorImage = new ImageIcon(Game.ImagesPath + "Eq6_Armor.png").getImage() ;
	public static Image ArrowImage = new ImageIcon(Game.ImagesPath + "Eq7_Arrow.png").getImage() ;
	//public static Image[] EquipImage = new Image[] {Sword, Staff, Bow, Claws, Dagger, Shield, Armor, Arrow} ;
	public Equip(int id, String Name, String Description, int price, float dropChance, int forgeLevel, AttributeBonus attBonus, String elem)
	{
		super(Name, Description, new ImageIcon(Game.ImagesPath + "items.png").getImage(), price, dropChance) ;
		this.id = id ;
		this.forgeLevel = forgeLevel ;
		this.attBonus = attBonus ;
		this.elem = elem ;
	}

	public int getId() {return id ;}
	public int getForgeLevel() {return forgeLevel ;}
	public String getElem() {return elem ;}
	public AttributeBonus getAttributeBonus() {return attBonus ;}
	public static Equip[] getAll() {return AllEquip ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Equip.csv") ;
		AllEquip = new Equip[input.size()] ;
		for (int p = 0; p <= AllEquip.length - 1; p += 1)
		{
			AllEquip[p] = new Equip(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]),
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
					input.get(p)[37]);																																					// elem
		}		
	}

	public void printAtt()
	{
		System.out.print("Equip id: " + AllEquip[id].getId() +
				"   name: " + AllEquip[id].getName() +
				"   description: " + AllEquip[id].getDescription() +
				"   price: " + AllEquip[id].getPrice() +
				"   drop chance: " + AllEquip[id].getDropChance() + "%" + 
				"   forge level: " + AllEquip[id].getForgeLevel() + " ") ;
		AllEquip[id].getAttributeBonus().printAtt() ;
		System.out.println("   elem: " + AllEquip[id].getElem());
	}
	
}
