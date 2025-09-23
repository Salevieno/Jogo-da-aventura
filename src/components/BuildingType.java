package components;

import java.awt.Image;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import main.Path;
import utilities.Util;

public class BuildingType
{
	private final BuildingNames name ;
	private final Image image ;
	private Image insideImage ;
	// private Image[] ornamentImages ;

	private static final String dadosPath = Path.DADOS + "buildings\\" ;
	private static final String assetsPath = Path.IMAGES + Path.BUILDINGS_IMG ;
	
	public BuildingType(BuildingNames name, Image image)
	{
		this.name = name ;
		this.image = image ;
		this.insideImage = null ;
		// this.ornamentImages = null ;
	}

	
	public static BuildingType[] load()
	{
		JSONArray dados = Util.readJsonArray(dadosPath + "buildingTypes.json") ;
		BuildingType[] buildingTypes = new BuildingType[dados.size()] ;
		
		for (int i = 0 ; i <= dados.size() - 1 ; i += 1)
		{
			JSONObject dado = (JSONObject) dados.get(i) ;
			BuildingNames name = BuildingNames.valueOf((String) dado.get("name")) ;
			Image outsideImage = Util.loadImage(assetsPath + "Building" + name + ".png") ;

			buildingTypes[i] = new BuildingType(name, outsideImage) ;

			boolean hasInterior = (boolean) dado.get("hasInterior") ;
			if (hasInterior)
			{
				Image insideImage = Util.loadImage(assetsPath + "Building" + name + "Inside.png") ;
				// Image[] OrnamentImages = new Image[] { Util.loadImage(assetsPath + "Building" + name + "Ornament.png") } ;
				buildingTypes[i].setInsideImage(insideImage) ;
				// buildingTypes[i].setOrnamentImages(OrnamentImages) ;
			}
		}

		return buildingTypes ;
	}	

	public BuildingNames getName() {return name ;}
	public Image getImage() {return image ;}
	public Image getInsideImage() {return insideImage ;}
	// public Image[] getOrnamentImages() {return ornamentImages ;}
	public void setInsideImage(Image insideImage) { this.insideImage = insideImage ;}
	// public void setOrnamentImages(Image[] ornamentImages) { this.ornamentImages = ornamentImages ;}

	@Override
	public String toString()
	{
		return "BuildingType: " + name ;
	}
}
