package windows;

import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import utilities.UtilS;

public abstract class AttributesWindow extends GameWindow
{
	
	Image phyAtkIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "PhyAtkIcon.png") ;
	Image phyDefIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "PhyDefIcon.png") ;
	Image magAtkIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "MagAtkIcon.png") ;
	Image magDefIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "MagDefIcon.png") ;
	Image dexIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "DexIcon.png") ;
	Image agiIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "AgiIcon.png") ;
	Image critIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "CritIcon.png") ;
	Image herbIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "HerbIcon.png") ;
	Image woodIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "WoodIcon.png") ;
	Image metalIcon = UtilS.loadImage("\\Windows\\attIcons\\" + "MetalIcon.png") ;
	
	Image[] attIcons = new Image[] {phyAtkIcon, magAtkIcon, phyDefIcon, magDefIcon, dexIcon, agiIcon} ;
	Image[] collectIcons = new Image[] {herbIcon, woodIcon, metalIcon} ;
	
	public AttributesWindow(Image image, int numberTabs)
	{
		super("Atributos", image, 0, numberTabs, 0, 0) ;
	}

	@Override
	public void navigate(String action)
	{
	}

	public void display(Point mousePos, DrawingOnPanel DP) { }
}
