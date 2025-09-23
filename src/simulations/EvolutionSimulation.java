package simulations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import UI.ButtonFunction;
import UI.GameButton;
import attributes.AttributeIncrease;
import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import battle.AtkEffects;
import battle.AtkResults;
import battle.AtkTypes;
import battle.Battle;
import graphics.Align;
import graphics.Scale;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import liveBeings.Creature;
import liveBeings.CreatureType;
import liveBeings.Genetics;
import liveBeings.LiveBeing;
import liveBeings.Pet;
import liveBeings.Player;
import main.Directions;
import main.Game;
import main.GamePanel;
import main.GameTimer;
import utilities.Util;

import windows.PlayerAttributesWindow;

public abstract class EvolutionSimulation
{
	private static final Font font ;
	
	private static Player player ;
	private static Pet pet ;
	private static int playerOpponentID = 0 ;
	private static Creature playerOpponent ;
	
	private static int playerPreviousExp ;	
	private static int numberFights = 0 ;
	private static int numberPlayerWins = 0 ;
	private static int numberCreatureWins = 0 ;
	
	private static int numberFightsRepetition = 0 ;
	
	private static List<GameButton> buttons ;

	private static final String pathSimulation = "Simulation/" ;
	private static final Image screenImage = Game.loadImage(pathSimulation + "SimulationScreen.png") ;
	private static final Image buttonImage = Game.loadImage(pathSimulation + "ButtonGeneral.png") ;
	private static final Image buttonSelectedImage = Game.loadImage(pathSimulation + "ButtonGeneralSelected.png") ;
	private static final Image fightingImage = Game.loadImage(pathSimulation + "fightingIcon.png") ;
	
	
	private static int BattleResultsPlayerLife = 0 ;
	private static int BattleResultsCreatureLife = 0 ;
	private static long battleClock ;
	
	private static Map<String, Double> stats = new LinkedHashMap<>() ;
	private static List<Double> battletimes = new ArrayList<>() ;
	
	private static int numberRandomGeneRounds = 6 ;
	private static int numberRoundsToEvolve = 4 ;
	private static int currentFitness = 0 ;
	private static int lowestFitness = 2000 ;
	private static int avrFitness = 0 ;
	private static int highestFitness = 0 ;
	private static List<Integer> listBestFitness = new ArrayList<>() ;
	private static List<Genetics> listBestGenes = new ArrayList<>() ;
	private static Genetics newGenes = new Genetics() ;
	private static boolean evolutionIsOn = false ;
	
	static
	{
		font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		buttons = new ArrayList<>() ;
		
		player = new Player("Player", "f", 0) ;
		player.setPos(new Point(45, 230)) ;
		pet = Game.getPet() ;
		if (pet != null)
		{
			pet.setPos(new Point(450, 230)) ;
		}
		playerOpponent = new Creature(CreatureType.all.get(playerOpponentID)) ;
		playerOpponent.setPos(new Point(460, 340)) ;
		
		playerPreviousExp = player.getExp().getCurrentValue() ;
		
		stats.put("CreatureMoves", 0.0) ;
		stats.put("BattleTimeAvr", 0.0) ;
		stats.put("NCreatureMoves", 0.0) ;
		stats.put("NCreature" + "none", 0.0) ;
		stats.put("NCreature" + "miss", 0.0) ;
		stats.put("NCreature" + "hit", 0.0) ;
		stats.put("NCreature" + "crit", 0.0) ;
		stats.put("NCreature" + "block", 0.0) ;
		stats.put("NCreatureAtks", 0.0) ;
		stats.put("NCreatureDefs", 0.0) ;
		stats.put("NCreatureSpells", 0.0) ;
		stats.put("NBloodApplied", 0.0) ;
		stats.put("NPlayerMoves", 0.0) ;
		stats.put("NPlayer" + "none", 0.0) ;
		stats.put("NPlayer" + "miss", 0.0) ;
		stats.put("NPlayer" + "hit", 0.0) ;
		stats.put("NPlayer" + "crit", 0.0) ;
		stats.put("NPlayer" + "block", 0.0) ;
		stats.put("NPlayerAtks", 0.0) ;
		stats.put("NPlayerDefs", 0.0) ;
		stats.put("NPlayerSpells", 0.0) ;
		
		addJobSection() ;	
		
		addPlayerSection() ;
		
		addPetSection() ;
		
		addBattleSection() ;
	}
	
	private static GameButton newButton(Point pos, String text, ButtonFunction action)
	{
		return new GameButton(pos, Align.topLeft, text, buttonImage, buttonSelectedImage, action) ;
	}
	
	private static void addJobSection()
	{
		Point sectionPos = new Point(80, 32) ;
		
		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("cavaleiro", () -> { playerResetJob(0) ;}) ;
		namesActions.put("mago", () -> { playerResetJob(1) ;}) ;
		namesActions.put("arqueiro", () -> { playerResetJob(2) ;}) ;
		namesActions.put("animal", () -> { playerResetJob(3) ;}) ;
		namesActions.put("ladrão", () -> { playerResetJob(4) ;}) ;
		
		addSection(sectionPos, new Point(90, 0), namesActions) ;
	}

	private static void addPlayerSection()
	{
		addPlayerTrainSection() ;
		addPlayerLevelUpSection() ;
		addPlayerWinSection() ;
	}
	
	private static void addPlayerTrainSection()
	{
		Point sectionPos = new Point(10, 60 + 10) ;

		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("encher vida", () -> { playerFullLife() ;}) ;
		namesActions.put("train", () -> { playerTrain() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addPlayerLevelUpSection()
	{
		Point sectionPos = new Point(100, 60 + 10) ;
		
		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("+ level", () -> { playerLevelUp(1) ;}) ;
		namesActions.put("+ 5 level", () -> { playerLevelUp(5) ;}) ;
		namesActions.put("+ 10 level", () -> { playerLevelUp(10) ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addPlayerWinSection()
	{
		Point sectionPos = new Point(190, 60 + 10) ;
		
		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("win until 99", () -> { simulateWinsUntilLevel99() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addPetSection()
	{
		addPetLevelUpSection() ;
		addPetTrainSection() ;
	}
	
	private static void addPetLevelUpSection()
	{
		Point sectionPos = new Point(311, 60 + 10) ;

		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("pet", () -> { petReset() ;}) ;
		namesActions.put("encher vida", () -> { petFullLife() ;}) ;
		namesActions.put("train", () -> { petTrain() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addPetTrainSection()
	{
		Point sectionPos = new Point(401, 60 + 10) ;

		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("+ level", () -> { petLevelUp(1) ;}) ;
		namesActions.put("+ 5 level", () -> { petLevelUp(5) ;}) ;
		namesActions.put("+ 10 level", () -> { petLevelUp(10) ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}

	private static void addBattleSection()
	{
		addBattleResetSection() ;
		addBattleFightsSection() ;
	}

	private static void addBattleResetSection()
	{
		Point sectionPos = new Point(10, 266 + 10) ;

		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("Reset fights", () -> { resetFights() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}

	private static void addBattleFightsSection()
	{
		Point sectionPos = new Point(100, 266 + 10) ;

		Map<String, ButtonFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("Battle", () -> { simulateBattle() ;}) ;
		namesActions.put("Battle x 10", () -> { simulateBattle10x() ;}) ;
		namesActions.put("Battle x 100", () -> { simulateBattle100x() ;}) ;
		namesActions.put("Evolve", () -> { evolve() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addSection(Point pos, Point spacing, Map<String, ButtonFunction> sectionNamesActions)
	{
		Iterator<?> it = sectionNamesActions.keySet().iterator();

		int i = 0 ;
	    while (it.hasNext())
	    {
	    	String key = (String) it.next() ;
	    	ButtonFunction action = sectionNamesActions.get(key) ;
			Point buttonPos = Util.translate(pos, i * spacing.x, i * spacing.y) ;
			buttons.add(newButton(buttonPos, key, action)) ;
			i++ ;
	    }
	}

	private static void resetPlayer()
	{
		player.getBag().removeGold(player.getBag().getGold()) ;
		player.setAttPoints(0) ;
		player.getBag().empty() ;
		playerResetJob(player.getJob()) ;
	}
	
	private static void playerFullLife()
	{
		player.getLife().setToMaximum() ;
		player.getMp().setToMaximum() ;
		player.getSatiation().setToMaximum() ;
		player.getThirst().setToMaximum() ;
	}
	
	private static void petFullLife()
	{
		if (pet != null)
		{
			pet.getLife().setToMaximum() ;
			pet.getMp().setToMaximum() ;
			pet.getSatiation().setToMaximum() ;
		}
	}
	
	private static void playerResetJob(int newJob)
	{
		player.setLevel(1) ;
		player.setJob(newJob) ;
		
		List<String[]> prop = Player.InitialAtts ;
	    BasicAttribute life = new BasicAttribute(Integer.parseInt(prop.get(newJob)[2]), Integer.parseInt(prop.get(newJob)[2]), 1) ;
	    BasicAttribute mp = new BasicAttribute(Integer.parseInt(prop.get(newJob)[3]), Integer.parseInt(prop.get(newJob)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 5, Double.parseDouble(prop.get(newJob)[34])) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, Integer.parseInt(prop.get(newJob)[35])) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, Integer.parseInt(prop.get(newJob)[36])) ;
		
		player.setPA(new PersonalAttributes(life, mp, exp, satiation, thirst)) ;

		BasicBattleAttribute phyAtk = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[5]), 0, 0) ;
		BasicBattleAttribute magAtk = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[6]), 0, 0) ;
		BasicBattleAttribute phyDef = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[7]), 0, 0) ;
		BasicBattleAttribute magDef = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[8]), 0, 0) ;
		BasicBattleAttribute dex = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[9]), 0, 0) ;	
		BasicBattleAttribute agi = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[10]), 0, 0) ;
		BasicBattleAttribute critAtk = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[11]), 0, 0) ;
		BasicBattleAttribute critDef = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[12]), 0, 0) ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(prop.get(newJob)[13]), 0, Double.parseDouble(prop.get(newJob)[14]), 0, Integer.parseInt(prop.get(newJob)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(prop.get(newJob)[16]), 0, Double.parseDouble(prop.get(newJob)[17]), 0, Integer.parseInt(prop.get(newJob)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(prop.get(newJob)[19]), 0, Double.parseDouble(prop.get(newJob)[20]), 0, Integer.parseInt(prop.get(newJob)[21]), 0, Integer.parseInt(prop.get(newJob)[22]), 0, Integer.parseInt(prop.get(newJob)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(prop.get(newJob)[24]), 0, Double.parseDouble(prop.get(newJob)[25]), 0, Integer.parseInt(prop.get(newJob)[26]), 0, Integer.parseInt(prop.get(newJob)[27]), 0, Integer.parseInt(prop.get(newJob)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(prop.get(newJob)[29]), 0, Double.parseDouble(prop.get(newJob)[30]), 0, Integer.parseInt(prop.get(newJob)[31])) ;
		BasicBattleAttribute atkSpeed = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[41]), 0, 0) ;
		BasicBattleAttribute knockbackPower = new BasicBattleAttribute (Double.parseDouble(prop.get(newJob)[42]), 0, 0) ;
		
		player.setBA(new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, critAtk, critDef, stun, block, blood, poison, silence, atkSpeed, knockbackPower)) ;
		
		player.setRange(Integer.parseInt(prop.get(newJob)[4])) ;
		player.setStep(Integer.parseInt(prop.get(newJob)[33])) ;
		player.setSatiationCounter(new GameTimer(Double.parseDouble(prop.get(newJob)[38]))) ;
		player.setThirstCounter(new GameTimer(Double.parseDouble(prop.get(newJob)[39]))) ;
		player.setMpCounter(new GameTimer(Double.parseDouble(prop.get(newJob)[40]))) ;
		player.setBattleActionCounter(new GameTimer(Double.parseDouble(prop.get(newJob)[41]) / 100.0)) ;
		player.setGoldMultiplier(Double.parseDouble(prop.get(newJob)[32])) ;
		
		List<Double> attIncrements = Arrays.asList(Player.EvolutionProperties.get(3 * newJob + 0)).subList(2, 10).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		List<Double> incChances = Arrays.asList(Player.EvolutionProperties.get(3 * newJob + 0)).subList(10, 18).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		AttributeIncrease attInc = new AttributeIncrease(attIncrements, incChances) ;
		player.setAttInc(attInc) ;

    	player.setSpells(Player.jobSpells(player.getJob())) ;
	}
	
	private static void petReset()
	{
		if (pet == null)
		{
			Game.letThereBePet();
		}
		else
		{
			Game.removePet();
		}
		pet = Game.getPet() ;
	}

	private static void playerLevelUp(int times)
	{
		for (int i = 0 ; i <= times - 1 ; i += 1)
		{			
			if (99 <= player.getLevel()) { break ;}
			
			player.getExp().incCurrentValue(player.getExp().getMaxValue());
			player.levelUp() ;	
		}
	}
	
	public static void playerLevelUpAvr()
	{	

		if (Player.maxLevel <= player.getLevel()) { return ;}
		
		AttributeIncrease attInc = player.getAttInc() ;
		double[] attIncrease =  new double[attInc.getIncrement().basic().length + 1] ;

		for (int i = 0 ; i <= attInc.getIncrement().basic().length - 1 ; i += 1)
		{
//			if (attInc.getChance().basic()[i] <= Math.random()) { continue ;}
			
			attIncrease[i] = attInc.getIncrement().basic()[i] * attInc.getChance().basic()[i] ;
		}
		
		attIncrease[attInc.getIncrement().basic().length] = Player.calcExpToLevelUp(player.getLevel()) ;
		
		
		player.setLevel(player.getLevel() + 1) ;
		player.getPA().getLife().incMaxValue((int) attIncrease[0]) ;
		player.getPA().getMp().incMaxValue((int) attIncrease[1]); ;	
		player.getBA().getPhyAtk().incBaseValue(attIncrease[2]) ;
		player.getBA().getMagAtk().incBaseValue(attIncrease[3]) ;
		player.getBA().getPhyDef().incBaseValue(attIncrease[4]) ;
		player.getBA().getMagDef().incBaseValue(attIncrease[5]) ;
		player.getBA().getAgi().incBaseValue(attIncrease[6]) ;
		player.getBA().getDex().incBaseValue(attIncrease[7]) ;
		player.getPA().getExp().incMaxValue((int) attIncrease[8]) ;
		player.getPA().getLife().setToMaximum() ;
		player.getPA().getMp().setToMaximum() ;
//		spellPoints += 1 ;
		player.setAttPoints(player.getAttPoints() + 2) ;
		
		((PlayerAttributesWindow) player.getAttWindow()).activateIncAttButtons(player.getAttPoints()) ;
		
		Animation.start(AnimationTypes.levelUp, new Object[] {attIncrease, player.getLevel()});
	}
	
	public static void autoApplyAttPoints(Player player)
	{
		double sum = player.getAttInc().getChance().getPhyAtk() + player.getAttInc().getChance().getMagAtk() + 
						player.getAttInc().getChance().getPhyDef() + player.getAttInc().getChance().getMagDef() + 
						player.getAttInc().getChance().getDex() + player.getAttInc().getChance().getAgi() ;
		player.getBA().getPhyAtk().incBaseValue(2 * player.getAttInc().getChance().getPhyAtk() / sum) ;
		player.getBA().getMagAtk().incBaseValue(2 * player.getAttInc().getChance().getMagAtk() / sum) ;
		player.getBA().getPhyDef().incBaseValue(2 * player.getAttInc().getChance().getPhyDef() / sum) ;
		player.getBA().getMagDef().incBaseValue(2 * player.getAttInc().getChance().getMagDef() / sum) ;
		player.getBA().getDex().incBaseValue(2 * player.getAttInc().getChance().getDex() / sum) ;
		player.getBA().getAgi().incBaseValue(2 * player.getAttInc().getChance().getAgi() / sum) ;
		player.decAttPoints(2) ;
	}

	public static void trainAvr(Player player)
	{
		player.getBA().getPhyAtk().incTrain(0.2 * player.getAttInc().getChance().getPhyAtk() ) ;
		player.getBA().getMagAtk().incTrain(0.2 * player.getAttInc().getChance().getMagAtk() ) ;
		player.getBA().getPhyDef().incTrain(0.33 * player.getAttInc().getChance().getPhyDef() ) ;
		player.getBA().getMagDef().incTrain(0.33 * player.getAttInc().getChance().getMagDef() ) ;
		player.getBA().getDex().incTrain(0.35 * player.getAttInc().getChance().getDex() ) ;
		player.getBA().getAgi().incTrain(0.3 * player.getAttInc().getChance().getAgi() ) ;
	}
	
	private static void playerTrain()
	{
		player.trainOffensive(new AtkResults(AtkTypes.physical, AtkEffects.hit, 0, null)) ;
	}
	
	private static void petTrain()
	{
		if (pet == null) { return ;}
		
 		pet.trainOffensive(new AtkResults(AtkTypes.physical, AtkEffects.hit, 0, null)) ;
	}
	
	private static void petLevelUp(int times)
	{
		if (pet == null) { return ;}
		
		for (int i = 0 ; i <= times - 1 ; i += 1)
		{			
			if (99 <= pet.getLevel()) { break ;}

			pet.getExp().incCurrentValue(pet.getExp().getMaxValue());
			pet.levelUp() ;
		}
	}
	
	private static void incPlayerOpponentID(int amount)
	{
		if (playerOpponentID + amount <= CreatureType.all.size() - 1)
		{
			playerOpponentID += amount ;
			playerOpponent = new Creature(CreatureType.all.get(playerOpponentID)) ;
		}
	}
	
	private static void decPlayerOpponentID(int amount)
	{
		if (0 <= playerOpponentID - amount)
		{
			playerOpponentID += -amount ;
			playerOpponent = new Creature(CreatureType.all.get(playerOpponentID)) ;
		}
	}
	
	private static void resetFights() { numberFights = 0 ; numberPlayerWins = 0 ; numberCreatureWins = 0 ;}
	
	private static void incNumberPlayerWins() { numberPlayerWins += 1 ;}
	
	private static void incNumberCreatureWins() { numberCreatureWins += 1 ;}
	
	private static void ResetBattleResults()
	{
		BattleResultsPlayerLife = 0 ;
		BattleResultsCreatureLife = 0 ;
		stats.put("CreatureMoves", 0.0) ;
	}
	
	private static void CreateNewCreature()
	{
		playerOpponent = new Creature(CreatureType.all.get(playerOpponentID)) ;
		playerOpponent.setPos(new Point(460, 340)) ;
		playerOpponent.setRange(1000) ;
		playerOpponent.getType().setGenes(new Genetics(newGenes.getGenes(), newGenes.getGeneMods()));
	}
	
	private static void simulateBattle()
	{
//		Log.battleStart() ;
		ResetBattleResults() ;
		CreateNewCreature() ;
		player.setRange(1000) ;
		player.engageInFight(playerOpponent) ;
		incNumberFights() ;
		Battle.removeRandomness() ;
		battleClock = System.nanoTime() ;
	}

	private static void simulateBattle10x()
	{
		numberFightsRepetition = 10 ;
	}

	private static void simulateBattle100x()
	{
		numberFightsRepetition = 300 ;
	}
		
	private static void simulateWinsUntilLevel99()
	{
		int totalExpUntilMaxLevel = 0 ;
		for (int level = 1 ; level <= 100 - 1 ; level += 1)
		{
			totalExpUntilMaxLevel += (int) Player.calcExpToLevelUp(level) ;
		}

		int numberCreatures = CreatureType.all.size() ;
//		for (int creatureID = 0 ; creatureID <= numberCreatures - 1; creatureID += 1)
//		{
//			Creature creature = new Creature(CreatureType.all.get(creatureID]) ;
//			int itemsValue = 0 ;
//			for (Item item : creature.getBag())
//			{
//				if (item instanceof GeneralItem)
//				{
//					itemsValue += item.getPrice() ;
//				}
//			}
//			System.out.println(itemsValue);
//		}
		
//		double avrGoldUntilMaxLevel = 0 ;
		for (int creatureID = 0 ; creatureID <= numberCreatures - 1; creatureID += 1)
		{
			Creature creature = new Creature(CreatureType.all.get(creatureID)) ;
			int numberWinsToMaxLevel = totalExpUntilMaxLevel / creature.getExp().getCurrentValue() + 1 ;
//			int cumGoldUntilMaxLevel = creature.getGold() * numberWinsToMaxLevel ;
//			avrGoldUntilMaxLevel += cumGoldUntilMaxLevel / (double) numberCreatures ;
			
			for (int i = 0 ; i <= numberWinsToMaxLevel - 1; i += 1)
			{
				player.win(creature, false) ;
				checkPlayerWin() ;
				if (player.shouldLevelUP()) { player.levelUp() ;}
			}
			
			System.out.println(player.getBag().calcValue());
			 
			resetPlayer() ;
		}
		
	}
	
	private static void evolve()
	{
		System.out.println("numberFights lowestFitness currentFitness highestFitness genes geneMods") ;
		numberFightsRepetition = 1000 ;
		evolutionIsOn = true ;
	}
	
	public static boolean shouldUpdateGenes() { return evolutionIsOn & numberFights % numberRoundsToEvolve == 0 ; }
	
	public static void setBattleResults(int playerLife, int creatureLife)
	{
		BattleResultsPlayerLife = playerLife ;
		BattleResultsCreatureLife = creatureLife ;
	}
	
	public static void incNumberFights() { numberFights += 1 ;}
	
	public static void checkPlayerWin()
	{
		
		if (player.getExp().getCurrentValue() != playerPreviousExp)
		{
			playerPreviousExp = player.getExp().getCurrentValue() ;
			incNumberPlayerWins() ;
			
			return ;
		}
		
		incNumberCreatureWins() ;
		
	}
		
	public static void checkBattleRepeat()
	{
		
		if (numberFights <= numberFightsRepetition - 1)
		{
			playerFullLife() ;
			petFullLife() ;
			simulateBattle() ;
		}
		else
		{
			if (numberFights == numberFightsRepetition)
			{
//				System.out.println();
//				battletimes.forEach(bat -> System.out.println(bat)) ;
//				stats.forEach((key, value) -> System.out.println(key + "," + value));
//				System.out.println();
				numberFights = 0 ;
			}
			numberFightsRepetition = 0 ;
		}
		
	}
	
	
	public static void updateRecords()
	{		
		Genetics genes = playerOpponent.getType().getGenes() ;
		genes.setFitness(avrFitness) ;
		if (genes.getFitness() <= lowestFitness)
		{
			lowestFitness = genes.getFitness() ;
		}
		
		if (highestFitness <= genes.getFitness())
		{
			highestFitness = genes.getFitness() ;
		}
		
		if (listBestFitness.size() <= numberRandomGeneRounds - 1)
		{
			listBestGenes.add(genes) ;
			listBestFitness.add(genes.getFitness()) ;
		}
		else if (genes.areSelected(listBestFitness))
		{
			int indexMinFitness = listBestFitness.indexOf(Collections.min(listBestFitness)) ;
			listBestGenes.remove(indexMinFitness) ;
			listBestGenes.add(genes) ;
			listBestFitness.remove(indexMinFitness) ;
			listBestFitness.add(genes.getFitness()) ;
		}
	}
	
	public static void updateFitness()
	{
		playerOpponent.getType().getGenes().updateFitness(BattleResultsPlayerLife, player.getLife().getMaxValue(), BattleResultsCreatureLife, playerOpponent.getLife().getMaxValue()) ;
		currentFitness = playerOpponent.getType().getGenes().getFitness() ;		
		avrFitness += currentFitness / (double) numberRoundsToEvolve ;
	}
	
	public static void updateCreatureGenes()
	{
		for (double gene : playerOpponent.getType().getGenes().getGenes())
		{
			System.out.print(gene + ";") ;
		}
		for (List<Double> geneMods : playerOpponent.getType().getGenes().getGeneMods())
		{
			for (double geneMod : geneMods)
			{
				System.out.print(geneMod + ";") ;
			}
		}
		System.out.println();
//		System.out.print(listBestFitness + ";") ;
//		System.out.println(listBestGenes) ;
		
		avrFitness = 0 ;
		if (listBestFitness.size() <= numberRandomGeneRounds - 1)
		{
			newGenes.randomizeGenes() ;
			newGenes.randomizeGeneMods() ;
			
			return ;
		}
		
		int totalParentFitness = (int) listBestFitness.stream().mapToDouble(a -> a).sum() ;
		newGenes.breed2(listBestGenes, totalParentFitness) ;
		newGenes.setGenes(Genetics.normalize(newGenes.getGenes())) ;
		
	}
	
	
	public static void playerAutoFight()
	{
		
		if (!player.isAlive()) { return ;}
		
		int spell = Util.randomInt(0, player.getActiveSpells().size() - 1) ;
		int qtdAvailableMoves = player.canUseSpell(player.getSpells().get(spell)) ? 2 : 1 ;

		int move = Util.randomInt(0, qtdAvailableMoves) ;
//		System.out.println(move);
		switch (move)
		{
			case 0: player.setCurrentAction("Y") ; break ;
			case 1: player.setCurrentAction("U") ; break ;
			case 2: player.setCurrentAction(String.valueOf(spell)) ; break ;
		}
		
	}
	
	public static void act(String action, Point mousePos)
	{
		switch (action)
		{
			case "B": player.getBag().open() ; break ;
			case "C": 
//				player.getAttWindow().open() ;
				((PlayerAttributesWindow) player.getAttWindow()).updateAttIncButtons(player) ;
				
				((PlayerAttributesWindow) player.getAttWindow()).setPlayer(player) ;
				player.switchOpenClose(player.getAttWindow());
				
				break ;
			case "P": if (pet != null) { pet.getAttWindow().open() ;} ; break ;
			case "T": player.getSpellsTreeWindow().setSpells(player.getSpells()) ; player.getSpellsTreeWindow().open() ; break ;
			case "O":
				if (playerOpponent == null) { break ;}
				
				if (!playerOpponent.getAttWindow().isOpen())
				{
					playerOpponent.getAttWindow().open() ;
				}
				else
				{
					playerOpponent.getAttWindow().close() ;
				}
				
				break ;
				
			case "MouseWheelUp": incPlayerOpponentID(1) ; break ;
			case "MouseWheelDown": decPlayerOpponentID(1) ; break ;
		}
		
		if (player.getAttWindow().isOpen())
		{
			player.getAttWindow().navigate(player.getCurrentAction()) ;
		}
		
		if (player.getBag().isOpen())
		{
			player.getBag().navigate(player.getCurrentAction()) ;
		}

		if (player.getSpellsTreeWindow().isOpen())
		{
			player.getSpellsTreeWindow().navigate(player.getCurrentAction()) ;

			if (action.equals("Enter") | action.equals("LeftClick"))
			{
				player.getSpellsTreeWindow().act(player) ;
			}
		}
		
		if (player.getAttWindow().isOpen())
		{
			((PlayerAttributesWindow) player.getAttWindow()).act(player, mousePos, player.getCurrentAction()) ;
		
			return ;
		}

		if (player.getBag().isOpen())
		{
//			if (player.getBag().getTab() == 1 & (player.getCurrentAction().equals("Enter") | player.getCurrentAction().equals("LeftClick")))
//			{
//				player.useItem(player.getBag().getSelectedItem()) ;
//			}
			
			return ;
		}
		
		buttons.forEach(button -> {
			if (button.isClicked(mousePos, action))
			{
				button.act() ;
			}
		}) ;
		
	}
	
	public static void run(Point mousePos)
	{
		player.takeBloodAndPoisonDamage() ;
		player.activateSpellCounters() ;
		player.activateCounters() ;
		player.getSatiation().setToMaximum() ;
		player.getThirst().setToMaximum() ;
		if (pet != null)
		{
			pet.takeBloodAndPoisonDamage() ;
			pet.activateSpellCounters() ;
			pet.activateCounters() ;
			pet.getSatiation().setToMaximum() ;
		}

		if (playerOpponent != null)
		{
			playerOpponent.takeBloodAndPoisonDamage() ;
		}
		if (player.getCurrentAction() != null)
		{
			EvolutionSimulation.act(player.getCurrentAction(), mousePos) ;
		}
		if (player.isFighting())
		{
			player.getOpponent().activateSpellCounters() ;
			if (player.canAtk())
			{
				EvolutionSimulation.playerAutoFight() ;
			}
			Battle.runBattle(player, pet, player.getOpponent()) ;
			if (!player.isAlive() | !player.getOpponent().isAlive())
			{
				Battle.finishBattle(player, pet, player.getOpponent()) ;
			}
			if (!player.isFighting())
			{				
				EvolutionSimulation.updateFitness() ;
				if (EvolutionSimulation.shouldUpdateGenes())
				{
					EvolutionSimulation.updateRecords() ;
					EvolutionSimulation.updateCreatureGenes() ;
				}
				EvolutionSimulation.checkPlayerWin() ;
				EvolutionSimulation.stopBattleClock() ;
				player.setPos(new Point(45, 230)) ;
				pet = Game.getPet() ;
				if (pet != null)
				{
					pet.setPos(new Point(450, 230)) ;
				}
			}
			EvolutionSimulation.updateStats() ;
		}
		else
		{
			EvolutionSimulation.checkBattleRepeat() ;
		}
		EvolutionSimulation.displayInterface(mousePos) ;
		if (player.isFighting())
		{
			if (player.isDefending()) { player.displayDefending() ;}
			if (pet != null && pet.isDefending()) { pet.displayDefending() ;}
			if (player.getOpponent().isDefending()) { player.getOpponent().displayDefending() ;}
		}
		if (player.shouldLevelUP())
		{
			player.levelUp() ;
		}
		if (pet != null)
		{
			if (pet.shouldLevelUP())
			{
				pet.levelUp() ;
			}
		}
		player.showWindows(pet, mousePos) ;

		if (playerOpponent != null)
		{
			playerOpponent.activateSpellCounters() ;
			playerOpponent.activateCounters() ;
			if (playerOpponent.getAttWindow().isOpen())
			{
				CreatureType.attWindow.display(playerOpponent.getType()) ;
			}
		}

		Animation.playAll() ;
		player.resetAction() ;
	}
	
	public static void updateBattleStats(LiveBeing attacker, LiveBeing receiver, AtkResults atkResults)
	{
		if (attacker instanceof Player)
		{
			stats.put("NPlayerMoves", stats.get("NPlayerMoves") + 1 ) ;
			stats.put("NPlayer" + atkResults.getEffect(), stats.get("NPlayer" + atkResults.getEffect()) + 1 ) ;
			switch (attacker.getCurrentAction())
			{
				case "Y":
					stats.put("NPlayerAtks", stats.get("NPlayerAtks") + 1 ) ;
					break ;
				
				case "U":
					stats.put("NPlayerDefs", stats.get("NPlayerDefs") + 1 ) ;
					break ;
				
				default:
					break ;
			}
		}
		if (!(attacker instanceof Player))
		{
			stats.put("NCreatureMoves", stats.get("NCreatureMoves") + 1 ) ;
			stats.put("NCreature" + atkResults.getEffect(), stats.get("NCreature" + atkResults.getEffect()) + 1 ) ;
			if (atkResults != null && atkResults.getStatus() != null && 0 < atkResults.getStatus()[2])
			{
				System.out.println("Applied blood!" + (receiver.getStatus().get(Attributes.blood).isActive() ? " Already in blood" : ""));
				stats.put("NBloodApplied", stats.get("NBloodApplied") + 1) ;
			}
			switch (attacker.getCurrentAction())
			{
				case "Y":
					stats.put("NCreatureAtks", stats.get("NCreatureAtks") + 1 ) ;
					break ;
				
				case "U":
					stats.put("NCreatureDefs", stats.get("NCreatureDefs") + 1 ) ;
					break ;
					
				case "0":
					stats.put("NCreatureSpells", stats.get("NCreatureSpells") + 1 ) ;
					break ;
				
				default:
					break ;
			}
		}
		
	}
	
	private static void updateStats()
	{
		if (playerOpponent.canAtk())
		{
			stats.put("CreatureMoves", stats.get("CreatureMoves") + 1) ;
		}
		if (!player.isFighting() & 1 <= numberFights)
		{
			double battleTime = Math.round(100 * (battleClock) * Math.pow(10, -9)) / 100.0 ;
			double totalBattleTime = stats.get("BattleTimeAvr") * (numberFights - 1) + battleTime ;
			stats.put("BattleTimeAvr", Util.round(totalBattleTime / numberFights, 2)) ;
			battletimes.add(stats.get("CreatureMoves")) ;
		}
	}
	
	private static void stopBattleClock()
	{
		battleClock = System.nanoTime() - battleClock ;
	}

	public static void drawBattleStatictics(Point pos)
	{
		Font textFont = new Font(font.getName(), font.getStyle(), 11) ;
		Color textColor = Game.palette[0] ;
		
		Iterator<?> it = stats.keySet().iterator();

		int i = 0 ;
	    while (it.hasNext())
	    {
	    	String attName = (String) it.next() ;
	    	double attValue = stats.get(attName) ;
	    	GamePanel.DP.drawText(Util.translate(pos, 0, i * 13), Align.topLeft, Draw.stdAngle, attName + ": " + attValue, textFont, textColor) ;
			i += 1;
	    }

	}
	
	public static void drawDerivedBattleAttributes(Point pos, LiveBeing attacker, LiveBeing defender)
	{
		Font textFont = new Font(font.getName(), font.getStyle(), 11) ;
		Color textColor = Game.palette[0] ;
		
		double rateMagAtkAttacker =	attacker instanceof Player ? 0.4147 :  0.366;
		double ratePhyAtkAttacker = 0.5 - rateMagAtkAttacker / 2.0  ;
		double rateDefDefender = 0.5 - rateMagAtkAttacker / 2.0 ;

		double critRate = attacker.getBA().getCritAtk().getTotal() - defender.getBA().getCritDef().getTotal() ;
		double hitRate = 1 - 1 / (1 + Math.pow(1.1, attacker.getBA().getDex().getTotal() - defender.getBA().getAgi().getTotal())) ;
		double bloodRate = Math.max(attacker.getBA().getBlood().getBasicAtkChance() - defender.getBA().getBlood().getBasicDefChance(), 0) ;
		
		double spellDam = attacker instanceof Player ? 2.0 + attacker.getBA().getMagAtk().getTotal() - defender.getBA().getMagDef().getTotal() : 1 + 1.02 * attacker.getBA().getMagAtk().getTotal() - defender.getBA().getMagDef().getTotal() ;
		
		int phyDamBase = Math.max((int) Util.round(attacker.getBA().getPhyAtk().getTotal() - defender.getBA().getPhyDef().getTotal(), 0), 0) ;
		int spellDamBase = Math.max((int) Util.round(spellDam, 0), 0) ;
		int phyDamInDefense = Math.max((int) Util.round(attacker.getBA().getPhyAtk().getTotal() - defender.getBA().getPhyDef().getTotal() - defender.getBA().getPhyDef().getBaseValue(), 0), 0) ;
		int magDamInDefense = Math.max((int) Util.round(spellDam - defender.getBA().getMagDef().getBaseValue(), 0), 0) ;
		int bloodDam = Math.max((int) Util.round(attacker.getBA().getBlood().TotalAtk() - defender.getBA().getBlood().TotalDef(), 0), 0) ;
		int phyDamCrit = (int) Util.round(attacker.getBA().getPhyAtk().getTotal(), 0) ;
		int spellDamCrit = (int) Util.round(attacker instanceof Player ? 2.0 + attacker.getBA().getMagAtk().getTotal() : 1 + 1.02 * attacker.getBA().getMagAtk().getTotal(), 0) ;
		
		double bloodDuration = attacker.getBA().getBlood().getDuration() ;
		double damPerMove = ratePhyAtkAttacker * hitRate * ((1 - critRate) * ((1 - rateDefDefender) * phyDamBase + rateDefDefender * phyDamInDefense) + critRate * phyDamCrit) +
							rateMagAtkAttacker * hitRate * ((1 - critRate) * ((1 - rateDefDefender) * spellDamBase + rateDefDefender * magDamInDefense) + critRate * spellDamCrit) +
							bloodRate * bloodDam * bloodDuration ;
		
		double movesPerSec = 1 / attacker.getBattleActionCounter().getDuration() ;
//		System.out.println();
//		System.out.println(attacker.getName());
//		System.out.println("hitRate: " + hitRate);
//		System.out.println("phyDamBase: " + phyDamBase);
//		System.out.println("spellDamBase: " + spellDamBase);
//		System.out.println("phyDamInDefense: " + phyDamInDefense);
//		System.out.println("magDamInDefense: " + magDamInDefense);
//		System.out.println("phyDamCrit: " + phyDamCrit);
//		System.out.println("spellDamCrit: " + spellDamCrit);
//		System.out.println("phyDamPerMove: " + (ratePhyAtkAttacker * hitRate * ((1 - critRate) * ((1 - rateDefDefender) * phyDamBase + rateDefDefender * phyDamInDefense) + critRate * phyDamCrit)));
//		System.out.println("spellDamPerMove: " + (rateMagAtkAttacker * hitRate * ((1 - critRate) * ((1 - rateDefDefender) * spellDamBase + rateDefDefender * magDamInDefense) + critRate * spellDamCrit)));
//		System.out.println(attacker.getName() + " moves to win: " + defender.getPA().getLife().getMaxValue() / damPerMove) ;
		double damPerSec = damPerMove * hitRate  * movesPerSec ;
		double timeToWin = defender.getPA().getLife().getMaxValue() / damPerSec ;
		
		Map<String, Double> atts = new LinkedHashMap<>() ;
		atts.put("Life max: ", attacker.getPA().getLife().getMaxValue() * 1.0) ;
		atts.put("Move / s: ", movesPerSec) ;
		atts.put("Hit rate: ", hitRate) ;
		atts.put("Crit rate: ", critRate) ;
		atts.put("Damage / s: ", damPerSec) ;
		atts.put("Time to win: ", timeToWin <= Math.pow(10, 8) ? Util.round(timeToWin, 2) : timeToWin) ;
		
		Iterator<?> it = atts.keySet().iterator();

		int i = 0 ;
	    while (it.hasNext())
	    {
	    	String attName = (String) it.next() ;
	    	double attValue = Math.round(100 * atts.get(attName)) / 100.0 ;
	    	GamePanel.DP.drawText(Util.translate(pos, 0, i * 13), Align.topLeft, Draw.stdAngle, attName + attValue, textFont, textColor) ;
			i += 1;
	    }
	}
	
	// private static void printCreaturesPowerList()
	// {
	// 	for (int i = 0 ; i <= CreatureType.all.size() - 1; i += 1)
	// 	{
	// 		Creature creature = new Creature(CreatureType.all.get(i)) ;
	// 		System.out.println(creature.totalPower()) ;
	// 	}		
	// }
	
	public static void drawBar(Point pos, int currentHeight, int maxHeight, Color color)
	{
		int width = 10 ;
		GamePanel.DP.drawRect(pos, Align.bottomLeft, new Dimension(width, currentHeight), color, null) ;
		GamePanel.DP.drawRect(pos, Align.bottomLeft, new Dimension(width, maxHeight), null, color) ;
	}
	
	public static void displayBattleStats()
	{
		Point pos = new Point(10, 400) ;
		int barsHeight = 50 ;
		
		int numberOpponentsToPlayerLevelUp = PersonalAttributes.numberFightsToLevelUp(player.getExp().getCurrentValue(), player.getExp().getMaxValue(), playerOpponent.getExp().getCurrentValue(), player.getExp().getMultiplier()) ;
		GamePanel.DP.drawText(Util.translate(pos, 170, 10), Align.bottomCenter, Draw.stdAngle, "+ " + numberOpponentsToPlayerLevelUp, font, Game.palette[5]);

		int playerExpBarSize = (int) (player.getExp().getRate() * barsHeight) ;
		drawBar(Util.translate(pos, 170, 70), playerExpBarSize, barsHeight, Game.palette[5]) ;
		
		if (pet != null)
		{
			int numberOpponentsToPetLevelUp = PersonalAttributes.numberFightsToLevelUp(pet.getExp().getCurrentValue(), pet.getExp().getMaxValue(), playerOpponent.getExp().getCurrentValue(), pet.getExp().getMultiplier()) ;
			GamePanel.DP.drawText(Util.translate(pos, 200, 10), Align.bottomCenter, Draw.stdAngle, "+ " + numberOpponentsToPetLevelUp, font, Game.palette[2]);
			
			int petExpBarSize = (int) (pet.getExp().getRate() * barsHeight) ;
			drawBar(Util.translate(pos, 200, 70), petExpBarSize, barsHeight, Game.palette[2]) ;
		}
		
		String percPlayerWins = 1 <= numberFights ? " (" + Util.round((100 * numberPlayerWins) / (double)numberFights, 2) + "%)" : "" ;
		String percCreatureWins = 1 <= numberFights ? " (" + Util.round((100 * numberCreatureWins) / (double)numberFights, 2) + "%)" : "" ;
		GamePanel.DP.drawText(Util.translate(pos, 0, 30), Align.bottomLeft, Draw.stdAngle, "total fights = " + numberFights, font, Game.palette[5]);
		GamePanel.DP.drawText(Util.translate(pos, 0, 50), Align.bottomLeft, Draw.stdAngle, "player wins = " + numberPlayerWins + percPlayerWins, font, Game.palette[5]);
		GamePanel.DP.drawText(Util.translate(pos, 0, 70), Align.bottomLeft, Draw.stdAngle, "creature wins = " + numberCreatureWins + percCreatureWins, font, Game.palette[5]);

	}
		
	public static void displayInterface(Point mousePos)
	{
		
		GamePanel.DP.drawImage(screenImage, new Point(0, 0), Align.topLeft) ;
		GamePanel.DP.drawText(new Point(300, 13), Align.center, Draw.stdAngle, "Simulador do jogo", font, Game.palette[0]) ;
		
		buttons.forEach(button -> button.display(0, true, mousePos)) ;

		playerOpponent.displayName(new Point(460, 300), Align.center, Color.yellow);
		playerOpponent.display(playerOpponent.getPos(), Scale.unit);
		playerOpponent.displayPowerBar(new Point(520, 360)) ;
		drawDerivedBattleAttributes(new Point(310, 400), playerOpponent, player) ;
		
		if (player.isAlive())
		{
			player.display(player.getPos(), new Scale(1.8, 1.8), Directions.right, false) ;
			drawDerivedBattleAttributes(new Point(100, 170), player, playerOpponent) ;
		}
		
		if (pet != null && pet.isAlive())
		{
			pet.display(pet.getPos(), Scale.unit) ;
			drawDerivedBattleAttributes(new Point(440, 170), pet, playerOpponent) ;
		}
		
		drawBattleStatictics(new Point(440, 400)) ;

		double battleTime = Math.round(100 * (battleClock) * Math.pow(10, -9)) / 100.0 ;
		if (player.isFighting())
		{
			battleTime = Math.round(100 * (System.nanoTime() - battleClock) * Math.pow(10, -9)) / 100.0 ;
			GamePanel.DP.drawImage(fightingImage, new Point(300, 240), Align.center) ;
		}
		GamePanel.DP.drawText(new Point(10, 410), Align.centerLeft, Draw.stdAngle, "Battle time: " + battleTime + "s", font, Game.palette[0]) ;
		
		
		displayBattleStats() ;
		
	}

}
