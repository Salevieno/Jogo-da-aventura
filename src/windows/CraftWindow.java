package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;
import java.util.Map;

import graphics.DrawingOnPanel;
import items.Item;
import items.Recipe;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class CraftWindow extends GameWindow
{
	private static int NumberRecipesPerWindow = 1 ;
	private List<Recipe> recipesForCrafting ;
	private List<Recipe> recipesInWindow ;

	public CraftWindow(List<Recipe> recipes)
	{
		super("Criação", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Craft.gif"), 1, 1, NumberRecipesPerWindow, recipes.size() / NumberRecipesPerWindow + 1) ;
		this.recipesForCrafting = recipes ;
		recipesInWindow = NumberRecipesPerWindow <= recipesForCrafting.size() ? recipesForCrafting.subList(window, NumberRecipesPerWindow + window) : recipesForCrafting ;
	}
	
	public void navigate(String action)
	{
		if (action.equals(Player.ActionKeys[3]))
		{
			windowUp() ;
			itemUp() ;
		}
		if (action.equals(Player.ActionKeys[1]))
		{
			windowDown() ;
			itemDown() ;
		}
		
		recipesInWindow = NumberRecipesPerWindow <= recipesForCrafting.size() ? recipesForCrafting.subList(window, NumberRecipesPerWindow + window) : recipesForCrafting ;
	}
	
	public void craft(BagWindow bag)
	{
		Recipe recipe = recipesInWindow.get(item) ;
		
		if (!bag.hasEnough(recipe.getIngredients())) {return ;}
		
		recipe.getIngredients().forEach( (ingredient, qtd) -> {
			bag.Remove(ingredient, qtd) ;
		});
		
		recipe.getProducts().forEach( (product, qtd) -> {
			bag.Add(product, qtd) ;
		});
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.34*Game.getScreen().getSize().width), (int)(0.22*Game.getScreen().getSize().height)) ;
		Point ingredientsPos = UtilG.Translate(windowPos, 20, 110) ;
		Point productsPos = UtilG.Translate(windowPos, 180, 110) ;
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 18) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font titleFont = new Font(Game.MainFontName, Font.BOLD, 16) ;
		Font subTitleFont = new Font(Game.MainFontName, Font.BOLD, 14) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		
		DP.DrawImage(image, windowPos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.topLeft) ;
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.ColorPalette[18]) ;
		
		DP.DrawText(UtilG.Translate(windowPos, 80, 70), Align.center, angle, "Ingredientes", subTitleFont, Game.ColorPalette[9]) ;
		DP.DrawLine(UtilG.Translate(windowPos, 20, 90), UtilG.Translate(windowPos, 140, 90), 1, Game.ColorPalette[9]) ;
		DP.DrawText(UtilG.Translate(windowPos, 220, 70), Align.center, angle, "Produtos", subTitleFont, Game.ColorPalette[9]) ;
		DP.DrawLine(UtilG.Translate(windowPos, 180, 90), UtilG.Translate(windowPos, 260, 90), 1, Game.ColorPalette[9]) ;
		
		for (Recipe recipe : recipesInWindow)
		{
			Map<Item, Integer> ingredients = recipe.getIngredients() ;
			Map<Item, Integer> products = recipe.getProducts() ;
			
			ingredients.forEach( (item, qtd) -> {
				Color itemNameColor = Game.ColorPalette[9] ;
				DP.DrawImage(Item.slot, ingredientsPos, angle, new Scale(1, 1), Align.center) ;
				DP.DrawImage(item.getImage(), ingredientsPos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.center) ;
				DP.DrawText(UtilG.Translate(ingredientsPos, 20, 0), Align.centerLeft, DrawingOnPanel.stdAngle, qtd + " " + item.getName(), font, itemNameColor) ;
				ingredientsPos.y += 30 ;
			}) ;
			
			products.forEach( (item, qtd) -> {
				Color itemNameColor = Game.ColorPalette[9] ;
				DP.DrawImage(Item.slot, productsPos, angle, new Scale(1, 1), Align.center) ;
				DP.DrawImage(item.getImage(), productsPos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.center) ;
				DP.DrawText(UtilG.Translate(productsPos, 20, 0), Align.centerLeft, DrawingOnPanel.stdAngle, qtd + " " + item.getName(), font, itemNameColor) ;
				productsPos.y += 30 ;
			}) ;		
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, size.width / 2, size.height + 10), size.width, window, numberWindows) ;
		
	}
}
