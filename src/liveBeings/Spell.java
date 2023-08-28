package liveBeings ;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;
import components.SpellTypes;
import main.Game;
import utilities.Elements;
import utilities.TimeCounter;
import utilities.UtilG;

public class Spell 
{	
	private String name ;
	private Image image ;
	private int level ;
	private int maxLevel ;
	private int mpCost ;
	private SpellTypes type ;
	private Map<Spell, Integer> preRequisites ;
	private List<Buff> buffs;
	private List<Buff> nerfs;
	private double[] atkMod ;
	private double[] defMod ;
	private double[] dexMod ;
	private double[] agiMod ;
	private double[] atkCritMod ;
	private double[] defCritMod ;
	private double[] stunMod ;
	private double[] blockMod ;
	private double[] bloodMod ;
	private double[] poisonMod ;
	private double[] silenceMod ;
	private boolean isActive ;
	private TimeCounter cooldownCounter ;
	private TimeCounter effectCounter ;
	private Elements elem ;
	private String[] info ;	// Effect and description

	public static final Image cooldownImage = UtilG.loadImage(Game.ImagesPath + "Cooldown.png") ;	
	
	
	public Spell(String name, Image image, int maxLevel, int mpCost, SpellTypes type, Map<Spell, Integer> preRequisites,
			List<Buff> buffs, List<Buff> nerfs, double[] atkMod, double[] defMod, double[] dexMod, double[] agiMod,
			double[] atkCritMod, double[] defCritMod, double[] stunMod, double[] blockMod, double[] bloodMod,
			double[] poisonMod, double[] silenceMod, int cooldown, int duration, Elements elem, String[] info)
	{
		this.name = name;
		this.image = image;
		level = 0;
		this.maxLevel = maxLevel;
		this.mpCost = mpCost;
		this.type = type;
		this.preRequisites = preRequisites;
		this.buffs = buffs;
		this.nerfs = nerfs;
		this.atkMod = atkMod;
		this.defMod = defMod;
		this.dexMod = dexMod;
		this.agiMod = agiMod;
		this.atkCritMod = atkCritMod;
		this.defCritMod = defCritMod;
		this.stunMod = stunMod;
		this.blockMod = blockMod;
		this.bloodMod = bloodMod;
		this.poisonMod = poisonMod;
		this.silenceMod = silenceMod;
		isActive = false;
		cooldownCounter = new TimeCounter(0, cooldown) ;
		effectCounter = new TimeCounter(0, duration) ;
		this.elem = elem;
		this.info = info;
	}

	
	public String getName() {return name ;}
	public Image getImage() {return image ;}
	public int getLevel() {return level ;}
	public int getMaxLevel() {return maxLevel ;}
	public int getMpCost() {return mpCost ;}
	public SpellTypes getType() {return type ;}
	public Map<Spell, Integer> getPreRequisites() {return preRequisites ;}
	public int getCooldown() {return cooldownCounter.getDuration() ;}
	public List<Buff> getBuffs() {return buffs ;}
//	public List<Buff> getNerfs() {return nerfs ;}
	public double[] getAtkMod() {return atkMod ;}
	public double[] getDefMod() {return defMod ;}
	public double[] getDexMod() {return dexMod ;}
	public double[] getAgiMod() {return agiMod ;}
	public double[] getAtkCritMod() {return atkCritMod ;}
	public double[] getDefCritMod() {return defCritMod ;}
	public double[] getStunMod() {return stunMod ;}
	public double[] getBlockMod() {return blockMod ;}
	public double[] getBloodMod() {return bloodMod ;}
	public double[] getPoisonMod() {return poisonMod ;}
	public double[] getSilenceMod() {return silenceMod ;}
	public Elements getElem() {return elem ;}
	public TimeCounter getCooldownCounter() {return cooldownCounter ;}
	public TimeCounter getDurationCounter() {return effectCounter ;}
	public String[] getInfo() {return info ;}
	public TimeCounter getEffectCounter() { return effectCounter ;}

	public boolean isReady() {return cooldownCounter.finished() ;}
	public boolean isActive() {return isActive ;}
	public void activate() {isActive = true ; System.out.println("activate spell " + name);}
	public void deactivate() {isActive = false ; System.out.println("deactivate spell " + name);}

	public static void saveImages()
	{
		BufferedImage img = UtilG.toBufferedImage(UtilG.loadImage(Game.ImagesPath + "genericSpell.png")) ;
		for (int i = 0 ; i <= 172 - 1; i += 1)
		{
			PlayerJobs job = PlayerJobs.jobFromSpellID(i) ;
//			System.out.println("spell" + job + i + ".png");
			try
			{
				UtilG.saveImage(img, Game.ImagesPath + "\\Spells\\" + "spell" + job + i) ;
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void incLevel(int increment)
	{
		if (level + increment <= maxLevel)
		{
			level += increment ;
		}
	}

	public boolean hasPreRequisitesMet()
	{
		return true ;
		
//		for (Spell spell : spells)
//		{
//			if (preRequisites.keySet().contains(spell.getType()) & spell.getLevel() < preRequisites.get(spell))
//			{
//				return false ;
//			}
//		}
//		
//		return true ;
		
	}
	
	public void applyBuff(int mult, LiveBeing receiver, Buff buff)
	{
		Map<Attributes, Double> percIncrease = buff.getPercentIncrease() ;
		Map<Attributes, Double> valueIncrease = buff.getValueIncrease() ;
		for (Attributes att : Attributes.values())
		{
			if (att.equals(Attributes.exp) | att.equals(Attributes.satiation) | att.equals(Attributes.thirst))
			{
				continue ;
			}
			
			BasicAttribute personalAttribute = receiver.getPA().mapAttributes(att) ;
			if (personalAttribute != null)
			{
				double increment = personalAttribute.getMaxValue() * percIncrease.get(att) + valueIncrease.get(att) ;
				personalAttribute.incBonus((int) Math.round(increment * level * mult));
				
				continue ;
			}
			
			BasicBattleAttribute battleAttribute = receiver.getBA().mapAttributes(att) ;
			if (battleAttribute != null)
			{
				double increment = battleAttribute.getBaseValue() * 10 * percIncrease.get(att) + valueIncrease.get(att) ;
				battleAttribute.incBonus(Math.round(increment * level * mult));
				
				continue ;
			}
			
			BattleSpecialAttribute battleSpecialAttribute = receiver.getBA().mapSpecialAttributes(att) ;
			if (battleSpecialAttribute != null)
			{
				battleSpecialAttribute.incAtkChanceBonus(Math.round(percIncrease.get(att) * level * mult));
				battleSpecialAttribute.incAtkChanceBonus(Math.round(valueIncrease.get(att) * level * mult));
			}
		}
	}

	public void applyBuffs(boolean activate, LiveBeing receiver)
	{
		int mult = activate ? 1 : -1 ;

		for (Buff buff : buffs)
		{
			applyBuff(mult, receiver, buff) ;
		}
	}

	public void applyNerfs(boolean activate, LiveBeing receiver)
	{
		int mult = activate ? 1 : -1 ;

		for (Buff nerf : nerfs)
		{
			applyBuff(mult, receiver, nerf) ;
		}
	}
	
	@Override
	public String toString()
	{
		return "Spell [name=" + name + ", image=" + image + ", level=" + level + ", maxLevel=" + maxLevel + ", mpCost=" + mpCost + ", type="
				+ type + ", preRequisites=" + preRequisites + ", buffs=" + buffs + ", AtkMod=" + Arrays.toString(atkMod) + ", DefMod="
				+ Arrays.toString(defMod) + ", DexMod=" + Arrays.toString(dexMod) + ", AgiMod="
				+ Arrays.toString(agiMod) + ", AtkCritMod=" + Arrays.toString(atkCritMod) + ", DefCritMod="
				+ Arrays.toString(defCritMod) + ", StunMod=" + Arrays.toString(stunMod) + ", BlockMod="
				+ Arrays.toString(blockMod) + ", BloodMod=" + Arrays.toString(bloodMod) + ", PoisonMod="
				+ Arrays.toString(poisonMod) + ", SilenceMod=" + Arrays.toString(silenceMod) + ", isActive=" + isActive
				+ ", cooldownCounter=" + cooldownCounter + ", effectCounter=" + effectCounter + ", elem=" + elem
				+ ", info=" + Arrays.toString(info) + "]";
	}

}
