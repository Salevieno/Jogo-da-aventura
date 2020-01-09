package GameComponents;

public class Creatures 
{
	private int Continent;
	private int Map;
	private int[] Coords;
	private int[] Skill;
	private int[] Bag;
	private float Life = 0;
	private float Mp = 0;
	private float LifeMax = 0;
	private float MpMax = 0;
	private float Range = 0;
	private float[] PhyAtk = new float[2];		// [Basic atk, bonus]
	private float[] MagAtk = new float[2];		// [Basic atk, bonus]
	private float[] PhyDef = new float[2];		// [Basic def, bonus]
	private float[] MagDef = new float[2];		// [Basic def, bonus]
	private float[] Dex = new float[2];			// [Basic dex, bonus]
	private float[] Agi = new float[2];			// [Basic agi, bonus]
	private float[] Crit = new float[2];		// [Basic crit atk, bonus]
	private String[] Elem = new String[5];		// [Atk, Weapon, Armor, Shield, SuperElem]
	private int Gold = 0;
	private int Step = 0;
	private float Exp = 0;
	private boolean[] Status;
	
	public Creatures(int Continent, int Map, int[] Coords, int[] Skill, int[] Bag, float Life, float Mp, float LifeMax, float MpMax, float Range, float[] PhyAtk, float[] MagAtk, float[] PhyDef, float[] MagDef, float[] Dex, float[] Agi, float[] Crit, String[] Elem, int Gold, int Step, float Exp, boolean[] Status)
	{
		this.Continent = Continent;
		this.Map = Map;
		this.Coords = Coords;
		this.Skill = Skill;
		this.Bag = Bag;
		this.Life = Life;
		this.Mp = Mp;
		this.LifeMax = LifeMax;
		this.MpMax = MpMax;
		this.Range = Range;
		this.PhyAtk = PhyAtk;
		this.MagAtk = MagAtk;
		this.PhyDef = PhyDef;
		this.MagDef = MagDef;
		this.Dex = Dex;
		this.Agi = Agi;
		this.Crit = Crit;
		this.Elem = Elem;
		this.Gold = Gold;
		this.Step = Step;
		this.Exp = Exp;
		this.Status = Status;
	}
	
	public int getContinent() {return Continent;}
	public int getMap() {return Map;}
	public int[] getCoords() {return Coords;}
	public int[] getSkill() {return Skill;}
	public int[] getBag() {return Bag;}
	public float getLife() {return Life;}
	public float getMp() {return Mp;}
	public float getLifeMax() {return LifeMax;}
	public float getMpMax() {return MpMax;}
	public float getRange() {return Range;}
	public float[] getPhyAtk() {return PhyAtk;}
	public float[] getMagAtk() {return MagAtk;}
	public float[] getPhyDef() {return PhyDef;}
	public float[] getMagDef() {return MagDef;}
	public float[] getDex() {return Dex;}
	public float[] getAgi() {return Agi;}
	public float[] getCrit() {return Crit;}
	public String[] getElem() {return Elem;}
	public int getGold() {return Gold;}
	public int getStep() {return Step;}
	public float getExp() {return Exp;}
	public boolean[] getStatus() {return Status;}
	public void setContinent(int C) {Continent = C;}
	public void setMap(int M) {Map = M;}
	public void setCoords(int[] C) {Coords = C;}
	public void setSkill(int[] S) {Skill = S;}
	public void setBag(int[] B) {Bag = B;}
	public void setLife(float L) {Life = L;}
	public void setMp(float M) {Mp = M;}
	public void setLifeMax(float LM) {LifeMax = LM;}
	public void setMpMax(float MM) {MpMax = MM;}
	public void setRange(float R) {Range = R;}
	public void setPhyAtk(float[] PA) {PhyAtk = PA;}
	public void setMagAtk(float[] MA) {MagAtk = MA;}
	public void setPhyDef(float[] PD) {PhyDef = PD;}
	public void setMagDef(float[] MD) {MagDef = MD;}
	public void setDex(float[] D) {Dex = D;}
	public void setAgi(float[] A) {Agi = A;}
	public void setCrit(float[] C) {Crit = C;}
	public void setElem(String[] E) {Elem = E;}
	public void setGold(int G) {Gold = G;}
	public void setStep(int S) {Step = S;}
	public void setExp(float E) {Exp = E;}
	public void setStatus(boolean[] S) {Status = S;}
}
