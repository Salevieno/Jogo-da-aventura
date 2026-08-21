package Buildings ;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import NPC.NPC;
import components.Collider;
import components.Hitbox;
import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Drawable;
import main.Game;
import main.GamePanel;
import main.Palette;
import main.Path;
import maps.GameMap;
import utilities.Util;


public class Building implements Drawable
{
	private Point pos ;
	private Polygon contour ;
	private final BuildingTypes type ;
	private final List<NPC> npcs ;
	private final List<Point> npcOffsets ;
	private final List<Collider> colliders ;
	
	private static final int LAYER = 1;
	private static final String BUILDINGS_JSON_PATH = Path.DADOS + "gameMaps\\" + "buildings.json" ;
	private static final List<Building> ALL = new ArrayList<>() ;


	private Building(BuildingTypes type, List<NPC> npcs, List<Point> npcOffsets)
	{
		this.type = type ;
		this.npcs = npcs ;
		this.npcOffsets = npcOffsets ;
		this.colliders = new ArrayList<>() ; // loadColliders() ;
		ALL.add(this) ;
	}
	
	// private List<Collider> loadColliders()
	// {
	//  List<Collider> colliders = new ArrayList<>() ;
	// 	Image collidersImage = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Building" + type.getName() + "Colliders.png") ;
		
	// 	if (collidersImage == null) { return ;}
		
	// 	for (int i = 0 ; i <= collidersImage.getWidth(null) - 1 ; i += 1)
	// 	{
	// 		for (int j = 0 ; j <= collidersImage.getHeight(null) - 1 ; j += 1)
	// 		{
	// 			if (!Util.isTransparent(collidersImage, new Point(i, j)))
	// 			{
	// 				colliders.add(new Collider(new Point(pos.x + i, pos.y - type.getImage().getHeight(null) + j))) ;
	// 			}
	// 		}
	// 	}
	//  return colliders ;
	// }

	@SuppressWarnings("unchecked")
	public static void load()
	{
		JSONArray jsonData = Util.readJsonArray(BUILDINGS_JSON_PATH) ;
		for (Object buildingObj : jsonData)
		{
			JSONObject buildingJsonObj = (JSONObject) buildingObj ;
			BuildingTypes type = BuildingTypes.valueOf((String) buildingJsonObj.get("type")) ;
			JSONArray npcsData = (JSONArray) buildingJsonObj.getOrDefault("npcs", new JSONArray()) ;
			List<NPC> npcs = new ArrayList<>(npcsData.size()) ;
			List<Point> npcOffsets = new ArrayList<>(npcsData.size()) ;
			for (Object npcObj : npcsData)
			{
				JSONObject npcJson = (JSONObject) npcObj ;
				int id = (int) (long) npcJson.get("id") ;
				JSONObject posJson = (JSONObject) npcJson.get("offset") ;
				int npcPosX = (int) (long) posJson.get("x") ;
				int npcPosY = (int) (long) posJson.get("y") ;
				NPC npc = NPC.getAll().get(id) ;
				npcOffsets.add(new Point(npcPosX, npcPosY)) ;
				npcs.add(npc) ;
			}
			
			new Building(type, npcs, npcOffsets) ;
		}
	}

	public int getLayer() { return LAYER ;}
	public Point getPos() { return pos ;}
	public List<NPC> getNPCs() {return npcs ;}
	public List<Collider> getColliders() { return colliders ;}
	public static List<Building> getAll() { return ALL ;}
	public void setPos(Point pos)
	{
		this.pos = pos ;
		
		// Add offset for contour
		Point topLeftPos = UtilAlignment.getTopLeft(pos, Align.center, Util.getSize(type.getExteriorImage())) ;
		contour = new Polygon() ;
		Polygon typeContour = type.getContour() ;
		if (0 < typeContour.npoints)
		{
			int[] xpoints = new int[typeContour.npoints];
			int[] ypoints = new int[typeContour.npoints];
	
			for (int i = 0; i <= typeContour.npoints - 1; i++)
			{
				xpoints[i] = topLeftPos.x + typeContour.xpoints[i];
				ypoints[i] = topLeftPos.y + typeContour.ypoints[i];
			}
			contour = new Polygon(xpoints, ypoints, typeContour.npoints);
		}

		// Add offset for NPCs inside
		for (int i = 0 ; i <= npcs.size() - 1 ; i += 1)
		{
			npcs.get(i).setPos(new Point((int) (pos.x + npcOffsets.get(i).x), (int) (pos.y + npcOffsets.get(i).y))) ;
		}
	}

	public boolean hasNPCs() {return npcs != null && !npcs.isEmpty() ;}

	public void displayNPCs(Hitbox playerHitbox)
	{
		if (!hasNPCs()) { return ;}
		
		npcs.forEach(npc -> npc.display(playerHitbox)) ;
	}

	public void display(Hitbox playerHitbox, Point playerPos, int cityID)
	{		
		if (!contour.contains(playerPos))
		{
			GamePanel.getDP().drawImage(type.getExteriorImage(), pos, Scale.unit, Align.center) ;
			if (Game.DEBUG_MODE)
			{
				GamePanel.getDP().drawPolyLine(contour.xpoints, contour.ypoints, Palette.colors[3]) ;
			}
			
			return ;
		}

		GamePanel.getDP().drawImage(type.getInteriorImage(), pos, Scale.unit, Align.center) ;
		displayNPCs(playerHitbox) ;
		
		for (Collider collider : colliders)
		{
			GamePanel.getDP().drawRect(collider.getPos(), Align.center, new Dimension(1, 1), Palette.colors[0], null) ;
		}

	}

	public void display()
	{
		display(Game.getPlayer().getHitbox(), Game.getPlayer().getPos(), GameMap.getAllMaps().indexOf(Game.getPlayer().getMap())) ;
	}

	@Override
	public String toString()
	{
		return "Building [type=" + type + ", pos=" + pos + ", npcs=" + npcs + ", colliders=" + colliders + "]";
	}
}
