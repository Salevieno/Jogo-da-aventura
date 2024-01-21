package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import items.Recipe;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class CraftWindow extends GameWindow
{
	private int amountOfCrafts ;
	private List<Recipe> recipes ;
	private List<Recipe> recipesInWindow ;
	
	private static final Point windowPos = Game.getScreen().pos(0.34, 0.22) ;	
	private static final int RecipesPerWindow = 1 ;
	private static final List<String> messages = Arrays.asList(
			"Items criados!",
			"Vc não possui todos os ingredientes") ;
	public static final Image craftImage = UtilS.loadImage("\\Windows\\" + "Craft.png") ;
// TODO permitir a criação de múltiplos itens
	// TODO corrigir as receitas
	public CraftWindow(List<Recipe> recipes)
	{
		super("Criação", windowPos, craftImage, 1, 1, RecipesPerWindow, recipes.size() / RecipesPerWindow) ;
		amountOfCrafts = 1 ;
		this.recipes = recipes ;
		recipesInWindow = RecipesPerWindow <= recipes.size() ? recipes.subList(window, RecipesPerWindow + window) : recipes ;
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
		
		recipesInWindow = RecipesPerWindow <= recipes.size() ? recipes.subList(window, RecipesPerWindow + window) : recipes ;
	}
	
	public void craft(BagWindow bag)
	{
		Recipe recipe = recipesInWindow.get(item) ;
//		System.out.println("Crafing");
		if (!bag.hasEnough(recipe.getIngredients())) { displayMessage(1) ; return ;}
//		System.out.println("bag hs enough items");
//		System.out.println("ingredients = " + recipe.getIngredients());
//		System.out.println("products = " + recipe.getProducts());
		recipe.getIngredients().forEach( (ingredient, qtd) -> {
			bag.remove(ingredient, qtd) ;
		});
//		System.out.println("removed items from the bag");
		recipe.getProducts().forEach( (product, qtd) -> {
			bag.add(product, qtd) ;
		});
		
		displayMessage(0) ;
//		System.out.println("added new items to the bag");
	}
	
	public void act(BagWindow bag, String action)
	{
		if (action == null) { return ;}
		
		if (action.equals("Enter"))
		{
			for (int i = 0 ; i <= amountOfCrafts - 1; i += 1)
			{
				craft(bag) ;
			}
		}
	}

	public void displayMessage(int i)
	{
		String message = messages.get(i) ;
		Point pos = UtilG.Translate(windowPos, 0, - 30) ;
//		Game.getAnimations().get(12).start(200, new Object[] {pos, message, Game.colorPalette[0]}) ;
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, border + 9) ;
		Color textColor = Game.colorPalette[0] ;
		double angle = Draw.stdAngle ;		
		
		DP.drawImage(image, windowPos, Draw.stdAngle, Scale.unit, Align.topLeft) ;
		
		DP.drawText(titlePos, Align.center, angle, name, titleFont, textColor) ;
		
		Point ingredientsTextPos = UtilG.Translate(windowPos, border + padding + 84, border + 32) ;
		Point ProductsTextPos = UtilG.Translate(windowPos, border + padding + 170 + 84, border + 32) ;
		DP.drawText(ingredientsTextPos, Align.center, angle, "Ingredientes", subTitleFont, textColor) ;
		DP.drawText(ProductsTextPos, Align.center, angle, "Produtos", subTitleFont, textColor) ;

		Point ingredientsPos = UtilG.Translate(windowPos, border + padding + Item.slot.getWidth(null) / 2, border + padding + Item.slot.getHeight(null) / 2 + 44) ;
		Point productsPos = UtilG.Translate(windowPos, border + padding + Item.slot.getWidth(null) / 2 + 169, border + padding + Item.slot.getHeight(null) / 2 + 44) ;
		for (Recipe recipe : recipesInWindow)
		{
			Map<Item, Integer> ingredients = recipe.getIngredients() ;
			Map<Item, Integer> products = recipe.getProducts() ;
			
			ingredients.forEach( (item, qtd) -> {
				Color itemNameColor = textColor ;
				DP.drawImage(Item.slot, ingredientsPos, angle, Scale.unit, Align.center) ;
				DP.drawImage(item.getImage(), ingredientsPos, Draw.stdAngle, Scale.unit, Align.center) ;
				DP.drawText(UtilG.Translate(ingredientsPos, 14, 0), Align.centerLeft, Draw.stdAngle, qtd + " " + item.getName(), stdFont, itemNameColor) ;
				ingredientsPos.y += 23 ;
			}) ;
			
			products.forEach( (item, qtd) -> {
				Color itemNameColor = textColor ;
				DP.drawImage(Item.slot, productsPos, angle, Scale.unit, Align.center) ;
				DP.drawImage(item.getImage(), productsPos, Draw.stdAngle, Scale.unit, Align.center) ;
				DP.drawText(UtilG.Translate(productsPos, 14, 0), Align.centerLeft, Draw.stdAngle, qtd + " " + item.getName(), stdFont, itemNameColor) ;
				productsPos.y += 23 ;
			}) ;		
		}
		
		Draw.windowArrows(UtilG.Translate(windowPos, 0, size.height + 10), size.width, window, numberWindows) ;
		
	}
}
