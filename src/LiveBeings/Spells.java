package LiveBeings ;

import java.awt.Image;
import java.util.Arrays;

import javax.swing.ImageIcon;

import Main.Game;

public class Spells 
{	
	private String Name ;
	private int level ;
	private int MaxLevel ;
	private float MpCost ;
	private String Type ;
	private int[][] PreRequisites ;
	private int Cooldown ;
	private int Duration ;
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
	private String Elem ;
	private String[] Info ;	// Effect and description
	
	public static Image cooldownImage ;	
	public static Image slotImage ;	
	public static Image ElementalCicle ;
	
	public Spells(String Name, int MaxLevel, float MpCost, String Type, int[][] PreRequisites, int Cooldown, int Duration, float[][] Buffs, float[][] Nerfs, float[] AtkMod, float[] DefMod, float[] DexMod, float[] AgiMod, float[] AtkCritMod, float[] DefCritMod, float[] StunMod, float[] BlockMod, float[] BloodMod, float[] PoisonMod, float[] SilenceMod, String Elem, String[] Info)
	{
		this.Name = Name ;
		level = 0 ;
		this.MaxLevel = MaxLevel ;
		this.MpCost = MpCost ;
		this.Type = Type ;
		this.PreRequisites = PreRequisites ;
		this.Cooldown = Cooldown ;
		this.Duration = Duration ;
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
		this.Elem = Elem ;
		this.Info = Info ;
		
		cooldownImage = new ImageIcon(Game.ImagesPath + "Cooldown.png").getImage() ;
		slotImage = new ImageIcon(Game.ImagesPath + "SkillSlot.png").getImage() ;
		ElementalCicle = new ImageIcon(Game.ImagesPath + "ElementalCicle.png").getImage() ;
	}
	
	public String getName() {return Name ;}
	public int getLevel() {return level ;}
	public int getMaxLevel() {return MaxLevel ;}
	public float getMpCost() {return MpCost ;}
	public String getType() {return Type ;}
	public int[][] getPreRequisites() {return PreRequisites ;}
	public int getCooldown() {return Cooldown ;}
	public int getDuration() {return Duration ;}
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
	public String getElem() {return Elem ;}
	public String[] getInfo() {return Info ;}
	public void setName(String N) {Name = N ;}
	public void setMaxLevel(int M) {MaxLevel = M ;}
	public void setMpCost(float M) {MpCost = M ;}
	public void setType(String T) {Type = T ;}
	public void setPreRequisites(int[][] P) {PreRequisites = P ;}
	public void setCooldown(int C) {Cooldown = C ;}
	public void setDuration(int D) {Duration = D ;}
	public void setBuffs(float[][] B) {Buffs = B ;}
	public void setNerfs(float[][] N) {Nerfs = N ;}
	public void setAtkMod(float[] A) {AtkMod = A ;}
	public void setDefMod(float[] D) {DefMod = D ;}
	public void setDexMod(float[] D) {DexMod = D ;}
	public void setAgiMod(float[] A) {AgiMod = A ;}
	public void setAtkCritMod(float[] C) {AtkCritMod = C ;}
	public void setDefCritMod(float[] C) {DefCritMod = C ;}
	public void setStunMod(float[] S) {StunMod = S ;}
	public void setBlockMod(float[] B) {BlockMod = B ;}
	public void setBloodMod(float[] B) {BloodMod = B ;}
	public void setPoisonMod(float[] P) {PoisonMod = P ;}
	public void setSilenceMod(float[] S) {SilenceMod = S ;}
	public void setElem(String E) {Elem = E ;}
	public void setInfo(String[] I) {Info = I ;}
	
	public void incLevel(int increment)
	{
		if (level + increment <= MaxLevel)
		{
			level += increment ;
		}
	}

	@Override
	public String toString() {
		return "Spells [Name=" + Name + ", level=" + level + ", MaxLevel=" + MaxLevel + ", MpCost=" + MpCost + ", Type="
				+ Type + ", PreRequisites=" + Arrays.toString(PreRequisites) + ", Cooldown=" + Cooldown + ", Duration="
				+ Duration + ", Buffs=" + Arrays.toString(Buffs) + ", Nerfs=" + Arrays.toString(Nerfs) + ", AtkMod="
				+ Arrays.toString(AtkMod) + ", DefMod=" + Arrays.toString(DefMod) + ", DexMod="
				+ Arrays.toString(DexMod) + ", AgiMod=" + Arrays.toString(AgiMod) + ", AtkCritMod="
				+ Arrays.toString(AtkCritMod) + ", DefCritMod=" + Arrays.toString(DefCritMod) + ", StunMod="
				+ Arrays.toString(StunMod) + ", BlockMod=" + Arrays.toString(BlockMod) + ", BloodMod="
				+ Arrays.toString(BloodMod) + ", PoisonMod=" + Arrays.toString(PoisonMod) + ", SilenceMod="
				+ Arrays.toString(SilenceMod) + ", Elem=" + Elem + ", Info=" + Arrays.toString(Info) + "]";
	}
}
