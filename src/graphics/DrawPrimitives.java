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
import java.util.List;

import libUtil.Align;
import libUtil.Util;
import main.Game;
import utilities.Scale;

public class DrawPrimitives
{
	private Graphics2D graphs ;
	
	public static double stdAngle ;
	public static int stdStroke ;
	public static Font stdFont ;

	static
	{
		stdAngle = 0 ;
		stdStroke = 1;
		stdFont = new Font("Comics", Font.BOLD, 13) ;
	}
	
	public void setGraphics(Graphics2D graph2D)
	{
		this.graphs = graph2D ;
	}

	public void setStdAngle(double newAngle) { stdAngle = newAngle ;}
	public void setStdFont(Font newFont) { stdFont = newFont ;}
	public void setStdStroke(int newStroke) { stdStroke = newStroke ;}
	
	public int TextL(String text, Font font)
	{
		if (graphs == null) { return 0 ;}
		
		FontMetrics metrics = graphs.getFontMetrics(font) ;
		return metrics.stringWidth(text) ;
	}
	
	public void drawText(Point pos, Align align, double angle, String text, Font font, Color color)
	{
		// by default starts at the left bottom
		Dimension size = new Dimension(TextL(text, font), Util.TextH(font.getSize())) ;
		Point offset = Util.offsetForAlignment(align, size) ;
		AffineTransform backup = graphs.getTransform() ;		
		
		graphs.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;

		graphs.setColor(color) ;
		graphs.setFont(font) ;
		graphs.drawString(text, pos.x + offset.x, pos.y + offset.y + size.height) ;
        
		graphs.setTransform(backup) ;
	}
	
	public void drawText(Point pos, Align align, String text, Color color)
	{
		drawText(pos, align, stdAngle, text, stdFont, color) ;
	}
		
	public void drawBufferedText(Point pos, Align align, double angle, String text, Font font, Color color, Color outlineColor, int outlineWidth)
	{
		// by default starts at the left bottom
		Dimension size = new Dimension(TextL(text, font), Util.TextH(font.getSize())) ;
		Point offset = Util.offsetForAlignment(align, size) ;
		AffineTransform backup = graphs.getTransform() ;
				
		graphs.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;

		graphs.setFont(font) ;
		graphs.setColor(outlineColor) ;
		graphs.drawString(text, pos.x + offset.x - outlineWidth, pos.y + offset.y + size.height - outlineWidth) ;
		graphs.drawString(text, pos.x + offset.x - outlineWidth, pos.y + offset.y + size.height + outlineWidth) ;
		graphs.drawString(text, pos.x + offset.x + outlineWidth, pos.y + offset.y + size.height - outlineWidth) ;
		graphs.drawString(text, pos.x + offset.x + outlineWidth, pos.y + offset.y + size.height + outlineWidth) ;
		
		graphs.setColor(color) ;
		graphs.drawString(text, pos.x + offset.x, pos.y + offset.y + size.height) ;
        
		graphs.setTransform(backup) ;
	}
	
	public void drawLine(Point p1, Point p2, int stroke, Color color)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		
		graphs.drawLine(p1.x, p1.y, p2.x, p2.y) ;
		
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawLine(Point p1, Point p2, Color color)
	{
		drawLine(p1, p2, stdStroke, color) ;
	}
	
	public void drawArc(Point center, int diameter, int stroke, int startAngle, int endAngle, Color color, Color contourColor)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graphs.fillArc(center.x - diameter / 2, center.y - diameter / 2, diameter, diameter, startAngle, endAngle) ;
		}
		if (contourColor != null)
		{
			graphs.setColor(contourColor) ;
			graphs.drawArc(center.x - diameter / 2, center.y - diameter / 2, diameter, diameter, startAngle, endAngle) ;
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
	public void drawPolyLine(int[] x, int[] y, Color color)
	{
		drawPolyLine(x, y, stdStroke, color) ;
	}
	public void drawPolyLine(List<Integer> x, List<Integer> y, Color color)
	{
		drawPolyLine(x.stream().mapToInt(Integer::intValue).toArray(), y.stream().mapToInt(Integer::intValue).toArray(), stdStroke, color) ;
	}
	
	public void drawRect(Point pos, Align align, Dimension size, int stroke, Color color, Color contourColor, double opacity)
	{
		// Rectangle by default starts at the left top
		Point offset = Util.offsetForAlignment(align, size) ;
		graphs.setComposite(AlphaComposite.SrcOver.derive((float) opacity)) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graphs.setColor(color) ;
			graphs.fillRect(pos.x + offset.x, pos.y + offset.y, size.width, size.height) ;
		}
		if (contourColor != null)
		{
			graphs.setColor(contourColor) ;
			graphs.drawRect(pos.x + offset.x, pos.y + offset.y, size.width, size.height) ;
		}
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawRect(Point pos, Align align, Dimension size, Color color, Color contourColor)
	{
		drawRect(pos, align, size, stdStroke, color, contourColor, 1.0) ;
	}
	public void drawRect(Point pos, Align align, Dimension size, Color color, Color contourColor, double opacity)
	{
		drawRect(pos, align, size, stdStroke, color, contourColor, opacity) ;
	}
	public void drawRoundRect(Point pos, Align align, Dimension size, int stroke, Color fillColor, Color contourColor, boolean contour, int arcWidth, int arcHeight)
	{
		drawGradRoundRect(pos, align, size, stroke, arcWidth, arcHeight, fillColor, fillColor, contourColor, contour) ;
	}
	public void drawRoundRect(Point pos, Align align, Dimension size, int stroke, Color fillColor, Color contourColor, boolean contour)
	{
		drawRoundRect(pos, align, size, stroke, fillColor, contourColor, contour, 10, 10) ;
	}
	public void drawRoundRect(Point pos, Align align, Dimension size, Color fillColor, Color contourColor, boolean contour)
	{
		drawRoundRect(pos, align, size, stdStroke, fillColor, contourColor, contour, 10, 10) ;
	}
	
	public void drawGradRoundRect(Point pos, Align align, Dimension size, int stroke, int arcW, int arcH, Color botColor, Color topColor, Color contourColor, boolean contour)
	{
		// Round rectangle by default starts at the left top
		Point offset = Util.offsetForAlignment(align, size) ;
		int[] corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (topColor != null & botColor != null)
		{
		    GradientPaint gradient = new GradientPaint(corner[0], corner[1], topColor, corner[0], corner[1] + size.height, botColor) ;
		    graphs.setPaint(gradient) ;
			graphs.fillRoundRect(corner[0], corner[1], size.width, size.height, arcW, arcH) ;
		}
		if (contour & contourColor != null)
		{
			graphs.setColor(contourColor) ;
			graphs.drawRoundRect(corner[0], corner[1], size.width, size.height, arcW, arcH) ;
		}
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawGradRoundRect(Point pos, Align align, Dimension size, int stroke, Color botColor, Color topColor, Color contourColor, boolean contour)
	{
		drawGradRoundRect(pos, align, size, stroke, 10, 10, botColor, topColor, contourColor, contour) ;
	}
	public void drawGradRoundRect(Point pos, Align align, Dimension size, Color botColor, Color topColor, Color contourColor, boolean contour)
	{
		drawGradRoundRect(pos, align, size, stdStroke, botColor, topColor, contourColor, contour) ;
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
	public void drawCircle(Point center, int diameter, Color color, Color contourColor)
	{
		drawCircle(center, diameter, stdStroke, color, contourColor) ;
	}
	public void drawPolygon(int[] x, int[] y, int stroke, Color color)
	{
		graphs.setColor(color) ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		graphs.fillPolygon(x, y, x.length) ;
		graphs.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void drawPolygon(int[] x, int[] y, Color color)
	{
		drawPolygon(x, y, stdStroke, color) ;
	}

	public void drawImage(Image image, Point pos, Align align)
	{
		drawImage(image, pos, 0, Scale.unit, false, false, align, 1) ;
	}
	public void drawImage(Image image, Point pos, Align align, double opacity)
	{
		drawImage(image, pos, 0, Scale.unit, false, false, align, opacity) ;
	}
	public void drawImage(Image image, Point pos, Scale scale, Align align)
	{
		drawImage(image, pos, 0, scale, false, false, align, 1) ;
	}
	public void drawImage(Image image, Point pos, double angle, Scale scale, Align align)
	{
		drawImage(image, pos, angle, scale, false, false, align, 1) ;
	}
	public void drawImage(Image image, Point pos, double angle, Scale scale, Align align, double opacity)
	{
		drawImage(image, pos, angle, scale, false, false, align, opacity) ;
	}
	public void drawImage(Image image, Point pos, double angle, Scale scale, boolean flipH, boolean flipV, Align align, double opacity)
	{       
		if (image == null) { System.out.println("Tentando desenhar imagem nula na pos " + pos) ; return ; }
		
		Dimension size = new Dimension((int)(scale.x * image.getWidth(null)), (int)(scale.y * image.getHeight(null))) ;
		size = new Dimension ((!flipH ? 1 : -1) * size.width, (!flipV ? 1 : -1) * size.height) ;
		Point offset = Util.offsetForAlignment(align, size) ;
		AffineTransform backup = graphs.getTransform() ;
		graphs.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;
		graphs.setComposite(AlphaComposite.SrcOver.derive((float) opacity)) ;
		
		graphs.drawImage(image, pos.x + offset.x, pos.y + offset.y, size.width, size.height, null) ;
		
		graphs.setComposite(AlphaComposite.SrcOver.derive((float) 1.0)) ;
        graphs.setTransform(backup) ;
	}

}
