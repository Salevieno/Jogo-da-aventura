package items;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;

import attributes.AttributeBonus;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import liveBeings.LiveBeing;
import liveBeings.Player;
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
	
	private static Image swordIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconSword.png") ;
	private static Image staffIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconStaff.png") ;
	private static Image bowIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconBow.png") ;
	private static Image clawsIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconClaws.png") ;
	private static Image daggerIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconDagger.png") ;
	private static Image shieldIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconShield.png") ;
	private static Image armorIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconArmor.png") ;
	
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
		super(name, description, imageFromID(id), price, dropChance) ;
		this.id = id ;
		this.forgeLevel = forgeLevel ;
		this.attBonus = attBonus ;
		this.elem = elem ;
	}

	
	public static Image imageFromID(int id)
	{
		return id % 3 == 0 ? swordIcon :
		id % 3 == 1 ? shieldIcon :
			armorIcon ;
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

	public boolean isSpecial()
	{
		if (0 <= id & id <= 99) { return false ;}
		if (200 <= id & id <= 299) { return false ;}
		if (400 <= id & id <= 499) { return false ;}
		if (600 <= id & id <= 699) { return false ;}
		if (800 <= id & id <= 899) { return false ;}
		
		return true ;
	}

	public boolean isWeapon()
	{
		if (0 <= id & id <= 99) { return id % 3 == 0 ;}
		if (100 <= id & id <= 199) { return id % 3 == 1 ;}
		if (200 <= id & id <= 299) { return id % 3 == 2 ;}
		if (300 <= id & id <= 399) { return id % 3 == 0 ;}
		if (400 <= id & id <= 499) { return id % 3 == 1 ;}
		if (500 <= id & id <= 599) { return id % 3 == 2 ;}
		if (600 <= id & id <= 699) { return id % 3 == 0 ;}
		if (700 <= id & id <= 799) { return id % 3 == 1 ;}
		if (800 <= id & id <= 899) { return id % 3 == 2 ;}
		if (900 <= id & id <= 999) { return id % 3 == 0 ;}
		
		System.out.println("Verificação se o equipamento é uma arma com item que não é equipamento");
		return false ;
	}
	
	public void resetForgeLevel()
	{
		attBonus.resetAll() ;
		forgeLevel = 0 ;
	}
	
	public void incForgeLevel()
	{
		double forgeBonus = 0.1 ;
		double[] bonuses = attBonus.all() ;
		double[] increment = new double[bonuses.length] ;
		for (int i = 0 ; i <= bonuses.length - 1; i += 1)
		{
			increment[i] = forgeBonus * bonuses[i] ;
		}
		attBonus.inc(increment) ;
		System.out.println(attBonus);
		forgeLevel += 1 ;
	}
	
	private void applyBonus(PersonalAttributes PA, BattleAttributes BA, Equip equip, double mult)
	{
		AttributeBonus attBonus = equip.getAttributeBonus() ;
		PA.getLife().incMaxValue((int) (attBonus.getLife() * mult)) ;
		PA.getMp().incMaxValue((int) (attBonus.getMP() * mult)) ;
		BA.getPhyAtk().incBonus(attBonus.getPhyAtk() * mult) ;
		BA.getMagAtk().incBonus(attBonus.getMagAtk() * mult) ;
		BA.getPhyDef().incBonus(attBonus.getPhyDef() * mult) ;
		BA.getMagDef().incBonus(attBonus.getMagDef() * mult) ;
		BA.getDex().incBonus(attBonus.getDex() * mult) ;
		BA.getAgi().incBonus(attBonus.getAgi() * mult) ;
		BA.getCrit()[0] += attBonus.getCritAtkChance() * mult ;
		BA.getCrit()[2] += attBonus.getCritDefChance() * mult ;
		BA.getStun().incAtkChanceBonus(attBonus.getStunAtkChance() * mult) ;
		BA.getStun().incDefChanceBonus(attBonus.getStunDefChance() * mult) ;
		BA.getStun().incDuration(attBonus.getStunDuration() * mult) ;
		BA.getBlock().incAtkChanceBonus(attBonus.getBlockAtkChance() * mult) ;
		BA.getBlock().incDefChanceBonus(attBonus.getBlockDefChance() * mult) ;
		BA.getBlock().incDuration(attBonus.getBlockDuration() * mult) ;
		BA.getBlood().incAtkChanceBonus(attBonus.getBloodAtkChance() * mult) ;
		BA.getBlood().incDefChanceBonus(attBonus.getBloodDefChance() * mult) ;
		BA.getBlood().incAtkBonus(attBonus.getBloodAtk() * mult) ;
		BA.getBlood().incDefBonus(attBonus.getBloodDef() * mult) ;
		BA.getBlood().incDuration(attBonus.getBloodDuration() * mult) ;
		BA.getPoison().incAtkChanceBonus(attBonus.getPoisonAtkChance() * mult) ;
		BA.getPoison().incDefChanceBonus(attBonus.getPoisonDefChance() * mult) ;
		BA.getPoison().incAtkBonus(attBonus.getPoisonAtk() * mult) ;
		BA.getPoison().incDefBonus(attBonus.getPoisonDef() * mult) ;
		BA.getPoison().incDuration(attBonus.getPoisonDuration() * mult) ;
		BA.getSilence().incAtkChanceBonus(attBonus.getSilenceAtkChance() * mult) ;
		BA.getSilence().incDefChanceBonus(attBonus.getSilenceDefChance() * mult) ;
		BA.getSilence().incDuration(attBonus.getSilenceDuration() * mult) ;
	}	
	
	public void use(LiveBeing user)
	{
		int type = (id) % 3 ;
		double setBonus = 0.2 ;
		
		if (!(user instanceof Player)) { return ;}

		Player player = (Player) user ;
		
		if (player.getEquips()[type] == Equip.getAll()[id])
		{
			// unequip
			applyBonus(user.getPA(), user.getBA(), Equip.getAll()[id], -1) ;
			user.getElem()[type + 1] = Elements.neutral ;
			if (Player.setIsFormed(player.getEquips()))
			{
				applyBonus(user.getPA(), user.getBA(), player.getEquips()[0], -setBonus) ;
				applyBonus(user.getPA(), user.getBA(), player.getEquips()[1], -setBonus) ;
				applyBonus(user.getPA(), user.getBA(), player.getEquips()[2], -setBonus) ;
			}				
			player.getEquips()[type] = null ;
			
			return ;
		}
		
		// equip
		player.getEquips()[type] = Equip.getAll()[id] ;
		user.getElem()[type + 1] = Equip.getAll()[id].elem ;				
		if (Player.setIsFormed(player.getEquips()))
		{
			applyBonus(user.getPA(), user.getBA(), player.getEquips()[0], setBonus) ;
			applyBonus(user.getPA(), user.getBA(), player.getEquips()[1], setBonus) ;
			applyBonus(user.getPA(), user.getBA(), player.getEquips()[2], setBonus) ;
		}				
		applyBonus(user.getPA(), user.getBA(), Equip.getAll()[id], 1) ;
			
		user.getElem()[4] = user.hasSuperElement() ? user.getElem()[1] : Elements.neutral ;
	}
	
	public void printAtt()
	{
		System.out.print("Equip id: " + allEquips[id].getId() +
				"   name: " + allEquips[id].getName() +
				"   description: " + allEquips[id].getDescription() +
				"   price: " + allEquips[id].getPrice() +
				"   drop chance: " + allEquips[id].getDropChance() + "%" + 
				"   forge level: " + allEquips[id].getForgeLevel() + " " +
				"   elem: " + allEquips[id].getElem());
	}
	


	@Override
	public String toString()
	{
		return "Equip [id=" + id + ", forgeLevel=" + forgeLevel + ", attBonus=" + attBonus + ", elem=" + elem + "]";
	}
	
	
}
