package components ;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class Building
{
	private BuildingType type ;
	private Point pos ;
	
	public Building(BuildingType type, Point pos)
	{
		this.type = type ;
		this.pos = pos ;
	}

	
	public BuildingType getType() {return type ;}
	public Point getPos() {return pos ;}
	public boolean isInside(Point pos) {return UtilG.isInside(pos, new Point(this.pos.x, this.pos.y - type.getImage().getHeight(null)), UtilG.getImageSize(type.getImage())) ;}
		
	public void display(Point playerPos, double angle, Scale scale, DrawingOnPanel DP)
	{
		if (!isInside(playerPos))
		{
			Image image = type.getImage() ;
			DP.DrawImage(image, pos, angle, scale, Align.bottomLeft) ;
			
			return ;
		}
		
		Image image = type.getInsideImage() ;
		DP.DrawImage(image, pos, angle, scale, Align.bottomLeft) ;
		
		if (type.getNPCs() == null) { return ;}
		
		for (NPCs npc : type.getNPCs())
		{
			npc.display(DP) ;
		}
		
		if (type.getName().equals("Sign"))
		{
			Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
			Point messagePos = UtilG.Translate(pos, 10, 20) ;
//			String message = player.allText.get("* Mensagem das placas *") signMessage[id + 1] ;
			String message = "Oi!" ;
			DP.DrawRoundRect(pos, Align.topLeft, new Dimension(300, 200), 3, Game.ColorPalette[4], Game.ColorPalette[4], true) ;			
			DP.DrawFitText(messagePos, font.getSize() + 2, Align.bottomLeft, message, font, 35, Game.ColorPalette[5]) ;			
		}
	}
}