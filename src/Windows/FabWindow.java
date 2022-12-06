package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import Items.Item;
import Items.Recipe;
import LiveBeings.Player;
import Main.Game;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;

public class FabWindow extends Window
{
    public static Image image ;
    //private static ArrayList<Recipe> recipes ;
    
	public FabWindow()
	{
		super(null, 0, 0, 0, 3) ;
		image = new ImageIcon(Game.ImagesPath + "Book.png").getImage() ;
		//LoadCraftingRecipes() ;
		//numberWindows = recipes.size() ;
	}

	public void navigate(String action)
	{
		if (action.equals(Player.ActionKeys[3]))
		{
			windowUp() ;
		}
		if (action.equals(Player.ActionKeys[1]))
		{
			windowDown() ;
		}
	}
	
	public ArrayList<Recipe> LoadCraftingRecipes()
	{
		ArrayList<String[]> CraftingInput = UtilG.ReadcsvFile(Game.CSVPath + "Craft.csv") ;
			
		ArrayList<Recipe> recipes = new ArrayList<>() ;
		Item[] allItems = Game.getAllItems() ;
		for (int i = 0 ; i <= CraftingInput.size() - 1 ; i += 1)
		{			
			// adding ingredients
			Map<Item, Integer> ingredients = new HashMap<>() ;
			for (int j = 0 ; j <= 10 - 1 ; j += 1)
			{
				if (0 < Integer.parseInt(CraftingInput.get(i)[2 * j + 2]))
				{
					Item ingredient = allItems[Integer.parseInt(CraftingInput.get(i)[2 * j + 1])] ;
					int ingredientAmount = Integer.parseInt(CraftingInput.get(i)[2 * j + 2]) ;
					ingredients.put(ingredient, ingredientAmount) ;
				}
			}
			
			// adding products
			Map<Item, Integer> products = new HashMap<>() ;
			for (int j = 10 ; j <= 20 - 1 ; j += 1)
			{
				if (0 < Integer.parseInt(CraftingInput.get(i)[2 * j + 2]))
				{
					Item product = allItems[Integer.parseInt(CraftingInput.get(i)[2 * j + 1])] ;
					int productAmount = Integer.parseInt(CraftingInput.get(i)[2 * j + 2]) ;
					products.put(product, productAmount) ;
				}
			}
			
			// adding recipe
			recipes.add(new Recipe(ingredients, products)) ;
		}
		
		return recipes ;
	}
	
	public void display(ArrayList<Recipe> recipes, Point MousePos, DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		Size screenSize = Game.getScreen().getSize() ;
		Color[] ColorPalette = Game.ColorPalette ;
		double OverallAngle = DrawPrimitives.OverallAngle ;
		
		Point windowPos = new Point((int)(0.5*screenSize.x), (int)(0.5*screenSize.y)) ;
		Point ingredientsCol = UtilG.Translate(windowPos, -image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		Point productsCol = UtilG.Translate(windowPos, image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
		Color textColor = ColorPalette[1] ;
		/*if (Ingredients != null)
		{
			NumberOfPages = Ingredients.length - 1 ;
		}*/
		
		//window = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, NumberOfPages) ;
		//Font titleFont = new Font(Game.MainFontName, Font.BOLD, 16) ;
		
		DP.DrawImage(image, windowPos, OverallAngle, new Scale(1, 1), new boolean[] {false, false}, "Center", 1) ;
		//DP.DrawText(new Point(windowPos.x - 3 * imageL / 8, windowPos.y - imageH / 5 - sy/4), "BotLeft", OverallAngle, "Ingredientes:", titleFont, ColorPalette[5]) ;
		//DP.DrawText(new Point(windowPos.x + 3 * imageL / 8, windowPos.y - imageH / 5 - sy/4), "TopRight", OverallAngle, "Produtos", titleFont, ColorPalette[5]) ;		
		if (recipes != null)
		{
			int sy = font.getSize() + 1 ;
			int id = window ;
			
			// draw ingredients
			Item[] ingredients = new Item[0];
			ingredients = recipes.get(id).getIngredients().keySet().toArray(ingredients) ;
			for (int i = 0 ; i <= ingredients.length - 1 ; ++i)
			{
				Point textPos = new Point(ingredientsCol.x, ingredientsCol.y + i*sy) ;
				String ingredientName = ingredients[i].getName() ;
				int ingredientAmount = recipes.get(id).getIngredients().get(ingredients[i]) ;
				String text = ingredientAmount + " " + ingredientName ;
				DP.DrawTextUntil(textPos, "TopLeft", OverallAngle, text, font, textColor, 10, MousePos) ;
			}
			
			// draw products
			Item[] products = new Item[0];
			products = recipes.get(id).getProducts().keySet().toArray(products) ;
			for (int i = 0 ; i <= products.length - 1 ; ++i)
			{
				Point textPos = new Point(productsCol.x, productsCol.y + i*sy) ;
				String productsName = products[i].getName() ;
				int productsAmount = recipes.get(id).getIngredients().get(ingredients[i]) ;
				String text = productsAmount + " " + productsName ;
				DP.DrawTextUntil(textPos, "TopRight", OverallAngle, text, font, textColor, 10, MousePos) ;
			}
		}
		Point arrowsPos = UtilG.Translate(windowPos, 0, image.getHeight(null) / 2) ;
		DF.DrawWindowArrows(arrowsPos, image.getWidth(null), window, numberWindows - 1) ;
	}
}
