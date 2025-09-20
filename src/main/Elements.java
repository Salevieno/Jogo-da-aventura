package main;

import java.awt.Image;
import java.util.Arrays;

public enum Elements
{
	neutral (Game.loadImage(Path.ELEMENTS_IMG + "ElementNeutral.png")),
	water (Game.loadImage(Path.ELEMENTS_IMG + "ElementWater.png")),
	fire (Game.loadImage(Path.ELEMENTS_IMG + "ElementFire.png")),
	plant (Game.loadImage(Path.ELEMENTS_IMG + "ElementPlant.png")),
	earth (Game.loadImage(Path.ELEMENTS_IMG + "ElementEarth.png")),
	air (Game.loadImage(Path.ELEMENTS_IMG + "ElementAir.png")),
	thunder (Game.loadImage(Path.ELEMENTS_IMG + "ElementThunder.png")),
	light (Game.loadImage(Path.ELEMENTS_IMG + "ElementLight.png")),
	dark (Game.loadImage(Path.ELEMENTS_IMG + "ElementDark.png")),
	snow (Game.loadImage(Path.ELEMENTS_IMG + "ElementSnow.png"));
	
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
