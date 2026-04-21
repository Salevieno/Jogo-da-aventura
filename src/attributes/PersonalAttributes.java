package attributes ;

import org.json.simple.JSONObject;

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

	public PersonalAttributes(int lifeCurrent, int lifeMax, double lifeMult,
		int mpCurrent, int mpMax, double mpMult,
		int expCurrent, int expMax, double expMult,
		int satiationCurrent, int satiationMax, double satiationMult,
		int thirstCurrent, int thirstMax, double thirstMult)
	{
		this.life = new BasicAttribute(lifeCurrent, lifeMax, lifeMult) ;
		this.mp = new BasicAttribute(mpCurrent, mpMax, mpMult) ;
		this.exp = new BasicAttribute(expCurrent, expMax, expMult) ;
		this.satiation = new BasicAttribute(satiationCurrent, satiationMax, satiationMult) ;
		this.thirst = new BasicAttribute(thirstCurrent, thirstMax, thirstMult) ;
	}
	
	public PersonalAttributes(PersonalAttributes PA)
	{
		this.life = new BasicAttribute(PA.getLife()) ;
		this.mp = new BasicAttribute(PA.getMp()) ;
		this.exp = new BasicAttribute(PA.getExp()) ;
		this.satiation = new BasicAttribute(PA.getSatiation()) ;
		this.thirst = new BasicAttribute(PA.getThirst()) ;
	}
	public BasicAttribute getLife() {return life ;}
	public BasicAttribute getMp() {return mp ;}
	public BasicAttribute getExp() {return exp ;}
	public BasicAttribute getSatiation() {return satiation ;}
	public BasicAttribute getThirst() {return thirst ;}

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
		return String.format("Personal Attributes:\n  Life: %s\n  Mp: %s\n  Exp: %s\n  Satiation: %s\n  Thirst: %s\n",
				life.toString(), mp.toString(), exp.toString(), satiation.toString(), thirst.toString()) ;
	}

}