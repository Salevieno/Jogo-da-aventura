package liveBeings;

import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import main.GamePanel;
import utilities.Directions;

public class MovingAnimations
{
	public final SpriteAnimation spriteIdle ;
	public final SpriteAnimation spriteMovingUp ;
	public final SpriteAnimation spriteMovingDown ;
	public final SpriteAnimation spriteMovingLeft ;
	public final SpriteAnimation spriteMovingRight ;

	public MovingAnimations(SpriteAnimation spriteIdle, SpriteAnimation spriteMovingUp, SpriteAnimation spriteMovingDown, SpriteAnimation spriteMovingLeft, SpriteAnimation spriteMovingRight)
	{
		this.spriteIdle = spriteIdle ;
		this.spriteMovingUp = spriteMovingUp ;
		this.spriteMovingDown = spriteMovingDown ;
		this.spriteMovingLeft = spriteMovingLeft ;
		this.spriteMovingRight = spriteMovingRight ;
	}
	
	public void displayIdle(Point pos, double angle, Scale scale, Align align)
	{
		spriteIdle.display(GamePanel.DP, pos, align) ;
	}
	
	public void displayMoving(Directions direction, Point pos, double angle, Scale scale, Align align)
	{
		switch (direction)
		{
			case up: spriteMovingUp.display(GamePanel.DP, pos, align) ; return ;
			case down: spriteMovingDown.display(GamePanel.DP, pos, align) ; return ;
			case left: spriteMovingLeft.display(GamePanel.DP, pos, align) ; return ;
			case right: spriteMovingRight.display(GamePanel.DP, pos, align) ; return ;
		}
	}
}
