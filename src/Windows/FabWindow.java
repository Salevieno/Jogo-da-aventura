package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import GameComponents.Items;
import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import Items.Item;
import Items.Recipe;
import LiveBeings.Player;
import Main.Game;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class FabWindow extends Window
{
    public static Image image ;
    private static ArrayList<Recipe> recipes ;
    
	public FabWindow()
	{
		super(null, 0, 0, 0, 0) ;
		image = new ImageIcon(Game.ImagesPath + "Book.png").getImage() ;
	}

	public void LoadCraftingRecipes()
	{
		ArrayList<String[]> CraftingInput = UtilG.ReadcsvFile(Game.CSVPath + "Craft.csv") ;
			
		recipes = new ArrayList<>() ;
		for (int i = 0 ; i <= CraftingInput.size() - 1 ; i += 1)
		{			
			// adding ingredients
			Map<Integer, Integer> ingredients = new HashMap<>() ;
			for (int j = 0 ; j <= 10 - 1 ; j += 1)
			{
				if (0 < Integer.parseInt(CraftingInput.get(i)[2 * j]))
				{
					int ingredientID = Integer.parseInt(CraftingInput.get(i)[2 * j + 1]) ;
					int ingredientAmount = Integer.parseInt(CraftingInput.get(i)[2 * j]) ;
					ingredients.put(ingredientID, ingredientAmount) ;
				}
			}
			
			// adding products
			Map<Integer, Integer> products = new HashMap<>() ;
			for (int j = 20 ; j <= 30 - 1 ; j += 1)
			{
				if (0 < Integer.parseInt(CraftingInput.get(i)[2 * j]))
				{
					int productID = Integer.parseInt(CraftingInput.get(i)[2 * j + 1]) ;
					int productAmount = Integer.parseInt(CraftingInput.get(i)[2 * j]) ;
					products.put(productID, productAmount) ;
				}
			}
			
			// adding recipe
			recipes.add(ingredients, products) ;
			System.out.println(recipes.get(i));
		}
	}
	
	public void display(Point MousePos, DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		Size screenSize = Game.getScreen().getSize() ;
		Color[] ColorPalette = Game.ColorPalette ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		int NumberOfPages = 0 ;
		/*if (Ingredients != null)
		{
			NumberOfPages = Ingredients.length - 1 ;
		}*/
		//window = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, NumberOfPages) ;
		Point Pos = new Point((int)(0.5*screenSize.x), (int)(0.5*screenSize.y)) ;
		Font Titlefont = new Font("SansSerif", Font.BOLD, 16) ;
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		int imageL = image.getWidth(null), imageH = image.getHeight(null);
		int sy = imageH / 15 ;
		int IngredientsCont = 0, ProductsCont = 0 ;
		int MaxTextL = 10 ;
		DP.DrawImage(image, Pos, OverallAngle, new Scale(1, 1), new boolean[] {false, false}, "Center", 1) ;
		DP.DrawText(new Point(Pos.x - 3 * imageL / 8, Pos.y - imageH / 5 - sy/4), "BotLeft", OverallAngle, "Ingredients:", Titlefont, ColorPalette[5]) ;
		DP.DrawText(new Point(Pos.x + 3 * imageL / 8, Pos.y - imageH / 5 - sy/4), "TopRight", OverallAngle, "Products:", Titlefont, ColorPalette[5]) ;		
		if (recipes != null)
		{
			/*for (int j = 0 ; j <= Ingredients[window].length - 1 ; ++j)
			{
				if (-1 < Ingredients[window][j])
				{
					if (Utg.MouseIsInside(MousePos, new int[] {Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2}, MaxTextL*font.getSize()/2, Utg.TextH(font.getSize())))
					{
						DP.DrawText(new Point(Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2}, "BotLeft", OverallAngle, items[Ingredients[window][j]].getName(), font, ColorPalette[5]) ;
					}
					else
					{
						//DP.DrawTextUntil(new Point(Pos.x - 3 * imageL / 8, Pos.y - imageH / 5 + IngredientsCont*sy + sy/2), "BotLeft", OverallAngle, items[Ingredients[window][j]].getName(), font, ColorPalette[5], MaxTextL, MousePos) ;
					//}
					IngredientsCont += 1 ;
				}
			}*/
		}
		/*if (Products != null)
		{
			for (int j = 0 ; j <= Products[window].length - 1 ; ++j)
			{
				if (-1 < Products[window][j])
				{
					if (Utg.MouseIsInside(MousePos, new int[] {Pos.x + 3*L/8 - MaxTextL*font.getSize()/2, Pos.y - H/5 + ProductsCont*sy + sy/2}, MaxTextL*font.getSize()/2, Utg.TextH(font.getSize())))
					{
						DP.DrawText(new Point(Pos.x + 3*L/8, Pos.y - H/5 + ProductsCont*sy + sy/2}, "TopRight", OverallAngle, items[Products[window][j]].getName(), font, ColorPalette[5]) ;
					}
					else
					{
						//DP.DrawTextUntil(new Point(Pos.x + 3 * imageL / 8, Pos.y - imageH / 5 + ProductsCont*sy + sy/2), "TopRight", OverallAngle, items[Products[window][j]].getName(), font, ColorPalette[5], MaxTextL, MousePos) ;
					//}
					ProductsCont += 1 ;
				}
			}
		}*/
		/*if (Ingredients != null)
		{
			DF.DrawWindowArrows(new Point(Pos.x, Pos.y + 15 * imageH / 32), imageL, 0, window, Ingredients.length - 1) ;
		}*/
	}
}
