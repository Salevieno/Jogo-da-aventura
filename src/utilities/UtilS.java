package utilities ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.Game;

public abstract class UtilS 
{
	public static final List<String> paths = new ArrayList<>() ;
	
	public static Color[] ReadColorPalette(Image paletteImage, String mode)
	{
		Color[] palette = new Color[28] ;

		switch (mode)
		{
			case "Normal":			
				for (int y = 0 ; y <= 7 - 1 ; y += 1)
				{
					for (int x = 0 ; x <= 4 - 1 ; x += 1)
					{
						palette[x + 4 * y] = Util.GetPixelColor(Util.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}				
				return palette ;
			
			case "Konami":			
				for (int x = 0 ; x <= 4 - 1 ; x += 1)
				{
					for (int y = 0 ; y <= 7 - 1 ; y += 1)
					{
						palette[7 * x + y] = Util.GetPixelColor(Util.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}				
				return palette ;

			default: return null ;
		}
	}

	public static Image loadImage(String path)
	{
		if (paths.contains(path)) { System.out.println("Warn: Loading image " + path + " multiple times") ;}

		paths.add(path);
		return Util.loadImage(Game.ImagesPath + path) ;
	}
	
	// TODO move to lib util
	public static RelativePos posRelativeToRectangle(Point pos, Point targetPos, Dimension targetSize)
	{
		if (Util.isInside(pos, targetPos, targetSize))
		{
			return RelativePos.inside;
		}
		if (pos.x == (targetPos.x - 1) & (targetPos.y <= pos.y & pos.y <= targetPos.y + targetSize.height))
		{
			return RelativePos.left ;
		}
		if (pos.x == (targetPos.x + targetSize.width + 1) & (targetPos.y <= pos.y & pos.y <= targetPos.y + targetSize.height))
		{
			return RelativePos.right ;
		}
		if ((targetPos.x <= pos.x & pos.x <= targetPos.x + targetSize.width) & pos.y == (targetPos.y - 1))
		{
			return RelativePos.above ;
		}
		if ((targetPos.x <= pos.x & pos.x <= targetPos.x + targetSize.width) & pos.y == (targetPos.y + targetSize.height + 1))
		{
			return RelativePos.below ;
		}
		
		return null ;
	}
 	

}