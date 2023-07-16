package items;

import java.awt.Image;
import java.util.List;

import attributes.AttributeBonus;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import utilities.Elements;
import utilities.UtilG;

public class Arrow extends Item
{
	private int id ;
	private float atkPower ;
	private String elem ;
	
	private static Arrow[] AllArrow ;

	private static final Image woodArrowIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconWoodArrow.png") ;
	private static final Image strongArrowIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconStrongArrow.png") ;
	private static final Image boltArrowIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconBoltArrow.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Arrow.csv") ;
		AllArrow = new Arrow[input.size()] ;
		for (int p = 0; p <= AllArrow.length - 1; p += 1)
		{
			AllArrow[p] = new Arrow(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]), Float.parseFloat(input.get(p)[7]), input.get(p)[8]);
		}
	}
	
	public Arrow(int id, String Name, String Description, int price, float dropChance, float atkPower, String elem)
	{
		super(Name, Description, imageFromID(id), price, dropChance) ;
		this.id = id ;
		this.atkPower = atkPower ;
		this.elem = elem ;
	}

	public int getId() {return id ;}
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
	// TODO equip arrow
	public void use(LiveBeing user)
	{
		if (!(user instanceof Player)) { return ;}

		Player player = (Player) user ;
		
		if (player.getEquips()[3] == Arrow.getAll()[id])
		{
			// unequip
			applyBonus(user.getPA(), user.getBA(), Arrow.getAll()[id], -1) ;
			user.getElem()[3 + 1] = Elements.neutral ;
			player.getEquips()[3] = null ;
			
			return ;
		}
		
		// equip
		player.getEquips()[3] = Arrow.getAll()[id] ;
		user.getElem()[3 + 1] = Arrow.getAll()[id].elem ;
		applyBonus(user.getPA(), user.getBA(), Arrow.getAll()[id], 1) ;
			
		user.getElem()[4] = user.hasSuperElement() ? user.getElem()[1] : Elements.neutral ;
	}
	
	private void applyBonus(PersonalAttributes PA, BattleAttributes BA, Arrow arrow, double mult)
	{
		AttributeBonus attBonus = arrow.getAttributeBonus() ;
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

	public void printAtt()
	{
		System.out.println("Arrow id: " + AllArrow[id].getId() +
				"   name: " + AllArrow[id].getName() +
				"   description: " + AllArrow[id].getDescription() +
				"   price: " + AllArrow[id].getPrice() +
				"   drop chance: " + AllArrow[id].getDropChance() + "%" + 
				"   atk power: " + AllArrow[id].getAtkPower() + 
				"   elem: " + AllArrow[id].getElem());
	}
	
}
