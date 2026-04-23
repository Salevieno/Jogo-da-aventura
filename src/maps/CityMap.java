package maps;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.Clip;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Buildings.Building;
import Buildings.Sign;
import NPC.NPC;
import graphics.Align;
import graphics2.SpriteAnimation;
import items.Fab;
import items.GeneralItem;
import main.Game;
import main.ImageLoader;
import main.Interactable;
import main.Music;
import main.Path;
import utilities.Util;


public class CityMap extends GameMap
{

	private final Sign sign ;
	private static final Clip musicCities = Music.musicFileToClip(new File(Path.MUSIC + "cidade.wav").getAbsoluteFile()) ;
	private static final List<CityMap> allCityMaps = new ArrayList<>() ;
	
	private CityMap(int id, String Name, Continents Continent, int[] Connections, Image image, Clip music, List<Building> buildings, List<NPC> npcs, Point signPos, List<GroundRegion> groundRegions, List<SpriteAnimation> animations)
	{
		super(id, Name, Continent, Connections, image, music, groundRegions, buildings, npcs, animations) ;
		sign = new Sign(signPos, id) ;
		diggingItems.put(Fab.getAll()[0], allDiggingItems.get(Fab.getAll()[0])) ;
		diggingItems.put(Fab.getAll()[25], allDiggingItems.get(Fab.getAll()[25])) ;
		diggingItems.put(GeneralItem.getAll()[4], allDiggingItems.get(GeneralItem.getAll()[4])) ;
		diggingItems.put(GeneralItem.getAll()[25], allDiggingItems.get(GeneralItem.getAll()[25])) ;
		diggingItems.put(GeneralItem.getAll()[35], allDiggingItems.get(GeneralItem.getAll()[35])) ;
		diggingItems.put(GeneralItem.getAll()[155], allDiggingItems.get(GeneralItem.getAll()[155])) ;
		calcDigItemChances() ;
		allCityMaps.add(this) ;
	}
	
	private static List<NPC> loadNPCs(JSONObject map)
	{
		JSONArray listNPCs = (JSONArray) map.get("NPCs") ;
		List<NPC> npcs = new ArrayList<>() ;

		for (Object npcObj : listNPCs)
		{
			JSONObject npcJson = (JSONObject) npcObj ;
			int id = (int) (long) npcJson.get("id") ;
			JSONObject posJson = (JSONObject) npcJson.get("pos") ;
			double posX = (double) posJson.get("x") ;
			double posY = (double) posJson.get("y") ;
			NPC npc = NPC.getAll().get(id) ;
			npc.setPos(Game.getScreen().getPointWithinBorders(posX, posY)) ;
			npcs.add(npc) ;
		}

		return npcs ;
	}
	
	private static List<Building> loadBuildings(JSONObject map)
	{		
		JSONArray listBuildingObjs = (JSONArray) map.get("Buildings") ;
		List<Building> buildings = new ArrayList<>() ;
		for (Object buildingObj : listBuildingObjs)
		{
			JSONObject buildingJsonObj = (JSONObject) buildingObj ;
			int buildingID = (int) (long) buildingJsonObj.get("id") ;
			double posX = (Double) ((JSONObject) buildingJsonObj.get("pos")).get("x") ;
			double posY = (Double) ((JSONObject) buildingJsonObj.get("pos")).get("y") ;
			Point pos = Game.getScreen().getPointWithinBorders(posX, posY) ;
			Building newBuilding = Building.getAll().get(buildingID) ;
			newBuilding.setPos(pos) ;
			buildings.add(newBuilding) ;
		}
		
		return buildings ;
	}

	public static void load()
	{
		JSONArray input = Util.readJsonArray(dadosPath + "mapsCity.json") ;
		CityMap[] cityMaps = new CityMap[input.size()] ;

		for (int id = 0 ; id <= input.size() - 1 ; id += 1)
		{
			JSONObject mapData = (JSONObject) input.get(id) ;

			String name = (String) mapData.get("Name") ;
			int continentID = (int) (long) mapData.get("Continent") ;
			Continents continent = Continents.values()[continentID] ;
			int[] connections = loadConnections(mapData) ;
			Image image = ImageLoader.loadImage(Path.MAPS_IMG + "Map" + String.valueOf(id) + ".png") ;
			List<Building> buildings = loadBuildings(mapData) ;
			List<NPC> npcs = loadNPCs(mapData) ;
			Point signPos = Game.getScreen().getPointWithinBorders((Double) ((JSONObject) mapData.get("signPos")).get("x"), (Double) ((JSONObject) mapData.get("signPos")).get("y")) ;
			// Point forgePos =Game.getScreen().getPointWithinBorders((Double) ((JSONObject) mapData.get("forgePos")).get("x"), (Double) ((JSONObject) mapData.get("forgePos")).get("y")) ;
			List<GroundRegion> groundRegions = groundRegionsFromJson((JSONObject) mapData.get("GroundRegions")) ;
			List<SpriteAnimation> animations = new ArrayList<>() ;
			if ("City of the archers".equals(name))
			{
				animations.add(new SpriteAnimation(Path.MAPS_IMG + "Map2_beach.png", new Point(Game.getScreen().mapSize().width, 192), Align.topRight, true, 12, 0.2, 0)) ;
			}
			cityMaps[id] = new CityMap(id, name, continent, connections, image, musicCities, buildings, npcs, signPos, groundRegions, animations) ;
		}
	}
    
	public Building getHospital() { return buildings.get(0) ;}
	public Building getStore() { return buildings.get(1) ;}
	public Building getCraft() { return buildings.get(2) ;}
	public Building getBank() { return buildings.get(3) ;}
	public Sign getSign() { return sign ;}
	public Set<Interactable> getInteractables() { return new HashSet<>(npcs) ;}
	public static List<CityMap> getAllCityMaps() { return allCityMaps ;}

}
