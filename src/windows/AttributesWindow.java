package windows;

import java.awt.Image;

import main.Game;
import utilities.UtilG;

public class AttributesWindow extends GameWindow
{
	
	Image phyAtkIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "PhyAtkIcon.png") ;
	Image phyDefIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "PhyDefIcon.png") ;
	Image magAtkIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "MagAtkIcon.png") ;
	Image magDefIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "MagDefIcon.png") ;
	Image dexIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "DexIcon.png") ;
	Image agiIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "AgiIcon.png") ;
	Image critIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "CritIcon.png") ;
	Image herbIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "HerbIcon.png") ;
	Image woodIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "WoodIcon.png") ;
	Image metalIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\attIcons\\" + "MetalIcon.png") ;
	
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
}
