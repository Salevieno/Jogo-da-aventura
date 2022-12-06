package LiveBeings ;

import java.awt.Image;
import java.util.Arrays;
import java.util.Map;

import javax.swing.ImageIcon;

import GameComponents.SpellTypes;
import Main.Game;

public class Spell 
{	
	private String name ;
	private int level ;
	private int maxLevel ;
	private float mpCost ;
	private SpellTypes type ;
	private Map<SpellType, Integer> preRequisites ;
	private int cooldown ;
	private int duration ;
	private float[][] Buffs ;
	private float[][] Nerfs ;
	private float[] AtkMod ;
	private float[] DefMod ;
	private float[] DexMod ;
	private float[] AgiMod ;
	private float[] AtkCritMod ;
	private float[] DefCritMod ;
	private float[] StunMod ;
	private float[] BlockMod ;
	private float[] BloodMod ;
	private float[] PoisonMod ;
	private float[] SilenceMod ;
	private boolean isActive ;
	private int cooldownCounter ;
	private int durationCounter ;
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
		this.cooldown = spellType.getCooldown() ;
		this.duration = spellType.getDuration() ;
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
		cooldownCounter = 0 ;
		durationCounter = 0 ;
		this.elem = spellType.getElem() ;
		this.info = spellType.getInfo() ;
	}
	
	public String getName() {return name ;}
	public int getLevel() {return level ;}
	public int getMaxLevel() {return maxLevel ;}
	public float getMpCost() {return mpCost ;}
	public SpellTypes getType() {return type ;}
	public Map<SpellType, Integer> getPreRequisites() {return preRequisites ;}
	public int getCooldown() {return cooldown ;}
	public int getDuration() {return duration ;}
	public float[][] getBuffs() {return Buffs ;}
	public float[][] getNerfs() {return Nerfs ;}
	public float[] getAtkMod() {return AtkMod ;}
	public float[] getDefMod() {return DefMod ;}
	public float[] getDexMod() {return DexMod ;}
	public float[] getAgiMod() {return AgiMod ;}
	public float[] getAtkCritMod() {return AtkCritMod ;}
	public float[] getDefCritMod() {return DefCritMod ;}
	public float[] getStunMod() {return StunMod ;}
	public float[] getBlockMod() {return BlockMod ;}
	public float[] getBloodMod() {return BloodMod ;}
	public float[] getPoisonMod() {return PoisonMod ;}
	public float[] getSilenceMod() {return SilenceMod ;}
	public String getElem() {return elem ;}
	public int getCooldownCounter() {return cooldownCounter ;}
	public int getDurationCounter() {return durationCounter ;}
	public String[] getInfo() {return info ;}

	public boolean isReady() {return cooldownCounter == 0 ;}
	public boolean isActive() {return isActive ;}
	public void activate() {isActive = true ;}
	public void deactivate() {isActive = false ;}
	public void incCooldownCounter() {cooldownCounter = (cooldownCounter + 1) % duration ;}	// TODO mesmo tempo para duration e cooldown
	public void incDurationCounter() {durationCounter = (durationCounter + 1) % duration ;}

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
	
}
