package Main ;

import java.awt.Color ;
import java.awt.Font ;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays ;

import Actions.Battle ;
import GameComponents.Buildings ;
import GameComponents.Creatures ;
import GameComponents.Items ;
import GameComponents.Maps ;
import GameComponents.NPCs ;
import GameComponents.Pet ;
import GameComponents.PetSkills ;
import GameComponents.Player ;
import GameComponents.Quests ;
import GameComponents.Screen ;
import GameComponents.Skills ;
import Graphics.Animations ;
import Graphics.DrawFunctions ;
import Graphics.DrawPrimitives ;

public class Uts 
{
	private static int TypingCounter = 0 ;
	private static String[] TypingText = new String[1] ;
	private static String TypedText = "" ;
	
	public static Color[] ReadColorPalette(Image ColorPaletteImage, String mode)
	{
		Color[] Palette = null ;	// 28 color palette

		if (mode.equals("Normal"))
		{
			for (int y = 0 ; y <= 7 - 1 ; y += 1)
			{
				for (int x = 0 ; x <= 4 - 1 ; x += 1)
				{
					Palette = Utg.AddElem(Palette, Utg.GetPixelColor(Utg.toBufferedImage(ColorPaletteImage), new int[] {x, y})) ;
				}
			}
		}
		else if (mode.equals("Konami"))
		{
			for (int x = 0 ; x <= 4 - 1 ; x += 1)
			{
				for (int y = 0 ; y <= 7 - 1 ; y += 1)
				{
					Palette = Utg.AddElem(Palette, Utg.GetPixelColor(Utg.toBufferedImage(ColorPaletteImage), new int[] {x, y})) ;
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
	
	public static String LiveTyping(int[] Pos, float angle, String Input, Font font, Color color, DrawPrimitives DP)
	{
		if (Utg.IsAlphaNumeric(Input))
		{
			TypingText = Utg.AddString(TypingText, Input) ;	
			++TypingCounter ;
			if(2 <= TypingCounter)
			{
				TypedText += TypingText[TypingCounter] ;	
			}
		} else if (Input.equals("Backspace") & 0 < TypedText.length())
		{			
			--TypingCounter ;
			TypingText = Utg.RemoveString(TypingText) ;	
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
	
	public static boolean SetIsFormed(int[] EquipID)
	{
		if ((EquipID[0] + 1) == EquipID[1] & (EquipID[1] + 1) == EquipID[2])
		{
			return true ;
		}
		return false ;
	}
	
	public static String RelPos(int[] RefPos, int[] Pos2)
	{
		if (Pos2[0] < RefPos[0])
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
	
	public static boolean CheckLevelUP(Player player, Animations Ani)
	{
		if (player.getExp()[1] <= player.getExp()[0] & !Ani.isActive(12))
		{
			return true ;
		}
		return false ;
	}
	
	public static boolean CheckLevelUP(Pet pet, Animations Ani)
	{
		if (pet.getExp()[1] <= pet.getExp()[0] & !Ani.isActive(13))
		{
			return true ;
		}
		return false ;
	}
	
 	/*public static String[][] GroundTypes(int map, String name, int Continent, int step, int SkyHeight, Buildings[] building, int[] screenDim)
 	{
 		String[][] GroundType = new String[screenDim[0] + 1][screenDim[1] + 1] ;
 		
		for (int j = 0 ; j <= GroundType.length - 1 ; ++j)
		{
			Arrays.fill(GroundType[j], "free") ;
		}
		float Skyratio = SkyHeight / (float) screenDim[1] ;
		float MinX = (float)(0.1), MinY = (float)(Skyratio + 0.1) ; 
		float RangeX = (float)(0.8), RangeY = (float)(1 - MinY) ;
		//int[] BuildingsInCity = Uts.BuildingsInCity(building, map) ;
 		if (name.contains("City"))
		{
			float[] Scale = new float[] {1, 1} ;
			int[][] FurniturePos = new int[][] {{2*step, 2*step}, {0, 2*step}, {0, 2*step}, {0, 1*step}, {0, 0}, {0, 0}} ;
			for (int b = 0 ; b <= 4 - 1 ; b += 1)	// Number of buildings
			{
				int BuildingID = 5*b + map ;
				int[] BuildingPos = new int[] {300, 300} ;
				float[] BuildingSize = new float[] {building[BuildingsInCity.length + b].Images[0].getWidth(null), building[BuildingsInCity.length + b].Images[0].getHeight(null)} ;
				int[] PosInSteps = new int[] {(BuildingPos[0]/step + 1)*step, BuildingPos[1]/step*step} ;
				int[] SizeInSteps = new int[] {(int) ((Scale[0]*BuildingSize[0])/step - 1)*step, (int)((Scale[1]*BuildingSize[1])/step - 1)*step} ;
				int DoorPos = (int)(0.8*SizeInSteps[0])/step*step ;
				int DoorL = step/2 ;
				if (b == 0)	// Hospital
				{
					DoorPos += -step ;
					DoorL = 2*step ;
				}
				if (b == 3)	// Bank
				{
					DoorPos += -2*step ;
				}
				for (int k = 0 ; k <= SizeInSteps[0] ; k += step)
				{
					MapsType[PosInSteps[0] + k][PosInSteps[1] - SizeInSteps[1]] = "Invisible wall" ;	// Top wall
					if (k < DoorPos | DoorPos + DoorL < k)	// Door
					{
						MapsType[PosInSteps[0] + k][PosInSteps[1]] = "Invisible wall" ;	// Bottom wall					
					}
				}
				for (int k = 0 ; k <= SizeInSteps[1] ; k += step)
				{
					MapsType[PosInSteps[0]][PosInSteps[1] - k] = "Invisible wall" ;	// Left wall
					MapsType[PosInSteps[0] + SizeInSteps[0]][PosInSteps[1] - k] = "Invisible wall" ;	// Right wall
				}
				for (int k = 0 ; k <= SizeInSteps[0] - FurniturePos[b][0] ; k += step)
				{
					MapsType[PosInSteps[0] + k + FurniturePos[b][0]][PosInSteps[1] - FurniturePos[b][1]] = "Invisible wall" ;	// Furniture wall
				}
			}
		}
		if (Continent == 0)
		{
			if (map == 2)
			{
				for (int j = SkyHeight ; j <= screenDim[1] ; j += 1)
				{
					for (int k = (int) (0.8 * screenDim[0]) ; k <= screenDim[0] ; k += 1)
					{
						GroundType[k][j] = "Water" ;
					}
				}
			}
			if (map == 4)
			{
				for (int k = 10 ; k <= 20 ; k += 1)
				{
					GroundType[3*step][k*step] = "Wall" ;	// Outside wall
					GroundType[21*step][k*step] = "Wall" ;	// Outside wall
				}
				for (int k = 3 ; k <= 21 ; k += 1)
				{
					GroundType[k*step][10*step] = "Wall" ;	// Outside wall
				}
				for (int k = 16 ; k <= 23 ; k += 1)
				{
					GroundType[8*step][k*step] = "Wall" ;	// Inside wall
					GroundType[26*step][k*step] = "Wall" ;	// Inside wall
				}
				for (int k = 8 ; k <= 26 ; k += 1)
				{
					GroundType[k*step][23*step] = "Wall" ;	// Inside wall
				}
			}
			if (map == 13 | map == 17)
			{ 
				RangeX = (float)(0.6) ;
				for (double j = Skyratio ; j <= 1.0 ; j += 1.0 / screenDim[1])
				{
					for (double k = 0.8 ; k <= 1.0 ; k += 1.0 / screenDim[0])
					{
						if (!Utg.isInside(new double[] {k, j}, new double[] {0.8, 0.8}, 0.1, 0.08) & !Utg.isInside(new double[] {k, j}, new double[] {0.9, 0.808}, 0.05, 0.16))
						{
							GroundType[(int) (k * screenDim[0])][(int) (j * screenDim[1])] = "Water" ;
						}
					}
				}
			}
			if (map == 25)
			{
				for (int j = 0 ; j <= screenDim[1] - 1 ; j += step)
				{
					for (int k = 0 ; k <= 5 ; ++k)
					{
						GroundType[k*step][j] = "Tree" ;
					}
				}
				for (int k = 0 ; k <= 5 ; ++k)
				{
					GroundType[k*step][screenDim[1] - 1] = "Tree" ;
				}
			}
			else
			{
				if (!name.contains("City"))
				{
					MapElements[] MapElem = new MapElements[5] ;
					for (int j = 0 ; j <= 4 ; ++j)
					{
						int[] Pos = new int[] {Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step), Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)} ;
						//MapElem[j] = new MapElements(j, "ForestTree", Pos, new ImageIcon(ImagesPath + "MapElem6_TreeForest.png").getImage()) ;
						//MapsType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Tree" ;					
					}	
				}
			}
			if (!name.contains("City"))
			{
				for (int j = 0 ; j <= 4 ; ++j)
				{
					GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Grass" ;
					GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Rock" ;	
				}
			}
		}
		if (Continent == 1)
		{
			if (map == 36)
			{
				 Positions of the maze walls are in % of the screen size
				int[] MazeStartPos = new int[] {10, 15, 51, 15, 39, 2, 35, 17, 77, 92, 67, 17, 57, 88, 49, 4, 81, 2, 5, 54, 71, 15, 58, 11, 39, 12, 80, 59, 5, 3, 0, 54} ;
				int[] MazeEndPos = new int[] {93, 88, 77, 39, 51, 27, 77, 67, 81, 100, 77, 46, 71, 100, 70, 25, 96, 70, 25, 77, 100, 25, 71, 20, 45, 61, 94, 80, 20, 20, 42, 100} ;
				int[] MazeHorizontalWallsPos = new int[] {2, 5, 12, 15, 16, 32, 32, 39, 42, 42, 45, 54, 54, 54, 74, 77, 80} ;
				int[] MazeVerticalWallsPos = new int[] {10, 15, 17, 38, 39, 45, 51, 67, 77, 81, 85, 88, 93, 100, 100} ;
				for (int j = 0 ; j <= MazeHorizontalWallsPos.length - 1 ; j += 1)
				{
					for (int k = MazeStartPos[j] ; k <= MazeEndPos[j] ; k += 1)
					{
						GroundType[k*35/100*step][MazeHorizontalWallsPos[j]*35/100*step] = "Invisible wall" ;	
					}
				}
				for (int j = 0 ; j <= MazeVerticalWallsPos.length - 1 ; j += 1)
				{
					for (int k = MazeStartPos[j + MazeHorizontalWallsPos.length] ; k <= MazeEndPos[j + MazeHorizontalWallsPos.length] ; k += 1)
					{
						GroundType[MazeVerticalWallsPos[j]*35/100*step][k*35/100*step] = "Invisible wall" ;
					}
				}
			}
			if (map == 39)
			{
				GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Chest " + String.valueOf(0) ;										
			}
			for (int j = 0 ; j <= 4 ; ++j)
			{
				if (map != 36)
				{
					GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Crystal" ;					
					GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Stalactite" ;										
				}
			}
		}
		if (Continent == 2)
		{
			//GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Tree" ;
			//GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Rock" ;						
		}
		if (Continent == 3)
		{
			for (int j = 0 ; j <= 4 ; ++j)
			{
				//GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Volcano" ;					
			}
			for (int j = 0 ; j <= 40 ; ++j)
			{
				GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Lava" ;					
			}
		}
		if (Continent == 4)
		{
			for (int j = 0 ; j <= 4 ; ++j)
			{
				GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Ice" ;					
			}
		}
		if (map == 60)
		{
			for (int j = 0 ; j <= 4 ; ++j)
			{
				//GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Tree" ;					
			}
			for (int j = 0 ; j <= 5 ; ++j)
			{
				//GroundType[Utg.RandomCoord1D(screenDim[0], MinX, RangeX, step)][Utg.RandomCoord1D(screenDim[1], MinY, RangeY, step)] = "Chest " + String.valueOf(j + 1) ;					
			}
		}
		if (60 < map & map <= 66)
		{
			for (int j = 0 ; j <= GroundType.length - 1 ; ++j)
			{
				Arrays.fill(GroundType[j], "Water") ;
			}
		}
		return GroundType ;
 	}*/
	
 	public static String CheckAdjacentGround(int[] playerPos, Screen screen, Maps map, String GroundType)
	{
		int[] PlayerPos = Arrays.copyOf(playerPos, playerPos.length) ;
		if (map.getgroundType() != null)
		{
			for (int i = 0; i <= map.getgroundType().length - 1; i += 1)
			{
				Object[] o = (Object[]) map.getgroundType()[i] ;
				String type = (String) o[0] ;	// ground type at the point
				Point p = (Point) o[1] ;		// point on the map
				if (type.equals(GroundType))
				{
					if (p.x == PlayerPos[0] & p.y == PlayerPos[1])
					{
						return "inside" ;	// The player is inside the ground
					}
					else if (p.x == (PlayerPos[0] - 1) & p.y == PlayerPos[1])
					{
						return "touching left" ;	// The ground is touching the player from the left
					}
					else if (p.x == (PlayerPos[0] + 1) & p.y == PlayerPos[1])
					{
						return "touching right" ;	// The player is touching the ground and the ground is on the left
					}
					else if (p.x == PlayerPos[0] & p.y == (PlayerPos[1] - 1))
					{
						return "touching up" ;	// The player is touching the ground and the ground is on the left
					}
					else if (p.x == (PlayerPos[0] - 1) & p.y == (PlayerPos[1] + 1))
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
 	
 	public static boolean isAdjacentTo(int[] pos, Screen screen, Maps maps, String GroundType)
 	{
 		if (CheckAdjacentGround(pos, screen, maps, GroundType).equals("Inside") | CheckAdjacentGround(pos, screen, maps, GroundType).equals("Touching Left") | CheckAdjacentGround(pos, screen, maps, GroundType).equals("Touching Right") | CheckAdjacentGround(pos, screen, maps, GroundType).equals("Touching Up") | CheckAdjacentGround(pos, screen, maps, GroundType).equals("Touching Down"))
 		{
 			return true ;
 		}
 		return false ;
 	}
		
	public static int ClosestCreatureInRange(Player player, Creatures[] creature, Maps[] maps, Screen screen)
	{	
		if (maps[player.getMap()].getCreatureIDs() != null)	// Map has creatures
		{
			int NumberOfCreaturesInMap = 0 ;
			for (int i = 0 ; i <= maps[player.getMap()].getCreatureTypes().length - 1 ; ++i)
			{
				if (-1 < maps[player.getMap()].getCreatureTypes()[i])
				{
					NumberOfCreaturesInMap += 1 ;
				}
			}
			float[] dist = new float[NumberOfCreaturesInMap] ;
			float MinDist = screen.getDimensions()[0] + screen.getDimensions()[1] ;
			for (int i = 0 ; i <= NumberOfCreaturesInMap - 1 ; ++i)
			{
				int ID = maps[player.getMap()].getCreatureIDs()[i] ;
				if (-1 < ID)
				{
					dist[i] = Utg.dist2D(new int[] {player.getPos()[0], player.getPos()[1]}, new int[] {creature[ID].getPos()[0], creature[ID].getPos()[1]}) ;				
					MinDist = Math.min(MinDist, dist[i]) ;
				}	
			}
			for (int i = 0 ; i <= NumberOfCreaturesInMap - 1 ; ++i)
			{
				if (dist[i] == MinDist & -1 < maps[player.getMap()].getCreatureIDs()[i] & dist[i] <= player.getRange())
				{
					return maps[player.getMap()].getCreatureIDs()[i] ;	// Closest creature ID
				}
			}
		}
		return -1 ;
	}
	
	public static boolean ActionIsSkill(String[] SkillKeys, String playerMove)
	{
		if(-1 < Utg.IndexOf(SkillKeys, playerMove))
		{
			return true ;
		}
		return false ;
	}
	
	public static boolean IsInRange(int[] Pos1, int[] Pos2, float Range)
	{
		boolean isInRange = false ;
		if (Utg.dist2D(Pos1, Pos2) < Range)
		{
			isInRange = true ;
		}
		return isInRange ;
	}
	
	public static void PrintBattleActions2(String useraction, String receiveraction, String user, String receiver, Object[] AtkResult, String[] elem)
	{
		int damage = (int) AtkResult[0] ;
		int effect = (int) AtkResult[1] ;
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
				if (effect == 0)
				{
					System.out.println(user + " deals damage on2 " + receiver + " ! Damage = " + damage) ;
				}
				else if (effect == 1)
				{
					System.out.println(user + " deals critical damage on2 " + receiver + " ! Damage = " + damage) ;
				}
				else if (effect == 2)
				{
					System.out.println(user + " misses2 " + receiver + "!") ;
				} 
				else if (effect == 3)
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
		return Utg.IndexOf(ElementNames, Elem) ;
	}
	
	public static String ElementName(int ElemID)
	{
		String[] ElementNames = new String[] {"n", "w", "f", "p", "e", "a", "t", "l", "d", "s"} ;
		return ElementNames[ElemID] ;
	}
	
	public static boolean SpellIsAvailable(Player player, Skills[] spells, int SelectedSkill)
	{
		boolean SkillAvailability = false ;
		int cont = 0 ;	
		if (0 <= SelectedSkill)
		{
			for (int i = 0 ; i <= spells[SelectedSkill].getPreRequisites().length - 1 ; ++i)
			{
				int PreReqID = spells[SelectedSkill].getPreRequisites()[i][0] ;
				if (0 <= PreReqID)
				{
					if (player.getSpell()[spells[SelectedSkill].getPreRequisites()[i][0]] < spells[SelectedSkill].getPreRequisites()[i][1])
					{
						++cont ;
					}
				}	
			}
			if (cont == 0)
			{
				SkillAvailability = true ;
			}	
		}
		return SkillAvailability ;
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
			Cats[7] = "* Estatísticas do jogador *" ;
			Cats[8] = "* Propriedades dos atributos especiais *" ;
			Cats[9] = "* Coleta *" ;
			Cats[10] = "* Vitória *" ;
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
			Cats[34] = "* Menu de personalização *" ;
			Cats[35] = "* Menu de quest *" ;
			Cats[36] = "* Menu de opções *" ;
			Cats[37] = "* Menu de dicas *" ;
			Cats[38] = "* Dimensões *" ;
			Cats[39] = "* Cores *" ;
			Cats[40] = "* Janela do jogador *" ;
			Cats[41] = "* Cidadão 0 *" ;
			Cats[42] = "* Cidadão 1 *" ;
			Cats[43] = "* Cidadão 2 *" ;
			Cats[44] = "* Cidadão 3 *" ;
			Cats[45] = "* Cidadão 4 *" ;
			Cats[46] = "* Cidadão 5 *" ;
			Cats[47] = "* Cidadão 6 *" ;
			Cats[48] = "* Cidadão 7 *" ;
			Cats[49] = "* Cidadão 8 *" ;
			Cats[50] = "* Cidadão 9 *" ;
			Cats[51] = "* Cidadão 10 *" ;
			Cats[52] = "* Cidadão 11 *" ;
			Cats[53] = "* Cidadão 12 *" ;
			Cats[54] = "* Cidadão 13 *" ;
			Cats[55] = "* Cidadão 14 *" ;
			Cats[56] = "* Cidadão 15 *" ;
			Cats[57] = "* Cidadão 16 *" ;
			Cats[58] = "* Cidadão 17 *" ;
			Cats[59] = "* Cidadão 18 *" ;
			Cats[60] = "* Cidadão 19 *" ;
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
	
	public static int[][] NPCsInBuildings(NPCs[] npc, Buildings[] buildings, int map, int[] BuildingsInCity)
	{
		int[][] IDs = null ;
		for (int b = 0 ; b <= BuildingsInCity.length - 1 ; b += 1)
		{
			IDs = Utg.IncreaseArraySize(IDs, 1) ;
			for (int n = 0 ; n <= npc.length - 1 ; n += 1)
			{
				if (npc[n].getMap() == map & npc[n].getPosRelToBuilding().equals(buildings[BuildingsInCity[b]].getName()))
				{
					IDs[b] = Utg.AddElem(IDs[b], npc[n].getID()) ;
				}
			}
		}
		return IDs ;
	}
	
	public static int[] BuildingsInCity(Buildings[] buildings, int map)
	{
		int[] IDs = null ;
		for (int b = 0 ; b <= buildings.length - 1 ; b += 1)
		{
			if (buildings[b].getMap() == map)
			{
				IDs = Utg.AddElem(IDs, buildings[b].getID()) ;
			}
		}
		return IDs ;
	}
	
	public static int[] BuildingPos(Buildings[] buildings, int map, String Name)
	{
		for (int b = 0 ; b <= buildings.length - 1 ; b += 1)
		{
			if (buildings[b].getName().equals(Name) & buildings[b].getMap() == map)
			{
				return buildings[b].getPos() ;
			}
		}
		return null ;
	}

	public static float[] LevelUpIncAtt(float[] AttributeIncrease, float[] ChanceIncrease, int Level)
	{
		// Life, Mp, Phyatk, Magatk, Phydef, Magdef, Dex, Agi, Exp
		float[] Increase = new float[AttributeIncrease.length + 1] ;
		for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= ChanceIncrease[i])
			{
				Increase[i] = AttributeIncrease[i] ;
			}
		}
		Increase[AttributeIncrease.length] = (float) (10*(3*Math.pow(Level - 1, 2) + 3*(Level - 1) + 1) - 5) ;
		return Increase ;
	}		
		
	public static String IdentifySave(Player player, Pet pet, float[][] EquipsBonus, int NumberOfSlots, Maps[] maps)
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
	}
	
	public static String[] RecordCombo(String[] CurrentCombo, String move, int length)
	{
		String[] newCombo = null ;
		if (!move.equals(""))
		{
			if (CurrentCombo == null)
			{
				newCombo = Utg.AddElem(newCombo, move) ;
			}
			else
			{
				if (CurrentCombo.length < length)
				{
					newCombo = new String[CurrentCombo.length + 1] ;
					newCombo[0] = move ;
					for (int i = 1 ; i <= CurrentCombo.length ; i += 1)
					{
						newCombo[i] = CurrentCombo[i - 1] ;
					}
				}
				else
				{
					newCombo = new String[length] ;
					newCombo[0] = move ;
					for (int i = 1 ; i <= length - 1 ; i += 1)
					{
						newCombo[i] = CurrentCombo[i - 1] ;
					}
				}
			}
			return newCombo ;
		}
		else
		{
			return CurrentCombo ;
		}
	}
	
	public static boolean KonamiCodeActivated(String[] Combo)
	{
		String[] KonamiCode = new String[] {"A", "B", "Direita", "Esquerda", "Direita", "Esquerda", "Abaixo", "Abaixo", "Acima", "Acima"} ;
		
		if (Combo != null)
		{
			if (Arrays.equals(Combo, KonamiCode))
			{
				return true ;
			}
		}
		return false ;
	}

	public void PrintBook(Items[] items, int[][] CraftingIngredients, int[][] CraftingProducts)
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
			/*for (int j = 0 ; j <= CraftingProducts[i].length - 1 ; ++j)
			{
				if (-1 < CraftingProducts[i][j])
				{
					System.out.println(items[CraftingProducts[i][j]].getID() + " " + items[CraftingProducts[i][j]].getName()) ;
				}
			}
			System.out.println() ;
			System.out.println() ;*/
		}
	}	
		
	public void BattleSimulation(Player player, Pet pet, Creatures creature, Screen screen, Skills[] skills, PetSkills[] petskills, int[] ActivePlayerSkills, String[] MoveKeys, Items[] items, Quests[] quest, int NumberOfSimulations, String[] ActionKeys, String[] SkillKeys, Battle B, DrawFunctions DF)
	{
		int move = 0 ;
		//String[] ItemsObtained = new String[10] ;
		int NumberOfPlayerWins = 0, NumberOfPlayerPhyAtks = 0, NumberOfPetPhyAtks = 0, NumberOfPlayerMagAtks = 0, NumberOfPetMagAtks = 0, NumberOfPlayerDefs = 0, NumberOfPetDefs = 0 ;
		float[] BattleResults = new float[5] ;
		System.out.println("Creature life: " + creature.getLife()[0] + " Creature phy atk: " + creature.getPhyAtk()[0]) ;
		
		for (int i = 0 ; i <= NumberOfSimulations - 1 ; ++i)
		{
			System.out.println("") ;
			System.out.println("Simulation " + i + ": player vs creature level " + creature.getLevel()) ;
			BattleResults[0] = 1 ;
			do
			{
				for (int j = 0 ; j <= player.getActions().length - 1 ; ++j)
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
				}
				for (int j = 0 ; j <= player.getBattleAtt().getBattleActions().length - 1 ; ++j)
				{
					if(player.getBattleAtt().getBattleActions()[j][0] < player.getBattleAtt().getBattleActions()[j][1])
					{
						player.getBattleAtt().getBattleActions()[j][0] += 1 ;
					}	
				}
				for (int j = 0 ; j <= pet.getBattleAtt().getBattleActions().length - 1 ; ++j)
				{
					if(pet.getBattleAtt().getBattleActions()[j][0] < pet.getBattleAtt().getBattleActions()[j][1])
					{
						pet.getBattleAtt().getBattleActions()[j][0] += 1 ;
					}	
				}
				for (int j = 0 ; j <= creature.getBattleAtt().getBattleActions().length - 1 ; ++j)
				{
					if(creature.getBattleAtt().getBattleActions()[j][0] < creature.getBattleAtt().getBattleActions()[j][1])
					{
						creature.getBattleAtt().getBattleActions()[j][0] += 1 ;
					}	
				}
				if (player.getBattleAtt().getBattleActions()[0][0] % player.getBattleAtt().getBattleActions()[0][1] == 0)
				{
					player.getBattleAtt().getBattleActions()[0][2] = 1 ;	// Player can atk
				}
				if (0 < pet.getLife()[0] & pet.getBattleAtt().getBattleActions()[0][0] % pet.getBattleAtt().getBattleActions()[0][1] == 0)
				{
					pet.getBattleAtt().getBattleActions()[0][2] = 1 ;		// pet can atk
				}
				if (player.getActions()[2][0] % player.getActions()[2][1] == 0)	// Player heals mp
				{
					player.getMp()[0] = (float)(Math.min(player.getMp()[0] + 0.02*player.getMp()[1], player.getMp()[1])) ;	
					player.getActions()[2][0] = 0 ;
				}
				if (pet.getActions()[2][0] % pet.getActions()[2][1] == 0)	// Pet heals mp
				{
					pet.getMp()[0] = (float)(Math.min(pet.getMp()[0] + 0.02*pet.getMp()[1], pet.getMp()[1])) ;	
					pet.getActions()[2][0] = 0 ;
				}
				for (int j = 0 ; j <= creature.getBattleAtt().getBattleActions().length - 1 ; ++j)
				{
					if (creature.getBattleAtt().getBattleActions()[j][0] % creature.getBattleAtt().getBattleActions()[j][1] == 0)
					{
						creature.getBattleAtt().getBattleActions()[j][2] = 1 ;	// Creature can atk
					}	
					if (creature.getActions()[1][0] % creature.getActions()[1][1] == 0)
					{
						creature.getMp()[0] = (float)(Math.min(creature.getMp()[0] + 0.02*creature.getMp()[1], creature.getMp()[1])) ;	// Creature heals mp
						creature.getActions()[1][0] = 0 ;
					}	
				}	
				if (0 < player.getLife()[0] & 0 < player.getBattleAtt().getBattleActions()[0][2])
				{				
					move = (int)(3*Math.random() - 0.01) ;
					if (move == 0)
					{
						player.action = ActionKeys[1] ;
						NumberOfPlayerPhyAtks += 1 ;
					}
					if (move == 1)
					{
						player.action = ActionKeys[3] ;
						NumberOfPlayerDefs += 1 ;
					}
					if (move == 2)
					{
						player.action = String.valueOf((int)(1*Math.random() - 0.01)) ;
						NumberOfPlayerMagAtks += 1 ;
					}
				}
				if (0 < pet.getLife()[0] & 0 < pet.getBattleAtt().getBattleActions()[0][2])
				{
					move = (int)(3*Math.random() - 0.01) ;
					if (move == 0)
					{
						pet.action = ActionKeys[1] ;
						NumberOfPetPhyAtks += 1 ;
					}
					if (move == 1)
					{
						pet.action = ActionKeys[3] ;
						NumberOfPetDefs += 1 ;
					}
					if (move == 2)
					{
						pet.action = String.valueOf((int)(10*Math.random() - 0.01)) ;
						NumberOfPetMagAtks += 1 ;
					}
				}
				B.RunBattle(player, pet, creature, screen, skills, petskills, ActivePlayerSkills, items, quest, true, new int[2], DF) ;
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
