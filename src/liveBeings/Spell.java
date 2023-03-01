package liveBeings ;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;
import attributes.PersonalAttributes;
import components.SpellTypes;
import utilities.Elements;
import utilities.TimeCounter;

public class Spell 
{	
	private String name ;
	private int level ;
	private int maxLevel ;
	private int mpCost ;
	private SpellTypes type ;
	private Map<SpellType, Integer> preRequisites ;
	//private int cooldown ;
	//private int effectDuration ;
	private List<Buff> buffs;
	private List<Buff> nerfs;
	private double[] AtkMod ;
	private double[] DefMod ;
	private double[] DexMod ;
	private double[] AgiMod ;
	private double[] AtkCritMod ;
	private double[] DefCritMod ;
	private double[] StunMod ;
	private double[] BlockMod ;
	private double[] BloodMod ;
	private double[] PoisonMod ;
	private double[] SilenceMod ;
	private boolean isActive ;
	//private int cooldownCounter ;
	//private int effectCounter ;
	private TimeCounter cooldownCounter ;
	private TimeCounter effectCounter ;
	private Elements elem ;

	private String[] info ;	// Effect and description
	
	public Spell(SpellType spellType)
	{
		this.name = spellType.getName() ;
		level = 0 ;
		this.maxLevel = spellType.getMaxLevel() ;
		this.mpCost = spellType.getMpCost() ;
		this.type = spellType.getType() ;
		this.preRequisites = spellType.getPreRequisites() ;
		//this.cooldown = spellType.getCooldown() ;
		//this.effectDuration = spellType.getEffectDuration() ;
		this.buffs = spellType.getBuffs() ;
		this.nerfs = spellType.getNerfs() ;
		this.AtkMod = spellType.getAtkMod() ;
		this.DefMod = spellType.getDefMod() ;
		this.DexMod = spellType.getDexMod() ;
		this.AgiMod = spellType.getAgiMod() ;
		this.AtkCritMod = spellType.getAtkCritMod() ;
		this.DefCritMod = spellType.getDefCritMod() ;
		this.StunMod = spellType.getStunMod() ;
		this.BlockMod = spellType.getBlockMod() ;
		this.BloodMod = spellType.getBloodMod() ;
		this.PoisonMod = spellType.getPoisonMod() ;
		this.SilenceMod = spellType.getSilenceMod() ;
		isActive = false ;
		cooldownCounter = new TimeCounter(0, spellType.getCooldown()) ;
		effectCounter = new TimeCounter(0, spellType.getEffectDuration()) ;
		this.elem = spellType.getElem() ;
		this.info = spellType.getInfo() ;
	}
	
	public String getName() {return name ;}
	public int getLevel() {return level ;}
	public int getMaxLevel() {return maxLevel ;}
	public int getMpCost() {return mpCost ;}
	public SpellTypes getType() {return type ;}
	public Map<SpellType, Integer> getPreRequisites() {return preRequisites ;}
	public int getCooldown() {return cooldownCounter.getDuration() ;}
	public List<Buff> getBuffs() {return buffs ;}
//	public List<Buff> getNerfs() {return nerfs ;}
	public double[] getAtkMod() {return AtkMod ;}
	public double[] getDefMod() {return DefMod ;}
	public double[] getDexMod() {return DexMod ;}
	public double[] getAgiMod() {return AgiMod ;}
	public double[] getAtkCritMod() {return AtkCritMod ;}
	public double[] getDefCritMod() {return DefCritMod ;}
	public double[] getStunMod() {return StunMod ;}
	public double[] getBlockMod() {return BlockMod ;}
	public double[] getBloodMod() {return BloodMod ;}
	public double[] getPoisonMod() {return PoisonMod ;}
	public double[] getSilenceMod() {return SilenceMod ;}
	public Elements getElem() {return elem ;}
	public TimeCounter getCooldownCounter() {return cooldownCounter ;}
	public TimeCounter getDurationCounter() {return effectCounter ;}
	public String[] getInfo() {return info ;}
	public TimeCounter getEffectCounter() { return effectCounter ;}

	public boolean isReady() {return cooldownCounter.finished() ;}
	public boolean isActive() {return isActive ;}
	public void activate() {isActive = true ;}
	public void deactivate() {isActive = false ;}
	//public void incCooldownCounter() {cooldownCounter = (cooldownCounter + 1) % cooldownDuration ;}
	//public void incDurationCounter() {effectCounter = (effectCounter + 1) % effectDuration ;}

	public void incLevel(int increment)
	{
		if (level + increment <= maxLevel)
		{
			level += increment ;
		}
	}

	public boolean hasPreRequisitesMet()
	{
		boolean preRequisitesMet = true ;
		
		/*for (Map.Entry<Spell, Integer> entry : preRequisites.entrySet())
		{
			if (entry.getKey().getLevel() < entry.getValue())
			{
				preRequisitesMet = false ;
			}
		}*/
		
		return preRequisitesMet ;
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
				double increment = battleAttribute.getBaseValue() * 10*percIncrease.get(att) + valueIncrease.get(att) ;
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
	
	public void applyBuffsAndNerfs(String action, LiveBeing receiver)
	{
		int ActionMult = 1 ;
//		double[][] Buff = new double[14][5] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
//		double[] OriginalValue = new double[14] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
		
		PersonalAttributes PA = receiver.getPA() ;
//		BattleAttributes BA = receiver.getBA() ;
		
//		OriginalValue = new double[] {PA.getLife().getMaxValue(), PA.getMp().getMaxValue(), BA.getPhyAtk().getBaseValue(), BA.getMagAtk().getBaseValue(),
//				BA.getPhyDef().getBaseValue(), BA.getMagDef().getBaseValue(), BA.getDex().getBaseValue(), BA.getAgi().getBaseValue(),
//				BA.getCrit()[0],
//				BA.getStun().getBasicAtkChance(),
//				BA.getBlock().getBasicAtkChance(),
//				BA.getBlood().getBasicAtkChance(), BA.getBlood().getBasicDefChance(), BA.getBlood().getBasicAtk(), BA.getBlood().getBasicDef(),
//				BA.getPoison().getBasicAtkChance(), BA.getPoison().getBasicDefChance(),
//				BA.getPoison().getBasicAtk(), BA.getPoison().getBasicDef(),
//				BA.getSilence().getBasicAtkChance()} ;
		if (action.equals("deactivate"))
		{
			ActionMult = -1 ;
		}
		
		for (Buff buff : buffs)
		{
			int level = 1 ;
			double increment = PA.getLife().getMaxValue() * buff.getPercentIncrease().get(Attributes.life)
					+ buff.getValueIncrease().get(Attributes.life) ;
			PA.getLife().incCurrentValue((int) increment * level * ActionMult);
		}
//		
//		if (!spellIsActive)
//		{
//		}
	}
	
	@Override
	public String toString()
	{
		return "Spell [name=" + name + ", level=" + level + ", maxLevel=" + maxLevel + ", mpCost=" + mpCost + ", type="
				+ type + ", preRequisites=" + preRequisites + ", buffs=" + buffs + ", AtkMod=" + Arrays.toString(AtkMod) + ", DefMod="
				+ Arrays.toString(DefMod) + ", DexMod=" + Arrays.toString(DexMod) + ", AgiMod="
				+ Arrays.toString(AgiMod) + ", AtkCritMod=" + Arrays.toString(AtkCritMod) + ", DefCritMod="
				+ Arrays.toString(DefCritMod) + ", StunMod=" + Arrays.toString(StunMod) + ", BlockMod="
				+ Arrays.toString(BlockMod) + ", BloodMod=" + Arrays.toString(BloodMod) + ", PoisonMod="
				+ Arrays.toString(PoisonMod) + ", SilenceMod=" + Arrays.toString(SilenceMod) + ", isActive=" + isActive
				+ ", cooldownCounter=" + cooldownCounter + ", effectCounter=" + effectCounter + ", elem=" + elem
				+ ", info=" + Arrays.toString(info) + "]";
	}

}
