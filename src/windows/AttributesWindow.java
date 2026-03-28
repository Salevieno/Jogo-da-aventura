package windows;

import java.awt.Image;
import java.awt.Point;

import main.ImageLoader;
import main.Path;



public abstract class AttributesWindow extends GameWindow
{

	private static final Image lifeIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "LifeIcon.png") ;
	private static final Image mpIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MPIcon.png") ;
	private static final Image phyAtkIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "PhyAtkIcon.png") ;
	private static final Image phyDefIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "PhyDefIcon.png") ;
	private static final Image magAtkIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MagAtkIcon.png") ;
	private static final Image magDefIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MagDefIcon.png") ;
	private static final Image dexIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "DexIcon.png") ;
	private static final Image agiIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "AgiIcon.png") ;
	protected static final Image critIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "CritIcon.png") ;
	protected static final Image herbIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "HerbIcon.png") ;
	protected static final Image woodIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "WoodIcon.png") ;
	protected static final Image metalIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "attIcons\\" + "MetalIcon.png") ;
	
	protected static final Image[] attIcons = new Image[] {phyAtkIcon, magAtkIcon, phyDefIcon, magDefIcon, dexIcon, agiIcon} ;
	protected static final Image[] collectIcons = new Image[] {herbIcon, woodIcon, metalIcon} ;
	
	public AttributesWindow(Image image, int numberTabs)
	{
		super("Atributos", new Point(0, 0), image, 0, numberTabs, 0, 0) ;
	}

	public static Image[] getIcons()
	{
		return new Image[] {lifeIcon, mpIcon, phyAtkIcon, magAtkIcon, phyDefIcon, magDefIcon, dexIcon, agiIcon} ;
	}
	
	@Override
	public void navigate(String action)
	{
	}

	public void display(Point mousePos) { }
}
