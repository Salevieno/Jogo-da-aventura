package attributes;

import java.util.List;

public class AttributeIncrease
{
	private final AttributeBonus increment ;
	private final AttributeBonus chance ;
	
	public AttributeIncrease(List<Double> increments, List<Double> chances)
	{
		increment = new AttributeBonus() ;
		chance = new AttributeBonus() ;
		increment.setBasic(increments) ;
		chance.setBasic(chances) ;
	}

	public AttributeBonus getIncrement() { return increment ;}
	public AttributeBonus getChance() { return chance ;}	
	
}
