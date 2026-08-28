package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import UI.GameButton;
import UI.GameTextButton;
import animations.MessageAnimation;
import graphics.Align;
import graphics.Scale;
import items.Arrow;
import items.GeneralItem;
import items.Item;
import items.Recipe;
import liveBeings.Player;
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
	private List<Recipe> recipesInWindow ;
	
    
	private final List<Recipe> recipes ;
	private final GameButton craftButton ;
    private final Point leftWindowPos ;
    private final Point centerWindowPos ;
    private final Point rightWindowPos ;
    private final List<Point> ingredientsPos ;
    private final List<Point> ingredientsTextPos ;
    private final List<Point> productsPos ;
    private final List<Point> productsTextPos ;

	private static final int RECIPES_PER_WINDOW = 1 ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Craft.png") ;
	private static final List<String> MESSAGES = List.of(   "Items criados!",
                                                            "Vc não possui todos os ingredientes") ;
	public CraftWindow(List<Recipe> recipes)
	{
		super("Craft window", Screen.getMe().pos(0.03, 0.25), IMAGE, 1, 1, RECIPES_PER_WINDOW, recipes.size() / RECIPES_PER_WINDOW) ;
        this.amountOfCrafts = 1 ;
        this.recipesInWindow = RECIPES_PER_WINDOW <= recipes.size() ? recipes.subList(window, RECIPES_PER_WINDOW + window) : recipes ;

		this.leftWindowPos = Util.translate(topLeftPos, 0, 0) ;
        this.centerWindowPos = Util.translate(topLeftPos, 384, 0) ;
        this.rightWindowPos = Util.translate(topLeftPos, 576, 0) ;
        this.ingredientsPos = new ArrayList<>() ;
        this.ingredientsTextPos = new ArrayList<>() ;
        this.productsPos = new ArrayList<>() ;
        this.productsTextPos = new ArrayList<>() ;
        for (int i = 0 ; i <= 10 - 1 ; i += 1)
        {
            this.ingredientsPos.add(Util.translate(leftWindowPos, 32, 32 + 23 * i)) ;
            this.ingredientsTextPos.add(Util.translate(leftWindowPos, 32 + 20, 32 + 23 * i)) ;
            this.productsPos.add(Util.translate(rightWindowPos, 32, 32 + 23 * i)) ;
            this.productsTextPos.add(Util.translate(rightWindowPos, 32 + 20, 32 + 23 * i)) ;
        }
		this.recipes = recipes ;
        Point craftButtonCenter = Util.translate(centerWindowPos, 88, 100) ;
		this.craftButton = new GameTextButton(craftButtonCenter, Align.center, "Fabricar", () -> {craft(playerBag) ;}) ;
		this.craftButton.deactivate() ;
		this.buttons.add(craftButton) ;
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
	
	public void setBag(BagWindow bag) { this.playerBag = bag ;}
	
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
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;

		for (Recipe recipe : recipesInWindow)
		{
			Map<Item, Integer> ingredients = recipe.getIngredients() ;
			Map<Item, Integer> products = recipe.getProducts() ;

            int i = 0;
            for (Map.Entry<Item, Integer> entry : ingredients.entrySet())
            {
                Item item = entry.getKey();
                int qtd = entry.getValue() * amountOfCrafts ;

				Color itemNameColor = playerBag.hasEnough(item, qtd) ? STD_COLOR : Palette.colors[2] ;
				String msg = item.getName() + " (" + playerBag.getAmount(item) + " / " + qtd + ")" ;
                item.displayInSlot(ingredientsPos.get(i), false) ;
				GamePanel.getDP().drawText(ingredientsTextPos.get(i), Align.centerLeft, msg, SUBTITLE_FONT, itemNameColor) ;

                i++;
            }
            
            i = 0;
            for (Map.Entry<Item, Integer> entry : products.entrySet())
            {
                Item item = entry.getKey();
                int qtd = entry.getValue() * amountOfCrafts ;

                item.displayInSlot(productsPos.get(i), false) ;
				GamePanel.getDP().drawText(productsTextPos.get(i), Align.centerLeft, qtd + " " + item.getName(), SUBTITLE_FONT, STD_COLOR) ;

                i++;
            }	
		}
		
		craftButton.setName("Fabricar " + amountOfCrafts) ;
		craftButton.display(true, mousePos) ;
		
		drawNavigationButtons(Util.translate(topLeftPos, 0, size.height + 10), size.width, SUBTITLE_FONT, window, numberWindows, stdOpacity) ;
	}
}
