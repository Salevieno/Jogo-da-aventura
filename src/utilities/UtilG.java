package utilities ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font ;
import java.awt.FontMetrics ;
import java.awt.Graphics ;
import java.awt.Graphics2D ;
import java.awt.Image ;
import java.awt.MouseInfo ;
import java.awt.Point;
import java.awt.image.BufferedImage ;
import java.io.BufferedReader ;
import java.io.File;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.FileReader ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.math.BigDecimal ;
import java.math.RoundingMode ;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel ;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import graphics.DrawingOnPanel;
import main.Languages;

public abstract class UtilG 
{	
	public static Point GetMousePos(JPanel panel)
 	{
 		Point panelLocation = new Point(panel.getLocationOnScreen().x, panel.getLocationOnScreen().y) ;
		return new Point(MouseInfo.getPointerInfo().getLocation().x - panelLocation.x, MouseInfo.getPointerInfo().getLocation().y - panelLocation.y) ;
 	} 	
	
	// reading file methods
	
	public static int randomIntFromTo(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max + 1) ;}
	
	public static boolean chance(double chance) { return Math.random() <= chance ;}
	
	public static int randomFromChanceList(List<Double> chances)
	{		
		int number = UtilG.randomIntFromTo(0,  100) ;
		
		int cum = 0 ;
		for (int i = 0 ; i <= chances.size() - 1; i += 1)
		{
			if (cum <= number & number <= cum + 100 * chances.get(i)) { return i ;}
			cum += 100 * chances.get(i) ;
		}
		
		return chances.size() ;
	}
	
	public static boolean isNumeric(String str) { return str.matches("-?\\d+(\\.\\d+)?") ;}  // match a number with optional '-' and decimal.
	
	public static Map<String, String[]> ReadTextFile(Languages language)
	{
		String fileName = language == Languages.portugues ? "Texto-PT-br.txt" : "Text-EN.txt" ;
		Map<String, String[]> fileContent = new HashMap<String, String[]>() ;
		
		try
		{	
			FileInputStream fileReader = new FileInputStream (fileName) ;
			InputStreamReader streamReader = new InputStreamReader(fileReader, StandardCharsets.UTF_8) ;
			BufferedReader bufferedReader = new BufferedReader(streamReader) ; 					
			String line = bufferedReader.readLine() ;
			String key = "" ;
			List<String> lineContent = new ArrayList<>() ;
			while (line != null)
			{
				if (line.contains("*"))
				{
					fileContent.put(key, lineContent.toArray(new String[lineContent.size()])) ;
					key = line ;
					lineContent = new ArrayList<>() ;
	            }
				lineContent.add(line) ;
				line = bufferedReader.readLine() ;
			}
			fileContent.put(key, lineContent.toArray(new String[lineContent.size()])) ;
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
		
		return fileContent ;
	}
	
	public static List<String[]> ReadcsvFile(String FileName)
	{
		BufferedReader br = null ;
        String line = "" ;
        String separator = "," ;
        List<String[]> fileContent = new ArrayList<String[]>() ;
        try 
        {
            br = new BufferedReader(new FileReader(FileName, Charset.forName("UTF-8"))) ;
            line = br.readLine() ;
            while ((line = br.readLine()) != null) 
            {
                fileContent.add(line.split(separator)) ;
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
            if (br == null) { return fileContent ;}
            
            try
            {
                br.close() ;
            }
            catch (IOException e)
            {
                e.printStackTrace() ;
            }
        }
        return fileContent ;
	}
	
	/*	
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
		*/
	
	public static JSONObject readJsonObject(String filePath)
    {
        JSONParser parser = new JSONParser();
        try
        {
            Object object = parser.parse(new FileReader(filePath, StandardCharsets.UTF_8));
            
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
	
	public static JSONArray readJsonArray(String filePath)
    {
        JSONParser parser = new JSONParser();
        try
        {
            Object object = parser.parse(new FileReader(filePath, StandardCharsets.UTF_8)) ;            
            JSONArray jsonObject = (JSONArray)object;
            
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

	public static void saveImage(BufferedImage img, String path) throws FileNotFoundException, IOException
    {
		File outputfile = new File(path + ".png") ;
		ImageIO.write(img, "png", outputfile) ;
    }
	
	public static Image loadImage(String filePath)
	{
		// this is not throwing an exception because it's not loading a file, it's creating a new ImageIcon
		Image image = new ImageIcon(filePath).getImage() ;
		if (image.getWidth(null) != -1 & image.getHeight(null) != -1)
		{
			return image ;
		}
		else
		{
			System.out.println("Image not found at " + filePath);
			return null ;
		}
	}
	/*
	
	// color methods
	
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
*/

	
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

	public static Dimension getSize(Image image)
	{
		if (image == null) { return new Dimension() ;}
		
		return new Dimension(image.getWidth(null), image.getHeight(null));
	}
	
	public static Color GetPixelColor(BufferedImage Bufferedimage, Point Pos)
	{
		int pixel = Bufferedimage.getRGB(Pos.x, Pos.y) ; 
		int red = (pixel & 0x00ff0000) >> 16 ;
		int green = (pixel & 0x0000ff00) >> 8 ;
		int blue = pixel & 0x000000ff ;
		return new Color(red, green, blue) ;
	}
	
	public static Color GetPixelColor(Image image, Point point)
	{
		int pixel = toBufferedImage(image).getRGB(point.x, point.y) ; 
		int red = (pixel & 0x00ff0000) >> 16 ;
		int green = (pixel & 0x0000ff00) >> 8 ;
		int blue = pixel & 0x000000ff ;
		return new Color(red, green, blue) ;
	}
	
	public static boolean isTransparent(Image image, Point point)
	{
		int pixel = toBufferedImage(image).getRGB(point.x, point.y);
		return pixel >> 24 == 0x00 ;
	}
	
	/*
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
	
	public static Image ChangeImageColor(Image image, double[] area, Color newColor, Color OriginalColor)
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
	
	
	
	

	*/
	
	public static void PlayGif(Point pos, Image gif, DrawingOnPanel DP)
	{
		DP.DrawGif(gif, pos, Align.center) ;
	}

	public static double RandomMult(double amplitude)
	{
		return (double)(Math.max(0, 1 - amplitude + 2 * amplitude * Math.random())) ;
	}

	public static Point offsetForAlignment(Align alignment, Dimension size)
	{
		if (size == null) { System.out.println("Offset from pos with null size!") ; return new Point() ;}
		
		Point offset = new Point(0, 0) ;
		switch (alignment)
		{
			case topLeft:
			{
				offset = new Point(0, 0) ;
				
				break ;
			}
			case centerLeft:
			{
				offset = new Point(0, -size.height / 2) ;
				
				break ;
			}
			case bottomLeft:
			{
				offset = new Point(0, -size.height) ;
				
				break ;
			}
			case topCenter:
			{
				offset = new Point(-size.width / 2, 0) ;
				
				break ;
			}
			case center:
			{
				offset = new Point(-size.width / 2, -size.height / 2) ;
				
				break ;
			}
			case bottomCenter:
			{
				offset = new Point(-size.width / 2, -size.height) ;
				
				break ;
			}
			case topRight:
			{
				offset = new Point(-size.width, 0) ;
				
				break ;
			}
			case centerRight:
			{
				offset = new Point(-size.width,  -size.height / 2) ;
				
				break ;
			}
			case bottomRight:
			{
				offset = new Point(-size.width,  -size.height) ;
				
				break ;
			}
		
		}
			
		return offset ;
	}

	public static Point getTopLeft(Point pos, Align alignment, Dimension size)
	{
		if (size == null) { System.out.println("getPosAt with null size!") ; return new Point() ;}
		
		Point offset = UtilG.offsetForAlignment(alignment, size) ;

		return UtilG.Translate(pos, offset.x, offset.y) ;
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

	public static Point Translate(Point originalPoint, int dx, int dy)
	{
		Point newPoint = new Point(originalPoint);
		return new Point(newPoint.x + dx, newPoint.y + dy) ;
	}
	
	public static int TextL(String Text, Font font, Graphics G)
	{
		if (G == null) { return 0 ;}
		
		FontMetrics metrics = G.getFontMetrics(font) ;
		return (int) (metrics.stringWidth(Text)) ;
	}
	
	public static int TextH(int TextSize)
	{
		return (int)(0.8*TextSize) ;
	}
		
	public static List<String> FitText(String inputstring, int NumberOfChars)
	{
		List<String> newstring = new ArrayList<String>() ;
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
		List<String> newstring2 = new ArrayList<>() ;
		for (int j = 0 ; j <= i - 1 ; j += 1)
		{
			newstring2.add(newstring.get(j)) ;
		}
		return newstring2 ;
	}
	
//	public static int IndexOf(int[] Array, int Value)
//	{
//		if (Array != null)
//		{
//			for (int i = 0 ; i <= Array.length - 1 ; i += 1)
//			{
//				if (Value == Array[i])
//				{
//					return i ;
//				}
//			}
//		}
//		return -1 ;
//	}
//
//	public static int IndexOf(String[] Vector, String Value)
//	{
//		if (Vector != null)
//		{
//			for (int i = 0 ; i <= Vector.length - 1 ; ++i)
//			{
//				if (Vector[i].equals(Value))
//				{
//					return i ;
//				}
//			}
//		}
//		return -1 ;
//	}
	
	public static double Round(double num, int decimals)
	{
		return BigDecimal.valueOf(num).setScale(decimals, RoundingMode.HALF_EVEN).doubleValue() ;
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
	
	public static Point RandomPos(Point minCoord, Dimension range, Dimension step)
	{
		return new Point((int)((range.width*Math.random() + minCoord.x)/step.width)*step.width, (int)((range.height*Math.random() + minCoord.y)/step.height)*step.height) ;
	}
	
	public static double dist1D(int PosA, int PosB)
	{
		return Math.abs(PosA - PosB) ;
	}
	
	public static double dist(Point pos1, Point pos2)
	{
		return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2)) ;
	}
	
	public static double getAngle(double[] speed)
	{
		if (speed[0] == 0)
		{
			if (0 < speed[1])
			{
				return (double) (Math.PI / 2) ;
			}
			else
			{
				return (double) (-Math.PI / 2) ;
			}
		}
		else
		{
			if (0 < speed[0])
			{
				if (0 < speed[1])	// 1st quadrant
				{
					return (double) Math.atan(speed[1] / speed[0]) ;
				}
				else				// 4th quadrant
				{
					return (double) (2 * Math.PI - Math.atan(speed[1] / speed[0])) ;
				}
			}
			else
			{
				if (0 < speed[1])	// 2st quadrant
				{
					return (double) (Math.PI - Math.atan(speed[1] / speed[0])) ;
				}
				else				// 3rd quadrant
				{
					return (double) (Math.PI + Math.atan(speed[1] / speed[0])) ;
				}
			}
		}
	}
		
	public static double calcOffset(int n, double L, double size, double spacing)
	{
		return (L - n * size - (n - 1) * spacing) / 2 ;
	}
	
	public static double spacing(double L, int n, double size, double offset)
	{
		double s = -1 ;
		
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
	
	public static boolean isInside(Point objPos, Point topLeft, Dimension size)
	{
		return (topLeft.x <= objPos.x & objPos.x <= topLeft.x + size.width &
				objPos.y <= topLeft.y + size.height & topLeft.y <= objPos.y);
	}
}
