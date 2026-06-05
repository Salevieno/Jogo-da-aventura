package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import items.Item;
import items.Recipe;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class BookWindow extends GameWindow
{
    private List<Recipe> recipes = new ArrayList<>() ;

	private static final Font FONT = new Font(Game.getMainFontName(), Font.BOLD, 14) ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Book.png") ;

	public BookWindow()
	{
		super("Livro", Screen.getMe().getCenter(), IMAGE, 0, 0, 0, 0) ;
	}

	public void setRecipes(List<Recipe> recipes) { this.recipes = recipes ; numberWindows = recipes.size() ;}
	
	public void addRecipes(List<Recipe> newRecipes) { recipes.addAll(newRecipes) ; numberWindows = recipes.size() ;}

	public void navigate(String action)
	{
		stdNavigation(action) ;
	}
	
	public void displayRecipes(Point mousePos)
	{
		if (recipes == null) { return ;}
		if (recipes.isEmpty()) { return ;}

		Point ingredientsCol = Util.translate(topLeftPos, -image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		Point productsCol = Util.translate(topLeftPos, image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		
		int sy = FONT.getSize() + 1 ;
		int id = window ;
		Color textColor = Palette.colors[5] ;
		
		// draw ingredients
		Item[] ingredients = new Item[0];
		ingredients = recipes.get(id).getIngredients().keySet().toArray(ingredients) ;
		for (int i = 0 ; i <= ingredients.length - 1 ; ++i)
		{
			Point textPos = new Point(ingredientsCol.x, ingredientsCol.y + i*sy) ;
			String ingredientName = ingredients[i].getName() ;
			int ingredientAmount = recipes.get(id).getIngredients().get(ingredients[i]) ;
			String text = ingredientAmount + " " + ingredientName ;
			Draw.textUntil(textPos, Align.topLeft, text, FONT, textColor, 10, mousePos) ;
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
			Draw.textUntil(textPos, Align.topRight, text, FONT, textColor, 10, mousePos) ;
		}
	}

	private void displayPageNumber()
	{
		if (numberWindows == 0) { return ;}
		
		Point textPos = Util.translate(UtilAlignment.getPosAt(topLeftPos, Align.center, Align.bottomLeft, size), size.width - 60, -50) ;
		String pageText = (window + 1) + " / " + numberWindows ;
		GamePanel.getDP().drawText(textPos, Align.centerRight, DrawPrimitives.stdAngle, pageText, FONT, Palette.colors[0]) ;
	}
	
	public void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.center) ;
		displayRecipes(mousePos) ;
		displayPageNumber() ;
		
		drawNavigationButtons(UtilAlignment.getPosAt(topLeftPos, Align.center, Align.bottomLeft, size), image.getWidth(null), FONT, window, numberWindows, stdOpacity) ;
	}
}
