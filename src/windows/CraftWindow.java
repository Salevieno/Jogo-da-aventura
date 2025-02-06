package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import components.GameButton;
import graphics.Align;
import graphics.Scale;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import items.Arrow;
import items.GeneralItem;
import items.Item;
import items.Recipe;
import liveBeings.Player;
import main.Game;
import utilities.Util;
import utilities.UtilS;

public class CraftWindow extends GameWindow
{
	private int amountOfCrafts ;
	private BagWindow playerBag ;
	private List<Recipe> recipes ;
	private List<Recipe> recipesInWindow ;
	private GameButton craftButton ;
	
	private static final Point windowPos = Game.getScreen().pos(0.03, 0.25) ;	
	private static final int RecipesPerWindow = 1 ;
	private static final Image buttonImg = UtilS.loadImage("\\Windows\\" + "CraftButton.png") ;
	private static final List<String> messages = Arrays.asList(
																"Items criados!",
																"Vc não possui todos os ingredientes") ;
	public static final Image craftImage = UtilS.loadImage("\\Windows\\" + "Craft.png") ;

	public CraftWindow(List<Recipe> recipes)
	{
		super("", windowPos, craftImage, 1, 1, RecipesPerWindow, recipes.size() / RecipesPerWindow) ;
		amountOfCrafts = 1 ;
		this.recipes = recipes ;
		recipesInWindow = RecipesPerWindow <= recipes.size() ? recipes.subList(window, RecipesPerWindow + window) : recipes ;
		craftButton = new GameButton(Util.Translate(windowPos, 286, 130), Align.center, "Fabricar " + String.valueOf(amountOfCrafts), buttonImg, buttonImg, () -> {craft(playerBag) ;}) ;
	}
	
	public void navigate(String action)
	{
		if (action.equals(stdWindowUp))
		{
			windowUp() ;
			itemUp() ;
		}
		if (action.equals(stdWindowDown))
		{
			windowDown() ;
			itemDown() ;
		}
		if (action.equals(stdMenuUp) || action.equals("MouseWheelUp"))
		{
			amountOfCrafts += 1 ;
		}
		if (action.equals(stdMenuDown) || action.equals("MouseWheelDown"))
		{
			if (amountOfCrafts <= 1) { return ;}
			amountOfCrafts += -1 ;
		}
		
		recipesInWindow = RecipesPerWindow <= recipes.size() ? recipes.subList(window, RecipesPerWindow + window) : recipes ;
	}
	
	public void setBag(BagWindow bag) {this.playerBag = bag ;}
	
	public void craft(BagWindow bag)
	{
		Recipe recipe = recipesInWindow.get(item) ;
		
		if (!bag.hasEnough(recipe.getIngredients())) { displayMessage(1) ; return ;}
		
		recipe.getIngredients().forEach((ingredient, qtd) -> bag.remove(ingredient, qtd)) ;
		recipe.getProducts().forEach((product, qtd) -> bag.add(product, qtd)) ;

		displayMessage(0) ;
	}
	
	private boolean meetsElementalArrowRules(Recipe recipe, Player player)
	{
		if (!recipe.productsContainAny(Arrow.elementalArrows())) { return true ;}
		
		if (player.getJob() != 2) { return false ;}
		int spellLevel = player.getSpells().get(7).getLevel() ;
		List<Item> arrowsLevel0 = List.of(Arrow.getAll()[6], Arrow.getAll()[7]) ;
		List<Item> arrowsLevel1 = List.of(Arrow.getAll()[8], Arrow.getAll()[9]) ;
		List<Item> arrowsLevel2 = List.of(Arrow.getAll()[10], Arrow.getAll()[11]) ;
		List<Item> arrowsLevel3 = List.of(Arrow.getAll()[12], Arrow.getAll()[13]) ;
		List<Item> arrowsLevel4 = List.of(Arrow.getAll()[14]) ;
		if (recipe.productsContainAny(arrowsLevel0) & spellLevel <= 0) { return false ;}
		if (recipe.productsContainAny(arrowsLevel1) & spellLevel <= 1) { return false ;}
		if (recipe.productsContainAny(arrowsLevel2) & spellLevel <= 2) { return false ;}
		if (recipe.productsContainAny(arrowsLevel3) & spellLevel <= 3) { return false ;}
		if (recipe.productsContainAny(arrowsLevel4) & spellLevel <= 4) { return false ;}
		
		return true ;
	}
	
	private boolean meetsPoisonousPotionsRules(Recipe recipe, Player player)
	{
		if (!recipe.productsContain(GeneralItem.getAll()[78])) { return true ;}
		if (player.getJob() != 4) { return false ;}
		if (player.getSpells().get(9).getLevel() <= 0) { return false ;}
		
		return true ;
	}
	
	public void act(BagWindow bag, Point mousePos, String action, Player player)
	{
		if (action == null) { return ;}
		
		if (action.equals("Enter"))
		{
			Recipe recipe = recipesInWindow.get(item) ;

			if (!meetsElementalArrowRules(recipe, player)) { return ;}
			if (!meetsPoisonousPotionsRules(recipe, player)) { return ;}
			
			for (int i = 0 ; i <= amountOfCrafts - 1; i += 1)
			{
				craft(bag) ;
			}
		}
		
		if (craftButton.isActive() & craftButton.isClicked(mousePos, action))
		{
			craftButton.act() ;
		}
	}

	public void displayMessage(int i)
	{
		String message = messages.get(i) ;
		Point pos = Util.Translate(windowPos, 320, -30) ;
		Animation.start(AnimationTypes.message, new Object[] {pos, message, Game.palette[0]}) ;
	}
	
	public void display(Point mousePos)
	{
		
		Point titlePos = Util.Translate(windowPos, size.width / 2, border + 9) ;
		double angle = Draw.stdAngle ;		
		
		Game.DP.drawImage(image, windowPos, Draw.stdAngle, Scale.unit, Align.topLeft, stdOpacity) ;
		
		Game.DP.drawText(titlePos, Align.center, angle, name, titleFont, stdColor) ;
		
		Point ingredientsTextPos = Util.Translate(windowPos, border + padding + 84, border + 10) ;
		Point productsTextPos = Util.Translate(windowPos, border + padding + 362 + 84, border + 10) ;
//		Point amountTextPos = Util.Translate(windowPos, border + padding + 276, border + 30) ;
		Game.DP.drawText(ingredientsTextPos, Align.center, angle, "Ingredientes", subTitleFont, stdColor) ;
		Game.DP.drawText(productsTextPos, Align.center, angle, "Produtos", subTitleFont, stdColor) ;
//		Game.DP.drawText(amountTextPos, Align.center, angle, "Fabricar", subTitleFont, textColor) ;
//		Game.DP.drawText(Util.Translate(amountTextPos, 0, 70), Align.center, angle, String.valueOf(amountOfCrafts), titleFont, textColor) ;

		Point ingredientsPos = Util.Translate(windowPos, border + padding + Item.slot.getWidth(null) / 2, border + padding + Item.slot.getHeight(null) / 2 + 30) ;
		Point productsPos = Util.Translate(windowPos, border + padding + Item.slot.getWidth(null) / 2 + 362, border + padding + Item.slot.getHeight(null) / 2 + 30) ;
		for (Recipe recipe : recipesInWindow)
		{
			Map<Item, Integer> ingredients = recipe.getIngredients() ;
			Map<Item, Integer> products = recipe.getProducts() ;
			
			ingredients.forEach((item, qtd) -> {
				Color itemNameColor = playerBag.hasEnough(item, qtd * amountOfCrafts) ? stdColor : Game.palette[2] ;
				String msg = qtd * amountOfCrafts + " " + item.getName() + " (" + playerBag.getAmount(item) + ")" ;
				Game.DP.drawImage(Item.slot, ingredientsPos, angle, Scale.unit, Align.center) ;
				Game.DP.drawImage(item.getImage(), ingredientsPos, Draw.stdAngle, Scale.unit, Align.center) ;
				Game.DP.drawText(Util.Translate(ingredientsPos, 14, 0), Align.centerLeft, Draw.stdAngle, msg, stdFont, itemNameColor) ;
				ingredientsPos.y += 23 ;
			}) ;
			
			products.forEach((item, qtd) -> {
				Color itemNameColor = stdColor ;
				Game.DP.drawImage(Item.slot, productsPos, angle, Scale.unit, Align.center) ;
				Game.DP.drawImage(item.getImage(), productsPos, Draw.stdAngle, Scale.unit, Align.center) ;
				Game.DP.drawText(Util.Translate(productsPos, 14, 0), Align.centerLeft, Draw.stdAngle, qtd * amountOfCrafts + " " + item.getName(), stdFont, itemNameColor) ;
				productsPos.y += 23 ;
			}) ;		
		}
		
		craftButton.setName("Fabricar " + amountOfCrafts) ;
		craftButton.display(0.0, true, mousePos) ;
		
		Draw.windowArrows(Util.Translate(windowPos, 0, size.height + 10), size.width, window, numberWindows, stdOpacity) ;
		
	}
}
