package attributes;

import org.json.simple.JSONObject;

import utilities.Util;

public class BattleSpecialAttributeWithDamage extends BattleSpecialAttribute
{
	private double basicAtk ;
	private double basicAtkBonus ;
	private double basicDef ;
	private double basicDefBonus ;
	
	public BattleSpecialAttributeWithDamage(double basicAtkChance, double basicAtkChanceBonus, double basicDefChance,
			double basicDefChanceBonus, double basicAtk, double basicAtkBonus, double basicDef, double basicDefBonus, double duration)
	{
		super(basicAtkChance, basicAtkChanceBonus, basicDefChance, basicDefChanceBonus, duration) ;
		this.basicAtk = basicAtk ;
		this.basicAtkBonus = basicAtkBonus ;
		this.basicDef = basicDef ;
		this.basicDefBonus = basicDefBonus ;
	}
	
	public BattleSpecialAttributeWithDamage(BattleSpecialAttributeWithDamage battleSAWD)
	{
		super(battleSAWD.getBasicAtkChance(), battleSAWD.getBasicAtkChanceBonus(), battleSAWD.getBasicDefChance(), battleSAWD.getBasicDefChanceBonus(), battleSAWD.getDuration()) ;
		this.basicAtk = battleSAWD.getBasicAtk() ;
		this.basicAtkBonus = battleSAWD.getBasicAtkBonus() ;
		this.basicDef = battleSAWD.getBasicDef() ;
		this.basicDefBonus = battleSAWD.getBasicDefBonus() ;
	}
		
	public double getBasicAtk()
	{
		return basicAtk;
	}

	public double getBasicAtkBonus()
	{
		return basicAtkBonus;
	}

	public double getBasicDef()
	{
		return basicDef;
	}

	public double getBasicDefBonus()
	{
		return basicDefBonus;
	}

	public double[] attributes() { return new double[] {basicAtkChance, basicAtk, basicDefChance, basicDef, duration} ;}
	public double[] bonuses() { return new double[] {basicAtkChanceBonus, basicAtkBonus, basicDefChanceBonus, basicDefBonus} ;}
	public String textAtkChance() { return (int) (100 * basicAtkChance) + "% + " + (int) (100 * basicAtkChanceBonus) + "%" ;}
	public String textAtk() { return Util.Round(basicAtk, 2) + " + " + Util.Round(basicAtkBonus, 2) ;}
	public String textDefChance() { return (int) (100 * basicDefChance) + "% + " + (int) (100 * basicDefChanceBonus) + "%" ;}
	public String textDef() { return Util.Round(basicDef, 2) + " + " + Util.Round(basicDefBonus, 2) ;}
	public String textDuration() { return String.valueOf(duration) ;}
	public String[] texts() { return new String[] {textAtkChance(), textAtk(), textDefChance(), textDef(), textDuration()} ;}


	public double TotalAtk()
	{
		return basicAtk + basicAtkBonus ;
	}
	public double TotalDef()
	{
		return basicDef + basicDefBonus ;
	}
	public void incBasicAtk(double inc) {basicAtk += inc ;}
	public void incBasicDef(double inc) {basicDef += inc ;}
	public void incAtkBonus(double inc) {basicAtkBonus += inc ;}
	public void incDefBonus(double inc) {basicDefBonus += inc ;}


	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject()
	{

        JSONObject content = new JSONObject();
        content.put("basicAtkChance", basicAtkChance);
        content.put("basicAtk", basicAtk);
        content.put("basicDefChance", basicDefChance);
        content.put("basicDef", basicDef);
        content.put("basicAtkChanceBonus", basicAtkChanceBonus);
        content.put("basicAtkBonus", basicAtkBonus);
        content.put("basicDefChanceBonus", basicDefChanceBonus);
        content.put("basicDefBonus", basicDefBonus);
        content.put("duration", duration);
        
        return content ;
        
	}

	public static BattleSpecialAttributeWithDamage fromJson(JSONObject jsonData)
	{

		double basicAtkChance = (double) (Double) jsonData.get("basicAtkChance") ;
		double basicAtk = (double) (Double) jsonData.get("basicAtk") ;
		double basicDefChance = (double) (Double) jsonData.get("basicDefChance") ;
		double basicDef = (double) (Double) jsonData.get("basicDef") ;
		double basicAtkChanceBonus = (double) (Double) jsonData.get("basicAtkChanceBonus") ;
		double basicAtkBonus = (double) (Double) jsonData.get("basicAtkBonus") ;
		double basicDefChanceBonus = (double) (Double) jsonData.get("basicDefChanceBonus") ;
		double basicDefBonus = (double) (Double) jsonData.get("basicDefBonus") ;
		double duration = (double) (Double) jsonData.get("duration") ;
		return new BattleSpecialAttributeWithDamage(basicAtkChance, basicAtk,
				basicDefChance, basicDef,
				basicAtkChanceBonus, basicAtkBonus,
				basicDefChanceBonus, basicDefBonus,
				duration) ;
		
	}

	@Override
	public String toString() {
		return "BattleSpecialAttributeWithDamage [basicAtk=" + basicAtk + ", basicAtkBonus=" + basicAtkBonus
				+ ", basicDef=" + basicDef + ", basicDefBonus=" + basicDefBonus + ", basicAtkChance=" + basicAtkChance
				+ ", basicAtkChanceBonus=" + basicAtkChanceBonus + ", basicDefChance=" + basicDefChance
				+ ", basicDefChanceBonus=" + basicDefChanceBonus + ", duration=" + duration + "]";
	}
	
}
