package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import utilities.AlignmentPoints;
import utilities.Scale;

public class SkyComponent
{
	private Image image ;
	private Point pos ;
	private int[] speed ;
	private Dimension size ;
	private Color[] color ;
	private int counter ;
	
	public SkyComponent(Image image, Point pos, int[] speed, Color[] color)
	{
		this.image = image ;
		this.pos = pos ;
		this.speed = speed ;
		this.color = color ;
		size = new Dimension(0, 0) ;
		if (image != null)
		{
			size.width = image.getWidth(null) ;
			size.height = image.getHeight(null) ;
		}
		counter = 0 ;
	}	

	public Image getImage() {return image ;}
	public Point getPos() {return pos ;}
	public int[] getSpeed() {return speed ;}
	public Dimension getSize() {return size ;}
	public Color[] getColor() {return color ;}
	public int getCounter() {return counter ;}
	public void setImage(Image I) {image = I ;}
	public void setPos(Point P) {pos = P ;}
	public void setSpeed(int[] S) {speed = S ;}
	public void setColor(Color[] C) {color = C ;}
	public void setCounter(int C) {counter = C ;}
	

	public void IncCounter(int MaxCounter)
	{
		counter = (counter + 1) % MaxCounter ;
	}
	public void display(double angle, DrawingOnPanel DP)
	{
		DP.DrawImage(image, pos, angle, new Scale(1, 1), AlignmentPoints.topLeft) ;
	}
}
