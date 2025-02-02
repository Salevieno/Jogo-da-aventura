package items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import main.Game;
import utilities.Util;

public class Recipe
{
	Map<Item, Integer> Ingredients ;
	Map<Item, Integer> Products ;
	
	public static final List<Recipe> all ;
	
	static
	{
		all = new ArrayList<>() ;
	}
	
	public Recipe(Map<Item, Integer> Ingredients, Map<Item, Integer> Products)
	{
		this.Ingredients = Ingredients ;
		this.Products = Products ;
		all.add(this) ;
	}
	
	public static void load(List<Item> allItems)
	{
		JSONArray input = Util.readJsonArray(Game.JSONPath + "craftRecipes.json") ;

		for (int i = 0 ; i <= input.size() - 1 ; i += 1)
		{
			JSONObject recipe = (JSONObject) input.get(i) ;

			JSONArray listIngredients = (JSONArray) recipe.get("IngredientIDs") ;
			JSONArray listIngredientAmounts = (JSONArray) recipe.get("IngredientAmounts") ;
			Map<Item, Integer> ingredients = new HashMap<>() ;
			for (int ing = 0 ; ing <= listIngredients.size() - 1 ; ing += 1)
			{
				ingredients.put(allItems.get((int) (long) listIngredients.get(ing)),
						(int) (long) listIngredientAmounts.get(ing)) ;
			}

			JSONArray listProducts = (JSONArray) recipe.get("ProductIDs") ;
			JSONArray listProductAmounts = (JSONArray) recipe.get("ProductAmounts") ;
			Map<Item, Integer> products = new HashMap<>() ;
			for (int prod = 0 ; prod <= listProducts.size() - 1 ; prod += 1)
			{
				products.put(allItems.get((int) (long) listProducts.get(prod)), (int) (long) listProductAmounts.get(prod)) ;
			}
//			String a = "" ;
//			for (Object aa : listProducts)
//			{
//				a += aa.toString() + ",";
//			}
//			a = a.substring(0, a.length() - 1) ;
//			String b = "" ;
//			for (Object bb : products.values())
//			{
//				b += bb.toString() + ",";
//			}
//			b = b.substring(0, b.length() - 1) ;
//			System.out.println("{\"ProductIDs\":[" + (int) (long) listIngredients.get(1) + "]," + 
//								"\"ProductAmounts\":[" + (int) (long) listIngredientAmounts.get(1) + "]," + 
//								"\"IngredientIDs\":[" + ((int) (long) listIngredients.get(0) + "," + a) + "]," + 
//								"\"IngredientAmounts\":[" + b + "]},");
			new Recipe(ingredients, products) ;
		}
	}
	
	public Map<Item, Integer> getIngredients() { return Ingredients ; }
	public Map<Item, Integer> getProducts() { return Products ; }
	
	public boolean ingredientsContain(Item item)
	{
		return Ingredients.keySet().contains(item) ;
	}
	
	public boolean ingredientsContain(List<Item> items)
	{
		for (Item item : items)
		{
			if (ingredientsContain(item)) { continue ;}
			
			return false ;
		}
		
		return true;
	}
	
	public boolean ingredientsContainAny(List<Item> items)
	{
		for (Item item : items)
		{
			if (ingredientsContain(item)) { return true ;}
		}
		
		return false;
	}
	
	public boolean productsContain(Item item)
	{
		return Products.keySet().contains(item) ;
	}
	
	public boolean productsContain(List<Item> items)
	{
		for (Item item : items)
		{
			if (productsContain(item)) { continue ;}
			
			return false ;
		}
		
		return true;
	}
	
	public boolean productsContainAny(List<Item> items)
	{
		for (Item item : items)
		{
			if (productsContain(item)) { return true ;}
		}
		
		return false;
	}

	@Override
	public String toString() {
//		return "Recipe [Ingredients=" + Ingredients + ", Products=" + Products + "]";
		
		String msg = "" ;
		for (Item ing : Ingredients.keySet())
		{
			msg += Ingredients.get(ing) + " " + ing.getName() + " + " ;
		}
		msg += " = " ;
		for (Item prod : Products.keySet())
		{
			msg += Products.get(prod) + " " + prod.getName()+ " + " ;
		}
		return msg ;
	}
}
