package utilities;

import java.awt.Image;
import java.util.Arrays;

public enum Elements
{
	neutral (UtilS.loadImage("\\Elements\\" + "ElementNeutral.png")),
	water (UtilS.loadImage("\\Elements\\" + "ElementWater.png")),
	fire (UtilS.loadImage("\\Elements\\" + "ElementFire.png")),
	plant (UtilS.loadImage("\\Elements\\" + "ElementPlant.png")),
	earth (UtilS.loadImage("\\Elements\\" + "ElementEarth.png")),
	air (UtilS.loadImage("\\Elements\\" + "ElementAir.png")),
	thunder (UtilS.loadImage("\\Elements\\" + "ElementThunder.png")),
	light (UtilS.loadImage("\\Elements\\" + "ElementLight.png")),
	dark (UtilS.loadImage("\\Elements\\" + "ElementDark.png")),
	snow (UtilS.loadImage("\\Elements\\" + "ElementSnow.png"));
	
	public final Image image ;
	
	private Elements(Image image)
	{
		this.image = image ;
	}
	
	public static int getID(Elements elem)
	{
		return Arrays.asList(Elements.values()).indexOf(elem) ;
	}
}
