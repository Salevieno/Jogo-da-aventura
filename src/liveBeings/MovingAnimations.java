package liveBeings;

import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
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
	
	public void display(Directions direction, Point feetPos, double angle, Scale scale, DrawingOnPanel DP)
	{
		switch (direction)
		{
			case up: DP.DrawImage(movingUpGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
			case down: DP.DrawImage(movingDownGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
			case left: DP.DrawImage(movingLeftGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
			case right: DP.DrawImage(movingRightGif, feetPos, angle, scale, Align.bottomCenter) ; break ;
		}
	}
}
