package components ;

import java.awt.Color ;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;

import javax.swing.ImageIcon;

import graphics.Animations;
import graphics.DrawFunctions;
import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.Pet;
import liveBeings.Player;
import main.Game;
import maps.GameMap;
import utilities.Align;
import utilities.Scale;

public class NPCs
{
	private int id ;
	private NPCType type ;
	//private String Name ;
	private Point Pos ;
	//private int Map ;
	//private String PosRelToBuilding ;
	private String[] options ;
	private int selOption ;
	private int menu ;
	private int window ;
	//public boolean Firstcontact ;

	public static Image SpeakingBubbleImage = new ImageIcon(Game.ImagesPath + "SpeakingBubble.png").getImage() ;
	
	public NPCs (int id, NPCType type, Point Pos, String[] options)
	{
		this.id = id ;
		this.type = type ;
		//this.Name = Name ;
		this.Pos = Pos ;
		//this.image = image ;
		//this.Map = Map ;
		//this.PosRelToBuilding = PosRelToBuilding ;
		//this.Info = Info ;
		//this.color = color ;
		this.options = options ;
		selOption = 0 ;
		menu = 0 ;
		//Firstcontact = true ;
		
		/*
		 * private int ScreenL, ScreenH ;
			private int TextCat ;
			private String[][] AllText ;
			private int[] AllTextCat ;  
			private Icon[] icons ;
			
			public int[] Menu ;
			private int[] SelectedItem, SelectedWindow ;
			private String[][] AcceptedChoices ;
			private int ElementalEquipID ;
			private String TypedDeposit, TypedWithdraw ;
			private int SelectedSkill ;
			private int FabAmount ;
			private int ForgeEquip ;
			private int selChoice ;
		
			private static String NPCTextFontName = Game.MainFontName ;
			private static Font NPCTextFont ;
			private static Image SpeakingBubbleImage, BoatImage ;
		 * 
		 * ScreenL = Game.getScreen().getSize().x ;
		ScreenH = Game.getScreen().getSize().y ;
		this.AllText = AllText ;
		this.AllTextCat = AllTextCat ;
		this.icons = icons ;

		Menu = new int[149] ;
		SelectedItem = new int[3] ;		// [Elemental, Crafting, Shopping]
		SelectedWindow = new int[2] ; 	// [Crafting, Shopping]
		AcceptedChoices = new String[6][] ;
		if (player.getLanguage().equals("P"))
		{
			AcceptedChoices[0] = new String[] {"Sim", "N�o"} ;
			AcceptedChoices[1] = new String[] {"Vender", "Comprar", "Nada"} ;
			AcceptedChoices[2] = new String[] {"Colocar dinheiro", "Sacar dinheiro", "S� estou passando"} ;
			AcceptedChoices[3] = new String[] {"Arma", "Escudo/Bandana", "Vestimenta"} ;
			AcceptedChoices[4] = new String[] {"Slot 1", "Slot 2", "Slot 3"} ;
			AcceptedChoices[5] = new String[] {"Enter"} ;
		} else if (player.getLanguage().equals("E"))
		{
			AcceptedChoices[0] = new String[] {"(1) Yes", "(2) No"} ;
			AcceptedChoices[1] = new String[] {"(1) Sell", "(2) Buy", "(3) Nothing"} ;
			AcceptedChoices[2] = new String[] {"(1) Deposit", "(2) Withdraw", "(3) Nope"} ;
			AcceptedChoices[3] = new String[] {"Weapon (1)", "Shield (2)", "Armor (3)"} ;
			AcceptedChoices[4] = new String[] {"(1)", "(2)", "(3)"} ;
			AcceptedChoices[5] = new String[] {"Enter"} ;
		}		
		ElementalEquipID = -1 ;
		TypedDeposit = "" ;
		TypedWithdraw = "" ;
		SelectedSkill = 1 ;
		FabAmount = 1 ;
		ForgeEquip = -1;
		
		NPCTextFont = new Font(NPCTextFontName, Font.BOLD, 20) ;
		SpeakingBubbleImage = new ImageIcon(ImagesPath + "SpeakingBubble.png").getImage() ;
		BoatImage = new ImageIcon(ImagesPath + "Boat.png").getImage() ;
		 * */
	}

	public int getID() {return id ;}
	//public String getName() {return Name ;}
	public Point getPos() {return Pos ;}
	//public Image getImage() {return image ;}
	//public int getMap() {return Map ;}
	//public String getPosRelToBuilding() {return PosRelToBuilding ;}
	//public String getInfo() {return Info ;}
	//public Color getColor() {return color ;}
	public void setID(int I) {id = I ;}
	//public void setName(String N) {Name = N ;}
	public void setPos(Point P) {Pos = P ;}
	//public void setImage(Image I) {image = I ;}
	//public void setMap(int M) {Map = M ;}
	//public void setPosRelToBuilding(String P) {PosRelToBuilding = P ;}
	//public void setInfo(String I) {Info = I ;}
	//public void setColor(Color C) {color = C ;}
	

	public void Contact(Player player, Pet pet, Creature[] creatures, GameMap[] maps, Quests[] quest, Point MousePos, boolean TutorialIsOn, Animations Ani, DrawFunctions DF)
	{
		String action = player.getPA().getCurrentAction() ;
		navigate(action) ;
		/*if (npc.getName().equals("Doctor") | npc.getName().equals("Doutor"))
		{
			Doctor(Choice, player, pet, npc, DF) ;
		}*/
		/*if (npc.getName().equals("Equips Seller") | npc.getName().equals("Vendedor de equipamentos"))
		{
			Sale(Choice, player, npc, items, MousePos, Player.CoinIcon, DF) ;
		}
		if (npc.getName().equals("Items Seller") | npc.getName().equals("Vendedor de itens"))
		{
			Sale(Choice, player, npc, items, MousePos, Player.CoinIcon, DF) ;
		}
		if (npc.getName().equals("Smuggle Seller") | npc.getName().equals("Contrabandista"))
		{
			Sale(Choice, player, npc, items, MousePos, Player.CoinIcon, DF) ;
		}
		if (npc.getName().equals("Banker") | npc.getName().equals("Banqueiro"))
		{
			Bank(Choice, player, npc, Player.CoinIcon, DF) ;
		}
		if (npc.getName().equals("Alchemist") | npc.getName().equals("Alquimista"))
		{
			Crafter(Choice, player, npc, items, MousePos, DF) ;
		}
		if (npc.getName().equals("Woodcrafter") | npc.getName().equals("Madeireiro"))
		{			
			Crafter(Choice, player, npc, items, MousePos, DF) ;
		}		
		if (npc.getName().equals("Forger") | npc.getName().equals("Forjador"))
		{
			Forger(Choice, player, pet, npc, items, maps, DF) ;
		}
		if (npc.getName().equals("Crafter") | npc.getName().equals("Fabricante"))
		{
			Crafter(Choice, player, npc, items, MousePos, DF) ;
		}
		if (npc.getName().equals("Elemental") | npc.getName().equals("Elemental"))
		{
			Elemental(Choice, player, npc, items, DF) ;
		}
		if (npc.getName().equals("Saver") | npc.getName().equals("Salvador"))
		{
			Saver(Choice, player, pet, npc, DF) ;
		}*/
		if (type.getName().equals("Master") | type.getName().equals("Mestre"))
		{
			masterAction(player, pet, MousePos, DF) ;
		}/*
		if (npc.getName().contains("Quest") | npc.getName().contains("Quest"))
		{
			Quest(Choice, player, pet, creatureTypes, creatures, npc, quest, items, DF) ;
		}
		if (npc.getName().contains("Cave") | npc.getName().contains("caverna"))
		{
			CaveEntrance(player, maps) ;
		}
		if (npc.getName().contains("Sailor") | npc.getName().contains("Navegador"))
		{
			Sailor(Choice, player, npc, BoatImage, Ani, DF) ;
		}
		if (npc.getName().contains("Citizen") | npc.getName().contains("Cidad�o"))
		{
			Citizen(Choice, npc, DF) ;
		}*/
	}
	
	public void navigate(String action)
	{
		switchOption(action) ;
		if (action.equals("Enter"))
		{
			enterMenu() ;
		}
		if (action.equals("Escape") & 0 < menu)
		{
			menu += -1 ;
			window = 0 ;
		}
	}
	
	public void switchOption(String action)
	{
		if (action.equals(Player.ActionKeys[2]))
		{
			if (selOption <= options.length - 2)
			{
				selOption += 1 ;
			}
		}
		if (action.equals(Player.ActionKeys[0]))
		{
			if (0 < selOption)
			{
				selOption += -1 ;
			}
		}
	}
	
	public void enterMenu()
	{
		menu = selOption ;
	}
	
	public void masterAction(Player player, Pet pet, Point MousePos, DrawFunctions DF)
	{
		String[] speech = player.allText.get("* " + type.getName() + " *") ;
		int[] numberOfSpellsAvailablePerPlayerJob = new int[] {14, 15, 15, 14, 14} ;	// TODO olha esse nome...
		Font NPCTextFont = new Font(Game.MainFontName, Font.BOLD, 20) ;
		//System.out.println(selOption + " " + menu) ;
		if (menu == 0)
		{
			if (player.getProJob() == 0 & 50 <= player.getLevel())
			{
				//DF.DrawSpeech(Pos, speech[3], NPCTextFont, image, SpeakingBubbleImage, color) ;
			}
			else
			{
				//DF.DrawSpeech(Pos, speech[1], NPCTextFont, image, SpeakingBubbleImage, color) ;
			}
			//DF.DrawOptionsWindow(Pos, NPCTextFont, selOption, options, image, color) ;
			
		}
		/*else if (menu == 1)
		{
			if (player.getProJob() == 0 & 50 <= player.getLevel())
			{
				DF.DrawSpeech(Pos, AllText[TextCat][4], NPCTextFont, image, SpeakingBubbleImage, color) ;
				//DF.DrawChoicesWindow(Pos, NPCTextFont, new String[] {"(1) " + AllText[AllTextCat[5]][2*player.getJob() + 1], "(2) " + AllText[AllTextCat[5]][2*player.getJob() + 2]}, image, color) ;
				if (Choice.equals("1"))
				{ 
					player.setProJob(1) ;
					menu = 3 ;
				} else if (Choice.equals("2"))
				{ 
					player.setProJob(2) ;
					for (int i = NumberOfSkillsAvailable[player.getJob()] ; i <= NumberOfSkillsAvailable[player.getJob()] + 10 - 1 ; i += 1)
					{
						skills[i] = skills[i + 10] ;
					}
					menu = 3 ;
				}
			}
			else
			{
				int[] Pos = new int[] {(int)(0.1*ScreenL), (int)(0.9*ScreenH)} ;
				int H = (int)(0.66*ScreenH) ;
				Size size = new Size((int)(0.2*ScreenL), (int)(0.1*ScreenH)) ;
				int Sx = size.x / 10, Sy = size.y / 10 ;
				int[] Sequence = player.GetSpellSequence() ;
				int[] x0 = new int[] {(int)(0.03*ScreenL + size.x + Sx), (int)(0.03*ScreenL + size.x / 2 + Sx / 2), (int)(0.03*ScreenL)} ;
				int id = 0 ;
				for (int row = 0 ; row <= Sequence.length - 1 ; row += 1)
				{
					for (int col = 0 ; col <= Sequence[row] - 1 ; col += 1)
					{
						int[] RectCenter = new int[] {(int) (Pos[0] + x0[Sequence[row] - 1] + (size.x + Sx)*col + size.x/2), (int) (Pos[1] - H + size.y/2 + Sy/2 + (size.y + Sy)*row)} ;
						if (UtilG.isInside(MousePos, new Point(RectCenter[0] - size.x/2, RectCenter[1] - size.y/2), size))
						{
							SelectedSkill = id ;
						}
						id += 1 ;
					}
				}
				
				DF.DrawSpellsTree(player, skills, MousePos, SelectedSkill, icons[8]) ;
				if (0 < player.getProJob())
				{
					NumberOfSkillsAvailable = new int[] {24, 25, 25, 24, 24} ;
				}
				if (Choice.equals(Player.ActionKeys[0]) & SelectedSkill < NumberOfSkillsAvailable[player.getJob()] - 1)
				{
					++SelectedSkill ;
				}
				if (Choice.equals(Player.ActionKeys[2]) & 0 < SelectedSkill)
				{
					--SelectedSkill ;
				}
				if(Choice.equals("Enter") | Choice.equals("MouseLeftClick"))
				{
					if (UtilS.SpellIsAvailable(player, skills, SelectedSkill) & 0 < player.getSkillPoints() & player.getSpell()[SelectedSkill] < skills[SelectedSkill].getMaxLevel())
					{
						++player.getSpell()[SelectedSkill] ;
						skills[SelectedSkill].setMpCost((float)1.2*skills[SelectedSkill].getMpCost()) ;
						player.addSkillPoint(-1) ;
						ActivatePassiveSkill(player, pet, SelectedSkill) ;
					}
				}
			}	
		}*/
		else if (menu == 2)
		{
			if (player.getProJob() == 0 & 50 <= player.getLevel())
			{
				//DF.DrawSpeech(Pos, speech[6], NPCTextFont, image, SpeakingBubbleImage, color) ;
				//DF.DrawChoicesWindow(Pos, NPCTextFont, AcceptedChoices[0], image, color) ;
				/*if (Choice.equals("1"))
				{ 
					menu = 1 ;
				}*/
			}
			else
			{
				//DF.DrawSpeech(Pos, speech[2], NPCTextFont, image, SpeakingBubbleImage, color) ;
			}
		}
		else if (menu == 3)
		{
			//DF.DrawSpeech(Pos, speech[5] + " " + speech[player.getProJob() + 2*player.getJob()] + "!", NPCTextFont, image, SpeakingBubbleImage, color) ;
		}
	}
	
	public void display(DrawingOnPanel DP)
	{
		//if (id == 11 + 17*player.getMap())	// Master
		//{
		//	player.display(Pos, new float[] {1, 1}, Player.MoveKeys[3], false, DP) ;
		//}
		//else
		//{
		DP.DrawImage(type.getImage(), Pos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.bottomCenter) ;
		//DP.DrawText(Pos, Align.bottomCenter, DrawingOnPanel.stdAngle, String.valueOf(id), new Font("SansSerif", Font.BOLD, 12), Color.blue) ;				
	}

	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/

	/*public String ChoicesMenu(String playerAction, NPCs npc, String[] choiceOption, DrawFunctions DF)
	{
		DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, selChoice, choiceOption, npc.getImage(), npc.getColor()) ;
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
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;	
				//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
				if (Choice.equals("1"))
				{
					Menu[npc.getID()] = 1 ;
				} else if (Choice.equals("2"))
				{
					DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		}
		if (2 <= Menu[npc.getID()])
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()]], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;	
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[1], npc.getImage(), npc.getColor()) ;
			if (Choice.equals("1"))
			{
				ItemsOnSale = player.getBag() ;
				Menu[npc.getID()] = 1 ;
			} else if (Choice.equals("2"))
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
				Menu[npc.getID()] = 2 ;
			} else if (Choice.equals("3"))
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
				Menu[npc.getID()] = 3 ;
			}
		} else if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			ItemsOnSale = Utg.ArrayWithIndexesGreaterThan(player.getBag(), 0) ;
			if (0 < ItemsOnSale.length)
			{
				Menu[npc.getID()] = Shopping(Choice, player, npc, items, "Selling", ItemsOnSale, MousePos, Player.CoinIcon, DF) ;
			}
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			ItemsOnSale = Utg.ArrayWithValuesGreaterThan(ItemsOnSale, 0) ;
			Menu[npc.getID()] = Shopping(Choice, player, npc, items, "Buying", ItemsOnSale, MousePos, Player.CoinIcon, DF) ;
		} else if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[2], npc.getImage(), npc.getColor()) ;
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
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][9], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		} else if (3 < Menu[npc.getID()])
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()]], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Forger(String Choice, Player player, Pet pet, NPCs npc, Items[] items, Maps[] maps, DrawFunctions DF)
	{
		int ForgeResult = 0 ;	// 0: Forge has not been attempted yet, 1: forge was successful, 2: the player doesn't have the rune, 3: the equip is not equipped, 4: forge failed
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
			if (Choice.equals("1"))
			{
				Menu[npc.getID()] = 1 ;
			} else if (Choice.equals("2"))
			{	
				Menu[npc.getID()] = 2 ;
			}
		} else if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[3], npc.getImage(), npc.getColor()) ;
			if (Choice.equals("1") | Choice.equals("2") | Choice.equals("3"))
			{
				ForgeEquip = player.getEquips()[Integer.parseInt(Choice) - 1] ;
				Menu[npc.getID()] = 3 ;
			} 
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][7], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		} 
		if (Menu[npc.getID()] == 3)
		{
			ForgeResult = Forging(player, pet, npc, items, ForgeEquip, maps) ;
			Menu[npc.getID()] = 4 ;
		}
		if (Menu[npc.getID()] == 4)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][ForgeResult + 2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Forger(String Choice, Player player, Pet pet, NPCs npc, Items[] items, Maps[] maps, DrawFunctions DF)
	{
		int ForgeResult = 0 ;	// 0: Forge has not been attempted yet, 1: forge was successful, 2: the player doesn't have the rune, 3: the equip is not equipped, 4: forge failed
		String[] choices = AcceptedChoices[0] ;
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			if (ChoicesMenu(Choice, npc, choices, DF) != null)
			{
				Menu[npc.getID()] = Integer.parseInt(ChoicesMenu(Choice, npc, AcceptedChoices[0], DF)) ;
			}
		} else if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[3], npc.getImage(), npc.getColor()) ;
			if (Choice.equals(choices[0]) | Choice.equals(choices[1]) | Choice.equals(choices[2]))
			{
				ForgeEquip = player.getEquips()[Integer.parseInt(Choice) - 1] ;
				Menu[npc.getID()] = 3 ;
			} 
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][7], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		} 
		if (Menu[npc.getID()] == 3)
		{
			ForgeResult = Forging(player, pet, npc, items, ForgeEquip, maps) ;
			Menu[npc.getID()] = 4 ;
		}
		if (Menu[npc.getID()] == 4)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][ForgeResult + 2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
			if (Choice.equals("1"))
			{
				Menu[npc.getID()] = 1 ;
			} else if (Choice.equals("2"))
			{
				Menu[npc.getID()] = 3 ;
			}
		} if (Menu[npc.getID()] == 1)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()] + 1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			PlayerHasTheIngredients = Crafting(Choice, player, npc, items, NPCCraftingIngredients, NPCCraftingIngredientAmounts, NPCCraftingProducts, NPCCraftingProductAmounts, MousePos, DF) ;			
			if (Choice.equals("Enter") | Choice.equals("MouseLeftClick"))
			{
				Menu[npc.getID()] = 2 ;
			}
		} if (Menu[npc.getID()] == 2)
		{
			if (PlayerHasTheIngredients)
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
				Menu[npc.getID()] = 3 ;
			}
			else
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			}
		} if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		}
	}*/
	/*public int Elemental(String Choice, Player player, NPCs npc, Items[] items, DrawFunctions DF)
	{
		int[] Ingredients = new int[] {1690, 1691, 1692, 1693, 1694, 1695, 1696, 1697, 1698, 1699} ;
		SelectedItem[0] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], Choice, SelectedItem[0], Ingredients.length - 1) ;
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, new Color(200, 200, 200)) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[3], npc.getImage(), npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][7], NPCTextFont, npc.getImage(), SpeakingBubbleImage, new Color(200, 200, 200)) ;
		} else if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc.getImage(), SpeakingBubbleImage, new Color(200, 200, 200)) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()]], NPCTextFont, npc.getImage(), SpeakingBubbleImage, new Color(200, 200, 200)) ;
		}
		return Menu[npc.getID()] ;
	}*/
	/*public int Saver(String Choice, Player player, Pet pet, NPCs npc, DrawFunctions DF)
	{
		String FileName = "" ;
		if (Menu[npc.getID()] == 0)
		{
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[4], npc.getImage(), npc.getColor()) ;
			if (Choice.equals("1") | Choice.equals("2") | Choice.equals("3"))
			{ 
				FileName = "save" + Choice + ".txt" ;
				Save(player, pet, FileName) ;
				Menu[npc.getID()] = 3 ;
			}
		} else if (Menu[npc.getID()] == 2)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
		} else if (Menu[npc.getID()] == 3)
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][3], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
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
				DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;	
				//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
				if (Choice.equals("1"))
				{ 
					Menu[npc.getID()] = 1 ;
				} else if (Choice.equals("2"))
				{ 
					Menu[npc.getID()] = 2 ;
				}
			} else if (QuestIsComplete(player, quest, npcID))	// Quest is complete
			{
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][5], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
				//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
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
				DF.DrawSpeech(npc.getPos(), AllText[TextCat][4], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
				DF.DrawQuestRequirementsList(creatureTypes, creatures, items, quest[npcID], npc, new int[] {npc.getPos()[0] - npc.getImage().getWidth(null), npc.getPos()[1] - npc.getImage().getHeight(null)}, QuestHasCreatures, QuestHasItems, SpeakingBubbleImage) ;
			}
		} else if (Menu[npc.getID()] == 1)	// Player accepted the quest
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
			DF.DrawQuestRequirementsList(creatureTypes, creatures, items, quest[npcID], npc, new int[] {npc.getPos()[0] - npc.getImage().getWidth(null), npc.getPos()[1] - npc.getImage().getHeight(null)}, QuestHasCreatures, QuestHasItems, SpeakingBubbleImage) ;
			player.getQuest()[npcID] = npcID ;
		} else if (Menu[npc.getID()] == 2)	// Player refused the quest
		{
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][6], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;	
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[0], npc.getImage(), npc.getColor()) ;
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
			DF.DrawSpeech(npc.getPos(),  AllText[TextCat][2], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;	
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
			DF.DrawSpeech(npc.getPos(), AllText[TextCat][Menu[npc.getID()] + 1], NPCTextFont, npc.getImage(), SpeakingBubbleImage, npc.getColor()) ;	
			//DF.DrawChoicesWindow(npc.getPos(), NPCTextFont, AcceptedChoices[5], npc.getImage(), npc.getColor()) ;
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
			if (Choice.equals("Enter") | Choice.equals("MouseLeftClick"))
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