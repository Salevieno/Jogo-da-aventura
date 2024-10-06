package components ;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Draw;
import graphics.DrawPrimitives;
import libUtil.Align;
import libUtil.Util;
import main.Game;
import main.TextCategories;
import utilities.Scale;
import utilities.UtilS;

public class Building
{
	private BuildingType type ;
	private Point pos ;
	private List<NPCs> npcs ;
	private List<Collider> colliders ;

	public Building(BuildingType type, Point pos)
	{
		this.type = type ;
		this.pos = pos ;
		addStandardNPCs() ;
		colliders = new ArrayList<>() ;
//		addColliders() ;
	}
	
	public Building(BuildingType type, Point pos, List<NPCs> npcs)
	{
		this(type, pos) ;
		this.npcs = npcs ;
	}
	
	private void addColliders()
	{
		Image collidersImage = UtilS.loadImage("\\Buildings\\" + "Building" + type.getName() + "Colliders.png") ;
		
		if (collidersImage == null) { return ;}
		
		for (int i = 0 ; i <= collidersImage.getWidth(null) - 1 ; i += 1)
		{
			for (int j = 0 ; j <= collidersImage.getHeight(null) - 1 ; j += 1)
			{
				if (!Util.isTransparent(collidersImage, new Point(i, j)))
				{
					colliders.add(new Collider(new Point(pos.x + i, pos.y - type.getImage().getHeight(null) + j))) ;
				}
			}
		}
	}

	
	public BuildingType getType() { return type ;}
	public Point getPos() { return pos ;}
	public List<NPCs> getNPCs() {return npcs ;}
	public List<Collider> getColliders() { return colliders ;}
	
	public boolean isInside(Point pos) {return Util.isInside(pos, new Point(this.pos.x, this.pos.y - type.getImage().getHeight(null)), Util.getSize(type.getImage())) ;}
	public boolean hasNPCs() {return npcs != null ;}
		
	public void addStandardNPCs()
	{
		npcs = new ArrayList<>() ;
		switch (type.getName())
		{
			case hospital: npcs.add(new NPCs(Game.getNPCTypes()[0], Util.Translate(pos, 50, -20))) ; break ;
			case store: 
				npcs.add(new NPCs(Game.getNPCTypes()[1], Util.Translate(pos, 120, -60))) ;
				npcs.add(new NPCs(Game.getNPCTypes()[2], Util.Translate(pos, 80, -60))) ;
				
				break ;
			case bank: npcs.add(new NPCs(Game.getNPCTypes()[4], Util.Translate(pos, 40, -30))) ; break ;
			case craft: npcs.add(new NPCs(Game.getNPCTypes()[8], Util.Translate(pos, 100, -30))) ; break ;
			default: break;
		}
	}
	
	public void displayNPCs(Point playerPos, DrawPrimitives DP)
	{
		if (npcs == null) { return ;}
		
		for (NPCs npc : npcs)
		{
			npc.display(playerPos, DP) ;
		}
	}
	
	public void displaySignMessage(int cityID, DrawPrimitives DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 10) ;
		Point messagePos = Util.Translate(pos, 10, 10) ;
		String message = Game.allText.get(TextCategories.signMessages)[cityID] ;
		DP.drawRoundRect(pos, Align.topLeft, new Dimension(230, 80), 2, Game.colorPalette[3], true) ;			
		Draw.fitText(messagePos, font.getSize() + 2, Align.centerLeft, message, font, 40, Game.colorPalette[0]) ;	
	}
	
	public void display(Point playerPos, int cityID, DrawPrimitives DP)
	{
		if (type.getName().equals(BuildingNames.sign) & isInside(playerPos))
		{
			displaySignMessage(cityID, DP) ;
		}
		
		if (type.getInsideImage() == null)
		{
			DP.drawImage(type.getImage(), pos, Draw.stdAngle, Scale.unit, Align.bottomLeft) ;
			displayNPCs(playerPos, DP) ;
			
			return ;
		}
		
		if (!isInside(playerPos))
		{
			DP.drawImage(type.getImage(), pos, Draw.stdAngle, Scale.unit, Align.bottomLeft) ;
			
			
			return ;
		}

		DP.drawImage(type.getInsideImage(), pos, Draw.stdAngle, Scale.unit, Align.bottomLeft) ;
		displayNPCs(playerPos, DP) ;
		
		for (Collider collider : colliders)
		{
			DP.drawRect(collider.getPos(), Align.center, new Dimension(1, 1), Game.colorPalette[0], null) ;
		}
		
	}


	@Override
	public String toString()
	{
		return "Building [type=" + type + ", pos=" + pos + ", npcs=" + npcs + ", colliders=" + colliders + "]";
	}
	
	
}