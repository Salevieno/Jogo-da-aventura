package screen;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import utilities.Util;

public class SkyComponent
{
	private Point2D.Double pos ;
	private double opacity ;
	private final Image image ;
	private final Point2D.Double speed ;
	private final Dimension size ;
	
	public SkyComponent(Image image, Point2D.Double pos, Point2D.Double speed)
	{
		this.pos = pos ;
		this.opacity = 1.0 ;
		this.image = image ;
		this.speed = speed ;
		size = Util.getSize(image) ;
	}	

	public Image getImage() {return image ;}
	public Point2D.Double getPos() {return pos ;}
	public Point2D.Double getSpeed() {return speed ;}
	public Dimension getSize() {return size ;}
	public void setPos(Point2D.Double P) {pos = P ;}
	public void setOpacity(double opacity) { this.opacity = opacity ;}
	
	public void move(double dt)
	{
		pos.x += speed.x * dt ;
		pos.y += speed.y * dt ;
	}
	
	public void display(double angle)
	{
		Point posAsPoint = new Point((int) pos.x, (int) pos.y) ;
		GamePanel.DP.drawSubImage(image, Game.getScreen().getSize().width, posAsPoint, angle, Scale.unit, false, false, Align.topLeft, opacity) ;
	}
	
	public void display()
	{
		display(Draw.stdAngle) ;
	}
}
