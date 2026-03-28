package Buildings;

import java.awt.Image;

import main.ImageLoader;
import main.Log;
import main.Path;

public enum BuildingTypes
{
	hospital(ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingHospital.png"), ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingHospitalInside.png")),
	bank(ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingBank.png"), ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingBankInside.png")),
	store(ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingStore.png"), ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingStoreInside.png")),
	craft(ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingCraft.png"), ImageLoader.loadImage(Path.BUILDINGS_IMG + "BuildingCraftInside.png"));

	private final Image exteriorImage ;
	private final Image interiorImage ;

	private BuildingTypes(Image exteriorImage, Image interiorImage)
	{
		if (exteriorImage == null) { Log.error("Building type with null exterior image") ;}
		if (interiorImage == null) { Log.error("Building type with null interior image") ;}

		this.exteriorImage = exteriorImage ;
		this.interiorImage = interiorImage ;
	}

	public Image getExteriorImage() {return exteriorImage ;}
	public Image getInteriorImage() {return interiorImage ;}
}
