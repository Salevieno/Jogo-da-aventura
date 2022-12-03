package Utilities ;

import java.awt.Color ;
import java.awt.Font ;
import java.awt.FontMetrics ;
import java.awt.Graphics ;
import java.awt.Graphics2D ;
import java.awt.GraphicsEnvironment;
import java.awt.Image ;
import java.awt.MouseInfo ;
import java.awt.Point;
import java.awt.image.BufferedImage ;
import java.io.BufferedInputStream ;
import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.FileReader ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.math.BigDecimal ;
import java.math.RoundingMode ;
import java.nio.charset.StandardCharsets ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream ;
import javax.sound.sampled.AudioSystem ;
import javax.sound.sampled.Clip ;
import javax.swing.JPanel ;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import GameComponents.Buildings;
import GameComponents.Icon ;
import GameComponents.NPCs;
import GameComponents.Projectiles ;
import Graphics.DrawFunctions;
import Graphics.DrawPrimitives ;
import Items.Arrow;
import Items.Potion;
import LiveBeings.CreatureTypes;
import LiveBeings.Creature;

public abstract class UtilG 
{	
	public static JSONObject readJson(String filePath)
    {
        JSONParser parser = new JSONParser();
        try
        {
            Object object = parser.parse(new FileReader(filePath));
            
            //convert Object to JSONObject
            JSONObject jsonObject = (JSONObject)object;
            
            //Reading the String
            //String name = (String) jsonObject.get("Name");
            //Long level = (Long) jsonObject.get("Level");
            
            //Reading the array
           // JSONArray countries = (JSONArray)jsonObject.get("Countries");
            
            return jsonObject ;
        }
        catch(FileNotFoundException fe)
        {
            fe.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null ;
    }
	
	/* Color and image methods */
	
	public static Color[] ColorPalette(int Palette)
	{
		Color[] color = new Color[28] ;
		if (Palette == 0)
		{
			color[0] = Color.cyan ;		// Sky, crystal, menu 1, menu ornaments, player window, quest requirements, bestiary windows, knight
			color[1] = Color.magenta ;	// Mage
			color[2] = Color.orange ;	// Archer
			color[3] = Color.green ;		// Animal, pet 0, grass, plant element, selected text, poison
			color[4] = Color.gray ;		// Thief, metal, rock, unavailable stuff, player att window 1
			color[5] = Color.blue ;		// ocean, text
			color[6] = Color.red ;		// berry, blood, selected bag item, life, level, crit
			color[7] = Color.white ;		// intro windows, snow land, choices menu, ok button, player eye
			color[8] = Color.lightGray ;	// Elemental NPC color, part of window gradient backgrounds
			color[9] = Color.black ;		// Contour, attack animation
			color[10] = Color.yellow ;	// Satiation, fire element
			color[11] = Color.pink ;		// Petal
			color[12] = new Color(0, 0, 128) ;	// Player legs
			color[13] = new Color(128, 0, 0) ;	// Player shoes
			color[14] = new Color(128, 128, 128) ;	// Player shirt, player arm
			color[15] = new Color(255, 179, 128) ;	// Player head (skin)
			color[16] = new Color(0, 128, 0) ;	// Grass contour color, player hair
			color[17] = new Color(200, 50, 200) ;	// pet 1
			color[18] = new Color(200, 200, 30) ;	// pet 2 and 3, map, gold color, chest reward animation
			color[19] = new Color(100, 50, 0) ;	// Soil, wood, bag menu, player window, pterodactile
			color[20] = new Color(150, 100, 0) ;	// Sand, menu 0, menu 2
			color[21] = new Color(180, 0, 180) ;	// Herb, exp, magic floor, shopping text
			color[22] = new Color(230, 230, 230) ;	// Neutral, air, light and snow elements
			color[23] = new Color(0, 50, 0) ;	// Herb contour
			color[24] = new Color(0, 150, 0) ;	// Status 1 (poison)
			color[25] = new Color(30, 200, 30) ;	// Bag text
			color[26] = new Color(0, 150, 255) ;	// Shopping menu
			color[27] = new Color(200, 200, 255) ;	// Sign window
		}
		else if (Palette == 1)
		{
			color[0] = new Color(27, 224, 233) ;		// Sky, crystal, menu 1, menu ornaments, player window, quest requirements, bestiary windows, knight
			color[1] = new Color(230, 46, 150) ;	// Mage
			color[2] = new Color(244, 162, 25) ;	// Archer
			color[3] = new Color(29, 237, 29) ;		// Animal, pet 0, grass, plant element, selected text, poison
			color[4] = new Color(109, 103, 97) ;		// Thief, metal, rock, unavailable stuff, player att window 1
			color[5] = new Color(63, 40, 231) ;		// ocean, text
			color[6] = new Color(236, 44, 44) ;		// berry, blood, selected bag item, life, level, crit
			color[7] = new Color(241, 233, 225) ;		// intro windows, snow land, choices menu, ok button, player eye
			color[8] = new Color(203, 195, 188) ;	// Elemental NPC color, part of window gradient backgrounds
			color[9] = new Color(11, 11, 11) ;		// Contour, attack animation
			color[10] = new Color(234, 237, 27) ;	// Satiation, fire element
			color[11] = new Color(232, 93, 143) ;		// Petal
			color[12] = new Color(78, 36, 193) ;	// Player legs
			color[13] = new Color(167, 28, 70) ;	// Player shoes
			color[14] = new Color(109, 103, 97) ;	// Player shirt, player arm
			color[15] = new Color(233, 158, 125) ;	// Player head (skin)
			color[16] = new Color(46, 192, 81) ;	// Grass contour color, player hair
			color[17] = new Color(194, 35, 144) ;	// pet 1
			color[18] = new Color(127, 192, 42) ;	// pet 2 and 3, map, gold color, chest reward animation
			color[19] = new Color(101, 61, 44) ;	// Soil, wood, bag menu, player window, pterodactile
			color[20] = new Color(143, 90, 52) ;	// Sand, menu 0, menu 2
			color[21] = new Color(155, 33, 128) ;	// Herb, exp, magic floor, shopping text
			color[22] = new Color(137, 249, 204) ;	// Neutral, air, light and snow elements
			color[23] = new Color(41, 88, 66) ;	// Herb contour
			color[24] = new Color(46, 192, 81) ;	// Status 1 (poison)
			color[25] = new Color(174, 242, 94) ;	// Bag text
			color[26] = new Color(34, 82, 194) ;	// Shopping menu
			color[27] = new Color(91, 247, 217) ;	// Sign window
		}
		else if (Palette == 2)
		{
			color[0] = new Color(61, 240, 226) ;		// Sky, crystal, menu 1, menu ornaments, player window, quest requirements, bestiary windows, knight
			color[1] = new Color(235, 38, 226) ;	// Mage, petal, pet 1, herb, exp, magic floor, shopping text
			color[2] = new Color(234, 237, 27) ;	// Archer, satiation, fire element
			color[3] = new Color(122, 236, 67) ;		// Animal, pet 0, grass, plant element, selected text, poison
			color[4] = new Color(164, 155, 147) ;		// Thief, metal, rock, wall, unavailable stuff, sign window, player att window 1
			color[5] = new Color(63, 40, 231) ;		// ocean, text
			color[6] = new Color(236, 44, 44) ;		// berry, blood, selected bag item, life, level, crit
			color[7] = new Color(241, 233, 225) ;		// Elemental NPC color, neutral and light elements, intro windows, choices menu, ok button, player eye
			color[8] = new Color(137, 249, 204) ;	// snow land, air and snow elements, part of window gradient backgrounds
			color[9] = new Color(11, 11, 11) ;		// Contour, attack animation
			color[10] = new Color(167, 28, 70) ;	// Lava, volcano
			color[11] = new Color(228, 89, 80) ; 
			color[12] = new Color(245, 117, 170) ;	// Player legs, herb contour
			color[13] = new Color(43, 45, 99) ;	// Player shoes
			color[14] = new Color(28, 162, 208) ;	// Player shirt, player arm
			color[15] = new Color(219, 251, 137) ;	// Player head (skin)
			color[16] = new Color(41, 88, 66) ;	// grass contour color, player hair
			color[17] = new Color(241, 199, 128) ;
			color[18] = new Color(171, 195, 49) ;	// pet 2 and 3, map, gold color, shopping menu, chest reward animation
			color[19] = new Color(143, 90, 52) ;	// Soil, wood, bag menu, stalactite, player window, pterodactile
			color[20] = new Color(242, 176, 63) ;	// Sand, menu 0, menu 2
			color[21] = new Color(247, 224, 143) ;
			color[22] = new Color(101, 131, 246) ;
			color[23] = new Color(141, 31, 159) ;	// Bag, shopping and crafting items text
			color[24] = new Color(48, 99, 97) ;	// 
			color[25] = new Color(76, 131, 42) ;	// 
			color[26] = new Color(242, 91, 168) ;	// 
			color[27] = new Color(105, 50, 50) ;	// 
		}
		return color ;
	}
	
	public void DrawColorPalette(Point Pos, Color[] Pallete, DrawPrimitives DP)
	{
		Size size = new Size(20, 20) ;
		DP.DrawRoundRect(Pos, "TopLeft", new Size(6 * size.x + 10, (Pallete.length / 6 + 1) * size.y + 10), 1, Color.white, Color.white, true) ;
		for (int i = 0 ; i <= Pallete.length - 1 ; i += 1)
		{
			Point ColorPos = new Point((int) (Pos.x + 5 + size.x / 2 + (i % 6) * size.x), (int) (Pos.y + 5 + size.y / 2 + i / 6 * size.y)) ;
			DP.DrawRoundRect(ColorPos, "Center", size, 1, Pallete[i], Pallete[i], false) ;
		}
	}
	
	public void DrawColorScheme(DrawPrimitives DP)
	{
		Point Pos = new Point(130, 30) ;
		for (int j = 0 ; j <= 4 - 1 ; j += 1)
		{
			for (int i = 0 ; i <= 12 - 1 ; i += 1)
			{
				Color[] palette = UtilG.ColorPalette(2) ;
				if (j == 0)
				{
					palette = UtilG.AddHue(palette, i * 30 / 360.0, 0, 0) ;
				}
				if (j == 1)
				{
					palette = UtilG.AddHue(palette, i * 30 / 360.0, 1, 0) ;
				}
				if (j == 2)
				{
					palette = UtilG.AddHue(palette, i * 30 / 360.0, 0, 1) ;
				}
				if (j == 3)
				{
					palette = UtilG.toGrayScale(palette) ;
				}
				DrawColorPalette(new Point(Pos.x + 100 * i, Pos.y + 160 * j), palette, DP) ;
			}
		}
	}
	
	public void DrawAllFonts(DrawPrimitives DP)
	{		
		Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		int numrows = 60 ;
		int sx = 100, sy = 14 ;
		DP.DrawRect(new Point(0, 0), "TopLeft", new Size(500, 500), 1, Color.white, Color.black, true) ;
		for (int i = 0 ; i <= fonts.length / numrows - 1 ; i += 1)
		{
			for (int j = 0 ; j <= numrows - 1 ; j += 1)
			{
				int id = i * numrows + j ;
				if (id < fonts.length)
				{
					DP.DrawText(new Point(5 + i * sx, 5 + j * sy), "TopLeft", 0, "Estilo", new Font(fonts[id].getFontName(), Font.BOLD, 13), Color.blue) ;
				}
			}
		}
	}
	
	
	public static double GamaDecompress(double color)
	{
		if (color < 0.04045)
		{
			return color / 12.92 ;
		}
		else
		{
			return Math.pow(((color + 0.055) / 1.055), 2.4) ;
		}
	}

	public static double GamaCompress(double luminance)
	{
		if (luminance < 0.0031308)
		{
			return 12.92 * luminance ;
		}
		else
		{
			return Math.pow(1.055 * luminance, 1 / 2.4) - 0.055 ;
		}
	}
	
	public static Color HSVtoRGB(double H, double V, double S)
	{
		Color newColor ;
		
		double C = V * S ;
		double X = C * (1 - (H / 60) % 2 - 1) ;
		double m = V - C ;
		double red = 0, green = 0, blue = 0 ;
		if (0 <= H & H <= 60)
		{
			red = C ; green = X ; blue = 0 ;
		}
		else if (60 <= H & H <= 120)
		{
			red = X ; green = C ; blue = 0 ;
		}
		else if (120 <= H & H <= 180)
		{
			red = 0 ; green = C ; blue = X ;
		}
		else if (180 <= H & H <= 240)
		{
			red = 0 ; green = X ; blue = C ;
		}
		else if (240 <= H & H <= 300)
		{
			red = X ; green = 0 ; blue = C ;
		}
		else if (300 <= H & H <= 360)
		{
			red = C ; green = 0 ; blue = X ;
		}
		newColor = new Color((int) ((red - m) * 255), (int) ((green - m) * 255), (int) ((blue - m) * 255)) ;
		
		return newColor ;
	}

	public static Color[] AddHue(Color[] OriginalColor, double incH, double incV, double incS)
	{
		Color[] NewColors = new Color[OriginalColor.length] ;
		
		for (int c = 0 ; c <= NewColors.length - 1 ; c += 1)
		{
			float[] HSV = Color.RGBtoHSB(OriginalColor[c].getRed(), OriginalColor[c].getGreen(), OriginalColor[c].getBlue(), null) ;
			double H = HSV[0], S = HSV[1], V = HSV[2] ;
			float newH = (float) (H + incH), newS = (float) (S + incS), newV = (float) (V + incV) ;
			if (newH < 0)
			{
				newH = newH + 1 ;
			}
			if (1.0 < newH)
			{
				newH = newH - 1 ;
			}
			if (newS < 0)
			{
				newS = 0 ;
			}
			if (1.0 < newS)
			{
				newS = 1 ;
			}
			if (newV < 0)
			{
				newV = 0 ;
			}
			if (1.0 < newV)
			{
				newV = 1 ;
			}
			NewColors[c] = new Color(Color.HSBtoRGB(newH, newS, newV)) ;
		}
		
		return NewColors ;
	}

	public static Color toGrayScale(Color OriginalColor)
	{
		Color newColor ;

		double linRed, linGreen, linBlue ;
		linRed = GamaDecompress(OriginalColor.getRed() / 255.0) ;
		linGreen = GamaDecompress(OriginalColor.getGreen() / 255.0) ;
		linBlue = GamaDecompress(OriginalColor.getBlue() / 255.0) ;				
		double linLuminance = 0.2126 * linRed + 0.7152 * linGreen + 0.0722 * linBlue ;
		int luminance = (int) (255 * GamaCompress(linLuminance)) ;
		newColor = new Color (luminance, luminance, luminance) ;
		
		return newColor ;
	}
	
	public static Color[] toGrayScale(Color[] OriginalColor)
	{
		Color[] newColor = new Color[OriginalColor.length] ;

		for (int c = 0 ; c <= newColor.length - 1 ; c += 1)
		{
			double linRed, linGreen, linBlue ;
			linRed = GamaDecompress(OriginalColor[c].getRed() / 255.0) ;
			linGreen = GamaDecompress(OriginalColor[c].getGreen() / 255.0) ;
			linBlue = GamaDecompress(OriginalColor[c].getBlue() / 255.0) ;				
			double linLuminance = 0.2126 * linRed + 0.7152 * linGreen + 0.0722 * linBlue ;
			int luminance = (int) (255 * GamaCompress(linLuminance)) ;
			newColor[c] = new Color (luminance, luminance, luminance) ;
		}
		
		return newColor ;
	}
	
	public static Image toGrayScale(BufferedImage image)
	{
		BufferedImage newImage = image ;
		
		for (int i = 0 ; i <= image.getWidth(null) - 1 ; i += 1)
		{
			for (int j = 0 ; j <= image.getHeight(null) - 1 ; j += 1)
			{
				Color PixelColor = GetPixelColor(image, new Point(i, j)) ;
				double linRed, linGreen, linBlue ;
				linRed = GamaDecompress(PixelColor.getRed() / 255.0) ;
				linGreen = GamaDecompress(PixelColor.getGreen() / 255.0) ;
				linBlue = GamaDecompress(PixelColor.getBlue() / 255.0) ;				
				double linLuminance = 0.2126 * linRed + 0.7152 * linGreen + 0.0722 * linBlue ;
				int luminance = (int) (255 * GamaCompress(linLuminance)) ;
				Color newColor = new Color (luminance, luminance, luminance) ;
				newImage.setRGB(i, j, newColor.getRGB()) ;
			}
		}
		
		return newImage ;
	}

	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img ;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB) ;

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics() ;
	    bGr.drawImage(img, 0, 0, null) ;
	    bGr.dispose() ;

	    // Return the buffered image
	    return bimage ;
	}

	public static Color GetPixelColor(BufferedImage Bufferedimage, Point Pos)
	{
		int clr = Bufferedimage.getRGB(Pos.x, Pos.y) ; 
		int red = (clr & 0x00ff0000) >> 16 ;
		int green = (clr & 0x0000ff00) >> 8 ;
		int blue = clr & 0x000000ff ;
		return new Color(red, green, blue) ;
	}
	
	public static Image SetPixelColor(Image File, Point Pos, Color color)
	{
		BufferedImage BufferedFile = toBufferedImage(File) ;
		BufferedFile.setRGB(Pos.x, Pos.y, color.getRGB()) ;
		return BufferedFile ;
	}
	
	public static boolean PixelisTransparent(Image image, int[] PixelPos)
	{
		BufferedImage bufferedimage = toBufferedImage(image) ;
		int pixel = bufferedimage.getRGB(PixelPos[0], PixelPos[1]) ;
		if( (pixel>>24) == 0x00 )
		{
		    return true ;
		}
		return false ;
	}
	
	public static Image ChangeImageColor(Image image, float[] area, Color newColor, Color OriginalColor)
	{
		int l = (int)(image.getWidth(null)), h = (int)(image.getHeight(null)) ;
		BufferedImage BufferedFile = toBufferedImage(image) ;
		for (int i = (int)(area[0]*l) ; i <= (int)(area[2]*l) - 1 ; i += 1)
		{
			for (int j = (int)(area[1]*h) ; j <= (int)(area[3]*h) - 1 ; j += 1)
			{
				Color PreviousColor = GetPixelColor(BufferedFile, new Point(i, j)) ;
				if (PreviousColor.getRed() == OriginalColor.getRed() & PreviousColor.getGreen() == OriginalColor.getGreen() & PreviousColor.getBlue() == OriginalColor.getBlue())
				{	
					BufferedFile.setRGB(i, j, newColor.getRGB()) ;
				}	
			}
		}
		
		Image A = BufferedFile ;
		return A ;
	}
	
	public static Image ChangeImageColor(Image image, Color newColor)
	{
		int l = (int)(image.getWidth(null)), h = (int)(image.getHeight(null)) ;
		BufferedImage BufferedFile = toBufferedImage(image) ;
		for (int i = 0 ; i <= l - 1 ; i += 1)
		{
			for (int j = 0 ; j <= h - 1 ; j += 1)
			{
				if (!PixelisTransparent(image, new int[] {i, j}))
				{
					BufferedFile.setRGB(i, j, newColor.getRGB()) ;
				}	
			}
		}
		
		Image A = BufferedFile ;
		return A ;
	}
	
	public static void PlayGif(Point Pos, Image gif, DrawPrimitives DP)
	{
		DP.DrawGif(gif, Pos, "Center") ;
	}
	
	public static void TestFont(int fontid, DrawFunctions DF)
	{
		GraphicsEnvironment ge;  
	    ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
		Font[] AllFonts = ge.getAllFonts();	// 286, 562, 683, 685, 689, 720
		DF.getDrawPrimitives().DrawText(new Point(200, 100), "TopLeft", 0, "Exemplo", new Font(AllFonts[fontid].getFontName(), Font.BOLD, 12), Color.blue) ;	
		DF.getDrawPrimitives().DrawText(new Point(200, 120), "TopLeft", 0, "Exemplo", new Font(AllFonts[fontid].getFontName(), Font.BOLD, 16), Color.blue) ;	
		DF.getDrawPrimitives().DrawText(new Point(200, 160), "TopLeft", 0, "Exemplo", new Font(AllFonts[fontid].getFontName(), Font.BOLD, 20), Color.blue) ;	
		DF.getDrawPrimitives().DrawText(new Point(200, 200), "TopLeft", 0, String.valueOf(562) + ": " + AllFonts[fontid].getFontName(), new Font(AllFonts[562].getFontName(), Font.BOLD, 20), Color.blue) ;	
	}
	
	public static float RandomMult(float amplitude)
	{
		return (float)(Math.max(0, 1 - amplitude + 2*amplitude*Math.random())) ;
	}

	public static int[] OffsetFromPos(String Alignment, int l, int h)
	{
		int[] offset = new int[2] ;
		if (Alignment.equals("TopLeft"))
		{
			offset[0] = 0 ;
			offset[1] = 0 ;
		}
		if (Alignment.equals("CenterLeft"))
		{
			offset[0] = 0 ;
			offset[1] = -h/2 ;
		}
		if (Alignment.equals("BotLeft"))
		{
			offset[0] = 0 ;
			offset[1] = -h ;
		}
		if (Alignment.equals("TopCenter"))
		{
			offset[0] = -l/2 ;
			offset[1] = 0 ;
		}
		if (Alignment.equals("Center"))
		{
			offset[0] = -l/2 ;
			offset[1] = -h/2 ;
		}
		if (Alignment.equals("BotCenter"))
		{
			offset[0] = -l/2 ;
			offset[1] = -h ;
		}
		if (Alignment.equals("TopRight"))
		{
			offset[0] = -l ;
			offset[1] = 0 ;
		}
		if (Alignment.equals("CenterRight"))
		{
			offset[0] = -l ;
			offset[1] = -h/2 ;
		}
		if (Alignment.equals("BotRight"))
		{
			offset[0] = -l ;
			offset[1] = -h ;
		}
		return offset ;
	}
	
 	public static Point GetMousePos(JPanel mainpanel)
 	{
 		Point PanelLocation = new Point(mainpanel.getLocationOnScreen().x, mainpanel.getLocationOnScreen().y) ;
		return new Point(MouseInfo.getPointerInfo().getLocation().x - PanelLocation.x, MouseInfo.getPointerInfo().getLocation().y - PanelLocation.y) ;
 	} 	 	
	
 	
 	// Reading files
	public static Map<String, String[]> ReadTextFile(String Language)
	{
		String fileName = "Texto-PT-br.txt" ;
		Map<String, String[]> Text = new HashMap<String, String[]>() ;
		if (Language.equals("E"))
		{
			fileName = "Text-EN.txt" ;
		}
		try
		{	
			FileInputStream fileReader = new FileInputStream (fileName) ;
			InputStreamReader streamReader = new InputStreamReader(fileReader, StandardCharsets.UTF_8) ;
			BufferedReader bufferedReader = new BufferedReader(streamReader) ; 					
			String Line = bufferedReader.readLine() ;
			String key = "" ;
			ArrayList<String> content = new ArrayList<>() ;
			while (Line != null)
			{
				if (Line.contains("*"))
				{
					Text.put(key, content.toArray(new String[content.size()])) ;
					key = Line ;
					content = new ArrayList<>() ;
	            }
				content.add(Line) ;
				Line = bufferedReader.readLine() ;
			}
			Text.put(key, content.toArray(new String[content.size()])) ;
			bufferedReader.close() ;
		}
		catch(FileNotFoundException ex) 
		{
            System.out.println("Unable to find file '" + fileName + "' (text file)") ;                
        }		
		catch(IOException ex) 
		{
            System.out.println("Error reading file '" + fileName + "' (text file)") ;                  
        }
		return Text ;
	}

	public static ArrayList<String[]> ReadcsvFile(String FileName)
	{
		BufferedReader br = null ;
        String line = "" ;
        String separator = "," ;
        ArrayList<String[]> Input = new ArrayList<String[]>() ;
        try 
        {
            br = new BufferedReader(new FileReader(FileName)) ;
            line = br.readLine() ;
            while ((line = br.readLine()) != null) 
            {
                Input.add(line.split(separator)) ;
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace() ;
        }
        catch (IOException e)
        {
            e.printStackTrace() ;
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close() ;
                }
                catch (IOException e)
                {
                    e.printStackTrace() ;
                }
            }
        }
        return Input ;
	}
	
	public static Object[][] ReadTextFileAsObject(String FileName, int Nrows, int Ncolumns)
	{
		BufferedReader br = null ;
        String line = "" ;
        String separator = "," ;
        int cont = 0 ;
        Object[][] Input = new String[Nrows][Ncolumns] ;
        try 
        {
            br = new BufferedReader(new FileReader(FileName)) ;
            line = br.readLine() ;
            while ((line = br.readLine()) != null) 
            {
                Input[cont] = line.split(separator) ;
                ++cont ;
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace() ;
        }
        catch (IOException e)
        {
            e.printStackTrace() ;
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close() ;
                }
                catch (IOException e)
                {
                    e.printStackTrace() ;
                }
            }
        }
        return Input ;
	}
	
	public static boolean FindFile(String FileName)
	{
		BufferedReader br = null ;
		try 
        {
            br = new BufferedReader(new FileReader(FileName)) ;
        } 
        catch (FileNotFoundException e) 
        {
            return false ;
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close() ;
                }
                catch (IOException e)
                {
                    e.printStackTrace() ;
                }
            }
        }
		return true ;
	}
	
	/*public static void ReadJSON()
	{
	// need to use the JSON library
		JSONParser parser = new JSONParser() ;
	    try
	    {
	       Object obj = parser.parse(new FileReader("/Users/User/Desktop/course.json")) ;
	       JSONObject jsonObject = (JSONObject)obj ;
	       String name = (String)jsonObject.get("Name") ;
	       String course = (String)jsonObject.get("Course") ;
	       JSONArray subjects = (JSONArray)jsonObject.get("Subjects") ;
	       System.out.println("Name: " + name) ;
	       System.out.println("Course: " + course) ;
	       System.out.println("Subjects:") ;
	       Iterator iterator = subjects.iterator() ;
	       while (iterator.hasNext())
	       {
	          System.out.println(iterator.next()) ;
	       }
	    }
	    catch(Exception e)
	    {
	       e.printStackTrace() ;
	    }
	}*/
	
	public static String[][] IncreaseArraySize(String[][] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			return new String[size][] ;
		}
		else
		{
			String[][] NewArray = new String[OriginalArray.length + size][] ;
			for (int i = 0 ; i <= OriginalArray.length - 1 ; i += 1)
			{
				NewArray[i] = OriginalArray[i] ;
			}
			return NewArray ;
		}
	}

	public static Clip MusicFileToClip(File MusicFile)
	{
		Clip MusicClip = null ;
		try 
 		{
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(MusicFile) ;
	        MusicClip = AudioSystem.getClip() ;
	        MusicClip.open(audioInputStream) ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error loading clip.") ;
 	        ex.printStackTrace() ;
 	    }
		
		return MusicClip ;
	}
	
	public static int[] ArrayWithValuesGreaterThan(int[] OriginalArray, int MinValue)
	{
		int NewArrayLength = 0 ;
		for (int i = 0 ; i <= OriginalArray.length - 1 ; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				++NewArrayLength ;
			}
		}
		int[] NewArray = new int[NewArrayLength] ;
		int cont = 0 ;
		for (int i = 0 ; i <= OriginalArray.length - 1 ; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				NewArray[cont] = OriginalArray[i] ;
				++cont ;
			}
		}
		return NewArray ;
	}
	
	public static int[] ArrayWithIndexesGreaterThan(int[] OriginalArray, int MinValue)
	{
		int NewArrayLength = 0 ;
		for (int i = 0 ; i <= OriginalArray.length - 1 ; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				++NewArrayLength ;
			}
		}
		int[] NewArray = new int[NewArrayLength] ;
		int cont = 0 ;
		for (int i = 0 ; i <= OriginalArray.length - 1 ; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				NewArray[cont] = i ;
				++cont ;
			}
		}
		return NewArray ;
	}
	
	public static int[][] ArrayWithFirstTermEqualTo(int FirstTerm, int[] OriginalFirstTerms, int[][] OriginalArray)
	{
		int NewArrayLength = 0 ;
		for (int i = 0 ; i <= OriginalArray.length - 1 ; ++i)
		{
			if (OriginalFirstTerms[i] == (FirstTerm))
			{
				++NewArrayLength ;
			}
		}
		int[][] NewArray = new int[NewArrayLength][] ;
		int cont = 0 ;
		for (int i = 0 ; i <= OriginalArray.length - 1 ; ++i)
		{
			if (OriginalFirstTerms[i] == FirstTerm)
			{
				NewArray[cont] = OriginalArray[i] ;
				++cont ;
			}
		}
		return NewArray ;
	}


	public static String[] AddElem(String[] OriginalArray, String NewElem)
	{
		if (OriginalArray == null)
		{
			return new String[] {NewElem} ;
		}
		else
		{
			String[] NewArray = new String[OriginalArray.length + 1] ;
			for (int i = 0 ; i <= OriginalArray.length - 1 ; i += 1)
			{
				NewArray[i] = OriginalArray[i] ;
			}
			NewArray[OriginalArray.length] = NewElem ;
			return NewArray ;
		}
	}
	
	public static Point Translate(Point originalPoint, int dx, int dy)
	{
		return new Point(originalPoint.x + dx, originalPoint.y + dy) ;
	}

	public static int TextL(String Text, Font font, Graphics G)
	{
		FontMetrics metrics = G.getFontMetrics(font) ;
		return (int) (metrics.stringWidth(Text)) ;
	}
	
	public static int TextH(int TextSize)
	{
		return (int)(0.8*TextSize) ;
	}
	
	public static boolean IsAlphaNumeric(String input)
	{
		if(input != null)
		{
			if (input.equals("A") | input.equals("B")  | input.equals("C")  | input.equals("D") | input.equals("E") | input.equals("F") | input.equals("G") | input.equals("H") | input.equals("I") | input.equals("J") | input.equals("K") | input.equals("L") | input.equals("M") | input.equals("N") | input.equals("O") | input.equals("P") | input.equals("Q") | input.equals("R") | input.equals("S") | input.equals("T") | input.equals("U") | input.equals("V") | input.equals("W") | input.equals("X") | input.equals("Y") | input.equals("Z") | input.equals("0") | input.equals("1") | input.equals("2") | input.equals("3") | input.equals("4") | input.equals("5") | input.equals("6") | input.equals("7") | input.equals("8") | input.equals("9"))
			{
				return true ;
			}
		}	
		return false ;
	}

	/*public static String[] FitText(String inputstring, int NumberOfChars)
	{
		String[] newstring = new String[inputstring.length()] ;
		int CharsExeeding = 0 ;		
		int i = 0 ;
		int FirstChar = 0 ;
		int LastChar = 0 ;
		do
		{
			FirstChar = i*NumberOfChars - CharsExeeding ;
			LastChar = FirstChar + Math.min(NumberOfChars, Math.min((i + 1)*NumberOfChars, inputstring.length() - i*NumberOfChars) + CharsExeeding) ;
			char[] chararray = new char[NumberOfChars] ;
			inputstring.getChars(FirstChar, LastChar, chararray, 0) ;
			if (chararray[LastChar - FirstChar - 1] != ' ' & chararray[LastChar - FirstChar - 1] != '.' & chararray[LastChar - FirstChar - 1] != '?' & chararray[LastChar - FirstChar - 1] != '!' & chararray[LastChar - FirstChar - 1] != '/' & chararray[LastChar - FirstChar - 1] != ':')
			{
				for (int j = chararray.length - 1 ; 0 <= j ; j += -1)
				{
					CharsExeeding += 1 ;
					LastChar += -1 ;
					if (chararray[j] == ' ' | chararray[j] == '.' | chararray[j] == '?' | chararray[j] == '!' | chararray[j] == '/' | chararray[j] == ':')
					{
						char[] chararray2 = new char[NumberOfChars] ;
						inputstring.getChars(Math.min(Math.max(0, FirstChar), inputstring.length()), LastChar, chararray2, 0) ;
						newstring[i] = String.valueOf(chararray2) ;
						CharsExeeding += -1 ;
						j = 0 ;
					}
				}
			}
			else
			{
				newstring[i] = String.valueOf(chararray) ;
			}
			i += 1 ;
		} while(LastChar != inputstring.length() & i != inputstring.length()) ;
		System.out.println(Arrays.toString(newstring));
		String[] newstring2 = new String[i] ;
		for (int j = 0 ; j <= newstring2.length - 1 ; j += 1)
		{
			newstring2[j] = newstring[j] ;
		}
		return newstring2 ;
	}*/
	
	public static ArrayList<String> FitText(String inputstring, int NumberOfChars)
	{
		ArrayList<String> newstring = new ArrayList<String>() ;
		int CharsExeeding = 0 ;		
		int i = 0 ;
		int FirstChar = 0 ;
		int LastChar = 0 ;
		do
		{
			int af = Math.min(NumberOfChars, Math.min((i + 1)*NumberOfChars, inputstring.length() - i*NumberOfChars) + CharsExeeding) ;
			FirstChar = i*NumberOfChars - CharsExeeding ;
			LastChar = FirstChar + af ;
			char[] chararray = new char[NumberOfChars] ;
			inputstring.getChars(FirstChar, LastChar, chararray, 0) ;
			if (chararray[LastChar - FirstChar - 1] != ' ' & chararray[LastChar - FirstChar - 1] != '.' & chararray[LastChar - FirstChar - 1] != '?' & chararray[LastChar - FirstChar - 1] != '!' & chararray[LastChar - FirstChar - 1] != '/' & chararray[LastChar - FirstChar - 1] != ':')
			{
				for (int j = chararray.length - 1 ; 0 <= j ; j += -1)
				{
					CharsExeeding += 1 ;
					LastChar += -1 ;
					if (chararray[j] == ' ' | chararray[j] == '.' | chararray[j] == '?' | chararray[j] == '!' | chararray[j] == '/' | chararray[j] == ':')
					{
						int af2 = Math.min(Math.max(0, FirstChar), inputstring.length()) ;
						char[] chararray2 = new char[LastChar - af2] ;
						inputstring.getChars(Math.min(Math.max(0, FirstChar), inputstring.length()), LastChar, chararray2, 0) ;
						newstring.add(String.valueOf(chararray2)) ;
						CharsExeeding += -1 ;
						j = 0 ;
					}
				}
			}
			else
			{
				chararray = new char[LastChar - FirstChar] ;
				inputstring.getChars(FirstChar, LastChar, chararray, 0) ;
				newstring.add(String.valueOf(chararray)) ;
			}
			i += 1 ;
		} while(LastChar != inputstring.length() & i != inputstring.length()) ;
		ArrayList<String> newstring2 = new ArrayList<>(i) ;
		for (int j = 0 ; j <= newstring2.size() - 1 ; j += 1)
		{
			newstring2.set(j, newstring.get(j)) ;
		}
		return newstring2 ;
	}

	public static int IndexOf(int[] Array, int Value)
	{
		if (Array != null)
		{
			for (int i = 0 ; i <= Array.length - 1 ; i += 1)
			{
				if (Value == Array[i])
				{
					return i ;
				}
			}
		}
		return -1 ;
	}

	public static int IndexOf(String[] Vector, String Value)
	{
		if (Vector != null)
		{
			for (int i = 0 ; i <= Vector.length - 1 ; ++i)
			{
				if (Vector[i].equals(Value))
				{
					return i ;
				}
			}
		}
		return -1 ;
	}

	public static String[] RemoveString(String[] input)
	{
		String[] result = new String[input.length - 1] ;
		for (int i = 0 ; i <= input.length - 2 ; ++i)
		{
			result[i] = input[i] ;
		}
		return result ;
	}
	
	public static String[] AddString(String[] input, String NewMember)
	{
		String[] result = new String[input.length + 1] ;
		for (int i = 0 ; i <= input.length - 1 ; ++i)
		{
			result[i] = input[i] ;
		}
		result[input.length] = NewMember ;
		return result ;
	}
	public static float Round(float num, int decimals)
	{
		return BigDecimal.valueOf(num).setScale(decimals, RoundingMode.HALF_EVEN).floatValue() ;
	}

	public static boolean ArrayContains(String[] Array, String value)
	{
		if (Array != null)
		{
			for (int i = 0 ; i <= Array.length - 1 ; i += 1)
			{
				if (Array[i].equals(value))
				{
					return true ;
				}
			}
		}
		return false ;
	}

	public static boolean ArrayContains(int[] Array, int value)
	{
		if (Array != null)
		{
			for (int i = 0 ; i <= Array.length - 1 ; i += 1)
			{
				if (Array[i] == value)
				{
					return true ;
				}
			}
		}
		return false ;
	}
	
	public static int[] ArrayInPos(int[][] Array, int pos)
	{
		int[] NewArray = new int[Array.length] ;
		for (int i = 0 ; i <= NewArray.length - 1 ; i += 1)
		{
			NewArray[i] = Array[i][pos] ;
		}
		return NewArray ;
	}
	
	public static int RandomCoord1D(int size, float MinCoord, float Range, int step)
	{
		return (int)(size*(Range*Math.random() + MinCoord)/step)*step ;
	}
	
	public static Point RandomPos(Point minCoord, Size range, Size step)
	{
		return new Point((int)((range.x*Math.random() + minCoord.x)/step.x)*step.x, (int)((range.y*Math.random() + minCoord.y)/step.y)*step.y) ;
	}
	
	public static void ResetMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.setMicrosecondPosition(0) ; ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with starting sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
	
	public static void PlayMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.loop(999) ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with playing sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
 	
 	public static void StopMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.stop() ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with stopping sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
 	
 	public static void SwitchMusic(Clip MusicFile1, Clip MusicFile2)
 	{
 		ResetMusic(MusicFile1) ;
 		StopMusic(MusicFile1) ;
		PlayMusic(MusicFile2) ;
 	}
	
	/*public Image SetPixelColor(Image File, int[] Pos, Color color)
	{
		BufferedImage BufferedFile = toBufferedImage(File) ;
		BufferedFile.setRGB(Pos[0], Pos[1], color.getRGB()) ;
		return BufferedFile ;
	}*/

	public static float dist1D(int PosA, int PosB)
	{
		return Math.abs(PosA - PosB) ;
	}
	
	public static float dist3D(int[] PosA, int[] PosB)
	{
		return (float)(Math.sqrt(Math.pow(PosB[0] - PosA[0], 2) + Math.pow(PosB[1] - PosA[1], 2) + Math.pow(PosB[2] - PosA[2], 2))) ;
	}
	
	public static float getAngle(float[] speed)
	{
		if (speed[0] == 0)
		{
			if (0 < speed[1])
			{
				return (float) (Math.PI / 2) ;
			}
			else
			{
				return (float) (-Math.PI / 2) ;
			}
		}
		else
		{
			if (0 < speed[0])
			{
				if (0 < speed[1])	// 1st quadrant
				{
					return (float) Math.atan(speed[1] / speed[0]) ;
				}
				else				// 4th quadrant
				{
					return (float) (2 * Math.PI - Math.atan(speed[1] / speed[0])) ;
				}
			}
			else
			{
				if (0 < speed[1])	// 2st quadrant
				{
					return (float) (Math.PI - Math.atan(speed[1] / speed[0])) ;
				}
				else				// 3rd quadrant
				{
					return (float) (Math.PI + Math.atan(speed[1] / speed[0])) ;
				}
			}
		}
	}
	
	public static float spacing(float L, int n, float size, float offset)
	{
		float s = -1 ;
		
		if (1 < n)
		{
			s = (L - 2 * offset - size) / (n - 1) ;
		}
		else if (n == 1)
		{
			s = L - 2 * offset ;
		}
		
		return s ;
	}

	public static float clearspacing(float L, int n, float size, float offset)
	{
		float s = -1 ;
		
		if (1 < n)
		{
			s = (L - 2 * offset - n * size) / (n - 1) ;
		}
		else if (n == 1)
		{
			s = L - 2 * offset ;
		}
		
		return s ;
	}
	
	public static boolean isInside(Point objPos, Point rectTopLeftPos, Size rectSize)
	{
		if (rectTopLeftPos.x <= objPos.x & objPos.y <= rectTopLeftPos.y + rectSize.y & objPos.x <= rectTopLeftPos.x + rectSize.x & rectTopLeftPos.y <= objPos.y)
		{
			return true ;
		} 
		else
		{
			return false ;
		}
	}

	/*public static boolean isInside(double[] ObjPos, double[] RectBotLeftPos, double L, double H)
	{
		if (RectBotLeftPos[0] <= ObjPos[0] & ObjPos[1] <= RectBotLeftPos[1] & ObjPos[0] <= RectBotLeftPos[0] + L & RectBotLeftPos[1] - H <= ObjPos[1])
		{
			return true ;
		} 
		else
		{
			return false ;
		}
	}*/
		
	public static boolean isNumeric(String str) 
	{ 
	  try 
	  {  
	    Double.parseDouble(str) ;  
	    return true ;
	  } catch(NumberFormatException e)
	  {  
	    return false ;  
	  }  
	}
	
	public static Object ConvertArray(Object[] OriginalArray, String From, String To)
	{
		if (OriginalArray != null)
		{
			if (!OriginalArray[0].equals("null"))
			{
				if (From.equals("String") & To.equals("int"))
				{
					int[] IntArray = new int[OriginalArray.length] ;
					for (int i = 0 ; i <= OriginalArray.length - 1 ; i += 1)
					{
						IntArray[i] = Integer.parseInt((String) OriginalArray[i]) ;
					}
					return IntArray ;
				}
				else if (From.equals("String") & To.equals("float"))
				{
					float[] FloatArray = new float[OriginalArray.length] ;
					for (int i = 0 ; i <= OriginalArray.length - 1 ; i += 1)
					{
						FloatArray[i] = Float.parseFloat((String) OriginalArray[i]) ;
					}
					return FloatArray ;
				}
				else if (From.equals("String") & To.equals("boolean"))
				{
					boolean[] BoolArray = new boolean[OriginalArray.length] ;
					for (int i = 0 ; i <= OriginalArray.length - 1 ; i += 1)
					{
						BoolArray[i] = ((String) OriginalArray[i]).equals("true") ;
					}
					return BoolArray ;
				}
				else
				{
					return OriginalArray ;
				}
			}
			else
			{
				return OriginalArray ;
			}
		}
		else
		{
			return OriginalArray ;
		}
	}
	
	public static Object ConvertDoubleArray(Object[][] OriginalArray, String From, String To)
	{
		if (From.equals("String") & To.equals("int"))
		{
			int[][] IntArray = new int[OriginalArray.length][OriginalArray[0].length] ;
			for (int i = 0 ; i <= OriginalArray.length - 1 ; i += 1)
			{
				for (int j = 0 ; j <= OriginalArray[i].length - 1 ; j += 1)
				{
					IntArray[i][j] = Integer.parseInt((String) OriginalArray[i][j]) ;
				}
			}
			return IntArray ;
		}
		else if (From.equals("String") & To.equals("float"))
		{
			float[][] FloatArray = new float[OriginalArray.length][OriginalArray[0].length] ;
			for (int i = 0 ; i <= OriginalArray.length - 1 ; i += 1)
			{
				for (int j = 0 ; j <= OriginalArray[i].length - 1 ; j += 1)
				{
					FloatArray[i][j] = Float.parseFloat((String) OriginalArray[i][j]) ;
				}
			}
			return FloatArray ;
		}
		else
		{
			return OriginalArray ;
		}
	}
	
	public static String[] toString(String[] str)
	{
		if (str != null)
		{
			String[] newstr = new String[str.length] ;
			for (int i = 0 ; i <= str.length - 1 ; ++i)
			{
				str[i] = str[i].replace(" ", "") ;
				if (i == 0)
				{
					str[i] = str[i].replace("[", "") ;
				}
				if (i == str.length - 1)
				{
					str[i] = str[i].replace("]", "") ;
				}
				newstr[i] = str[i] ;
			}
			return newstr ;
		}
		else
		{
			return null ;
		}
	}
	
	public static String[][] deepToString(String[] str, int NumberOfColumns)
	{
		int NumberOfRows = str.length/NumberOfColumns ;
		String[][] newstr = new String[NumberOfRows][NumberOfColumns] ;
		for (int i = 0 ; i <= NumberOfRows - 1 ; i += 1)
		{
			for (int j = 0 ; j <= NumberOfColumns - 1 ; j += 1)
			{
				String a = str[NumberOfColumns*i + j].replace(" ", "") ;
				a = a.replace("[", "") ;
				a = a.replace("]", "") ;
				a = a.replace(",", "") ;
				newstr[i][j] = a ;
			}
		}
		return newstr ;
	}
	
	public static Color[] toColor(String[] StrReader)
	{
		int cont = 0 ;
		int red = 0, green = 0, blue = 0 ;
		Color[] NewColor = new Color[StrReader.length/3] ;
		for (int i = 0 ; i <= StrReader.length - 1 ; i += 1)
		{
			StrReader[i] = StrReader[i].replace(" ", "") ;
			StrReader[i] = StrReader[i].replace("java.awt.Color", "") ;
			StrReader[i] = StrReader[i].replace("[", "") ;
			StrReader[i] = StrReader[i].replace("]", "") ;
			if (StrReader[i].contains("r="))
			{
				StrReader[i] = StrReader[i].replace("r=", "") ;
				red = Integer.parseInt(StrReader[i]) ;
			}
			if (StrReader[i].contains("g="))
			{
				StrReader[i] = StrReader[i].replace("g=", "") ;
				green = Integer.parseInt(StrReader[i]) ;
			}
			if (StrReader[i].contains("b="))
			{
				StrReader[i] = StrReader[i].replace("b=", "") ;
				blue = Integer.parseInt(StrReader[i]) ;
				NewColor[cont] = new Color(red, green, blue) ;
				++cont ;
			}
		}
		return NewColor ;
	}
}
