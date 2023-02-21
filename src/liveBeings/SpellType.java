package liveBeings ;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import components.SpellTypes;
import main.Game;
import utilities.UtilG;

public class SpellType 
{	
	private String name ;
	private int maxLevel ;
	private int mpCost ;
	private SpellTypes type ;
	private Map<SpellType, Integer> preRequisites ;
	private int cooldown ;
	//private int cooldownDuration ;
	private int effectDuration ;
	private List<Buff> buffs;
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
	
	public static final Image cooldownImage = UtilG.loadImage(Game.ImagesPath + "Cooldown.png") ;	
	//public static Image ElementalCicle = UtilG.loadImage(Game.ImagesPath + "ElementalCicle.png") ;
	
	public SpellType(String Name, int MaxLevel, int MpCost, SpellTypes type, Map<SpellType, Integer> preRequisites, int Cooldown, int Duration, List<Buff> buffs,
			double[] AtkMod, double[] DefMod, double[] DexMod, double[] AgiMod, double[] AtkCritMod, double[] DefCritMod, double[] StunMod, double[] BlockMod, double[] BloodMod,
			double[] PoisonMod, double[] SilenceMod, String Elem, String[] Info)
	{
		this.name = Name ;
		this.maxLevel = MaxLevel ;
		this.mpCost = MpCost ;
		this.type = type ;
		this.preRequisites = preRequisites ;
		this.cooldown = Cooldown ;
		//this.cooldownDuration = Duration ;
		this.effectDuration = Duration ;
		this.buffs = buffs ;
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
	//public int getCooldownDuration() {return cooldownDuration ;}
	public int getEffectDuration() {return effectDuration ;}
	public List<Buff> getBuffs() {return buffs ;}
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
	public String toString()
	{
		return "SpellType [name=" + name + ", maxLevel=" + maxLevel + ", mpCost=" + mpCost + ", type=" + type
				+ ", preRequisites=" + preRequisites + ", cooldown=" + cooldown + ", effectDuration=" + effectDuration
				+ ", buffs=" + buffs + ", AtkMod=" + Arrays.toString(AtkMod) + ", DefMod=" + Arrays.toString(DefMod)
				+ ", DexMod=" + Arrays.toString(DexMod) + ", AgiMod=" + Arrays.toString(AgiMod) + ", AtkCritMod="
				+ Arrays.toString(AtkCritMod) + ", DefCritMod=" + Arrays.toString(DefCritMod) + ", StunMod="
				+ Arrays.toString(StunMod) + ", BlockMod=" + Arrays.toString(BlockMod) + ", BloodMod="
				+ Arrays.toString(BloodMod) + ", PoisonMod=" + Arrays.toString(PoisonMod) + ", SilenceMod="
				+ Arrays.toString(SilenceMod) + ", elem=" + elem + ", info=" + Arrays.toString(info) + "]";
	}

}