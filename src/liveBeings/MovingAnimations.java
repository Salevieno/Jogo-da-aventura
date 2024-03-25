package liveBeings;

import java.awt.Image;
import java.awt.Point;

import graphics.DrawPrimitives;
import utilities.Align;
import utilities.Directions;
import utilities.Scale;

public class MovingAnimations
{
	public Image idleGif, movingUpGif, movingDownGif, movingLeftGif, movingRightGif ;
	
	public MovingAnimations(Image idleGif, Image movingUpGif, Image movingDownGif, Image movingLeftGif, Image movingRightGif)
	{
		this.idleGif = idleGif ;
		this.movingUpGif = movingUpGif ;
		this.movingDownGif = movingDownGif ;
		this.movingLeftGif = movingLeftGif ;
		this.movingRightGif = movingRightGif ;
	}
	
	public void displayIdle(Point pos, double angle, Scale scale, Align align, DrawPrimitives DP)
	{
		DP.drawImage(idleGif, pos, angle, scale, align) ;
	}
	
	public void displayMoving(Directions direction, Point pos, double angle, Scale scale, Align align, DrawPrimitives DP)
	{
		switch (direction)
		{
			case up: DP.drawImage(movingUpGif, pos, angle, scale, align) ; break ;
			case down: DP.drawImage(movingDownGif, pos, angle, scale, align) ; break ;
			case left: DP.drawImage(movingLeftGif, pos, angle, scale, align) ; break ;
			case right: DP.drawImage(movingRightGif, pos, angle, scale, align) ; break ;
		}
	}
}
