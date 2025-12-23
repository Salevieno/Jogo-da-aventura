package components ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import attributes.PersonalAttributes;
import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import items.Equip;
import items.Item;
import items.Recipe;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import main.Game;
import main.GamePanel;
import main.Path;
import main.TextCategories;
import maps.GameMap;
import utilities.Util;

import windows.BagWindow;
import windows.BankAction;
import windows.BankWindow;
import windows.CraftWindow;
import windows.ElementalWindow;
import windows.ForgeWindow;
import windows.GameWindow;
import windows.ShoppingWindow;
import windows.SpellsTreeWindow;

public class NPC
{
	private int id ;
	private final NPCType type ;
	private final Point pos ;
	private int menu ;
	private int selOption ;
	private int numberMenus ;
	private GameWindow window ;
	private Hitbox hitbox ;
	private final List<Collider> colliders ;
	
	private static boolean renewStocks = false ;

	private static final Font NPCfont = new Font(Game.MainFontName, Font.BOLD, 12) ;
	private static final Image speakingBubble = Game.loadImage(Path.NPC_IMG + "SpeechBubble.png") ;
	private static final Image choicesWindow = Game.loadImage(Path.NPC_IMG + "ChoicesWindow.png") ;
	private static final Color stdColor = Game.palette[0] ;

	public NPC(NPCType type, Point pos)
	{
		
		this.id = 0 ;
		this.type = type ;
		this.pos = pos ;
		selOption = 0 ;
		menu = 0 ;
		numberMenus = 0 ;
		colliders = new ArrayList<>() ;
		hitbox = null ;

		if (type == null) { System.out.println("Erro ao criar npc: tipo nulo") ; return ;}
		
		if (type.getSpeech() != null)
		{
			numberMenus = type.getSpeech().length - 1 ;
		}

		switch (type.getJob())
		{
			case equipsSeller: case itemsSeller: case smuggleSeller:
				break ;
				
			case alchemist:
			{
		    	List<Recipe> recipes = Recipe.all.subList(0, 40) ;
				
				window = new CraftWindow(recipes) ;
				
				break ;
			}
			case woodcrafter:
			{
		    	List<Recipe> recipes = Recipe.all.subList(40, 58) ;
				
				window = new CraftWindow(recipes) ;
				
				break ;
			}
			case crafter:
			{
		    	List<Recipe> recipes = Recipe.all.subList(58, Recipe.all.size()) ;
				
				window = new CraftWindow(recipes) ;
				
				break ;
			}
			case forger:
			{
				window = new ForgeWindow() ;
				
				break ;
			}
			case elemental:
			{
				window = new ElementalWindow() ;

				break ;
			}
			case banker:
			{
				window = new BankWindow() ;

				break ;
			}
			default: window = null ; break ;
		}

		hitbox = new HitboxRectangle(Util.translate(pos, 0, -type.getImage().getHeight(null) / 2), Util.getSize(type.getImage()), 0.8) ;
		colliders.add(new Collider(pos)) ;
	}

	public int getID() {return id ;}
	public void setID(int I) {id = I ;}
	public NPCType getType() {return type ;}
	public Point getPos() {return pos ;}
	public GameWindow getWindow() { return window ;}
	public Hitbox getHitbox() {return hitbox ;}
	public List<Collider> getColliders() { return colliders ;}
	
	
	public static boolean actionIsForward(String action) { return action.equals("Enter") | action.equals("LeftClick") ;}
	
	public static NPCType typeFromName(String name)
	{

		if (Game.getNPCTypes() == null) { System.out.println("Erro ao obter nome do npc. Tipos de NPC não existem") ; return null ;}
		if (Game.getNPCTypes().length == 0) { System.out.println("Erro ao obter nome do npc. Não há nenhum tipo de NPC") ; return null ;}
		
		NPCJobs npcJob = NPCJobs.valueOf(name) ;
		for (NPCType type : Game.getNPCTypes())
		{
			if (!npcJob.equals(type.getJob()))
			{
				continue ;
			}

			return type ;
		}
		
		return null ;
	}

	@SuppressWarnings("unchecked")
	public static void loadText(JSONObject textData, Object key, Map <TextCategories, String[]> allText, TextCategories catName)
	{
		JSONArray npcData = (JSONArray) textData.get(key) ;

		for (int i = 0 ; i <= npcData.size() - 1 ; i += 1)
		{
			JSONObject npc = (JSONObject) npcData.get(i) ;
			String npcName = (String) npc.get("Nome") ;
			List<String> falas = (List<String>) npc.get("Falas") ;
			List<JSONArray> opcoes = (List<JSONArray>) npc.get("Opcoes") ;
			TextCategories textCatName = TextCategories.catFromBRName(catName + "Nome") ;
			TextCategories textCatFala = TextCategories.catFromBRName(catName + npcName + "Falas") ;

			if (textCatName != null)
			{
				allText.put(textCatName, falas.toArray(new String[0])) ;
			}

			if (textCatFala != null)
			{
				allText.put(textCatFala, falas.toArray(new String[0])) ;
			}

			for (int j = 0 ; j <= opcoes.size() - 1 ; j += 1)
			{
				List<String> opcoesMenu = (List<String>) opcoes.get(j) ;
				TextCategories textCatOption = TextCategories.catFromBRName(catName + npcName + "Opcoes" + j) ;

				if (textCatOption == null)
				{
					continue ;
				}

				allText.put(textCatOption, opcoesMenu.toArray(new String[0])) ;
			}
		}
	}
	
	
	public void resetMenu() { menu = 0 ;}
	public void incMenu() { if (menu <= numberMenus - 1) menu += 1 ;}
	public void decMenu() { if (1 <= menu) menu += -1 ;}

//	public boolean isClose(Point target) {return pos.distance(target) <= type.getImage().getWidth(null) ;}
	
	public static NPCType typeFromJob(NPCJobs job) { return Arrays.asList(Game.getNPCTypes()).stream().filter(npcType -> job.equals(npcType.getJob())).toList().get(0) ;}
	public static void setIDs()
	{
		GameMap[] allMaps = Game.getMaps() ;
		int i = 0 ;

		for (GameMap map : allMaps)
		{
			List<NPC> npcsInMap = map.getNPCs() ;

			if (npcsInMap == null) { continue ;}
			if (npcsInMap.isEmpty()) { continue ;}
			
			for (NPC npc : npcsInMap)
			{
				npc.setID(i) ;
				i += 1 ;
			}
		}
	}
	public static int getQuestNPCid(NPC questNPC)
	{
		GameMap[] allMaps = Game.getMaps() ;
		int questId = 0 ;

		for (GameMap map : allMaps)
		{
			List<NPC> npcsInMap = map.getNPCs() ;
			if (npcsInMap == null) { continue ;}
			if (npcsInMap.isEmpty()) { continue ;}
			
			for (int i = 0 ; i <= npcsInMap.size() - 1 ; i += 1)
			{
				NPCJobs npcJob = npcsInMap.get(i).getType().getJob() ;
				if (!npcJob.equals(NPCJobs.questExp) & !npcJob.equals(NPCJobs.questItem)) { continue ;}
				if (!npcsInMap.get(i).equals(questNPC)) { questId += 1; continue ;}
				
				return questId ;
			}
		}
		
		return -1 ;
	}
	public static Quest getQuest(int id)
	{
		GameMap[] allMaps = Game.getMaps() ;
		Quest[] allQuests = Game.getAllQuests() ;
		int questNPCid = 0 ;
		for (GameMap map : allMaps)
		{
			List<NPC> npcsInMap = map.getNPCs() ;
			if (npcsInMap == null) { return null ;}
			if (npcsInMap.isEmpty()) { return null ;}
			
			for (NPC npc : npcsInMap)
			{
				NPCJobs npcJob = npc.getType().getJob() ;
				if (!npcJob.equals(NPCJobs.questExp) & !npcJob.equals(NPCJobs.questItem)) { continue ;}
				if (questNPCid != id) { questNPCid += 1 ; continue ;}
				
				return allQuests[questNPCid] ;
			}
		}
		
		return null ;
	}
	
	private int[] newSmuggledStock()
	{
		List<Integer> fullStock = new ArrayList<>() ;
		for (int i = 0 ; i <= 99 - 1 ; i += 1)
		{
			fullStock.add(200 + i) ;
		}
		int[] newStockIDs = new int[12] ; 
		for (int i = 0 ; i <= newStockIDs.length - 1 ; i += 1)
		{
			int newItem = Util.randomInt(0, fullStock.size() - 1) ;
			newStockIDs[i] = fullStock.get(newItem) ;
			fullStock.remove(newItem) ;
		}
		
		return newStockIDs ;
	}
	
	public static void renewStocks() { renewStocks = true ;}
	
	public void action(Player player, Pet pet, Point mousePos)
	{
				
		String playerAction = player.getCurrentAction() ;
		

		if (playerAction == null) { return ;}
				
		if (window != null) { if (window.isOpen()) { return ;} ;}

		BagWindow playerBag = player.getBag() ;
		switch (type.getJob())
		{
			case alchemist: case woodcrafter: case crafter:
			{
				crafterAction(player, playerBag, playerAction, mousePos, (CraftWindow) window) ;
				
				break ;
			}
			case banker:
			{
				bankerAction(player, (BankWindow) window, playerAction) ;

				break ;
			}
			case caveEntry:
			{
				portalAction(player) ;

				break ;
			}
			case caveExit:
			{
				if (playerBag.contains(Game.getAllItems()[1338])) { portalAction(player) ;}

				break ;
			}
			case doctor: 
			{
				if (pet == null) { doctorAction(playerAction, player.getPA(), null) ;}
				else { doctorAction(playerAction, player.getPA(), pet.getPA()) ;}

				break ;
			}
			case elemental:
			{
				List<Equip> listEquips = new ArrayList<Equip> (playerBag.getEquip().keySet()) ;

				((ElementalWindow) window).setItems(listEquips, ElementalWindow.spheresInBag(playerBag)) ;
				
				elementalAction(player, playerBag, (ElementalWindow) window, player.getCurrentAction()) ;

				break ;
			}
			case equipsSeller:
			{
				int[] itemIDs = new int[] {300, 305, 307, 309, 315, 322, 326, 328, 332, 336, 340, 344} ;
				int cityID = id / 17 ;
				for (int i = 0 ; i <= itemIDs.length - 1; i += 1) { itemIDs[i] += 200 * cityID ;}
//		    	List<Item> itemsOnSale = new ArrayList<>() ;
//		    	for (int itemID : itemIDs) { itemsOnSale.add(Game.getAllItems()[itemID]) ;}
		    	
		    	window = new ShoppingWindow(Item.getItems(itemIDs)) ;
		    	
		    	sellerAction(player, playerAction, (ShoppingWindow) window) ;
		    	
		    	break ;
			}
			case itemsSeller:
				int[] itemIDs = new int[] {1329, 0, 1, 4, 5, 121, 122, 125, 130, 1301, 1305, 1702, 1708, 1710, 1713} ;
//		    	List<Item> itemsOnSale = new ArrayList<>() ;
//		    	for (int itemID : itemIDs) { itemsOnSale.add(Game.getAllItems()[itemID]) ;}
		    	
		    	window = new ShoppingWindow(Item.getItems(itemIDs)) ;
		    	
		    	sellerAction(player, playerAction, (ShoppingWindow) window) ;
		    	
		    	break ;
				
			case smuggleSeller:
				int[] itemids = newSmuggledStock() ;
				if (renewStocks)
				{
					itemids = newSmuggledStock() ;
					renewStocks = false ;
				}
				int cityID = id / 17 ;
				for (int i = 0 ; i <= itemids.length - 1; i += 1)
				{
					itemids[i] += 200 * cityID ;
				}
		    	
		    	window = new ShoppingWindow(Item.getItems(itemids)) ;
		    	
		    	sellerAction(player, playerAction, (ShoppingWindow) window) ;
				
		    	break ;
				
				 
			case forger:
			{
				List<Equip> equipsForForge = new ArrayList<>() ;
				playerBag.getEquip().keySet().forEach(equipsForForge::add) ;
				equipsForForge = equipsForForge.stream().filter(eq -> !Arrays.asList(player.getEquips()).contains(eq)).collect(Collectors.toList());
				((ForgeWindow) window).setItemsForForge(equipsForForge);
				((ForgeWindow) window).setBag(playerBag);
				
				forgerAction(player, playerBag, playerAction, (ForgeWindow) window) ;
				
				break ;
			}
			case master:
			{
				player.getSpellsTreeWindow().setSpells(player.getSpells()) ;
				
				if (50 <= player.getLevel() & player.getProJob() == 0 & menu == 0)
				{
					menu = 2 ;
					String[] proClassesText = Game.allText.get(TextCategories.proclasses) ;
					String proJob1 = proClassesText[2 * player.getJob() + 0] ;
					String proJob2 = proClassesText[2 * player.getJob() + 1] ;
					type.getOptions().set(3, new ArrayList<String>(Arrays.asList(proJob1, proJob2))) ;
				}
				window = player.getSpellsTreeWindow() ;
				
				masterAction(player, player.getCurrentAction(), mousePos, (SpellsTreeWindow) window) ;

				break ;
			}
			case saver:
			{
				saverAction(player, pet, playerAction) ;

				break ;
			}
			case sailorToIsland: case sailorToForest:
			{
				sailorAction(player, playerAction) ;

				break ;
			}
			case questExp: case questItem:
			{
				questAction(player.getQuests(), playerBag, player.getPA(), player.getQuestSkills(), playerAction) ;

				break ;
			}
			
			default: break;			
		}
		navigate(playerAction) ;
		
	}
	
	public void navigate(String action)
	{
		
		switchOption(action) ;

		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ENTER)) && menu <= numberMenus - 1)
		{
			if (type.getOptions().size() == 0) { return ;}
			if (type.getOptions().size() <= menu) { return ;}
			if (type.getOptions().get(menu).isEmpty()) { return ;}
			menu = type.getDestination().get(menu).get(selOption) ;
			selOption = 0 ;
			return ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ESCAPE)) && 0 < menu)
		{
			menu = 0 ;
			selOption = 0 ;
		}
	}
	
	public void switchOption(String action)
	{
		if (type == null) { return ;}
		if (type.getOptions() == null) { return ;}
		if (type.getOptions().size() <= 0) { return ;}
		if (type.getOptions().get(menu) == null) { return ;}
		
		if (action.equals(PlayerActions.moveDown.getKey()) & selOption <= type.getOptions().get(menu).size() - 2)
		{
			selOption += 1 ;
		}
		if (action.equals(PlayerActions.moveUp.getKey()) & 1 <= selOption)
		{
			selOption += -1 ;
		}
	}
	
	public void displaySpeech(Point pos)
	{

		if (type.getSpeech() == null) { return ;}
		if (type.getSpeech().length <= menu) { return ;}
		if (type.getSpeech()[menu].isEmpty()) { return ;}
		if (type.getImage() == null) { return ;}

		String content = type.getSpeech()[menu] ;
		
		if (content == null) { return ;}
		
		Point speechPos = Util.translate(pos, 0, 10 - type.height()) ;

		Draw.speech(speechPos, content, NPCfont, speakingBubble, stdColor) ;
		
	}	
	
	public void displaySpeech() { displaySpeech(pos) ;}

	public void displayOptions(Point windowPos)
	{
		if (type.getOptions() == null) { return ;}
		if (type.getOptions().size() <= menu) { return ;}
		if (type.getOptions().get(menu) == null) { return ;}
		
		List<String> options = type.getOptions().get(menu) ;
		
		if (options == null) { return ;}		
		if (options.size() <= 0) { return ;}
		
		GamePanel.DP.drawImage(choicesWindow, windowPos, Align.topLeft) ;
		
		int sy = NPCfont.getSize() + 5 ;
		for (int i = 0 ; i <= options.size() - 1 ; i += 1)
		{
			Point textPos = Util.translate(windowPos, 5, 5 + i * sy) ;
			String text = options.get(i) ;
			Color textColor = stdColor ;
			if (i == selOption)
			{
				Draw.textSelection(UtilAlignment.getPosAt(textPos, Align.topLeft, Align.center, new Dimension(-45, 8)), new Dimension(45, 8)) ;
			}
			GamePanel.DP.drawText(textPos, Align.topLeft, Draw.stdAngle, text, NPCfont, textColor) ;
		}
		
	}
	
	public void displayOptions() { displayOptions(Util.translate(pos, 20, -10)) ;}

	private void bankerAction(Player player, BankWindow bankWindow, String action)
	{
		if (action == null) { return ;}		
		if (!actionIsForward(action)) { return ;}
		if (menu != 0) { return ;}

		switch (selOption)
		{
			case 0: bankWindow.setMode(BankAction.deposit) ; player.switchOpenClose(bankWindow) ; return ;
			case 1: bankWindow.setMode(BankAction.withdraw) ; player.switchOpenClose(bankWindow) ; return ;
			case 2: bankWindow.setMode(BankAction.investmentLowRisk) ; player.switchOpenClose(bankWindow) ; return ;
			case 3: bankWindow.setMode(BankAction.investmentHighRisk) ; player.switchOpenClose(bankWindow) ; return ;
			default: return ;
		}		
	}

	private void crafterAction(Player player, BagWindow bag, String action, Point mousePos, CraftWindow craftWindow)
	{
		if (action == null) { return ;}

		if (menu == 0 & selOption == 0 & actionIsForward(action))
		{
			craftWindow.setBag(bag) ;
			player.switchOpenClose(craftWindow) ;
		}
	}
	
	private void doctorAction(String action, PersonalAttributes playerPA, PersonalAttributes petPA)
	{

		if (petPA == null & playerPA.getLife().isMaxed())
		{
			menu = 1 ;
			return ;
		}
		if (petPA != null && petPA.getLife().isMaxed() & playerPA.getLife().isMaxed())
		{
			menu = 1 ;
			return ;
		}
		
		if (selOption == 0 & actionIsForward(action))
		{

			if (petPA != null)
			{
				petPA.getLife().setToMaximum() ;
				petPA.getMp().setToMaximum() ;
			}		
			
			playerPA.getLife().setToMaximum() ;
			playerPA.getMp().setToMaximum() ;
		}

	}

	private void elementalAction(Player player, BagWindow bag, ElementalWindow elementalWindow, String action)
	{
		
		if (action == null) { return ;}

		if (menu == 0 & selOption == 0 & actionIsForward(action))
		{
			player.switchOpenClose(elementalWindow) ;
		}	
		
	}
	
	private void forgerAction(Player player, BagWindow bag, String action, ForgeWindow forgeWindow)
	{

		if (action == null) { return ;}

		if (menu == 0 & selOption == 0 & actionIsForward(action))
		{
			player.switchOpenClose(forgeWindow) ;
		}
		
	}

	private void masterAction(Player player, String action, Point mousePos, SpellsTreeWindow spellsTree)
	{
		if (50 <= player.getLevel() & player.getProJob() == 0 & menu == 3)
		{
			if (action == null) { return ;}

			if (actionIsForward(action))
			{
				player.setProJob(1 + selOption) ;
				player.addProSpells() ;
				player.getSpellsTreeWindow().switchTo2Tabs() ;
			}			
		}
		
		if (action == null) { return ;}
	
		if ((menu == 0 | menu == 5) & actionIsForward(action))
		{
//			player.setFocusWindow(spellsTree) ;

			spellsTree.setplayerCurrentSpells(player.getSpells()) ;
			spellsTree.setPoints(player.getSpellPoints()) ;
			spellsTree.updateSpellsOnWindow() ;
			spellsTree.updateSpellsDistribution() ;
			player.switchOpenClose(spellsTree) ;
		}
		
	}

	private void portalAction(Player player)
	{
		// proTODO usar o move to map
		if (player.getMap().getName().equals("Forest 2"))
		{
			player.setMap(Game.getMaps()[30]) ;
			// player.setPos(Util.translate(pos, type.getImage().getWidth(null), 0)) ;
			return ;
		}

		if (player.getMap().getName().equals("Cave 1"))
		{ 
			player.setMap(Game.getMaps()[6]) ;
			// player.setPos(Util.translate(pos, type.getImage().getWidth(null), 0)) ;
			return ;
		}
	}
	
	private void questAction(List<Quest> quests, BagWindow bag, PersonalAttributes PA, Map<QuestSkills, Boolean> skills, String action)
	{

		if (action == null) { return ;}

		int questID = getQuestNPCid(this) ;

		if (questID == -1) { System.out.println("Quest id não encontrado para npc " + type.getName() + " " + id) ; return ;}
		Quest quest = Game.getAllQuests()[questID] ;

		if (action.equals("Enter") & selOption == 0)
		{

			if (!quests.contains(quest))
			{
				quest.checkCompletion(bag) ;
				if (!quest.isRepeatable() & quest.isComplete())
				{
					quest.complete(bag, PA, skills) ;
					return ;
				}
				
				quests.add(quest) ;
			}
			
			quest.checkCompletion(bag) ;
			
			if (!quest.isComplete()) { return ; }
			
			quest.complete(bag, PA, skills) ;
			quests.remove(quest) ;
			
		}
		
	}
	
	private void sailorAction(Player player, String action)
	{// proTODO sailor animation
		if (action == null) { return ;}		

		if (action.equals("Enter") & selOption == 0)
		{
			if (player.getMap().getName().equals("Forest 13"))
			{
				player.setMap(Game.getMaps()[40]) ;
				// player.setPos(Game.getScreen().pos(0.2, 0.8)) ;
				return ;
			}
			if (player.getMap().getName().equals("Island 1"))
			{
				player.setMap(Game.getMaps()[17]) ;
				// player.setPos(Game.getScreen().pos(0.8, 0.8)) ;
				return ;
			}
		}
	}

	private void saverAction(Player player, Pet pet, String action)
	{
		
		if (action == null) { return ;}
		
		if (actionIsForward(action) & menu == 1)
		{
			int slot = selOption + 1 ;
	        player.save(slot) ;
	        Game.setSaveSlotInUse(slot) ;
		}
		
	}
	
	private void sellerAction(Player player, String action, ShoppingWindow shopping)
	{

		if (action == null) { return ;}
		
		if (menu == 0 & actionIsForward(action))
		{
			shopping.setBuyMode(selOption == 0) ;
			if (selOption == 1)
			{
				shopping.setIemsForSellingMode(player.getBag()) ;
			}
			player.switchOpenClose(shopping) ;
		}
		
	}

	public void display(Hitbox playerHitbox)
	{
		
		GamePanel.DP.drawImage(type.getImage(), pos, Draw.stdAngle, Scale.unit, Align.bottomCenter) ;
		if (hitbox.overlaps(playerHitbox))
		{
			Point buttonPos = Util.translate(pos, -type.getImage().getWidth(null), -type.getImage().getHeight(null)) ;
			Draw.keyboardButton(buttonPos, PlayerActions.interact.getKey()) ;
		}
		
		if (Game.debugMode)
		{
			hitbox.display() ;
		}
		
	}

	@Override
	public String toString() { return type.getName() ;}

}