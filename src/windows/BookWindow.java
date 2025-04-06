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
import graphics2.Draw;
import items.Item;
import items.Recipe;
import main.Game;
import main.GamePanel;
import utilities.Util;
import utilities.UtilS;

public class BookWindow extends GameWindow
{
    private List<Recipe> recipes = new ArrayList<>() ;

	private static final Point windowPos = Game.getScreen().getCenter() ;
	private static final Image windowImage = UtilS.loadImage("\\Windows\\" + "Book.png") ;
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
	
	public BookWindow()
	{
		super("Livro", windowPos, windowImage, 0, 0, 0, 0) ;
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

		Point ingredientsCol = Util.Translate(windowPos, -image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		Point productsCol = Util.Translate(windowPos, image.getWidth(null) / 3, -image.getHeight(null) / 3) ;
		
		int sy = font.getSize() + 1 ;
		int id = window ;
		Color textColor = Game.palette[5] ;
		
		// draw ingredients
		Item[] ingredients = new Item[0];
		ingredients = recipes.get(id).getIngredients().keySet().toArray(ingredients) ;
		for (int i = 0 ; i <= ingredients.length - 1 ; ++i)
		{
			Point textPos = new Point(ingredientsCol.x, ingredientsCol.y + i*sy) ;
			String ingredientName = ingredients[i].getName() ;
			int ingredientAmount = recipes.get(id).getIngredients().get(ingredients[i]) ;
			String text = ingredientAmount + " " + ingredientName ;
			Draw.textUntil(textPos, Align.topLeft, Draw.stdAngle, text, font, textColor, 10, mousePos) ;
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
			Draw.textUntil(textPos, Align.topRight, Draw.stdAngle, text, font, textColor, 10, mousePos) ;
		}
	}

	private void displayPageNumber()
	{
		if (numberWindows == 0) { return ;}
		
		Point textPos = Util.Translate(Util.getPosAt(windowPos, Align.center, Align.bottomLeft, size), size.width - 60, -50) ;
		String pageText = (window + 1) + " / " + numberWindows ;
		GamePanel.DP.drawText(textPos, Align.centerRight, DrawPrimitives.stdAngle, pageText, font, Game.palette[0]) ;
	}
	
	public void display(Point mousePos)
	{
		GamePanel.DP.drawImage(image, windowPos, Draw.stdAngle, Scale.unit, Align.center) ;
		displayRecipes(mousePos) ;
		displayPageNumber() ;
		
		Draw.windowArrows(Util.getPosAt(windowPos, Align.center, Align.bottomLeft, size), image.getWidth(null), window, numberWindows, stdOpacity) ;
	}
}
