package main;

public abstract class Loading
{
//	private int selectedSlot, tab ;
	
//	public Loading()
//	{
////		selectedSlot = 0 ;
////		tab = 0 ;
//		LoadingGif = UtilS.loadImage("\\Opening\\" + "Loading.gif") ;
//    	
//	}
	
	public void Run()
	{
		/*DrawPrimitives DP = DF.getDrawPrimitives() ;
		Font font = new Font("BoldSansSerif", Font.BOLD, 20) ;
		int NumberOfSlots = 3 ;
		int NumberOfTabs = 2 ;
		int NumberOfUsedSlots = 0 ;
		//Object[] LoadingResult = new Object[2] ;
		for (int i = 0 ; i <= NumberOfSlots - 1 ; ++i)
		{
			if (Utg.FindFile("save" + (i + 1) + ".txt"))
			{
				NumberOfUsedSlots += 1 ;
			}
		}
		if (Utg.FindFile("save" + (selectedSlot + 1) + ".txt"))
		{
			player = null ;
			pet = null ;
			player.Load("save" + (selectedSlot + 1) + ".txt", pet, maps) ;
			//Items.EquipsBonus = (float[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 33)], Items.EquipsBonus[0].length), "String", "float") ;
			//FirstNPCContact = (int[]) UtilGeral.ConvertArray(UtilGeral.toString(ReadFile[2*(NumberOfPlayerAttributes + 34)]), "String", "int") ;
			//DifficultLevel = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 35)][0]) ;
			if (player == null)
			{
				Game.DP.DrawText(new Point((int) (0.5 * screen.getSize().x), (int) (0.5 * screen.getSize().y)), "Center", DrawPrimitives.OverallAngle, "Save version is old, do you want to convert to a new version?", font, ColorPalette[5]) ;
			}
			else
			{
		 		//pet = (Pet)LoadingResult[1] ;	
		 		//Items.EquipsBonus = (float[][]) LoadingResult[2] ;
		 		//player.DifficultLevel = (int)LoadingResult[3] ;
		 		GameLanguage = player.getLanguage() ;
		 		AllText = Utg.ReadTextFile(GameLanguage) ;
				AllTextCat = Uts.FindAllTextCat(AllText, GameLanguage) ;
				npc = InitializeNPCs(GameLanguage, screen.getSize()) ;
		 		buildings = InitializeBuildings(screen.getSize(), npc) ;
				maps = InitializeMaps(screen, buildings, creatureTypes, sky) ;
				creatureTypes = InitializeCreatureTypes(GameLanguage, player.DifficultLevel) ;
				creature = InitializeCreatures(creatureTypes, screen.getSize(), maps, player.getStep()) ;
				spells = InitializeSpells(GameLanguage) ;
				petspells = InitializePetSpells() ;
				for (int map = 0 ; map <= maps.length - 1 ; map += 1)
				{
					maps[map].InitializeNPCsInMap(npc) ;
					maps[map].InitializeBuildings(buildings) ;
				}
				//items = InitializeItems(player.DifficultLevel, GameLanguage) ;
				quest = InitializeQuests(GameLanguage, player.getJob()) ;
				DF.DrawLoadingGameScreen(player, pet, plusSignIcon, selectedSlot, NumberOfUsedSlots, CoinIcon) ;
			}
	 	}
		else
		{
			DF.DrawEmptyLoadingSlot(selectedSlot, NumberOfSlots - 1) ;
		}
		selectedSlot = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], player.action, selectedSlot, NumberOfSlots - 1) ;
		tab = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], player.action, tab, NumberOfTabs) ;
		if (player.action.equals("Enter"))
		{
			selectedSlot += 1 ;
			if (0 < selectedSlot & selectedSlot <= NumberOfSlots)
			{
				if (Utg.FindFile("save" + selectedSlot + ".txt"))
				{
					//LoadingResult = Uts.Load("save" + LoadingSelectedSlot + ".txt") ;
					//player = (Player)LoadingResult[0] ;
			 		//pet = (Pet)LoadingResult[1] ;
			 		//Items.EquipsBonus = (float[][]) LoadingResult[2] ;
			 		PlayerInitialSex = player.getSex() ;		
			 		GameLanguage = player.getLanguage() ;	
			 		AllText = Utg.ReadTextFile(GameLanguage) ;		
				}
				LoadingGameIsOn = false ;
				CustomizationIsOn = true ;
			}
		}*/
	}
	
}
