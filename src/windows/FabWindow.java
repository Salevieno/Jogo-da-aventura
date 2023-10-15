package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

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
    private List<Recipe> recipes ;
    
	private static final Image windowImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Book.png") ;
	private static final Point windowPos = Game.getScreen().pos(0.5, 0.5) ;
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
	
	public FabWindow()
	{
		super("Livro", windowImage, 0, 0, 0, 3) ;
		//LoadCraftingRecipes() ;
		//numberWindows = recipes.size() ;
	}
	
	

	public void setRecipes(List<Recipe> recipes) { this.recipes = recipes ;}



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
	
	public void displayRecipes(Point mousePos, DrawingOnPanel DP)
	{
		if (recipes == null) { return ;}
		if (recipes.isEmpty()) { return ;}
		
		Point ingredientsCol = UtilG.Translate(windowPos, -image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		Point productsCol = UtilG.Translate(windowPos, image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		
		int sy = font.getSize() + 1 ;
		int id = window ;
		Color textColor = Game.colorPalette[1] ;
		double stdAngle = DrawingOnPanel.stdAngle ;
		
		// draw ingredients
		Item[] ingredients = new Item[0];
		ingredients = recipes.get(id).getIngredients().keySet().toArray(ingredients) ;
		for (int i = 0 ; i <= ingredients.length - 1 ; ++i)
		{
			Point textPos = new Point(ingredientsCol.x, ingredientsCol.y + i*sy) ;
			String ingredientName = ingredients[i].getName() ;
			int ingredientAmount = recipes.get(id).getIngredients().get(ingredients[i]) ;
			String text = ingredientAmount + " " + ingredientName ;
			DP.DrawTextUntil(textPos, Align.topLeft, stdAngle, text, font, textColor, 10, mousePos) ;
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
			DP.DrawTextUntil(textPos, Align.topRight, stdAngle, text, font, textColor, 10, mousePos) ;
		}
	}

	public void display(Point mousePos, DrawingOnPanel DP)
	{
		System.out.println("displaying fab window");
		
		/*if (Ingredients != null)
		{
			NumberOfPages = Ingredients.length - 1 ;
		}*/
		
		//window = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, NumberOfPages) ;
		//Font titleFont = new Font(Game.MainFontName, Font.BOLD, 16) ;
		
		DP.DrawImage(image, windowPos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.center) ;
		//DP.DrawText(new Point(windowPos.x - 3 * imageL / 8, windowPos.y - imageH / 5 - sy/4), "BotLeft", OverallAngle, "Ingredientes:", titleFont, ColorPalette[5]) ;
		//DP.DrawText(new Point(windowPos.x + 3 * imageL / 8, windowPos.y - imageH / 5 - sy/4), "TopRight", OverallAngle, "Produtos", titleFont, ColorPalette[5]) ;		
		displayRecipes(mousePos, DP) ;
		
		Point arrowsPos = UtilG.Translate(windowPos, 0, image.getHeight(null) / 2) ;
		DP.DrawWindowArrows(arrowsPos, image.getWidth(null), window, numberWindows - 1) ;
	}
}
