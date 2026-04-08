package main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import NPC.NPC;
import NPC.NPCType;
import UI.ButtonFunction;
import UI.GameButton;
import components.Quest;
import graphics.Align;
import graphics2.SpriteAnimation;
import items.Item;
import items.Recipe;
import liveBeings.CreatureData;
import liveBeings.CreatureType;
import liveBeings.LiveBeing;
import liveBeings.Player;
import maps.CityMap;
import maps.FieldMap;
import maps.GameMap;
import maps.SpecialMap;
import screen.Screen;
import screen.SideBar;
import screen.Sky;
import spells.Buff;
import spells.Spell;
import utilities.Util;

public abstract class LoadingGame
{
    private static int step = 0 ;
    private static final GameButton startButton ;
	private static final SpriteAnimation loadingAni = new SpriteAnimation(Path.OPENING_IMG + "LoadingSprite.png", new Point(), Align.center, 3, 0.2) ;
	private static final SpriteAnimation petIdle = new SpriteAnimation(Path.PET_IMG + "pet0_idle.png", Game.getScreen().getCenter(), Align.center, 4, 0.13) ;

    static
    {
		Point startButtonPos = Util.translate(Game.getScreen().getCenter(), 0, 80) ;
    	Image startImage = ImageLoader.loadImage(Path.OPENING_IMG + "Start.png") ;
    	Image startImageSelected = ImageLoader.loadImage(Path.OPENING_IMG + "StartSelected.png") ;
		ButtonFunction startAction = () -> { step = 12 ;} ;
    	startButton = new GameButton(startButtonPos, Align.center, "start game", startImage, startImageSelected, startAction) ;
    	startButton.deactivate() ;
		petIdle.activate();
    }
    
    
    public static boolean isOver() { return 11 <= step ;}
    
    private static boolean startGameButtonClicked() { return step == 12 ;}
	
	public static void load(Player player, Point mousePos, Languages language)
	{
		String message = switch(step)
		{
			case 0 -> "Loading initial stuff..." ;
			case 1 -> "Loading text..." ;
			case 2 -> "Loading spells..." ;
			case 3 -> "Loading items..." ;
			case 4 -> "Loading creature types..." ;
			case 5 -> "Loading recipes..." ;
			case 6 -> "Loading npc types..." ;
			case 7 -> "Loading building types..." ;
			case 8 -> "Loading quests..." ;
			case 9 -> "Loading maps..." ;
			case 10 -> "Loading final stuff..." ;
			default -> "" ;
		};
		display(player.getCurrentAction(), mousePos, message) ;
		if (!isOver())
		{
			initialize(step, player, language) ;
			step += 1 ;
		}

		if (startButton.isClicked(mousePos, player.getCurrentAction()))
		{
			startButton.act() ;
		}
		
		if (isOver())
		{
			startButton.activateAndSelect() ;
		}

		if (startGameButtonClicked())
		{
			loadingAni.deactivate() ;
			petIdle.deactivate() ;
			startButton.deactivate() ;			
			player.startCounters() ;
			Game.switchToMainState() ;
		}
	}
	
	private static void logInitializationTime(String item, long initialTime)
	{
		long elapsedTime = (long) ((System.nanoTime() - initialTime) * Math.pow(10,  -6)) ;
		Log.info("Loaded " + item + " in " + elapsedTime + " ms") ;
	}

	private static void initialize(int step, Player player, Languages language)
	{
		long initialTime = System.nanoTime();

		switch (step)
		{
			case 0:
				System.out.println();
				Log.info("Game version " + MainGame3_4.getVersion()) ;
				Game.getScreen().setBorders(new int[] { 0 + Screen.borderOffset, Sky.height + Screen.borderOffset,
						Game.getScreen().getSize().width - 60 - Screen.borderOffset, Game.getScreen().getSize().height - Screen.borderOffset });
				Game.getScreen().setMapCenter();
				logInitializationTime("initial stuff", initialTime) ;
				return;

			case 1:
				loadAllText();
				logInitializationTime("text", initialTime);
				return;

			case 2:
				Buff.loadBuffs();
				Buff.loadDebuffs();
				Spell.load(language, Buff.allBuffs, Buff.allDebuffs);
				logInitializationTime("spells", initialTime);
				return;

			case 3:
				Item.load();
				logInitializationTime("items", initialTime);
				return;

			case 4:
				CreatureData.load() ;
				logInitializationTime("creature types", initialTime);
				return;

			case 5:
				Recipe.load(Item.allItems);
				logInitializationTime("recipes", initialTime);
				return;

			case 6:
				Game.setNpcTypes(NPCType.load(language)) ;
				logInitializationTime("npc types", initialTime);
				return;

			case 7:
				logInitializationTime("building types", initialTime);
				return;

			case 8:
				Game.setAllQuests(Quest.load(language, player.getJob(), CreatureType.all, Item.allItems)) ;
				logInitializationTime("quests", initialTime);
				return;

			case 9:
				CityMap[] cityMaps = CityMap.load() ;
				FieldMap[] fieldMaps = FieldMap.load(Game.getNPCTypes());
				SpecialMap[] specialMaps = SpecialMap.load(Item.allItems);
				Game.setCityMaps(cityMaps);
				Game.setFieldMaps(fieldMaps) ;
				Game.setAllMaps(GameMap.assemble(cityMaps, fieldMaps, specialMaps)) ;
				logInitializationTime("maps", initialTime);
				return;

			case 10:
				NPC.setIDs();

				// player.InitializeSpells() ;
				if (player.getSpells().isEmpty())
				{
					player.setSpells(Player.jobSpells(player.getJob()));
				}
				player.setMap(Game.getAllMaps()[player.getJob()]);
				player.setPos(new Point2D.Double(Game.getScreen().getCenter().x, Game.getScreen().getCenter().y));
				player.getMap().activateAnimations() ;
				LiveBeing.updateDamageAnimation(Game.getSettings().getDamageAnimation());
				SideBar.initialize();

				if (Game.getSettings().getMusicIsOn())
				{
					Music.SwitchMusic(player.getMap().getMusic());
				}
				Game.getDayTimer().start();
				logInitializationTime("final stuff", initialTime);


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

			if (catName.equals(TextCategories.npcs))
			{
				NPC.loadText(textData, key, Game.allText, catName);
				continue;
			}

			JSONArray listText = (JSONArray) textData.get(key);

			List<String> listValues = new ArrayList<>();
			for (int j = 0; j <= listText.size() - 1; j += 1)
			{
				listValues.add((String) listText.get(j));
			}

			Game.allText.put(catName, listValues.toArray(new String[] {}));
		}

	}
	
	private static void display(String action, Point mousePos, String message)
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
			GamePanel.DP.drawText(Util.translate(Game.getScreen().getCenter(), 0, 30), Align.center, message, Palette.colors[3]);
		}

		if (startButton.isActive())
		{
			startButton.display(0, true, mousePos) ;
		}

		GamePanel.DP.drawText(Util.translate(Game.getScreen().getCenter(), 0, 160), Align.center, "Game version " + MainGame3_4.getVersion(), Palette.colors[3]);
	}
}
