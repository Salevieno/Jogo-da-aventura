package liveBeings;

import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import main.GamePanel;
import utilities.Directions;

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
	
	public void displayIdle(Point pos, double angle, Scale scale, Align align)
	{
		GamePanel.DP.drawImage(idleGif, pos, angle, scale, align) ;
	}
	
	public void displayMoving(Directions direction, Point pos, double angle, Scale scale, Align align)
	{
		switch (direction)
		{
			case up: GamePanel.DP.drawImage(movingUpGif, pos, angle, scale, align) ; break ;
			case down: GamePanel.DP.drawImage(movingDownGif, pos, angle, scale, align) ; break ;
			case left: GamePanel.DP.drawImage(movingLeftGif, pos, angle, scale, align) ; break ;
			case right: GamePanel.DP.drawImage(movingRightGif, pos, angle, scale, align) ; break ;
		}
	}
}
