package maps;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import components.Building;
import components.BuildingNames;
import components.BuildingType;
import components.NPC;
import components.NPCType;
import items.Fab;
import items.GeneralItem;
import main.Game;
import utilities.Util;
import utilities.UtilS;

public class CityMap extends GameMap
{

	private static final List<Image> images ;
	
	static
	{
		images = new ArrayList<>() ;
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			images.add(UtilS.loadImage("\\Maps\\" + "Map" + String.valueOf(i) + ".png")) ;
		}
	}
	
	public CityMap(String Name, Continents Continent, int[] Connections, Image image, Clip music, List<Building> buildings, List<NPC> npcs)
	{
		super(Name, Continent, Connections, image, music, buildings, npcs) ;
		
//		if (Name.equals("City of the knights"))
//		{
//			
//			mapElems.add(new MapElement(0, "Knight'sCityWall", new Point(0, 96 - knightsCityWallImage.getHeight(null)))) ;
//			mapElems.add(new MapElement(0, "Knight'sCityWall", new Point(0, 480 - knightsCityWallImage.getHeight(null)))) ;
//		}
		
		diggingItems.put(Fab.getAll()[0], allDiggingItems.get(Fab.getAll()[0])) ;
		diggingItems.put(Fab.getAll()[25], allDiggingItems.get(Fab.getAll()[25])) ;
		diggingItems.put(GeneralItem.getAll()[4], allDiggingItems.get(GeneralItem.getAll()[4])) ;
		diggingItems.put(GeneralItem.getAll()[25], allDiggingItems.get(GeneralItem.getAll()[25])) ;
		diggingItems.put(GeneralItem.getAll()[35], allDiggingItems.get(GeneralItem.getAll()[35])) ;
		diggingItems.put(GeneralItem.getAll()[155], allDiggingItems.get(GeneralItem.getAll()[155])) ;
		calcDigItemChances() ;
	}
	


	private static NPC readNPCfromJson(JSONObject npcJSONObject)
	{

		JSONObject npc = npcJSONObject ;
		String npcName = (String) npc.get("name") ;
		double posX = (Double) ((JSONObject) npc.get("pos")).get("x") ;
		double posY = (Double) ((JSONObject) npc.get("pos")).get("y") ;
		Point npcPos = Game.getScreen().getPointWithinBorders(posX, posY) ;
		NPCType npcType = NPC.typeFromName(npcName) ;
		return new NPC(npcType, npcPos) ;

	}
	
	private static List<NPC> loadNPCs(JSONObject map)
	{
		JSONArray listNPCs = (JSONArray) map.get("NPCs") ;
		List<NPC> npcs = new ArrayList<>() ;
		for (int i = 0 ; i <= listNPCs.size() - 1 ; i += 1)
		{
			NPC npc = readNPCfromJson((JSONObject) listNPCs.get(i)) ;
			npcs.add(npc) ;
		}
		
		return npcs ;
	}
	
	private static List<Building> loadBuildings(BuildingType[] buildingTypes, JSONObject map)
	{		
		JSONArray listBuildings = (JSONArray) map.get("Buildings") ;
		List<Building> buildings = new ArrayList<>() ;
		for (int i = 0 ; i <= listBuildings.size() - 1 ; i += 1)
		{
			JSONObject building = (JSONObject) listBuildings.get(i) ;
			BuildingNames buildingName = BuildingNames.valueOf((String) building.get("name")) ;
			double posX = (Double) ((JSONObject) building.get("pos")).get("x") ;
			double posY = (Double) ((JSONObject) building.get("pos")).get("y") ;
			Point buildingPos = Game.getScreen().getPointWithinBorders(posX, posY) ;

			BuildingType buildingType = null ;
			for (BuildingType type : buildingTypes)
			{
				if (!buildingName.equals(type.getName()))
				{
					continue ;
				}

				buildingType = type ;
			}

			buildings.add(new Building(buildingType, buildingPos)) ;
		}
		
		return buildings ;
	}
	
	private static int[] loadConnections(JSONObject map)
	{
		JSONObject connectionIDs = (JSONObject) map.get("Connections") ;
		int[] connections = new int[8] ;
		connections[0] = (int) (long) connectionIDs.get("topRight") ;
		connections[1] = (int) (long) connectionIDs.get("topLeft") ;
		connections[2] = (int) (long) connectionIDs.get("leftTop") ;
		connections[3] = (int) (long) connectionIDs.get("leftBottom") ;
		connections[4] = (int) (long) connectionIDs.get("bottomLeft") ;
		connections[5] = (int) (long) connectionIDs.get("bottomRight") ;
		connections[6] = (int) (long) connectionIDs.get("rightBottom") ;
		connections[7] = (int) (long) connectionIDs.get("rightTop") ;
		
		return connections ;
	}

	public static CityMap[] load(BuildingType[] buildingTypes)
	{
		JSONArray input = Util.readJsonArray(dadosPath + "mapsCity.json") ;
		CityMap[] cityMaps = new CityMap[input.size()] ;

		for (int id = 0 ; id <= input.size() - 1 ; id += 1)
		{
			JSONObject map = (JSONObject) input.get(id) ;

			String name = (String) map.get("Name") ;
			int continentID = (int) (long) map.get("Continent") ;
			Continents continent = Continents.values()[continentID] ;
			int[] connections = loadConnections(map) ;
			Image image = CityMap.images.get(id) ;
			Clip music = GameMap.musicCities ;
			List<Building> buildings = loadBuildings(buildingTypes, map) ;
			List<NPC> npcs = loadNPCs(map) ;

			cityMaps[id] = new CityMap(name, continent, connections, image, music, buildings, npcs) ;
			
			switch (id)
			{
				case 2:
//					cityMaps[id].addGroundType(new GroundType(GroundTypes.water, new Point(500, Sky.height), new Dimension(140, 480 - Sky.height))) ;
					break ;
				default: break ;
			}
		}

		return cityMaps ;
	}
    
	public Building getHospital() { return buildings.get(0) ;}
	public Building getStore() { return buildings.get(1) ;}
	public Building getCraft() { return buildings.get(2) ;}
	public Building getBank() { return buildings.get(3) ;}
	public Building getForge() { return buildings.get(4) ;}
	public Building getSign() { return buildings.get(5) ;}

}
