package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Potion extends Item implements UsableItem
{
	private final double lifeHeal ;
	private final double MPHeal ;
	
	private static final Image LIFE_POTION_SMALL_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionSmall.png") ;
	private static final Image LIFE_POTION_MEDIUM_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionMedium.png") ;
	private static final Image LIFE_POTION_LARGE_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionLarge.png") ;
	private static final Image MP_POTION_SMALL_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionSmall.png") ;
	private static final Image MP_POTION_MEDIUM_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionMedium.png") ;
	private static final Image MP_POTION_LARGE_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionLarge.png") ;
	private static final Potion[] ALL = new Potion[60] ;
	
	public Potion(int id, int price, double dropChance, double lifeHeal, double MPHeal)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/PotionText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
	}

	public static Image imageFromID(int id)
	{
		if (id % 6 == 0) { return LIFE_POTION_SMALL_ICON ;}
		if (id % 4 == 0) { return LIFE_POTION_MEDIUM_ICON ;}
		if (id % 2 == 0) { return LIFE_POTION_LARGE_ICON ;}
		if (id % 5 == 0) { return MP_POTION_SMALL_ICON ;}
		if (id % 3 == 0) { return MP_POTION_MEDIUM_ICON ;}
		if (id % 1 == 0) { return MP_POTION_LARGE_ICON ;}
		
		return null ;
	}

	public static Potion[] getAll() {return ALL ;}

    private double calcLifeInc(LiveBeing target)
    {
        double powerMult = 1.0 ;
        if (target instanceof Player)
        {
            powerMult = ((Player) target).getPotionMultiplier() ;
        }
        return lifeHeal * target.getPA().getLife().getMaxValue() * powerMult ;
    }

    private double calcMpInc(LiveBeing target)
    {
        double powerMult = 1.0 ;
        if (target instanceof Player)
        {
            powerMult = ((Player) target).getPotionMultiplier() ;
        }
        return MPHeal * target.getPA().getMp().getMaxValue() * powerMult ;
    }

    public boolean canUse(LiveBeing user)
    {
        if (user == null) { return false ;}
        if (user.getPA() == null) { return false ;}

        int lifeInc = (int) calcLifeInc(user) ;
        int mpInc = (int) calcMpInc(user) ;

        if (0 < lifeInc && !user.canIncreasePersonalAttribute(user.getPA().getLife(), lifeInc) && mpInc <= 0) { return false ;}
        if (lifeInc <= 0 && 0 < mpInc && !user.canIncreasePersonalAttribute(user.getPA().getMp(), mpInc)) { return false ;}
        if (0 < lifeInc && !user.canIncreasePersonalAttribute(user.getPA().getLife(), lifeInc) && 0 < mpInc && !user.canIncreasePersonalAttribute(user.getPA().getMp(), mpInc)) { return false ;}

        return true ;
    }

	public void use(LiveBeing user)
	{
        if (!canUse(user)) { return ;}

        int lifeInc = (int) calcLifeInc(user) ;
        int mpInc = (int) calcMpInc(user) ;

        user.incPersonalAttribute(user.getPA().getLife(), lifeInc, true);
        user.incPersonalAttribute(user.getPA().getMp(), mpInc, true);

		// proTODO - bebidas
	}

	public void displayInfo(Point pos, Align align)
	{
		drawMenu(pos, align, Util.getSize(INFO_MENU_IMAGE)) ;
	}
		
	@Override
	public String toString()
	{
		return "Potion," + id + "," + name;
	}	
}
