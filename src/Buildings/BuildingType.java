package Buildings;

import java.awt.Image;

import main.ImageLoader;
import main.Path;

public class BuildingType
{
	private final BuildingTypes type ;
	private final Image exteriorImage ;
	private final Image interiorImage ;
	// TODO essa classe pode ser um enum?
	private BuildingType(BuildingTypes type, Image image, Image interiorImage)
	{
		this.type = type ;
		this.exteriorImage = image ;
		this.interiorImage = interiorImage ;
	}
	
	public static BuildingType[] initialize()
	{
		BuildingType[] buildingTypes = new BuildingType[BuildingTypes.values().length] ;
		
		for (int i = 0 ; i <= BuildingTypes.values().length - 1 ; i += 1)
		{
			BuildingTypes name = BuildingTypes.values()[i] ;
			Image outsideImage = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Building" + name + ".png") ;
			Image insideImage = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Building" + name + "Inside.png") ;
			buildingTypes[i] = new BuildingType(name, outsideImage, insideImage) ;
		}

		return buildingTypes ;
	}	

	public BuildingTypes getType() {return type ;}
	public Image getExteriorImage() {return exteriorImage ;}
	public Image getInteriorImage() {return interiorImage ;}

	@Override
	public String toString()
	{
		return "BuildingType: " + type ;
	}
}
