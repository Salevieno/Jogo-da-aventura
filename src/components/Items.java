package components ;

import java.awt.Image ;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon ;

import main.Game;
import utilities.Elements;
import utilities.UtilG;

public class Items
{
	private int id ;
	private String Name ;
	private Image image ;
	private int Price ;
	private double DropChance ;
	private double[][] Buffs ;
	private String Description ;
	private String Type ;

	public static String LongestName ;
	public static int NumberOfAllItems ;
	public static int[] NumberOfItems = new int[] {60, 60, 40, 60, 60, 20, 1000, 400, 100, 200} ;
	public static int[] BagIDs = new int[] {0, 60, 120, 160, 220, 280, 300, 1300, 1700, 1800, 2000} ;
	public static double[][] PotionsHealing = new double[NumberOfItems[0]][3] ;
	public static double[][] PetItems = new double[NumberOfItems[3]][4] ;
	public static double[][] FoodSatiation = new double[NumberOfItems[4]][4] ;
	public static double[][] ArrowPower = new double[NumberOfItems[5]][1] ;
	public static double[][] EquipsBonus = new double[NumberOfItems[6]][32] ;
	public static Elements[] ArrowElem = new Elements[NumberOfItems[5]] ;
	public static Elements[] EquipsElem = new Elements[NumberOfItems[6]] ;
	public static int[] ItemsWithEffects ;
	public static String[] ItemsTargets, ItemsElement ;
	public static double[][][] ItemsEffects ;
	public static double[][][] ItemsBuffs ;	

	// Equip images
//	private static Image Sword = UtilG.loadImage(Game.ImagesPath + "Eq0_Sword.png") ;
//	private static Image Staff = UtilG.loadImage(Game.ImagesPath + "Eq1_Staff.png") ;
//	private static Image Bow = UtilG.loadImage(Game.ImagesPath + "Eq2_Bow.png") ;
//	private static Image Claws = UtilG.loadImage(Game.ImagesPath + "Eq3_Claws.png") ;
//	private static Image Dagger = UtilG.loadImage(Game.ImagesPath + "Eq4_Dagger.png") ;
//	private static Image Shield = UtilG.loadImage(Game.ImagesPath + "Eq5_Shield.png") ;
//	private static Image Armor = UtilG.loadImage(Game.ImagesPath + "Eq6_Armor.png") ;
//	private static Image Arrow = UtilG.loadImage(Game.ImagesPath + "Eq7_Arrow.png") ;
//	public static Image[] EquipImage = new Image[] {Sword, Staff, Bow, Claws, Dagger, Shield, Armor, Arrow} ; 
	public static Image[] EquipGif ;
	
	public Items(int ID, String Name, Image image, int Price, double DropChance, double[][] Buffs, String Description, String Type)
	{
		// TODO revisar inicialização dos itens
		this.id = ID ;
		this.Name = Name ;
		this.image = image ;
		this.Price = Price ;
		this.DropChance = DropChance ;
		this.Buffs = Buffs ;
		this.Description = Description ;
		this.Type = Type ;
		
		NumberOfItems = new int[] {60, 60, 40, 60, 60, 20, 1000, 400, 100, 200} ;	// Potions, Alchemy, Forge, Pet, Food, Arrows, Equips, General items, Fab, Quest
		BagIDs = new int[NumberOfItems.length + 1] ;	// First id of: Potions, Alchemy, Forge, Pet, Food, Arrows, Equips, General items, Fab, Quest ; and last id of Quest
		PotionsHealing = new double[NumberOfItems[0]][3] ;	// [ID, life healing, mp healing]
		PetItems = new double[NumberOfItems[3]][4] ;		// [ID, life healing, mp healing, satiation]
		FoodSatiation = new double[NumberOfItems[4]][4] ;	// [ID, life healing, mp healing, satiation]
		ArrowPower = new double[NumberOfItems[5]][1] ;		// [ID, atk power]
		EquipsBonus = new double[NumberOfItems[6]][32] ;	// [ID, Forge level, Life bonus, Mp bonus, PhyAtk bonus, MagAtk bonus, PhyDef bonus, MagDef bonus, Dex bonus, Agi bonus, Crit bonus, Stun bonus, Block bonus, Blood bonus, Poison bonus]
		ArrowElem = new Elements[NumberOfItems[5]] ;		// [ID]
		EquipsElem = new Elements[NumberOfItems[6]] ;
//		ItemsWithEffects ;
//		ItemsTargets ;
//		ItemsElement ;
//		ItemsEffects ;
//		ItemsBuffs ;

		// Equip gifs
		/*Image SwordGif = UtilG.loadImage(ImagesPath + "Eq0_Sword.gif") ;
		Image StaffGif = UtilG.loadImage(ImagesPath + "Eq1_Staff.gif") ;
		Image BowGif = UtilG.loadImage(ImagesPath + "Eq2_Bow.gif") ;
		Image ClawsGif = UtilG.loadImage(ImagesPath + "Eq3_Claws.gif") ;
		Image DaggerGif = UtilG.loadImage(ImagesPath + "Eq4_Dagger.gif") ;
		Image ShieldGif = UtilG.loadImage(ImagesPath + "Eq5_Shield.gif") ;
		Image ArmorGif = UtilG.loadImage(ImagesPath + "Eq6_Armor.gif") ;
		Image ArrowGif = UtilG.loadImage(ImagesPath + "Eq7_Arrow.gif") ;
		EquipGif = new Image[] {SwordGif, StaffGif, BowGif, ClawsGif, DaggerGif, ShieldGif, ArmorGif, ArrowGif} ;*/
	}
	
	
	
	public int getID() {return id ;}
	public String getName() {return Name ;}
	public Image getImage() {return image ;}
	public int getPrice() {return Price ;}
	public double getDropChance() {return DropChance ;}
	public double[][] getBuffs() {return Buffs ;}
	public String getDescription() {return Description ;}
	public String getType() {return Type ;}
	public void setID(int I) {id = I ;}
	public void setName(String N) {Name = N ;}
	public void setImage(Image I) {image = I ;}
	public void setPrice(int P) {Price = P ;}
	public void setDropChance(double DC) {DropChance = DC ;}
	public void setBuffs(double[][] B) {Buffs = B ;}
	public void setDescription(String D) {Description = D ;}

	public static void CalcNumberOfAllItems()
	{
		NumberOfAllItems = 0 ;
		for (int i = 0 ; i <= NumberOfItems.length - 1 ; ++i)
		{
			NumberOfAllItems += NumberOfItems[i] ;
		}
	}
	public static void CalcBagIDs()
	{
		BagIDs[0] = 0 ;
		for (int i = 1 ; i <= NumberOfItems.length ; ++i)
		{
			for (int j = 1 ; j <= i ; ++j)
			{
				BagIDs[i] += NumberOfItems[j - 1] ;
			}
		}
	}
	public static void CalcItemEffects(String CSVPath)
	{
		// TODO calc item effects is reading a csv during gameplay time
		
		List<String[]> PotionsInput = UtilG.ReadcsvFile(CSVPath + "Potions.csv") ;	
		for (int i = 0 ; i <= Items.NumberOfItems[0] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 3 - 1 ; ++j)
			{
				PotionsHealing[i][j] = Double.parseDouble(PotionsInput.get(i)[j]) ;
			}
		}
		List<String[]> PetItemsInput = UtilG.ReadcsvFile(CSVPath + "PetItems.csv") ;	
		for (int i = 0 ; i <= Items.NumberOfItems[3] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 4 - 1 ; ++j)
			{
				PetItems[i][j] = Double.parseDouble(PetItemsInput.get(i)[j]) ;
			}
		}
		List<String[]> FoodInput = UtilG.ReadcsvFile(CSVPath + "Food.csv") ;	
		for (int i = 0 ; i <= Items.NumberOfItems[4] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 4 - 1 ; ++j)
			{
				FoodSatiation[i][j] = Double.parseDouble(FoodInput.get(i)[j]) ;
			}
		}
		List<String[]> ArrowInput = UtilG.ReadcsvFile(CSVPath + "ArrowPower.csv") ;	
		for (int i = 0 ; i <= Items.NumberOfItems[5] - 1 ; ++i)
		{
			for (int j = 0 ; j <= ArrowPower[i].length - 1 ; ++j)
			{
				ArrowPower[i][j] = Double.parseDouble(ArrowInput.get(i)[j + 1]) ;
			}
			ArrowElem[i] = Elements.valueOf(ArrowInput.get(i)[2]) ;
		}
		ArrayList<String[]> EquipsInput = UtilG.ReadcsvFile(CSVPath + "Equips.csv") ;	
		for (int i = 0 ; i <= Items.NumberOfItems[6] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 31 - 1 ; ++j)
			{
				EquipsBonus[i][j] = Double.parseDouble(EquipsInput.get(i)[j]) ;
			}
			EquipsElem[i] = Elements.valueOf(EquipsInput.get(i)[31]) ;
		}
	}
	/*public static void CalcCrafting(String CSVPath)
	{
		CraftingNPCIDs = new int[Items.NumberOfCraftingItems] ;
		CraftingIngredients = new int[Items.NumberOfCraftingItems][10] ;
		CraftingIngredientAmounts = new int[Items.NumberOfCraftingItems][10] ;
		CraftingProducts = new int[Items.NumberOfCraftingItems][10] ;
		CraftingProductAmounts = new int[Items.NumberOfCraftingItems][10] ;
		ArrayList<String[]> CraftingInput = UtilG.ReadcsvFile(CSVPath + "Craft.csv") ;	
		for (int i = 0 ; i <= Items.NumberOfCraftingItems - 1 ; i += 1)
		{
			CraftingNPCIDs[i] = Integer.parseInt(CraftingInput.get(i)[0]) ;
			for (int j = 0 ; j <= 10 - 1 ; ++j)
			{
				CraftingIngredients[i][j] = Integer.parseInt(CraftingInput.get(i)[2*j + 1]) ;
				CraftingIngredientAmounts[i][j] = Integer.parseInt(CraftingInput.get(i)[2*j + 2]) ;
				CraftingProducts[i][j] = Integer.parseInt(CraftingInput.get(i)[2*j + 21]) ;
				CraftingProductAmounts[i][j] = Integer.parseInt(CraftingInput.get(i)[2*j + 22]) ;
			}
		}
	}*/
	public static void CalcItemsWithEffects(String CSVPath)
	{
		ItemsWithEffects = new int[111] ;
		ItemsTargets = new String[ItemsWithEffects.length] ;
		ItemsElement = new String[ItemsWithEffects.length] ;
		ItemsEffects = new double[ItemsWithEffects.length][15][3] ;
		ItemsBuffs = new double[ItemsWithEffects.length][14][13] ;
		ArrayList<String[]> ItemsEffectsInput = UtilG.ReadcsvFile(CSVPath + "ItemsEffects.csv") ;
		for (int i = 0 ; i <= ItemsWithEffects.length - 1 ; ++i)
		{
			ItemsWithEffects[i] = Integer.parseInt(ItemsEffectsInput.get(i)[0]) ;
			int BuffCont = 0 ;
			for (int j = 0 ; j <= 14 - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= 13 - 1 ; k += 1)
					{
						ItemsBuffs[i][j][k] = Double.parseDouble(ItemsEffectsInput.get(i)[BuffCont + 18]) ;
						BuffCont += 1 ;
					}
				}
				else
				{
					ItemsBuffs[i][j][0] = Double.parseDouble(ItemsEffectsInput.get(i)[BuffCont + 18]) ;
					ItemsBuffs[i][j][1] = Double.parseDouble(ItemsEffectsInput.get(i)[BuffCont + 19]) ;
					ItemsBuffs[i][j][2] = Double.parseDouble(ItemsEffectsInput.get(i)[BuffCont + 20]) ;
					ItemsBuffs[i][j][12] = Double.parseDouble(ItemsEffectsInput.get(i)[BuffCont + 21]) ;
					BuffCont += 4 ;
				}
			}
			ItemsTargets[i] = ItemsEffectsInput.get(i)[1] ;
			ItemsElement[i] = ItemsEffectsInput.get(i)[2] ;
			ItemsEffects[i] = new double[][] {{1, Double.parseDouble(ItemsEffectsInput.get(i)[3]), 0}, {1, Double.parseDouble(ItemsEffectsInput.get(i)[4]), Double.parseDouble(ItemsEffectsInput.get(i)[5])}, {1, Double.parseDouble(ItemsEffectsInput.get(i)[6]), Double.parseDouble(ItemsEffectsInput.get(i)[7])}, {Double.parseDouble(ItemsEffectsInput.get(i)[8]), Double.parseDouble(ItemsEffectsInput.get(i)[9]), Double.parseDouble(ItemsEffectsInput.get(i)[10])}, {Double.parseDouble(ItemsEffectsInput.get(i)[11]), Double.parseDouble(ItemsEffectsInput.get(i)[12]), Double.parseDouble(ItemsEffectsInput.get(i)[13])}, {1, Double.parseDouble(ItemsEffectsInput.get(i)[14]), Double.parseDouble(ItemsEffectsInput.get(i)[15])}, {1, Double.parseDouble(ItemsEffectsInput.get(i)[16]), Double.parseDouble(ItemsEffectsInput.get(i)[17])}} ;			
		}
	}
}