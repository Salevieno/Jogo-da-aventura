package windows;

import java.awt.Image;
import java.awt.Point;

import graphics.DrawPrimitives;
import utilities.UtilS;

public abstract class AttributesWindow extends GameWindow
{

	public static final Image lifeIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "LifeIcon.png") ;
	public static final Image mpIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "MPIcon.png") ;
	public static final Image phyAtkIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "PhyAtkIcon.png") ;
	public static final Image phyDefIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "PhyDefIcon.png") ;
	public static final Image magAtkIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "MagAtkIcon.png") ;
	public static final Image magDefIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "MagDefIcon.png") ;
	public static final Image dexIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "DexIcon.png") ;
	public static final Image agiIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "AgiIcon.png") ;
	public static final Image critIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "CritIcon.png") ;
	public static final Image herbIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "HerbIcon.png") ;
	public static final Image woodIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "WoodIcon.png") ;
	public static final Image metalIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "MetalIcon.png") ;
	
	public static final Image[] attIcons = new Image[] {phyAtkIcon, magAtkIcon, phyDefIcon, magDefIcon, dexIcon, agiIcon} ;
	public static final Image[] collectIcons = new Image[] {herbIcon, woodIcon, metalIcon} ;
	
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

	public void display(Point mousePos, DrawPrimitives DP) { }
}
