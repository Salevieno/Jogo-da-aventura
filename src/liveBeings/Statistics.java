package liveBeings;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import main.AtkResults;
import main.AtkTypes;
import utilities.AttackEffects;

public class Statistics
{
	private int numberPhyAtk ;
	private int numberMagAtk ;
	private int numberDef ;
	private int numberHitsInflicted ;
	private int numberHitsReceived ;
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
	private int bloodDamageInflicted ;
	private int bloodDamageDefended ;
	private int poisonDamageInflicted ;
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
	public int getNumberHitsReceived()
	{
		return numberHitsReceived;
	}
	public int getNumberDodges()
	{
		return numberDodges;
	}
	public int getNumberCritInflicted()
	{
		return critDamageInflicted;
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
		return numberCritInflicted;
	}
	public int getNumberPoisonInflicted()
	{
		return bloodDamageDefended;
	}
	public int getNumberSilenceInflicted()
	{
		return poisonDamageDefended;
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
		return numberBloodInflicted;
	}
	public int getBloodDamageInflicted()
	{
		return bloodDamageInflicted;
	}
	public int getBloodDamageDefended()
	{
		return numberPoisonInflicted;
	}
	public int getPoisonDamageInflicted()
	{
		return poisonDamageInflicted;
	}
	public int getPoisonDamageDefended()
	{
		return numberSilenceInflicted;
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
	public void incNumberHitsReceived() { numberHitsReceived += 1 ;}
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
	

	public void update(AtkResults atkResult)
	{		
		if (atkResult == null) { return ;}
		
		AtkTypes atkType = atkResult.getAtkType() ;
		AttackEffects effect = (AttackEffects) atkResult.getEffect() ;
		int damage = (int) atkResult.getDamage() ;
		
		
		if (0 <= damage)
		{	
			if (atkType.equals(AtkTypes.magical))
			{
				incNumberMagAtk() ;
				incMagDamageInflicted(damage) ;
				highestMagDamageInflicted = Math.max(highestMagDamageInflicted, damage) ;
			}
			else
			{
				incNumberPhyAtk() ;
				incPhyDamageInflicted(damage) ;
				highestPhyDamageInflicted = Math.max(highestPhyDamageInflicted, damage) ;
			}			
		}
		
		if (atkType.equals(AtkTypes.defense))
		{
			incNumberDef() ;
		}
		
		if (effect == null) { return ;}
		
		if (effect.equals(AttackEffects.hit))							// player performed a successful hit
		{
			incNumberHitsInflicted() ;
			// for the status, dividing the duration of the status by the duration applied to get the number of times the status was applied
//			if (0 < BA.getStun().getDuration())
//			{
//				statistics[14] += creature.getBA().getSpecialStatus()[0] / BA.getStun().getDuration() ;	// total number of stun inflicted by the player
//			}
//			if (0 < BA.getBlock().getDuration())
//			{
//				statistics[15] += creature.getBA().getSpecialStatus()[1] / BA.getBlock().getDuration() ;	// total number of block performed by the player
//			}
//			if (0 < BA.getBlood().getDuration())
//			{
//				statistics[16] += creature.getBA().getSpecialStatus()[2] / BA.getBlood().getDuration() ;	// total number of blood inflicted by the player
//				if (0 < creature.getBA().getSpecialStatus()[2])
//				{
//					statistics[17] += 1 ;	// total number of blood inflicted by the player
//				}
//			}
//			if (0 < BA.getPoison().getDuration())
//			{
//				statistics[19] += creature.getBA().getSpecialStatus()[3] / BA.getPoison().getDuration() ;	// total number of poison inflicted by the player
//				if (0 < creature.getBA().getSpecialStatus()[3])
//				{
//					statistics[20] += 1 ;	// total number of blood inflicted by the player
//				}
//			}
//			if (0 < BA.getSilence().getDuration())
//			{
//				statistics[22] += creature.getBA().getSpecialStatus()[4] / BA.getSilence().getDuration() ;	// total number of silence inflicted by the player
//			}
		}
//		if (0 < BloodDamage)
//		{
//			statistics[18] += BA.getBlood().TotalDef() ;
//		}
//		if (0 < PoisonDamage)
//		{
//			statistics[21] += BA.getPoison().TotalDef() ;
//		}
		if (effect.equals(AttackEffects.crit))
		{
			incNumberCritInflicted() ;
			incCritDamageInflicted(damage) ;
		}
	}
	public void updatedefensiveStats(int damage, AttackEffects effect, boolean creaturePhyAtk, Creature creature)
	{
		if (effect.equals(AttackEffects.hit))
		{			
			incNumberHitsReceived() ;
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

	public Map<String, Double> allStatistics()
	{
		Map<String, Double> allStats = new HashMap<>() ;
		Field[] fields = Statistics.class.getDeclaredFields() ;
		for (Field field : fields)
		{
			try
			{
				if (field.getType().equals(int.class))
				{
					allStats.put(field.getName(), (Double) (double) (int) (Integer) field.get(this)) ;
				}
				else
				{
					allStats.put(field.getName(), (Double) field.get(this)) ;
				}
			}
			catch (IllegalArgumentException e) { e.printStackTrace() ;}
			catch (IllegalAccessException e) { e.printStackTrace() ;}
		}
		
		return allStats ;
	}
}
