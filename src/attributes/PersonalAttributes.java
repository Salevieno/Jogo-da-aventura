package attributes ;

import java.awt.Point;

import org.json.simple.JSONObject;

import utilities.Directions;
import utilities.UtilG;

public class PersonalAttributes
{
	
	private BasicAttribute life ;
	private BasicAttribute mp ;
	private BasicAttribute exp ;
	private BasicAttribute satiation ;
	private BasicAttribute thirst ;
	
	public PersonalAttributes(BasicAttribute Life, BasicAttribute Mp, BasicAttribute Exp, BasicAttribute Satiation, BasicAttribute Thirst)
	{
		this.life = Life ;
		this.mp = Mp ;
		this.exp = Exp ;
		this.satiation = Satiation ;
		this.thirst = Thirst ;
	}
	public BasicAttribute getLife() {return life ;}
	public BasicAttribute getMp() {return mp ;}
	public BasicAttribute getExp() {return exp ;}
	public BasicAttribute getSatiation() {return satiation ;}
	public BasicAttribute getThirst() {return thirst ;}
	
	public void setLife(BasicAttribute newValue) {life = newValue ;}
	public void setMp(BasicAttribute newValue) {mp = newValue ;}
	public void setExp(BasicAttribute newValue) {exp = newValue ;}
	public void setSatiation(BasicAttribute newValue) {satiation = newValue ;}
	public void setThirst(BasicAttribute newValue) {thirst = newValue ;}

	public BasicAttribute mapAttributes(Attributes att)
	{
		switch (att)
		{
			case life: return life ;
			case mp: return mp ;
			case exp: return exp ;
			case satiation: return satiation ;
			case thirst: return thirst ;
			
			default: return null ;
		}
	}
	public static int numberFightsToLevelUp(int currentExp, int totalExp, int opponentExp, double expMult)
	{
		return 1 + (int) ((totalExp - currentExp) / (opponentExp * expMult)) ;
	}
	
	public static Directions randomDir()
	{
		// TODO what is directions doing here?
		int dir = UtilG.randomIntFromTo(0, 3) ;

		return Directions.getDir(dir) ;
	}
	public Point CalcNewPos(Directions dir, Point currentPos, int step)
	{
		Point newPos = new Point(0, 0) ;
		step = 1 ;
		switch (dir)
		{
			case up: newPos = new Point(currentPos.x, currentPos.y - step) ; break ;
			case down: newPos = new Point(currentPos.x, currentPos.y + step) ; break ;
			case left: newPos = new Point(currentPos.x - step, currentPos.y) ; break ;
			case right: newPos = new Point(currentPos.x + step, currentPos.y) ; break ;
		}
		
		return newPos ;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject()
	{

        JSONObject content = new JSONObject();
        content.put("life", life.toJson());
        content.put("mp", mp.toJson());
        content.put("exp", exp.toJson());
        content.put("satiation", satiation.toJson());
        content.put("thirst", thirst.toJson());
        
        return content ;
        
	}
	
	public static PersonalAttributes fromJson(JSONObject jsonData)
	{

		BasicAttribute life = BasicAttribute.fromJson((JSONObject) jsonData.get("life")) ;
		BasicAttribute mp = BasicAttribute.fromJson((JSONObject) jsonData.get("mp")) ;
		BasicAttribute exp = BasicAttribute.fromJson((JSONObject) jsonData.get("exp")) ;
		BasicAttribute satiation = BasicAttribute.fromJson((JSONObject) jsonData.get("satiation")) ;
		BasicAttribute thirst = BasicAttribute.fromJson((JSONObject) jsonData.get("thirst")) ;
		
		return new PersonalAttributes(life, mp, exp, satiation, thirst) ;
	}
	
	@Override
	public String toString()
	{
		return "PersonalAttributes [Life=" + life + ", Mp=" + mp + ", Exp=" + exp + ", Satiation=" + satiation
				+ ", Thirst=" + thirst + "]";
	}

}