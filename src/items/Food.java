package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Align;
import graphics2.Draw;
import liveBeings.LiveBeing;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Food extends Item
{
	private final double lifeHeal ;
	private final double MPHeal ;
	private final int satiationHeal ;
	
	private static final Image iconFoodBerry = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconFoodBerry.png") ;
	private static final Food[] all = new Food[60];
	
	public Food(int id, int price, double dropChance, double lifeHeal, double MPHeal, int SatiationHeal)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		this.satiationHeal = SatiationHeal ;
		all[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/FoodText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			all[id].setName(line[1]) ;
			all[id].setDescription(line[2]) ;
		}
	}

	public double getLifeHeal() {return lifeHeal ;}
	public double getMPHeal() {return MPHeal ;}	
	public int getSatiationHeal() {return satiationHeal ;}	
	public static Food[] getAll() {return all ;}

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
		return "Food " + id + ": " + name;
	}	
}
