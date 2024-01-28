package liveBeings;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import main.AtkResults;
import main.AtkTypes;
import main.Game;
import main.TextCategories;
import utilities.AtkEffects;

public class Statistics
{
	private int numberPhyAtk ;
	private int numberMagAtk ;
	private int numberDef ;
	private int numberHitsInflicted ;
	private int numberDodges ;
	private int numberCritInflicted ;
	private int numberStunInflicted ;
	private int numberBlockInflicted ;
	private int numberBloodInflicted ;
	private int numberPoisonInflicted ;
	private int numberSilenceInflicted ;
	private int phyDamageInflicted ;
	private int phyDamageReceived ;
	private int phyDamageDefended ;
	private int magDamageInflicted ;
	private int magDamageReceived ;
	private int magDamageDefended ;
	private int critDamageInflicted ;
	private int critDamageReceived ;
	private int critDamageDefended ;
	private int bloodDamageInflicted ;
	private int bloodDamageReceived ;
	private int bloodDamageDefended ;
	private int poisonDamageInflicted ;
	private int poisonDamageReceived ;
	private int poisonDamageDefended ;
	private int highestPhyDamageInflicted ;
	private int highestMagDamageInflicted ;
		
	
	public int getNumberPhyAtk()
	{
		return numberPhyAtk;
	}
	public int getNumberMagAtk()
	{
		return numberMagAtk;
	}
	public int getNumberDef()
	{
		return numberDef;
	}
	public int getNumberHitsInflicted()
	{
		return numberHitsInflicted;
	}
	public int getNumberDodges()
	{
		return numberDodges;
	}
	public int getNumberCritInflicted()
	{
		return numberCritInflicted;
	}
	public int getNumberStunInflicted()
	{
		return numberStunInflicted;
	}
	public int getNumberBlockInflicted()
	{
		return numberBlockInflicted;
	}
	public int getNumberBloodInflicted()
	{
		return numberBloodInflicted;
	}
	public int getNumberPoisonInflicted()
	{
		return numberPoisonInflicted;
	}
	public int getNumberSilenceInflicted()
	{
		return numberSilenceInflicted;
	}
	public int getPhyDamageInflicted()
	{
		return phyDamageInflicted;
	}
	public int getPhyDamageReceived()
	{
		return phyDamageReceived;
	}
	public int getPhyDamageDefended()
	{
		return phyDamageDefended;
	}
	public int getMagDamageInflicted()
	{
		return magDamageInflicted;
	}
	public int getMagDamageReceived()
	{
		return magDamageReceived;
	}
	public int getMagDamageDefended()
	{
		return magDamageDefended;
	}
	public int getCritDamageInflicted()
	{
		return critDamageInflicted;
	}
	public int getCritDamageReceived()
	{
		return critDamageReceived;
	}
	public int getCritDamageDefended()
	{
		return critDamageDefended;
	}
	public int getBloodDamageInflicted()
	{
		return bloodDamageInflicted;
	}
	public int getBloodDamageReceived()
	{
		return bloodDamageReceived;
	}
	public int getBloodDamageDefended()
	{
		return bloodDamageDefended;
	}
	public int getPoisonDamageInflicted()
	{
		return poisonDamageInflicted;
	}
	public int getPoisonDamageReceived()
	{
		return poisonDamageReceived;
	}
	public int getPoisonDamageDefended()
	{
		return poisonDamageDefended;
	}
	public int getHighestPhyDamageInflicted()
	{
		return highestPhyDamageInflicted;
	}
	public int getHighestMagDamageInflicted()
	{
		return highestMagDamageInflicted;
	}
	
	
	public void incNumberPhyAtk() { numberPhyAtk += 1 ;}
	public void incNumberMagAtk() { numberMagAtk += 1 ;}
	public void incNumberDef() { numberDef += 1 ;}
	public void incNumberHitsInflicted() { numberHitsInflicted += 1 ;}
	public void incNumberDodges() { numberDodges += 1 ;}
	public void incNumberCritInflicted() { numberCritInflicted += 1 ;}
	public void incPhyDamageInflicted(int amount) { phyDamageInflicted += amount ;}
	public void incPhyDamageReceived(int amount) { phyDamageReceived += amount ;}
	public void incPhyDamageDefended(int amount) { phyDamageDefended += amount ;}
	public void incMagDamageInflicted(int amount) { magDamageInflicted += amount ;}
	public void incMagDamageReceived(int amount) { magDamageReceived += amount ;}
	public void incMagDamageDefended(int amount) { magDamageDefended += amount ;}
	public void incCritDamageInflicted(int amount) { critDamageInflicted += amount ;}
	public void incNumberStunInflicted() { numberStunInflicted += 1 ;}
	public void incNumberBlockInflicted() { numberBlockInflicted += 1 ;}
	public void incNumberBloodInflicted() { numberBloodInflicted += 1 ;}
	public void incBloodDamageInflicted(int amount) { bloodDamageInflicted += amount ;}
	public void incBloodDamageDefended(int amount) { bloodDamageDefended += amount ;}
	public void incNumberPoisonInflicted() { numberPoisonInflicted += 1 ;}
	public void incPoisonDamageInflicted(int amount) { poisonDamageInflicted += amount ;}
	public void incPoisonDamageDefended(int amount) { poisonDamageDefended += amount ;}
	public void incNumberSilenceInflicted() { numberSilenceInflicted += 1 ;}
	

	public void update(AtkResults atkResults)
	{		
		if (atkResults == null) { return ;}
		if (atkResults.getEffect().equals(AtkEffects.none)) { return ;}
		
		AtkTypes atkType = atkResults.getAtkType() ;
		AtkEffects effect = atkResults.getEffect() ;
		int damage = (int) atkResults.getDamage() ;
		
		
		if (0 <= damage)
		{	
			if (atkType.equals(AtkTypes.magical))
			{
				incNumberMagAtk() ;
				if (effect.equals(AtkEffects.hit))
				{
					incMagDamageInflicted(damage) ;
				}
				highestMagDamageInflicted = Math.max(highestMagDamageInflicted, damage) ;
			}
			else
			{
				incNumberPhyAtk() ;
				if (effect.equals(AtkEffects.hit))
				{
					incPhyDamageInflicted(damage) ;
				}
				highestPhyDamageInflicted = Math.max(highestPhyDamageInflicted, damage) ;
			}			
		}
		
		if (atkType.equals(AtkTypes.defense))
		{
			incNumberDef() ;
		}
		
		if (effect == null) { return ;}
		
		if (effect.equals(AtkEffects.hit))							// player performed a successful hit
		{
			incNumberHitsInflicted() ;
		}
		if (effect.equals(AtkEffects.crit))
		{
			incNumberHitsInflicted() ;
			incNumberCritInflicted() ;
			incCritDamageInflicted(damage) ;
		}
	}
	public void updatedefensiveStats(int damage, AtkEffects effect, boolean creaturePhyAtk, Creature creature)
	{
		if (effect.equals(AtkEffects.hit))
		{
			if (creaturePhyAtk)
			{				
				incPhyDamageReceived(damage) ;
//				stats[7] += BA.TotalPhyDef() ;		// total phy damage defended by the player
			}
			else				// Creature magical atk
			{
				incMagDamageReceived(damage) ;
//				stats[8] += BA.TotalMagDef() ;		// total mag damage defended by the player
			}
//			if (0 < BA.getSpecialStatus()[2])
//			{
//				statistics[18] += BA.getBlood()[3] + BA.getBlood()[4] ;		// total number of blood defended by the player
//			}
//			if (0 < BA.getSpecialStatus()[3])
//			{
//				statistics[21] += BA.getPoison()[3] + BA.getPoison()[4] ;	// total number of blood defended by the player
//			}
			
			return ;
		}

		incNumberDodges() ;
	}

	public Map<String, Integer> numberStats()
	{
		String[] statsText = Game.allText.get(TextCategories.statistics) ;
		Map<String, Integer> numberStats = new LinkedHashMap<>() ;
		Map<String, Double> allStats = allStatistics() ;
		for (int i = 0 ; i <= 11 - 1; i += 1)
		{
			numberStats.put(statsText[i], (Integer) (int) (double) allStats.get(statsText[i])) ;
		}
		
		return numberStats ;
	}
	
	public Map<String, Double> damageStats()
	{
		String[] statsText = Game.allText.get(TextCategories.statistics) ;
		Map<String, Double> damageStats = new LinkedHashMap<>() ;
		Map<String, Double> allStats = allStatistics() ;
		for (int i = 11 ; i <= statsText.length - 2 - 1; i += 1)
		{
			damageStats.put(statsText[i], allStats.get(statsText[i])) ;
		}
		
		return damageStats ;
	}
	
	public Map<String, Double> maxStats()
	{
		String[] statsText = Game.allText.get(TextCategories.statistics) ;
		Map<String, Double> maxStats = new LinkedHashMap<>() ;
		Map<String, Double> allStats = allStatistics() ;
		for (int i = statsText.length - 2 ; i <= statsText.length - 1; i += 1)
		{
			maxStats.put(statsText[i], allStats.get(statsText[i])) ;
		}
		
		return maxStats ;
	}
	
	public Map<String, Double> allStatistics()
	{
		Map<String, Double> allStats = new LinkedHashMap<>() ;
		String[] statsText = Game.allText.get(TextCategories.statistics) ;
		Field[] fields = Statistics.class.getDeclaredFields() ;
		for (int i = 0 ; i <= fields.length - 1 ; i += 1)
		{
			try
			{
				if (fields[i].getType().equals(int.class))
				{
					allStats.put(statsText[i], (Double) (double) (int) (Integer) fields[i].get(this)) ;
				}
				else
				{
					allStats.put(statsText[i], (Double) fields[i].get(this)) ;
				}
			}
			catch (IllegalArgumentException e) { e.printStackTrace() ;}
			catch (IllegalAccessException e) { e.printStackTrace() ;}
		}
		
//		for (Field field : fields)
//		{
//			try
//			{
//				if (field.getType().equals(int.class))
//				{
//					allStats.put(field.getName(), (Double) (double) (int) (Integer) field.get(this)) ;
//				}
//				else
//				{
//					allStats.put(field.getName(), (Double) field.get(this)) ;
//				}
//			}
//			catch (IllegalArgumentException e) { e.printStackTrace() ;}
//			catch (IllegalAccessException e) { e.printStackTrace() ;}
//		}
		
		return allStats ;
	}
}
