package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import animations.MessageAnimation;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.Align;
import graphics2.Draw;
import liveBeings.Pet;
import main.Game;
import main.ImageLoader;
import main.Palette;
import main.Path;
import utilities.Util;


public class PetItem extends Item
{
	private final double lifeHeal ;
	private final double mpHeal ;
	private final int satiationHeal ;
	private final int power ;
	
	private static final Image petLifePotion = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconPetLifePotion.png") ;
	private static final Image petMPPotion = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconPetMPPotion.png") ;
	private static final Image petFood = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconPetFood.png") ;
	private static final Image petSet = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconPetSet.png") ;
	private static final PetItem[] all = new PetItem[60] ;

	public PetItem(int id, int price, double dropChance, double lifeHeal, double mpHeal, int satiationHeal)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.mpHeal = mpHeal ;
		this.satiationHeal = satiationHeal ;
		this.power = id / 4 ;
		all[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/PetItemText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			all[id].setName(line[1]) ;
			all[id].setDescription(line[2]) ;
		}
	}

	public double getLifeHeal() {return lifeHeal ;}
	public double getMPHeal() {return mpHeal ;}	
	public int getSatiationHeal() {return satiationHeal ;}	
	public static PetItem[] getAll() {return all ;}

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

		// Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.4, 0.3), equip.getName() + " equipado!", Palette.colors[0]}) ;
		MessageAnimation.start(Game.getScreen().pos(0.4, 0.3), equip.getName() + " equipado!", Palette.colors[0]) ;

		//		pet.getElem()[4] = pet.hasSuperElement() ? pet.getElem()[1] : Elements.neutral ;
		pet.setEquip(equip) ;
	}
	
	private void unequip(Pet pet, PetItem equip)
	{
		applyBonus(pet.getPA(), pet.getBA(), equip, -1) ;
//		pet.setElem(Elements.neutral) ;
		
		// Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.4, 0.36), equip.getName() + " desequipado!", Palette.colors[0]}) ;
		MessageAnimation.start(Game.getScreen().pos(0.4, 0.36), equip.getName() + " desequipado!", Palette.colors[0]) ;
		pet.setEquip(null) ;
	}
	
	public void use(Pet pet)
	{
		// proTODO - customizar pet item power
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
