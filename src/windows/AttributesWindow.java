package windows;

import java.awt.Image;
import java.awt.Point;

import main.ImageLoader;
import main.Path;



public abstract class AttributesWindow extends GameWindow
{
	private static final Image LIFE_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "LifeIcon.png") ;
	private static final Image MP_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MPIcon.png") ;
	private static final Image PHY_ATK_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "PhyAtkIcon.png") ;
	private static final Image PHY_DEF_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "PhyDefIcon.png") ;
	private static final Image MAG_ATK_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MagAtkIcon.png") ;
	private static final Image MAG_DEF_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MagDefIcon.png") ;
	private static final Image DEX_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "DexIcon.png") ;
	private static final Image AGI_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "AgiIcon.png") ;
	protected static final Image CRIT_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "CritIcon.png") ;
	protected static final Image HERB_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "HerbIcon.png") ;
	protected static final Image WOOD_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "WoodIcon.png") ;
	protected static final Image METAL_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MetalIcon.png") ;
	
	protected static final Image[] ATT_ICONS = new Image[] {PHY_ATK_ICON, MAG_ATK_ICON, PHY_DEF_ICON, MAG_DEF_ICON, DEX_ICON, AGI_ICON} ;
	protected static final Image[] COLLECT_ICONS = new Image[] {HERB_ICON, WOOD_ICON, METAL_ICON} ;
	
	public AttributesWindow(Image image, int numberTabs)
	{
		super("Atributos", new Point(0, 0), image, 0, numberTabs, 0, 0) ;
	}

	public static Image[] getIcons()
	{
		return new Image[] {LIFE_ICON, MP_ICON, PHY_ATK_ICON, MAG_ATK_ICON, PHY_DEF_ICON, MAG_DEF_ICON, DEX_ICON, AGI_ICON} ;
	}
	
	@Override
	public void navigate(String action)
	{
	}

	public void display(Point mousePos) { }
}
