package liveBeings;

import java.awt.geom.Point2D;

import main.Directions;

public enum MovePattern
{
    pattern0(1.5),
    pattern1(3.0),
	pattern2(5.0),
	pattern3(5.0);

	private final double duration ;
	private MovePattern(double duration)
	{
		this.duration = duration ;
	}

	public double getDuration() { return duration ;}
	
    public static Point2D.Double calcNewPos(MovePattern movePattern, Directions dir, Point2D.Double currentPos, int step, double dt)
    {
        return calcNewPos(movePattern, dir, step, dt, currentPos, 0.0) ;
    }

    public static Point2D.Double calcNewPos(double angle, Point2D.Double currentPos, int step)
	{
		Point2D.Double newPos = new Point2D.Double((int) (currentPos.x + step * Math.cos(angle)), (int) (currentPos.y + step * Math.sin(angle))) ;
		return newPos ;
	}

    public static Point2D.Double calcNewPos(Directions dir, Point2D.Double currentPos, int step)
	{
		Point2D.Double newPos = switch (dir)
		{
			case up -> calcNewPos(3 * Math.PI / 2, currentPos, step) ;
			case down -> calcNewPos(Math.PI / 2, currentPos, step) ;
			case left -> calcNewPos(Math.PI, currentPos, step) ;
			case right -> calcNewPos(0, currentPos, step) ;
		} ;
		
		return newPos ;
	}

	public static Point2D.Double calcNewPos(MovePattern pattern, Directions dir, int step, double dt, Point2D.Double currentPos, double moveRate)
	{
		Point2D.Double speed = calcSpeed(pattern, moveRate) ;

		Point2D.Double newPos = switch (dir)
		{
			case up -> new Point2D.Double(currentPos.x - step * dt * speed.y, currentPos.y - step * dt * speed.x) ;
			case down -> new Point2D.Double(currentPos.x - step * dt * speed.y, currentPos.y + step * dt * speed.x) ;
			case left -> new Point2D.Double(currentPos.x - step * dt * speed.x, currentPos.y - step * dt * speed.y) ;
			case right -> new Point2D.Double(currentPos.x + step * dt * speed.x, currentPos.y - step * dt * speed.y) ;
		} ;
		
		return newPos ;
	}

	private static Point2D.Double calcSpeed(MovePattern pattern, double moveRate)
	{
		return switch (pattern)
		{
			case pattern0 -> new Point2D.Double(100 * moveRate, 0.0) ;
			case pattern1 -> new Point2D.Double(100 * (3 * moveRate * moveRate - 3 * moveRate + 0.5), 0.0) ;
			case pattern2 -> new Point2D.Double(600.0 * (moveRate / 2.0 - moveRate * moveRate / 2.0), 0.0) ;
			case pattern3 -> new Point2D.Double(100.0 * Math.tanh(2 * moveRate), 0.0) ;
		} ;
	}

	// new Point(currentPos.x + step, currentPos.y + (int) (2 * step * Math.cos(2 * (Math.PI / 2 + 3 * Math.PI / 2 * moveRate))))
}
