package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Align;
import liveBeings.LiveBeing;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Food extends Item implements UsableItem
{
	private final double lifeHeal ;
	private final double MPHeal ;
	private final int satiationHeal ;
	
	private static final Image FOOD_BERRY_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconFoodBerry.png") ;
	private static final Food[] ALL = new Food[60];
	
	public Food(int id, int price, double dropChance, double lifeHeal, double MPHeal, int SatiationHeal)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		this.satiationHeal = SatiationHeal ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/FoodText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
	}

	public double getLifeHeal() {return lifeHeal ;}
	public double getMPHeal() {return MPHeal ;}	
	public int getSatiationHeal() {return satiationHeal ;}	
	public static Food[] getAll() {return ALL ;}

	public static Image imageFromID(int id) { return FOOD_BERRY_ICON ;}
	
    public boolean canUse(LiveBeing user)
    {
        if (user == null) { return false ;}
        if (user.getPA() == null) { return false ;}
        return true ;
    }

	public void use(LiveBeing user)
	{
        if (!canUse(user)) { return ;}

		PersonalAttributes PA = user.getPA() ;
        user.incPersonalAttribute(PA.getLife(), (int) (lifeHeal * PA.getLife().getMaxValue()), true) ;
        user.incPersonalAttribute(PA.getMp(), (int) (MPHeal * PA.getMp().getMaxValue()), true) ;
        user.incPersonalAttribute(PA.getSatiation(), (int) (satiationHeal * PA.getSatiation().getMaxValue()), true) ;
	}

	public void displayInfo(Point pos, Align align)
	{
		drawMenu(pos, align, Util.getSize(INFO_MENU_IMAGE)) ;
	}
	
	@Override
	public String toString()
	{
		return "Food " + id + ": " + name;
	}	
}
