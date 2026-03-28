package main;

import java.awt.Image;
import java.util.Arrays;

public enum Elements
{
	neutral (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementNeutral.png")),
	water (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementWater.png")),
	fire (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementFire.png")),
	plant (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementPlant.png")),
	earth (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementEarth.png")),
	air (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementAir.png")),
	thunder (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementThunder.png")),
	light (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementLight.png")),
	dark (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementDark.png")),
	snow (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementSnow.png"));
	
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
