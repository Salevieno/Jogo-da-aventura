package GameComponents ;

import java.awt.Image ;
import java.util.Arrays;

public class PersonalAttributes
{
	private String Name ;
	private Image[] image ;
	private int Level ;
	private int continent ;
	private int map ;
	private int[] Pos ;
	private String dir ;			// direction of the movement
	private String Thought ;		// current thought
	private int[] Size ;
	private float[] Life ;		// 0: Current life, 1: max life]
	private float[] Mp ;			// 0: Current mp, 1: max mp]
	private float Range ;
	private int Step ;
	private float[] Exp ;		// 0: Current exp, 1: max exp, 2: multiplier
	private float[] Satiation ;	// 0: Current satiation, 1: max satiation, 2: multiplier
	private float[] Thirst ;	// 0: Current satiation, 1: max satiation, 2: multiplier
	
	public PersonalAttributes(String Name, Image[] image, int Level, int continent, int map, int[] Pos, String dir, String Thought, int[] Size, float[] Life, float[] Mp, float Range, int Step, float[] Exp, float[] Satiation, float[] Thirst)
	{
		this.Name = Name ;
		this.image = image ;
		this.Level = Level ;
		this.continent = continent ;
		this.map = map ;
		this.Pos = Pos ;
		this.dir = dir ;
		this.Thought = Thought ;
		this.Size = Size ;
		this.Life = Life ;
		this.Mp = Mp ;
		this.Range = Range ;
		this.Step = Step ;
		this.Exp = Exp ;
		this.Satiation = Satiation ;
		this.Thirst = Thirst ;
	}

	public String getName() {return Name ;}
	public Image[] getimage() {return image ;}
	public int getLevel() {return Level ;}
	public int getContinent() {return continent ;}
	public int getMap() {return map ;}
	public String getDir() {return dir ;}
	public String getThought() {return Thought ;}
	public int[] getPos() {return Pos ;}
	public int[] getSize() {return Size ;}
	public float[] getLife() {return Life ;}
	public float[] getMp() {return Mp ;}
	public float getRange() {return Range ;}
	public int getStep() {return Step ;}
	public float[] getExp() {return Exp ;}
	public float[] getSatiation() {return Satiation ;}
	public float[] getThirst() {return Thirst ;}
	public void setName(String N) {Name = N ;}
	public void setimage(Image[] I) {image = I ;}
	public void setLevel(int L) {Level = L ;}
	public void setContinent(int C) {continent = C ;}
	public void setMap(int M) {map = M ;}
	public void setdir(String d) {dir = d ;}
	public void setThought(String T) {Thought = T ;}
	public void setPos(int[] P) {Pos = P ;}
	public void setSize(int[] S) {Size = S ;}
	public void setLife(float[] L) {Life = L ;}
	public void setMp(float[] M) {Mp = M ;}
	public void setRange(float R) {Range = R ;}
	public void setStep(int S) {Step = S ;}
	public void setExp(float[] E) {Exp = E ;}
	public void setSatiation(float[] S) {Satiation = S ;}
	public void setThirst(float[] T) {Thirst = T ;}

	public String randomDir()
	{
		int dir = (int)(4*Math.random() - 0.01) ;

		return Player.MoveKeys[dir] ;
	}
	public int[] CalcNewPos(int move, int[] CurrentPos, int step)
	{
		int[] newPos = new int[2] ;
		step = 2 ;
		if (move == 0)	// Down
		{
			newPos = new int[] {CurrentPos[0], CurrentPos[1] - step} ;	
		}
		if (move == 1)	// Left
		{
			newPos = new int[] {CurrentPos[0] - step, CurrentPos[1]} ;
		}
		if (move == 2)	// Up
		{
			newPos = new int[] {CurrentPos[0], CurrentPos[1] + step} ;
		}
		if (move == 3)	// Right
		{
			newPos = new int[] {CurrentPos[0] + step, CurrentPos[1]} ;
		}
		
		return newPos ;
	}
	public int[] CalcNewPos(String dir, int[] CurrentPos, int step)
	{
		int[] newPos = new int[2] ;
		step = 1 ;
		if (dir.equals(Player.MoveKeys[0]))	// North
		{
			newPos = new int[] {CurrentPos[0], CurrentPos[1] - step} ;
		}
		if (dir.equals(Player.MoveKeys[1]))	// West
		{
			newPos = new int[] {CurrentPos[0] - step, CurrentPos[1]} ;
		}
		if (dir.equals(Player.MoveKeys[2]))	// South
		{
			newPos = new int[] {CurrentPos[0], CurrentPos[1] + step} ;	
		}
		if (dir.equals(Player.MoveKeys[3]))	// East
		{
			newPos = new int[] {CurrentPos[0] + step, CurrentPos[1]} ;
		}
		
		return newPos ;
	}
	/*public int[] CenterPos()
	{
		return new int[] {(int) (Pos[0]), (int) (Pos[1] - 0.5 * Size[1])} ;
	}*/
	
	public void incLife(float amount)
	{
		Life[0] = Math.max(0, Math.min(Life[0] + amount, Life[1])) ;	
	}
	public void incMP(float amount)
	{
		Mp[0] = Math.max(0, Math.min(Mp[0] + amount, Mp[1])) ;	
	}
	public void incSatiation(float amount)
	{
		Satiation[0] = Math.max(0, Math.min(Satiation[0] + amount, Satiation[1])) ;	
	}
	public void incThirst(float amount)
	{
		Thirst[0] = Math.max(0, Math.min(Thirst[0] + amount, Thirst[1])) ;	
	}
	
	
	public void printAtt()
	{
		System.out.println("Name: " + Name);
		System.out.println("Image: " + Arrays.toString(image));
		System.out.println("Level: " + Level);
		System.out.println("Continent: " + continent);
		System.out.println("Map: " + map);
		System.out.println("Pos: " + Arrays.toString(Pos));
		System.out.println("dir: " + dir);
		System.out.println("Thought: " + Thought);
		System.out.println("Size: " + Arrays.toString(Size));
		System.out.println("Life: " + Arrays.toString(Life));
		System.out.println("Mp: " + Arrays.toString(Mp));
		System.out.println("Range: " + Range);
		System.out.println("Step: " + Step);
		System.out.println("Exp: " + Arrays.toString(Exp));
		System.out.println("Satiation: " + Arrays.toString(Satiation));
		System.out.println("Thirst: " + Arrays.toString(Thirst));
	}
}