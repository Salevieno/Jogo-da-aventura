package graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class DrawPrimitives
{
	private Graphics2D graphs ;
	
	public static double stdAngle ;
	public static final int stdStroke ;
	public static final Font stdFont ;

	static
	{
		stdAngle = 0 ;
		stdStroke = 1;
		stdFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
	}
	
	public void setGraphics(Graphics2D graph2D)
	{
		this.graphs = graph2D ;
	}
	
	public int TextL(String text, Font font)
	{
		if (graphs == null) { return 0 ;}
		
		FontMetrics metrics = graphs.getFontMetrics(font) ;
		return metrics.stringWidth(text) ;
	}
	
	public void drawText(Point pos, Align align, double angle, String text, Font font, Color color)
	{
		// by default starts at the left bottom
		Dimension size = new Dimension(TextL(text, font), UtilG.TextH(font.getSize())) ;
		Point offset = UtilG.offsetForAlignment(align, size) ;
		AffineTransform backup = graphs.getTransform() ;		
		
		graphs.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;

		graphs.setColor(color) ;
		graphs.setFont(font) ;
		graphs.drawString(text, pos.x + offset.x, pos.y + offset.y + size.height) ;
        
		graphs.setTransform(backup) ;
	}
	
	public void drawLine(Point p1, Point p2, int stroke, Color color)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		
		graphs.drawLine(p1.x, p1.y, p2.x, p2.y) ;
		
		graphs.setStroke(new BasicStroke(stdStroke)) ;
        //Ut.CheckIfPosIsOutsideScreen(new int[] {x[0], y[0]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
		//Ut.CheckIfPosIsOutsideScreen(new int[] {x[1], y[1]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
	}
	public void drawArc(Point center, int diameter, int stroke, int startAngle, int endAngle, Color color, Color contourColor)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graphs.fillArc(center.x - diameter/2, center.y - diameter/2, diameter, diameter, startAngle, endAngle) ;
		}
		if (contourColor != null)
		{
			graphs.setColor(contourColor) ;
			graphs.drawArc(center.x - diameter/2, center.y - diameter/2, diameter, diameter, startAngle, endAngle) ;
		}
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawPolyLine(int[] x, int[] y, int stroke, Color color)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		graphs.drawPolyline(x, y, x.length) ;
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	
	public void drawRect(Point pos, Align align, Dimension size, int stroke, Color color, Color contourColor)
	{
		// Rectangle by default starts at the left top
		Point offset = UtilG.offsetForAlignment(align, size) ;
		int[] Corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graphs.setColor(color) ;
			graphs.fillRect(Corner[0], Corner[1], size.width, size.height) ;
		}
		if (contourColor != null)
		{
			//int[] xPoints = new int[] {Corner[0], Corner[0] + size.width, Corner[0] + size.width, Corner[0], Corner[0]} ;
			//int[] yPoints = new int[] {Corner[1], Corner[1], Corner[1] + size.height, Corner[1] + size.height, Corner[1]} ;
			graphs.setColor(contourColor) ;
			graphs.drawRect(Corner[0], Corner[1], size.width, size.height) ;
			//G.drawPolyline(xPoints, yPoints, xPoints.length) ;
		}
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawRoundRect(Point pos, Align align, Dimension size, int stroke, Color fillColor, boolean contour)
	{
		drawRoundRect(pos, align, size, stroke, fillColor, contour, 10, 10) ;
	}
	public void drawRoundRect(Point pos, Align align, Dimension size, int stroke, Color fillColor, boolean contour, int arcWidth, int arcHeight)
	{
		// Round rectangle by default starts at the left top
		Point offset = UtilG.offsetForAlignment(align, size) ;
		int[] Corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (fillColor != null)
		{
		    graphs.setColor(fillColor) ;
			graphs.fillRoundRect(Corner[0], Corner[1], size.width, size.height, arcWidth, arcHeight) ;
		}
		if (contour)
		{
			graphs.setColor(Game.colorPalette[0]) ;
			graphs.drawRoundRect(Corner[0], Corner[1], size.width, size.height, arcWidth, arcHeight) ;
		}
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawGradRoundRect(Point pos, Align align, Dimension size, int stroke, Color topColor, Color botColor, boolean contour)
	{
		// Round rectangle by default starts at the left top
		int ArcWidth = 10, ArcHeight = 10 ;
		Point offset = UtilG.offsetForAlignment(align, size) ;
		int[] Corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (topColor != null & botColor != null)
		{
		    GradientPaint gradient = new GradientPaint(Corner[0], Corner[1], topColor, Corner[0], Corner[1] + size.height, botColor) ;
		    graphs.setPaint(gradient) ;
			graphs.fillRoundRect(Corner[0], Corner[1], size.width, size.height, ArcWidth, ArcHeight) ;
		}
		if (contour)
		{
			graphs.setColor(Game.colorPalette[0]) ;
			graphs.drawRoundRect(Corner[0], Corner[1], size.width, size.height, ArcWidth, ArcHeight) ;
		}
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawCircle(Point center, int diameter, int stroke, Color color, Color contourColor)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graphs.fillOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		if (contourColor != null)
		{
			graphs.setColor(contourColor) ;
			graphs.drawOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawPolygon(int[] x, int[] y, int stroke, Color color)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		graphs.fillPolygon(x, y, x.length) ;
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}

	public void drawImage(Image image, Point pos, Align align)
	{
		drawImage(image, pos, 0, Scale.unit, false, false, align, 1) ;
	}
	public void drawImage(Image image, Point pos, Scale scale, Align align)
	{
		drawImage(image, pos, 0, scale, false, false, align, 1) ;
	}
	public void drawImage(Image image, Point pos, double angle, Scale scale, Align align)
	{
		drawImage(image, pos, angle, scale, false, false, align, 1) ;
	}
	public void drawImage(Image image, Point pos, double angle, Scale scale, boolean flipH, boolean flipV, Align align, double alpha)
	{       
		if (image == null) { System.out.println("Tentando desenhar imagem nula na pos " + pos) ; return ; }
		
		Dimension size = new Dimension((int)(scale.x * image.getWidth(null)), (int)(scale.y * image.getHeight(null))) ;
		size = new Dimension ((!flipH ? 1 : -1) * size.width, (!flipV ? 1 : -1) * size.height) ;
		Point offset = UtilG.offsetForAlignment(align, size) ;
		AffineTransform backup = graphs.getTransform() ;
		graphs.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;
		graphs.setComposite(AlphaComposite.SrcOver.derive((float) alpha)) ;
		
		graphs.drawImage(image, pos.x + offset.x, pos.y + offset.y, size.width, size.height, null) ;
		
		graphs.setComposite(AlphaComposite.SrcOver.derive((float) 1.0)) ;
        graphs.setTransform(backup) ;
	}

}
