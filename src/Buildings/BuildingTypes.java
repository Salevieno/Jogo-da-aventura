package Buildings;

import java.awt.Image;
import java.awt.Polygon;

import main.ImageLoader;
import main.Log;
import main.Path;

public enum BuildingTypes
{
	hospital("Hospital", new Polygon(new int[] {3, 161, 195, 195, 118, 82, 3, 3}, new int[] {193, 193, 159, 91, 1, 19, 120, 193}, 8)),
	bank("Bank", new Polygon(new int[] {140, 205, 276, 315, 338, 345, 345, 293, 239, 194, 152, 107, 52, 0, 0, 7, 29, 68, 140}, new int[] {201, 201, 179, 156, 131, 113, 100, 41, 7, 0, 0, 7, 40, 99, 112, 130, 155, 178, 201}, 19)),
	store("Store", new Polygon(new int[] {30, 202, 351, 240, 198, 154, 101, 30, 30}, new int[] {250, 250, 103, 9, 58, 96, 134, 228, 250}, 9)),
	craft("Craft", new Polygon(new int[] {19, 228, 282, 239, 199, 199, 163, 110, 19, 19}, new int[] {228, 228, 172, 87, 87, 38, 2, 56, 154, 228}, 10));

	private final Image exteriorImage ;
	private final Image interiorImage ;
	private final Polygon contour ;

	private BuildingTypes(String name, Polygon contour)
	{
		Image exteriorImage = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Building" + name + ".png") ;
		Image interiorImage = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Building" + name + "Inside.png") ;

		if (exteriorImage == null) { Log.error("Building type with null exterior image") ;}
		if (interiorImage == null) { Log.error("Building type with null interior image") ;}

		this.exteriorImage = exteriorImage ;
		this.interiorImage = interiorImage ;
		this.contour = contour ;
	}

	public Image getExteriorImage() { return exteriorImage ;}
	public Image getInteriorImage() { return interiorImage ;}
	public Polygon getContour() { return contour ;}
}
