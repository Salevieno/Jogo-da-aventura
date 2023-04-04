package utilities ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays ;
import java.util.List;

import maps.GameMap;
import maps.GroundType;
import maps.GroundTypes;

public abstract class UtilS 
{	
	
	public static Color[] ReadColorPalette(Image paletteImage, String mode)
	{
		Color[] palette = new Color[28] ;

		switch (mode)
		{
			case "Normal":
			{
				for (int y = 0 ; y <= 7 - 1 ; y += 1)
				{
					for (int x = 0 ; x <= 4 - 1 ; x += 1)
					{
						palette[x + 4 * y] = UtilG.GetPixelColor(UtilG.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}
				
				break ;
			}
			case "Konami":
			{
				for (int x = 0 ; x <= 4 - 1 ; x += 1)
				{
					for (int y = 0 ; y <= 7 - 1 ; y += 1)
					{
						palette[7 * x + y] = UtilG.GetPixelColor(UtilG.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}
				
				break ;
			}
		}
		return palette ;
	}

	public static String RelPos(Point point, Point refPos) { return refPos.x < point.x ? "Right" : "Left" ;}
	
	public static int MirrorFromRelPos(String relPos) { return relPos.equals("Left") ? -1 : 1 ;}
	
	public static RelativePos calcRelativePos(Point pos, Point targetPos, Dimension targetSize)
	{
		if (UtilG.isInside(pos, targetPos, targetSize))
		{
			return RelativePos.inside;
		}
		if (pos.x == (targetPos.x - 1) & (targetPos.y <= pos.y & pos.y <= targetPos.y + targetSize.height))
		{
			return RelativePos.left ;
		}
		if (pos.x == (targetPos.x + 1) & (targetPos.y <= pos.y & pos.y <= targetPos.y + targetSize.height))
		{
			return RelativePos.right ;
		}
		if ((targetPos.x <= pos.x & pos.x <= targetPos.x + targetSize.width) & pos.y == (targetPos.y - 1))
		{
			return RelativePos.above ;
		}
		if ((targetPos.x <= pos.x & pos.x <= targetPos.x + targetSize.width) & pos.y == (targetPos.y + 1))
		{
			return RelativePos.below ;
		}
		
		return null ;
	}
		
 	public static RelativePos checkAdjacentGround(Point pos, GameMap map, GroundTypes targetGroundType)
	{
 		
 		Point userPos = new Point(pos) ;

		if (map == null) { return null ;}
		
		if (map.getgroundTypes() == null) { return null ;}

		for (GroundType groundType : map.getgroundTypes())
		{
			if (!groundType.getType().equals(targetGroundType)) { continue ;}	
			
			return calcRelativePos(userPos, groundType.getPos(), groundType.getSize()) ;
		}
		
		return null ;
		
	}
 	
 	public static boolean isTouching(Point pos, GameMap map, GroundTypes groundType)
 	{
 		RelativePos adjGround = checkAdjacentGround(pos, map, groundType) ;
 		
 		if (adjGround == null) { return false ;}
 		
 		List<RelativePos> adjPositions = Arrays.asList(RelativePos.values()) ;
 		
 		return adjPositions.contains(adjGround) ;
 	}
 	
 	public static boolean isInside(Point pos, GameMap map, GroundTypes groundType)
 	{
 		RelativePos adjGround = checkAdjacentGround(pos, map, groundType) ;
 		
 		if (adjGround == null) { return false ;}
 		
 		return adjGround.equals(RelativePos.inside) ;
 	}
		
//	public static int UpAndDownCounter(int counter, int duration)
//	{
//		if ((counter/duration & 1) == 0)	// counter % looptime is even
//		{
//			return counter % duration ;
//		}
//		else
//		{
//			return duration - (counter % duration) ;
//		}
//	}

// 	public static void PrintBattleActions(int message, String user, String receiver, int damage, int effect, int[] status, String[] elem)
//	{
//		System.out.println() ;
//		if (message == 0)
//		{
//			System.out.println(user + " inflicts status on " + receiver + " " + Arrays.toString(status)) ;
//			if (effect == 0)
//			{
//				System.out.println(user + " deals damage on " + receiver + " ! Damage = " + damage) ;
//			} else if (effect == 1)
//			{
//				System.out.println(user + " deals critical damage on " + receiver + " ! Damage = " + damage) ;
//			}
//			else if (effect == 2)
//			{
//				System.out.println(user + " misses " + receiver + "!") ;
//			} 
//			else if (effect == 3)
//			{
//				System.out.println(receiver + " blocks " + user + "!") ;
//			}
//		}
//		if (message == 1)
//		{
//			System.out.println(user + " is stun!") ;
//		}
//		if (message == 2)
//		{
//			System.out.println(user + " uses spell on " + receiver + " ! Damage = " + damage) ;
//		}
//		if (message == 3)
//		{
//			System.out.println(user + " defends!") ;
//		}
//		if (message == 4)
//		{
//			System.out.println("Arrow power added!") ;
//		}
//		if (message == 5)
//		{
//			System.out.println(user + " elem: " + Arrays.toString(elem)) ;
//		}
//		if (message == 6)
//		{
//			System.out.println(user + " def down!") ;
//		}
//		if (message == 7)
//		{
//			System.out.println("Skill buff") ;
//		}
//		System.out.println() ;
//		System.out.println() ;
//	}
	
//	public static RelativePos calcRelativePos(Point pos, Point targetPos)
//	{
//		if (pos.equals(targetPos))
//		{
//			return RelativePos.inside;
//		}
//		if (pos.x == (targetPos.x - 1) & pos.y == targetPos.y)
//		{
//			return RelativePos.left ;
//		}
//		if (pos.x == (targetPos.x + 1) & pos.y == targetPos.y)
//		{
//			return RelativePos.right ;
//		}
//		if (pos.x == targetPos.x & pos.y == (targetPos.y - 1))
//		{
//			return RelativePos.below ;
//		}
//		if (pos.x == targetPos.x & pos.y == (targetPos.y + 1))
//		{
//			return RelativePos.above ;
//		}
//		
//		return null ;
//	}
	
	/*public static void PrintBattleActions2(String useraction, String receiveraction, String user, String receiver, Object[] AtkResult, String[] elem)
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
	*/
	
	/*
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
*/
	
	
	/*
	
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
			Cats[1] = "* Besti�rio *" ;
			Cats[2] = "* Novo jogo *" ;
			Cats[3] = "* Tutorial *" ;
			Cats[4] = "* Classes *" ;
			Cats[5] = "* ProClasses *" ;
			Cats[6] = "* Atributos *" ;
			Cats[7] = "* Estatísticas do jogador *" ;
			Cats[8] = "* Propriedades dos atributos especiais *" ;
			Cats[9] = "* Coleta *" ;
			Cats[10] = "* Vit�ria *" ;
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
			Cats[34] = "* Menu de personaliza��o *" ;
			Cats[35] = "* Menu de quest *" ;
			Cats[36] = "* Menu de op��es *" ;
			Cats[37] = "* Menu de dicas *" ;
			Cats[38] = "* Dimens�es *" ;
			Cats[39] = "* Cores *" ;
			Cats[40] = "* Janela do jogador *" ;
			Cats[41] = "* Cidad�o 0 *" ;
			Cats[42] = "* Cidad�o 1 *" ;
			Cats[43] = "* Cidad�o 2 *" ;
			Cats[44] = "* Cidad�o 3 *" ;
			Cats[45] = "* Cidad�o 4 *" ;
			Cats[46] = "* Cidad�o 5 *" ;
			Cats[47] = "* Cidad�o 6 *" ;
			Cats[48] = "* Cidad�o 7 *" ;
			Cats[49] = "* Cidad�o 8 *" ;
			Cats[50] = "* Cidad�o 9 *" ;
			Cats[51] = "* Cidad�o 10 *" ;
			Cats[52] = "* Cidad�o 11 *" ;
			Cats[53] = "* Cidad�o 12 *" ;
			Cats[54] = "* Cidad�o 13 *" ;
			Cats[55] = "* Cidad�o 14 *" ;
			Cats[56] = "* Cidad�o 15 *" ;
			Cats[57] = "* Cidad�o 16 *" ;
			Cats[58] = "* Cidad�o 17 *" ;
			Cats[59] = "* Cidad�o 18 *" ;
			Cats[60] = "* Cidad�o 19 *" ;
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
		*/
	
/*
	public static void BattleSimulation(Player player, Pet pet, Creature creature, Spell[] skills, Spell[] petskills, int[] ActivePlayerSkills, String[] MoveKeys, Items[] items, Quests[] quest, int NumberOfSimulations, String[] ActionKeys, String[] SkillKeys, Battle B, DrawFunctions DF)
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
				//for (int j = 0 ; j <= player.getActions().length - 1 ; ++j)
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
				//}
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
				//if (player.getActions()[2][0] % player.getActions()[2][1] == 0)	// Player heals mp
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
				//}	
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
*/









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

	/*public static boolean GroundIsWalkable(String GroundType, String SuperElem)
	{
		if (GroundType.equals("free") | GroundType.equals("Lava") | GroundType.contains("Chest") | GroundType.equals("Grass")  | GroundType.equals("Berry") | GroundType.equals("Herb") | GroundType.equals("Wood") | GroundType.equals("Metal") | (GroundType.equals("Water") & SuperElem.equals("w")) | ((GroundType.equals("Tree") | (GroundType.equals("Rock"))) & SuperElem.equals("a")))
		{
			return true ;
		}
		return false ;
	}*/
	

}