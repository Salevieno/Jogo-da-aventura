package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import main.Battle;
import utilities.AttackEffects;
import utilities.Elements;

class BattleTest
{
	
	@Test
	void calcEffectHit()
	{
		AttackEffects actualResult = Battle.calcEffect(99999, 0, 0, 0, 0) ;
		assertEquals(AttackEffects.hit, actualResult) ;
	}
	
	@Test
	void calcEffectMiss()
	{
		AttackEffects actualResult = Battle.calcEffect(0, 99999, 0, 0, 0) ;
		assertEquals(AttackEffects.miss, actualResult) ;
	}
	
	@Test
	void calcEffectCrit()
	{
		AttackEffects actualResult = Battle.calcEffect(99999, 0, 1, 0, 0) ;
		assertEquals(AttackEffects.crit, actualResult) ;
	}
	
	@Test
	void calcEffectBlock()
	{
		AttackEffects actualResult = Battle.calcEffect(99999, 0, 0, 0, 1) ;
		assertEquals(AttackEffects.block, actualResult) ;
	}
	
	@Test
	void calcDamage()
	{
		int actualResult = Battle.calcDamage(AttackEffects.hit, 1.0, 0.0, new Elements[] {Elements.water}, new Elements[] {Elements.dark}, 1.0) ;
		assertEquals(1.0, actualResult) ;
	}

}
