package main;

import java.awt.Image;
import java.util.Arrays;

public enum Elements
{
	neutral (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementNeutral.png"), new double[] {1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00}),
	water (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementWater.png"), new double[] {1.00,1.00,1.22,0.87,0.92,0.95,0.71,1.00,1.00,1.05}),
	fire (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementFire.png"), new double[] {1.00,0.71,1.00,1.22,0.89,1.05,0.95,0.84,1.14,0.89}),
	plant (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementPlant.png"), new double[] {1.00,1.14,0.71,1.00,1.22,1.00,0.89,1.00,1.00,1.05}),
	earth (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementEarth.png"), new double[] {1.00,1.10,1.14,0.84,1.00,0.89,1.14,1.00,1.00,1.05}),
	air (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementAir.png"), new double[] {1.00,0.95,0.84,1.00,0.89,1.00,1.10,0.95,1.05,1.00}),
	thunder (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementThunder.png"), new double[] {1.00,1.22,0.95,1.05,0.71,0.95,1.00,0.89,1.10,1.05}),
	light (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementLight.png"), new double[] {1.00,1.00,0.89,1.00,1.00,0.95,0.89,1.00,1.22,1.00}),
	dark (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementDark.png"), new double[] {1.00,1.00,1.10,1.00,1.00,1.05,1.10,1.22,1.00,1.00}),
	snow (ImageLoader.loadImage(Path.ELEMENTS_IMG + "ElementSnow.png"), new double[] {1.00,1.10,1.10,0.95,0.89,1.00,0.95,1.00,1.00,1.00});
	
	public final Image image ;
	private final double[] mult ;
	
	private Elements(Image image, double[] mult)
	{
		this.image = image ;
		this.mult = mult ;
	}
	
	public static int getID(Elements elem)
	{
		return Arrays.asList(Elements.values()).indexOf(elem) ;
	}

	public double getMultToElem(Elements elem) { return this.mult[elem.ordinal()] ;}
	
	
}
