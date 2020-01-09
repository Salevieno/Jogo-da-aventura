package GameComponents;

public class Player 
{
	private String Name;
	private String Language;
	private String Sex;
	private int Job;
	private int ProJob;
	private int Continent;
	private int Map;
	private int[] Coords;
	private int[] Skill;
	private int[] Quest;
	private int[] Bag;
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
	private float[] Collect = new float[3];		// [Herb, wood, metal]
	private int Level = 0;
	private int Gold = 0;
	private int Step = 0;
	private float Craft = 0;
	private float Exp = 0;
	private float Hunger = 0;
	private boolean Ride = false;
	private boolean[] Status; 					// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	
	public Player(String Language, String Name, String Sex, int Job, int ProJob, int Continent, int Map, int[] Coords, int[] Skill, int[] Quest, int[] Bag, int SkillPoints, float Life, float Mp, float LifeMax, float MpMax, float Range, float[] PhyAtk, float[] MagAtk, float[] PhyDef, float[] MagDef, float[] Dex, float[] Agi, float[] Crit, String[] Elem, float[] Collect, int Level, int Gold, int Step, float Craft, float Exp, float Hunger, boolean Ride, boolean[] Status)
	{
		this.Language = Language;
		this.Name = Name;
		this.Sex = Sex;
		this.Job = Job;
		this.ProJob = ProJob;
		this.Continent = Continent;
		this.Map = Map;
		this.Coords = Coords;
		this.Skill = Skill;
		this.Quest = Quest;
		this.Bag = Bag;
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
		this.Collect = Collect;
		this.Level = Level;
		this.Gold = Gold;
		this.Step = Step;
		this.Craft = Craft;
		this.Exp = Exp;
		this.Hunger = Hunger;
		this.Ride = Ride;
		this.Status = Status;
	}

	public String getLanguage() {return Language;}
	public String getName() {return Name;}
	public String getSex() {return Sex;}
	public int getJob() {return Job;}
	public int getProJob() {return ProJob;}
	public int getContinent() {return Continent;}
	public int getMap() {return Map;}
	public int[] getCoords() {return Coords;}
	public int[] getSkill() {return Skill;}
	public int[] getQuest() {return Quest;}
	public int[] getBag() {return Bag;}
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
	public float[] getCollect() {return Collect;}
	public int getLevel() {return Level;}
	public int getGold() {return Gold;}
	public int getStep() {return Step;}
	public float getCraft() {return Craft;}
	public float getExp() {return Exp;}
	public float getHunger() {return Hunger;}
	public boolean getRide() {return Ride;}
	public boolean[] getStatus() {return Status;}
	public void setLanguage(String L) {Language = L;}
	public void setName(String N) {Name = N;}
	public void setSex(String S) {Sex = S;}
	public void setJob(int J) {Job = J;}
	public void setProJob(int PJ) {ProJob = PJ;}
	public void setContinent(int C) {Continent = C;}
	public void setMap(int M) {Map = M;}
	public void setCoords(int[] C) {Coords = C;}
	public void setSkill(int[] S) {Skill = S;}
	public void setQuest(int[] Q) {Quest = Q;}
	public void setBag(int[] B) {Bag = B;}
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
	public void setCollect(float[] C) {Collect = C;}
	public void setLevel(int L) {Level = L;}
	public void setGold(int G) {Gold = G;}
	public void setStep(int S) {Step = S;}
	public void setCraft(float C) {Craft = C;}
	public void setExp(float E) {Exp = E;}
	public void setHunger(float H) {Hunger = H;}
	public void setRide(boolean R) {Ride = R;}
	public void setStatus(boolean[] S) {Status = S;}
}