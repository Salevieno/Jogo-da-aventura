package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import main.Battle;
import utilities.AtkEffects;
import utilities.Elements;

class BattleTest
{
	
	@Test
	void calcEffectHit()
	{
		AtkEffects actualResult = Battle.calcEffect(99999, 0, 0, 0, 0) ;
		assertEquals(AtkEffects.hit, actualResult) ;
	}
	
	@Test
	void calcEffectMiss()
	{
		AtkEffects actualResult = Battle.calcEffect(0, 99999, 0, 0, 0) ;
		assertEquals(AtkEffects.miss, actualResult) ;
	}
	
	@Test
	void calcEffectCrit()
	{
		AtkEffects actualResult = Battle.calcEffect(99999, 0, 1, 0, 0) ;
		assertEquals(AtkEffects.crit, actualResult) ;
	}
	
	@Test
	void calcEffectBlock()
	{
		AtkEffects actualResult = Battle.calcEffect(99999, 0, 0, 0, 1) ;
		assertEquals(AtkEffects.block, actualResult) ;
	}
	
	@Test
	void calcDamage()
	{
		Elements[] atkElems = new Elements[] {Elements.water, Elements.water, Elements.water} ;
		Elements[] defElem = new Elements[] {Elements.fire};
		int actualResult = Battle.calcDamage(AtkEffects.hit, 1.0, 0.0, atkElems, defElem, 1.0) ;
		assertEquals(3.0, actualResult) ;
		
	}

}
