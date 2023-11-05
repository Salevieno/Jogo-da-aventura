package simulations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import attributes.AttributeBonus;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import components.GameButton;
import components.IconFunction;
import graphics.Animation;
import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.Genetics;
import liveBeings.LiveBeingStatus;
import liveBeings.Pet;
import liveBeings.Player;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import utilities.Align;
import utilities.AtkEffects;
import utilities.Directions;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import utilities.UtilS;
import windows.CreatureAttributesWindow;
import windows.GameWindow;
import windows.PlayerAttributesWindow;

public abstract class PlayerEvolutionSimulation
{
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
	
	private static Player player = Game.getPlayer() ;
	private static Pet pet = Game.getPet() ;
	private static int playerOpponentID = 0 ;
	private static Creature playerOpponent = new Creature(Game.getCreatureTypes()[playerOpponentID]) ;
	
	private static int playerPreviousExp = player.getExp().getCurrentValue() ;	
	private static int numberFights = 0 ;
	private static int numberPlayerWins = 0 ;
	private static int numberCreatureWins = 0 ;
	
	private static int numberFightsRepetition = 0 ;
	
	private static List<GameButton> buttons ;

	private static final Image screenImage = UtilS.loadImage("SimulationScreen.png") ;
	private static final Image buttonImage = UtilS.loadImage("ButtonGeneral.png") ;
	private static final Image buttonSelectedImage = UtilS.loadImage("ButtonGeneralSelected.png") ;
	private static final Image fightingImage = UtilS.loadImage("fightingIcon.png") ;
	
	
	private static int BattleResultsPlayerLife = 0 ;
	private static int BattleResultsCreatureLife = 0 ;
	
	private static int numberRandomGeneRounds = 6 ;
	private static int numberRoundsToEvolve = 4 ;
	private static int currentFitness = 0 ;
	private static int lowestFitness = 2000 ;
	private static int avrFitness = 0 ;
	private static int highestFitness = 0 ;
	private static List<Integer> listBestFitness = new ArrayList<>() ;
//	private static List<List<Double>> listBestGenes = new ArrayList<>() ;
	private static List<Genetics> listBestGenes = new ArrayList<>() ;
//	private static List<Double> bestGenes = new ArrayList<>() ;
	private static Genetics newGenes = new Genetics() ;
	private static boolean evolutionIsOn = false ;
	
	static
	{		
		buttons = new ArrayList<>() ;
		player.setPos(new Point(45, 230)) ;
		if (pet != null)
		{
			pet.setPos(new Point(450, 230)) ;
		}
		
		addJobSection() ;	
		
		addPlayerSection() ;
		
		addPetSection() ;
		
		addBattleSection() ;
	}
	
	private static GameButton newButton(Point pos, String text, IconFunction action)
	{
		return new GameButton(pos, Align.topLeft, text, buttonImage, buttonSelectedImage, action) ;
	}
	
	private static void addJobSection()
	{
		Point sectionPos = new Point(80, 32) ;
		
		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
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

		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("encher vida", () -> { playerFullLife() ;}) ;
		namesActions.put("train", () -> { playerTrain() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addPlayerLevelUpSection()
	{
		Point sectionPos = new Point(100, 60 + 10) ;
		
		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("+ level", () -> { playerLevelUp(1) ;}) ;
		namesActions.put("+ 5 level", () -> { playerLevelUp(5) ;}) ;
		namesActions.put("+ 10 level", () -> { playerLevelUp(10) ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addPlayerWinSection()
	{
		Point sectionPos = new Point(190, 60 + 10) ;
		
		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
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

		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("pet", () -> { petReset() ;}) ;
		namesActions.put("encher vida", () -> { petFullLife() ;}) ;
		namesActions.put("train", () -> { petTrain() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addPetTrainSection()
	{
		Point sectionPos = new Point(401, 60 + 10) ;

		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
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

		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("Reset fights", () -> { resetFights() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}

	private static void addBattleFightsSection()
	{
		Point sectionPos = new Point(100, 266 + 10) ;

		Map<String, IconFunction> namesActions = new LinkedHashMap<>() ;
		namesActions.put("Battle", () -> { simulateBattle() ;}) ;
		namesActions.put("Battle x 10", () -> { simulateBattle10x() ;}) ;
		namesActions.put("Battle x 100", () -> { simulateBattle100x() ;}) ;
		namesActions.put("Evolve", () -> { evolve() ;}) ;
		
		addSection(sectionPos, new Point(0, 30), namesActions) ;
	}
	
	private static void addSection(Point pos, Point spacing, Map<String, IconFunction> sectionNamesActions)
	{
		Iterator<?> it = sectionNamesActions.keySet().iterator();

		int i = 0 ;
	    while (it.hasNext())
	    {
	    	String key = (String) it.next() ;
	    	IconFunction action = sectionNamesActions.get(key) ;
			Point buttonPos = UtilG.Translate(pos, i * spacing.x + buttonImage.getWidth(null) / 2, i * spacing.y + buttonImage.getHeight(null) / 2) ;
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
		
		List<String[]> prop = Player.Properties ;
	    BasicAttribute life = new BasicAttribute(Integer.parseInt(prop.get(newJob)[2]), Integer.parseInt(prop.get(newJob)[2]), 1) ;
	    BasicAttribute mp = new BasicAttribute(Integer.parseInt(prop.get(newJob)[3]), Integer.parseInt(prop.get(newJob)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 5, Double.parseDouble(prop.get(newJob)[34])) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, Integer.parseInt(prop.get(newJob)[35])) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, Integer.parseInt(prop.get(newJob)[36])) ;
		
		player.setPA(new PersonalAttributes(life, mp, exp, satiation,	thirst)) ;

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
		LiveBeingStatus status = new LiveBeingStatus() ;
		
		player.setBA(new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, critAtk, critDef, stun, block, blood, poison, silence, status)) ;
		
		player.setRange(Integer.parseInt(prop.get(newJob)[4])) ;
		player.setStep(Integer.parseInt(prop.get(newJob)[33])) ;
		player.setActionCounter(new TimeCounter(0, Integer.parseInt(prop.get(newJob)[37]))) ;
		player.setSatiationCounter(new TimeCounter(0, Integer.parseInt(prop.get(newJob)[38]))) ;
		player.setThirstCounter(new TimeCounter(0, Integer.parseInt(prop.get(newJob)[39]))) ;
		player.setMpCounter(new TimeCounter(0, Integer.parseInt(prop.get(newJob)[40]))) ;
		player.setBattleActionCounter(new TimeCounter(0, Integer.parseInt(prop.get(newJob)[41]))) ;
		player.setGoldMultiplier(Double.parseDouble(prop.get(newJob)[32])) ;
		
		double [] basicAttInc = new double[8] ;
		double [] basicAttChanceInc = new double[8] ;
		for (int att = 0 ; att <= basicAttInc.length - 1 ; att += 1)
		{
			basicAttInc[att] = Double.parseDouble(Player.EvolutionProperties.get(3 * newJob)[att + 2]) ;
			basicAttChanceInc[att] = Double.parseDouble(Player.EvolutionProperties.get(3 * newJob)[att + 10]) ;
		}
		player.setAttIncrease(new AttributeBonus()) ;
		player.getAttIncrease().setBasic(basicAttInc) ;
		player.setAttChanceIncrease(new AttributeBonus()) ;
		player.getAttChanceIncrease().setBasic(basicAttChanceInc) ;

    	player.InitializeSpells() ;
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
			player.levelUp(null) ; // Game.getAnimations()[4]			
		}
	}
	
	private static void playerTrain()
	{
		player.train(new AtkResults(AtkTypes.physical, AtkEffects.hit, 0)) ;
	}
	
	private static void petTrain()
	{
		player.train(new AtkResults(AtkTypes.physical, AtkEffects.hit, 0)) ;
	}
	
	private static void petLevelUp(int times)
	{
		if (pet == null) { return ;}
		
		for (int i = 0 ; i <= times - 1 ; i += 1)
		{			
			if (99 <= pet.getLevel()) { break ;}

			pet.getExp().incCurrentValue(pet.getExp().getMaxValue());
			pet.levelUp(null) ; // Game.getAnimations()[4]
		}
	}
	
	private static void incPlayerOpponentID(int amount)
	{
		if (playerOpponentID + amount <= Game.getCreatureTypes().length - 1)
		{
			playerOpponentID += amount ;
			playerOpponent = new Creature(Game.getCreatureTypes()[playerOpponentID]) ;
		}
	}
	
	private static void decPlayerOpponentID(int amount)
	{
		if (0 <= playerOpponentID - amount)
		{
			playerOpponentID += -amount ;
			playerOpponent = new Creature(Game.getCreatureTypes()[playerOpponentID]) ;
		}
	}
	
	private static void resetFights() { numberFights = 0 ; numberPlayerWins = 0 ; numberCreatureWins = 0 ;}
	
	private static void incNumberPlayerWins() { numberPlayerWins += 1 ;}
	
	private static void incNumberCreatureWins() { numberCreatureWins += 1 ;}
	
	private static void ResetBattleResults()
	{
		BattleResultsPlayerLife = 0 ;
		BattleResultsCreatureLife = 0 ;
	}
	
	private static void CreateNewCreature()
	{
		playerOpponent = new Creature(Game.getCreatureTypes()[playerOpponentID]) ;
		playerOpponent.setPos(player.getPos());
		playerOpponent.getType().setGenes(new Genetics(newGenes.getGenes(), newGenes.getGeneMods()));
	}
	
	private static void simulateBattle()
	{
		Battle.PrintStart() ;
		ResetBattleResults() ;
		CreateNewCreature() ;
		player.engageInFight(playerOpponent) ;
		incNumberFights() ;
	}

	private static void simulateBattle10x()
	{
		numberFightsRepetition = 10 ;
	}

	private static void simulateBattle100x()
	{
		numberFightsRepetition = 100 ;
	}
		
	private static void simulateWinsUntilLevel99()
	{
		int totalExpUntilMaxLevel = 0 ;
		for (int level = 1 ; level <= 100 - 1 ; level += 1)
		{
			totalExpUntilMaxLevel += (int) Player.calcExpToLevelUp(level) ;
		}

		int numberCreatures = Game.getCreatureTypes().length ;
//		for (int creatureID = 0 ; creatureID <= numberCreatures - 1; creatureID += 1)
//		{
//			Creature creature = new Creature(Game.getCreatureTypes()[creatureID]) ;
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
			Creature creature = new Creature(Game.getCreatureTypes()[creatureID]) ;
			int numberWinsToMaxLevel = totalExpUntilMaxLevel / creature.getExp().getCurrentValue() + 1 ;
//			int cumGoldUntilMaxLevel = creature.getGold() * numberWinsToMaxLevel ;
//			avrGoldUntilMaxLevel += cumGoldUntilMaxLevel / (double) numberCreatures ;
			
			for (int i = 0 ; i <= numberWinsToMaxLevel - 1; i += 1)
			{
				player.win(creature, false) ;
				checkPlayerWin() ;
				if (player.shouldLevelUP()) { player.levelUp(null) ;}
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
//			System.out.println(" ------ new best fitness ------ ") ;
			highestFitness = genes.getFitness() ;
//			bestGenes = new ArrayList<>(genes.getGenes());
		}
		
		if (listBestFitness.size() <= numberRandomGeneRounds - 1)
		{
			listBestGenes.add(genes) ;
			listBestFitness.add(genes.getFitness()) ;
//			listBestFitness.sort(null) ;	
		}
		else if (genes.areSelected(listBestFitness))
		{
			int indexMinFitness = listBestFitness.indexOf(Collections.min(listBestFitness)) ;
			listBestGenes.remove(indexMinFitness) ;
			listBestGenes.add(genes) ;
			listBestFitness.remove(indexMinFitness) ;
			listBestFitness.add(genes.getFitness()) ;
//			listBestFitness.sort(null) ;	
			
//			System.out.println("listFitness = " + listFitness) ;
//			System.out.println("listBestGenes = " + listBestGenes) ;
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
//		System.out.println("\n Fight n° " + numberFights) ;
		System.out.print(numberFights + ";") ;
		System.out.print(lowestFitness + ";") ;
		System.out.print(avrFitness + ";") ;
		System.out.print(highestFitness + ";") ;
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
	
	public static void playerFight()
	{
		if (!player.isAlive()) { return ;}
		
		int move = UtilG.randomIntFromTo(0, 2) ;
		switch (move)
		{
			case 0: player.setCurrentAction("Y") ; break ;
			case 1: player.setCurrentAction("U") ; break ;
			case 2:
			{
				int spell = UtilG.randomIntFromTo(0, player.getActiveSpells().size() - 1) ;
				player.setCurrentAction(String.valueOf(spell)) ; break ;
			}
		}
		
	}
	
	public static void act(String action, Point mousePos)
	{

		switch (action)
		{
			case "B": player.getBag().open() ; break ;
			case "C": player.getAttWindow().open() ; ((PlayerAttributesWindow) player.getAttWindow()).updateAttIncButtons(player) ; break ;
			case "P": if (pet != null) { pet.getAttWindow().open() ;} ; break ;
			case "T": player.getSpellsTreeWindow().setSpells(player.getSpells()) ; player.getSpellsTreeWindow().open() ; break ;
			case "O": if (player.getOpponent() != null) { player.getOpponent().getAttWindow().open() ;} ; break ;
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
			if (player.getBag().getTab() == 1 & (player.getCurrentAction().equals("Enter") | player.getCurrentAction().equals("LeftClick")))
			{
				player.useItem(player.getBag().getSelectedItem()) ;
			}
			
			return ;
		}
		
		buttons.forEach(button -> {
			if (button.isClicked(mousePos, action))
			{
				button.act() ;
			}
		}) ;
		
	}
	
	public static GameWindow getPlayerAttWindow()
	{
		return player.getAttWindow() ;
	}
	
	public static void run(Point mousePos, List<Animation> animations, DrawingOnPanel DP)
	{
		player.incrementCounters() ;
		player.activateCounters() ;
		player.getSatiation().setToMaximum() ;
		player.getThirst().setToMaximum() ;
		if (pet != null)
		{
			pet.incrementCounters() ;
			pet.activateCounters() ;
			pet.getSatiation().setToMaximum() ;
		}

		if (player.getCurrentAction() != null)
		{
			PlayerEvolutionSimulation.act(player.getCurrentAction(), mousePos) ;
		}
		if (player.isInBattle())
		{
//	        		Creature creature = player.getOpponent() ;
//	        		creature.fight() ;
			player.getOpponent().incrementCounters() ;
			PlayerEvolutionSimulation.playerFight() ;
//	        		if (pet != null) { pet.fight() ;}
			Battle.runBattle(player, pet, player.getOpponent(), DP) ;
			if (!player.isInBattle())
			{
				PlayerEvolutionSimulation.updateFitness() ;
				if (PlayerEvolutionSimulation.shouldUpdateGenes())
				{
					PlayerEvolutionSimulation.updateRecords() ;
					PlayerEvolutionSimulation.updateCreatureGenes() ;
				}
				PlayerEvolutionSimulation.checkPlayerWin() ;
			}
		}
		else
		{
			PlayerEvolutionSimulation.checkBattleRepeat() ;
		}
		PlayerEvolutionSimulation.displayInterface(mousePos, DP) ;
		if (player.shouldLevelUP())
		{
			player.levelUp(null) ;
		}
		if (pet != null)
		{
			if (pet.shouldLevelUP())
			{
				pet.levelUp(null) ;
			}
		}
		player.showWindows(pet, mousePos, DP) ;

		if (player.getOpponent() != null)
		{
			if (player.getOpponent().getAttWindow().isOpen())
			{
				((CreatureAttributesWindow) player.getOpponent().getAttWindow()).display(player.getOpponent(), DP) ;
			}
		}

		for (Animation ani : animations)
		{
			ani.run(DP) ;
		}
		player.resetAction() ;
	}
	
	public static void drawBar(Point pos, int currentHeight, int maxHeight, Color color, DrawingOnPanel DP)
	{
		int width = 10 ;
		DP.DrawRect(pos, Align.bottomLeft, new Dimension(width, currentHeight), 1, color, null) ;
		DP.DrawRect(pos, Align.bottomLeft, new Dimension(width, maxHeight), 1, null, color) ;
	}
	
	public static void displayBattleStats(DrawingOnPanel DP)
	{
		Point pos = new Point(10, 400) ;
		int barsHeight = 50 ;
		
		int numberOpponentsToPlayerLevelUp = PersonalAttributes.numberFightsToLevelUp(player.getExp().getCurrentValue(), player.getExp().getMaxValue(), playerOpponent.getExp().getCurrentValue(), player.getExp().getMultiplier()) ;
		DP.DrawText(UtilG.Translate(pos, 170, 10), Align.bottomCenter, DrawingOnPanel.stdAngle, "+ " + numberOpponentsToPlayerLevelUp, font, Game.colorPalette[5]);

		int playerExpBarSize = (int) (player.getExp().getRate() * barsHeight) ;
		drawBar(UtilG.Translate(pos, 170, 70), playerExpBarSize, barsHeight, Game.colorPalette[5], DP) ;
		
		if (pet != null)
		{
			int numberOpponentsToPetLevelUp = PersonalAttributes.numberFightsToLevelUp(pet.getExp().getCurrentValue(), pet.getExp().getMaxValue(), playerOpponent.getExp().getCurrentValue(), pet.getExp().getMultiplier()) ;
			DP.DrawText(UtilG.Translate(pos, 200, 10), Align.bottomCenter, DrawingOnPanel.stdAngle, "+ " + numberOpponentsToPetLevelUp, font, Game.colorPalette[2]);
			
			int petExpBarSize = (int) (pet.getExp().getRate() * barsHeight) ;
			drawBar(UtilG.Translate(pos, 200, 70), petExpBarSize, barsHeight, Game.colorPalette[2], DP) ;
		}
		
		String percPlayerWins = 1 <= numberFights ? " (" + UtilG.Round((100 * numberPlayerWins) / (double)numberFights, 2) + "%)" : "" ;
		String percCreatureWins = 1 <= numberFights ? " (" + UtilG.Round((100 * numberCreatureWins) / (double)numberFights, 2) + "%)" : "" ;
		DP.DrawText(UtilG.Translate(pos, 0, 30), Align.bottomLeft, DrawingOnPanel.stdAngle, "total fights = " + numberFights, font, Game.colorPalette[5]);
		DP.DrawText(UtilG.Translate(pos, 0, 50), Align.bottomLeft, DrawingOnPanel.stdAngle, "player wins = " + numberPlayerWins + percPlayerWins, font, Game.colorPalette[5]);
		DP.DrawText(UtilG.Translate(pos, 0, 70), Align.bottomLeft, DrawingOnPanel.stdAngle, "creature wins = " + numberCreatureWins + percCreatureWins, font, Game.colorPalette[5]);

	}
	
	
	public static void displayInterface(Point mousePos, DrawingOnPanel DP)
	{
		
//		DP.DrawRect(new Point(0, 0), Align.topLeft, Game.getScreen().getSize(), 1, Color.black, null) ;
		DP.DrawImage(screenImage, new Point(0, 0), Align.topLeft) ;
		DP.DrawText(new Point(300, 13), Align.center, DrawingOnPanel.stdAngle, "Simulador do jogo", font, Game.colorPalette[0]) ;
		
		buttons.forEach(button -> button.display(0, true, mousePos, DP)) ;

		playerOpponent.displayName(new Point(460, 300), Align.center, Color.yellow, DP);
		playerOpponent.display(new Point(460, 340), Scale.unit, DP);
		
		if (player.isAlive())
		{
			player.display(player.getPos(), new Scale(1.8, 1.8), Directions.right, false, DP) ;
		}
		
		if (pet != null)
		{
			if (pet.isAlive())
			{
				pet.display(pet.getPos(), Scale.unit, DP) ;
			}
		}
		
		if (player.isInBattle())
		{
			DP.DrawImage(fightingImage, new Point(300, 240), Align.center) ;
		}
		
		displayBattleStats(DP) ;
		
	}

}
