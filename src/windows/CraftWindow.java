package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import UI.GameButton;
import UI.GameTextButton;
import animations.MessageAnimation;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import items.Arrow;
import items.GeneralItem;
import items.Item;
import items.Recipe;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class CraftWindow extends GameWindow
{
	private int amountOfCrafts ;
	private BagWindow playerBag ;
	private List<Recipe> recipes ;
	private List<Recipe> recipesInWindow ;
	private GameButton craftButton ;
	
	private static final int RECIPES_PER_WINDOW = 1 ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Craft.png") ;
	private static final List<String> MESSAGES = Arrays.asList(
																"Items criados!",
																"Vc não possui todos os ingredientes") ;
	public CraftWindow(List<Recipe> recipes)
	{
		super("", Screen.getMe().pos(0.03, 0.25), IMAGE, 1, 1, RECIPES_PER_WINDOW, recipes.size() / RECIPES_PER_WINDOW) ;
		this.amountOfCrafts = 1 ;
		this.recipes = recipes ;
		this.recipesInWindow = RECIPES_PER_WINDOW <= recipes.size() ? recipes.subList(window, RECIPES_PER_WINDOW + window) : recipes ;
		this.craftButton = new GameTextButton(Util.translate(topLeftPos, 286, 130), Align.center, "Fabricar " + String.valueOf(amountOfCrafts), () -> {setBag(Game.getPlayer().getBag()) ; craft(playerBag) ;}) ;
		this.craftButton.deactivate() ;
		addButton(craftButton) ;
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
		
		recipesInWindow = RECIPES_PER_WINDOW <= recipes.size() ? recipes.subList(window, RECIPES_PER_WINDOW + window) : recipes ;
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
		String message = MESSAGES.get(i) ;
		Point pos = Util.translate(topLeftPos, 320, -30) ;
		MessageAnimation.start(pos, message, Palette.colors[0]) ;
	}
	
	public void display(Point mousePos)
	{
		
		Point titlePos = Util.translate(topLeftPos, size.width / 2, BORDER + 9) ;
		double angle = Draw.stdAngle ;		
		
		GamePanel.getDP().drawImage(image, topLeftPos, Draw.stdAngle, Scale.unit, Align.topLeft, stdOpacity) ;
		
		GamePanel.getDP().drawText(titlePos, Align.center, angle, name, TITLE_FONT, STD_COLOR) ;
		
		Point ingredientsTextPos = Util.translate(topLeftPos, BORDER + PADDING + 84, BORDER + 10) ;
		Point productsTextPos = Util.translate(topLeftPos, BORDER + PADDING + 362 + 84, BORDER + 10) ;
//		Point amountTextPos = Util.translate(windowPos, border + padding + 276, border + 30) ;
		GamePanel.getDP().drawText(ingredientsTextPos, Align.center, angle, "Ingredientes", SUBTITLE_FONT, STD_COLOR) ;
		GamePanel.getDP().drawText(productsTextPos, Align.center, angle, "Produtos", SUBTITLE_FONT, STD_COLOR) ;
//		GamePanel.getDP().drawText(amountTextPos, Align.center, angle, "Fabricar", subTitleFont, textColor) ;
//		GamePanel.getDP().drawText(Util.translate(amountTextPos, 0, 70), Align.center, angle, String.valueOf(amountOfCrafts), titleFont, textColor) ;

		Point ingredientsPos = Util.translate(topLeftPos, BORDER + PADDING + Item.getSlotImage().getWidth(null) / 2, BORDER + PADDING + Item.getSlotImage().getHeight(null) / 2 + 30) ;
		Point productsPos = Util.translate(topLeftPos, BORDER + PADDING + Item.getSlotImage().getWidth(null) / 2 + 362, BORDER + PADDING + Item.getSlotImage().getHeight(null) / 2 + 30) ;
		for (Recipe recipe : recipesInWindow)
		{
			Map<Item, Integer> ingredients = recipe.getIngredients() ;
			Map<Item, Integer> products = recipe.getProducts() ;
			
			ingredients.forEach((item, qtd) -> {
				Color itemNameColor = playerBag.hasEnough(item, qtd * amountOfCrafts) ? STD_COLOR : Palette.colors[2] ;
				String msg = qtd * amountOfCrafts + " " + item.getName() + " (" + playerBag.getAmount(item) + ")" ;
				GamePanel.getDP().drawImage(Item.getSlotImage(), ingredientsPos, angle, Scale.unit, Align.center) ;
				GamePanel.getDP().drawImage(item.getImage(), ingredientsPos, Draw.stdAngle, Scale.unit, Align.center) ;
				GamePanel.getDP().drawText(Util.translate(ingredientsPos, 14, 0), Align.centerLeft, Draw.stdAngle, msg, STD_FONT, itemNameColor) ;
				ingredientsPos.y += 23 ;
			}) ;
			
			products.forEach((item, qtd) -> {
				Color itemNameColor = STD_COLOR ;
				GamePanel.getDP().drawImage(Item.getSlotImage(), productsPos, angle, Scale.unit, Align.center) ;
				GamePanel.getDP().drawImage(item.getImage(), productsPos, Draw.stdAngle, Scale.unit, Align.center) ;
				GamePanel.getDP().drawText(Util.translate(productsPos, 14, 0), Align.centerLeft, Draw.stdAngle, qtd * amountOfCrafts + " " + item.getName(), STD_FONT, itemNameColor) ;
				productsPos.y += 23 ;
			}) ;		
		}
		
		craftButton.setName("Fabricar " + amountOfCrafts) ;
		craftButton.display(0.0, true, mousePos) ;
		
		Draw.windowArrows(Util.translate(topLeftPos, 0, size.height + 10), size.width, SUBTITLE_FONT, window, numberWindows, stdOpacity) ;
		
	}
}
