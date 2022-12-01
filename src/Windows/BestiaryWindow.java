package Windows;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import Graphics.DrawPrimitives;
import LiveBeings.Creatures;
import LiveBeings.Player;
import Main.Game;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class BestiaryWindow extends Window
{
	private ArrayList<Creatures> discoveredCreatures ;
	
	public BestiaryWindow()
	{
		super(null, 0, 0, 0, 0) ;
		discoveredCreatures = new ArrayList<>() ;
	}
	
	public ArrayList<Creatures> getDiscoveredCreatures() { return discoveredCreatures ; }
	public void addDiscoveredCreature(Creatures newCreature) { discoveredCreatures.add(newCreature) ; }
	
	public void navigate(String action)
	{
		int windowLimit = 0 ;
		if (discoveredCreatures != null)
		{
			windowLimit = Math.max(discoveredCreatures.size() - 1, 0)/5 ;
		}
		window = UtilS.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, windowLimit) ;
	}
	
	public void display(Point MousePos, DrawPrimitives DP)
	{
		Size screenSize = Game.getScreen().getSize() ;
		Color[] ColorPalette = Game.ColorPalette ;
		Point windowPos = new Point((int)(0.5*screenSize.x), (int)(0.5*screenSize.y)) ;
		int[] NumCreatureWindows = new int[] {6, 6} ;
		int NumberOfCreaturesPerWindow = NumCreatureWindows[0]*NumCreatureWindows[1] ;
		int NumberOfCreaturesInWindow = 0 ;
		if (discoveredCreatures != null)
		{
			NumberOfCreaturesInWindow = Math.min(NumberOfCreaturesPerWindow, discoveredCreatures.size() - NumberOfCreaturesPerWindow*window) ;
		}
		int L = (int)(0.6*screenSize.x), H = (int)(0.6*screenSize.y) ;
		float offset = 25 ;	// Offset from the edges
		float Sx = offset, Sy = offset ;
		Size size = new Size((int) ((L - 2*offset - (NumCreatureWindows[0] - 1)*Sx)/NumCreatureWindows[0]), (int) (H - 2*offset - (NumCreatureWindows[1] - 1)*Sy)/NumCreatureWindows[1]) ;
		int thickness = 2 ;
		//DF.DrawMenuWindow(Pos, L, H, AllText[AllTextCat[1]][1], 0, ColorPalette[7], ColorPalette[2]) ;
		//DF.DrawWindowArray(NumCreatureWindows, new Point((int) (Pos.x + offset), (int) (Pos.y - H + h + offset)), "BotLeft", l, h, Sx, Sy, thickness, new Color[] {ColorPalette[7], ColorPalette[0]}, NumberOfCreaturesInWindow) ;

		drawGenericWindow(DP) ;
		if (discoveredCreatures != null)
		{
			Creatures selectedCreature = null ;
			for (int cx = 0 ; cx <= NumCreatureWindows[0] - 1 ; cx += 1)
			{
				for (int cy = 0 ; cy <= NumCreatureWindows[1] - 1 ; cy += 1)
				{
					int creatureID = NumberOfCreaturesPerWindow*window + cx*NumCreatureWindows[0] + cy;
					if (creatureID < discoveredCreatures.size())
					{
						Creatures creature = discoveredCreatures.get(creatureID) ;
						Point creaturePos = new Point((int) (windowPos.x + offset + cx * (size.x + Sx)), (int) (windowPos.y + size.y + offset + cy * (size.y + Sy))) ;
						if (UtilG.isInside(MousePos, creaturePos, size))
						{
							selectedCreature = creature ;
						}
						System.out.println(creaturePos);
						creature.display(creaturePos, new float[] {(float) (39.0 / creature.getSize().x), (float) (39.0 / creature.getSize().y)}, DP) ;
						//DF.DrawCreature(new Point((int) (InitPos.x + 0.5*l), (int) (InitPos.y - 0.5*h)), new int[] {creatureTypes[CreatureID].getSize()[0]/2, creatureTypes[CreatureID].getSize()[1]/2}, creatureTypes[CreatureID].getimage(), creatureTypes[CreatureID].getColor()) ;
					}
				}
			}
			if (selectedCreature != null)
			{
				//DF.DrawCreatureInfoWindow(new Point(Pos.x + L, Pos.y), creatureTypes[SelectedCreature], items) ;
			}
		}
	}
}
