package liveBeings;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import main.GamePanel;
import utilities.Directions;

public class MovingAnimations
{
	public final Image idleGif ;
	public final Image movingUpGif ;
	public final Image movingDownGif ;
	public final Image movingLeftGif ;
	public final Image movingRightGif ;

	public final SpriteAnimation movingRightAni ;
	
	public MovingAnimations(Image idleGif, Image movingUpGif, Image movingDownGif, Image movingLeftGif, Image movingRightGif, String pathMovingRightSheet)
	{
		this.idleGif = idleGif ;
		this.movingUpGif = movingUpGif ;
		this.movingDownGif = movingDownGif ;
		this.movingLeftGif = movingLeftGif ;
		this.movingRightGif = movingRightGif ;
		this.movingRightAni = new SpriteAnimation(pathMovingRightSheet, new Point(0, 0), Align.bottomCenter, new Dimension(46, 78), 6, 5) ;
	}
	
	public void displayIdle(Point pos, double angle, Scale scale, Align align)
	{
		GamePanel.DP.drawImage(idleGif, pos, angle, scale, align) ;
	}
	
	public void displayMoving(Directions direction, Point pos, double angle, Scale scale, Align align)
	{
		switch (direction)
		{
			case up: GamePanel.DP.drawImage(movingUpGif, pos, angle, scale, align) ; return ;
			case down: GamePanel.DP.drawImage(movingDownGif, pos, angle, scale, align) ; return ;
			case left: GamePanel.DP.drawImage(movingLeftGif, pos, angle, scale, align) ; return ;
			case right: movingRightAni.display(GamePanel.DP, pos, align) ; return ;
		}
	}
}
