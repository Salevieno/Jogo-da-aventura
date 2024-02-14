package components ;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Draw;
import graphics.DrawPrimitives;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

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
	}
	
	public Building(BuildingType type, Point pos, List<NPCs> npcs)
	{
		this.type = type ;
		this.pos = pos ;
		this.npcs = npcs ;
		colliders = new ArrayList<>() ;
		
//		Image collidersImage = UtilS.loadImage("\\Buildings\\" + "Building" + type.getName() + "Colliders.png") ;
//		
//		if (collidersImage == null) { return ;}
//		
//		for (int i = 0 ; i <= collidersImage.getWidth(null) - 1 ; i += 1)
//		{
//			for (int j = 0 ; j <= collidersImage.getHeight(null) - 1 ; j += 1)
//			{
//				if (!UtilG.isTransparent(collidersImage, new Point(i, j)))
//				{
//					colliders.add(new Collider(new Point(pos.x + i, pos.y - type.getImage().getHeight(null) + j))) ;
//				}
//			}
//		}
	}

	
	public BuildingType getType() { return type ;}
	public Point getPos() { return pos ;}
	public List<NPCs> getNPCs() {return npcs ;}
	public List<Collider> getColliders() { return colliders ;}
	
	public boolean isInside(Point pos) {return UtilG.isInside(pos, new Point(this.pos.x, this.pos.y - type.getImage().getHeight(null)), UtilG.getSize(type.getImage())) ;}
	public boolean hasNPCs() {return npcs != null ;}
		
	public void addStandardNPCs()
	{
		npcs = new ArrayList<>() ;
		switch (type.getName())
		{
			case hospital: npcs.add(new NPCs(Game.getNPCTypes()[0], UtilG.Translate(pos, 120, -60))) ; break ;
			case store: 
				npcs.add(new NPCs(Game.getNPCTypes()[1], UtilG.Translate(pos, 120, -60))) ;
				npcs.add(new NPCs(Game.getNPCTypes()[2], UtilG.Translate(pos, 80, -60))) ;
				
				break ;
			case bank: npcs.add(new NPCs(Game.getNPCTypes()[4], UtilG.Translate(pos, 40, -30))) ; break ;
			case craft: npcs.add(new NPCs(Game.getNPCTypes()[8], UtilG.Translate(pos, 40, -30))) ; break ;
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
		Point messagePos = UtilG.Translate(pos, 10, 10) ;
		String message = Game.allText.get(TextCategories.signMessages)[cityID] ;
		DP.drawGradRoundRect(pos, Align.topLeft, new Dimension(230, 80), 2, Game.colorPalette[3], Game.colorPalette[3], true) ;			
		Draw.fitText(messagePos, font.getSize() + 2, Align.centerLeft, message, font, 40, Game.colorPalette[0]) ;	
	}
	
	public void display(Point playerPos, int cityID, DrawPrimitives DP)
	{
		if (type.getName().equals(BuildingNames.sign) & isInside(playerPos))
		{
			displaySignMessage(cityID, DP) ;
		}
		
//		for (Collider collider : colliders)
//		{
//			DP.DrawRect(collider.getPos(), Align.center, new Dimension(1, 1), 1, Color.black, null) ;
//		}
		
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
		
	}


	@Override
	public String toString()
	{
		return "Building [type=" + type + ", pos=" + pos + ", npcs=" + npcs + ", colliders=" + colliders + "]";
	}
	
	
}