package maps ;

import java.awt.Point ;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;

import main.GamePanel;

public class GroundRegion
{
	
	private final GroundType type ;
	private final Polygon region ;
	
	public GroundRegion(GroundType type, Polygon region)
	{
		this.type = type ;
		this.region = region ;
	}
	public GroundRegion(GroundType type, List<Point> regionPoints)
	{
		this(type, createPolygonFromPoints(regionPoints)) ;
	}

	private static Polygon createPolygonFromPoints(List<Point> points)
	{
		int[] xPoints = points.stream().mapToInt(p -> p.x).toArray() ;
		int[] yPoints = points.stream().mapToInt(p -> p.y).toArray() ;
		return new Polygon(xPoints, yPoints, points.size()) ;
	}
	
	public GroundType getType() { return type ;}
	public Polygon getRegion() { return region ;}

	public boolean containsPoint(Point point)
	{
		Rectangle bounds = region.getBounds();

		if (!bounds.contains(point)) { return false ;}

		return region.contains(point) ;
	}

	public void display()
	{
		switch (type)
		{
			case invisibleWall: return ;
			default:
				int[] closedXPoints = new int[region.npoints + 1];
				int[] closedYPoints = new int[region.npoints + 1];

				System.arraycopy(region.xpoints, 0, closedXPoints, 0, region.npoints);
				System.arraycopy(region.ypoints, 0, closedYPoints, 0, region.npoints);

				closedXPoints[region.npoints] = region.xpoints[0];
				closedYPoints[region.npoints] = region.ypoints[0];

				GamePanel.DP.drawPolyLine(closedXPoints, closedYPoints, 2, type.getColor());
				return ;
		}
	}
	@Override
	public String toString()
	{
		return "GroundRegion [type=" + type + ", region=" + region.getBounds() + "]";
	}
	
}
