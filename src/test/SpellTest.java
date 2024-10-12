package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import attributes.BattleAttributes;
import components.SpellTypes;
import items.Arrow;
import items.Fab;
import items.GeneralItem;
import items.Item;
import items.Potion;
import items.Recipe;
import liveBeings.Buff;
import liveBeings.Creature;
import liveBeings.CreatureType;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import liveBeings.Spell;
import main.AtkResults;
import main.Battle;
import main.Languages;
import utilities.Elements;
import windows.CraftWindow;

public class SpellTest
{

	static Player knightLevel0 ;
	static Player mageLevel0 ;
	static Player archerLevel0 ;
	static Player animalLevel0 ;
	static Player thiefLevel0 ;
	
	static Pet pet ;
	
	static Creature refCreature ;
	// TODO optional - test jackpot spell
	@BeforeAll
	static void initializeGame()
	{
		Buff.loadBuffs();
		Buff.loadDebuffs();
		Spell.load(Languages.portugues, Buff.allBuffs, Buff.allDebuffs);
		Item.load();
		Recipe.load(Item.allItems) ;
		CreatureType.load(Languages.portugues, 2);

		knightLevel0 = new Player("Player", "", 0) ;
		mageLevel0 = new Player("Player", "", 1) ;
		archerLevel0 = new Player("Player", "", 2) ;
		animalLevel0 = new Player("Player", "", 3) ;
		thiefLevel0 = new Player("Player", "", 4) ;
		List.of(knightLevel0, mageLevel0, archerLevel0, animalLevel0, thiefLevel0).forEach(Player::InitializeSpells) ;
		
		pet = new Pet(1) ;
		
		refCreature = new Creature(CreatureType.all.get(0)) ;
		
		Battle.removeRandomness();
	}
	
	@BeforeEach
	void resetAttributes()
	{
		List.of(knightLevel0, mageLevel0, archerLevel0, animalLevel0, thiefLevel0, pet, refCreature).forEach(being ->
		{
			being.getPA().getLife().setToMaximum() ;
			being.getPA().getMp().setToMaximum() ;
		});
		knightLevel0.setPA(Player.InitializePersonalAttributes(0)) ;
		mageLevel0.setPA(Player.InitializePersonalAttributes(1)) ;
		archerLevel0.setPA(Player.InitializePersonalAttributes(2)) ;
		animalLevel0.setPA(Player.InitializePersonalAttributes(3)) ;
		thiefLevel0.setPA(Player.InitializePersonalAttributes(4)) ;
		knightLevel0.setBA(new BattleAttributes(Player.InitialAtts.get(1), 1, Player.InitialAtts.get(1)[41])) ;
		mageLevel0.setBA(new BattleAttributes(Player.InitialAtts.get(2), 1, Player.InitialAtts.get(2)[41])) ;
		archerLevel0.setBA(new BattleAttributes(Player.InitialAtts.get(3), 1, Player.InitialAtts.get(3)[41])) ;
		animalLevel0.setBA(new BattleAttributes(Player.InitialAtts.get(4), 1, Player.InitialAtts.get(4)[41])) ;
		thiefLevel0.setBA(new BattleAttributes(Player.InitialAtts.get(5), 1, Player.InitialAtts.get(5)[41])) ;
		pet.setPA(Pet.InitializePersonalAttributes(pet.getJob())) ;
		pet.setBA(new BattleAttributes(Pet.InitialAtts.get(pet.getJob()), 1, Player.InitialAtts.get(pet.getJob())[36])) ;
		refCreature.setPA(CreatureType.all.get(0).getPA());
		refCreature.setBA(CreatureType.all.get(0).getBA());
		knightLevel0.engageInFight(refCreature) ;
		mageLevel0.engageInFight(refCreature) ;
		archerLevel0.engageInFight(refCreature) ;
		animalLevel0.engageInFight(refCreature) ;
		thiefLevel0.engageInFight(refCreature) ;
		
		List.of(knightLevel0, mageLevel0, archerLevel0, animalLevel0, thiefLevel0).forEach(player -> {
	    	for (Spell spell : player.getSpells())
	    	{
	    		if (spell.getLevel() == spell.getMaxLevel()) { spell.incLevel(-spell.getMaxLevel()) ;}
	    		if (spell.getType().equals(SpellTypes.passive)) { continue ;}
	    		spell.incLevel(5) ;
	    		
	    		// make spell ready
//	    		for (int i = 0 ; i <= spell.getCooldownCounter().getDuration() - 1; i += 1)
//	    		{
//	    			spell.getCooldownCounter().inc() ;
//	    		}
	    	}
		}) ;
	}
	

	
	@Test
	void spellLevelIncrease()
	{
		Player player = knightLevel0 ;
		Spell pancada = player.getSpells().get(0) ;
		
		assertEquals(1, pancada.getLevel()) ;
		assertEquals(20, pancada.getMpCost()) ;
		
		pancada.incLevel(4); ;
		
		assertEquals(5, pancada.getLevel()) ;
		assertEquals(39, pancada.getMpCost()) ;
	}
	
	@Test
	void knightOffensiveSpells()
	{
		Player player = knightLevel0 ;
		Spell pancada = player.getSpells().get(0) ;
		AtkResults atkResults = player.useSpell(pancada, refCreature) ;
		int expectedDamage = switch(atkResults.getEffect())
		{
			case hit -> 8 ;
			case crit -> 10 ;
			default -> 0 ;
		};
		
		assertEquals(30, player.getPA().getMp().getTotalValue()) ;		
		assertEquals(expectedDamage, atkResults.getDamage()) ;
	}
	
	
	@Test
	void mageOffensiveSpells()
	{
		Player player = mageLevel0 ;
		Spell chama = player.getSpells().get(0) ;
		AtkResults atkResults = player.useSpell(chama, refCreature) ;
		int expectedDamage = switch(atkResults.getEffect())
		{
			case hit -> 13 ;
			case crit -> 15 ;
			default -> 0 ;
		};
		
		assertEquals(80, player.getPA().getMp().getTotalValue()) ;		
		assertEquals(expectedDamage, atkResults.getDamage()) ;
	}
	
	
	@Test
	void archerOffensiveSpells()
	{
		Player player = archerLevel0 ;
		Spell flechadaForte = player.getSpells().get(0) ;
		AtkResults atkResults = player.useSpell(flechadaForte, refCreature) ;
		int expectedDamage = switch(atkResults.getEffect())
		{
			case hit -> 11 ;
			case crit -> 13 ;
			default -> 0 ;
		};
		
		assertEquals(65, player.getPA().getMp().getTotalValue()) ;		
		assertEquals(expectedDamage, atkResults.getDamage()) ;
	}
	
	
	@Test
	void animalOffensiveSpells()
	{
		Player player = animalLevel0 ;
		Spell mordidaProfunda = player.getSpells().get(0) ;
		AtkResults atkResults = player.useSpell(mordidaProfunda, refCreature) ;
		int expectedDamage = switch(atkResults.getEffect())
		{
			case hit -> 7 ;
			case crit -> 9 ;
			default -> 0 ;
		};
		
		assertEquals(50, player.getPA().getMp().getTotalValue()) ;		
		assertEquals(expectedDamage, atkResults.getDamage()) ;
	}
	
	
	@Test
	void thiefOffensiveSpells()
	{
		Player player = thiefLevel0 ;
		Spell ataqueFurtivo = player.getSpells().get(0) ;
		AtkResults atkResults = player.useSpell(ataqueFurtivo, refCreature) ;
		int expectedDamage = switch(atkResults.getEffect())
		{
			case hit -> 1 ;
			case crit -> 3 ;
			default -> 0 ;
		};
		
		assertEquals(30, player.getPA().getMp().getTotalValue()) ;		
		assertEquals(expectedDamage, atkResults.getDamage()) ;
	}
	
	
	
	@Test
	void passive1vitality()
	{
		Player player = knightLevel0 ;
		Spell eternalLife = player.getSpells().get(1) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(eternalLife);
		}
		assertEquals(181, player.getPA().getLife().getTotalValue()) ;
	}
	
	
	@Test
	void passive2strength()
	{
		Player player = knightLevel0 ;
		Spell forca = player.getSpells().get(2) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(forca);
		}
		assertEquals(15, player.getBA().getPhyAtk().getBaseValue()) ;
	}
	
	
	@Test
	void passive3resistance()
	{
		Player player = knightLevel0 ;
		Spell resistencia = player.getSpells().get(3) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(resistencia);
		}
		assertEquals(15, player.getBA().getPhyDef().getBaseValue()) ;
	}
	
	
	@Test
	void passive7thickBlood()
	{
		Player player = knightLevel0 ;
		Spell sangueGrosso = player.getSpells().get(7) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(sangueGrosso);
		}
		assertEquals(2.21025, player.getBA().getBlood().getBasicDef(), 0.0001) ;
	}
	
	
	@Test
	void passive10neutralResistance()
	{
		Player player = knightLevel0 ;
		Spell resistenciaNeutra = player.getSpells().get(10) ;
		resistenciaNeutra.incLevel(5) ;
		player.applyPassiveSpell(resistenciaNeutra);
		assertEquals(0.85, player.getBA().getElemResistanceMult().get(Elements.neutral), 0.0001) ;
	}
	
	
	@Test
	void passive11physicalCondition()
	{
		Player player = knightLevel0 ;
		Spell condicaoFisica = player.getSpells().get(11) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(condicaoFisica);
		}
		assertEquals(15, player.getBA().getPhyAtk().getBaseValue(), 0.0001) ;
		assertEquals(15, player.getBA().getDex().getBaseValue(), 0.0001) ;
		assertEquals(7, player.getBA().getAgi().getBaseValue(), 0.0001) ;
	}

	
//	@Test
//	void passive26improvedBlock()
//	{
//		// Teste de shielder, ainda precisa implementar.
//		Player player = mageLevel0 ;
//		Spell bloqueioMelhorado = player.getSpells().get(2) ;
//		for (int i = 0 ; i <= 5 - 1; i += 1)
//		{
//			player.applyPassiveSpell(bloqueioMelhorado);
//		}
//		assertEquals(15, player.getBA().getMagAtk().getBaseValue(), 0.0001) ;
//	}
	
	
	@Test
	void passive36Meditation()
	{
		Player player = mageLevel0 ;
		Spell meditation = player.getSpells().get(2) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(meditation);
		}
		assertEquals(15, player.getBA().getMagAtk().getBaseValue(), 0.0001) ;
		assertEquals(15, player.getBA().getMagDef().getBaseValue(), 0.0001) ;
	}
	
	@Test
	void passive70archery()
	{
		Player player = archerLevel0 ;
		Spell archery = player.getSpells().get(1) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(archery);
		}
		assertEquals(23, player.getBA().getDex().getBaseValue(), 0.0001) ;
		assertEquals(5, player.getBA().getAgi().getBaseValue(), 0.0001) ;
	}
	
	
	@Test
	void passive73arrowExpertise()
	{
		Player player = archerLevel0 ;
		Spell archery = player.getSpells().get(4) ;
		player.getBag().add(Arrow.getAll()[1], 1);
		player.getBag().add(Arrow.getAll()[4], 1);
		player.getBag().add(Arrow.getAll()[5], 1);
		player.getBag().add(Arrow.getAll()[6], 1);
		player.getBag().add(Arrow.getAll()[15], 1);
		player.useItem(Arrow.getAll()[1]);
		assertEquals(null, player.getEquippedArrow()) ;
		
		archery.incLevel(1) ;
		player.useItem(Arrow.getAll()[1]);
		assertEquals(Arrow.getAll()[1], player.getEquippedArrow()) ;

		player.useItem(Arrow.getAll()[1]);
		player.useItem(Arrow.getAll()[4]);
		assertEquals(null, player.getEquippedArrow()) ;
		
		archery.incLevel(1) ;
		player.useItem(Arrow.getAll()[4]);
		assertEquals(Arrow.getAll()[4], player.getEquippedArrow()) ;

		player.useItem(Arrow.getAll()[4]);
		player.useItem(Arrow.getAll()[5]);
		assertEquals(null, player.getEquippedArrow()) ;
		
		archery.incLevel(1) ;
		player.useItem(Arrow.getAll()[5]);
		assertEquals(Arrow.getAll()[5], player.getEquippedArrow()) ;

		player.useItem(Arrow.getAll()[5]);
		player.useItem(Arrow.getAll()[6]);
		assertEquals(null, player.getEquippedArrow()) ;
		
		archery.incLevel(1) ;
		player.useItem(Arrow.getAll()[6]);
		assertEquals(Arrow.getAll()[6], player.getEquippedArrow()) ;

		player.useItem(Arrow.getAll()[6]);
		player.useItem(Arrow.getAll()[15]);
		assertEquals(null, player.getEquippedArrow()) ;
	}
	
	
	@Test
	void passive76intensity()
	{
		Player player = archerLevel0 ;
		Spell intensidade = player.getSpells().get(7) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(intensidade);
		}
		assertEquals(8, player.getBA().getPhyAtk().getBaseValue(), 0.0001) ;
		assertEquals(8, player.getBA().getMagAtk().getBaseValue(), 0.0001) ;
	}
	
	
	@Test
	void passive77elementalArrows()
	{
		Player player = archerLevel0 ;
		Spell flechasElementais = player.getSpells().get(7) ;
		player.getBag().add(Fab.getAll()[0], 10);
		player.getBag().add(GeneralItem.getAll()[114], 5);
		player.getBag().add(GeneralItem.getAll()[115], 5);
		player.getBag().add(GeneralItem.getAll()[116], 5);
		player.getBag().add(GeneralItem.getAll()[117], 5);
		player.getBag().add(GeneralItem.getAll()[118], 5);
		player.getBag().add(GeneralItem.getAll()[119], 5);
		player.getBag().add(GeneralItem.getAll()[120], 5);
		player.getBag().add(GeneralItem.getAll()[121], 5);
		player.getBag().add(GeneralItem.getAll()[122], 5);
		
    	List<Recipe> recipes = Recipe.all ;
    	CraftWindow craftWindow = new CraftWindow(recipes) ;
		player.switchOpenClose(craftWindow) ;
		
    	for (int i = 0 ; i <= 73 - 1; i += 1)
    	{
        	craftWindow.navigate(PlayerActions.moveRight.getKey());
    	}
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
		assertTrue(!player.getBag().contains(Arrow.getAll()[6])) ;

		flechasElementais.incLevel(1) ;
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(player.getBag().hasEnough(Arrow.getAll()[6], 20)) ;
		
    	for (int i = 0 ; i <= 2 - 1; i += 1)
    	{
        	craftWindow.navigate(PlayerActions.moveRight.getKey());
    	}
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(!player.getBag().contains(Arrow.getAll()[8])) ;

		flechasElementais.incLevel(1) ;
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(player.getBag().hasEnough(Arrow.getAll()[8], 20)) ;
		
    	for (int i = 0 ; i <= 2 - 1; i += 1)
    	{
        	craftWindow.navigate(PlayerActions.moveRight.getKey());
    	}
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(!player.getBag().contains(Arrow.getAll()[10])) ;

		flechasElementais.incLevel(1) ;
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(player.getBag().hasEnough(Arrow.getAll()[10], 20)) ;
		
    	for (int i = 0 ; i <= 2 - 1; i += 1)
    	{
        	craftWindow.navigate(PlayerActions.moveRight.getKey());
    	}
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(!player.getBag().contains(Arrow.getAll()[12])) ;

		flechasElementais.incLevel(1) ;
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(player.getBag().hasEnough(Arrow.getAll()[12], 20)) ;
		
    	for (int i = 0 ; i <= 2 - 1; i += 1)
    	{
        	craftWindow.navigate(PlayerActions.moveRight.getKey());
    	}
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(!player.getBag().contains(Arrow.getAll()[14])) ;

		flechasElementais.incLevel(1) ;
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(player.getBag().hasEnough(Arrow.getAll()[14], 20)) ;
	}
	
	
	@Test
	void passive79huntingTactics()
	{
		Player player = archerLevel0 ;
		Spell taticasDeCaca = player.getSpells().get(10) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(taticasDeCaca);
		}
		assertEquals(0.25, player.getBA().getBlood().getBasicDefChance(), 0.0001) ;
		assertEquals(0.25, player.getBA().getPoison().getBasicDefChance(), 0.0001) ;
	}
	
	
	@Test
	void passive105fourPaws()
	{
		Player player = animalLevel0 ;
		Spell quatroPatas = player.getSpells().get(1) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(quatroPatas);
		}
		assertEquals(22, player.getBA().getDex().getBaseValue()) ;
		assertEquals(11, player.getBA().getAgi().getBaseValue()) ;
	}
	
	
	
	@Test
	void passive107criticalPoints()
	{
		Player player = animalLevel0 ;
		Spell pontosCriticos = player.getSpells().get(3) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(pontosCriticos);
		}
		assertEquals(0.27, player.getBA().getCritAtk().getBaseValue(), 0.0001) ;
	}
	
	@Test
	void passive111naturalElements()
	{
		Player player = animalLevel0 ;
		Spell elementosNaturais = player.getSpells().get(7) ;
		player.getBag().add(Potion.getAll()[0], 2);
		player.takeDamage(60) ;
		player.useItem(Potion.getAll()[0]) ;
		assertEquals(17, player.getLife().getCurrentValue()) ;
		
		elementosNaturais.incLevel(5) ;
		player.useItem(Potion.getAll()[0]) ;
		assertEquals(26, player.getLife().getCurrentValue()) ;
	}
	
//	@Test
//	void passive114naturalHelp()
//	{
//		Player player = animalLevel0 ;
//		Spell ajudinhaNatural = player.getSpells().get(10) ;
//		player.getBag().add(Alchemy.getAll()[0], 1);
//		player.getBag().add(Alchemy.getAll()[1], 1);
//		player.getBag().add(Alchemy.getAll()[2], 1);
//		ajudinhaNatural.incLevel(5) ;
//		Game.setPet(pet);
//		assertEquals(50, pet.getLife().getCurrentValue()) ;
//		assertEquals(100, pet.getMp().getCurrentValue()) ;
//		
//		pet.takeDamage(40);
//		pet.getMp().decTotalValue(90);
//
//		player.useItem(Alchemy.getAll()[0]) ;
//		assertEquals(10, pet.getLife().getBonus()) ;
//		assertEquals(20, pet.getMp().getBonus()) ;
//		
//		player.useItem(Alchemy.getAll()[1]) ;
//		assertEquals(0.4, pet.getPhyDef().getBonus(), 0.0001) ;
//		assertEquals(1, pet.getMagDef().getBonus(), 0.0001) ;
//		assertEquals(0.2, pet.getAgi().getBonus(), 0.0001) ;
//		
//		player.useItem(Alchemy.getAll()[2]) ;
//		assertEquals(0.4, pet.getPhyAtk().getBonus(), 0.0001) ;
//		assertEquals(1, pet.getMagAtk().getBonus(), 0.0001) ;
//		assertEquals(2.4, pet.getDex().getBonus(), 0.0001) ;
//	}
	
	
	
	@Test
	void passive113sturdy()
	{
		Player player = animalLevel0 ;
		Spell robusto = player.getSpells().get(9) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(robusto);
		}
		assertEquals(80, player.getPA().getLife().getTotalValue()) ;
		assertEquals(14, player.getBA().getPhyAtk().getBaseValue(), 0.0001) ;
		assertEquals(14, player.getBA().getPhyDef().getBaseValue(), 0.0001) ;
	}
	
	
	@Test
	void passive117bestFriend()
	{
		Player player = animalLevel0 ;
		Spell melhorAmigo = player.getSpells().get(13) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			pet.applyPassiveSpell(melhorAmigo);
		}
		assertEquals(100, pet.getPA().getLife().getTotalValue()) ;
		assertEquals(150, pet.getPA().getMp().getTotalValue()) ;
		assertEquals(12, pet.getBA().getPhyAtk().getBaseValue(), 0.0001) ;
		assertEquals(15, pet.getBA().getMagAtk().getBaseValue(), 0.0001) ;
		assertEquals(17, pet.getBA().getDex().getBaseValue(), 0.0001) ;
		assertEquals(6, pet.getBA().getAgi().getBaseValue(), 0.0001) ;
	}
	
	
	@Test
	void passive139speed()
	{
		Player player = thiefLevel0 ;
		Spell rapidez = player.getSpells().get(1) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(rapidez);
		}
		assertEquals(13, player.getBA().getAgi().getBaseValue(), 0.0001) ;
	}

	
	@Test
	void passive142combatPractice()
	{
		Player player = thiefLevel0 ;
		Spell praticaDeCombate = player.getSpells().get(4) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(praticaDeCombate);
		}
		assertEquals(18, player.getBA().getPhyAtk().getBaseValue(), 0.0001) ;
		assertEquals(20, player.getBA().getDex().getBaseValue(), 0.0001) ;
	}

	
	@Test
	void passive144Jackpot()
	{
		Player player = thiefLevel0 ;
		Spell bolada = player.getSpells().get(6) ;
		
		assertEquals(0.0, player.getDigBonus(), 0.0001) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(bolada);
		}
		assertEquals(0.2, player.getDigBonus(), 0.0001) ;
	}

	
	

	@Test
	void passive146dailyWeapons()
	{
		Player player = thiefLevel0 ;
		Spell armasCotidianas = player.getSpells().get(8) ;
		List<GeneralItem> items = List.of(GeneralItem.getAll()[2], GeneralItem.getAll()[64], GeneralItem.getAll()[70],
				GeneralItem.getAll()[114], GeneralItem.getAll()[123], GeneralItem.getAll()[132]);
		items.forEach(item -> player.getBag().add(item, 1));

		player.useItem(items.get(0)) ;
		player.useItem(items.get(1)) ;
		assertTrue(!player.getBag().contains(items.get(0))) ;
		assertTrue(player.getBag().contains(items.get(1))) ;
		
		for (int i = 0 ; i <= 4 - 1 ; i += 1)
		{
			armasCotidianas.incLevel(1) ;
			player.useItem(items.get(i + 1)) ;
			player.useItem(items.get(i + 2)) ;
			assertTrue(!player.getBag().contains(items.get(i + 1))) ;
			assertTrue(player.getBag().contains(items.get(i + 2))) ;
		}
		
		armasCotidianas.incLevel(1) ;
		player.useItem(items.get(5)) ;
		assertTrue(!player.getBag().contains(items.get(5))) ;
		
	}
	
	@Test
	void passive147poisonPot()
	{
		Player player = thiefLevel0 ;
		Spell dailyWeapons = player.getSpells().get(8) ;
		Spell pocaoVenenosa = player.getSpells().get(9) ;
		dailyWeapons.incLevel(5) ;
		player.getBag().add(Item.allItems.get(1364), 2);
		player.getBag().add(Item.allItems.get(1376), 2);
		player.getBag().add(Item.allItems.get(1700), 2);
		
    	List<Recipe> recipes = Recipe.all ;
    	CraftWindow craftWindow = new CraftWindow(recipes) ;
		player.switchOpenClose(craftWindow) ;
		
    	for (int i = 0 ; i <= 598 - 1; i += 1)
    	{
        	craftWindow.navigate(PlayerActions.moveRight.getKey());
    	}
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
		assertTrue(!player.getBag().contains(Item.allItems.get(1378))) ;

		pocaoVenenosa.incLevel(1) ;
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	craftWindow.act(player.getBag(), null, "Enter", player) ;
    	assertTrue(player.getBag().hasEnough(Item.allItems.get(1378), 2)) ;

    	player.useItem(Item.allItems.get(1378));
    	assertEquals(30, refCreature.getLife().getCurrentValue());
    	
		pocaoVenenosa.incLevel(4) ;
    	player.useItem(Item.allItems.get(1378));
    	assertEquals(2, refCreature.getLife().getCurrentValue());
	}
	
	@Test
	void passive151hardToKill()
	{
		Player player = thiefLevel0 ;
		Spell duroDeMatar = player.getSpells().get(13) ;
		for (int i = 0 ; i <= 5 - 1; i += 1)
		{
			player.applyPassiveSpell(duroDeMatar);
		}
		assertEquals(5, player.getBA().getPoison().getBasicDef(), 0.0001) ;
	}

	
	
	@Test
	void support4eternalLife()
	{
		Player player = knightLevel0 ;
		Spell eternalLife = player.getSpells().get(4) ;
		player.useSpell(eternalLife, player);
		assertEquals(200, player.getPA().getLife().getTotalValue()) ;
	}
	
	
	@Test
	void support5offensivePosture()
	{
		Player player = knightLevel0 ;
		Spell posturaOfensiva = player.getSpells().get(5) ;
		player.useSpell(posturaOfensiva, player);
		assertEquals(2.5, player.getBA().getPhyAtk().getBonus(), 0.0001) ;
		assertEquals(-1.25, player.getBA().getPhyDef().getBonus(), 0.0001) ;
	}
	
	
	@Test
	void support9block()
	{
		Player player = knightLevel0 ;
		Spell bloqueio = player.getSpells().get(9) ;
		player.useSpell(bloqueio, player);
		assertEquals(1, player.getBA().getPhyDef().getBonus(), 0.0001) ;
		assertEquals(0.4, player.getBA().getMagDef().getBonus(), 0.0001) ;
		assertEquals(0.8, player.getBA().getBlock().getBasicAtkChanceBonus(), 0.0001) ;
	}
	
	
	@Test
	void support112wavingTail()
	{
		Player player = animalLevel0 ;
		Spell abanandoORabinho = player.getSpells().get(8) ;
		refCreature.takeDamage(25) ;
		player.useSpell(abanandoORabinho, refCreature);
		assertEquals(30, refCreature.getPA().getLife().getCurrentValue()) ;
		assertEquals(-6, refCreature.getBA().getDex().getBonus(), 0.0001) ;
		assertEquals(-1, refCreature.getBA().getAgi().getBonus(), 0.0001) ;
	}
	
	
//	@Test
//	void support114naturalHelp()
//	{
//		Player player = animalLevel0 ;
//		Spell ajudinhaNatural = player.getSpells().get(10) ;
//		player.useSpell(ajudinhaNatural, pet);
//		assertEquals(30, pet.getPA().getLife().getCurrentValue()) ;
//		assertEquals(-6, pet.getBA().getDex().getBonus(), 0.0001) ;
//		assertEquals(-1, pet.getBA().getAgi().getBonus(), 0.0001) ;
//	}
	
	
	@Test
	void support141steal()
	{
		Player player = thiefLevel0 ;
		Spell roubo = player.getSpells().get(3) ;
		
		assertTrue(!player.getBag().contains(Item.allItems.get(0))) ;
		player.useSpell(roubo, refCreature);
		
		boolean containsAny = false ;
		for (Item item : refCreature.getBag())
		{
			if (player.getBag().contains(item))
			{
				containsAny = true ;
			}
		}
		assertTrue(containsAny) ;
	}
	
	
	@Test
	void support44heal()
	{
		mageLevel0.takeDamage(40) ;
		mageLevel0.useSpell(mageLevel0.getSpells().get(10), mageLevel0) ;
		assertEquals(20, mageLevel0.getPA().getLife().getTotalValue()) ;
	}
	
	@Test
	void auto42Restoration()
	{
		Player player = mageLevel0 ;
		player.setCurrentAction("0") ;
		player.useSpell(player.getSpells().get(0), refCreature) ;
		assertEquals(68, player.getMp().getCurrentValue()) ;
	}
	
	@Test	
	void auto82bouncyArrow()
	{
		Player player = archerLevel0 ;
		player.getBag().add(Arrow.getAll()[2], 10) ;
		player.useItem(Arrow.getAll()[2]) ;
		player.useAutoSpell(true, player.getSpells().get(13));
		assertTrue(player.getBag().hasEnough(Arrow.getAll()[2], 10)) ;
	}
	
	@Test
	void auto116survivorInstinct()
	{
		Player player = animalLevel0 ;
		player.takeDamage((int) (0.8 * player.getLife().getCurrentValue())) ;
		player.useAutoSpell(true, player.getSpells().get(12)) ;
		assertEquals(2, player.getBA().getPhyAtk().getBonus(), 0.0001) ;
		assertEquals(1.5, player.getBA().getMagAtk().getBonus(), 0.0001) ;
		assertEquals(2, player.getBA().getPhyDef().getBonus(), 0.0001) ;
		assertEquals(1.5, player.getBA().getMagDef().getBonus(), 0.0001) ;
		assertEquals(6, player.getBA().getDex().getBonus(), 0.0001) ;
		assertEquals(3, player.getBA().getAgi().getBonus(), 0.0001) ;
	}

	
	
//	@Test
//	void auto108doubleCollect()
//	{
//		Player player = animalLevel0 ;
//		Collectible herb = new Collectible(60, 1, null, 0) ;
//		
//		assertTrue(!player.getBag().contains(herb.getItem())) ;
//		player.collect(herb);
//		assertTrue(player.getBag().hasEnough(herb.getItem(), 2)) ;
//	}
	
	void auto149surpriseAttack()
	{
		Player player = thiefLevel0 ;
		player.useAutoSpell(true, player.getSpells().get(11)) ;
		assertEquals(44, refCreature.getPA().getLife().getCurrentValue()) ;
	}
}
