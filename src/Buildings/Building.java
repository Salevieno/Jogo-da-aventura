package Buildings ;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import NPC.NPC;
import components.Collider;
import components.Hitbox;
import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import graphics2.Drawable;
import main.Game;
import main.GamePanel;
import main.Palette;
import utilities.Util;


public class Building implements Drawable
{
	private static final int layer = 1;
	private final BuildingTypes type ;
	private final Point pos ;
	private final List<NPC> npcs ;
	private final List<Collider> colliders ;


	private Building(BuildingTypes type, Point pos, List<NPC> npcs)
	{
		this.type = type ;
		this.pos = pos ;
		this.npcs = npcs ;
		this.colliders = new ArrayList<>() ;

		// if (Game.getNPCTypes() == null) { Log.error("Ao adicionar NPCs nas construções: tipos de NPC não existem") ; return ;}
		// if (Game.getNPCTypes().length <= 9) { Log.error("Ao adicionar NPCs nas construções: não há tipos de NPCs suficientes") ; return ;}

		// switch (type)
		// {
		// 	case hospital:
		// 		npcs.add(new NPCDoctor(Util.translate(pos, 50, -20), List.of("Test", "Test doc 1 menu 1"))) ;
		// 		break ;

		// 	case store: 
		// 		int[] itemIDs = new int[] {300, 305, 307, 309, 315, 322, 326, 328, 332, 336, 340, 344} ;
		// 		npcs.add(new NPCSeller(Util.translate(pos, 120, -60), speech, Item.getItems(itemIDs))) ;
		// 		itemIDs = new int[] {1329, 0, 1, 4, 5, 121, 122, 125, 130, 1301, 1305, 1702, 1708, 1710, 1713} ;
		// 		npcs.add(new NPCSeller(Util.translate(pos, 80, -60), speech, Item.getItems(itemIDs))) ;
		// 		// npcs.add(new NPC(Game.getNPCTypes()[1], Util.translate(pos, 120, -60))) ;
		// 		// npcs.add(new NPC(Game.getNPCTypes()[2], Util.translate(pos, 80, -60))) ;
				
		// 		break ;
		// 	case bank: npcs.add(new NPCBanker(Util.translate(pos, 40, -30))) ; break ;
		// 	case craft: npcs.add(new NPCCrafter(Util.translate(pos, 100, -30))) ; break ;
		// 	default: break;
		// }

//		addColliders() ;
	}
	
	// private void addColliders()
	// {
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
	// }

	@SuppressWarnings("unchecked")
	public static Building load(JSONObject buildingObj)
	{
		BuildingTypes type = BuildingTypes.valueOf((String) buildingObj.get("name")) ;
		double posX = (Double) ((JSONObject) buildingObj.get("pos")).get("x") ;
		double posY = (Double) ((JSONObject) buildingObj.get("pos")).get("y") ;
		Point pos = Game.getScreen().getPointWithinBorders(posX, posY) ;
		List<JSONArray> npcsData = (JSONArray) buildingObj.get("npcs") ;
		if (npcsData == null)
		{
			npcsData = new ArrayList<>() ;
		}
		List<NPC> npcs = new ArrayList<>(npcsData.size()) ;
		for (Object npcObj : npcsData)
		{
			JSONObject npcJson = (JSONObject) npcObj ;
			int id = (int) (long) npcJson.get("id") ;
			JSONObject posJson = (JSONObject) npcJson.get("pos") ;
			double npcPosX = (double) posJson.get("x") ;
			double npcPosY = (double) posJson.get("y") ;
			NPC npc = NPC.getAll().get(id) ;
			npc.setPos(Game.getScreen().getPointWithinBorders(npcPosX, npcPosY)) ;
			npcs.add(npc) ;
		}

		return new Building(type, pos, npcs) ;
	}

	public int getLayer() { return layer ;}
	public Point getPos() { return pos ;}
	public List<NPC> getNPCs() {return npcs ;}
	public List<Collider> getColliders() { return colliders ;}
	
	private boolean isInside(Point pos)
	{
		Point topLeftPos = UtilAlignment.getPosAt(this.pos, Align.center, Align.topLeft, Util.getSize(type.getExteriorImage())) ;
		return Util.isInside(pos, topLeftPos, Util.getSize(type.getExteriorImage())) ;
	}
	public boolean hasNPCs() {return npcs != null ;}

	public void displayNPCs(Hitbox playerHitbox)
	{
		if (!hasNPCs()) { return ;}
		
		npcs.forEach(npc -> npc.display(playerHitbox)) ;
	}

	public void display(Hitbox playerHitbox, Point playerPos, int cityID)
	{		
		if (!isInside(playerPos))
		{
			GamePanel.DP.drawImage(type.getExteriorImage(), pos, Draw.stdAngle, Scale.unit, Align.center) ;			
			
			return ;
		}

		GamePanel.DP.drawImage(type.getInteriorImage(), pos, Draw.stdAngle, Scale.unit, Align.center) ;
		displayNPCs(playerHitbox) ;
		
		for (Collider collider : colliders)
		{
			GamePanel.DP.drawRect(collider.getPos(), Align.center, new Dimension(1, 1), Palette.colors[0], null) ;
		}
	}

	public void display()
	{
		display(Game.getPlayer().getHitbox(), Game.getPlayer().getPos(), Arrays.asList(Game.getAllMaps()).indexOf(Game.getPlayer().getMap())) ;
	}

	@Override
	public String toString()
	{
		return "Building [type=" + type + ", pos=" + pos + ", npcs=" + npcs + ", colliders=" + colliders + "]";
	}
}
