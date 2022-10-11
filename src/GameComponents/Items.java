package GameComponents ;

import java.awt.Image ;
import java.io.IOException ;
import javax.swing.ImageIcon ;
import Main.Utg ;

public class Items
{
	private int id ;
	private String Name ;
	private Image image ;
	private int Price ;
	private float DropChance ;
	private float[][] Buffs ;
	private String Description ;
	private String Type ;

	public static String LongestName ;
	public static int NumberOfAllItems ;
	public static int[] NumberOfItems = new int[] {60, 60, 40, 60, 60, 20, 1000, 400, 100, 200} ;	// Potions, Alchemy, Forge, Pet, Food, Arrows, Equips, General items, Fab, Quest
	public static int[] BagIDs = new int[NumberOfItems.length + 1] ;	// First id of: Potions, Alchemy, Forge, Pet, Food, Arrows, Equips, General items, Fab, Quest ; and last id of Quest
	public static float[][] PotionsHealing, PetItems, FoodSatiation, ArrowPower, EquipsBonus ;
	public static int NumberOfCraftingItems ;
	public static int[] CraftingNPCIDs ;
	public static int[][] CraftingIngredients, CraftingIngredientAmounts, CraftingProducts, CraftingProductAmounts ;
	public static String[] ArrowElem ;
	public static String[] EquipsElem ;
	public static int[] ItemsWithEffects ;
	public static String[] ItemsTargets, ItemsElement ;
	public static float[][][] ItemsEffects ;
	public static float[][][] ItemsBuffs ;	 
	public static Image[] EquipImage, EquipGif ;
	
	public Items(int ID, String Name, Image image, int Price, float DropChance, float[][] Buffs, String Description, String Type)
	{
		this.id = ID ;
		this.Name = Name ;
		this.image = image ;
		this.Price = Price ;
		this.DropChance = DropChance ;
		this.Buffs = Buffs ;
		this.Description = Description ;
		this.Type = Type ;
	}
	
	
	
	public int getID() {return id ;}
	public String getName() {return Name ;}
	public Image getImage() {return image ;}
	public int getPrice() {return Price ;}
	public float getDropChance() {return DropChance ;}
	public float[][] getBuffs() {return Buffs ;}
	public String getDescription() {return Description ;}
	public String getType() {return Type ;}
	public void setID(int I) {id = I ;}
	public void setName(String N) {Name = N ;}
	public void setImage(Image I) {image = I ;}
	public void setPrice(int P) {Price = P ;}
	public void setDropChance(float DC) {DropChance = DC ;}
	public void setBuffs(float[][] B) {Buffs = B ;}
	public void setDescription(String D) {Description = D ;}

	public static void CalcNumberOfAllItems()
	{
		NumberOfAllItems = 0 ;
		for (int i = 0 ; i <= NumberOfItems.length - 1 ; ++i)
		{
			NumberOfAllItems += NumberOfItems[i] ;
		}
	}
	public static void CalcNumberOfCraftingItems(String CSVPath)
	{
		try
		{
			NumberOfCraftingItems = Utg.count(CSVPath + "Craft.csv") ;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace() ;
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
		PotionsHealing = new float[NumberOfItems[0]][3] ;// [ID, life healing, mp healing]
		PetItems = new float[NumberOfItems[3]][4] ;		// [ID, life healing, mp healing, satiation]
		FoodSatiation = new float[NumberOfItems[4]][4] ;	// [ID, life healing, mp healing, satiation]
		ArrowPower = new float[NumberOfItems[5]][1] ;	// [ID, atk power]
		ArrowElem = new String[NumberOfItems[5]] ;		// [ID]
		EquipsBonus = new float[NumberOfItems[6]][32] ;	// [ID, Forge level, Life bonus, Mp bonus, PhyAtk bonus, MagAtk bonus, PhyDef bonus, MagDef bonus, Dex bonus, Agi bonus, Crit bonus, Stun bonus, Block bonus, Blood bonus, Poison bonus]
		EquipsElem = new String[NumberOfItems[6]] ;
		String[][] PotionsInput = Utg.ReadTextFile(CSVPath + "Potions.csv", Items.NumberOfItems[0]) ;	
		for (int i = 0 ; i <= Items.NumberOfItems[0] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 3 - 1 ; ++j)
			{
				PotionsHealing[i][j] = Float.parseFloat(PotionsInput[i][j]) ;
			}
		}
		String[][] PetItemsInput = Utg.ReadTextFile(CSVPath + "PetItems.csv", Items.NumberOfItems[3]) ;	
		for (int i = 0 ; i <= Items.NumberOfItems[3] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 4 - 1 ; ++j)
			{
				PetItems[i][j] = Float.parseFloat(PetItemsInput[i][j]) ;
			}
		}
		String[][] FoodInput = Utg.ReadTextFile(CSVPath + "Food.csv", Items.NumberOfItems[4]) ;	
		for (int i = 0 ; i <= Items.NumberOfItems[4] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 4 - 1 ; ++j)
			{
				FoodSatiation[i][j] = Float.parseFloat(FoodInput[i][j]) ;
			}
		}
		String[][] ArrowInput = Utg.ReadTextFile(CSVPath + "ArrowPower.csv", Items.NumberOfItems[5]) ;	
		for (int i = 0 ; i <= Items.NumberOfItems[5] - 1 ; ++i)
		{
			for (int j = 0 ; j <= ArrowPower[i].length - 1 ; ++j)
			{
				ArrowPower[i][j] = Float.parseFloat(ArrowInput[i][j + 1]) ;
			}
			ArrowElem[i] = ArrowInput[i][2] ;
		}
		String[][] EquipsInput = Utg.ReadTextFile(CSVPath + "Equips.csv", Items.NumberOfItems[6]) ;	
		for (int i = 0 ; i <= Items.NumberOfItems[6] - 1 ; ++i)
		{
			for (int j = 0 ; j <= 31 - 1 ; ++j)
			{
				EquipsBonus[i][j] = Float.parseFloat(EquipsInput[i][j]) ;
			}
			EquipsElem[i] = EquipsInput[i][31] ;
		}
	}
	public static void CalcCrafting(String CSVPath)
	{
		CraftingNPCIDs = new int[Items.NumberOfCraftingItems] ;
		CraftingIngredients = new int[Items.NumberOfCraftingItems][10] ;
		CraftingIngredientAmounts = new int[Items.NumberOfCraftingItems][10] ;
		CraftingProducts = new int[Items.NumberOfCraftingItems][10] ;
		CraftingProductAmounts = new int[Items.NumberOfCraftingItems][10] ;
		String[][] CraftingInput = Utg.ReadTextFile(CSVPath + "Craft.csv", Items.NumberOfCraftingItems) ;	
		for (int i = 0 ; i <= Items.NumberOfCraftingItems - 1 ; i += 1)
		{
			CraftingNPCIDs[i] = Integer.parseInt(CraftingInput[i][0]) ;
			for (int j = 0 ; j <= 10 - 1 ; ++j)
			{
				CraftingIngredients[i][j] = Integer.parseInt(CraftingInput[i][2*j + 1]) ;
				CraftingIngredientAmounts[i][j] = Integer.parseInt(CraftingInput[i][2*j + 2]) ;
				CraftingProducts[i][j] = Integer.parseInt(CraftingInput[i][2*j + 21]) ;
				CraftingProductAmounts[i][j] = Integer.parseInt(CraftingInput[i][2*j + 22]) ;
			}
		}
	}
	public static void CalcItemsWithEffects(String CSVPath)
	{
		ItemsWithEffects = new int[111] ;
		ItemsTargets = new String[ItemsWithEffects.length] ;
		ItemsElement = new String[ItemsWithEffects.length] ;
		ItemsEffects = new float[ItemsWithEffects.length][15][3] ;
		ItemsBuffs = new float[ItemsWithEffects.length][14][13] ;
		String[][] ItemsEffectsInput = Utg.ReadTextFile(CSVPath + "ItemsEffects.csv", ItemsWithEffects.length) ;
		for (int i = 0 ; i <= ItemsWithEffects.length - 1 ; ++i)
		{
			ItemsWithEffects[i] = Integer.parseInt(ItemsEffectsInput[i][0]) ;
			int BuffCont = 0 ;
			for (int j = 0 ; j <= 14 - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= 13 - 1 ; k += 1)
					{
						ItemsBuffs[i][j][k] = Float.parseFloat(ItemsEffectsInput[i][BuffCont + 18]) ;
						BuffCont += 1 ;
					}
				}
				else
				{
					ItemsBuffs[i][j][0] = Float.parseFloat(ItemsEffectsInput[i][BuffCont + 18]) ;
					ItemsBuffs[i][j][1] = Float.parseFloat(ItemsEffectsInput[i][BuffCont + 19]) ;
					ItemsBuffs[i][j][2] = Float.parseFloat(ItemsEffectsInput[i][BuffCont + 20]) ;
					ItemsBuffs[i][j][12] = Float.parseFloat(ItemsEffectsInput[i][BuffCont + 21]) ;
					BuffCont += 4 ;
				}
			}
			ItemsTargets[i] = ItemsEffectsInput[i][1] ;
			ItemsElement[i] = ItemsEffectsInput[i][2] ;
			ItemsEffects[i] = new float[][] {{1, Float.parseFloat(ItemsEffectsInput[i][3]), 0}, {1, Float.parseFloat(ItemsEffectsInput[i][4]), Float.parseFloat(ItemsEffectsInput[i][5])}, {1, Float.parseFloat(ItemsEffectsInput[i][6]), Float.parseFloat(ItemsEffectsInput[i][7])}, {Float.parseFloat(ItemsEffectsInput[i][8]), Float.parseFloat(ItemsEffectsInput[i][9]), Float.parseFloat(ItemsEffectsInput[i][10])}, {Float.parseFloat(ItemsEffectsInput[i][11]), Float.parseFloat(ItemsEffectsInput[i][12]), Float.parseFloat(ItemsEffectsInput[i][13])}, {1, Float.parseFloat(ItemsEffectsInput[i][14]), Float.parseFloat(ItemsEffectsInput[i][15])}, {1, Float.parseFloat(ItemsEffectsInput[i][16]), Float.parseFloat(ItemsEffectsInput[i][17])}} ;			
		}
	}

	public static void InitializeStaticVars(String ImagesPath)
	{		
		// Equip images
		Image Sword = new ImageIcon(ImagesPath + "Eq0_Sword.png").getImage() ;
		Image Staff = new ImageIcon(ImagesPath + "Eq1_Staff.png").getImage() ;
		Image Bow = new ImageIcon(ImagesPath + "Eq2_Bow.png").getImage() ;
		Image Claws = new ImageIcon(ImagesPath + "Eq3_Claws.png").getImage() ;
		Image Dagger = new ImageIcon(ImagesPath + "Eq4_Dagger.png").getImage() ;
		Image Shield = new ImageIcon(ImagesPath + "Eq5_Shield.png").getImage() ;
		Image Armor = new ImageIcon(ImagesPath + "Eq6_Armor.png").getImage() ;
		Image Arrow = new ImageIcon(ImagesPath + "Eq7_Arrow.png").getImage() ;
		EquipImage = new Image[] {Sword, Staff, Bow, Claws, Dagger, Shield, Armor, Arrow} ;
		
		// Equip gifs
		/*Image SwordGif = new ImageIcon(ImagesPath + "Eq0_Sword.gif").getImage() ;
		Image StaffGif = new ImageIcon(ImagesPath + "Eq1_Staff.gif").getImage() ;
		Image BowGif = new ImageIcon(ImagesPath + "Eq2_Bow.gif").getImage() ;
		Image ClawsGif = new ImageIcon(ImagesPath + "Eq3_Claws.gif").getImage() ;
		Image DaggerGif = new ImageIcon(ImagesPath + "Eq4_Dagger.gif").getImage() ;
		Image ShieldGif = new ImageIcon(ImagesPath + "Eq5_Shield.gif").getImage() ;
		Image ArmorGif = new ImageIcon(ImagesPath + "Eq6_Armor.gif").getImage() ;
		Image ArrowGif = new ImageIcon(ImagesPath + "Eq7_Arrow.gif").getImage() ;
		EquipGif = new Image[] {SwordGif, StaffGif, BowGif, ClawsGif, DaggerGif, ShieldGif, ArmorGif, ArrowGif} ;*/
	}
}