package components ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import attributes.PersonalAttributes;
import graphics.DrawingOnPanel;
import items.Equip;
import items.Item;
import items.Recipe;
import liveBeings.Pet;
import liveBeings.Player;
import main.Game;
import main.TextCategories;
import maps.GameMap;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;
import windows.BagWindow;
import windows.BankWindow;
import windows.CraftWindow;
import windows.ElementalWindow;
import windows.ForgeWindow;
import windows.GameWindow;
import windows.ShoppingWindow;
import windows.SpellsTreeWindow;

public class NPCs
{
	private int id ;
	private NPCType type ;
	private Point pos ;
	private int selOption ;
	private int numberMenus ;
	private int menu ;
	private GameWindow window ;
	private List<Collider> colliders ;

	public static final Font NPCfont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	public static final Image SpeakingBubble = UtilG.loadImage(Game.ImagesPath + "\\NPCs\\" + "SpeechBubble.png") ;
	public static final Image ChoicesWindow = UtilG.loadImage(Game.ImagesPath + "\\NPCs\\" + "ChoicesWindow.png") ;

	public NPCs(NPCType type, Point pos)
	{
		this.id = 0 ;
		this.type = type ;
		this.pos = pos ;
		selOption = 0 ;
		menu = 0 ;
		numberMenus = 0 ;

		if (type.getSpeech() != null) { numberMenus = type.getSpeech().length - 1 ;}
		
		switch (type.getJob())
		{
			case equipsSeller:
			{
				int[] itemIDs = new int[] {300, 305, 307, 309, 315, 322, 326, 328, 332, 336, 340, 344} ;
				int cityID = id / 17 ;
				for (int i = 0 ; i <= itemIDs.length - 1; i += 1) { itemIDs[i] += 200 * cityID ;}
		    	List<Item> itemsOnSale = new ArrayList<>() ;
		    	for (int itemID : itemIDs) { itemsOnSale.add(Game.getAllItems()[itemID]) ;}
		    	
		    	window = new ShoppingWindow(itemsOnSale) ;
		    	
		    	break ;
			}
			case itemsSeller:
			{
				int[] itemIDs = new int[] {0, 1, 4, 5, 121, 122, 125, 130, 1301, 1305, 1702, 1708, 1710, 1713} ;
		    	List<Item> itemsOnSale = new ArrayList<>() ;
		    	for (int itemID : itemIDs) { itemsOnSale.add(Game.getAllItems()[itemID]) ;}
		    	
		    	window = new ShoppingWindow(itemsOnSale) ;
		    	
		    	break ;
			}
			case smuggleSeller:
			{
				// TODO smuggle seller renew stock
				int[] itemIDs = new int[] {400, 405, 407, 409, 415, 422, 426, 428, 432, 436, 440, 444} ;
				int cityID = id / 17 ;
				for (int i = 0 ; i <= itemIDs.length - 1; i += 1) { itemIDs[i] += 200 * cityID ;}
		    	List<Item> itemsOnSale = new ArrayList<>() ;
		    	for (int itemID : itemIDs) { itemsOnSale.add(Game.getAllItems()[itemID]) ;}
		    	
		    	window = new ShoppingWindow(itemsOnSale) ;
		    	
		    	break ;
			}
			case alchemist:
			{
		    	List<Recipe> recipes = Game.getAllRecipes().subList(0, 39) ;
				
				window = new CraftWindow(recipes) ;
				
				break ;
			}
			case woodcrafter:
			{
		    	List<Recipe> recipes = Game.getAllRecipes().subList(40, 59) ;
				
				window = new CraftWindow(recipes) ;
				
				break ;
			}
			case crafter:
			{
		    	List<Recipe> recipes = Game.getAllRecipes().subList(60, Game.getAllRecipes().size()) ;
				
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
		
		colliders = new ArrayList<>() ;
		colliders.add(new Collider(pos)) ;
	}

	public int getID() {return id ;}
	public void setID(int I) {id = I ;}
	public NPCType getType() {return type ;}
	public Point getPos() {return pos ;}
	public void setPos(Point P) {pos = P ;}	
	public GameWindow getWindow() { return window ;}
	public List<Collider> getColliders() { return colliders ;}
	
	public void resetMenu() { menu = 0 ;}
	public void incMenu() { if (menu <= numberMenus - 1) menu += 1 ;}
	public void decMenu() { if (1 <= menu) menu += -1 ;}

	public static NPCType typeFromJob(NPCJobs job) { return Arrays.asList(Game.getNPCTypes()).stream().filter(npcType -> job.equals(npcType.getJob())).toList().get(0) ;}
	public static void setIDs()
	{
		GameMap[] allMaps = Game.getMaps() ;
		int i = 0 ;

		for (GameMap map : allMaps)
		{
			List<NPCs> npcsInMap = map.getNPCs() ;

			if (npcsInMap == null) { continue ;}
			if (npcsInMap.isEmpty()) { continue ;}
			
			for (NPCs npc : npcsInMap)
			{
				npc.setID(i) ;
				i += 1 ;
			}
		}
	}
	public static int getQuestNPCid(NPCs questNPC)
	{
		GameMap[] allMaps = Game.getMaps() ;
		int questId = 0 ;

		for (GameMap map : allMaps)
		{
			List<NPCs> npcsInMap = map.getNPCs() ;
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
			List<NPCs> npcsInMap = map.getNPCs() ;
			if (npcsInMap == null) { return null ;}
			if (npcsInMap.isEmpty()) { return null ;}
			
			for (NPCs npc : npcsInMap)
			{
				NPCJobs npcJob = npc.getType().getJob() ;
				if (!npcJob.equals(NPCJobs.questExp) & !npcJob.equals(NPCJobs.questItem)) { continue ;}
				if (questNPCid != id) { questNPCid += 1 ; continue ;}
				
				return allQuests[questNPCid] ;
			}
		}
		
		return null ;
	}
	
	public void action(Player player, Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		
		speak(pos, DP) ;
		drawOptions(UtilG.Translate(pos, 20, -10), DP) ;
				
		String playerAction = player.getCurrentAction() ;
		

		if (playerAction == null) { return ;}
				
		if (window != null) { if (window.isOpen()) { return ;} ;}

		BagWindow playerBag = player.getBag() ;
		switch (type.getJob())
		{
			case alchemist: case woodcrafter: case crafter:
			{
				crafterAction(player, playerBag, playerAction, mousePos, (CraftWindow) window, DP) ;
				
				break ;
			}
			case banker:
			{
				bankerAction(player, (BankWindow) window, playerAction, DP) ;

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
				((ElementalWindow) window).setEquipsForElemChange(listEquips) ;
				((ElementalWindow) window).setSpheres(ElementalWindow.spheresInBag(playerBag)) ;
				
				elementalAction(player, playerBag, (ElementalWindow) window, player.getCurrentAction(), DP) ;

				break ;
			}
			case equipsSeller: case itemsSeller: case smuggleSeller:
			{
		    	sellerAction(player, playerAction, (ShoppingWindow) window, DP) ;
		    	
		    	break ;
			}
			case forger:
			{
				List<Equip> equipsForForge = new ArrayList<>() ;
				playerBag.getEquip().keySet().forEach(equipsForForge::add) ;
				((ForgeWindow) window).setItemsForForge(equipsForForge);
				
				forgerAction(player, playerBag, playerAction, (ForgeWindow) window, DP) ;
				
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
				
				masterAction(player, player.getCurrentAction(), mousePos, (SpellsTreeWindow) window, DP) ;

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

		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ENTER)) & menu <= numberMenus - 1)
		{
			if (type.getOptions().size() == 0) { return ;}
			if (type.getOptions().size() <= menu) { return ;}
			if (type.getOptions().get(menu).isEmpty()) { return ;}
			menu = type.getDestination().get(menu).get(selOption) ;
			selOption = 0 ;
			return ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ESCAPE)) & 0 < menu)
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
		
		if (action.equals(Player.ActionKeys[2]) & selOption <= type.getOptions().get(menu).size() - 2)
		{
			selOption += 1 ;
		}
		if (action.equals(Player.ActionKeys[0]) & 1 <= selOption)
		{
			selOption += -1 ;
		}
	}
	
	public void speak(Point pos, DrawingOnPanel DP)
	{
		
		if (type.getSpeech() == null) { return ;}
		if (type.getSpeech().length <= menu) { return ;}
		if (type.getSpeech()[menu].equals("")) { return ;}
		if (type.getImage() == null) { return ;}
		
		String content = type.getSpeech()[menu] ;
		
		if (content == null) { return ;}
		
		Point speechPos = UtilG.Translate(pos, -22, -2 - type.getImage().getHeight(null)) ;

		DP.DrawSpeech(speechPos, content, NPCfont, SpeakingBubble, Game.colorPalette[9]) ;
		
	}

	public void drawOptions(Point windowPos, DrawingOnPanel DP)
	{
		if (type.getOptions() == null) { return ;}
		if (type.getOptions().size() <= menu) { return ;}
		if (type.getOptions().get(menu) == null) { return ;}
		
		List<String> options = type.getOptions().get(menu) ;
		Color selColor = Game.colorPalette[3] ;
		
		if (options == null) { return ;}		
		if (options.size() <= 0) { return ;}
		
		DP.DrawImage(ChoicesWindow, windowPos, Align.topLeft) ;
		
		int sy = NPCfont.getSize() + 5 ;
		for (int i = 0 ; i <= options.size() - 1 ; i += 1)
		{
			Point textPos = UtilG.Translate(windowPos, 5, 5 + i * sy) ;
			Color textColor = i == selOption ? selColor : Game.colorPalette[9] ;
			DP.DrawText(textPos, Align.topLeft, DrawingOnPanel.stdAngle, options.get(i), NPCfont, textColor) ;
		}
		
	}

	private void bankerAction(Player player, BankWindow bankWindow, String action, DrawingOnPanel DP)
	{

		if (action == null) { return ;}
		
		if (Player.actionIsForward(action))
		{
			if (menu == 0 & (selOption == 0 | selOption == 1 | selOption == 2 | selOption == 3))
			{
				if (selOption == 0)
				{
					bankWindow.setMode("deposit");
				}
				if (selOption == 1)
				{
					bankWindow.setMode("withdraw");
				}
				if (selOption == 2)
				{
					bankWindow.setMode("investment low risk");
				}
				if (selOption == 3)
				{
					bankWindow.setMode("investment hight risk");
				}
				player.switchOpenClose(bankWindow) ;
				return ;
			}
		}

//		if (menu == 1)
//		{
//			bankWindow.readValue(action, DP) ;
//		}
		
		
//		if (menu == 0) { return ;}
//		
//		
//		bankWindow.display(null, DP) ;
//
//		if (menu == 1) { bankWindow.displayInput("Quanto gostaria de depositar?", action, DP) ;}
//		if (menu == 2) { bankWindow.displayInput("Quanto gostaria de sacar?", action, DP) ;}
//		
//		if (action == null) { return ;}
//
//		bankWindow.navigate(action) ;
//		
//		if (menu == 1 | menu == 2) { bankWindow.readValue(action, DP) ;}
//		
//		if (!Player.actionIsForward(action)) { return ;}
//		
//		switch (menu)
//		{
//			case 0: menu = selOption == 0 ? 1 : type.getSpeech().length - 1 ; break ;
//			case 1: case 2:
//			{
//				int amount = bankWindow.getAmountTyped() ;
//				switch (selOption)
//				{
//					case 0: bankWindow.deposit(bag, amount) ; break ;
//					case 1: bankWindow.withdraw(bag, amount) ; break ;
//					case 2: bankWindow.invest(bag, amount, false) ; break ;
//					case 3: bankWindow.invest(bag, amount, true) ; break ;
//				}
//
//				menu = numberMenus ;
//				break ;
//			}
//		}
		
	}

	private void crafterAction(Player player, BagWindow bag, String action, Point mousePos, CraftWindow craftWindow, DrawingOnPanel DP)
	{
//		System.out.println("crafting menu = " + menu);
		if (action == null) { return ;}

		if (menu == 0 & selOption == 0 & Player.actionIsForward(action))
		{
			player.switchOpenClose(craftWindow) ;
		}
	}
	
	private void doctorAction(String action, PersonalAttributes playerPA, PersonalAttributes petPA)
	{
		if (menu != 1) { return ;}
		
		if (playerPA.getLife().isMaxed())
		{
			if (petPA == null) { return ;}

			petPA.getLife().setToMaximum() ;
			petPA.getMp().setToMaximum() ;
			
			menu = 2 ;
			return ;
		}
		
		playerPA.getLife().setToMaximum() ;
		playerPA.getMp().setToMaximum() ;
		
		menu = 3 ;
	}

	private void elementalAction(Player player, BagWindow bag, ElementalWindow elementalWindow, String action, DrawingOnPanel DP)
	{
		
		if (action == null) { return ;}

		if (menu == 0 & selOption == 0 & Player.actionIsForward(action))
		{
			player.switchOpenClose(elementalWindow) ;
		}	
		
	}
	
	private void forgerAction(Player player, BagWindow bag, String action, ForgeWindow forgeWindow, DrawingOnPanel DP)
	{

		if (action == null) { return ;}

		if (menu == 0 & selOption == 0 & Player.actionIsForward(action))
		{
			player.switchOpenClose(forgeWindow) ;
		}
		
	}

	private void masterAction(Player player, String action, Point mousePos, SpellsTreeWindow spellsTree, DrawingOnPanel DP)
	{
		if (50 <= player.getLevel() & player.getProJob() == 0 & menu == 3)
		{
			if (action == null) { return ;}

			if (Player.actionIsForward(action))
			{
				player.setProJob(1 + selOption) ;
				player.addProSpells() ;
				player.getSpellsTreeWindow().switchTo2Tabs() ;
			}			
		}
		
		if (action == null) { return ;}
	
		if ((menu == 0 | menu == 5) & Player.actionIsForward(action))
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
		// TODO usar o move to map
		if (player.getMap().getName().equals("Forest 2")) { player.setMap(Game.getMaps()[30]) ; player.setPos(UtilG.Translate(pos, type.getImage().getWidth(null), 0)) ; return ;}
		if (player.getMap().getName().equals("Cave 1")) { player.setMap(Game.getMaps()[6]) ; player.setPos(UtilG.Translate(pos, type.getImage().getWidth(null), 0)) ; return ;}
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
	{
		if (action == null) { return ;}		

		if (action.equals("Enter") & selOption == 0)
		{
			if (player.getMap().getName().equals("Forest 13")) { player.setMap(Game.getMaps()[39]) ; player.setPos(pos) ; return ;}
			if (player.getMap().getName().equals("Island 1")) { player.setMap(Game.getMaps()[17]) ; player.setPos(pos) ; return ;}
		}
	}

	private void saverAction(Player player, Pet pet, String action)
	{
		
		if (action == null) { return ;}
		
		if (Player.actionIsForward(action) & menu == 1)
		{
			int slot = selOption + 1 ;
	        player.save(slot) ;
		}
		
	}
	
	private void sellerAction(Player player, String action, ShoppingWindow shopping, DrawingOnPanel DP)
	{

		if (action == null) { return ;}
		
		if (menu == 0 & Player.actionIsForward(action))
		{
			shopping.setBuyMode(selOption == 0) ;
			player.switchOpenClose(shopping) ;
		}
		
	}

	public void display(DrawingOnPanel DP)
	{
		DP.DrawImage(type.getImage(), pos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.bottomCenter) ;
	}

	@Override
	public String toString()
	{
//		return "NPCs [id=" + id + ", type=" + type + ", pos=" + pos + ", selOption=" + selOption + ", numberMenus="
//				+ numberMenus + ", menu=" + menu + ", window=" + window + "]";
		return type.getName() ;
	}
	
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/

	/*public String ChoicesMenu(String playerAction, NPCs npc, String[] choiceOption, DrawFunctions DF)
	{
		DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, selChoice, choiceOption, npc, npc.getColor()) ;
		selChoice = UtilS.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], playerAction, selChoice, choiceOption.length - 1) ;
		System.out.println(selChoice);
		for (int c = 0; c <= choiceOption.length - 1; c += 1)
		{
			if (playerAction.equals("Enter"))
			{
				return choiceOption[selChoice] ;
			}
		}
		
		return null ;
	}*/
	
	/*public int Doctor(String Choice, Player player, Pet pet, NPCs npc, DrawFunctions DF)
	{
		if (Menu[npc.getID()] == 0)
		{
			if (player.getLife()[0] < player.getLife()[1] | (pet.isAlive() & pet.getLife()[0] < pet.getLife()[1]))
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;	
				//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
				if (Choice.equals("1"))
				{
					Menu[npc.getID()] = 1 ;
				} else if (Choice.equals("2"))
				{
					DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
					Menu[npc.getID()] = 2 ;
				}
			}
			else
			{
				Menu[npc.getID()] = 3 ;
			}
		}
		if (Menu[npc.getID()] == 1)
		{
			player.getLife()[0] = player.getLife()[1] ;
			if (0 < pet.getLife()[0])
			{
				pet.getLife()[0] = pet.getLife()[1] ;
			}
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
		if (2 <= Menu[npc.getID()])
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()]], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;	
		}
		return Menu[npc.getID()] ;
	}	*/
	/*public int Sale(String Choice, Player player, NPCs npc, Items[] items, int[] MousePos, Image Player.CoinIcon, DrawFunctions DF)
	{
		int[] ItemsOnSale = new int[player.getBag().length] ;
		if (npc.getName().equals("Equips Seller") | npc.getName().equals("Vendedor de equipamentos"))
		{
			for (int i = 0 ; i <= 13 - 1 ; i += 1)
			{
				ItemsOnSale[i] += Items.BagIDs[5] + (Items.BagIDs[6] - Items.BagIDs[5])/5*player.getMap() + i ;
			}
		}
		if (npc.getName().equals("Items Seller") | npc.getName().equals("Vendedor de itens"))
		{
			for (int i = 0 ; i <= 8 - 1 ; i += 1)
			{
				ItemsOnSale[i] += i ;
			}
		}
		if (npc.getName().equals("Smuggle Seller") | npc.getName().equals("Contrabandista"))
		{
			ItemsOnSale = new int[] {Items.BagIDs[1] + 3, Items.BagIDs[1] + 4, Items.BagIDs[1] + 8, Items.BagIDs[1] + 10, Items.BagIDs[1] + 25, Items.BagIDs[2] + 3, Items.BagIDs[3] + 3, Items.BagIDs[4] + 2, Items.BagIDs[6] + 2, Items.BagIDs[6] + 8, Items.BagIDs[6] + 13, Items.BagIDs[7] + 1, Items.BagIDs[7] + 5, Items.BagIDs[7] + 7, Items.BagIDs[7] + 23} ;
		}
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[1], npc, npc.getColor()) ;
			if (Choice.equals("1"))
			{
				ItemsOnSale = player.getBag() ;
				Menu[npc.getID()] = 1 ;
			} else if (Choice.equals("2"))
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
				Menu[npc.getID()] = 2 ;
			} else if (Choice.equals("3"))
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
				Menu[npc.getID()] = 3 ;
			}
		} else if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			ItemsOnSale = Utg.ArrayWithIndexesGreaterThan(player.getBag(), 0) ;
			if (0 < ItemsOnSale.length)
			{
				Menu[npc.getID()] = Shopping(Choice, player, npc, items, "Selling", ItemsOnSale, MousePos, Player.CoinIcon, DF) ;
			}
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			ItemsOnSale = Utg.ArrayWithValuesGreaterThan(ItemsOnSale, 0) ;
			Menu[npc.getID()] = Shopping(Choice, player, npc, items, "Buying", ItemsOnSale, MousePos, Player.CoinIcon, DF) ;
		} else if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Bank(String Choice, Player player, NPCs npc, DrawFunctions DF)
	{
		float Tax = (float)(0.01) ;
		int Deposit = 0, Withdraw = 0 ;
		float Textangle = DrawPrimitives.OverallAngle ;
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[2], npc, npc.getColor()) ;
			if (Choice.equals("1"))
			{			
				Menu[npc.getID()] = 1 ;
			} else if (Choice.equals("2"))
			{
				Menu[npc.getID()] = 2 ;
			} else if (Choice.equals("3"))
			{
				Menu[npc.getID()] = 3 ;
			}
		} else if (Menu[npc.getID()] == 1)
		{	
			if (Tax*player.getGold()[1] <= player.getGold()[0])
			{	
				Point WindowPos = new Point((int)(0.5*ScreenL) - 10, (int)(0.5*ScreenH)) ;
				//DP.DrawRoundRect(WindowPos, "BotLeft", 40 + 10*TypedDeposit.length(), (int)(1.8*Utg.TextH(FontSize)), 1, Color.white, Color.lightGray, true) ;
				DP.DrawImage(Player.CoinIcon, WindowPos, Textangle, new float[] {1, 1}, new boolean[] {false, false}, "BotLeft", 1) ;
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
				if (!Choice.equals("Enter"))
				{
					TypedDeposit = UtilS.LiveTyping(new Point(WindowPos.x + 30, WindowPos.y - 8), Textangle, Choice, NPCTextFont, npc.getColor(), DP) ;			
				}
				if (Choice.equals("Enter"))
				{
					Menu[npc.getID()] = 3 ;
					UtilS.LiveTyping(new Point(WindowPos.x + 30, WindowPos.y - 8), Textangle, Choice, NPCTextFont, Color.yellow, DP) ;	// Clean the typed text
					if (!TypedDeposit.equals("") & UtilG.isNumeric(TypedDeposit))
					{
						Deposit = Integer.parseInt(TypedDeposit) ;	
						if (Deposit + Tax*player.getGold()[0] <= player.getGold()[0])
						{
							player.getGold()[0] += -Tax*player.getGold()[0] - Deposit ;						
							player.getGold()[1] += Deposit ;
							Menu[npc.getID()] = 7 ;
						}
						else if (Deposit <= player.getGold()[0])
						{
							Menu[npc.getID()] = 6 ;
						}	
						else
						{
							Menu[npc.getID()] = 5 ;
						}
					}
					else
					{
						Menu[npc.getID()] = 4 ;
					}
				}
			}
			else
			{
				Menu[npc.getID()] = 6 ;
			}
		}  else if (Menu[npc.getID()] == 2)
		{
			Point WindowPos = new Point((int)(0.5*ScreenL) - 10, (int)(0.5*ScreenH)) ;
			//DP.DrawRoundRect(WindowPos, "BotLeft", 40 + 10*TypedWithdraw.length(), (int)(1.8*Utg.TextH(FontSize)), 1, Color.white, Color.lightGray, true) ;
			DP.DrawImage(Player.CoinIcon, WindowPos, Textangle, new float[] {1, 1}, new boolean[] {false, false}, "BotLeft", 1) ;
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			if (!Choice.equals("Enter"))
			{
				TypedWithdraw = UtilS.LiveTyping(new Point(WindowPos.x + 30, WindowPos.y - 8), Textangle, Choice, NPCTextFont, Color.yellow, DP) ;			
			}
			if (Choice.equals("Enter"))
			{
				Menu[npc.getID()] = 3 ;
				UtilS.LiveTyping(new Point((int)(0.3*ScreenL), (int)(0.5*ScreenH)), Textangle, Choice, NPCTextFont, Color.yellow, DP) ;	// Clean the typed text
				if (!TypedWithdraw.equals("") & UtilG.isNumeric(TypedWithdraw))
				{
					Withdraw = Integer.parseInt(TypedWithdraw) ;	
					if (Withdraw <= player.getGold()[1])
					{
						player.getGold()[0] += Withdraw ;						
						player.getGold()[1] += -Withdraw ;
						Menu[npc.getID()] = 8 ;
					}
					else
					{
						Menu[npc.getID()] = 5 ;
					}	
				}
				else
				{
					Menu[npc.getID()] = 4 ;
				}
			}
		} else if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][9], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		} else if (3 < Menu[npc.getID()])
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()]], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Forger(String Choice, Player player, Pet pet, NPCs npc, Items[] items, Maps[] maps, DrawFunctions DF)
	{
		int ForgeResult = 0 ;	// 0: Forge has not been attempted yet, 1: forge was successful, 2: the player doesn't have the rune, 3: the equip is not equipped, 4: forge failed
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
			if (Choice.equals("1"))
			{
				Menu[npc.getID()] = 1 ;
			} else if (Choice.equals("2"))
			{	
				Menu[npc.getID()] = 2 ;
			}
		} else if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[3], npc, npc.getColor()) ;
			if (Choice.equals("1") | Choice.equals("2") | Choice.equals("3"))
			{
				ForgeEquip = player.getEquips()[Integer.parseInt(Choice) - 1] ;
				Menu[npc.getID()] = 3 ;
			} 
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][7], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		} 
		if (Menu[npc.getID()] == 3)
		{
			ForgeResult = Forging(player, pet, npc, items, ForgeEquip, maps) ;
			Menu[npc.getID()] = 4 ;
		}
		if (Menu[npc.getID()] == 4)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][ForgeResult + 2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Forger(String Choice, Player player, Pet pet, NPCs npc, Items[] items, Maps[] maps, DrawFunctions DF)
	{
		int ForgeResult = 0 ;	// 0: Forge has not been attempted yet, 1: forge was successful, 2: the player doesn't have the rune, 3: the equip is not equipped, 4: forge failed
		String[] choices = AcceptedChoices[0] ;
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			if (ChoicesMenu(Choice, npc, choices, DF) != null)
			{
				Menu[npc.getID()] = Integer.parseInt(ChoicesMenu(Choice, npc, AcceptedChoices[0], DF)) ;
			}
		} else if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[3], npc, npc.getColor()) ;
			if (Choice.equals(choices[0]) | Choice.equals(choices[1]) | Choice.equals(choices[2]))
			{
				ForgeEquip = player.getEquips()[Integer.parseInt(Choice) - 1] ;
				Menu[npc.getID()] = 3 ;
			} 
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][7], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		} 
		if (Menu[npc.getID()] == 3)
		{
			ForgeResult = Forging(player, pet, npc, items, ForgeEquip, maps) ;
			Menu[npc.getID()] = 4 ;
		}
		if (Menu[npc.getID()] == 4)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][ForgeResult + 2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public void Crafter(String Choice, Player player, NPCs npc, Items[] items, int[] MousePos, DrawFunctions DF)
	{
		int[][] NPCCraftingIngredients = Utg.ArrayWithFirstTermEqualTo(npc.getID() - 17 * player.getJob(), Items.CraftingNPCIDs, Items.CraftingIngredients) ;
		int[][] NPCCraftingIngredientAmounts = Utg.ArrayWithFirstTermEqualTo(npc.getID() - 17 * player.getJob(), Items.CraftingNPCIDs, Items.CraftingIngredientAmounts) ; 
		int[][] NPCCraftingProducts = Utg.ArrayWithFirstTermEqualTo(npc.getID() - 17 * player.getJob(), Items.CraftingNPCIDs, Items.CraftingProducts) ;
		int[][] NPCCraftingProductAmounts = Utg.ArrayWithFirstTermEqualTo(npc.getID() - 17 * player.getJob(), Items.CraftingNPCIDs, Items.CraftingProductAmounts) ;
		boolean PlayerHasTheIngredients = false ;
		for (int i = 0 ; i <= NPCCraftingIngredients.length - 1 ; i += 1)
		{
			NPCCraftingIngredients[i] = Utg.ArrayWithValuesGreaterThan(NPCCraftingIngredients[i], -1) ;
		}
		for (int i = 0 ; i <= NPCCraftingIngredientAmounts.length - 1 ; i += 1)
		{
			NPCCraftingIngredientAmounts[i] = Utg.ArrayWithValuesGreaterThan(NPCCraftingIngredientAmounts[i], -1) ;
		}
		for (int i = 0 ; i <= NPCCraftingProducts.length - 1 ; i += 1)
		{
			NPCCraftingProducts[i] = Utg.ArrayWithValuesGreaterThan(NPCCraftingProducts[i], -1) ;
		}
		for (int i = 0 ; i <= NPCCraftingProductAmounts.length - 1 ; i += 1)
		{
			NPCCraftingProductAmounts[i] = Utg.ArrayWithValuesGreaterThan(NPCCraftingProductAmounts[i], -1) ;
		}
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
			if (Choice.equals("1"))
			{
				Menu[npc.getID()] = 1 ;
			} else if (Choice.equals("2"))
			{
				Menu[npc.getID()] = 3 ;
			}
		} if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()] + 1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			PlayerHasTheIngredients = Crafting(Choice, player, npc, items, NPCCraftingIngredients, NPCCraftingIngredientAmounts, NPCCraftingProducts, NPCCraftingProductAmounts, MousePos, DF) ;			
			if (Choice.equals("Enter") | Choice.equals("LeftClick"))
			{
				Menu[npc.getID()] = 2 ;
			}
		} if (Menu[npc.getID()] == 2)
		{
			if (PlayerHasTheIngredients)
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
				Menu[npc.getID()] = 3 ;
			}
			else
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			}
		} if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
	}*/
	/*public int Elemental(String Choice, Player player, NPCs npc, Items[] items, DrawFunctions DF)
	{
		int[] Ingredients = new int[] {1690, 1691, 1692, 1693, 1694, 1695, 1696, 1697, 1698, 1699} ;
		SelectedItem[0] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], Choice, SelectedItem[0], Ingredients.length - 1) ;
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
			if (Choice.equals("1"))
			{ 
				Menu[npc.getID()] = 1 ;
			}
			else if (Choice.equals("2"))
			{
				Menu[npc.getID()] = 2 ;
			}
		} else if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, new Color(200, 200, 200)) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[3], npc, npc.getColor()) ;
			if (Choice.equals("1") | Choice.equals("2") | Choice.equals("3"))
			{
				if (0 < player.getEquips()[Integer.parseInt(Choice) - 1])
				{
					ElementalEquipID = player.getEquips()[Integer.parseInt(Choice) - 1] ;
					Menu[npc.getID()] = 3 ;
				}
				else
				{
					Menu[npc.getID()] = 4 ;
				}
			} 
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][7], NPCTextFont, npc, SpeakingBubbleImage, new Color(200, 200, 200)) ;
		} else if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc, SpeakingBubbleImage, new Color(200, 200, 200)) ;
			DF.DrawElementalNPCScreen(SelectedItem[0], items, Ingredients) ;
			if (Choice.equals("Enter"))
			{
				if (0 < player.getBag()[Ingredients[SelectedItem[0]]])
				{
					Items.EquipsElem[ElementalEquipID - Items.BagIDs[5]] = Uts.ElementName(SelectedItem[0]) ;
					player.getBag()[Ingredients[SelectedItem[0]]] += -1 ;
					player.Equip(items, ElementalEquipID) ;
					player.Equip(items, ElementalEquipID) ;
					Menu[npc.getID()] = 5 ;
				}
				else
				{
					Menu[npc.getID()] = 6 ;
				}
			} 
		} else if (4 <= Menu[npc.getID()])
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()]], NPCTextFont, npc, SpeakingBubbleImage, new Color(200, 200, 200)) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Saver(String Choice, Player player, Pet pet, NPCs npc, DrawFunctions DF)
	{
		String FileName = "" ;
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
			if (Choice.equals("1"))
			{ 
				Menu[npc.getID()] = 1 ;
			}
			else if (Choice.equals("2"))
			{
				Menu[npc.getID()] = 2 ;
			}
		}
		else if (Menu[npc.getID()] == 1)	// Save the game
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[4], npc, npc.getColor()) ;
			if (Choice.equals("1") | Choice.equals("2") | Choice.equals("3"))
			{ 
				FileName = "save" + Choice + ".txt" ;
				Save(player, pet, FileName) ;
				Menu[npc.getID()] = 3 ;
			}
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		} else if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	
	/*public int Quest(String Choice, Player player, Pet pet, CreatureTypes[] creatureTypes, Creatures[] creatures, NPCs npc, Quests[] quest, Items[] items, DrawFunctions DF)
	{
		int npcID = -1 ;
		if (npc.getName().length() == 8)
		{
			npcID = 10*Character.getNumericValue(npc.getName().charAt(npc.getName().length() - 2)) + Character.getNumericValue(npc.getName().charAt(npc.getName().length() - 1)) ;	
		} else if (npc.getName().length() == 7)
		{
			npcID = Character.getNumericValue(npc.getName().charAt(npc.getName().length() - 1)) ;		
		}		
		TextCat = Uts.FindTextPos(AllText, "* Quest *") ;
		if (quest[npcID].getType().equals("Exp"))
		{
			TextCat = Uts.FindTextPos(AllText, "* Quest exp *") ;
		}
		else if (quest[npcID].getType().equals("Item"))
		{
			TextCat = Uts.FindTextPos(AllText, "* Quest items *") ;
		}
		boolean QuestHasCreatures = false, QuestHasItems = false ;
		for (int i = 0 ; i <= quest[npcID].getReqCreatures().length - 1 ; i += 1)
		{
			if(-1 < quest[npcID].getReqCreatures()[i])
			{
				QuestHasCreatures = true ;
			}
		}
		for (int i = 0 ; i <= quest[npcID].getReqItems().length - 1 ; i += 1)
		{
			if(-1 < quest[npcID].getReqItems()[i])
			{
				QuestHasItems = true ;
			}
		}
		if (Menu[npc.getID()] == 0)
		{
			if (player.getQuest()[npcID] == -1)	// Player has not gotten the quest
			{
				DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;	
				//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
				if (Choice.equals("1"))
				{ 
					Menu[npc.getID()] = 1 ;
				} else if (Choice.equals("2"))
				{ 
					Menu[npc.getID()] = 2 ;
				}
			} else if (QuestIsComplete(player, quest, npcID))	// Quest is complete
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][5], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
				//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
				if (Choice.equals("1"))
				{ 
					QuestReward(player, pet, quest, npcID) ;
					player.getQuest()[npcID] = -1 ;
				} else if (Choice.equals("2"))
				{ 
					Menu[npc.getID()] = 2 ;
				}
			} else	// Quest is active but not complete
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
				DF.DrawQuestRequirementsList(creatureTypes, creatures, items, quest[npcID], npc, new int[] {npc.getPos()[0] - npc.getWidth(null), npc.getPos()[1] - npc.getHeight(null)}, QuestHasCreatures, QuestHasItems, SpeakingBubbleImage) ;
			}
		} else if (Menu[npc.getID()] == 1)	// Player accepted the quest
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
			DF.DrawQuestRequirementsList(creatureTypes, creatures, items, quest[npcID], npc, new int[] {npc.getPos()[0] - npc.getWidth(null), npc.getPos()[1] - npc.getHeight(null)}, QuestHasCreatures, QuestHasItems, SpeakingBubbleImage) ;
			player.getQuest()[npcID] = npcID ;
		} else if (Menu[npc.getID()] == 2)	// Player refused the quest
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][6], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public void CaveEntrance(Player player, Maps[] maps)
	{
		if (player.getMap() == 5)
		{
			player.setMap(30, maps) ;
			player.getPos()[0] += player.getStep() ;
		} else if (0 < player.getBag()[1502]) // if the player has the climbing hook
		{
			player.setMap(5, maps) ;
			player.getPos()[0] += -player.getStep() ;
		}		
	}*/
	/*public int Sailor(String Choice, Player player, NPCs npc, Image BoatImage, Animations Ani, DrawFunctions DF)
	{
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;	
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc, npc.getColor()) ;
		}
		if (Choice.equals("1"))
		{
			Menu[npc.getID()] = 1 ;
		} else if (Choice.equals("2"))
		{
			Menu[npc.getID()] = 2 ;
		}
		if (Menu[npc.getID()] == 1)
		{
			Ani.SetAniVars(19, new Object[] {150, player, BoatImage}) ;
			Ani.StartAni(19) ;
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][2], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;	
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Citizen(String Choice, NPCs npc, DrawFunctions DF)
	{
		if (Choice.equals("Enter") & Menu[npc.getID()] < AllText[TextCat].length - 2)
		{
			Menu[npc.getID()] += 1 ;
		}
		if (Menu[npc.getID()] <= AllText[TextCat].length - 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()] + 1], NPCTextFont, npc, SpeakingBubbleImage, npc.getColor()) ;	
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[5], npc, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	
	/* related methods */
	/*public void ActivatePassiveSkill(Player player, Pet pet, int SelectedSkill)
	{
		if (player.getJob() == 0)
		{
			if (SelectedSkill == 1)
			{
				player.getLife()[1] += 10 + 0.04564*player.getLife()[1] ;
			}
			if (SelectedSkill == 2)
			{
				player.getPhyAtk()[0] += 2 + 0.0372*player.getPhyAtk()[0] ;
			}
			if (SelectedSkill == 3)
			{
				player.getPhyDef()[0] += 2 + 0.0372*player.getPhyDef()[0] ;
			}
			if (SelectedSkill == 7)
			{
				player.getBlood()[6] += 0.05 ;
			}
			if (SelectedSkill == 10)
			{
				player.getElemMult()[0] += -0.03 ;
			}
			if (SelectedSkill == 11)
			{
				player.getPhyAtk()[0] += 2 ;
				player.getDex()[0] += 1 ;
				player.getAgi()[0] += 1 ;
			}
			if (player.getProJob() == 1)
			{
				if (SelectedSkill == 15)
				{
					player.getLife()[0] += 220 ;
					player.getLife()[1] += 220 ;
					player.getPhyAtk()[0] += 9 ;
					player.getPhyDef()[0] += 5 ;
				}
				if (SelectedSkill == 15)
				{
					player.getDex()[0] += 3 ;
					player.getAgi()[0] += 3 ;
					player.getStun()[0] += 0.04 ;
				}
				if (SelectedSkill == 24)
				{
					player.getPhyAtk()[2] += 0.0372*player.getPhyAtk()[2] ;
					player.getMagAtk()[2] += 0.0372*player.getMagAtk()[2] ;
					player.getPhyDef()[2] += 0.0372*player.getPhyDef()[2] ;
					player.getMagDef()[2] += 0.0372*player.getMagDef()[2] ;
					player.getDex()[2] += 0.0372*player.getDex()[2] ;
					player.getAgi()[2] += 0.0372*player.getAgi()[2] ;
				}
			}
			if (player.getProJob() == 2)
			{
				if (SelectedSkill == 15)
				{
					player.getLife()[0] += 220 ;
					player.getLife()[1] += 220 ;
					player.getPhyAtk()[0] += 5 ;
					player.getPhyDef()[0] += 9 ;
				}
				if (SelectedSkill == 18)
				{
					player.getMagDef()[0] += 0.0845*player.getMagDef()[0] ;
				}
				if (SelectedSkill == 19)
				{
					player.getCrit()[2] += 0.15 ;
				}
				if (SelectedSkill == 20)
				{
					player.getPoison()[2] += 0.1 ;
				}
			}
		}
		if (player.getJob() == 1)
		{
			if (SelectedSkill == 2)
			{
				player.getMagAtk()[1] += 2 + 0.04*player.getMagAtk()[0] ;
			}
			if (SelectedSkill == 8)
			{
				player.getMagDef()[1] += 2 + 0.04*player.getMagDef()[0] ;
			}
			if (player.getProJob() == 1)
			{
				if (SelectedSkill == 15)
				{
					player.getMp()[0] += 220 ;
					player.getMp()[1] += 220 ;
					player.getMagAtk()[0] += 9 ;
					player.getMagDef()[0] += 5 ;
				}
			}
			if (player.getProJob() == 2)
			{
				if (SelectedSkill == 15)
				{
					player.getMp()[0] += 220 ;
					player.getMp()[1] += 220 ;
					player.getMagAtk()[0] += 5 ;
					player.getMagDef()[0] += 9 ;
				}
			}	
		}
		if (player.getJob() == 2)
		{
			if (SelectedSkill == 1)
			{
				player.getDex()[1] += 1 ;
				player.getAgi()[1] += 0.4 ;
			}
			if (SelectedSkill == 7)
			{
				player.getPhyAtk()[1] += 1 + 0.02*player.getPhyAtk()[0] ;
				player.getMagAtk()[1] += 1 + 0.02*player.getPhyAtk()[0] ;
			}
			if (SelectedSkill == 10)
			{
				player.getBlood()[3] += 0.04 ;
				player.getPoison()[3] += 0.04 ;
			}
			if (player.getProJob() == 1)
			{
				if (SelectedSkill == 15)
				{
					player.getPhyAtk()[0] += 7 ;
					player.getDex()[0] += 10 ;
					player.getAgi()[0] += 3 ;
				}
				if (SelectedSkill == 17)
				{
					player.incRange((float) 0.0845*player.getRange()) ;
				}
			}
			if (player.getProJob() == 2)
			{
				if (SelectedSkill == 15)
				{
					player.getMp()[0] += 68 ;
					player.getMp()[1] += 68 ;
					player.getMagAtk()[0] += 7 ;
					player.getDex()[0] += 3 ;
					player.getAgi()[0] += 2 ;
				}
			}
		}
		if (player.getJob() == 3)
		{
			if (SelectedSkill == 1)
			{
				player.getDex()[1] += 0.4 ;
				player.getAgi()[1] += 1 ;
			}
			if (SelectedSkill == 3)
			{
				player.getCrit()[1] += 0.03 ;
			}
			if (SelectedSkill == 9)
			{
				player.getLife()[1] += 0.03*player.getLife()[1] ;
				player.getPhyAtk()[1] += 2 ;
				player.getPhyDef()[1] += 2 ;
			}
			if (SelectedSkill == 13)
			{
				pet.getLife()[1] += 10 ;
				pet.getMp()[1] += 10 ;
				pet.getPhyAtk()[0] += 2 ;
				pet.getPhyDef()[0] += 2 ;
				pet.getDex()[0] += 0.4 ;
				pet.getAgi()[0] += 0.4 ;
			}
			if (player.getProJob() == 1)
			{
				if (SelectedSkill == 15)
				{
					player.getMp()[0] += 48 ;
					player.getMp()[1] += 48 ;
					player.getPhyAtk()[0] += 7 ;
					player.getMagAtk()[0] += 4 ;
					player.getDex()[0] += 2 ;
					player.getAgi()[0] += 3 ;
				}
			}
			if (player.getProJob() == 2)
			{
				if (SelectedSkill == 15)
				{
					player.getLife()[0] += 15 ;
					player.getLife()[1] += 15 ;
					player.getPhyAtk()[0] += 7 ;
					player.getPhyDef()[0] += 4 ;
					player.getDex()[0] += 3 ;
					player.getAgi()[0] += 5 ;
				}
			}
		}
		if (player.getJob() == 4)
		{
			if (SelectedSkill == 1)
			{
				player.getAgi()[1] += 1 ;
			}
			if (SelectedSkill == 4)
			{
				player.getPhyAtk()[1] += 3 ;
				player.getDex()[1] += 1 ;
			}
			if (player.getProJob() == 1)
			{
				if (SelectedSkill == 15)
				{
					player.getLife()[0] += 16 ;
					player.getLife()[1] += 16 ;
					player.getPhyAtk()[0] += 3 ;
					player.getPhyDef()[0] += 3 ;
					player.getDex()[0] += 5 ;
					player.getAgi()[0] += 6 ;
				}
			}
			if (player.getProJob() == 2)
			{
				if (SelectedSkill == 15)
				{
					player.getLife()[0] += 10 ;
					player.getLife()[1] += 10 ;
					player.getPhyAtk()[0] += 6 ;
					player.getPhyDef()[0] += 6 ;
					player.getDex()[0] += 3 ;
					player.getAgi()[0] += 10 ;
				}
				if (SelectedSkill == 19)
				{
					player.getStun()[3] += 0.1 ;
				}
			}
		}
	}*/
	/*public int Forging(Player player, Pet pet, NPCs npc, Items[] items, int EquipID, Maps[] maps)
	{
		int RuneID = 0 ;
		int equiplocalID = EquipID - Items.BagIDs[6] ;
		int NumberOfEquipTypes = 3 ;
		int ForgeResult = 0 ;
		int ForgeLevel = (int) Items.EquipsBonus[equiplocalID][1] ;
		float ForgeBonus = (float) 1.1 ;
		
		RuneID = (Items.BagIDs[2] + 2 * ForgeLevel + (EquipID + player.getJob()) % NumberOfEquipTypes) ;
		if (Items.BagIDs[5] <= EquipID)						// the player doesn't have the equip equipped
		{
			ForgeResult = 3 ;
		}
		else if (player.getBag()[RuneID] <= 0)	// the player doesn't have the appropriate rune
		{
			ForgeResult = 2 ;
		}
		else if (Items.EquipsBonus[equiplocalID][1] <= 9)	 //  the equipment bonus is not maximized yet
		{
			if (Math.random() <= (1 - ForgeLevel / 20.0))	 //  forge is successful
			{
				ForgeResult = 1 ;
				player.getBag()[RuneID] += -1 ;				// destroy the rune
				player.Equip(items, EquipID) ;				// unequip the equipment
				Items.EquipsBonus[equiplocalID][1] += 1 ;	// increase the bonus of the equipment
				for (int i = 2 ; i <= 13 ; ++i)
				{
					Items.EquipsBonus[equiplocalID][i] = (float)(ForgeBonus * Items.EquipsBonus[equiplocalID][i]) ;	// increase the bonus of the equipment
				}
				player.Equip(items, EquipID) ;				// equip the equipment
			}
		}
		else									// forge fails
		{
			ForgeResult = 4 ;
			player.getBag()[RuneID] += -1 ;				// destroy the equipment
			player.Equip(items, EquipID) ;				// unequip the equipment
			for (int i = 2 ; i <= 13 ; ++i)
			{
				Items.EquipsBonus[equiplocalID][i] = (float)(Items.EquipsBonus[equiplocalID][i] / Math.pow(ForgeBonus, Items.EquipsBonus[equiplocalID][1])) ;		// reset the bonus of the equipment			
			}
			Items.EquipsBonus[equiplocalID][1] = 0 ;	// reset the bonus of the equipment
			--player.getBag()[EquipID] ;
		}
		//if (!Uts.IdentifySave(player, pet, Items.EquipsBonus, 3, maps).equals("save not found"))
		//{
		//	Save(player, pet, Uts.IdentifySave(player, pet, Items.EquipsBonus, 3, maps)) ;	
		//}
		return ForgeResult ;
	}*/
	/*public boolean Crafting(String Choice, Player player, NPCs npc, Items[] items, int[][] Ingredients, int[][] IngredientAmounts, int[][] Products, int[][] ProductAmounts, int[] MousePos, DrawFunctions DF)
	{
		int ItemsPerWindow = 10 ;
		int MaxWindow = Math.max(0, (Products.length - 1)/ItemsPerWindow) ;
		int[] AvailableProducts = null ;
		boolean PlayerHasTheIngredients = true ;
		
		for (int p = 0 ; p <= Products.length - 1 ; p += 1)
		{
			PlayerHasTheIngredients = Uts.PlayerHasTheIngredients(player.getBag(), Ingredients, IngredientAmounts, FabAmount, p) ;
			if (PlayerHasTheIngredients)
			{
				AvailableProducts = Utg.AddVectorElem(AvailableProducts, Products[p][0]) ;
			}
		}
		
		if (Menu[npc.getID()] == 0)
		{
			SelectedItem[1] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], Choice, SelectedItem[1], Products.length - 1) ;
			SelectedWindow[0] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], Choice, SelectedWindow[0], MaxWindow) ;
			if (Choice.equals(Player.ActionKeys[1]) | Choice.equals(Player.ActionKeys[3]))
			{
				SelectedItem[1] = SelectedWindow[0]*ItemsPerWindow ;
			}
			if (Choice.equals("Enter") | Choice.equals("LeftClick"))
			{
				Choice = "" ;
				Menu[npc.getID()] = 1 ;
			}
			if (Choice.equals("Escape") | Choice.equals("MouseRightClick"))
			{
				Menu[npc.getID()] = 0 ;
			}
		}
		if (Menu[npc.getID()] == 1)
		{
			PlayerHasTheIngredients = Uts.PlayerHasTheIngredients(player.getBag(), Ingredients, IngredientAmounts, FabAmount, SelectedItem[1]) ;
			if (PlayerHasTheIngredients)
			{
				int ProductItemID = Products[SelectedItem[1]][0] ;
				if (player.getJob() == 2 & Items.BagIDs[4] + 6 <= ProductItemID & ProductItemID <= Items.BagIDs[4] + 14)	// Elemental arrows
				{
					if (Items.BagIDs[4] + 6 <= ProductItemID & ProductItemID <= Items.BagIDs[4] + 7 & 1 <= player.getSpell()[8] | Items.BagIDs[4] + 8 <= ProductItemID & ProductItemID <= Items.BagIDs[4] + 9 & 2 <= player.getSpell()[8]  | Items.BagIDs[4] + 10 <= ProductItemID & ProductItemID <= Items.BagIDs[4] + 11 & 3 <= player.getSpell()[8]  | Items.BagIDs[4] + 12 <= ProductItemID & ProductItemID <= Items.BagIDs[4] + 13 & 4 <= player.getSpell()[8]  | Items.BagIDs[4] + 14 == ProductItemID & 5 <= player.getSpell()[8])
					{
						player.setBag(Uts.Craft(player.getBag(), Ingredients, IngredientAmounts, Products, ProductAmounts, FabAmount, SelectedItem[1])) ;
					}
				}
				else if (player.getJob() == 4 & ProductItemID == 1378)											// Poisonous potions
				{
					if (Math.random() <= 0.2*player.getSpell()[9])
					{
						player.setBag(Uts.Craft(player.getBag(), Ingredients, IngredientAmounts, Products, ProductAmounts, FabAmount, SelectedItem[1])) ;
					}
				}
				else
				{
					player.setBag(Uts.Craft(player.getBag(), Ingredients, IngredientAmounts, Products, ProductAmounts, FabAmount, SelectedItem[1])) ;
				}
			}
			Menu[npc.getID()] = 0 ;
		}
		Font font = new Font(NPCTextFontName, Font.BOLD, 14) ;
		int[] Pos = new int[] {(int) (0.1 * ScreenL), (int) (0.88 * ScreenH)} ;
		int L = (int) (0.35 * ScreenL), H = (int) (0.45 * ScreenH) ;
		int sy = (int) (0.1 * H) ;
		int NmaxItemsPerWindow = 10 ;
		int NitensOnWindow = Math.min(Products.length - SelectedWindow[0] * NmaxItemsPerWindow, NmaxItemsPerWindow) ;
		DF.DrawCrafting(Menu[npc.getID()], SelectedItem[1], SelectedWindow[0], items, Ingredients, IngredientAmounts, Products, ProductAmounts, AvailableProducts, MousePos) ;
		for (int i = 0 ; i <= NitensOnWindow - 1 ; i += 1)
		{
			int ItemID = i + SelectedWindow[0] * NmaxItemsPerWindow ;
			if (0 < Products[ItemID].length)
			{
				int[] TextPos = new int[] {(int) (Pos[0] + 0.03 * L), (int) (Pos[1] - 0.97 * H + Utg.TextH(font.getSize())) + i * sy} ;
				if (Utg.ArrayContains(AvailableProducts, Products[ItemID][0]))
				{
					if (Menu[npc.getID()] == 0 & Utg.isInside(MousePos, TextPos, L, Utg.TextH(font.getSize())))
					{
						SelectedItem[1] = ItemID ;
					}
				}
			}
		}
		return PlayerHasTheIngredients ;
	}*/
	/*public int Shopping(String Choice, Player player, NPCs npc, Items[] items, String mode, int[] ItemsOnSale, int[] MousePos, Image Player.CoinIcon, DrawFunctions DF)
	{
		int ItemsPerWindow = 10 ;
		int MaxWindow = Math.max(0, (ItemsOnSale.length - 1)/ItemsPerWindow) ;
		SelectedItem[2] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], Choice, SelectedItem[2], Math.min(ItemsOnSale.length - ItemsPerWindow*SelectedWindow[1], ItemsPerWindow) - 1) ;
		SelectedWindow[1] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], Choice, SelectedWindow[1], MaxWindow) ;
		if (Choice.equals(Player.ActionKeys[1]) | Choice.equals(Player.ActionKeys[3]))
		{
			SelectedItem[2] = SelectedWindow[1]*ItemsPerWindow ;
		}
		if (Choice.equals("Enter"))
		{
			int ItemID = ItemsOnSale[SelectedItem[2]] ;
			if (mode.equals("Selling"))
			{
				if (0 < ItemsOnSale[SelectedItem[2]])
				{
					player.getBag()[ItemID] += -1 ;
					if (Items.BagIDs[5] <= ItemID & ItemID < Items.BagIDs[6])
					{
						player.Equip(items, ItemID) ;
					}
					player.getGold()[0] += (int)(0.7*items[ItemID].getPrice()) ;
				}
			} else if (mode.equals("Buying"))
			{
				if (items[ItemID].getPrice() <= player.getGold()[0])
				{
					player.getBag()[ItemID] += 1 ;
					player.getGold()[0] += -items[ItemID].getPrice() ;
				}
			}
			SelectedItem[2] = 0 ;
		}
		int[] WindowPos = new int[] {(int) (0.1 * ScreenL), (int) (0.88 * ScreenH)} ;
		int WindowL = (int) (0.5 * ScreenL), WindowH = (int) (0.5 * ScreenH) ;
		int Sx = (int) (0.7 * WindowL), Sy = (int) (0.1 * WindowH) ;
		int[][] TextPos = new int[Math.min(ItemsOnSale.length - ItemsPerWindow * SelectedWindow[1], ItemsPerWindow)][2] ;
		for (int i = 0 ; i <= Math.min(ItemsOnSale.length - ItemsPerWindow * SelectedWindow[1], ItemsPerWindow) - 1 ; i += 1)
		{
			int ItemID = i + ItemsPerWindow * SelectedWindow[1] ;
			TextPos[i] = new int[] {(int) (WindowPos[0] + 10), (int) (WindowPos[1] + 16 + i * Sy)} ;
			if (Utg.isInside(MousePos, TextPos[i], Sx, (int) (0.99 * Sy)))
			{
				SelectedItem[2] = ItemID ;
			}
		}
		DF.DrawShopping(WindowPos, WindowL, WindowH, SelectedItem[2], SelectedWindow[1], TextPos, mode, items, ItemsOnSale, MousePos, Player.CoinIcon) ;
		return Menu[npc.getID()] ;
	}*/
	/*public void Save(Player player, Pet pet, String fileName)
	{
		player.Save(fileName, pet) ;
	}*/
	
	/*public boolean QuestIsComplete(Player player, Quests[] quest, int QuestID)
	{
		int[] PlayerItems = new int[quest[QuestID].getReqItems().length] ;
		for (int i = 0 ; i <= quest[QuestID].getCounter().length - 1 ; i += 1)
		{
			if (quest[QuestID].getCounter()[i] < quest[QuestID].getReqCreaturesAmounts()[i])
			{
				return false ;
			}
		}
		for (int i = 0 ; i <= quest[QuestID].getReqItems().length - 1 ; i += 1)
		{
			if(-1 < quest[QuestID].getReqItems()[i])
			{
				PlayerItems[i] += player.getBag()[quest[QuestID].getReqItems()[i]] ;
			}
		}
		for (int i = 0 ; i <= quest[QuestID].getReqItems().length - 1 ; i += 1)
		{
			if (-1 < quest[QuestID].getReqItems()[i])
			{
				if (PlayerItems[i] < 1)
				{
					return false ;
				}
				PlayerItems[i] += -1 ;
			}
		}
		return true ;
	}*/
	/*public void QuestReward(Player player, Pet pet, Quests[] quest, int QuestID)
	{
		for (int i = 0 ; i <= quest[QuestID].getCounter().length/2 - 1 ; ++i)
		{
			quest[QuestID].getCounter()[i] = 0 ;
		}
		for (int i = 0 ; i <= quest[QuestID].getReqItems().length - 1 ; ++i)
		{
			if(-1 < quest[QuestID].getReqItems()[i])
			{
				player.getBag()[quest[QuestID].getReqItems()[i]] += -1 ;
			}
		}
		if (-1 < quest[QuestID].getExpReward())
		{
			player.getExp()[0] += quest[QuestID].getExpReward() ;
		}
		if (-1 < quest[QuestID].getGoldReward())
		{
			player.getGold()[0] += quest[QuestID].getGoldReward() ;
		}
		for (int i = 0 ; i <= quest[QuestID].getItemsReward().length - 1 ; ++i)
		{
			if (-1 < quest[QuestID].getItemsReward()[i])
			{
				player.getBag()[quest[QuestID].getItemsReward()[i]] += 1 ;
			}
		}
		if (QuestID == 27)	// Book
		{
			player.getQuestSkills()[6] = true ; // 1308	1324	1324	1324	1834
		}
		if (QuestID == 39)
		{
			pet.getLife()[0] = pet.getLife()[1] ;	// Pet is alive
			pet.getPersonalAtt().setPos(player.getPos()) ;
		}
		if (QuestID == 42 | QuestID == 46 | QuestID == 50 | QuestID == 56 | QuestID == 62)	// Map
		{
			// 42: 208	208	208	1843
			// 46: 1501	1897
			// 50: 1559	1930
			// 56: 1587	1946	1947
			// 62: 1394	1395	1404
			player.getQuestSkills()[player.getContinent()] = true ;
		}
		if (QuestID == 48)	// Shovel
		{
			player.getQuestSkills()[5] = true ;
		}
		if (QuestID == 57) // Ride 1596	1597	1948
		{
			player.getQuestSkills()[7] = true ;
		}
		if (QuestID == 60) // Dragon's aura 1598	1599	1600	1601	1602
		{
			player.getQuestSkills()[8] = true ;
		}
		if (QuestID == 8 | QuestID == 10 | QuestID == 11 | QuestID == 34 | QuestID == 44) // Bestiary 1598	1599	1600	1601	1602
		{
			player.getQuestSkills()[9] = true ;
		}
	}*/
}