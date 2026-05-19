package spells;

import java.util.HashMap;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;
import liveBeings.LiveBeing;
import main.Log;

public class Buff
{
	private final Map<Attributes, BuffPower> power ;

	private static final Map<Integer, Buff> allBuffs ;
	private static final Map<Integer, Buff> allNerfs ;
	
	static
	{
		allBuffs = new HashMap<>() ;
		allNerfs = new HashMap<>() ;
	}
	
	protected Buff(boolean isBuff, int id, Map<Attributes, BuffPower> power)
	{
		this.power = power ;
		if (isBuff)
		{
			allBuffs.put(id, this) ;
		}
		else
		{
			allNerfs.put(id, this) ;
		}
	}

	public void apply(int mult, int level, LiveBeing receiver)
	{
		if (receiver == null) { Log.warn("Tentando usar buffs de magia em ninguém!") ; return ;}

		for (Attributes att : power.keySet())
		{			
			BasicAttribute personalAttribute = receiver.getPA().mapAttributes(att) ;
			if (personalAttribute != null)
			{
				double increment = personalAttribute.getMaxValue() * power.get(att).getPercentIncrease() + power.get(att).getValueIncrease() ;
				personalAttribute.incBonus((int) (increment * level * mult));
				
				continue ;
			}
			
			BasicBattleAttribute battleAttribute = receiver.getBA().mapAttributes(att) ;
			if (battleAttribute != null)
			{
				double increment = battleAttribute.getBaseValue() * power.get(att).getPercentIncrease() + power.get(att).getValueIncrease() ;
				battleAttribute.incBonus(increment * level * mult);

				continue ;
			}
			
			BattleSpecialAttribute battleSpecialAttribute = receiver.getBA().mapSpecialAttributes(att) ;
			if (battleSpecialAttribute != null)
			{
				battleSpecialAttribute.incAtkChanceBonus(power.get(att).getPercentIncrease() * level * mult);
				battleSpecialAttribute.incAtkChanceBonus(power.get(att).getValueIncrease() * level * mult);
			}
		}
	}

	public static Map<Integer, Buff> getAllBuffs() { return allBuffs ;}
	public static Map<Integer, Buff> getAllNerfs() { return allNerfs ;}

	@Override
	public String toString()
	{
		return "Buff [power=" + power + "]";
	}
	
}
