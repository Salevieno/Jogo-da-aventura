package spells;

import java.util.HashMap;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;
import liveBeings.LiveBeing;
import main.Log;
import utilities.Util;

public class Buff
{
    private LiveBeing target ;
	private Map<Attributes, Double> incApplied ;
    private Map<Attributes, BuffPower> powerApplied ;

    private final double chance ;
	private final Map<Attributes, BuffPower> power ;

	private static final Map<Integer, Buff> ALL_BUFFS = new HashMap<>() ;
	private static final Map<Integer, Buff> ALL_NERFS = new HashMap<>() ;

	protected Buff(boolean isBuff, int id, double chance, Map<Attributes, BuffPower> power)
	{
        this.chance = chance ;
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

	public Buff(Buff buff)
	{
        this.chance = buff.chance ;
        this.power = buff.power ;
        this.incApplied = new HashMap<>() ;
        this.powerApplied = new HashMap<>() ;
	}

	private void apply(int mult, int level, LiveBeing receiver)
	{
		if (receiver == null) { Log.warn("Tentando usar buffs de magia em ninguém!") ; return ;}

        if (!Util.chance(chance)) { return ;}

		for (Attributes att : power.keySet())
		{			
			BasicAttribute personalAttribute = receiver.getPA().mapAttributes(att) ;
			if (personalAttribute != null)
			{
				double increment = personalAttribute.getMaxValue() * power.get(att).getPercentIncrease() + power.get(att).getValueIncrease() ;
				personalAttribute.incBonus((int) (increment * level * mult));
                incApplied.put(att, (double) (int) (increment * level * mult)) ;
				
				continue ;
			}
			
			BasicBattleAttribute battleAttribute = receiver.getBA().mapAttributes(att) ;
			if (battleAttribute != null)
			{
                System.out.println("Applying buff/nerf to " + receiver.getName() + ": attribute=" + att + " power=" + power.get(att) + ", level=" + level + ", mult=" + mult);
				double increment = battleAttribute.getBaseValue() * power.get(att).getPercentIncrease() + power.get(att).getValueIncrease() ;
                System.out.println("Increment calculated: " + increment);
				battleAttribute.incBonus(increment * level * mult);
                incApplied.put(att, increment * level * mult) ;

				continue ;
			}

			BattleSpecialAttribute battleSpecialAttribute = receiver.getBA().mapSpecialAttributes(att) ;
			if (battleSpecialAttribute != null)
			{
				battleSpecialAttribute.incAtkChanceBonus(power.get(att).getPercentIncrease() * level * mult);
				battleSpecialAttribute.incAtkChanceBonus(power.get(att).getValueIncrease() * level * mult);
                powerApplied.put(att, new BuffPower(power.get(att).getPercentIncrease() * level * mult, power.get(att).getValueIncrease() * level * mult)) ;
			}
		}
	}

    private void remove(int level, LiveBeing target)
    {
        if (target == null) { Log.warn("Tentando remover buffs de magia de ninguém!") ; return ;}

        incApplied.forEach((att, increment) -> {
            BasicAttribute personalAttribute = target.getPA().mapAttributes(att) ;
            if (personalAttribute != null)
            {
                personalAttribute.incBonus(-increment);
                return ;
            }

            BasicBattleAttribute battleAttribute = target.getBA().mapAttributes(att) ;
            if (battleAttribute != null)
            {
                battleAttribute.incBonus(-increment);
                return ;
            }
        }) ;

        powerApplied.forEach((att, increment) -> {
            BattleSpecialAttribute battleSpecialAttribute = target.getBA().mapSpecialAttributes(att) ;
            if (battleSpecialAttribute != null)
            {
                battleSpecialAttribute.incAtkChanceBonus(-increment.getPercentIncrease()) ;
                battleSpecialAttribute.incAtkChanceBonus(-increment.getValueIncrease()) ;
                return ;
            } 
        }) ;
    }

    public void applyBuff(int level, LiveBeing receiver)
	{
        // System.out.println("\nApplying buff to " + receiver.getName() + ": level=" + level) ;
        target = receiver ;
        apply(1, level, target) ;
    }

    public void applyNerf(int level, LiveBeing receiver)
	{
        System.out.println("\nApplying nerf to " + receiver.getName() + ": level=" + level) ;
        target = receiver ;
        apply(-1, level, target) ;
    }

    public void removeBuff(int level)
	{
        // System.out.println("\nRemoving buff from " + target.getName() + ": level=" + level) ;
        remove(level, target) ;
        target = null ;
    }

    public void removeNerf(int level)
	{
        System.out.println("\nRemoving nerf from " + target.getName() + ": level=" + level) ;
        remove(level, target) ;
        target = null ;
    }

	public static Map<Integer, Buff> getAllBuffs() { return ALL_BUFFS ;}
	public static Map<Integer, Buff> getAllNerfs() { return ALL_NERFS ;}

	@Override
	public String toString()
	{
		return "Buff [power=" + power + "]";
	}
	
}
