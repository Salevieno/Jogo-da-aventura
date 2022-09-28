package Graphics ;

import java.awt.AlphaComposite ;
import java.awt.BasicStroke ;
import java.awt.Color ;
import java.awt.Font ;
import java.awt.GradientPaint ;
import java.awt.Graphics2D ;
import java.awt.Image ;
import java.awt.geom.AffineTransform ;
import Main.Utg ;

public class DrawPrimitives 
{
	public static float OverallAngle = 0 ;
	private int StdThickness ;
	private Graphics2D G ;
	
	public static Color[] ColorPalette ;
	
	public DrawPrimitives(int StdThickness, Color[] colorPalette, Graphics2D G)
	{
		this.StdThickness = StdThickness ;
		this.G = G ;
		ColorPalette = colorPalette ;
	}
	public void DrawImage(Image icon, int[] Pos, String Alignment)
	{       
		if (icon != null)
		{
			int l = (int)(icon.getWidth(null)), h = (int)(icon.getHeight(null)) ;
			int[] offset = Utg.OffsetFromPos(Alignment, l, h) ;
			G.drawImage(icon, Pos[0] + offset[0], Pos[1] + offset[1], l, h, null) ;
	        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "An image is being drawn outside window") ;
		}
	}
	public void DrawImage(Image icon, int[] Pos, float[] scale, String Alignment)
	{       
		if (icon != null)
		{
			int l = (int)(scale[0] * icon.getWidth(null)), h = (int)(scale[1] * icon.getHeight(null)) ;
			int[] offset = Utg.OffsetFromPos(Alignment, l, h) ;
			G.drawImage(icon, Pos[0] + offset[0], Pos[1] + offset[1], l, h, null) ;
	        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "An image is being drawn outside window") ;
		}
	}
	public void DrawImage(Image icon, int[] Pos, float angle, float[] scale, boolean[] mirror, String Alignment, double alpha)
	{       
		if (icon != null)
		{
			int l = (int)(scale[0] * icon.getWidth(null)), h = (int)(scale[1] * icon.getHeight(null)) ;
			int[] offset = Utg.OffsetFromPos(Alignment, l, h) ;
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
			G.setTransform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, Pos[0] + offset[0], Pos[1] + offset[1])) ;	 // Rotate image
			G.setComposite(AlphaComposite.SrcOver.derive((float) alpha)) ;
			G.drawImage(icon, Pos[0] + offset[0], Pos[1] + offset[1], m[0] * l, m[1] * h, null) ;
			G.setComposite(AlphaComposite.SrcOver.derive((float) 1.0)) ;
	        G.setTransform(backup) ;
	        // Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "An image is being drawn outside window") ;
		}
	}
	public void DrawGif(Image icon, int[] Pos, String Alignment)
	{
		int l = (int)(icon.getWidth(null)), h = (int)(icon.getHeight(null)) ;
		int[] offset = Utg.OffsetFromPos(Alignment, l, h) ;
		G.drawImage(icon, Pos[0] + offset[0], Pos[1] + offset[1], null) ;
	}
	public void DrawText(int[] Pos, String Alignment, float angle, String Text, Font font, Color color)
	{
		// Rectangle by default starts at the left bottom
		int TextL = Utg.TextL(Text, font, G), TextH = Utg.TextH(font.getSize()) ;
		int[] offset = Utg.OffsetFromPos(Alignment, TextL, TextH) ;
		AffineTransform backup = G.getTransform() ;		
		G.setColor(color) ;
		G.setFont(font) ;
		G.setTransform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, Pos[0], Pos[1])) ;	// Rotate text
		G.drawString(Text, Pos[0] + offset[0], Pos[1] + offset[1] + TextH) ;
        G.setTransform(backup) ;
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "A text is being drawn outside window") ;
	}
	public void DrawFitText(int[] Pos, int sy, String Alignment, String Text, Font font, int length, Color color)
	{
		String[] FitText = Utg.FitText(Text, length) ;
		for (int i = 0 ; i <= FitText.length - 1 ; i += 1)
		{
			//System.out.println(FitText[i]);
			DrawText(new int[] {Pos[0], Pos[1] + i*sy}, Alignment, OverallAngle, FitText[i], font, color) ;						
		}
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL, ScreenH}, "A fit text is being drawn outside window") ;
	}
	public void DrawTextUntil(int[] Pos, String Alignment, float angle, String Text, Font font, Color color, int maxlength, int[] MousePos)
	{
		int[] offset = Utg.OffsetFromPos(Alignment, maxlength, Utg.TextH(font.getSize())) ;
		String ShortText = Text ;
		if (maxlength < Text.length())
		{
			char[] chararray = new char[maxlength - 3] ;	// This 3 is the length of "..."
			Text.getChars(0, maxlength - 4, chararray, 0) ;
			ShortText = String.valueOf(chararray) ;
		}
		if (Text.length() <= maxlength | Utg.isInside(MousePos, new int[] {Pos[0] + offset[0], Pos[1] + offset[1]}, Utg.TextL(ShortText, font, G), Utg.TextH(font.getSize())))
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
	public void DrawRect(int[] Pos, String Alignment, int l, int h, int Thickness, Color color, Color contourColor, boolean contour)
	{
		// Rectangle by default starts at the left top
		int[] offset = Utg.OffsetFromPos(Alignment, l, h) ;
		int[] Corner = new int[] {Pos[0] + offset[0], Pos[1] + offset[1]} ;
		G.setStroke(new BasicStroke(Thickness)) ;
		if (color != null)
		{
			G.setColor(color) ;
			G.fillRect(Corner[0], Corner[1], l, h) ;
		}
		if (contour & contourColor != null)
		{
			G.setColor(contourColor) ;
			G.drawPolyline(new int[] {Corner[0], Corner[0] + l, Corner[0] + l, Corner[0], Corner[0]}, new int[] {Corner[1], Corner[1], Corner[1] + h, Corner[1] + h, Corner[1]}, 5) ;
		}
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "A rect is being drawn outside window") ;
	}
	public void DrawRoundRect(int[] Pos, String Alignment, int l, int h, int Thickness, Color color0, Color color1, boolean contour)
	{
		// Round rectangle by default starts at the left top
		int ArcWidth = 10, ArcHeight = 10 ;
		int[] offset = Utg.OffsetFromPos(Alignment, l, h) ;
		int[] Corner = new int[] {Pos[0] + offset[0], Pos[1] + offset[1]} ;
		G.setStroke(new BasicStroke(Thickness)) ;
		if (color0 != null & color1 != null)
		{
		    GradientPaint gradient = new GradientPaint(Corner[0], Corner[1], color0, Corner[0], Corner[1] + h, color1) ;
		    G.setPaint(gradient) ;
			G.fillRoundRect(Corner[0], Corner[1], l, h, ArcWidth, ArcHeight) ;
		}
		if (contour)
		{
			G.setColor(Color.black) ;
			G.drawRoundRect(Corner[0], Corner[1], l, h, ArcWidth, ArcHeight) ;
		}
		G.setStroke(new BasicStroke(StdThickness)) ;
        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "A round rect is being drawn outside window") ;
	}
	public void DrawCircle(int[] Center, int size, int Thickness, Color color, boolean fill, boolean contour)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(Thickness)) ;
		if (fill)
		{
			G.fillOval(Center[0] - size/2, Center[1] - size/2, size, size) ;
			G.setColor(Color.black) ;
		}
		if (contour)
		{
			G.drawOval(Center[0] - size/2, Center[1] - size/2, size, size) ;
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
