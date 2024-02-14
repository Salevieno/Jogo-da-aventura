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
	public void incNumberStunInflicted() { numberStunInflicted += 1 ;}
	public void incNumberBlockInflicted() { numberBlockInflicted += 1 ;}
	public void incNumberBloodInflicted() { numberBloodInflicted += 1 ;}
	public void incNumberPoisonInflicted() { numberPoisonInflicted += 1 ;}
	public void incNumberSilenceInflicted() { numberSilenceInflicted += 1 ;}

	public void incPhyDamageInflicted(int amount) { phyDamageInflicted += amount ;}
	public void incPhyDamageReceived(int amount) { phyDamageReceived += amount ;}
	public void incPhyDamageDefended(int amount) { phyDamageDefended += amount ;}
	public void incMagDamageInflicted(int amount) { magDamageInflicted += amount ;}
	public void incMagDamageReceived(int amount) { magDamageReceived += amount ;}
	public void incMagDamageDefended(int amount) { magDamageDefended += amount ;}
	public void incCritDamageInflicted(int amount) { critDamageInflicted += amount ;}
	public void incCritDamageReceived(int amount) { critDamageReceived += amount ;}
	public void incCritDamageDefended(int amount) { critDamageDefended += amount ;}
	public void incBloodDamageInflicted(int amount) { bloodDamageInflicted += amount ;}
	public void incBloodDamageReceived(int amount) { bloodDamageReceived += amount ;}
	public void incBloodDamageDefended(int amount) { bloodDamageDefended += amount ;}
	public void incPoisonDamageInflicted(int amount) { poisonDamageInflicted += amount ;}
	public void incPoisonDamageReceived(int amount) { poisonDamageReceived += amount ;}
	public void incPoisonDamageDefended(int amount) { poisonDamageDefended += amount ;}
	

	public void updateOffensive(AtkResults atkResults)
	{		
		if (atkResults == null) { return ;}
		if (atkResults.getEffect().equals(AtkEffects.none)) { return ;}
		
		AtkTypes atkType = atkResults.getAtkType() ;
		AtkEffects effect = atkResults.getEffect() ;
		int damage = (int) atkResults.getDamage() ;

		if (atkType.equals(AtkTypes.defense))
		{
			incNumberDef() ;
			return ;
		}
		
		switch (effect)
		{
			case hit:
				incNumberHitsInflicted() ;
				if (atkType.equals(AtkTypes.magical))
				{
					incNumberMagAtk() ;
					if (0 <= damage)
					{
						incMagDamageInflicted(damage) ;
						highestMagDamageInflicted = Math.max(highestMagDamageInflicted, damage) ;
					}
				}
				if (atkType.equals(AtkTypes.physical))
				{
					incNumberPhyAtk() ;
					if (0 <= damage)
					{
						incPhyDamageInflicted(damage) ;
						highestPhyDamageInflicted = Math.max(highestPhyDamageInflicted, damage) ;
					}
				}
				return ;
				
			case crit: 
				incNumberCritInflicted() ;
				incCritDamageInflicted(damage) ;
				return ;
				
			default: return ;
		}
		
	}
	public void updateDefensive(AtkResults atkResults, double receiverPhyDef, double receiverMagDef)
	{
		if (atkResults == null) { return ;}
		if (atkResults.getEffect().equals(AtkEffects.none)) { return ;}

		AtkTypes atkType = atkResults.getAtkType() ;
		AtkEffects effect = atkResults.getEffect() ;
		int damage = (int) atkResults.getDamage() ;
		
		switch (effect)
		{
			case hit:
				switch (atkType)
				{
					case physical: incPhyDamageReceived(damage) ; incPhyDamageDefended((int) receiverPhyDef) ; return ;
					case magical: incMagDamageReceived(damage) ; incMagDamageDefended((int) receiverMagDef) ; return ;
					default: return ;
				}
			case crit: incCritDamageReceived(damage) ; return ;	
			case miss: incNumberDodges() ; return ;	
			case block: incNumberBlockInflicted() ; return ;
			default: return ;
		}
	}
	public void updateInflictedBlood(int damageInflicted)
	{
		incBloodDamageInflicted(damageInflicted) ;
	}
	public void updateInflictedPoison(int damageInflicted)
	{
		incPoisonDamageInflicted(damageInflicted) ;
	}
	public void updateReceivedBlood(int damageReceived, double damageDefended)
	{
		incBloodDamageReceived(damageReceived) ;
		incBloodDamageDefended((int) damageDefended) ;
	}
	public void updateReceivedPoison(int damageReceived, double damageDefended)
	{
		incPoisonDamageReceived(damageReceived) ;
		incPoisonDamageDefended((int) damageDefended) ;
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

		for (int i = 0 ; i <= statsText.length - 1 ; i += 1)
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
		
		return allStats ;
	}
	
	public void updateInflicedStatus(int[] appliedStatus)
	{
		if (appliedStatus.length <= 4) { return ;}
		if (0 < appliedStatus[0]) { incNumberStunInflicted() ;}
		if (0 < appliedStatus[1]) { incNumberBlockInflicted() ;}
		if (0 < appliedStatus[2]) { incNumberBloodInflicted() ;}
		if (0 < appliedStatus[3]) { incNumberPoisonInflicted() ;}
		if (0 < appliedStatus[4]) { incNumberSilenceInflicted() ;}
	}
}
