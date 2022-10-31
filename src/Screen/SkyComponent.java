package Screen;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import Graphics.DrawPrimitives;
import Utilities.Size;

public class SkyComponent
{
	private Image image ;
	private Point pos ;
	private float[] speed ;
	private Size size ;
	private Color[] color ;
	private int counter ;
	
	public SkyComponent(Image image, Point pos, float[] speed, Color[] color)
	{
		this.image = image ;
		this.pos = pos ;
		this.speed = speed ;
		this.color = color ;
		size = new Size(0, 0) ;
		if (image != null)
		{
			size.x = image.getWidth(null) ;
			size.y = image.getHeight(null) ;
		}
		counter = 0 ;
	}	

	public Image getImage() {return image ;}
	public Point getPos() {return pos ;}
	public float[] getSpeed() {return speed ;}
	public Size getSize() {return size ;}
	public Color[] getColor() {return color ;}
	public int getCounter() {return counter ;}
	public void setImage(Image I) {image = I ;}
	public void setPos(Point P) {pos = P ;}
	public void setSpeed(float[] S) {speed = S ;}
	public void setColor(Color[] C) {color = C ;}
	public void setCounter(int C) {counter = C ;}
	

	public void IncCounter(int MaxCounter)
	{
		counter = (counter + 1) % MaxCounter ;
	}
	public void display(float angle, DrawPrimitives DP)
	{
		DP.DrawImage(image, pos, angle, new float[] {1, 1}, new boolean[] {false, false}, "TopLeft", 1) ;
	}
}
