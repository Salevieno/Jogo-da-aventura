package Items;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import GameComponents.AttributeBonus;
import Main.Game;
import Main.Utg;

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
		super(Name, Description, price, dropChance) ;
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
		int NumEquip = Utg.count(Game.CSVPath + "Item_Equip.csv") ;
		String[][] Input = Utg.ReadTextFile(Game.CSVPath + "Item_Equip.csv", NumEquip) ;
		AllEquip = new Equip[NumEquip] ;
		for (int p = 0; p <= NumEquip - 1; p += 1)
		{
			AllEquip[p] = new Equip(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]),
					Integer.parseInt(Input[p][7]),																																	// forge level
					new AttributeBonus(Integer.parseInt(Input[p][8]), Integer.parseInt(Input[p][9]),																				// life and mp
					Integer.parseInt(Input[p][10]),	Integer.parseInt(Input[p][11]), Integer.parseInt(Input[p][12]), Integer.parseInt(Input[p][13]),									// phyatk magatk phydef magdef
					Integer.parseInt(Input[p][14]), Integer.parseInt(Input[p][15]),																									// dex and agi
					Float.parseFloat(Input[p][16]), Float.parseFloat(Input[p][17]),																									// crit atk and def chance
					Float.parseFloat(Input[p][18]), Float.parseFloat(Input[p][19]), Integer.parseInt(Input[p][20]),																	// stun
					Float.parseFloat(Input[p][21]), Float.parseFloat(Input[p][22]), Integer.parseInt(Input[p][23]),																	// block
					Float.parseFloat(Input[p][24]), Float.parseFloat(Input[p][25]), Integer.parseInt(Input[p][26]), Integer.parseInt(Input[p][27]), Integer.parseInt(Input[p][28]),	// blood
					Float.parseFloat(Input[p][29]), Float.parseFloat(Input[p][30]), Integer.parseInt(Input[p][31]), Integer.parseInt(Input[p][32]), Integer.parseInt(Input[p][33]),	// poison
					Float.parseFloat(Input[p][34]), Float.parseFloat(Input[p][35]), Integer.parseInt(Input[p][36])),																// silence
					Input[p][37]);																																					// elem
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
