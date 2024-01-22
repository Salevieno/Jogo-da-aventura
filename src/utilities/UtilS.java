package utilities ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays ;
import java.util.List;

import main.Game;
import maps.GameMap;
import maps.GroundType;
import maps.GroundTypes;

public abstract class UtilS 
{
	public static final List<String> paths = new ArrayList<>() ;
	
	public static Color[] ReadColorPalette(Image paletteImage, String mode)
	{
		Color[] palette = new Color[28] ;

		switch (mode)
		{
			case "Normal":
			{
				for (int y = 0 ; y <= 7 - 1 ; y += 1)
				{
					for (int x = 0 ; x <= 4 - 1 ; x += 1)
					{
						palette[x + 4 * y] = UtilG.GetPixelColor(UtilG.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}
				
				break ;
			}
			case "Konami":
			{
				for (int x = 0 ; x <= 4 - 1 ; x += 1)
				{
					for (int y = 0 ; y <= 7 - 1 ; y += 1)
					{
						palette[7 * x + y] = UtilG.GetPixelColor(UtilG.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}
				
				break ;
			}
		}
		return palette ;
	}

	public static String RelPos(Point point, Point refPos) { return refPos.x < point.x ? "Right" : "Left" ;}
	
	public static Image loadImage(String path) {
		if (paths.contains(path))
		{
			System.out.println(path + " repetido ");
		}
		paths.add(path); return UtilG.loadImage(Game.ImagesPath + path) ;}
	
	public static int MirrorFromRelPos(String relPos) { return relPos.equals("Left") ? -1 : 1 ;}
	
	public static RelativePos calcRelativePos(Point pos, Point targetPos, Dimension targetSize)
	{
		if (UtilG.isInside(pos, targetPos, targetSize))
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
		
 	public static RelativePos checkAdjacentGround(Point pos, GameMap map, GroundTypes targetGroundType)
	{
 		
 		Point userPos = new Point(pos) ;

		if (map == null) { return null ;}
		
		if (map.getgroundTypes() == null) { return null ;}

		for (GroundType groundType : map.getgroundTypes())
		{
			if (!groundType.getType().equals(targetGroundType)) { continue ;}	
			
			return calcRelativePos(userPos, groundType.getPos(), groundType.getSize()) ;
		}
		
		return null ;
		
	}
 	
 	public static boolean isTouching(Point pos, GameMap map, GroundTypes groundType)
 	{
 		RelativePos adjGround = checkAdjacentGround(pos, map, groundType) ;
 		
 		if (adjGround == null) { return false ;}
 		
 		List<RelativePos> adjPositions = Arrays.asList(RelativePos.values()) ;
 		
 		return adjPositions.contains(adjGround) ;
 	}
 	
 	public static boolean isInside(Point pos, GameMap map, GroundTypes groundType)
 	{
 		RelativePos adjGround = checkAdjacentGround(pos, map, groundType) ;
 		
 		if (adjGround == null) { return false ;}
 		
 		return adjGround.equals(RelativePos.inside) ;
 	}


}