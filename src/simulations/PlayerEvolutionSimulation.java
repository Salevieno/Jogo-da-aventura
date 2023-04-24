package simulations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
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
import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.LiveBeing;
import liveBeings.LiveBeingStatus;
import liveBeings.Pet;
import liveBeings.Player;
import main.AtkResults;
import main.Game;
import utilities.Align;
import utilities.Directions;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
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
	
	private static final Image buttonImage = UtilG.loadImage(Game.ImagesPath + "ButtonGeneral.png") ;
	private static final Image buttonSelectedImage = UtilG.loadImage(Game.ImagesPath + "ButtonGeneralSelected.png") ;
	private static final Image fightingImage = UtilG.loadImage(Game.ImagesPath + "fightingIcon.png") ;
	
	static
	{		
		buttons = new ArrayList<>() ;

		Point jobSectionPos = new Point(20, 60) ;
		
		Map<String, IconFunction> jobSectionNamesActions = new LinkedHashMap<>() ;
		jobSectionNamesActions.put("cavaleiro", () -> { playerResetJob(0) ;}) ;
		jobSectionNamesActions.put("mago", () -> { playerResetJob(1) ;}) ;
		jobSectionNamesActions.put("arqueiro", () -> { playerResetJob(2) ;}) ;
		jobSectionNamesActions.put("animal", () -> { playerResetJob(3) ;}) ;
		jobSectionNamesActions.put("ladrão", () -> { playerResetJob(4) ;}) ;
		
		addSection(jobSectionPos, new Point(90, 0), jobSectionNamesActions) ;
		
		IconFunction fullLifePlayerAction = () -> { playerFullLife() ;} ;
		IconFunction fullLifePetAction = () -> { petFullLife() ;} ;
		
		buttons.add(newButton(new Point(100, 160), "encher vida", fullLifePlayerAction)) ;
		buttons.add(newButton(new Point(100, 200), "pet encher vida", fullLifePetAction)) ;
		
		IconFunction petAction = () -> { petReset() ;} ;
		IconFunction plusPetLevelAction = () -> { petLevelUp(1) ;} ;
		IconFunction plus5PetLevelAction = () -> { petLevelUp(5) ;} ;
		IconFunction plus10PetLevelAction = () -> { petLevelUp(10) ;} ;
		
		IconFunction plusLevelAction = () -> { playerLevelUp(1) ;} ;
		IconFunction plus5LevelAction = () -> { playerLevelUp(5) ;} ;
		IconFunction plus10LevelAction = () -> { playerLevelUp(10) ;} ;
		
		IconFunction battleAction = () -> { simulateBattle() ;} ;
		IconFunction battlex10Action = () -> { simulateBattle10x() ;} ;
		IconFunction battlex100Action = () -> { simulateBattle100x() ;} ;
		
		
		buttons.add(newButton(new Point(500, 60), "pet", petAction)) ;
		buttons.add(newButton(new Point(500, 100), "pet+ level", plusPetLevelAction)) ;
		buttons.add(newButton(new Point(500, 140), "pet+ 5 level", plus5PetLevelAction)) ;
		buttons.add(newButton(new Point(500, 180), "pet+ 10 level", plus10PetLevelAction)) ;
		
		buttons.add(newButton(new Point(60, 100), "+ level", plusLevelAction)) ;
		buttons.add(newButton(new Point(160, 100), "+ 5 level", plus5LevelAction)) ;
		buttons.add(newButton(new Point(260, 100), "+ 10 level", plus10LevelAction)) ;
		
		buttons.add(newButton(new Point(460, 220), "Battle", battleAction)) ;
		buttons.add(newButton(new Point(460, 260), "Battle x 10", battlex10Action)) ;
		buttons.add(newButton(new Point(460, 300), "Battle x 100", battlex100Action)) ;
		
	}
	
	private static GameButton newButton(Point pos, String text, IconFunction action)
	{
		return new GameButton(pos, text, buttonImage, buttonSelectedImage, action) ;
	}
	
	private static void addSection(Point pos, Point s, Map<String, IconFunction> sectionNamesActions)
	{
		int i = 0 ;
		Iterator it = sectionNamesActions.entrySet().iterator();
	    while (it.hasNext())
	    {
	        Map.Entry<String, IconFunction> pair = (Map.Entry) it.next();
			Point buttonPos = UtilG.Translate(pos, i * s.x, i * s.y) ;
			buttons.add(newButton(buttonPos, pair.getKey(), pair.getValue())) ;
			i++ ;
	    }
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
		pet.getLife().setToMaximum() ;
		pet.getMp().setToMaximum() ;
		pet.getSatiation().setToMaximum() ;
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
		double[] crit = new double[] {Double.parseDouble(prop.get(newJob)[11]), 0, Double.parseDouble(prop.get(newJob)[12]), 0} ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(prop.get(newJob)[13]), 0, Double.parseDouble(prop.get(newJob)[14]), 0, Integer.parseInt(prop.get(newJob)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(prop.get(newJob)[16]), 0, Double.parseDouble(prop.get(newJob)[17]), 0, Integer.parseInt(prop.get(newJob)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(prop.get(newJob)[19]), 0, Double.parseDouble(prop.get(newJob)[20]), 0, Integer.parseInt(prop.get(newJob)[21]), 0, Integer.parseInt(prop.get(newJob)[22]), 0, Integer.parseInt(prop.get(newJob)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(prop.get(newJob)[24]), 0, Double.parseDouble(prop.get(newJob)[25]), 0, Integer.parseInt(prop.get(newJob)[26]), 0, Integer.parseInt(prop.get(newJob)[27]), 0, Integer.parseInt(prop.get(newJob)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(prop.get(newJob)[29]), 0, Double.parseDouble(prop.get(newJob)[30]), 0, Integer.parseInt(prop.get(newJob)[31])) ;
		LiveBeingStatus status = new LiveBeingStatus() ;
		
		player.setBA(new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, crit, stun, block, blood, poison, silence, status)) ;
		
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
			player.levelUp(null) ;			
		}
	}
	
	private static void petLevelUp(int times)
	{
		if (pet == null) { return ;}
		
		for (int i = 0 ; i <= times - 1 ; i += 1)
		{			
			if (99 <= pet.getLevel()) { break ;}

			pet.getExp().incCurrentValue(pet.getExp().getMaxValue());
			pet.levelUp(null) ;			
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
	
	private static void resetNumberPlayerWins() { numberPlayerWins = 0 ;}
	private static void resetNumberCreatureWins() { numberCreatureWins = 0 ;}
	private static void incNumberPlayerWins() { numberPlayerWins += 1 ;}
	private static void incNumberCreatureWins() { numberCreatureWins += 1 ;}
	
	private static void simulateBattle()
	{
		playerOpponent = new Creature(Game.getCreatureTypes()[playerOpponentID]) ;
		playerOpponent.setPos(player.getPos());
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
	
	private static void train(int amountAtks, AtkResults atkResults, LiveBeing user)
	{
		
		if (user instanceof Creature) { return ;}
			
		for (int i = 0 ; i <= amountAtks - 1 ; i += 1)
		{
			user.train(atkResults) ;
		}
		
	}
	
	public static void playerFight()
	{
		
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

			if (action.equals("Enter") | action.equals("MouseLeftClick"))
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
			if (player.getBag().getTab() == 1 & (player.getCurrentAction().equals("Enter") | player.getCurrentAction().equals("MouseLeftClick")))
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
	
	public static void displayBattleStats(DrawingOnPanel DP)
	{
		int numberOpponentsToPlayerLevelUp = 1 + (int) ((player.getExp().getMaxValue() - player.getExp().getCurrentValue()) / (playerOpponent.getExp().getCurrentValue() * player.getExp().getMultiplier())) ;
		DP.DrawText(new Point(400, 400), Align.centerLeft, DrawingOnPanel.stdAngle, numberOpponentsToPlayerLevelUp + " to level up", font, Game.ColorPalette[1]);

		int barHeight = 50 ;
		Dimension playerExpBarSize = new Dimension(10, (int) (player.getExp().getRate() * barHeight)) ;
		DP.DrawRect(new Point(400, 460), Align.bottomLeft, playerExpBarSize, 1, Game.ColorPalette[1], null) ;
		DP.DrawRect(new Point(400, 460), Align.bottomLeft, new Dimension(10, barHeight), 1, null, Game.ColorPalette[1]) ;
		
		if (pet != null)
		{
			int numberOpponentsToPetLevelUp = 1 + (int) ((pet.getExp().getMaxValue() - pet.getExp().getCurrentValue()) / (playerOpponent.getExp().getCurrentValue() * pet.getExp().getMultiplier())) ;
			DP.DrawText(new Point(450, 420), Align.centerLeft, DrawingOnPanel.stdAngle, numberOpponentsToPetLevelUp + " to pet level up", font, Game.ColorPalette[2]);
			
			Dimension petExpBarSize = new Dimension(10, (int) (pet.getExp().getRate() * barHeight)) ;
			DP.DrawRect(new Point(420, 460), Align.bottomLeft, petExpBarSize, 1, Game.ColorPalette[2], null) ;
			DP.DrawRect(new Point(420, 460), Align.bottomLeft, new Dimension(10, barHeight), 1, null, Game.ColorPalette[2]) ;
		}
		
		DP.DrawText(new Point(100, 400), Align.centerLeft, DrawingOnPanel.stdAngle, "total fights = " + numberFights, font, Game.ColorPalette[1]);
		DP.DrawText(new Point(100, 420), Align.centerLeft, DrawingOnPanel.stdAngle, "player wins = " + numberPlayerWins, font, Game.ColorPalette[1]);
		DP.DrawText(new Point(100, 440), Align.centerLeft, DrawingOnPanel.stdAngle, "creature wins = " + numberCreatureWins, font, Game.ColorPalette[1]);

	}
	
	public static void displayInterface(Point mousePos, DrawingOnPanel DP)
	{
		
		DP.DrawRect(new Point(0, 0), Align.topLeft, Game.getScreen().getSize(), 1, Color.black, null) ;
		buttons.forEach(button -> {
			button.display(0, Align.center, true, mousePos, DP) ;
		});

		playerOpponent.displayName(new Point(460, 340), Align.centerLeft, Color.yellow, DP);
		playerOpponent.display(new Point(460, 360), new Scale(1, 1), DP);
		if (player.isAlive())
		{
			player.display(new Point(380, 360), new Scale(1.8, 1.8), Directions.right, false, DP) ;
		}
		if (pet != null)
		{
			if (pet.isAlive())
			{
				pet.display(new Point(400, 360), new Scale(1, 1), DP) ;
			}
		}
		
		if (player.isInBattle())
		{
			DP.DrawImage(fightingImage, new Point(480, 400), Align.center) ;
		}
		
		displayBattleStats(DP) ;
		
	}
}
