package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.Align;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import liveBeings.Pet;
import main.Game;
import utilities.Util;
import utilities.UtilS;

public class PetItem extends Item
{
	private final float lifeHeal ;
	private final float mpHeal ;
	private final int satiationHeal ;
	private final int power ;
	
	private static final PetItem[] AllPetItems ;

	private static final Image petLifePotion = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconPetLifePotion.png") ;
	private static final Image petMPPotion = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconPetMPPotion.png") ;
	private static final Image petFood = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconPetFood.png") ;
	private static final Image petSet = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconPetSet.png") ;
	
	static
	{
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "Item_PetItem.csv") ;
		AllPetItems = new PetItem[input.size()] ;
		for (int p = 0; p <= AllPetItems.length - 1; p += 1)
		{
			AllPetItems[p] = new PetItem(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]), Float.parseFloat(input.get(p)[7]), Float.parseFloat(input.get(p)[8]), Integer.parseInt(input.get(p)[9]));
		}	
	}
	public PetItem(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.mpHeal = MPHeal ;
		this.satiationHeal = SatiationHeal ;
		this.power = id / 4 ;
	}

	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return mpHeal ;}	
	public int getSatiationHeal() {return satiationHeal ;}	
	public static PetItem[] getAll() {return AllPetItems ;}

	public static boolean isLifePotion(int id) { return id % 4 == 0 ;}
	public static boolean isMpPotion(int id) { return id % 4 == 1 ;}
	public static boolean isFood(int id) { return id % 4 == 2 ;}
	public static boolean isEquipSet(int id) { return id % 4 == 3 ;}
	
	public Image fullSizeImage() { return petSet ;}

	public static Image imageFromID(int id)
	{		
		if (isLifePotion(id)) { return petLifePotion ;}
		if (isMpPotion(id)) { return petMPPotion ;}
		if (isFood(id)) { return petFood ;}
		if (isEquipSet(id)) { return petSet ;}
		
		return null ;
	}
	
	private void equip(Pet pet, PetItem equip)
	{
//		pet.setElem(equip.elem) ;
		
		applyBonus(pet.getPA(), pet.getBA(), equip, 1) ;

		Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.4, 0.3), equip.getName() + " equipado!", Game.palette[0]}) ;
//		pet.getElem()[4] = pet.hasSuperElement() ? pet.getElem()[1] : Elements.neutral ;
		pet.setEquip(equip) ;
	}
	
	private void unequip(Pet pet, PetItem equip)
	{
		applyBonus(pet.getPA(), pet.getBA(), equip, -1) ;
//		pet.setElem(Elements.neutral) ;
		
		Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.4, 0.36), equip.getName() + " desequipado!", Game.palette[0]}) ;
		pet.setEquip(null) ;
	}
	
	public void use(Pet pet)
	{
		// TODO pro - customizar pet item power
		if (isLifePotion(id))
		{
			pet.getLife().incCurrentValue(20 + 10 * power) ;
		}
		if (isMpPotion(id))
		{
			pet.getMp().incCurrentValue(20 + 10 * power) ;
		}
		if (isFood(id))
		{
			pet.getSatiation().incCurrentValue(20 + 10 * power) ;
		}
		if (isEquipSet(id))
		{
			boolean isEquippingTheSameEquip = pet.getEquip() != null ? pet.getEquip().getId() == id : false ;
			if (pet.getEquip() != null)
			{
				unequip(pet, pet.getEquip()) ;
			}
			
			if (!isEquippingTheSameEquip)
			{
				equip(pet, PetItem.getAll()[id]) ;
			}
		}
	}

	private void applyBonus(PersonalAttributes PA, BattleAttributes BA, PetItem equip, double mult)
	{
//		AttributeBonus attBonus = new AttributeBonus() ;
		BA.getPhyDef().incBonus(10 * (1 + 0.2 * equip.power) * mult) ;
		BA.getMagDef().incBonus(10 * (1 + 0.2 * equip.power) * mult) ;
//		PA.getLife().incMaxValue((int) (attBonus.getLife() * mult)) ;
//		PA.getMp().incMaxValue((int) (attBonus.getMP() * mult)) ;
//		BA.getPhyAtk().incBonus(attBonus.getPhyAtk() * mult) ;
//		BA.getMagAtk().incBonus(attBonus.getMagAtk() * mult) ;
//		BA.getDex().incBonus(attBonus.getDex() * mult) ;
//		BA.getAgi().incBonus(attBonus.getAgi() * mult) ;
//		BA.getCritAtk().incBonus(attBonus.getCritAtkChance() * mult) ;
//		BA.getCritDef().incBonus(attBonus.getCritDefChance() * mult) ;
//		BA.getStun().incAtkChanceBonus(attBonus.getStunAtkChance() * mult) ;
//		BA.getStun().incDefChanceBonus(attBonus.getStunDefChance() * mult) ;
//		BA.getStun().incDuration(attBonus.getStunDuration() * mult) ;
//		BA.getBlock().incAtkChanceBonus(attBonus.getBlockAtkChance() * mult) ;
//		BA.getBlock().incDefChanceBonus(attBonus.getBlockDefChance() * mult) ;
//		BA.getBlock().incDuration(attBonus.getBlockDuration() * mult) ;
//		BA.getBlood().incAtkChanceBonus(attBonus.getBloodAtkChance() * mult) ;
//		BA.getBlood().incDefChanceBonus(attBonus.getBloodDefChance() * mult) ;
//		BA.getBlood().incAtkBonus(attBonus.getBloodAtk() * mult) ;
//		BA.getBlood().incDefBonus(attBonus.getBloodDef() * mult) ;
//		BA.getBlood().incDuration(attBonus.getBloodDuration() * mult) ;
//		BA.getPoison().incAtkChanceBonus(attBonus.getPoisonAtkChance() * mult) ;
//		BA.getPoison().incDefChanceBonus(attBonus.getPoisonDefChance() * mult) ;
//		BA.getPoison().incAtkBonus(attBonus.getPoisonAtk() * mult) ;
//		BA.getPoison().incDefBonus(attBonus.getPoisonDef() * mult) ;
//		BA.getPoison().incDuration(attBonus.getPoisonDuration() * mult) ;
//		BA.getSilence().incAtkChanceBonus(attBonus.getSilenceAtkChance() * mult) ;
//		BA.getSilence().incDefChanceBonus(attBonus.getSilenceDefChance() * mult) ;
//		BA.getSilence().incDuration(attBonus.getSilenceDuration() * mult) ;
	}
	
	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
	
	@Override
	public String toString()
	{
//		return "PetItem [lifeHeal=" + lifeHeal + ", MPHeal=" + MPHeal + ", SatiationHeal=" + SatiationHeal + ", id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "PetItem," + id + "," + name;
	}
	
}
