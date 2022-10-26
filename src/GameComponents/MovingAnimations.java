package GameComponents;

import java.awt.Image;

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
}
