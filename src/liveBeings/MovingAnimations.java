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
	
	public void display(Directions direction, Point feetPos, double angle, Scale scale, DrawPrimitives DP)
	{
		switch (direction)
		{
			case up: DP.drawImage(movingUpGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
			case down: DP.drawImage(movingDownGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
			case left: DP.drawImage(movingLeftGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
			case right: DP.drawImage(movingRightGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
		}
	}
}
