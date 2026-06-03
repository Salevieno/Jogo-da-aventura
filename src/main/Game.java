package main;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Buildings.Building;
import UI.GameButton;
import UI.GameTextButton;
import animations.Animation;
import battle.AtkTypes;
import battle.Battle;
import components.Projectiles;
import graphics.Align;
import graphics2.Draw;
import graphics2.Drawable;
import graphics2.SpriteAnimation;
import liveBeings.Creature;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import maps.CityMap;
import maps.Continents;
import maps.FieldMap;
import screen.Screen;
import sidebar.HotKeysBar;
import sidebar.SideBar;
import simulations.EvolutionSimulation;
import utilities.Util;
import windows.BankWindow;

public class Game
{
	// TODO arquivos - nomes das criaturas
	// TODO optional - unificar throw item, calcPhysicalAtk e useSpell dos liveBeings
	// TODO ideia - magias e flechas são projéteis que precisam acertar o oponente para dar dano. O oponente pode se mover durante a luta E usar magias de longe enquanto se move (ou defender)
	// TODO ideia - todo personagem pode inspecionar para aprender 1 att ou 2 da criatura, mas tem que estar perto e isso pode provocar certas criaturas meio agressivas
	// TODO shopping de cada cidade vender itens diferentes

	private GameStates state ;
	private Languages gameLanguage ;
	private boolean shouldRepaint; // tells if the panel should be repainted, responding to multiple requests only once
	private boolean konamiCodeIsActive;
	private double dt ;	
	private Player player;
	private Pet pet;
	private int difficultLevel ; // TODO use
	private int saveSlotInUse ;	
	private GameTimer dayTimer ;
	private final Map<TextCategories, String[]> allText ;	
    private final Settings settings ;
	
	private static final String MAIN_FONT_NAME = "Comics";
	private static final Font STD_FONT = new Font(MAIN_FONT_NAME, Font.PLAIN, 16) ;
	private static final List<String> ARROW_KEYS = List.of(KeyEvent.getKeyText(KeyEvent.VK_UP),
															KeyEvent.getKeyText(KeyEvent.VK_LEFT),
															KeyEvent.getKeyText(KeyEvent.VK_DOWN),
															KeyEvent.getKeyText(KeyEvent.VK_RIGHT)) ;
	private static final List<String> KONAMI_CODE = List.of("Up", "Up", "Down", "Down", "Left", "Right", "Left", "Right", "B", "A") ;
	private static final GameStates MAIN_STATE = GameStates.running;
	// private static final boolean testMode = true;
	public static final boolean DEBUG_MODE = false;
	private static final int DAY_DURATION = 600 ;
	private static Game game ;

	private Game()
	{
		System.out.println();
		Log.info("Game version " + MainGame3_4.getVersion()) ;	
		this.allText = new HashMap<>();
		this.settings = new Settings(false, true, false, 1, 0) ;
		this.player = new Player("", "", 2);
		this.player.setPos(new Point2D.Double(Screen.getMe().getCenter().x, Screen.getMe().getCenter().y));
		this.difficultLevel = 0;
		this.saveSlotInUse = -1;
		this.state = GameStates.opening;
		this.gameLanguage = Languages.portugues;
		this.dayTimer = new GameTimer(DAY_DURATION);
		this.dt = System.nanoTime() ;
	}

	protected static void create()
	{
		game = new Game() ;
	}

	protected static Game getMe() { return game ;}

	public static String getMainFontName() { return MAIN_FONT_NAME ;}
	public static GameStates getState() { return game.state ;}
	public static Languages getLanguage() { return game.gameLanguage ;}
	public static Settings getSettings() { return game.settings ;}
	public static Player getPlayer() { return game.player ;}
	public static Pet getPet() { return game.pet ;}
	public static List<String> getArrowKeys() { return ARROW_KEYS ;}
	public static boolean getShouldRepaint() { return game.shouldRepaint ;}
	public static Map<TextCategories, String[]> getAllText() { return game.allText ;}
	public static void setDifficultLevel(int difficultLevel) { game.difficultLevel = difficultLevel ;}
	public static int getSaveSlotInUse() { return game.saveSlotInUse ;}
	public static void setSaveSlotInUse(int newSaveSlotInUse) { game.saveSlotInUse = newSaveSlotInUse ;}
	public static void setPlayer(Player newPlayer) { game.player = newPlayer ;}
	public static void setState(GameStates newState) { game.state = newState ;}
	public static GameTimer getDayTimer() { return game.dayTimer ;}

	public static void switchToMainState() { setState(MAIN_STATE) ;}
	public static double dayTimeRate() { return game.dayTimer.rate() <= 0.5 ? game.dayTimer.rate() + 0.5 : game.dayTimer.rate() - 0.5 ;}

	private static void activateKonamiEffect()
	{
		Palette.switchToKonami() ;
		game.dayTimer.setDuration(DAY_DURATION / 100.0);
	}

	private static void deactivateKonamiEffect()
	{
		Palette.switchToNormal() ;
		game.dayTimer.setDuration(DAY_DURATION);
		Draw.stdAngle = 0;
	}

	private static void checkKonamiCode(List<String> combo)
	{
		if (!KONAMI_CODE.equals(combo)) { return ;}

		game.konamiCodeIsActive = !game.konamiCodeIsActive;
		if (game.konamiCodeIsActive)
		{
			activateKonamiEffect() ;
		}
		else
		{
			deactivateKonamiEffect() ;
		}
		game.player.resetCombo();
	}

	private static void applyKonamiEffect(double dt)
	{
		if (game.dayTimer.rate() <= 0.25)
		{
			Draw.stdAngle += 0.04;
		}
		else if (game.dayTimer.rate() <= 0.75)
		{
			Draw.stdAngle -= 0.04;
		}
		else
		{
			Draw.stdAngle += 0.04;
		}
	}

	public static void removePet()
	{
		game.pet = null;
	}

	public static void letThereBePet()
	{
		int job = Util.randomInt(0, 3);
		game.pet = new Pet(job);
		game.pet.setPos(game.player.getPosAsDouble());
		if (game.player.getJob() == 3 & 0 < game.player.getSpells().get(13).getLevel()) // Best friend
		{
			int spellLevel = game.player.getSpells().get(13).getLevel();
			game.pet.getPA().getLife().incMaxValue(10 * spellLevel);
			game.pet.getPA().getLife().setToMaximum();
			game.pet.getPA().getMp().incMaxValue(10 * spellLevel);
			game.pet.getPA().getMp().setToMaximum();
			game.pet.getBA().getPhyAtk().incBaseValue(2 * spellLevel);
			game.pet.getBA().getMagAtk().incBaseValue(2 * spellLevel);
			game.pet.getBA().getDex().incBaseValue(1 * spellLevel);
			game.pet.getBA().getAgi().incBaseValue(1 * spellLevel);
		}
		SideBar.addPetButton(game.player, game.pet);
	}

	// private static void initializeTestMode()
	// {
	// 	player.setName("Rosquinhawwwwwwwwwwwwwww");
	// 	player.setMap(CityMap.getAllCityMaps().get(3));

	// 	for (int i = 0; i <= FieldMap.getAllFieldMaps().size() - 1; i += 1)
	// 	{
	// 		player.discoverCreature(FieldMap.getAllFieldMaps().get(i).getCreatures().get(0).getType());
	// 	}

	// 	player.getPA().getLife().incMaxValue(1000);
	// 	player.getPA().getMp().incMaxValue(1000);
	// 	player.getBA().getDex().incBaseValue(1000);
	// 	player.getPA().getLife().setToMaximum();

	// 	// player.getBA().getStun().incAtkChance(1) ;
	// 	player.getBA().getStun().incDuration(1);

	// 	// player.getBA().getBlock().incAtkChance(1) ;
	// 	player.getBA().getBlock().incDuration(1);

	// 	// player.getBA().getBlood().incAtkChance(1) ;
	// 	player.getBA().getBlood().incAtkBonus(8);
	// 	player.getBA().getBlood().incDefBonus(1);
	// 	player.getBA().getBlood().incDuration(1);

	// 	// player.getBA().getPoison().incAtkChance(1) ;
	// 	player.getBA().getPoison().incAtkBonus(1);
	// 	player.getBA().getPoison().incDefBonus(1);
	// 	player.getBA().getPoison().incDuration(1);

	// 	// player.getBA().getSilence().incAtkChance(1) ;
	// 	player.getBA().getSilence().incDuration(1);

	// 	player.takeDamage(500);
	// 	player.getBag().addGold(30000);
	// 	for (Spell spell : player.getSpells())
	// 	{
	// 		spell.incLevel(5);
	// 	}
	// 	SpellsBar.updateSpells(player.getActiveSpells());

	// 	//
	// 	for (Item item : Potion.getAll())
	// 	{
	// 		player.getBag().add(item, 10);
	// 	}
	// 	for (Item item : Alchemy.getAll())
	// 	{
	// 		player.getBag().add(item, 20);
	// 	}
	// 	for (Item item : Forge.getAll())
	// 	{
	// 		player.getBag().add(item, 3);
	// 	}
	// 	for (Item item : PetItem.getAll())
	// 	{
	// 		player.getBag().add(item, 2);
	// 	}
	// 	for (Item item : Food.getAll())
	// 	{
	// 		player.getBag().add(item, 10);
	// 	}
	// 	for (Item item : Arrow.getAll())
	// 	{
	// 		player.getBag().add(item, 20);
	// 	}
	// 	for (Item item : Equip.getAll())
	// 	{
	// 		player.getBag().add(item, 20) ;
	// 	}
	// 	for (Item item : GeneralItem.getAll())
	// 	{
	// 		player.getBag().add(item, 2);
	// 	}
	// 	for (Item item : Fab.getAll())
	// 	{
	// 		player.getBag().add(item, 10);
	// 	}
	// 	for (Item item : QuestItem.getAll())
	// 	{
	// 		player.getBag().add(item, 10);
	// 	}
	// 	player.getBag().add(Equip.getAll()[0], 20);
	// 	player.getBag().add(Equip.getAll()[1], 20);
	// 	player.getBag().add(Equip.getAll()[2], 20);
	// 	player.getBag().add(Equip.getAll()[100], 20);
	// 	player.getBag().add(Equip.getAll()[102], 20);
	// 	player.getBag().add(Equip.getAll()[110], 20);
	// 	player.getBag().add(Equip.getAll()[111], 20);
	// 	player.getBag().add(Equip.getAll()[112], 20);
	// 	player.getBag().add(Equip.getAll()[115], 20);
	// 	player.getBag().add(Equip.getAll()[116], 20);
	// 	player.getBag().add(Equip.getAll()[117], 20);
	// 	player.getBag().add(Equip.getAll()[121], 20);
	// 	player.getBag().add(Equip.getAll()[122], 20);
	// 	player.getBag().add(Equip.getAll()[123], 20);
	// 	for (QuestSkills skill : QuestSkills.values())
	// 	{
	// 		player.getQuestSkills().replace(skill, true);
	// 	}
	// 	Quest.getAll().forEach(quest -> player.addQuest(quest));
	// }

	private void activateCounters()
	{
		if (dayTimer.hasFinished())
		{
			dayTimer.restart();
			// NPC.renewStocks();
		}
		player.activateSpellCounters();

		if (pet != null)
		{
			pet.activateSpellCounters();
		}

		if (player.getMap().isField())
		{
			FieldMap fm = (FieldMap) player.getMap();
			fm.getCreatures().forEach(Creature::activateSpellCounters);
		}

		for (CityMap city : CityMap.getAllCityMaps())
		{
			Building bank = city.getBank();

			if (!bank.hasNPCs())
			{
				continue;
			}

			BankWindow bankWindow = (BankWindow) bank.getNPCs().get(0).getWindow();
			if (bankWindow != null && bankWindow.hasInvestment() && bankWindow.investmentIsComplete())
			{
				bankWindow.completeInvestment();
			}
		}

		player.activateCounters();

		if (pet != null)
		{
			if (0 < pet.getLife().getCurrentValue())
			{
				pet.activateCounters();
			}
		}

		if (!player.getMap().isField())
		{
			return;
		}

		FieldMap fm = (FieldMap) player.getMap();
		fm.getCreatures().forEach(Creature::activateCounters);
		fm.activateCollectiblesCounter();

	}

	private void creaturesAct(double dt)
	{
		List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures();
		for (Creature creature : creaturesInMap)
		{
			if (creature.getBattleActionCounter().hasFinished() && AtkTypes.defense.equals(creature.getCurrentAtkType()))
			{
				creature.deactivateDef() ;
				creature.setCurrentAtkType(null);
			}
			creature.takeBloodAndPoisonDamage();
			creature.act(player.getPosAsDouble(), player.getMap(), dt);
		}
	}

	private void petActs(double dt)
	{
		if (!pet.isAlive())
		{
			return;
		}

		pet.takeBloodAndPoisonDamage();
		pet.updateCombo();
		pet.think(player.isFighting(), player.getPosAsDouble());
		pet.act(player, dt);
	}

	private void playerActs(double dt)
	{
		player.takeBloodAndPoisonDamage();
		player.updateBloodAndPoisonStatistics();
		if (player.canAct() && player.hasActed())
		{
			player.act(pet, GamePanel.getMousePos());
		}

		player.doCurrentAction();
		player.applyAdjacentGroundEffect();
		player.finishStatus();

		if (player.isMoving() && !player.isCollecting())
		{
			player.move(pet, dt);
		}
		player.updateCombo() ;
	}

	private void run(double dt)
	{
		if (Math.pow(10, 1) <= dt) { return ;}

		Screen.getMe().updateSky(dt);
		activateCounters();
		
		// TODO mover as verificações de batalha pra cá e unir às gerais
		if (player.getBattleActionCounter().hasFinished() & player.getCurrentAtkType() != null)
		{
			if (player.getCurrentAtkType().equals(AtkTypes.defense))
			{
				player.deactivateDef() ;
				player.setCurrentAtkType(null);
			}
		}

		checkKonamiCode(player.getCombo());
		if (konamiCodeIsActive)
		{
			applyKonamiEffect(dt);
		}

		if (player.getMap().isField())
		{
			creaturesAct(dt);
			shouldRepaint = true;
		}

		if (pet != null && pet.isAlive())
		{
			if (pet.getBattleActionCounter().hasFinished() && AtkTypes.defense.equals(pet.getCurrentAtkType()))
			{
				pet.deactivateDef() ;
			}
			petActs(dt);
		}

		playerActs(dt);

		player.checkMeet(GamePanel.getMousePos());

		if (player.isFighting() && player.getOpponent() != null)
		{
			if (Battle.isOver(player, pet, player.getOpponent()))
			{
				Battle.finishBattle(player, pet, player.getOpponent());
			}
		}

		if (player.getMap().isField())
		{
			player.setClosestCreature(player.closestCreatureInRange());
		}

		if (player.shouldLevelUP())
		{
			player.levelUp();
		}

		if (pet != null)
		{
			if (pet.shouldLevelUP())
			{
				pet.levelUp();
			}
		}

		if (player.isInContactWithNPC() && player.getNPCInContact().isInteracting())
		{
			// player.getNPCInContact().act(player, pet, player.getCurrentAction()) ;
// 		if (playerAction == null) { return ;}				
// 		if (window != null && window.isOpen()) { return ;}
			player.getNPCInContact().navigate(player.getCurrentAction()) ;
		}

		Projectiles.updateAll(player, pet);

		if (player.getJob() == 3)
		{
			player.useAutoSpell(true, player.getSpells().get(12));
		}

		player.resetAction();

		SpriteAnimation.updateAll();

	}

	private void draw()
	{
		if (!player.getMap().getContinent().equals(Continents.cave))
		{
			Screen.getMe().displaySky();
		}
		player.getMap().display();
		player.getMap().displayGroundTypes();
		Draw.mapElements(player.getHitbox(), player.getPos(), player.getMap(), STD_FONT);
		
		SpriteAnimation.displayAllFromLayer(0, GamePanel.getDP());

		List<Drawable> drawables = new ArrayList<>();

		if (player.getMap().isField())
		{
			List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures();
			for (Creature creature : creaturesInMap)
			{
				drawables.add(creature);
			}
		}

		if (pet != null)
		{
			drawables.add(pet);
		}

		drawables.add(player);
		if (player.getMap().getBuildings() != null)
		{
			player.getMap().getBuildings().forEach(building -> drawables.add(building));
		}
		player.getMap().getMapElem().forEach(mapElem -> drawables.add(mapElem));
		drawables.sort(Comparator.comparingInt(d -> d.getPos().y));
		drawables.forEach(Drawable::display);
		if (player.getMap() instanceof CityMap)
		{
			((CityMap) player.getMap()).getSign().display(player.getPos()) ;
		}

		if (player.isInContactWithNPC() && player.getNPCInContact().isInteracting())
		{
			player.getNPCInContact().displaySpeech();
			player.getNPCInContact().displayOptions();
		}

		player.showWindows(pet, GamePanel.getMousePos());

		if (player.getBag().getItemFetched() != null)
		{
			GamePanel.getDP().drawImage(player.getBag().getItemFetched().getImage(), GamePanel.getMousePos(), Align.center);
		}

		for (Interactable interactable : player.getMap().getInteractables())
		{
			if (interactable.getHitbox().overlaps(player.getHitbox()))
			{				
				Point buttonPos = Util.translate(interactable.getPos(), -interactable.getImage().getWidth(null), -interactable.getImage().getHeight(null)) ;
				Draw.keyboardButton(buttonPos, PlayerActions.interact.getKey(), STD_FONT) ;
			}
		}

		Animation.playAll();
		SpriteAnimation.displayAllFromLayer(1, GamePanel.getDP());

		SideBar.display(player, pet, GamePanel.getMousePos());

		if (DEBUG_MODE)
		{
			Screen.getMe().displayBorders();
		}
	}

	protected void update()
	{		
		GameTimer.updateAll() ;
		GameButton.updateMouseCursor(GamePanel.getMousePos());
		GameButton.updateSelected(GamePanel.getMousePos());
		clickedButtonActs(GamePanel.getMousePos(), player.getCurrentAction()) ;

		switch (state)
		{
			case opening:
				Opening.run(player, GamePanel.getMousePos());
				shouldRepaint = true;
				return;

			case loading:
				LoadingGame.load(player, settings.getLanguage());
				LoadingGame.display(GamePanel.getMousePos()) ;

				shouldRepaint = true;
				return;

			case simulation:
				EvolutionSimulation.run(GamePanel.getMousePos());
				return;

			case running:
				run((System.nanoTime() - dt) / Math.pow(10, 9));
				dt = System.nanoTime();
				draw();
				return;

			case paused:
				return;
			default:
				return;
		}
	}

	private void clickedButtonActs(Point mousePos, String action)
	{
		if (action == null) { return ;}

		for (GameButton button : GameButton.getALL())
		{
			if (button.isActive() && button.isSelected() && (button.isClicked(mousePos, action) || action.equals("Enter")))
			{	
				button.act() ;
				player.resetAction() ;
				return ;
			}
		}
		
	}

	private String keypadNumberValue(int keyCode)
	{
		return String.valueOf(keyCode - 96);
	}

	private void switchPauseRunning()
	{
		if (state.equals(GameStates.paused))
		{
			GameFrame.resumeGame();
			GameTimer.resumeAll();
		} else
		{
			GameFrame.pauseGame();
			GameTimer.stopAll();
		}
	}

	protected static void requestTextResize()
	{
		GameTextButton.updateAllTextSize() ;
	}

	protected void keyPressedAction(KeyEvent event)
	{
		int keyCode = event.getKeyCode();
		if (KeyEvent.VK_NUMPAD0 <= keyCode & keyCode <= KeyEvent.VK_NUMPAD9)
		{
			player.setCurrentAction(keypadNumberValue(keyCode));
			return;
		}

		if (keyCode == KeyEvent.VK_PAUSE)
		{
			switchPauseRunning();
		}

		if (keyCode == KeyEvent.VK_F12)
		{
			GameFrame.getMe().switchFullscreen();
		}

		if (ARROW_KEYS.contains(KeyEvent.getKeyText(keyCode)))
		{
			player.addKeyPressed(KeyEvent.getKeyText(keyCode));
			if (player.isMoving())
			{
				player.updateDirection(KeyEvent.getKeyText(keyCode));
			}
		}

		player.setCurrentAction(KeyEvent.getKeyText(keyCode));
	}

	protected void keyReleasedAction(KeyEvent event)
	{
		int keyCode = event.getKeyCode();
		if (ARROW_KEYS.contains(KeyEvent.getKeyText(keyCode)))
		{
			player.removeKeyPressed(KeyEvent.getKeyText(keyCode));
		}
	}

	protected void mouseClickedAction(MouseEvent evt)
	{
	}

	protected void mousePressedAction(MouseEvent evt)
	{
		if (!GameStates.running.equals(state))
		{
			return;
		}

		if (player.getBag().isOpen())
		{
			player.getBag().setItemFetched(player.getBag().itemHovered(GamePanel.getMousePos()));
		}
	}

	protected void mouseReleaseAction(MouseEvent evt)
	{
		if (evt.getButton() == 1)
		{
			player.setCurrentAction("LeftClick");
		}
		if (evt.getButton() == 3)
		{
			player.setCurrentAction("MouseRightClick");
			player.setPos(new Point2D.Double(GamePanel.getMousePos().x, GamePanel.getMousePos().y));
			// Log.attributes(player) ;
			if (pet != null)
			{
				pet.setPos(player.getPosAsDouble());
			}
		}

		if (!GameStates.running.equals(state))
		{
			return;
		}

		int hotKeySlotHovered = HotKeysBar.slotHovered(GamePanel.getMousePos());

		if (-1 < hotKeySlotHovered)
		{
			player.getHotItems().set(hotKeySlotHovered, player.getBag().getItemFetched());
		}

		player.getBag().setItemFetched(null);
	}

	protected void mouseWheelAction(MouseWheelEvent evt)
	{
		if (evt.getWheelRotation() < 0)
		{
			player.setCurrentAction("MouseWheelUp");
		} else
		{
			player.setCurrentAction("MouseWheelDown");
		}
	}

}