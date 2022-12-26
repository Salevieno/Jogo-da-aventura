package items;

import java.util.Map;

public class Recipe
{
	Map<Item, Integer> Ingredients ;
	Map<Item, Integer> Products ;
	
	public Recipe(Map<Item, Integer> Ingredients, Map<Item, Integer> Products)
	{
		this.Ingredients = Ingredients ;
		this.Products = Products ;
	}
	
	public Map<Item, Integer> getIngredients() { return Ingredients ; }
	public Map<Item, Integer> getProducts() { return Products ; }
}
