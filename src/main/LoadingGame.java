package main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import NPC.NPC;
import UI.ButtonFunction;
import UI.GameButton;
import components.Quest;
import graphics.Align;
import graphics2.SpriteAnimation;
import items.Item;
import items.Recipe;
import liveBeings.CreatureData;
import liveBeings.CreatureType;
import liveBeings.Player;
import maps.CityMap;
import maps.FieldMap;
import maps.GameMap;
import maps.SpecialMap;
import sidebar.SideBar;
import spells.Buff;
import spells.Spell;
import utilities.Util;

public abstract class LoadingGame
{
    private static int step = 0 ;
	private static boolean isDone ;
    private static final GameButton startButton ;
	private static final SpriteAnimation loadingAni = new SpriteAnimation(Path.OPENING_IMG + "LoadingSprite.png", new Point(), Align.center, 3, 0.2) ;
	private static final SpriteAnimation petIdle = new SpriteAnimation(Path.PET_IMG + "pet0_idle.png", Game.getScreen().getCenter(), Align.center, 4, 0.13) ;
	private static final List<String> message = List.of(
		"Loading initial stuff...",
		"Loading text...",
		"Loading spells...",
		"Loading items...",
		"Loading creature types...",
		"Loading recipes...",
		"Loading npc types...",
		"Loading building types...",
		"Loading quests...",
		"Loading maps...",
		"Loading final stuff..."
	);

    static
    {
		Point startButtonPos = Util.translate(Game.getScreen().getCenter(), 0, 80) ;
    	Image startImage = ImageLoader.loadImage(Path.OPENING_IMG + "Start.png") ;
    	Image startImageSelected = ImageLoader.loadImage(Path.OPENING_IMG + "StartSelected.png") ;
		ButtonFunction startAction = () ->
		{
			startGame() ;
		} ;
    	startButton = new GameButton(startButtonPos, Align.center, "start game", startImage, startImageSelected, startAction) ;
    	startButton.deactivate() ;
		petIdle.activate();
    }
    
    public static boolean isOver() { return isDone ;}

	private static void startGame()
	{		
		loadingAni.deactivate() ;
		petIdle.deactivate() ;
		startButton.deactivate() ;
		Game.getPlayer().startCounters() ;
		Game.switchToMainState() ;
	}

	public static void load(Player player, String language)
	{
		if (!isOver())
		{
			initialize(step, player, language) ;
			step += 1 ;
		}
		
		if (isOver() && !startButton.isActive())
		{
			if (player.getSpells().isEmpty())
			{
				player.setSpells(Player.jobSpells(player.getJob()));
			}
			player.setMap(GameMap.getAllMaps().get(player.getJob())) ;
			player.getMap().activateAnimations() ;
			SideBar.initialize();

			if (Game.getSettings().getMusicIsOn())
			{
				Music.SwitchMusic(player.getMap().getMusic());
			}
			Game.getDayTimer().start();
			// if (Game.testMode)
			// {
			// 	Game.initializeTestMode();
			// }
			startButton.activateAndSelect() ;
		}
	}
	
	private static void logInitializationTime(String item, long initialTime)
	{
		long elapsedTime = (long) ((System.nanoTime() - initialTime) * Math.pow(10,  -6)) ;
		Log.info("Loaded " + item + " in " + elapsedTime + " ms") ;
	}

	private static void initialize(int step, Player player, String language)
	{
		long initialStepLoadingTime = System.nanoTime();

		switch (step)
		{
			case 0:
				loadAllText();
				logInitializationTime("text", initialStepLoadingTime);
				return;

			case 1:
				Buff.loadBuffs();
				Buff.loadDebuffs();
				Spell.load(language, Buff.getAllBuffs(), Buff.getAllDebuffs());
				logInitializationTime("spells", initialStepLoadingTime);
				return;

			case 2:
				Item.load();
				logInitializationTime("items", initialStepLoadingTime);
				return;

			case 3:
				CreatureData.load() ;
				logInitializationTime("creature types", initialStepLoadingTime);
				return;

			case 4:
				Recipe.load(Item.getAllItems());
				logInitializationTime("recipes", initialStepLoadingTime);
				return;

			case 5:
				NPC.load(language) ;
				logInitializationTime("npc types", initialStepLoadingTime);
				return;

			case 6:
				// Buildings are being created when city is loaded
				logInitializationTime("building types", initialStepLoadingTime);
				return;

			case 7:
				Quest.load(language, player.getJob(), CreatureType.getAll(), Item.getAllItems()) ;
				logInitializationTime("quests", initialStepLoadingTime);
				return;

			case 8:
				CityMap.load() ;
				FieldMap.load();
				SpecialMap.load(Item.getAllItems());
				logInitializationTime("maps", initialStepLoadingTime);
				isDone = true ;
				return;

			default:
				return;
		}

	}

	private static void loadAllText()
	{

		JSONObject textData = Util.readJsonObject(Path.TEXT_BR);

		Iterator<?> iterator = textData.keySet().iterator();

		while (iterator.hasNext())
		{
			Object key = iterator.next();
			TextCategories catName = TextCategories.catFromBRName((String) key);

			if (catName == null) { continue ;}

			JSONArray listText = (JSONArray) textData.get(key);

			List<String> listValues = new ArrayList<>();
			for (int j = 0; j <= listText.size() - 1; j += 1)
			{
				listValues.add((String) listText.get(j));
			}

			Game.allText.put(catName, listValues.toArray(new String[] {}));
		}

	}
	
	protected static void display(Point mousePos)
	{
		GamePanel.DP.drawRect(new Point(0, 0), Align.topLeft, Game.getScreen().getSize(), Palette.colors[0], null) ;
		SpriteAnimation.updateAll();
		petIdle.display(GamePanel.DP);

		if (!isOver())
		{
			Dimension loadingBarSize = new Dimension(400, 30) ;
			Point loadingTextCenter = Util.translate(Game.getScreen().getCenter(), 0, 80) ;
			Point loadingBarCenterLeft = Util.translate(Game.getScreen().getCenter(), -loadingBarSize.width / 2, 80) ;
			Dimension loadedBarSize = new Dimension(step * loadingBarSize.width / 11, loadingBarSize.height) ;

			GamePanel.DP.drawRoundRect(loadingBarCenterLeft, Align.centerLeft, loadingBarSize, 2, null, Palette.colors[0], true);
			GamePanel.DP.drawRoundRect(loadingBarCenterLeft, Align.centerLeft, loadedBarSize, 1, Palette.colors[18], Palette.colors[0], false);
			loadingAni.setPos(loadingTextCenter) ;
			loadingAni.activateIfInactive() ;
			loadingAni.display(GamePanel.DP);
			GamePanel.DP.drawText(Util.translate(Game.getScreen().getCenter(), 0, 30), Align.center, message.get(step), Palette.colors[3]);
		}

		if (startButton.isActive())
		{
			startButton.display(0, true, mousePos) ;
		}

		GamePanel.DP.drawText(Util.translate(Game.getScreen().getCenter(), 0, 160), Align.center, "Game version " + MainGame3_4.getVersion(), Palette.colors[3]);
	}
}
