//package test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.awt.Dimension;
//import java.util.ArrayList;
//import java.util.HashSet;
//
//import org.junit.jupiter.api.Test;
//
//import attributes.BasicAttribute;
//import attributes.BasicBattleAttribute;
//import attributes.BattleAttributes;
//import attributes.BattleSpecialAttribute;
//import attributes.BattleSpecialAttributeWithDamage;
//import attributes.PersonalAttributes;
//import liveBeings.Creature;
//import liveBeings.CreatureType;
//import liveBeings.LiveBeing;
//import liveBeings.LiveBeingStatus;
//import liveBeings.Player;
//import main.AtkResults;
//import main.AtkTypes;
//import main.Battle;
//import utilities.AtkEffects;
//import utilities.Elements;
//
//class BattleTest
//{
//
//	LiveBeing knightLevel0 = new Player("", "", 0) ;
//	LiveBeing refCreature = new Creature(
//			new CreatureType(0, "", 0, new Dimension(0, 0), 60, 0, new Elements[] {Elements.water}, 0, 0, 0, 0, 0, null,
//					new PersonalAttributes(new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1)),
//					new BattleAttributes(new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0),
//							new BasicBattleAttribute(0.1, 0, 0), new BasicBattleAttribute(0, 0, 0),
//							new BattleSpecialAttribute(0, 0, 0, 0, 0), new BattleSpecialAttribute(0, 0, 0, 0, 0), new BattleSpecialAttributeWithDamage(0, 0, 0, 0, 0, 0, 0, 0, 0), new BattleSpecialAttributeWithDamage(0, 0, 0, 0, 0, 0, 0, 0, 0), new BattleSpecialAttribute(0, 0, 0, 0, 0),
//							new LiveBeingStatus()),
//					new ArrayList<>(),
//					new HashSet<>(),
//					0, null, new int[] {})) ;
//	
//	@Test
//	void calcBasicElemMult()
//	{
//		double elemMult = Battle.basicElemMult(Elements.water, Elements.air) ;
//		assertEquals(elemMult, 0.95) ;
//	}
//
//	@Test
//	void calcHit()
//	{
//		boolean hit = Battle.hit(99999, 0) ;
//		assertEquals(hit, true) ;
//	}
//	
//	@Test 
//	void calcBlock()
//	{
//		boolean block = Battle.block(1.0) ;
//		assertEquals(true, block);
//	}
//	
//	@Test 
//	void calcCriticalAtk()
//	{
//		boolean crit = Battle.criticalAtk(1.0, 0.0) ;
//		assertEquals(true, crit);
//	}
//
//	@Test
//	void calcElemMult()
//	{
//		double elemMult = Battle.calcElemMult(Elements.water, Elements.dark, Elements.earth, Elements.water, Elements.air) ;
//		assertEquals(elemMult, 0.77786) ;
//	}
//	
//	@Test
//	void calcPhysicalAtk()
//	{
//		for (int i = 0 ; i <= 50 ; i += 1)
//		{
//			AtkResults results = Battle.calcPhysicalAtk(knightLevel0, refCreature) ;
//			assertEquals(results.getAtkType(), AtkTypes.physical) ;
//			if (results.getEffect().equals(AtkEffects.hit))
//			{
//				double minDamage = 2 * (1 - Battle.getRandomAmp()) ;
//				double maxDamage = 2 * (1 + Battle.getRandomAmp()) ;
//				assertEquals(minDamage <= results.getDamage() & results.getDamage() <= maxDamage, true) ;
//			}
//			if (results.getEffect().equals(AtkEffects.crit))
//			{
//				double minDamage = 5 * (1 - Battle.getRandomAmp()) ;
//				double maxDamage = 5 * (1 + Battle.getRandomAmp()) ;
//				assertEquals(minDamage <= results.getDamage() & results.getDamage() <= maxDamage, true) ;
//			}
//		}
//	}
//	
//	@Test
//	void calcEffectHit()
//	{
//		AtkEffects actualResult = Battle.calcEffect(99999, 0, 0, 0, 0) ;
//		assertEquals(AtkEffects.hit, actualResult) ;
//	}
//	
//	@Test
//	void calcEffectMiss()
//	{
//		AtkEffects actualResult = Battle.calcEffect(0, 99999, 0, 0, 0) ;
//		assertEquals(AtkEffects.miss, actualResult) ;
//	}
//	
//	@Test
//	void calcEffectCrit()
//	{
//		AtkEffects actualResult = Battle.calcEffect(99999, 0, 1, 0, 0) ;
//		assertEquals(AtkEffects.crit, actualResult) ;
//	}
//	
//	@Test
//	void calcEffectBlock()
//	{
//		AtkEffects actualResult = Battle.calcEffect(99999, 0, 0, 0, 1) ;
//		assertEquals(AtkEffects.block, actualResult) ;
//	}
//	
//	@Test
//	void calcDamage()
//	{
//		Elements[] atkElems = new Elements[] {Elements.water, Elements.water, Elements.water} ;
//		Elements[] defElem = new Elements[] {Elements.fire};
//		int actualResult = Battle.calcDamage(AtkEffects.hit, 1.0, 0.0, atkElems, defElem, 1.0) ;
//		assertEquals(3.0, actualResult) ;
//		
//	}
//
//}
