package components;

import java.awt.Image;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import main.Game;
import main.Path;
import utilities.Util;

public class BuildingType
{
	private final BuildingNames name ;
	private final Image image ;
	private Image insideImage ;
	// private Image[] ornamentImages ;

	private static final String dadosPath = Path.DADOS + "buildings\\" ;
	
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
			Image outsideImage = Game.loadImage(Path.BUILDINGS_IMG + "Building" + name + ".png") ;

			buildingTypes[i] = new BuildingType(name, outsideImage) ;

			boolean hasInterior = (boolean) dado.get("hasInterior") ;
			if (hasInterior)
			{
				Image insideImage = Game.loadImage(Path.BUILDINGS_IMG + "Building" + name + "Inside.png") ;
				// Image[] OrnamentImages = new Image[] { Game.loadImage(Path.BUILDINGS_IMG + "Building" + name + "Ornament.png") } ;
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
