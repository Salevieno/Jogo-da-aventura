package Graphics ;

import java.awt.AlphaComposite ;
import java.awt.BasicStroke ;
import java.awt.Color ;
import java.awt.Font ;
import java.awt.GradientPaint ;
import java.awt.Graphics2D ;
import java.awt.Image ;
import java.awt.Point;
import java.awt.geom.AffineTransform ;
import java.util.ArrayList;

import Utilities.Size;
import Utilities.UtilG;

public class DrawPrimitives 
{
	public static float OverallAngle = 0 ;
	public static int StdThickness = 1;
	private Graphics2D G ;
	
	public static Color[] ColorPalette ;
	
	public DrawPrimitives(Color[] colorPalette, Graphics2D G)
	{
		this.G = G ;
		ColorPalette = colorPalette ;
	}
	public void DrawImage(Image icon, Point Pos, String Alignment)
	{       
		if (icon != null)
		{
			int l = (int)(icon.getWidth(null)), h = (int)(icon.getHeight(null)) ;
			int[] offset = UtilG.OffsetFromPos(Alignment, l, h) ;
			G.drawImage(icon, Pos.x + offset[0], Pos.y + offset[1], l, h, null) ;
	        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "An image is being drawn outside window") ;
		}
	}
	public void DrawImage(Image icon, Point Pos, float[] scale, String Alignment)
	{       
		if (icon != null)
		{
			int l = (int)(scale[0] * icon.getWidth(null)), h = (int)(scale[1] * icon.getHeight(null)) ;
			int[] offset = UtilG.OffsetFromPos(Alignment, l, h) ;
			G.drawImage(icon, Pos.x + offset[0], Pos.y + offset[1], l, h, null) ;
	        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "An image is being drawn outside window") ;
		}
	}
	public void DrawImage(Image icon, Point Pos, float angle, float[] scale, boolean[] mirror, String Alignment, double alpha)
	{       
		if (icon != null)
		{
			int l = (int)(scale[0] * icon.getWidth(null)), h = (int)(scale[1] * icon.getHeight(null)) ;
			int[] offset = UtilG.OffsetFromPos(Alignment, l, h) ;
			int[] m = new int[] {1, 1} ;
			if (mirror[0])
			{
				m[0] = -1 ;
			}
			if (mirror[1])
			{
				m[1] = -1 ;
			}			
			AffineTransform backup = G.getTransform() ;
			G.setTransform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, Pos.x + offset[0], Pos.y + offset[1])) ;	 // Rotate image
			G.setComposite(AlphaComposite.SrcOver.derive((float) alpha)) ;
			G.drawImage(icon, Pos.x + offset[0], Pos.y + offset[1], m[0] * l, m[1] * h, null) ;
			G.setComposite(AlphaComposite.SrcOver.derive((float) 1.0)) ;
	        G.setTransform(backup) ;
	        // Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "An image is being drawn outside window") ;
		}
	}
	public void DrawGif(Image icon, Point Pos, String Alignment)
	{
		int l = (int)(icon.getWidth(null)), h = (int)(icon.getHeight(null)) ;
		int[] offset = UtilG.OffsetFromPos(Alignment, l, h) ;
		G.drawImage(icon, Pos.x + offset[0], Pos.y + offset[1], null) ;
	}
	public void DrawText(Point Pos, String Alignment, float angle, String Text, Font font, Color color)
	{
		// Rectangle by default starts at the left bottom
		int TextL = UtilG.TextL(Text, font, G), TextH = UtilG.TextH(font.getSize()) ;
		int[] offset = UtilG.OffsetFromPos(Alignment, TextL, TextH) ;
		AffineTransform backup = G.getTransform() ;		
		G.setColor(color) ;
		G.setFont(font) ;
		G.setTransform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, Pos.x, Pos.y)) ;	// Rotate text
		G.drawString(Text, Pos.x + offset[0], Pos.y + offset[1] + TextH) ;
        G.setTransform(backup) ;
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "A text is being drawn outside window") ;
	}
	public void DrawFitText(Point Pos, int sy, String Alignment, String Text, Font font, int length, Color color)
	{
		ArrayList<String> FitText = UtilG.FitText(Text, length) ;
		for (int i = 0 ; i <= FitText.size() - 1 ; i += 1)
		{
			//System.out.println(FitText[i]);
			DrawText(new Point(Pos.x, Pos.y + i*sy), Alignment, OverallAngle, FitText.get(i), font, color) ;						
		}
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL, ScreenH}, "A fit text is being drawn outside window") ;
	}
	public void DrawTextUntil(Point Pos, String Alignment, float angle, String Text, Font font, Color color, int maxlength, Point MousePos)
	{
		int[] offset = UtilG.OffsetFromPos(Alignment, maxlength, UtilG.TextH(font.getSize())) ;
		String ShortText = Text ;
		if (maxlength < Text.length())
		{
			char[] chararray = new char[maxlength - 3] ;	// This 3 is the length of "..."
			Text.getChars(0, maxlength - 4, chararray, 0) ;
			ShortText = String.valueOf(chararray) ;
		}
		if (Text.length() <= maxlength | UtilG.isInside(MousePos, new Point(Pos.x + offset[0], Pos.y + offset[1]), new Size(UtilG.TextL(ShortText, font, G), UtilG.TextH(font.getSize()))))
		{
			DrawText(Pos, Alignment, OverallAngle, Text, font, color) ;
		}
		else
		{
			int minlength = 3 ;
			maxlength = Math.max(maxlength, minlength) ;
			DrawText(Pos, Alignment, OverallAngle, ShortText + "...", font, color) ;
		}
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "A text until is being drawn outside window") ;
	}
	public void DrawLine(int[] x, int[] y, int Thickness, Color color)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(Thickness)) ;
		G.drawLine(x[0], y[0], x[1], y[1]) ;
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(new int[] {x[0], y[0]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
		//Ut.CheckIfPosIsOutsideScreen(new int[] {x[1], y[1]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
	}
	public void DrawRect(Point Pos, String Alignment, Size size, int Thickness, Color color, Color contourColor, boolean contour)
	{
		// Rectangle by default starts at the left top
		int[] offset = UtilG.OffsetFromPos(Alignment, size.x, size.y) ;
		int[] Corner = new int[] {Pos.x + offset[0], Pos.y + offset[1]} ;
		G.setStroke(new BasicStroke(Thickness)) ;
		if (color != null)
		{
			G.setColor(color) ;
			G.fillRect(Corner[0], Corner[1], size.x, size.y) ;
		}
		if (contour & contourColor != null)
		{
			G.setColor(contourColor) ;
			G.drawPolyline(new int[] {Corner[0], Corner[0] + size.x, Corner[0] + size.x, Corner[0], Corner[0]}, new int[] {Corner[1], Corner[1], Corner[1] + size.y, Corner[1] + size.y, Corner[1]}, 5) ;
		}
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "A rect is being drawn outside window") ;
	}
	public void DrawRoundRect(Point Pos, String Alignment, Size size, int Thickness, Color color0, Color color1, boolean contour)
	{
		// Round rectangle by default starts at the left top
		int ArcWidth = 10, ArcHeight = 10 ;
		int[] offset = UtilG.OffsetFromPos(Alignment, size.x, size.y) ;
		int[] Corner = new int[] {Pos.x + offset[0], Pos.y + offset[1]} ;
		G.setStroke(new BasicStroke(Thickness)) ;
		if (color0 != null & color1 != null)
		{
		    GradientPaint gradient = new GradientPaint(Corner[0], Corner[1], color0, Corner[0], Corner[1] + size.y, color1) ;
		    G.setPaint(gradient) ;
			G.fillRoundRect(Corner[0], Corner[1], size.x, size.y, ArcWidth, ArcHeight) ;
		}
		if (contour)
		{
			G.setColor(Color.black) ;
			G.drawRoundRect(Corner[0], Corner[1], size.x, size.y, ArcWidth, ArcHeight) ;
		}
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "A round rect is being drawn outside window") ;
	}
	public void DrawCircle(Point Center, int diameter, int Thickness, Color color, boolean fill, boolean contour)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(Thickness)) ;
		if (fill)
		{
			G.fillOval(Center.x - diameter/2, Center.y - diameter/2, diameter, diameter) ;
			G.setColor(Color.black) ;
		}
		if (contour)
		{
			G.drawOval(Center.x - diameter/2, Center.y - diameter/2, diameter, diameter) ;
		}
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(Center, new int[] {ScreenL, ScreenH}, "A circle is being drawn outside window") ;
	}
	public void DrawPolygon(int[] x, int[] y, int Thickness, Color color)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(Thickness)) ;
		G.fillPolygon(x, y, x.length) ;
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(new int[] {x[0], y[0]}, new int[] {ScreenL, ScreenH}, "A polygon is being drawn outside window") ;
	}
	public void DrawPolyLine(int[] x, int[] y, int Thickness, Color color)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(Thickness)) ;
		G.drawPolyline(x, y, x.length) ;
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(new int[] {x[0], y[0]}, new int[] {ScreenL, ScreenH}, "A polyline is being drawn outside window") ;
	}
}
