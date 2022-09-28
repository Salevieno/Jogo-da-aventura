package GameComponents ;

public class PetSkills 
{
	private String Name ;
	private int MaxLevel ;
	private float MpCost ;
	private String Type ;
	private int[][] PreRequisites ;
	private int Cooldown ;
	private float[][] Buffs ;
	private float[][] Nerfs ;
	private String Elem ;
	private String[] Info ;	// Effect and description
	
	public PetSkills(String Name, int MaxLevel, float MpCost, String Type, int[][] PreRequisites, int Cooldown, float[][] Buffs, float[][] Nerfs, String Elem, String[] Info)
	{
		this.Name = Name ;
		this.MaxLevel = MaxLevel ;
		this.MpCost = MpCost ;
		this.Type = Type ;
		this.PreRequisites = PreRequisites ;
		this.Cooldown = Cooldown ;
		this.Buffs = Buffs ;
		this.Nerfs = Nerfs ;
		this.Elem = Elem ;
		this.Info = Info ;
	}
	
	public String getName() {return Name ;}
	public int getMaxLevel() {return MaxLevel ;}
	public float getMpCost() {return MpCost ;}
	public String getType() {return Type ;}
	public int[][] getPreRequisites() {return PreRequisites ;}
	public int getCooldown() {return Cooldown ;}
	public float[][] getBuffs() {return Buffs ;}
	public float[][] getNerfs() {return Nerfs ;}
	public String getElem() {return Elem ;}
	public String[] getInfo() {return Info ;}
	public void setName(String N) {Name = N ;}
	public void setMaxLevel(int M) {MaxLevel = M ;}
	public void setMpCost(float M) {MpCost = M ;}
	public void setType(String T) {Type = T ;}
	public void setPreRequisites(int[][] P) {PreRequisites = P ;}
	public void setCooldown(int C) {Cooldown = C ;}
	public void setBuffs(float[][] B) {Buffs = B ;}
	public void setNerfs(float[][] N) {Nerfs = N ;}
	public void setElem(String E) {Elem = E ;}
	public void setInfo(String[] I) {Info = I ;}
}
