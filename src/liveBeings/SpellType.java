package liveBeings ;

import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

import components.SpellTypes;
import main.Game;

public class SpellType 
{	
	private String name ;
	private int maxLevel ;
	private int mpCost ;
	private SpellTypes type ;
	private Map<SpellType, Integer> preRequisites ;
	private int cooldown ;
	private int cooldownDuration ;
	private int effectDuration ;
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
	private String elem ;
	private String[] info ;	// Effect and description
	
	public static Image cooldownImage = new ImageIcon(Game.ImagesPath + "Cooldown.png").getImage() ;	
	public static Image slotImage = new ImageIcon(Game.ImagesPath + "SkillSlot.png").getImage() ;
	public static Image ElementalCicle = new ImageIcon(Game.ImagesPath + "ElementalCicle.png").getImage() ;
	
	public SpellType(String Name, int MaxLevel, int MpCost, SpellTypes type, Map<SpellType, Integer> preRequisites, int Cooldown, int Duration, double[][] Buffs, double[][] Nerfs,
			double[] AtkMod, double[] DefMod, double[] DexMod, double[] AgiMod, double[] AtkCritMod, double[] DefCritMod, double[] StunMod, double[] BlockMod, double[] BloodMod,
			double[] PoisonMod, double[] SilenceMod, String Elem, String[] Info)
	{
		this.name = Name ;
		this.maxLevel = MaxLevel ;
		this.mpCost = MpCost ;
		this.type = type ;
		this.preRequisites = preRequisites ;
		this.cooldown = Cooldown ;
		this.cooldownDuration = Duration ;
		this.effectDuration = Duration ;
		this.Buffs = Buffs ;
		this.Nerfs = Nerfs ;
		this.AtkMod = AtkMod ;
		this.DefMod = DefMod ;
		this.DexMod = DexMod ;
		this.AgiMod = AgiMod ;
		this.AtkCritMod = AtkCritMod ;
		this.DefCritMod = DefCritMod ;
		this.StunMod = StunMod ;
		this.BlockMod = BlockMod ;
		this.BloodMod = BloodMod ;
		this.PoisonMod = PoisonMod ;
		this.SilenceMod = SilenceMod ;
		this.elem = Elem ;
		this.info = Info ;
	}
	
	public String getName() {return name ;}
	public int getMaxLevel() {return maxLevel ;}
	public int getMpCost() {return mpCost ;}
	public SpellTypes getType() {return type ;}
	public Map<SpellType, Integer> getPreRequisites() {return preRequisites ;}
	public int getCooldown() {return cooldown ;}
	public int getCooldownDuration() {return cooldownDuration ;}
	public int getEffectDuration() {return effectDuration ;}
	public double[][] getBuffs() {return Buffs ;}
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
	public String[] getInfo() {return info ;}

	@Override
	public String toString() {
		return "SpellType [name=" + name + "]";
	}
}