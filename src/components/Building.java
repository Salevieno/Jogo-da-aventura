package components ;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class Building
{
	private BuildingType type ;
	private Point pos ;
	private List<NPCs> npcs ;	// NPCs in the building
	private List<Collider> colliders ;
	
	public Building(BuildingType type, Point pos, List<NPCs> npcs)
	{
		this.type = type ;
		this.pos = pos ;
		this.npcs = npcs ;
		colliders = new ArrayList<>() ;
		
		switch (type.getName())
		{
			case hospital: 
			{
				for (int i = 0 ; i <= 86 ; i += 1)
				{
					colliders.add(new Collider(new Point(pos.x + i, pos.y))) ;
				}
				
				for (int i = 118 ; i <= 177 ; i += 1)
				{
					colliders.add(new Collider(new Point(pos.x + i, pos.y))) ;
				}
				
				 break ;
			}
			default: break ;
		}
	}

	
	public BuildingType getType() { return type ;}
	public Point getPos() { return pos ;}
	public List<NPCs> getNPCs() {return npcs ;}
	public List<Collider> getColliders() { return colliders ;}
	
	public boolean isInside(Point pos) {return UtilG.isInside(pos, new Point(this.pos.x, this.pos.y - type.getImage().getHeight(null)), UtilG.getImageSize(type.getImage())) ;}
	public boolean hasNPCs() {return npcs != null ;}
		
	public void displayNPCs(DrawingOnPanel DP)
	{
		if (npcs == null) { return ;}
		
		for (NPCs npc : npcs)
		{
			npc.display(DP) ;
		}
	}
	
	public void display(Point playerPos, DrawingOnPanel DP)
	{
		if (type.getInsideImage() == null)
		{
			DP.DrawImage(type.getImage(), pos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.bottomLeft) ;
			displayNPCs(DP) ;
			
			return ;
		}
		
		if (!isInside(playerPos))
		{
			DP.DrawImage(type.getImage(), pos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.bottomLeft) ;
			
//			for (Collider collider : colliders)
//			{
//				DP.DrawRect(collider.getPos(), Align.center, new Dimension(1, 1), 1, Color.red, null) ;
//			}
			
			return ;
		}
		
		DP.DrawImage(type.getInsideImage(), pos, DrawingOnPanel.stdAngle, new Scale(1, 1), Align.bottomLeft) ;
		displayNPCs(DP) ;
		
		if (type.getName().equals(BuildingNames.sign))
		{
			Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
			Point messagePos = UtilG.Translate(pos, 10, 20) ;
//			String message = player.allText.get("* Mensagem das placas *") signMessage[id + 1] ;
			String message = "Oi!" ;
			DP.DrawRoundRect(pos, Align.topLeft, new Dimension(300, 200), 3, Game.ColorPalette[4], Game.ColorPalette[4], true) ;			
			DP.DrawFitText(messagePos, font.getSize() + 2, Align.bottomLeft, message, font, 35, Game.ColorPalette[5]) ;			
		}
	}


	@Override
	public String toString()
	{
		return "Building [type=" + type + ", pos=" + pos + ", npcs=" + npcs + ", colliders=" + colliders + "]";
	}
	
	
}