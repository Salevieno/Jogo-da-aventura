package attributes;

import org.json.simple.JSONObject;

public class BasicAttribute extends LiveBeingAttribute
{
	private int currentValue ;
	private int maxValue ;
	private double multiplier ;

	public BasicAttribute(int currentValue, int maxValue, double multiplier)
	{
		super(0);
		this.currentValue = currentValue;
		this.maxValue = maxValue;
		this.multiplier = multiplier;
	}

	public int getCurrentValue() { return currentValue ;}
	public int getMaxValue() { return maxValue ;}
	public double getMultiplier() { return multiplier ;}
	public int getTotalValue() { return (int) (currentValue + bonus) ;}
	public double getRate() { return getTotalValue() / (double) maxValue ;}
	public boolean isMaxed() { return getTotalValue() == maxValue ;}

	public void incCurrentValue(int amount)
	{
		currentValue = Math.max(0, Math.min(currentValue + amount, maxValue)) ;
	}
	public void decTotalValue(int amount)
	{
		if (amount <= 0) { System.out.println("tentando usar decTotalValue com valor negativo ou 0") ; return ;}
		
		if (Math.abs(amount) <= bonus) { bonus += -amount ; return ;}
		
		currentValue = (int) Math.max(currentValue - Math.abs(amount - bonus), 0) ;
		bonus = 0 ;
		
		return ;
	}
	public void incMaxValue(int amount) {maxValue += amount ;}
	public void setToMaximum() {currentValue = maxValue ;}

	public static BasicAttribute fromJson(JSONObject jsonData)
	{

		int currentValue = (int) (long) jsonData.get("currentValue") ;
		int maxValue = (int) (long) jsonData.get("maxValue") ;
		double multiplier = (double) (Double) jsonData.get("multiplier") ;
		return new BasicAttribute(currentValue, maxValue, multiplier) ;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{

        JSONObject content = new JSONObject();
        content.put("currentValue", currentValue);
        content.put("maxValue", maxValue);
        content.put("multiplier", multiplier);
        
        return content ;
	}
	
	@Override
	public String toString()
	{
		return "BasicAttribute [currentValue=" + currentValue + ", bonus=" + bonus + ", maxValue=" + maxValue + " totalValue=" + getTotalValue() + ", multiplier=" + multiplier+ "]" ;
//		return " bonus=" + bonus ;
	}
	
}