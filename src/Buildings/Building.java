package Buildings ;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import utilities.Util;


public class Building implements Drawable
{
	private final BuildingType type ;
	private final Point pos ;
	private List<NPC> npcs ;
	private List<Collider> colliders ;

	public Building(BuildingType type, Point pos)
	{
		this.type = type ;
		this.pos = pos ;
		addStandardNPCs() ;
		colliders = new ArrayList<>() ;
//		addColliders() ;
	}
	
	public Building(BuildingType type, Point pos, List<NPC> npcs)
	{
		this(type, pos) ;
		this.npcs = npcs ;
	}
	
	// private void addColliders()
	// {
	// 	Image collidersImage = Game.loadImage(Path.BUILDINGS_IMG + "Building" + type.getName() + "Colliders.png") ;
		
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

	public static Building load(BuildingType[] buildingTypes, JSONObject buildingObj)
	{
		BuildingTypes buildingName = BuildingTypes.valueOf((String) buildingObj.get("name")) ;
		double posX = (Double) ((JSONObject) buildingObj.get("pos")).get("x") ;
		double posY = (Double) ((JSONObject) buildingObj.get("pos")).get("y") ;
		Point buildingPos = Game.getScreen().getPointWithinBorders(posX, posY) ;

		BuildingType buildingType = null ;
		for (BuildingType type : buildingTypes)
		{
			if (!buildingName.equals(type.getType()))
			{
				continue ; 
			}

			buildingType = type ;
		}

		return new Building(buildingType, buildingPos) ;
	}

	
	public BuildingType getType() { return type ;}
	public Point getPos() { return pos ;}
	public List<NPC> getNPCs() {return npcs ;}
	public List<Collider> getColliders() { return colliders ;}
	
	public boolean isInside(Point pos)
	{
		Point topLeftPos = UtilAlignment.getPosAt(this.pos, Align.center, Align.topLeft, Util.getSize(type.getExteriorImage())) ;
		return Util.isInside(pos, topLeftPos, Util.getSize(type.getExteriorImage())) ;
	}
	public boolean hasNPCs() {return npcs != null ;}
		
	public void addStandardNPCs()
	{

		if (Game.getNPCTypes() == null) { System.out.println("Erro ao adicionar NPCs nas construções: tipos de NPC não existem") ; return ;}
		if (Game.getNPCTypes().length <= 0) { System.out.println("Erro ao adicionar NPCs nas construções: não há nenhum tipo de NPC") ; return ;}
		
		npcs = new ArrayList<>() ;
		switch (type.getType())
		{
			case hospital: npcs.add(new NPC(Game.getNPCTypes()[0], Util.translate(pos, 50, -20))) ; break ;
			case store: 
				npcs.add(new NPC(Game.getNPCTypes()[1], Util.translate(pos, 120, -60))) ;
				npcs.add(new NPC(Game.getNPCTypes()[2], Util.translate(pos, 80, -60))) ;
				
				break ;
			case bank: npcs.add(new NPC(Game.getNPCTypes()[4], Util.translate(pos, 40, -30))) ; break ;
			case craft: npcs.add(new NPC(Game.getNPCTypes()[8], Util.translate(pos, 100, -30))) ; break ;
			default: break;
		}
	}
	
	public void displayNPCs(Hitbox playerHitbox)
	{
		if (npcs == null) { return ;}
		
		for (NPC npc : npcs)
		{
			npc.display(playerHitbox) ;
		}
	}
	
	public void display(Hitbox playerHitbox, Point playerPos, int cityID)
	{		
		if (type.getInteriorImage() == null)
		{
			GamePanel.DP.drawImage(type.getExteriorImage(), pos, Draw.stdAngle, Scale.unit, Align.center) ;
			displayNPCs(playerHitbox) ;
			
			return ;
		}
		
		if (!isInside(playerPos))
		{
			GamePanel.DP.drawImage(type.getExteriorImage(), pos, Draw.stdAngle, Scale.unit, Align.center) ;			
			
			return ;
		}

		GamePanel.DP.drawImage(type.getInteriorImage(), pos, Draw.stdAngle, Scale.unit, Align.center) ;
		displayNPCs(playerHitbox) ;
		
		for (Collider collider : colliders)
		{
			GamePanel.DP.drawRect(collider.getPos(), Align.center, new Dimension(1, 1), Game.palette[0], null) ;
		}
		
	}

	public void display()
	{
		display(Game.getPlayer().getHitbox(), Game.getPlayer().getPos(), Arrays.asList(Game.getMaps()).indexOf(Game.getPlayer().getMap())) ;
	}


	@Override
	public String toString()
	{
		return "Building [type=" + type + ", pos=" + pos + ", npcs=" + npcs + ", colliders=" + colliders + "]";
	}
	
	
}