package screen;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import main.GamePanel;

public class SkyComponent
{
	private final Image image ;
	private Point pos ;
	private final Point speed ;
	private final Dimension size ;
	private int counter ;
	
	public SkyComponent(Image image, Point pos, Point speed)
	{
		this.image = image ;
		this.pos = pos ;
		this.speed = speed ;
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
	public Point getSpeed() {return speed ;}
	public Dimension getSize() {return size ;}
	public int getCounter() {return counter ;}
	public void setPos(Point P) {pos = P ;}
	public void setCounter(int C) {counter = C ;}

	
	public void move()
	{
		setPos(new Point(pos.x + speed.x, pos.y + speed.y)) ;
	}
	
	public void display(double angle, double alpha)
	{
		GamePanel.DP.drawImage(image, pos, angle, Scale.unit, false, false, Align.topLeft, alpha) ;
	}
}
