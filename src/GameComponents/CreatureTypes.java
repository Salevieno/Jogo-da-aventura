package GameComponents ;

import java.awt.Color ;
import java.awt.Image ;
import java.io.IOException;
import java.util.Arrays;

import Main.Utg;

public class CreatureTypes 
{
	private int Type ;
	private Image image ;
	private Image[] animations ;
	private String Name ;
	private int Level ;
	private int Map ;
	private int[] Size ;
	private int[] Skill ;
	private float[] Life ;		// [Current life, max life]
	private float[] Mp ;			// [Current mp, max mp]
	private float Range ;
	private float[] PhyAtk ;		// [Basic atk, bonus]
	private float[] MagAtk ;		// [Basic atk, bonus]
	private float[] PhyDef ;		// [Basic def, bonus]
	private float[] MagDef ;		// [Basic def, bonus]
	private float[] Dex ;		// [Basic dex, bonus]
	private float[] Agi ;		// [Basic agi, bonus]
	private float[] Crit ;		// [Basic crit atk, bonus, basic crit def, bonus]
	private float[] Stun ;		// [Basic stun atk, bonus, basic stun def, bonus]
	private float[] Block ;		// [Basic block atk, bonus, basic block def, bonus]
	private float[] Blood ;		// [Basic blood atk, bonus, basic blood def, bonus]
	private float[] Poison ;		// [Basic poison atk, bonus, basic poison def, bonus]
	private float[] Silence ;	// [Basic silence atk, bonus, basic silence def, bonus]
	private String[] Elem ;		// [Atk, Weapon, Armor, Shield, SuperElem]
	private int Exp ;
	private int[] Bag ;
	private int Gold ;
	private int Step ;	
	private int[] Status ;		// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi]
	private int[] SpecialStatus ;// [Stun, Block, Blood, Poison, Silence]
	private Color color ;
	private int[][] Actions ;	// [Move, Mp][Counter, delay, permission]
	private int[][] BattleActions ;	// [Atk][Counter, delay, permission]
	private int[] StatusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]	
	
	private static int NumberOfCreatureTypes ;
	
	public CreatureTypes(int Type, Image image, Image[] animations, String Name, int Level, int Map, int[] Size, int[] Skill, float[] Life, float[] Mp, float Range, float[] PhyAtk, float[] MagAtk, float[] PhyDef, float[] MagDef, float[] Dex, float[] Agi, float[] Crit, float[] Stun, float[] Block, float[] Blood, float[] Poison, float[] Silence, String[] Elem, int Exp, int[] Bag, int Gold, int Step, int[] Status, int[] SpecialStatus, Color color, int[][] Actions, int[][] BattleActions, int[] StatusCounter)
	{
		this.Type = Type ;
		this.image = image ;
		this.animations = animations ;
		this.Name = Name ;
		this.Level = Level ;
		this.Map = Map ;
		this.Size = Size ;
		this.Skill = Skill ;
		this.Life = Life ;
		this.Mp = Mp ;
		this.Range = Range ;
		this.PhyAtk = PhyAtk ;
		this.MagAtk = MagAtk ;
		this.PhyDef = PhyDef ;
		this.MagDef = MagDef ;
		this.Dex = Dex ;
		this.Agi = Agi ;
		this.Crit = Crit ;
		this.Stun = Stun ;
		this.Block = Block ;
		this.Blood = Blood ;
		this.Poison = Poison ;
		this.Silence = Silence ;
		this.Elem = Elem ;
		this.Exp = Exp ;
		this.Bag = Bag ;
		this.Gold = Gold ;
		this.Step = Step ;
		this.Status = Status ;
		this.SpecialStatus = SpecialStatus ;
		this.color = color ;
		this.Actions = Actions ;
		this.BattleActions = BattleActions ;
		this.StatusCounter = StatusCounter ;
	}

	public int getID() {return Type ;}
	public Image getimage() {return image ;}
	public Image[] getanimations() {return animations ;}
	public String getName() {return Name ;}
	public int getLevel() {return Level ;}
	public int getMap() {return Map ;}
	public int[] getSize() {return Size ;}
	public int[] getSkill() {return Skill ;}
	public float[] getLife() {return Life ;}
	public float[] getMp() {return Mp ;}
	public float getRange() {return Range ;}
	public float[] getPhyAtk() {return PhyAtk ;}
	public float[] getMagAtk() {return MagAtk ;}
	public float[] getPhyDef() {return PhyDef ;}
	public float[] getMagDef() {return MagDef ;}
	public float[] getDex() {return Dex ;}
	public float[] getAgi() {return Agi ;}
	public float[] getCrit() {return Crit ;}
	public float[] getStun() {return Stun ;}
	public float[] getBlock() {return Block ;}
	public float[] getBlood() {return Blood ;}
	public float[] getPoison() {return Poison ;}
	public float[] getSilence() {return Silence ;}
	public String[] getElem() {return Elem ;}
	public int getExp() {return Exp ;}
	public int[] getBag() {return Bag ;}
	public int getGold() {return Gold ;}
	public int getStep() {return Step ;}
	public int[] getStatus() {return Status ;}
	public int[] getSpecialStatus() {return SpecialStatus ;}
	public Color getColor() {return color ;}
	public int[][] getActions() {return Actions ;}
	public int[][] getBattleActions() {return BattleActions ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public void setID(int I) {Type = I ;}
	public void setimage(Image I) {image = I ;}
	public void setName(String N) {Name = N ;}
	public void setLevel(int L) {Level = L ;}
	public void setMap(int M) {Map = M ;}
	public void setSize(int[] S) {Size = S ;}
	public void setSkill(int[] S) {Skill = S ;}
	public void setLife(float[] L) {Life = L ;}
	public void setMp(float[] M) {Mp = M ;}
	public void setRange(float R) {Range = R ;}
	public void setPhyAtk(float[] PA) {PhyAtk = PA ;}
	public void setMagAtk(float[] MA) {MagAtk = MA ;}
	public void setPhyDef(float[] PD) {PhyDef = PD ;}
	public void setMagDef(float[] MD) {MagDef = MD ;}
	public void setDex(float[] D) {Dex = D ;}
	public void setAgi(float[] A) {Agi = A ;}
	public void setCrit(float[] C) {Crit = C ;}
	public void setStun(float[] S) {Stun = S ;}
	public void setBlock(float[] B) {Block = B ;}
	public void setBlood(float[] B) {Blood = B ;}
	public void setPoison(float[] P) {Poison = P ;}
	public void setSilence(float[] S) {Silence = S ;}
	public void setElem(String[] E) {Elem = E ;}
	public void setExp(int E) {Exp = E ;}
	public void setBag(int[] B) {Bag = B ;}
	public void setGold(int G) {Gold = G ;}
	public void setStep(int S) {Step = S ;}
	public void setStatus(int[] S) {Status = S ;}
	public void setSpecialStatus(int[] S) {SpecialStatus = S ;}
	public void setColor(Color C) {color = C ;}
	public void setActions(int[][] A) {Actions = A ;}
	public void setBattleActions(int[][] A) {BattleActions = A ;}
	public void setStatusCounter(int[] S) {StatusCounter = S ;}
	
	public static int getNumberOfCreatureTypes()
	{
		return NumberOfCreatureTypes ;
	}
	public static void setNumberOfCreatureTypes(String CSVPath)
	{
		try
		{
			NumberOfCreatureTypes = Utg.count(CSVPath + "CreatureTypes.csv") ;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
	}
	
	/* Print methods */
	public void printAtt()
	{
		System.out.println();
		System.out.println("** Creature attributes **");
		System.out.println("Name: " + Name);
		System.out.println("Level: " + Level);
		System.out.println("Life max: " + Life[1]);
		System.out.println("Mp max: " + Mp[1]);
		System.out.println("Phy atk: " + Arrays.toString(PhyAtk));
		System.out.println("Mag atk: " + Arrays.toString(MagAtk));
		System.out.println("Phy def: " + Arrays.toString(PhyDef));
		System.out.println("Mag def: " + Arrays.toString(MagDef));
		System.out.println("Dex: " + Arrays.toString(Dex));
		System.out.println("Agi: " + Arrays.toString(Agi));
		System.out.println("Crit: " + Arrays.toString(Crit));
		System.out.println("Elem: " + Arrays.toString(Elem));
	}
}
