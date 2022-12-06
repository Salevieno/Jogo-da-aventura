package LiveBeings ;

import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

import GameComponents.SpellTypes;
import Main.Game;

public class SpellType 
{	
	private String name ;
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
	private String elem ;
	private String[] info ;	// Effect and description
	
	public static Image cooldownImage = new ImageIcon(Game.ImagesPath + "Cooldown.png").getImage() ;	
	public static Image slotImage = new ImageIcon(Game.ImagesPath + "SkillSlot.png").getImage() ;
	public static Image ElementalCicle = new ImageIcon(Game.ImagesPath + "ElementalCicle.png").getImage() ;
	
	public SpellType(String Name, int MaxLevel, float MpCost, SpellTypes type, Map<SpellType, Integer> preRequisites, int Cooldown, int Duration, float[][] Buffs, float[][] Nerfs,
			float[] AtkMod, float[] DefMod, float[] DexMod, float[] AgiMod, float[] AtkCritMod, float[] DefCritMod, float[] StunMod, float[] BlockMod, float[] BloodMod,
			float[] PoisonMod, float[] SilenceMod, String Elem, String[] Info)
	{
		this.name = Name ;
		this.maxLevel = MaxLevel ;
		this.mpCost = MpCost ;
		this.type = type ;
		this.preRequisites = preRequisites ;
		this.cooldown = Cooldown ;
		this.duration = Duration ;
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
	public String[] getInfo() {return info ;}
	
}