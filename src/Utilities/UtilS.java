package Utilities ;

import java.awt.Color ;
import java.awt.Font ;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays ;
import java.util.HashMap;
import java.util.Map;

import Actions.Battle ;
import GameComponents.Buildings ;
import GameComponents.Items ;
import GameComponents.NPCs ;
import GameComponents.Quests ;
import Graphics.Animations ;
import Graphics.DrawFunctions ;
import Graphics.DrawPrimitives ;
import Items.Equip;
import LiveBeings.Creature;
import LiveBeings.Pet;
import LiveBeings.Player;
import LiveBeings.Spell;
import Main.Game;
import Maps.FieldMap;
import Maps.Maps;
import Screen.Screen;

public class UtilS 
{
	private static int TypingCounter = 0 ;
	private static String[] TypingText = new String[1] ;
	private static String TypedText = "" ;
	
	public static Color[] ReadColorPalette(Image ColorPaletteImage, String mode)
	{
		Color[] Palette = new Color[28] ;	// 28 color palette

		if (mode.equals("Normal"))
		{
			for (int y = 0 ; y <= 7 - 1 ; y += 1)
			{
				for (int x = 0 ; x <= 4 - 1 ; x += 1)
				{
					Palette[x + 4 * y] = UtilG.GetPixelColor(UtilG.toBufferedImage(ColorPaletteImage), new Point(x, y)) ;
				}
			}
		}
		else if (mode.equals("Konami"))
		{
			for (int x = 0 ; x <= 4 - 1 ; x += 1)
			{
				for (int y = 0 ; y <= 7 - 1 ; y += 1)
				{
					Palette[7 * x + y] = UtilG.GetPixelColor(UtilG.toBufferedImage(ColorPaletteImage), new Point(x, y)) ;
				}
			}
		}
		return Palette ;
	}
	
	/*public static boolean GroundIsWalkable(String GroundType, String SuperElem)
	{
		if (GroundType.equals("free") | GroundType.equals("Lava") | GroundType.contains("Chest") | GroundType.equals("Grass")  | GroundType.equals("Berry") | GroundType.equals("Herb") | GroundType.equals("Wood") | GroundType.equals("Metal") | (GroundType.equals("Water") & SuperElem.equals("w")) | ((GroundType.equals("Tree") | (GroundType.equals("Rock"))) & SuperElem.equals("a")))
		{
			return true ;
		}
		return false ;
	}*/
	
	public static String LiveTyping(Point Pos, double angle, String Input, Font font, Color color, DrawPrimitives DP)
	{
		if (UtilG.IsAlphaNumeric(Input))
		{
			TypingText = UtilG.AddString(TypingText, Input) ;	
			++TypingCounter ;
			if(2 <= TypingCounter)
			{
				TypedText += TypingText[TypingCounter] ;	
			}
		} else if (Input.equals("Backspace") & 0 < TypedText.length())
		{			
			--TypingCounter ;
			TypingText = UtilG.RemoveString(TypingText) ;	
			TypedText = TypedText.substring(0, TypedText.length() - 1) ;
		} else if (Input.equals("Enter"))
		{
			TypedText = "" ;
		}
		if (TypedText != null)
		{
			DP.DrawText(Pos, "BotLeft", angle, TypedText.toString(), font, color) ;									
		}	
		return TypedText ;
	}
	
	public static boolean SetIsFormed(Equip[] EquipID)
	{
		if ((EquipID[0].getId() + 1) == EquipID[1].getId() & (EquipID[1].getId() + 1) == EquipID[2].getId())
		{
			return true ;
		}
		return false ;
	}
	
	public static String RelPos(Point RefPos, Point Pos2)
	{
		if (Pos2.x < RefPos.x)
		{
			return "Right" ;
		}
		else
		{
			return "Left" ;
		}
	}
	
	public static int MirrorFromRelPos(String relPos)
	{
		if (relPos.equals("Left"))
		{
			return -1 ;
		}
		return 1 ;
	}

	public static int MenuSelection(String UpKey, String DownKey, String Choice, int SelectedMenu, int MenuLength)
	{
		if (Choice.equals(DownKey) & SelectedMenu < MenuLength)
		{
			SelectedMenu += 1 ;
		}
		if (Choice.equals(UpKey) & 0 < SelectedMenu)
		{
			SelectedMenu += -1 ;
		}
		return SelectedMenu ;
	}
	
 	public static String CheckAdjacentGround(Point playerPos, Maps map, String GroundType)
	{
 		Point PlayerPos = new Point(playerPos.x, playerPos.y) ;
		if (map.getgroundType() != null)
		{
			for (int i = 0; i <= map.getgroundType().length - 1; i += 1)
			{
				Object[] o = (Object[]) map.getgroundType()[i] ;
				String type = (String) o[0] ;	// ground type at the point
				Point p = (Point) o[1] ;		// point on the map
				if (type.equals(GroundType))
				{
					if (p.x == PlayerPos.x & p.y == PlayerPos.y)
					{
						return "inside" ;	// The player is inside the ground
					}
					else if (p.x == (PlayerPos.x - 1) & p.y == PlayerPos.y)
					{
						return "touching left" ;	// The ground is touching the player from the left
					}
					else if (p.x == (PlayerPos.x + 1) & p.y == PlayerPos.y)
					{
						return "touching right" ;	// The player is touching the ground and the ground is on the left
					}
					else if (p.x == PlayerPos.x & p.y == (PlayerPos.y - 1))
					{
						return "touching up" ;	// The player is touching the ground and the ground is on the left
					}
					else if (p.x == (PlayerPos.x - 1) & p.y == (PlayerPos.y + 1))
					{
						return "touching down" ;	// The player is touching the ground and the ground is on the left
					}
				}			
			}
		}
		/*if (map.getType()[PlayerPos[0]][PlayerPos[1]].equals(GroundType))
		{
			return "Inside" ;	// The player is inside the ground
		}
		if (screen.getBorders()[0] <= PlayerPos[0] - step)
		{
			if (map.getType()[PlayerPos[0] - step][PlayerPos[1]].equals(GroundType))
			{
				return "Touching Left" ;	// The player is touching the ground and the ground is on the left
			}
		}
		if (PlayerPos[0] + step < screen.getBorders()[2] - 1)
		{
			if (map.getType()[PlayerPos[0] + step][PlayerPos[1]].equals(GroundType))
			{
				return "Touching Right" ;	// The player is touching the ground and the ground is on the right
			}
		}
		if (screen.getBorders()[1] <= PlayerPos[1] - step)
		{
			if (map.getType()[PlayerPos[0]][PlayerPos[1] - step].equals(GroundType))
			{
				return "Touching Up" ;	// The player is touching the ground and the ground is above
			}
		}
		if (PlayerPos[1] + step < screen.getBorders()[3] - 1)
		{
			if (map.getType()[PlayerPos[0]][PlayerPos[1] + step].equals(GroundType))
			{
				return "Touching Down" ;	// The player is touching the ground and the ground is below
			}
		}*/
		
		return "";
	}
 	
 	public static boolean isAdjacentTo(Point pos, Maps maps, String GroundType)
 	{
 		String adjGround = CheckAdjacentGround(pos, maps, GroundType) ;
 		if (adjGround.equals("Inside") | adjGround.equals("Touching Left") | adjGround.equals("Touching Right") | adjGround.equals("Touching Up") | adjGround.equals("Touching Down"))
 		{
 			return true ;
 		}
 		return false ;
 	}
		
	public static Creature ClosestCreatureInRange(Player player, Creature[] creatures, Maps[] maps)
	{	
		Size screenSize = Game.getScreen().getSize() ;
		if (player.getMap().isAField())	// map is a field, so it has creatures
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			int NumberOfCreaturesInMap = fm.getCreatures().size() ;
			/*for (int i = 0 ; i <= player.getMap().getCreatureTypes().size() - 1 ; ++i)
			{
				if (-1 < player.getMap().getCreatureTypes().get(i).getID())
				{
					NumberOfCreaturesInMap += 1 ;
				}
			}*/
			double[] dist = new double[NumberOfCreaturesInMap] ;
			double MinDist = screenSize.x + screenSize.y ;
			for (int i = 0 ; i <= NumberOfCreaturesInMap - 1 ; ++i)
			{
				Creature creature = fm.getCreatures().get(i) ;
				if (fm.getCreatures().get(i) != null)
				{
					dist[i] = (double) new Point(player.getPos().x, player.getPos().y).distance(new Point(creature.getPos().x, creature.getPos().y)) ;				
					MinDist = Math.min(MinDist, dist[i]) ;
				}	
			}
			for (int i = 0 ; i <= NumberOfCreaturesInMap - 1 ; ++i)
			{
				Creature creature = fm.getCreatures().get(i) ;
				if (dist[i] == MinDist & fm.getCreatures() != null & dist[i] <= player.getRange())
				{
					return creature ;	// Closest creature ID
				}
			}
		}
		return null ;
	}
	
	public static boolean IsInRange(Point Pos1, Point Pos2, double Range)
	{
		boolean isInRange = false ;
		if (Pos1.distance(Pos2) < Range)
		{
			isInRange = true ;
		}
		return isInRange ;
	}
	
	public static void PrintBattleActions2(String useraction, String receiveraction, String user, String receiver, Object[] AtkResult, String[] elem)
	{
		int damage = (int) AtkResult[0] ;
		String effect = (String) AtkResult[1] ;
		int[] status = (int[]) AtkResult[2] ;

		if (!useraction.equals(""))
		{
			System.out.println() ;
			if (user.equals("player"))
			{
				System.out.println(user + " action2: " + useraction) ;
			}
			if (user.equals("creature"))
			{
				System.out.println(receiver + " action2: " + receiveraction) ;
			}
			if (0 <= damage)
			{
				System.out.println(user + " inflicts status on2 " + receiver + " " + Arrays.toString(status)) ;
				if (effect.equals("Hit"))
				{
					System.out.println(user + " deals damage on2 " + receiver + " ! Damage = " + damage) ;
				}
				else if (effect.equals("Crit"))
				{
					System.out.println(user + " deals critical damage on2 " + receiver + " ! Damage = " + damage) ;
				}
				else if (effect.equals("Miss"))
				{
					System.out.println(user + " misses2 " + receiver + "!") ;
				} 
				else if (effect.equals("Block"))
				{
					System.out.println(receiver + " blocks2 " + user + "!") ;
				}
			}
		}
	}
	
	public static void PrintBattleActions(int message, String user, String receiver, int damage, int effect, int[] status, String[] elem)
	{
		System.out.println() ;
		if (message == 0)
		{
			System.out.println(user + " inflicts status on " + receiver + " " + Arrays.toString(status)) ;
			if (effect == 0)
			{
				System.out.println(user + " deals damage on " + receiver + " ! Damage = " + damage) ;
			} else if (effect == 1)
			{
				System.out.println(user + " deals critical damage on " + receiver + " ! Damage = " + damage) ;
			}
			else if (effect == 2)
			{
				System.out.println(user + " misses " + receiver + "!") ;
			} 
			else if (effect == 3)
			{
				System.out.println(receiver + " blocks " + user + "!") ;
			}
		}
		if (message == 1)
		{
			System.out.println(user + " is stun!") ;
		}
		if (message == 2)
		{
			System.out.println(user + " uses spell on " + receiver + " ! Damage = " + damage) ;
		}
		if (message == 3)
		{
			System.out.println(user + " defends!") ;
		}
		if (message == 4)
		{
			System.out.println("Arrow power added!") ;
		}
		if (message == 5)
		{
			System.out.println(user + " elem: " + Arrays.toString(elem)) ;
		}
		if (message == 6)
		{
			System.out.println(user + " def down!") ;
		}
		if (message == 7)
		{
			System.out.println("Skill buff") ;
		}
		System.out.println() ;
		System.out.println() ;
	}
	
	public static int ElementID(String Elem)
	{
		String[] ElementNames = new String[] {"n", "w", "f", "p", "e", "a", "t", "l", "d", "s"} ;
		return UtilG.IndexOf(ElementNames, Elem) ;
	}
	
	public static String ElementName(int ElemID)
	{
		String[] ElementNames = new String[] {"n", "w", "f", "p", "e", "a", "t", "l", "d", "s"} ;
		return ElementNames[ElemID] ;
	}
	
	

	public static boolean PlayerHasTheIngredients(int[] Bag, int[][] Ingredients, int[][] IngredientAmounts, int FabAmount, int SelectedItem)
	{
		int IngredientItemID = -1 ;
		if (Ingredients != null)
		{
			if (0 < Ingredients.length)
			{
				for (int ing = 0 ; ing <= Ingredients[SelectedItem].length - 1 ; ing += 1)
				{
					IngredientItemID = Ingredients[SelectedItem][ing] ;
					if (Bag[IngredientItemID] < IngredientAmounts[SelectedItem][ing]*FabAmount)
					{
						return false ;
					}
				}
			}
			else
			{
				return false ;
			}
		}
		else
		{
			return false ;
		}
		return true ;
	}
	
	public static int[] Craft(int[] Bag, int[][] Ingredients, int[][] IngredientAmounts, int[][] Products, int[][] ProductAmounts, int FabAmount, int SelectedItem)
	{
		for (int pro = 0 ; pro <= Products[SelectedItem].length - 1 ; pro += 1)
		{
			int ProductItemID = Products[SelectedItem][pro] ;
			Bag[ProductItemID] += ProductAmounts[SelectedItem][pro]*FabAmount ;
		}
		for (int ing = 0 ; ing <= Ingredients[SelectedItem].length - 1 ; ing += 1)
		{
			int IngredientItemID = Ingredients[SelectedItem][ing] ;
			Bag[IngredientItemID] += -IngredientAmounts[SelectedItem][ing]*FabAmount ;
		}
		return Bag ;
	}

	public static int UpAndDownCounter(int counter, int duration)
	{
		if ((counter/duration & 1) == 0)	// counter % looptime is even
		{
			return counter % duration ;
		}
		else
		{
			return duration - (counter % duration) ;
		}
	}
	
	public static int FindTextPos(String[][] AllText, String Category)
	{
		if (AllText != null)
		{
			for (int i = 0 ; i <= AllText.length - 1 ; i += 1)
			{
				if (Category.equals(AllText[i][0]))
				{
					return i ;
				}
			}
		}
		return -1 ;
	}
	
	
	public static int[] FindAllTextCat(String[][] AllText, String Language)
	{
		int[] AllTextCats = new int[AllText.length] ;
		String[] Cats = new String[63] ;
		if (Language.equals("P"))
		{
			Cats[0] = "* *" ;
			Cats[1] = "* Bestiário *" ;
			Cats[2] = "* Novo jogo *" ;
			Cats[3] = "* Tutorial *" ;
			Cats[4] = "* Classes *" ;
			Cats[5] = "* ProClasses *" ;
			Cats[6] = "* Atributos *" ;
			Cats[7] = "* EstatÃ­sticas do jogador *" ;
			Cats[8] = "* Propriedades dos atributos especiais *" ;
			Cats[9] = "* Coleta *" ;
			Cats[10] = "* Vitï¿½ria *" ;
			Cats[11] = "* Equipamentos *" ;
			Cats[12] = "* Doutor *" ;
			Cats[13] = "* Vendedor de equipamentos *" ;
			Cats[14] = "* Vendedor de itens *" ;
			Cats[15] = "* Contrabandista *" ;
			Cats[16] = "* Banqueiro *" ;
			Cats[17] = "* Alquimista *" ;
			Cats[18] = "* Madeireiro *" ;
			Cats[19] = "* Mestre *" ;
			Cats[20] = "* Quest *" ;
			Cats[21] = "* Forjador *" ;
			Cats[22] = "* Fabricante *" ;
			Cats[23] = "* Salvador *" ;
			Cats[24] = "* Elemental *" ;
			Cats[25] = "* Navegador 1*" ;
			Cats[26] = "* Quest exp *" ;
			Cats[27] = "* Quest items *" ;
			Cats[28] = "* Partes do corpo *" ;
			Cats[29] = "* Tesouros *" ;
			Cats[30] = "* Menus da mochila *" ;
			Cats[31] = "* Pterodactile *" ;
			Cats[32] = "* Nomes dos continentes *" ;
			Cats[33] = "* Mensagem das placas *" ;
			Cats[34] = "* Menu de personalizaï¿½ï¿½o *" ;
			Cats[35] = "* Menu de quest *" ;
			Cats[36] = "* Menu de opï¿½ï¿½es *" ;
			Cats[37] = "* Menu de dicas *" ;
			Cats[38] = "* Dimensï¿½es *" ;
			Cats[39] = "* Cores *" ;
			Cats[40] = "* Janela do jogador *" ;
			Cats[41] = "* Cidadï¿½o 0 *" ;
			Cats[42] = "* Cidadï¿½o 1 *" ;
			Cats[43] = "* Cidadï¿½o 2 *" ;
			Cats[44] = "* Cidadï¿½o 3 *" ;
			Cats[45] = "* Cidadï¿½o 4 *" ;
			Cats[46] = "* Cidadï¿½o 5 *" ;
			Cats[47] = "* Cidadï¿½o 6 *" ;
			Cats[48] = "* Cidadï¿½o 7 *" ;
			Cats[49] = "* Cidadï¿½o 8 *" ;
			Cats[50] = "* Cidadï¿½o 9 *" ;
			Cats[51] = "* Cidadï¿½o 10 *" ;
			Cats[52] = "* Cidadï¿½o 11 *" ;
			Cats[53] = "* Cidadï¿½o 12 *" ;
			Cats[54] = "* Cidadï¿½o 13 *" ;
			Cats[55] = "* Cidadï¿½o 14 *" ;
			Cats[56] = "* Cidadï¿½o 15 *" ;
			Cats[57] = "* Cidadï¿½o 16 *" ;
			Cats[58] = "* Cidadï¿½o 17 *" ;
			Cats[59] = "* Cidadï¿½o 18 *" ;
			Cats[60] = "* Cidadï¿½o 19 *" ;
			Cats[61] = "* Barra de habilidades *" ;
			Cats[62] = "* Navegador 2*" ;
			for (int i = 0 ; i <= AllText.length - 1 ; i += 1)
			{
				AllTextCats[i] = FindTextPos(AllText, Cats[i]) ;
			}
			return AllTextCats ;
		}
		else if (Language.equals("E"))
		{
			Cats[0] = "* *" ;
			Cats[1] = "* Bestiary *" ;
			Cats[2] = "* New game *" ;
			Cats[3] = "* Tutorial *" ;
			Cats[4] = "* Classes *" ;
			Cats[5] = "* ProClasses *" ;
			Cats[6] = "* Attributes *" ;
			Cats[7] = "* Player stats *" ;
			Cats[8] = "* Properties of special attributes *" ;
			Cats[9] = "* Collect *" ;
			Cats[10] = "* Victory *" ;
			Cats[11] = "* Equipment *" ;
			Cats[12] = "* Doctor *" ;
			Cats[13] = "* Equipment seller *" ;
			Cats[14] = "* Item seller *" ;
			Cats[15] = "* Smuggler *" ;
			Cats[16] = "* Banker *" ;
			Cats[17] = "* Alchemist *" ;
			Cats[18] = "* Lumberjack *" ;
			Cats[19] = "* Master *" ;
			Cats[20] = "* Quest *" ;
			Cats[21] = "* Forger *" ;
			Cats[22] = "* Manufacturer *" ;
			Cats[23] = "* Savior *" ;
			Cats[24] = "* Elemental *" ;
			Cats[25] = "* Sailor 1*" ;
			Cats[26] = "* Quest exp *" ;
			Cats[27] = "* Quest items *" ;
			Cats[28] = "* Body parts *" ;
			Cats[29] = "* Treasures *" ;
			Cats[30] = "* Backpack menus *" ;
			Cats[31] = "* Pterodactile *" ;
			Cats[32] = "* Names of continents *" ;
			Cats[33] = "* Message from the plates *" ;
			Cats[34] = "* Customization menu *" ;
			Cats[35] = "* Quest menu *" ;
			Cats[36] = "* Options menu *" ;
			Cats[37] = "* Tip menu *" ;
			Cats[38] = "* Dimensions *" ;
			Cats[39] = "* Colors *" ;
			Cats[40] = "* player window *" ;
			Cats[41] = "* Citizen 0 *" ;
			Cats[42] = "* Citizen 1 *" ;
			Cats[43] = "* Citizen 2 *" ;
			Cats[44] = "* Citizen 3 *" ;
			Cats[45] = "* Citizen 4 *" ;
			Cats[46] = "* Citizen 5 *" ;
			Cats[47] = "* Citizen 6 *" ;
			Cats[48] = "* Citizen 7 *" ;
			Cats[49] = "* Citizen 8 *" ;
			Cats[50] = "* Citizen 9 *" ;
			Cats[51] = "* Citizen 10 *" ;
			Cats[52] = "* Citizen 11 *" ;
			Cats[53] = "* Citizen 12 *" ;
			Cats[54] = "* Citizen 13 *" ;
			Cats[55] = "* Citizen 14 *" ;
			Cats[56] = "* Citizen 15 *" ;
			Cats[57] = "* Citizen 16 *" ;
			Cats[58] = "* Citizen 17 *" ;
			Cats[59] = "* Citizen 18 *" ;
			Cats[60] = "* Citizen 19 *" ;
			Cats[61] = "* Skills bar *" ;
			Cats[62] = "* Sailor 2*" ;
			for (int i = 0 ; i <= AllText.length - 1 ; i += 1)
			{
				AllTextCats[i] = FindTextPos(AllText, Cats[i]) ;
			}
			return AllTextCats ;
		}
		return null ;	
	}
	
	/*public static int[][] NPCsInBuildings(NPCs[] npc, Buildings[] buildings, int map, int[] BuildingsInCity)
	{
		int[][] IDs = null ;
		for (int b = 0 ; b <= BuildingsInCity.length - 1 ; b += 1)
		{
			IDs = UtilG.IncreaseArraySize(IDs, 1) ;
			for (int n = 0 ; n <= npc.length - 1 ; n += 1)
			{
				if (npc[n].getMap() == map & npc[n].getPosRelToBuilding().equals(buildings[BuildingsInCity[b]].getName()))
				{
					IDs[b] = UtilG.AddElem(IDs[b], npc[n].getID()) ;
				}
			}
		}
		return IDs ;
	}*/
	
	/*public static ArrayList<NPCs> NPCsInBuilding(NPCs[] npc, String buildingName)
	{
		ArrayList<NPCs> npcs = new ArrayList<NPCs>() ;
		for (int n = 0 ; n <= npc.length - 1 ; n += 1)
		{
			if (npc[n].getMap() == map & npc[n].getPosRelToBuilding().equals(buildingName))
			{
				npcs.add(npc[n]) ;
			}
		}
		return npcs ;
	}*/
	
	/*public static Point BuildingPos(Buildings[] buildings, int map, String Name)
	{
		for (int b = 0 ; b <= buildings.length - 1 ; b += 1)
		{
			if (buildings[b].getName().equals(Name) & buildings[b].getMap() == map)
			{
				return buildings[b].getPos() ;
			}
		}
		return null ;
	}*/

	public static double[] LevelUpIncAtt(double[] AttributeIncrease, double[] ChanceIncrease, int Level)
	{
		// Life, Mp, Phyatk, Magatk, Phydef, Magdef, Dex, Agi, Exp
		double[] Increase = new double[AttributeIncrease.length + 1] ;
		for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= ChanceIncrease[i])
			{
				Increase[i] = AttributeIncrease[i] ;
			}
		}
		Increase[AttributeIncrease.length] = (double) (10*(3*Math.pow(Level - 1, 2) + 3*(Level - 1) + 1) - 5) ;
		return Increase ;
	}		
		
	/*public static String IdentifySave(Player player, Pet pet, double[][] EquipsBonus, int NumberOfSlots, Maps[] maps)
	{
		for (int i = 0 ; i <= NumberOfSlots - 1 ; i += 1)
		{
			//ReadFile = ReadTextFile("save" + (i + 1) + ".txt", 156, 150) ;	// Number of rows and columns
			Player p = null ;
			p.Load("save" + (i + 1) + ".txt", pet, maps) ;
			System.out.println(player.getName() + " " + p.getName()) ;
			if (player.getName().equals(p.getName()))
			{
				return "save" + (i + 1) + ".txt" ;
			}
		}
		return "save not found" ;
	}
	
	public static String TranslatePlayerAction(String action)
	{
		if (action.equals("W"))
		{
			return "SelectionUp" ;
		}
		else if (action.equals("A"))
		{
			return "SelectionLeft" ;
		}
		else if (action.equals("S"))
		{
			return "SelectionDown" ;
		}
		else if (action.equals("D"))
		{
			return "SelectionRight" ;
		}
		else if (action.equals("B"))
		{
			return "Bag" ;
		}
		else if (action.equals("C"))
		{
			return "CharacterWindow" ;
		}
		else if (action.equals("F"))
		{
			return "Fabrication" ;
		}
		else if (action.equals("M"))
		{
			return "Map" ;
		}
		else if (action.equals("P"))
		{
			return "Petwindow" ;
		}
		else if (action.equals("O"))
		{
			return "Options" ;
		}
		else if (action.equals("Q"))
		{
			return "Quest" ;
		}
		else if (action.equals("H"))
		{
			return "Hint" ;
		}
		else if (action.equals("R"))
		{
			return "Ride" ;
		}
		else if (action.equals("T"))
		{
			return "Tent" ;
		}
		else if (action.equals("Z"))
		{
			return "Bestiary" ;
		}
		
		return null ;
	}*/
	
	public static boolean KonamiCodeActivated(ArrayList<String> Combo)
	{
		String[] KonamiCode = new String[] {"A", "B", "Direita", "Esquerda", "Direita", "Esquerda", "Abaixo", "Abaixo", "Acima", "Acima"} ;
		String[] combo = Combo.toArray(new String[Combo.size()]) ;
		
		if (Combo != null)
		{
			if (Arrays.equals(combo, KonamiCode))
			{
				return true ;
			}
		}
		return false ;
	}

	/*public void PrintBook(Items[] items, int[][] CraftingIngredients, int[][] CraftingProducts)
	{
		for (int i = 0 ; i <= CraftingIngredients.length - 1 ; ++i)
		{
			for (int j = 0 ; j <= CraftingIngredients[i].length - 1 ; ++j)
			{
				if (-1 < CraftingIngredients[i][j])
				{
					System.out.println(items[CraftingIngredients[i][j]].getID() + " " + items[CraftingIngredients[i][j]].getName()) ;
				}
			}
			for (int j = 0 ; j <= CraftingProducts[i].length - 1 ; ++j)
			{
				if (-1 < CraftingProducts[i][j])
				{
					System.out.println(items[CraftingProducts[i][j]].getID() + " " + items[CraftingProducts[i][j]].getName()) ;
				}
			}
			System.out.println() ;
			System.out.println() ;
		}
	}	*/
		
	public void BattleSimulation(Player player, Pet pet, Creature creature, Spell[] skills, Spell[] petskills, int[] ActivePlayerSkills, String[] MoveKeys, Items[] items, Quests[] quest, int NumberOfSimulations, String[] ActionKeys, String[] SkillKeys, Battle B, DrawFunctions DF)
	{
		int move = 0 ;
		//String[] ItemsObtained = new String[10] ;
		int NumberOfPlayerWins = 0, NumberOfPlayerPhyAtks = 0, NumberOfPetPhyAtks = 0, NumberOfPlayerMagAtks = 0, NumberOfPetMagAtks = 0, NumberOfPlayerDefs = 0, NumberOfPetDefs = 0 ;
		double[] BattleResults = new double[5] ;
		System.out.println("Creature life: " + creature.getLife()[0] + " Creature phy atk: " + creature.getPhyAtk().getBaseValue()) ;
		
		for (int i = 0 ; i <= NumberOfSimulations - 1 ; ++i)
		{
			System.out.println("") ;
			System.out.println("Simulation " + i + ": player vs creature level " + creature.getLevel()) ;
			BattleResults[0] = 1 ;
			do
			{
				/*for (int j = 0 ; j <= player.getActions().length - 1 ; ++j)
				{
					if(player.getActions()[j][0] < player.getActions()[j][1])
					{
						player.getActions()[j][0] += 1 ;
					}	
				}
				for (int j = 0 ; j <= pet.getActions().length - 1 ; ++j)
				{
					if(pet.getActions()[j][0] < pet.getActions()[j][1])
					{
						pet.getActions()[j][0] += 1 ;
					}	
				}
				for (int j = 0 ; j <= creature.getActions().length - 1 ; ++j)
				{
					if(creature.getActions()[j][0] < creature.getActions()[j][1])
					{
						creature.getActions()[j][0] += 1 ;
					}
				}*/
				for (int j = 0 ; j <= player.getBA().getBattleActions().length - 1 ; ++j)
				{
					if(player.getBA().getBattleActions()[j][0] < player.getBA().getBattleActions()[j][1])
					{
						player.getBA().getBattleActions()[j][0] += 1 ;
					}	
				}
				for (int j = 0 ; j <= pet.getBA().getBattleActions().length - 1 ; ++j)
				{
					if(pet.getBA().getBattleActions()[j][0] < pet.getBA().getBattleActions()[j][1])
					{
						pet.getBA().getBattleActions()[j][0] += 1 ;
					}	
				}
				for (int j = 0 ; j <= creature.getBA().getBattleActions().length - 1 ; ++j)
				{
					if(creature.getBA().getBattleActions()[j][0] < creature.getBA().getBattleActions()[j][1])
					{
						creature.getBA().getBattleActions()[j][0] += 1 ;
					}	
				}
				if (player.getBA().getBattleActions()[0][0] % player.getBA().getBattleActions()[0][1] == 0)
				{
					player.getBA().getBattleActions()[0][2] = 1 ;	// Player can atk
				}
				if (0 < pet.getLife()[0] & pet.getBA().getBattleActions()[0][0] % pet.getBA().getBattleActions()[0][1] == 0)
				{
					pet.getBA().getBattleActions()[0][2] = 1 ;		// pet can atk
				}
				/*if (player.getActions()[2][0] % player.getActions()[2][1] == 0)	// Player heals mp
				{
					player.getMp()[0] = (double)(Math.min(player.getMp()[0] + 0.02*player.getMp()[1], player.getMp()[1])) ;	
					player.getActions()[2][0] = 0 ;
				}
				if (pet.getActions()[2][0] % pet.getActions()[2][1] == 0)	// Pet heals mp
				{
					pet.getMp()[0] = (double)(Math.min(pet.getMp()[0] + 0.02*pet.getMp()[1], pet.getMp()[1])) ;	
					pet.getActions()[2][0] = 0 ;
				}
				for (int j = 0 ; j <= creature.getBA().getBattleActions().length - 1 ; ++j)
				{
					if (creature.getBA().getBattleActions()[j][0] % creature.getBA().getBattleActions()[j][1] == 0)
					{
						creature.getBA().getBattleActions()[j][2] = 1 ;	// Creature can atk
					}	
					if (creature.getActions()[1][0] % creature.getActions()[1][1] == 0)
					{
						creature.getMp()[0] = (double)(Math.min(creature.getMp()[0] + 0.02*creature.getMp()[1], creature.getMp()[1])) ;	// Creature heals mp
						creature.getActions()[1][0] = 0 ;
					}	
				}	*/
				if (0 < player.getLife()[0] & 0 < player.getBA().getBattleActions()[0][2])
				{				
					move = (int)(3*Math.random() - 0.01) ;
					if (move == 0)
					{
		        		player.setCurrentAction(ActionKeys[1]) ;
						NumberOfPlayerPhyAtks += 1 ;
					}
					if (move == 1)
					{
		        		player.setCurrentAction(ActionKeys[3]) ;
						NumberOfPlayerDefs += 1 ;
					}
					if (move == 2)
					{
		        		player.setCurrentAction(String.valueOf((int)(1*Math.random() - 0.01))) ;
						NumberOfPlayerMagAtks += 1 ;
					}
				}
				if (0 < pet.getLife()[0] & 0 < pet.getBA().getBattleActions()[0][2])
				{
					move = (int)(3*Math.random() - 0.01) ;
					if (move == 0)
					{
						pet.setCurrentAction(ActionKeys[1]) ;
						NumberOfPetPhyAtks += 1 ;
					}
					if (move == 1)
					{
						pet.setCurrentAction(ActionKeys[3]) ;
						NumberOfPetDefs += 1 ;
					}
					if (move == 2)
					{
						pet.setCurrentAction(String.valueOf((int)(10*Math.random() - 0.01))) ;
						NumberOfPetMagAtks += 1 ;
					}
				}
				B.RunBattle(player, pet, creature, quest, new Point(0, 0), DF) ;
				if (0 == BattleResults[0])
				{
					if (0 < player.getLife()[0])
					{
						//player.Win(creature, items, quest, null) ;
						NumberOfPlayerWins += 1 ;
						System.out.println("Result = Player wins") ;
						//System.out.println("Items obtained: " + Arrays.toString(ItemsObtained)) ;
					}
					else
					{
						System.out.println("Result = Creature wins") ;
					}
					player.getLife()[0] = player.getLife()[1] ;
				}
			} while (1 == BattleResults[0]) ;
			System.out.println("Highest player inflicted damage: " + BattleResults[1]) ;
		}
		System.out.println("") ;
		System.out.println("Player win rate: " + 100*NumberOfPlayerWins/NumberOfSimulations + "%") ;
		System.out.println("Number of player phy atks: " + NumberOfPlayerPhyAtks) ;
		System.out.println("Number of player mag atks: " + NumberOfPlayerMagAtks) ;
		System.out.println("Number of player defs: " + NumberOfPlayerDefs) ;
		System.out.println("Number of pet phy atks: " + NumberOfPetPhyAtks) ;
		System.out.println("Number of pet mag atks: " + NumberOfPetMagAtks) ;
		System.out.println("Number of pet defs: " + NumberOfPetDefs) ;
		System.out.println("Highest player inflicted damage: " + BattleResults[1]) ;
		System.out.println("Highest pet inflicted damage: " + BattleResults[2]) ;
		System.out.println("Highest creature inflicted damage on player: " + BattleResults[3]) ;
		System.out.println("Highest creature inflicted damage on pet :" + BattleResults[4]) ;
	}
}
