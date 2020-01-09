package Main;

import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Actions.Battle;
import GameComponents.Buildings;
import GameComponents.Creatures;
import GameComponents.Items;
import GameComponents.Maps;
import GameComponents.NPCs;
import GameComponents.Pet;
import GameComponents.Player;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import Graphics.DrawFunctions;
import Speech.Speech;

public class Game 
{
	private int ScreenWidth, ScreenHeight;
	private Font TextFont;
	private int NumberOfSkills;
	private int NumberOfPetSkills;
	private int NumberOfQuests;
	private int NumberOfItems;
	private int NumberOfStatus;
	private int NumberOfCreatures;
	private int NumberOfMaps;
	private int NumberOfCreaturesPerMap;
	private int NumberOfBuildings;
	private int NumberOfNPCs;
	private int MinX, MinY, MaxX, MaxY;
	private DrawFunctions DF;

	public Game()
	{
		ScreenWidth = 700;
		ScreenHeight = 700;
		TextFont = new Font("SansSerif", Font.PLAIN, 20);
		NumberOfSkills = 25;
		NumberOfPetSkills = 10;
		NumberOfQuests = 50;
		NumberOfItems = 2000;
		NumberOfStatus = 13;
		NumberOfCreatures = 500;
		NumberOfMaps = 50;
		NumberOfCreaturesPerMap = 5;
		NumberOfBuildings = 30;
		NumberOfNPCs = 115;
		MinX = 0;
		MinY = 0;
		MaxX = 100;
		MaxY = 100;
		DF = new DrawFunctions(ScreenWidth, ScreenHeight);
	}
	
	public void temp()
	{
		//NPCsMethods NPC = new NPCsMethods();
		Player player = new Player("", "", "", 0, 0, 0, 0, new int[3], new int[NumberOfSkills], new int[NumberOfQuests], new int[NumberOfItems], 0, 0, 0, 0, 0, 0, new float[3], new float[3], new float[3], new float[3], new float[3], new float[3], new float[] {0, 0}, new String[5], new float[3], 0, 0, 0, 0, 0, 0, false, new boolean[NumberOfStatus]);		
		Pet pet = new Pet("", 0, new int[3], new int[NumberOfPetSkills], 0, 0, 0, 0, 0, 0, new float[3], new float[3], new float[3], new float[3], new float[3], new float[3], new float[] {0, 0}, new String[5], 0, 0, 0, new boolean[NumberOfStatus]);
		Creatures[] creature = new Creatures[NumberOfCreatures];
		Items[] items = new Items[NumberOfItems];
		Buildings[] hospital = new Buildings[NumberOfBuildings];
		Maps[] maps = new Maps[NumberOfMaps];
		NPCs[] npc = new NPCs[NumberOfNPCs];
		
		InitialPlayerStats(player);
		InitialCreaturesStats(creature);
		ItemsProperties(items);
		BuildingsProperties(hospital);
		MapsProperties(maps);
		NPCsProperties(npc);
		Save(player, pet);
		//Battle B = new Battle();
		//System.out.println(B.ElemMult("w", "w", "f", "w", "n"));
		//NPC.Doctor(player, pet, npc[0], TextFont);
		Opening(player);
		/*do
		{
			//System.out.println("");
			//System.out.println("Player coords (" + player.getCoords()[0] + "," + player.getCoords()[1] + "," + player.getCoords()[2] + ") " + "Player map = " + player.getMap());
			PlayerMove(player);
			CreaturesMove(creature);
			int meet = CheckMeet(player, creature);
			if(meet > 0)
			{
				Battle(player, creature[meet], items);
			}
			if (player.getLife() < 0)
			{
				player.setGold((int)(0.8*player.getGold()));
				player.setLife(player.getLifeMax());
				player.setMp(player.getMpMax());
				ResetPlayerPosition(player);
			}
		} while (true);*/
	}
	
	public String PlayerChoice(Player player)
	{
		return DF.ReturnPlayerChoice();
		/*class ListenForButton implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				System.out.println("a");
				System.out.println(event.getSource());		
			}
			
		}*/
		/*KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( 
				new KeyEventDispatcher()
				{  
					public boolean dispatchKeyEvent(KeyEvent e)
					{
						if(e.getID() == KeyEvent.KEY_PRESSED)
						{
							if (e.getKeyCode() == 37)	// Left arrow key
							{
								Choice = "n";
							}
							if (e.getKeyCode() == 38)	// Up arrow key
							{
								if(player.getCoords()[0] > MinX)
								{
									++player.getCoords()[1];
									player.setCoords(player.getCoords());						
								}
								else
								{
									MovePlayerMap(player, "w");
								}
							}
							if (e.getKeyCode() == 39)	// Right arrow key
							{
								if(player.getCoords()[1] > MinY)
								{
									++player.getCoords()[0];
									player.setCoords(player.getCoords());	
								}
								else
								{
									MovePlayerMap(player, "d");
								}
							}					
						}
						return false;
					}  
				});*/
		/*KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( 
		new KeyEventDispatcher()
		{  
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if(e.getID() == KeyEvent.KEY_PRESSED)
				{
					if (e.getKeyCode() == 78)	// n key
					{
						Choice = "n";
					}
				}
				if(e.getID() == KeyEvent.KEY_PRESSED)
				{
					if (e.getKeyCode() == 87)	// w key
					{
						Choice = "w";
					}
				}
				return false;
			}
			
		});*/
	}
	
	public void Opening(Player player)
	{
		//DrawScreens DS = new DrawScreens();
		Speech S = new Speech();
		//Scanner in = new Scanner(System.in);
		String PlayerChoice;
		String PlayerLanguage = "";
		String PlayerName = "";
		String PlayerSex = "";
		int PlayerJob = 0;
		
		DF.OpeningScreen();
		DF.DrawText(new int[] {(int)(0.28*ScreenWidth), (int)(0.2*ScreenHeight)}, TextFont, S.OpeningText("* Boas vindas *", 1), 60, Color.BLUE);
		DF.DrawText(new int[] {(int)(0.28*ScreenWidth), (int)(0.3*ScreenHeight)}, TextFont, S.OpeningText("* Boas vindas *", 2), 40, Color.BLUE);
		//PlayerChoice = in.nextLine();	// Reads the player choice
		do
		{
			//System.out.println("Player choice = " + PlayerChoice);
			PlayerChoice = PlayerChoice(player);
			System.out.println("Player choice = " + PlayerChoice);
		} while (!PlayerChoice.equals("N") & !PlayerChoice.equals("L"));
		DF.OpeningScreen();
		if (PlayerChoice.equals("N"))
		{
			DF.DrawText(new int[] {(int)(0.4*ScreenWidth), (int)(0.2*ScreenHeight)}, TextFont, S.OpeningText("* Novo jogo *", 1), 40, Color.BLUE);
			do
			{
				PlayerLanguage = PlayerChoice(player);
				System.out.println("Player choice = " + PlayerChoice);
			} while (!PlayerLanguage.equals("P") & !PlayerLanguage.equals("E"));
			DF.OpeningScreen();
			DF.DrawText(new int[] {(int)(0.5*ScreenWidth), (int)(0.2*ScreenHeight)}, TextFont, S.OpeningText("* Novo jogo *", 2), 40, Color.BLUE);
			
			//PlayerName = in.nextLine();
			DF.OpeningScreen();
			DF.DrawText(new int[] {(int)(0.5*ScreenWidth), (int)(0.2*ScreenHeight)}, TextFont, S.OpeningText("* Novo jogo *", 3), 40, Color.BLUE);
			do
			{
				//PlayerSex = in.nextLine();
				PlayerSex = PlayerChoice(player);
				System.out.println("Player choice = " + PlayerChoice);
			} while (!PlayerSex.equals("M") & !PlayerSex.equals("F"));
			for (int i = 4; i <= 9; ++i)
			{
				System.out.println(S.OpeningText("* Novo jogo *", i));	
			}
			do
			{
				//PlayerJob = in.nextInt();
			} while (PlayerJob < 1 | PlayerJob > 5);
		}
		player.setLanguage(PlayerLanguage);
		player.setName(PlayerName);
		player.setSex(PlayerSex);
		player.setJob(PlayerJob);
	}
	
	public void InitialPlayerStats(Player player)
	{
		int PlayerJob = player.getJob();
		int PlayerProJob = 0;
		int PlayerContinent = 0;
		int PlayerMap = 0;
		int[] PlayerCoords = new int[3];
		int[] PlayerSkill = new int[NumberOfSkills];
		int[] PlayerQuest = new int[NumberOfQuests];
		int[] PlayerBag = new int[NumberOfItems];
		int PlayerSkillPoints = 0;
		float PlayerLife = 0;
		float PlayerMp = 0;
		float PlayerLifeMax = 0;
		float PlayerMpMax = 0;
		float PlayerRange = 0;
		float[] PlayerPhyAtk = new float[3];		// [Basic atk, bonus, train]
		float[] PlayerMagAtk = new float[3];		// [Basic atk, bonus, train]
		float[] PlayerPhyDef = new float[3];		// [Basic def, bonus, train]
		float[] PlayerMagDef = new float[3];		// [Basic def, bonus, train]
		float[] PlayerDex = new float[3];			// [Basic dex, bonus, train]
		float[] PlayerAgi = new float[3];			// [Basic agi, bonus, train]
		float[] PlayerCrit = new float[2];			// [Basic crit atk, bonus]
		String[] PlayerElem = new String[5];
		float[] PlayerCollect = new float[3];		// Herb, wood, metal
		int PlayerLevel = 0;
		int PlayerGold = 0;
		int PlayerStep = 0;
		float PlayerCraft = 0;
		float PlayerExp = 0;
		float PlayerHunger = 0;
		boolean PlayerRide = false;
		boolean[] PlayerStatus = new boolean[11];	// [Life, Mp, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Block, Blood, Poison]
		
		if (PlayerJob == 1)
		{
			PlayerLife = 100;
			PlayerMp = 50;
			PlayerPhyAtk = new float[] {5, 0, 0};
			PlayerMagAtk = new float[] {2, 0, 0};
			PlayerPhyDef = new float[] {5, 0, 0};
			PlayerMagDef = new float[] {2, 0, 0};
			PlayerDex = new float[] {3, 0, 0};
			PlayerAgi = new float[] {2, 0, 0};
			PlayerCrit = new float[] {(float)0.1, 0};
			PlayerRange = 1;
		}
		if (PlayerJob == 2)
		{
			PlayerLife = 50;
			PlayerMp = 100;
			PlayerPhyAtk = new float[] {2, 0, 0};
			PlayerMagAtk = new float[] {5, 0, 0};
			PlayerPhyDef = new float[] {2, 0, 0};
			PlayerMagDef = new float[] {5, 0, 0};
			PlayerDex = new float[] {3, 0, 0};
			PlayerAgi = new float[] {1, 0, 0};
			PlayerCrit = new float[] {(float)0.1, 0};
			PlayerRange = 4;
		}
		if (PlayerJob == 3)
		{
			PlayerLife = 60;
			PlayerMp = 80;
			PlayerPhyAtk = new float[] {3, 0, 0};
			PlayerMagAtk = new float[] {3, 0, 0};
			PlayerPhyDef = new float[] {3, 0, 0};
			PlayerMagDef = new float[] {3, 0, 0};
			PlayerDex = new float[] {8, 0, 0};
			PlayerAgi = new float[] {3, 0, 0};
			PlayerCrit = new float[] {(float)0.12, 0};
			PlayerRange = 6;
		}
		if (PlayerJob == 4)
		{
			PlayerLife = 70;
			PlayerMp = 70;
			PlayerPhyAtk = new float[] {4, 0, 0};
			PlayerMagAtk = new float[] {3, 0, 0};
			PlayerPhyDef = new float[] {4, 0, 0};
			PlayerMagDef = new float[] {3, 0, 0};
			PlayerDex = new float[] {4, 0, 0};
			PlayerAgi = new float[] {6, 0, 0};
			PlayerCrit = new float[] {(float)0.15, 0};
			PlayerRange = 1;
		}
		if (PlayerJob == 5)
		{
			PlayerLife = 30;
			PlayerMp = 50;
			PlayerPhyAtk = new float[] {3, 0, 0};
			PlayerMagAtk = new float[] {2, 0, 0};
			PlayerPhyDef = new float[] {3, 0, 0};
			PlayerMagDef = new float[] {2, 0, 0};
			PlayerDex = new float[] {4, 0, 0};
			PlayerAgi = new float[] {8, 0, 0};
			PlayerCrit = new float[] {(float)0.18, 0};
			PlayerRange = 1;
		}
		PlayerContinent = 1;
		PlayerMap = PlayerJob;
		PlayerLifeMax = PlayerLife;
		PlayerMpMax = PlayerMp;
		PlayerElem = new String[] {"n", "n", "n", "n", "n"};
		PlayerSkill[0] = 1;
		PlayerLevel = 1;
		PlayerStep = 1;
		PlayerCoords = new int[] {50, 50, 50};
		player.setProJob(PlayerProJob);
		player.setContinent(PlayerContinent);
		player.setMap(PlayerMap);
		player.setCoords(PlayerCoords);
		player.setSkill(PlayerSkill);
		player.setQuest(PlayerQuest);
		player.setBag(PlayerBag);
		player.setSkillPoints(PlayerSkillPoints);
		player.setLife(PlayerLife);
		player.setMp(PlayerMp);
		player.setLifeMax(PlayerLifeMax);
		player.setMpMax(PlayerMpMax);
		player.setRange(PlayerRange);
		player.setPhyAtk(PlayerPhyAtk);
		player.setMagAtk(PlayerMagAtk);
		player.setPhyDef(PlayerPhyDef);
		player.setMagDef(PlayerMagDef);
		player.setDex(PlayerDex);
		player.setAgi(PlayerAgi);
		player.setCrit(PlayerCrit);
		player.setElem(PlayerElem);
		player.setCollect(PlayerCollect);
		player.setLevel(PlayerLevel);
		player.setGold(PlayerGold);
		player.setStep(PlayerStep);
		player.setCraft(PlayerCraft);
		player.setExp(PlayerExp);
		player.setHunger(PlayerHunger);
		player.setRide(PlayerRide);
		player.setStatus(PlayerStatus);
	}
	
	public void InitialCreaturesStats(Creatures[] creature)
	{
		for (int i = 0; i <= NumberOfCreatures - 1; ++i)
		{
			creature[i] = new Creatures(0, 0, new int[] {50, 50, 50}, new int[] {1}, new int[] {1, 2, 3, 4, 5}, 50, 20, 50, 20, 1, new float[] {5, 0}, new float[] {3, 0}, new float[] {2, 0}, new float[] {2, 0}, new float[] {5, 0}, new float[] {5, 0}, 	new float[] {(float)0.1, 0}, new String[] {"n", "n", "n", "n", "n"}, 10, 1, 50, new boolean[11]);
			/*creature[i].setContinent(0);
			creature[i].setMap(0);
			creature[i].setCoords(new float[] {50, 50, 50});
			creature[i].setSkill(new int[] {1});
			creature[i].setBag(new int[] {1, 2, 3, 4, 5});
			creature[i].setLife(50);
			creature[i].setMp(20);
			creature[i].setLifeMax(creature[i].getLife());
			creature[i].setMpMax(creature[i].getMp());
			creature[i].setRange(1);
			creature[i].setPhyAtk(new float[] {5, 0});
			creature[i].setMagAtk(new float[] {3, 0});
			creature[i].setPhyDef(new float[] {2, 0});
			creature[i].setMagDef(new float[] {2, 0});
			creature[i].setDex(new float[] {5, 0});
			creature[i].setAgi(new float[] {5, 0});
			creature[i].setGold(10);
			creature[i].setStep(1);
			creature[i].setExp(5);
			creature[i].setStatus(new boolean[11]);*/
		}
	}
	
	public void ItemsProperties(Items[] items)
	{
		String[] Name = new String[NumberOfItems];
		int[] Price = new int[NumberOfItems], DropChance = new int[NumberOfItems];
		Name = new String[] {"tiny hp","tiny mp","small hp","small mp","better hp","better mp","medium hp","medium mp","dense hp","dense mp","big hp","big mp","master hp","master mp","huge hp","huge mp","mighty hp","mighty mp","legend hp","legend mp","small fire","small water","small earth","small grass","small air","small bolt","Medium fire","Medium water","Medium earth","Medium grass","Medium air","Medium bolt","Big fire","Big water","Big earth","Big grass","Big air","Big bolt","Big dark","Big light","Small Dragon","Medium Dragon","Big Dragon","Big snow","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","herb","wood","metal","herb","wood","metal","herb","wood","metal","herb","wood","metal","herb","wood","metal","herb","wood","metal","herb","wood","metal","herb","wood","metal","herb","wood","metal","herb","wood","metal","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","small atk rune","small def rune","medium atk rune","medium def rune","big atk rune","big def rune","rough atk rune","rough def rune","strong atk rune","strong def rune","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","food","wood equip","wood weapon","apple smoothie","banana smoothie","straw smoothie","blackb smoothie","pet exp","atk train","def train","steel equip","steel weapon","hp medium pot","mini coconut","pet medium exp","refresh water","high hp pot","high mp pot","pet high exp","special equip","-","-","mp medium pot","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","apple","banana","strawberry","chilli peper","blackberry","mushroom","fish","raspberry","lemon","orange","tomato","carrot","honey","mango","watermelon","grapes","fern","meat","bat","cave rat","coconut","crab","shrimp","oyster","Soup","Hot beverage","Coffee","Boiling water","Noodles","Yakisoba","Melted cheese","Dragon's food","Hot chocolate","Milk","Pizza","Magic soup","Ice Cream","Iced tea","Sweeties","Chocolate milk","coconut water","coconut meat","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","arrow","strong arrow","fire arrow","water arrow","earth arrow","grass arrow","air arrow","bolt arrow","dark arrow","light arrow","snow arrow","-","-","-","-","-","-","-","-","-","wood sword","wood shield","wood armor","Copper sword","Copper shield","Copper armor","light sword","light shield","light armor","heavy sword","heavy shield","heavy armor","knight sword","knight shield","knight armor","super sword","super shield","super armor","iron sword","iron shield","iron armor","metal sword","metal shield","metal armor","rare sword","rare shield","rare armor","epic sword","epic shield","epic armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","Toy sword","Toy shield","Toy armor","rookie's sword","rookie's shield","rookie's armor","Dreamer's sword","Dreamer's shield","Dreamer's armor","mid sword","mid shield","mid armor","courage sword","courage shield","courage armor","partner's sword","partner's shield","partner's armor","evo sword","evo shield","evo armor","grow sword","grow shield","grow armor","love sword","love shield","love armor","Conquer's sword","Conquer's shield","Conquer's armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","train staff","train shield","train robe","great staff","great shield","great robe","power staff","power shield","power robe","Mystical staff","Mystical shield","Mystical robe","mage staff","mage shield","mage robe","arcane staff","arcane shield","arcane robe","magic staff","magic shield","magic robe","learn staff","learn shield","learn robe","wise staff","wise shield","wise robe","epic staff","epic shield","epic robe","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","Toy staff","Toy shield","Toy robe","rookie's staff","rookie's shield","rookie's robe","Dreamer's staff","Dreamer's shield","Dreamer's robe","mid staff","mid shield","mid robe","courage staff","courage shield","courage robe","partner's staff","partner's shield","partner's robe","evo staff","evo shield","evo robe","grow staff","grow shield","grow robe","love staff","love shield","love robe","Conquer's staff","Conquer's shield","Conquer's robe","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","wood bow","wood armor","long bow","long armor","flexible bow","flexible armor","strong bow","strong armor","precise bow","precise armor","reflex bow","reflex armor","archer bow","archer armor","fibre bow","fibre armor","recursive bow","recursive armor","modern bow","modern armor","speedy bow","speedy armor","composite bow","composite armor","keen bow","keen armor","self bow","self armor","epic bow","epic armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","Toy bow","Toy armor","simple bow","simple armor","rookie's bow","rookie's armor","Dreamer's bow","Dreamer's armor","fight bow","fight armor","mid bow","mid armor","courage bow","courage armor","partner's bow","partner's armor","journey bow","journey armor","evo bow","evo armor","effort bow","effort armor","grow bow","grow armor","love bow","love armor","happiness bow","happiness armor","Conquer's bow","Conquer's armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","sharpier claws","wood shield","wood armor","long claws","Copper shield","Copper armor","fast claws","light shield","light armor","iron claws","iron shield","iron armor","animal claws","animal shield","animal armor","deepy claws","rigid shield","rigid armor","stiff claws","stiff shield","stiff armor","mighty claws","mighty shield","mighty armor","nature claws","nature shield","nature armor","epic claws","epic shield","epic armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","Toy claws","Toy shield","Toy armor","rookie's claws","rookie's shield","rookie's armor","Dreamer's claws","Dreamer's shield","Dreamer's armor","mid claws","mid shield","mid armor","courage claws","courage shield","courage armor","partner's claws","partner's shield","partner's armor","evo claws","evo shield","evo armor","grow claws","grow shield","grow armor","love claws","love shield","love armor","Conquer's claws","Conquer's shield","Conquer's armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","kitchen dagger","wood shield","wood armor","Copper dagger","Copper shield","Copper armor","light dagger","light shield","light armor","iron dagger","iron shield","iron armor","assassin dagger","assassin shield","assassin armor","cutting dagger","super shield","super armor","stiff dagger","stiff shield","stiff armor","metal dagger","metal shield","metal armor","thin dagger","thick shield","thick armor","epic dagger","epic shield","epic armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","Toy dagger","Toy shield","Toy armor","rookie's dagger","rookie's shield","rookie's armor","Dreamer's dagger","Dreamer's shield","Dreamer's armor","-","mid shield","mid armor","courage dagger","courage shield","courage armor","partner's dagger","partner's shield","partner's armor","evo dagger","mid dagger","evo armor","grow dagger","grow shield","grow armor","love dagger","love shield","love armor","Conquer's dagger","Conquer's shield","Conquer's armor","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","fur","wings","nuts","grass","rocks","pot","spores","slime","feather","shell","ambrosia","ferns","ivy","maple leaf","tulsi","orchid","violet","blackhaw","dew","lilac","flower","doll","battery","tail","papyrus","worms","lit bottle","med bottle","big bottle","lit w bottle","med w bottle","big w bottle","wire","heavy rock","stiff wood","humus","shovel","ladder","String","Rope","fishing rod","shelter","teleport","map","book","knife","sphere of fire","sphere of water","sphere of grass","sphere of earth","sphere of air","sphere of bolt","stalactite","stalagmite","crystal","subrock","relic","fossil","bone","lime","mineral","crystalwater","crableg","lei","coconut shell","sand","beak","palm leaf","palm shell","shell","oyster","lobster","magma","ash","igneous rock","golem arm","serpent tail","square eye","Platinum","Sphere of dark","Sphere of light","Titanium","Core","Heart","Dragon's eyes","Dragon's ears","Dragon's skin","Dragon's head","Sphere of snow","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","ply","yard","copper ore","iron ore","flex wood","leather","cotton","plastic","latex","ferky","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","rigid shell","rabbit fur","polymer","strap","sand","branches","needle","proof of courage","proof of valor","proof of force","proof of honor","proof of benignity","proof of ambition","proof of agility","proof of crime","proof of money","proof of magic","proof of wisdom","proof of knowledge","proof of power","proof of dexterity","proof of determination","proof of peace","proof of equilibrum","proof of harmony","proof of wild","proof of nature","proof of simplicity","proof of loyalty","sharp rocks","stalk","jackknife","big leaf","scroll","ink","plume","long stick","hook","Elixir of life","sphere of slime","prayer","iron sheet","iron strip","tracing paper","sinew","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-"}; 
		Price = new int[] {5,5,10,10,15,15,20,20,25,25,30,30,35,35,40,40,45,45,50,50,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265,270,275,280,285,290,295,300,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265,270,275,280,285,290,295,300,20,20,60,60,100,100,140,140,180,180,220,220,260,260,300,300,340,340,380,380,420,420,460,460,500,500,540,540,580,580,620,620,660,660,700,700,740,740,780,780,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265,270,275,280,285,290,295,300,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265,270,275,280,285,290,295,300,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400,2500,2600,2700,2800,2900,3000,3100,3200,3300,3400,3500,3600,3700,3800,3900,4000,4100,4200,4300,4400,4500,4600,4700,4800,4900,5000,5100,5200,5300,5400,5500,5600,5700,5800,5900,6000,6100,6200,6300,6400,6500,6600,6700,6800,6900,7000,7100,7200,7300,7400,7500,7600,7700,7800,7900,8000,8100,8200,8300,8400,8500,8600,8700,8800,8900,9000,9100,9200,9300,9400,9500,9600,9700,9800,9900,10000,10100,10200,10300,10400,10500,10600,10700,10800,10900,11000,11100,11200,11300,11400,11500,11600,11700,11800,11900,12000,12100,12200,12300,12400,12500,12600,12700,12800,12900,13000,13100,13200,13300,13400,13500,13600,13700,13800,13900,14000,14100,14200,14300,14400,14500,14600,14700,14800,14900,15000,15100,15200,15300,15400,15500,15600,15700,15800,15900,16000,16100,16200,16300,16400,16500,16600,16700,16800,16900,17000,17100,17200,17300,17400,17500,17600,17700,17800,17900,18000,18100,18200,18300,18400,18500,18600,18700,18800,18900,19000,19100,19200,19300,19400,19500,19600,19700,19800,19900,20000,100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400,2500,2600,2700,2800,2900,3000,3100,3200,3300,3400,3500,3600,3700,3800,3900,4000,4100,4200,4300,4400,4500,4600,4700,4800,4900,5000,5100,5200,5300,5400,5500,5600,5700,5800,5900,6000,6100,6200,6300,6400,6500,6600,6700,6800,6900,7000,7100,7200,7300,7400,7500,7600,7700,7800,7900,8000,8100,8200,8300,8400,8500,8600,8700,8800,8900,9000,9100,9200,9300,9400,9500,9600,9700,9800,9900,10000,10100,10200,10300,10400,10500,10600,10700,10800,10900,11000,11100,11200,11300,11400,11500,11600,11700,11800,11900,12000,12100,12200,12300,12400,12500,12600,12700,12800,12900,13000,13100,13200,13300,13400,13500,13600,13700,13800,13900,14000,14100,14200,14300,14400,14500,14600,14700,14800,14900,15000,15100,15200,15300,15400,15500,15600,15700,15800,15900,16000,16100,16200,16300,16400,16500,16600,16700,16800,16900,17000,17100,17200,17300,17400,17500,17600,17700,17800,17900,18000,18100,18200,18300,18400,18500,18600,18700,18800,18900,19000,19100,19200,19300,19400,19500,19600,19700,19800,19900,20000,100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400,2500,2600,2700,2800,2900,3000,3100,3200,3300,3400,3500,3600,3700,3800,3900,4000,4100,4200,4300,4400,4500,4600,4700,4800,4900,5000,5100,5200,5300,5400,5500,5600,5700,5800,5900,6000,6100,6200,6300,6400,6500,6600,6700,6800,6900,7000,7100,7200,7300,7400,7500,7600,7700,7800,7900,8000,8100,8200,8300,8400,8500,8600,8700,8800,8900,9000,9100,9200,9300,9400,9500,9600,9700,9800,9900,10000,10100,10200,10300,10400,10500,10600,10700,10800,10900,11000,11100,11200,11300,11400,11500,11600,11700,11800,11900,12000,12100,12200,12300,12400,12500,12600,12700,12800,12900,13000,13100,13200,13300,13400,13500,13600,13700,13800,13900,14000,14100,14200,14300,14400,14500,14600,14700,14800,14900,15000,15100,15200,15300,15400,15500,15600,15700,15800,15900,16000,16100,16200,16300,16400,16500,16600,16700,16800,16900,17000,17100,17200,17300,17400,17500,17600,17700,17800,17900,18000,18100,18200,18300,18400,18500,18600,18700,18800,18900,19000,19100,19200,19300,19400,19500,19600,19700,19800,19900,20000,100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400,2500,2600,2700,2800,2900,3000,3100,3200,3300,3400,3500,3600,3700,3800,3900,4000,4100,4200,4300,4400,4500,4600,4700,4800,4900,5000,5100,5200,5300,5400,5500,5600,5700,5800,5900,6000,6100,6200,6300,6400,6500,6600,6700,6800,6900,7000,7100,7200,7300,7400,7500,7600,7700,7800,7900,8000,8100,8200,8300,8400,8500,8600,8700,8800,8900,9000,9100,9200,9300,9400,9500,9600,9700,9800,9900,10000,10100,10200,10300,10400,10500,10600,10700,10800,10900,11000,11100,11200,11300,11400,11500,11600,11700,11800,11900,12000,12100,12200,12300,12400,12500,12600,12700,12800,12900,13000,13100,13200,13300,13400,13500,13600,13700,13800,13900,14000,14100,14200,14300,14400,14500,14600,14700,14800,14900,15000,15100,15200,15300,15400,15500,15600,15700,15800,15900,16000,16100,16200,16300,16400,16500,16600,16700,16800,16900,17000,17100,17200,17300,17400,17500,17600,17700,17800,17900,18000,18100,18200,18300,18400,18500,18600,18700,18800,18900,19000,19100,19200,19300,19400,19500,19600,19700,19800,19900,20000,100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400,2500,2600,2700,2800,2900,3000,3100,3200,3300,3400,3500,3600,3700,3800,3900,4000,4100,4200,4300,4400,4500,4600,4700,4800,4900,5000,5100,5200,5300,5400,5500,5600,5700,5800,5900,6000,6100,6200,6300,6400,6500,6600,6700,6800,6900,7000,7100,7200,7300,7400,7500,7600,7700,7800,7900,8000,8100,8200,8300,8400,8500,8600,8700,8800,8900,9000,9100,9200,9300,9400,9500,9600,9700,9800,9900,10000,10100,10200,10300,10400,10500,10600,10700,10800,10900,11000,11100,11200,11300,11400,11500,11600,11700,11800,11900,12000,12100,12200,12300,12400,12500,12600,12700,12800,12900,13000,13100,13200,13300,13400,13500,13600,13700,13800,13900,14000,14100,14200,14300,14400,14500,14600,14700,14800,14900,15000,15100,15200,15300,15400,15500,15600,15700,15800,15900,16000,16100,16200,16300,16400,16500,16600,16700,16800,16900,17000,17100,17200,17300,17400,17500,17600,17700,17800,17900,18000,18100,18200,18300,18400,18500,18600,18700,18800,18900,19000,19100,19200,19300,19400,19500,19600,19700,19800,19900,20000,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265,270,275,280,285,290,295,300,305,310,315,320,325,330,335,340,345,350,355,360,365,370,375,380,385,390,395,400,405,410,415,420,425,430,435,440,445,450,455,460,465,470,475,480,485,490,495,500,505,510,515,520,525,530,535,540,545,550,555,560,565,570,575,580,585,590,595,600,605,610,615,620,625,630,635,640,645,650,655,660,665,670,675,680,685,690,695,700,705,710,715,720,725,730,735,740,745,750,755,760,765,770,775,780,785,790,795,800,805,810,815,820,825,830,835,840,845,850,855,860,865,870,875,880,885,890,895,900,905,910,915,920,925,930,935,940,945,950,955,960,965,970,975,980,985,990,995,1000,1005,1010,1015,1020,1025,1030,1035,1040,1045,1050,1055,1060,1065,1070,1075,1080,1085,1090,1095,1100,1105,1110,1115,1120,1125,1130,1135,1140,1145,1150,1155,1160,1165,1170,1175,1180,1185,1190,1195,1200,1205,1210,1215,1220,1225,1230,1235,1240,1245,1250,1255,1260,1265,1270,1275,1280,1285,1290,1295,1300,1305,1310,1315,1320,1325,1330,1335,1340,1345,1350,1355,1360,1365,1370,1375,1380,1385,1390,1395,1400,1405,1410,1415,1420,1425,1430,1435,1440,1445,1450,1455,1460,1465,1470,1475,1480,1485,1490,1495,1500,1505,1510,1515,1520,1525,1530,1535,1540,1545,1550,1555,1560,1565,1570,1575,1580,1585,1590,1595,1600,1605,1610,1615,1620,1625,1630,1635,1640,1645,1650,1655,1660,1665,1670,1675,1680,1685,1690,1695,1700,1705,1710,1715,1720,1725,1730,1735,1740,1745,1750,1755,1760,1765,1770,1775,1780,1785,1790,1795,1800,1805,1810,1815,1820,1825,1830,1835,1840,1845,1850,1855,1860,1865,1870,1875,1880,1885,1890,1895,1900,1905,1910,1915,1920,1925,1930,1935,1940,1945,1950,1955,1960,1965,1970,1975,1980,1985,1990,1995,2000,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265,270,275,280,285,290,295,300,305,310,315,320,325,330,335,340,345,350,355,360,365,370,375,380,385,390,395,400,405,410,415,420,425,430,435,440,445,450,455,460,465,470,475,480,485,490,495,500,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265,270,275,280,285,290,295,300,305,310,315,320,325,330,335,340,345,350,355,360,365,370,375,380,385,390,395,400,405,410,415,420,425,430,435,440,445,450,455,460,465,470,475,480,485,490,495,500,505,510,515,520,525,530,535,540,545,550,555,560,565,570,575,580,585,590,595,600,605,610,615,620,625,630,635,640,645,650,655,660,665,670,675,680,685,690,695,700,705,710,715,720,725,730,735,740,745,750,755,760,765,770,775,780,785,790,795,800,805,810,815,820,825,830,835,840,845,850,855,860,865,870,875,880,885,890,895,900,905,910,915,920,925,930,935,940,945,950,955,960,965,970,975,980,985,990,995,1000};
		DropChance = new int[] {20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20};
		for (int i = 0; i <= NumberOfItems - 1; ++i)
		{
			items[i] = new Items(i, Name[i], Price[i], DropChance[i]);
		}
	}
	
	public void BuildingsProperties(Buildings[] buildings)
	{
		String[] Name = new String[NumberOfBuildings];
		int[][] Pos = new int[NumberOfBuildings][3];
		Name = new String[] {"Hospital", "Hospital", "Hospital", "Hospital", "Hospital", "Store", "Store", "Store", "Store", "Store", "Forge", "Forge", "Forge", "Forge", "Forge", "Bank", "Bank", "Bank", "Bank", "Bank", "Craft", "Craft", "Craft", "Craft", "Craft", "Sign", "Sign", "Sign", "Sign", "Sign"};
		Pos = new int[][] {{50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}};
		for (int i = 0; i <= NumberOfBuildings - 1; ++i)
		{
			buildings[i] = new Buildings(Name[i], Pos[i], 1);
		}
	}

	public void NPCsProperties(NPCs[] npc)
	{
		String[] Name = new String[NumberOfNPCs];
		int[][] Pos = new int[NumberOfNPCs][3];
		Color[] color = new Color[NumberOfNPCs];
		Name = new String[] {"Doctor", "Doctor", "Doctor", "Doctor", "Doctor", "Equips Seller", "Equips Seller", "Equips Seller", "Equips Seller", "Equips Seller", "Items Seller", "Items Seller", "Items Seller", "Items Seller", "Items Seller", "Smuggle Seller", "Smuggle Seller", "Smuggle Seller", "Smuggle Seller", "Smuggle Seller", "Alchemist", "Alchemist", "Alchemist", "Alchemist", "Alchemist", "WoodCrafter", "WoodCrafter", "WoodCrafter", "WoodCrafter", "WoodCrafter", "Forger", "Forger", "Forger", "Forger", "Forger", "Crafter", "Crafter", "Crafter", "Crafter", "Crafter", "Elemental", "Elemental", "Elemental", "Elemental", "Elemental", "Saver", "Saver", "Saver", "Saver", "Saver", "Master knight", "Master mage", "Master archer", "Master animal", "Master assassin", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Quest", "Sailer", "Special"};
		Pos = new int[][] {{50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}, {50, 50, 50}};
		color = new Color[] {Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue, Color.blue};
		for (int i = 0; i <= NumberOfNPCs - 1; ++i)
		{
			npc[i] = new NPCs(Name[i], Pos[i], color[i]);
		}
	}
	
	public void MapsProperties(Maps[] maps)
	{
		String[] Name = new String[NumberOfMaps], Continent = new String[NumberOfMaps];
		boolean[][][] Walkable = new boolean[NumberOfMaps][MaxX][MaxY];
		Name = new String[] {"City of the knights"};
		Continent = new String[] {"City"};
		for (int i = 0; i <= NumberOfMaps - 1; ++i)
		{
			if (i == 0)
			{
				Walkable[i][0][0] = false;
			}			
		}
	}
	
	public void MovePlayerMap(Player player, String PlayerMove)
	{
		int[][] Maps = new int[NumberOfMaps][4];	// Maps[Current map][map above, map to the left, map below, map to the right]
		for(int i = 0; i <= NumberOfMaps - 1; ++i)
		{
			if(player.getMap() == i)
			{
				if (PlayerMove.equals("w"))
				{
					player.getCoords()[1] = MinY;
					player.setCoords(player.getCoords());
					player.setMap(Maps[i][0]);
				}
				if (PlayerMove.equals("a"))
				{
					player.getCoords()[0] = MaxX;
					player.setCoords(player.getCoords());
					player.setMap(Maps[i][1]);
				}
				if (PlayerMove.equals("s"))
				{
					player.getCoords()[1] = MaxY;
					player.setCoords(player.getCoords());
					player.setMap(Maps[i][2]);
				}
				if (PlayerMove.equals("d"))
				{
					player.getCoords()[0] = MinX;
					player.setCoords(player.getCoords());
					player.setMap(Maps[i][3]);
				}
			}
		}
	}
	
	public void PlayerMove(Player player)
	{
		Scanner in = new Scanner(System.in);
		String PlayerMove;
		/*
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( 
		new KeyEventDispatcher()
		{  
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if(e.getID() == KeyEvent.KEY_PRESSED)
				{
					if (e.getKeyCode() == 37)	// Left arrow key
					{
						if(player.getCoords()[1] < MaxY)
						{
							--player.getCoords()[0];
							player.setCoords(player.getCoords());
						}
						else
						{
							MovePlayerMap(player, "a");
						}
					}
					if (e.getKeyCode() == 38)	// Up arrow key
					{
						if(player.getCoords()[0] > MinX)
						{
							++player.getCoords()[1];
							player.setCoords(player.getCoords());						
						}
						else
						{
							MovePlayerMap(player, "w");
						}
					}
					if (e.getKeyCode() == 39)	// Right arrow key
					{
						if(player.getCoords()[1] > MinY)
						{
							++player.getCoords()[0];
							player.setCoords(player.getCoords());	
						}
						else
						{
							MovePlayerMap(player, "d");
						}
					}					
				}
				return false;
			}  
		});*/
		
		PlayerMove = in.nextLine();
		if (PlayerMove.equals("w"))
		{
			if(player.getCoords()[1] < MaxY)
			{
				++player.getCoords()[1];
				player.setCoords(player.getCoords());
			}
			else
			{
				MovePlayerMap(player, PlayerMove);
			}
		}
		if (PlayerMove.equals("a"))
		{
			if(player.getCoords()[0] > MinX)
			{
				--player.getCoords()[0];
				player.setCoords(player.getCoords());
			}
			else
			{
				MovePlayerMap(player, PlayerMove);
			}
		}
		if (PlayerMove.equals("s"))
		{
			if(player.getCoords()[1] > MinY)
			{
				--player.getCoords()[1];
				player.setCoords(player.getCoords());	
			}
			else
			{
				MovePlayerMap(player, PlayerMove);
			}
		}
		if (PlayerMove.equals("d"))
		{
			if(player.getCoords()[0] < MaxX)
			{
				++player.getCoords()[0];
				player.setCoords(player.getCoords());
			}
			else
			{
				MovePlayerMap(player, PlayerMove);
			}
		}
	}

	public void CreaturesMove(Creatures[] creature)
	{
		int CreatureMove;
		
		for (int i = 0; i <= 4; ++i)
		{
			CreatureMove = (int)(3*Math.random());
			if (CreatureMove == 0 & creature[i].getCoords()[1] < MaxY)
			{
				++creature[i].getCoords()[1];
				creature[i].setCoords(creature[i].getCoords());
			}
			if (CreatureMove == 1 & creature[i].getCoords()[0] > MinX)
			{
				--creature[i].getCoords()[0];
				creature[i].setCoords(creature[i].getCoords());
			}
			if (CreatureMove == 2 & creature[i].getCoords()[1] > MinY)
			{
				--creature[i].getCoords()[1];
				creature[i].setCoords(creature[i].getCoords());
			}
			if (CreatureMove == 3 & creature[i].getCoords()[0] < MaxX)
			{
				++creature[i].getCoords()[0];
				creature[i].setCoords(creature[i].getCoords());
			}
			//System.out.print("(" + creature[i].getCoords()[0] + "," + creature[i].getCoords()[1] + "," + creature[i].getCoords()[2] + ") ");
		}
	}
	
	public int CheckMeet(Player player, Creatures[] creature)
	{
		/* Meeting with creatures */
		for (int i = 0; i <= NumberOfCreaturesPerMap - 1; ++i)
		{
			//System.out.println("Number = " + i);
			//System.out.println(Arrays.toString(creature[i].getCoords()));
			if (player.getCoords() == creature[i].getCoords())
			{
				System.out.println("* Met! *");
				//System.out.println(Arrays.toString(player.getCoords()));
				return i;
			}
			
		}
		/* Meeting with buildings */
		/*for (int i = 0; i <= 4; ++i)
		{
			if (player.getCoords() == HosPos[i])
			{
				
			}
		}*/
		return 0;
	}

	public void ShowPlayerAttributes(Player player)
	{
		System.out.println("* Player attributes *");
		System.out.println("Player level = " + player.getLevel());
		System.out.println("Player life = " + player.getLife());
		System.out.println("Player mp = " + player.getMp());
		System.out.println("Player phy atk = " + player.getPhyAtk()[0]);
		System.out.println("Player mag atk = " + player.getMagAtk()[0]);
		System.out.println("Player phy def = " + player.getPhyDef()[0]);
		System.out.println("Player mag def = " + player.getMagDef()[0]);
		System.out.println("Player dex = " + player.getDex()[0]);
		System.out.println("Player agi = " + player.getAgi()[0]);		
	}
	
	public void LevelUp(Player player)
	{
		player.setLevel(player.getLevel() + 1);
		player.setLifeMax(player.getLifeMax() + 20);
		player.setMpMax(player.getMpMax() + 20);
		if (Math.random() < 0.2)
		{
			player.setPhyAtk(new float[] {player.getPhyAtk()[0] + 1, player.getPhyAtk()[1], player.getPhyAtk()[2]});
		}
		if (Math.random() < 0.2)
		{
			player.setMagAtk(new float[] {player.getMagAtk()[0] + 1, player.getMagAtk()[1], player.getMagAtk()[2]});
		}
		if (Math.random() < 0.2)
		{
			player.setPhyDef(new float[] {player.getPhyDef()[0] + 1, player.getPhyDef()[1], player.getPhyDef()[2]});
		}
		if (Math.random() < 0.2)
		{
			player.setMagDef(new float[] {player.getMagDef()[0] + 1, player.getMagDef()[1], player.getMagDef()[2]});
		}
		if (Math.random() < 0.2)
		{
			player.setDex(new float[] {player.getDex()[0] + 1, player.getDex()[1], player.getDex()[2]});
		}
		if (Math.random() < 0.2)
		{
			player.setAgi(new float[] {player.getAgi()[0] + 1, player.getAgi()[1], player.getAgi()[2]});
		}
	}
	
	public void Win(Player player, Creatures creature, Items[] items)
	{
		player.setLife(player.getLifeMax());
		for (int i = 0; i <= 9; ++i)
		{
			if(Math.random() < items[i].getDropChance())
			{
				++player.getBag()[creature.getBag()[i]];		
			}
		}
		player.setBag(player.getBag());
		player.setGold(player.getGold() + creature.getGold());
		player.setExp(player.getExp() + creature.getExp());
		if (player.getExp() > (50*Math.pow(2, player.getLevel()) - 50))
		{
			LevelUp(player);
		}
		ShowPlayerAttributes(player);
	}
	
	public void Battle(Player player, Creatures creature, Items[] items)
	{
		Battle B = new Battle();
		do
		{
			B.PlayerAtk(player, creature);
			B.CreatureAtk(player, creature);
		} while (player.getLife() > 0 & creature.getLife() > 0);
		if (player.getLife() > 0)
		{
			Win(player, creature, items);
			creature.setLife(creature.getLifeMax());
		}
	}
	
	public void ResetPlayerPosition(Player player)
	{
		player.setContinent(1);
		player.setMap(player.getJob());
		player.setCoords(new int[] {50, 50, 50});
	}
	
	public void Save(Player player, Pet pet)
	{
		String fileName = player.getName() + " save.txt";
		try
		{	
			FileWriter fileWriter = new FileWriter (fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); 
			bufferedWriter.write("Player name = " + player.getName());
			bufferedWriter.write("\nPlayer language = " + player.getLanguage());
			bufferedWriter.write("\nPlayer sex = " + player.getSex());
			bufferedWriter.write("\nPlayer job = " + player.getJob());
			bufferedWriter.write("\nPlayer proJob = " + player.getProJob());
			bufferedWriter.write("\nPlayer continent = " + player.getContinent());
			bufferedWriter.write("\nPlayer map = " + player.getMap());
			bufferedWriter.write("\nPlayer coords = " + player.getCoords());
			bufferedWriter.write("\nPlayer skill = " + player.getSkill());
			bufferedWriter.write("\nPlayer quest = " + player.getQuest());
			bufferedWriter.write("\nPlayer bag = " + player.getBag());
			bufferedWriter.write("\nPlayer skillPoints = " + player.getSkillPoints());
			bufferedWriter.write("\nPlayer life = " + player.getLife());
			bufferedWriter.write("\nPlayer mp = " + player.getMp());
			bufferedWriter.write("\nPlayer lifeMax = " + player.getLifeMax());
			bufferedWriter.write("\nPlayer mpMax = " + player.getMpMax());
			bufferedWriter.write("\nPlayer range = " + player.getRange());
			bufferedWriter.write("\nPlayer phyAtk = " + player.getPhyAtk());
			bufferedWriter.write("\nPlayer magAtk = " + player.getMagAtk());
			bufferedWriter.write("\nPlayer phyDef = " + player.getPhyDef());
			bufferedWriter.write("\nPlayer magDef = " + player.getMagDef());
			bufferedWriter.write("\nPlayer dex = " + player.getDex());
			bufferedWriter.write("\nPlayer agi = " + player.getAgi());
			bufferedWriter.write("\nPlayer collect = " + player.getCollect());
			bufferedWriter.write("\nPlayer level = " + player.getLevel());
			bufferedWriter.write("\nPlayer gold = " + player.getGold());
			bufferedWriter.write("\nPlayer step = " + player.getStep());
			bufferedWriter.write("\nPlayer craft = " + player.getCraft());
			bufferedWriter.write("\nPlayer exp = " + player.getExp());
			bufferedWriter.write("\nPlayer ride = " + player.getRide());
			bufferedWriter.write("\nPlayer status = " + player.getStatus()); 
			bufferedWriter.write("\nPet name = " + pet.getName());
			bufferedWriter.write("\nPet job = " + pet.getJob());
			bufferedWriter.write("\nPet coords = " + pet.getCoords());
			bufferedWriter.write("\nPet skill = " + pet.getSkill());
			bufferedWriter.write("\nPet skillPoints = " + pet.getSkillPoints());
			bufferedWriter.write("\nPet life = " + pet.getLife());
			bufferedWriter.write("\nPet mp = " + pet.getMp());
			bufferedWriter.write("\nPet lifeMax = " + pet.getLifeMax());
			bufferedWriter.write("\nPet mpMax = " + pet.getMpMax());
			bufferedWriter.write("\nPet range = " + pet.getRange());
			bufferedWriter.write("\nPet phyAtk = " + pet.getPhyAtk());
			bufferedWriter.write("\nPet magAtk = " + pet.getMagAtk());
			bufferedWriter.write("\nPet phyDef = " + pet.getPhyDef());
			bufferedWriter.write("\nPet magDef = " + pet.getMagDef());
			bufferedWriter.write("\nPet dex = " + pet.getDex());
			bufferedWriter.write("\nPet agi = " + pet.getAgi());
			bufferedWriter.write("\nPet level = " + pet.getLevel());
			bufferedWriter.write("\nPet exp = " + pet.getExp());
			bufferedWriter.write("\nPet status = " + pet.getStatus()); 
			bufferedWriter.close();
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + fileName + "'");
        }
	}
}
