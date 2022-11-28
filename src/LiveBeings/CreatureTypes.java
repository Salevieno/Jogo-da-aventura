package LiveBeings ;

import java.awt.Color ;
import java.awt.Image ;
import java.io.IOException;
import java.util.Arrays;

import Utilities.UtilG;

public class CreatureTypes 
{
	private int Type ;
	protected MovingAnimations movingAni ;
	protected PersonalAttributes PA ;
	protected BattleAttributes BA ;
	private Spells[] spell ;
	private int[] Bag ;
	private int Gold ;
	private Color color ;
	private int[] StatusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]	
	
	private static int NumberOfCreatureTypes ;
	
	public CreatureTypes(int Type, MovingAnimations movingAni, PersonalAttributes PA, BattleAttributes BA, Spells[] spell, int[] Bag, int Gold, Color color, int[] StatusCounter)
	{
		this.Type = Type ;
		this.movingAni = movingAni ;
		this.PA = PA ;
		this.BA = BA ;
		this.spell = spell ;
		this.Bag = Bag ;
		this.Gold = Gold ;
		this.color = color ;
		this.StatusCounter = StatusCounter ;
	}

	public int getID() {return Type ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public PersonalAttributes getPA() {return PA ;}
	public BattleAttributes getBA() {return BA ;}
	public Spells[] getSkill() {return spell ;}
	public int[] getBag() {return Bag ;}
	public int getGold() {return Gold ;}
	public Color getColor() {return color ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public void setID(int I) {Type = I ;}
	public void setSkill(Spells[] S) {spell = S ;}
	public void setBag(int[] B) {Bag = B ;}
	public void setGold(int G) {Gold = G ;}
	public void setColor(Color C) {color = C ;}
	public void setStatusCounter(int[] S) {StatusCounter = S ;}
	
	public static int getNumberOfCreatureTypes()
	{
		return NumberOfCreatureTypes ;
	}
	public static void setNumberOfCreatureTypes(int num)
	{
		NumberOfCreatureTypes = num ;
	}
	
	/* Print methods */
	public void printAtt()
	{
		//TODO print attributes
		System.out.println();
		System.out.println("** Creature attributes **");
	}

	@Override
	public String toString() {
		return "CreatureTypes [Type=" + Type + ", movingAni=" + movingAni + ", PA=" + PA + ", BA=" + BA + ", Skill="
				+ Arrays.toString(spell) + ", Bag=" + Arrays.toString(Bag) + ", Gold=" + Gold + ", color=" + color
				+ ", StatusCounter=" + Arrays.toString(StatusCounter) + "]";
	}
}
