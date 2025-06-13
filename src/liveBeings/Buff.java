package liveBeings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;
import main.Game;
import utilities.Util;

public class Buff
{
	private final Map<Attributes, Double> percentIncrease;
	private final Map<Attributes, Double> valueIncrease;
	private final Map<Attributes, Double> chance;
	
	public static final List<Buff> allBuffs ;
	public static final List<Buff> allDebuffs ;
	
	static
	{
		allBuffs = new ArrayList<>() ;
		allDebuffs = new ArrayList<>() ;
	}
	
	public Buff(Map<Attributes, Double> percentIncrease, Map<Attributes, Double> valueIncrease, Map<Attributes, Double> chance)
	{
		this.percentIncrease = percentIncrease;
		this.valueIncrease = valueIncrease;
		this.chance = chance;
	}
	
	public static void loadBuffs()
	{
		List<String[]> spellsBuffsInput = Util.ReadcsvFile(Game.CSVPath + "Buffs.csv") ;
		
		for (int i = 0 ; i <= spellsBuffsInput.size() - 1 ; i += 1)
		{
			allBuffs.add(Buff.load(spellsBuffsInput.get(i))) ;
		}
	}
	
	public static void loadDebuffs()
	{
		List<String[]> spellsDebuffsInput = Util.ReadcsvFile(Game.CSVPath + "Debuffs.csv") ;
		
		for (int i = 0 ; i <= spellsDebuffsInput.size() - 1 ; i += 1)
		{
			allDebuffs.add(Buff.load(spellsDebuffsInput.get(i))) ;
		}
	}
	
	public Map<Attributes, Double> getPercentIncrease() { return percentIncrease ;}
	public Map<Attributes, Double> getValueIncrease() { return valueIncrease ;}
	public Map<Attributes, Double> getChance() { return chance ;}
	
	public void apply(int mult, int level, LiveBeing receiver)
	{
		if (receiver == null) { System.out.println("Tentando usar buffs de magia em ninguém!") ; return ;}

		for (Attributes att : Attributes.values())
		{
			if (att.equals(Attributes.exp) | att.equals(Attributes.satiation) | att.equals(Attributes.thirst))
			{
				continue ;
			}
			
			BasicAttribute personalAttribute = receiver.getPA().mapAttributes(att) ;
			if (personalAttribute != null)
			{
				double increment = personalAttribute.getMaxValue() * percentIncrease.get(att) + valueIncrease.get(att) ;
				personalAttribute.incBonus((int) (increment * level * mult));
				
				continue ;
			}
			
			BasicBattleAttribute battleAttribute = receiver.getBA().mapAttributes(att) ;
			if (battleAttribute != null)
			{
				double increment = battleAttribute.getBaseValue() * percentIncrease.get(att) + valueIncrease.get(att) ;
				battleAttribute.incBonus(increment * level * mult);

				continue ;
			}
			
			BattleSpecialAttribute battleSpecialAttribute = receiver.getBA().mapSpecialAttributes(att) ;
			if (battleSpecialAttribute != null)
			{
				battleSpecialAttribute.incAtkChanceBonus(percentIncrease.get(att) * level * mult);
				battleSpecialAttribute.incAtkChanceBonus(valueIncrease.get(att) * level * mult);
			}
		}
	}
	
	public static Buff load(String[] spellsBuffsInp)
	{
		Map<Attributes, Double> percentIncrease = new HashMap<>() ;
		Map<Attributes, Double> valueIncrease = new HashMap<>() ;
		Map<Attributes, Double> chance = new HashMap<>() ;
		int BuffCont = 0 ;

		for (Attributes att : Attributes.values())
		{
			if (att.equals(Attributes.exp) | att.equals(Attributes.satiation) | att.equals(Attributes.thirst) | att.equals(Attributes.atkSpeed))
			{
				continue ;
			}
			if (att.equals(Attributes.blood) | att.equals(Attributes.poison))
			{
				percentIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 3])) ;
				valueIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 4])) ;
				chance.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 5])) ;
				BuffCont += 12 ;
			}
			else
			{
				percentIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 3])) ;
				valueIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 4])) ;
				chance.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 5])) ;
				BuffCont += 3 ;
			}
		}
		
		return new Buff(percentIncrease, valueIncrease, chance) ;
	}
	
	@Override
	public String toString()
	{
		return "Buff: inc% = " + percentIncrease + "\n incValue = " + valueIncrease + "\n chance = " + chance ;
	}
	
	
}
