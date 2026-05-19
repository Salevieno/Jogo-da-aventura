package attributes;

import org.json.simple.JSONObject;

import spells.AttMod;
import utilities.Util;

public class BasicBattleAttribute extends LiveBeingAttribute
{
	private double baseValue ;
	private double train ;		// increases with use

	public BasicBattleAttribute(double baseValue)
	{
		super(0.0);
		this.baseValue = baseValue;
		this.train = 0.0 ;
	}

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

	public double getBaseValue() { return baseValue ;}
	public double getTrain() { return train ;}

	public void incBaseValue(double inc) {baseValue += inc ;}
	public void incTrain(double inc) {train += inc ;}
	public String text() {return Util.round(baseValue, 1) + " + " + Util.round(bonus, 1) + " + " + Util.round(train, 1) ;}
	
	public double getTotal() {return baseValue + bonus + train ;}
	public double modified(double perc, double value) { return baseValue * (1 + perc) + value ;}
	public double totalModified(double perc, double value) { return getTotal() * (1 + perc) + value ;}
	public double totalModified(AttMod attMod) { return getTotal() * (1 + attMod.getPercentualIncrease()) + attMod.getValueIncrease() ;}

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
