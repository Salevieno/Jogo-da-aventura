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

	private static final Map<Integer, Buff> ALL_BUFFS ;
	private static final Map<Integer, Buff> ALL_NERFS ;
	
	static
	{
		ALL_BUFFS = new HashMap<>() ;
		ALL_NERFS = new HashMap<>() ;
	}
	
	protected Buff(boolean isBuff, int id, Map<Attributes, BuffPower> power)
	{
		this.power = power ;
		if (isBuff)
		{
			ALL_BUFFS.put(id, this) ;
		}
		else
		{
			ALL_NERFS.put(id, this) ;
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

	public static Map<Integer, Buff> getAllBuffs() { return ALL_BUFFS ;}
	public static Map<Integer, Buff> getAllNerfs() { return ALL_NERFS ;}

	@Override
	public String toString()
	{
		return "Buff [power=" + power + "]";
	}
	
}
