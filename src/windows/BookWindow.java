package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
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


public class BookWindow extends GameWindow
{
    private final Point ingredientsCol ;
    private final Point productsCol ;
    private final int sy ;
    private final Color textColor ;
    private List<Recipe> recipes = new ArrayList<>() ;
    private Recipe recipeDisplayed ;
    private int qtdIngredients ;
    private int qtdProducts ;
    private List<Point> ingredientsPos ;
    private List<Point> productsPos ;
	private final Point pageTextPos ;

	private static final Font FONT = new Font(Game.getMainFontName(), Font.BOLD, 14) ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Book.png") ;

	public BookWindow()
	{
		super("Livro", Screen.getMe().getCenter(), IMAGE, 0, 0, 0, 0) ;
        this.ingredientsCol = Util.translate(topLeftPos, -image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
        this.productsCol = Util.translate(topLeftPos, image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
        this.sy = FONT.getSize() + 1 ;
        this.textColor = Palette.colors[5] ;
        this.pageTextPos = Util.translate(UtilAlignment.getPosAt(topLeftPos, Align.center, Align.bottomLeft, size), size.width - 60, -50) ;
        this.recipeDisplayed = recipes.get(0) ;
        this.qtdIngredients = recipeDisplayed.getIngredients().size() ;
        this.qtdProducts = recipeDisplayed.getProducts().size() ;
        this.ingredientsPos = new ArrayList<>() ;
        this.productsPos = new ArrayList<>() ;
	}

    protected void onOpen()
    {
        
    }

    public void act(Player player, Point mousePos)
    {
        
    }

	public void setRecipes(List<Recipe> recipes) { this.recipes = recipes ; numberPages = recipes.size() ;}
	
	public void addRecipes(List<Recipe> newRecipes) { recipes.addAll(newRecipes) ; numberPages = recipes.size() ;}

	public void navigate(String action)
	{
		if (action.equals(stdPageUp))
        {
            pageUp() ;
            updateRecipeOnDisplay() ;
        }
		if (action.equals(stdPageDown))
        {
            pageDown() ;
            updateRecipeOnDisplay() ;
        }
		if (action.equals(stdMenuUp))
        {
            menuUp() ;
        }
		if (action.equals(stdMenuDown))
        {
            menuDown() ;
        }
		if (action.equals(stdEnter))
        {
            tabUp() ;
        }
		if (action.equals(stdReturn))
        {
            tabDown() ;
        }
	}

    private void updateRecipeOnDisplay()
    {
        recipeDisplayed = recipes.get(page) ;
        qtdIngredients = recipeDisplayed.getIngredients().size() ;
        qtdProducts = recipeDisplayed.getProducts().size() ;
        ingredientsPos = new ArrayList<>() ;
        productsPos = new ArrayList<>() ;
        for (int i = 0 ; i <= qtdIngredients - 1 ; i += 1)
        {
            ingredientsPos.add(new Point(ingredientsCol.x, ingredientsCol.y + i * sy)) ;
        }
        for (int i = 0 ; i <= qtdProducts - 1 ; i += 1)
        {
            productsPos.add(new Point(productsCol.x, productsCol.y + i * sy)) ;
        }
    }
	
	public void displayRecipes(Point mousePos)
	{
		if (recipes == null) { return ;}
		if (recipes.isEmpty()) { return ;}

        int i = 0;
        for (Map.Entry<Item, Integer> entry : recipeDisplayed.getIngredients().entrySet())
        {
            String text = entry.getValue() + " " + entry.getKey().getName();
            Draw.textUntil(ingredientsPos.get(i), Align.topLeft, text, FONT, textColor, 10, mousePos) ;
        }

        i = 0;
        for (Map.Entry<Item, Integer> entry : recipeDisplayed.getProducts().entrySet())
        {
            String text = entry.getValue() + " " + entry.getKey().getName();
            Draw.textUntil(productsPos.get(i), Align.topLeft, text, FONT, textColor, 10, mousePos) ;
        }
	}

	private void displayPageNumber()
	{
		if (numberPages == 0) { return ;}

		String pageText = (page + 1) + " / " + numberPages ;
		GamePanel.getDP().drawText(pageTextPos, Align.centerRight, DrawPrimitives.stdAngle, pageText, FONT, Palette.colors[0]) ;
	}
	
	public void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.center) ;
		displayRecipes(mousePos) ;
		displayPageNumber() ;
		
		drawNavigationButtons(UtilAlignment.getPosAt(topLeftPos, Align.center, Align.bottomLeft, size), image.getWidth(null), FONT, page, numberPages, stdOpacity) ;
	}

    protected void onClose() { }
}
