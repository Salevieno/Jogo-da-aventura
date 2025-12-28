package attributes;

import org.json.simple.JSONObject;

import utilities.Util;

public class BasicBattleAttribute extends LiveBeingAttribute
{
	private double baseValue ;	// base value of the attribute
	private double train ;		// training value of the attribute (increases with use)
	
	public BasicBattleAttribute(double baseValue, double bonus, double train)
	{
		super(bonus);
		this.baseValue = baseValue;
		this.train = train ;
	}
	
	public BasicBattleAttribute(BasicBattleAttribute basicBA)
	{
		super(basicBA.getBonus());
		this.baseValue = basicBA.getBaseValue();
		this.train = basicBA.getTrain() ;
	}

	public double getBaseValue()
	{
		return baseValue;
	}

	public void setBaseValue(double baseValue)
	{
		this.baseValue = baseValue;
	}
	
	public double getTrain() {return train ;}
	public void setTrain(double newValue) {train = newValue ;}

	public void incBaseValue(double inc) {baseValue += inc ;}
	public void incTrain(double inc) {train += inc ;}
	public String text() {return Util.round(baseValue, 1) + " + " + Util.round(bonus, 1) + " + " + Util.round(train, 1) ;}
	
	public double getTotal() {return baseValue + bonus + train ;}

	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject()
	{

        JSONObject content = new JSONObject();
        content.put("baseValue", baseValue);
        content.put("bonus", bonus);
        content.put("train", train);
        
        return content ;
        
	}

	public static BasicBattleAttribute fromJson(JSONObject jsonData)
	{

		double baseValue = (double) (Double) jsonData.get("baseValue") ;
		double bonus = (double) (Double) jsonData.get("bonus") ;
		double train = (double) (Double) jsonData.get("train") ;
		return new BasicBattleAttribute(baseValue, bonus, train) ;
		
	}

	public String toStringSimple()
	{
		return String.format("%.3f (%.3f + %.3f + %.3f)",
				getTotal(), baseValue, bonus, train) ;
	}
	
	@Override
	public String toString()
	{
		return String.format("Basic Battle Attribute: Base Value: %.3f, Bonus: %.3f, Train: %.3f, Total: %.3f",
				baseValue, bonus, train, getTotal()) ;
	}
	
	
}
