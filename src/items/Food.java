package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Align;
import graphics2.Draw;
import liveBeings.LiveBeing;
import main.Game;
import main.Path;
import utilities.Util;


public class Food extends Item
{
	private final float lifeHeal ;
	private final float MPHeal ;
	private final int satiationHeal ;
	
	private static final Food[] allFood ;
	
	private static final Image iconFoodBerry ;
	
	static
	{
		List<String[]> input = Util.readcsvFile(Path.CSV + "Item_Food.csv") ;
		allFood = new Food[input.size()] ;
		iconFoodBerry = Game.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconFoodBerry.png") ;
		for (int p = 0; p <= allFood.length - 1; p += 1)
		{
			allFood[p] = new Food(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3],
					Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]),
					Float.parseFloat(input.get(p)[7]), Float.parseFloat(input.get(p)[8]),
					Integer.parseInt(input.get(p)[9]));
		}
	}
	
	public Food(int id, String Name, String Description, int price,
			float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		this.satiationHeal = SatiationHeal ;
	}

	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}	
	public int getSatiationHeal() {return satiationHeal ;}	
	public static Food[] getAll() {return allFood ;}

	public static Image imageFromID(int id) { return iconFoodBerry ;}
	
	public void use(LiveBeing user)
	{
		PersonalAttributes PA = user.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue())) ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue())) ;
		PA.getSatiation().incCurrentValue((int) (satiationHeal / 310.0 * PA.getSatiation().getMaxValue())) ;
	}

	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
	
	@Override
	public String toString()
	{
//		return "Food [lifeHeal=" + lifeHeal + ", MPHeal=" + MPHeal + ", SatiationHeal=" + SatiationHeal + ", id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "Food," + id + "," + name;
	}	
}
