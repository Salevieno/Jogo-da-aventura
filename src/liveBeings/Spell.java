package liveBeings ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.SpellTypes;
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
	private double[][] Buffs ;
	private double[][] Nerfs ;
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
	private String elem ;

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
		this.Buffs = spellType.getBuffs() ;
		this.Nerfs = spellType.getNerfs() ;
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
		buffs = new ArrayList<>();
		buffs.add(new Buff(new HashMap<>(), new HashMap<>(), new HashMap<>()));
	}
	
	public String getName() {return name ;}
	public int getLevel() {return level ;}
	public int getMaxLevel() {return maxLevel ;}
	public int getMpCost() {return mpCost ;}
	public SpellTypes getType() {return type ;}
	public Map<SpellType, Integer> getPreRequisites() {return preRequisites ;}
	public int getCooldown() {return cooldownCounter.getDuration() ;}
	public List<Buff> getBuffs() {return buffs ;}
	//public double[][] getBuffs() {return Buffs ;}
	public double[][] getNerfs() {return Nerfs ;}
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
	public String getElem() {return elem ;}
	public TimeCounter getCooldownCounter() {return cooldownCounter ;}
	public TimeCounter getDurationCounter() {return effectCounter ;}
	public String[] getInfo() {return info ;}

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

	@Override
	public String toString()
	{
		return "Spell [name=" + name + ", level=" + level + ", maxLevel=" + maxLevel + ", mpCost=" + mpCost + ", type="
				+ type + ", preRequisites=" + preRequisites + ", buffs=" + buffs + ", Buffs=" + Arrays.toString(Buffs)
				+ ", Nerfs=" + Arrays.toString(Nerfs) + ", AtkMod=" + Arrays.toString(AtkMod) + ", DefMod="
				+ Arrays.toString(DefMod) + ", DexMod=" + Arrays.toString(DexMod) + ", AgiMod="
				+ Arrays.toString(AgiMod) + ", AtkCritMod=" + Arrays.toString(AtkCritMod) + ", DefCritMod="
				+ Arrays.toString(DefCritMod) + ", StunMod=" + Arrays.toString(StunMod) + ", BlockMod="
				+ Arrays.toString(BlockMod) + ", BloodMod=" + Arrays.toString(BloodMod) + ", PoisonMod="
				+ Arrays.toString(PoisonMod) + ", SilenceMod=" + Arrays.toString(SilenceMod) + ", isActive=" + isActive
				+ ", cooldownCounter=" + cooldownCounter + ", effectCounter=" + effectCounter + ", elem=" + elem
				+ ", info=" + Arrays.toString(info) + "]";
	}

}
