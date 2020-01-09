package GameComponents;

public class Pet
{
	private String Name;
	private int Job;
	private int[] Coords;
	private int[] Skill;
	private int SkillPoints = 0;
	private float Life = 0;
	private float Mp = 0;
	private float LifeMax = 0;
	private float MpMax = 0;
	private float Range = 0;
	private float[] PhyAtk = new float[3];		// [Basic atk, bonus, train]
	private float[] MagAtk = new float[3];		// [Basic atk, bonus, train]
	private float[] PhyDef = new float[3];		// [Basic def, bonus, train]
	private float[] MagDef = new float[3];		// [Basic def, bonus, train]
	private float[] Dex = new float[3];			// [Basic dex, bonus, train]
	private float[] Agi = new float[3];			// [Basic agi, bonus, train]
	private float[] Crit = new float[2];		// [Basic crit atk, bonus]
	private String[] Elem = new String[5];		// [Atk, Weapon, Armor, Shield, SuperElem]
	private int Level = 0;
	private float Hunger = 0;
	private float Exp = 0;
	private boolean[] Status; 
	
	public Pet(String Name, int Job, int[] Coords, int[] Skill, int SkillPoints, float Life, float Mp, float LifeMax, float MpMax, float Range, float[] PhyAtk, float[] MagAtk, float[] PhyDef, float[] MagDef, float[] Dex, float[] Agi, float[] Crit, String[] Elem, int Level, float Exp, float Hunger, boolean[] Status)
	{
		this.Name = Name;
		this.Job = Job;
		this.Coords = Coords;
		this.Skill = Skill;
		this.SkillPoints = SkillPoints;
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
		this.Level = Level;
		this.Exp = Exp;
		this.Hunger = Hunger;
		this.Status = Status;
	}

	public String getName() {return Name;}
	public int getJob() {return Job;}
	public int[] getCoords() {return Coords;}
	public int[] getSkill() {return Skill;}
	public int getSkillPoints() {return SkillPoints;}
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
	public int getLevel() {return Level;}
	public float getExp() {return Exp;}
	public float getHunger() {return Hunger;}
	public boolean[] getStatus() {return Status;}
	public void setName(String N) {Name = N;}
	public void setJob(int J) {Job = J;}
	public void setCoords(int[] C) {Coords = C;}
	public void setSkill(int[] S) {Skill = S;}
	public void setSkillPoints(int SP) {SkillPoints = SP;}
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
	public void setLevel(int L) {Level = L;}
	public void setExp(float E) {Exp = E;}
	public void setHunger(float H) {Hunger = H;}
	public void setStatus(boolean[] S) {Status = S;}
}