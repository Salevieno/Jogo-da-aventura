package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import graphics.DrawingOnPanel;
import items.Item;
import items.Recipe;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class FabWindow extends GameWindow
{
    public static Image image ;
    //private static ArrayList<Recipe> recipes ;
    
	public FabWindow()
	{
		super(null, 0, 0, 0, 3) ;
		image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Book.png") ;
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

	public void display(ArrayList<Recipe> recipes, Point MousePos, DrawingOnPanel DP)
	{
		Dimension screenSize = Game.getScreen().getSize() ;
		Color[] ColorPalette = Game.ColorPalette ;
		double OverallAngle = DrawingOnPanel.stdAngle ;
		
		Point windowPos = new Point((int)(0.5*screenSize.width), (int)(0.5*screenSize.height)) ;
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
		
		DP.DrawImage(image, windowPos, OverallAngle, new Scale(1, 1), Align.center) ;
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
				DP.DrawTextUntil(textPos, Align.topLeft, OverallAngle, text, font, textColor, 10, MousePos) ;
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
				DP.DrawTextUntil(textPos, Align.topRight, OverallAngle, text, font, textColor, 10, MousePos) ;
			}
		}
		Point arrowsPos = UtilG.Translate(windowPos, 0, image.getHeight(null) / 2) ;
		DP.DrawWindowArrows(arrowsPos, image.getWidth(null), window, numberWindows - 1) ;
	}
}
