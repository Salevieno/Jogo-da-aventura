package main;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import utilities.Util;

public abstract class Palette
{
	public static Color[] colors;
	private static final Color[] normalPalette;
	private static final Color[] konamiPalette;

    static
    {        
		Image paletteImage = ImageLoader.loadImage(Path.GAME_IMG + "ColorPalette.png");
		normalPalette = readColorPalette(paletteImage, "Normal");
		konamiPalette = readColorPalette(paletteImage, "Konami");
		colors = normalPalette;
    }

    public static void switchToNormal() { colors = normalPalette ;}
    public static void switchToKonami() { colors = konamiPalette ;}
    
	private static Color[] readColorPalette(Image paletteImage, String mode)
	{
		Color[] palette = new Color[28];

		switch (mode)
		{
		case "Normal":
			for (int y = 0; y <= 7 - 1; y += 1)
			{
				for (int x = 0; x <= 4 - 1; x += 1)
				{
					palette[x + 4 * y] = Util.getPixelColor(Util.toBufferedImage(paletteImage), new Point(x, y));
				}
			}
			return palette;

		case "Konami":
			for (int x = 0; x <= 4 - 1; x += 1)
			{
				for (int y = 0; y <= 7 - 1; y += 1)
				{
					palette[7 * x + y] = Util.getPixelColor(Util.toBufferedImage(paletteImage), new Point(x, y));
				}
			}
			return palette;

		default:
			return null;
		}
	}

}
