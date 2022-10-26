package Windows;

import java.awt.Color;
import java.awt.Point;

import GameComponents.Size;
import LiveBeings.Player;
import Main.Game;
import Main.Utg;
import Main.Uts;

public class Bestiary extends Window
{
	public Bestiary()
	{
		super(null, 0, 0, 0, 0) ;
	}
	
	public void navigate(int[] creaturesDiscovered, String action)
	{
		int windowLimit = 0 ;
		if (creaturesDiscovered != null)
		{
			windowLimit = Math.max(creaturesDiscovered.length - 1, 0)/5 ;
		}
		window = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, windowLimit) ;
	}
	
	public void display(int[] creaturesDiscovered, Point MousePos, int[] AllTextCat, String[][] AllText)
	{
		Size screenSize = Game.getScreen().getSize() ;
		Color[] ColorPalette = Game.ColorPalette ;
		Point Pos = new Point((int)(0.05*screenSize.x), (int)(0.95*screenSize.y)) ;
		int[] NumCreatureWindows = new int[] {6, 6} ;
		int NumberOfCreaturesPerWindow = NumCreatureWindows[0]*NumCreatureWindows[1] ;
		int NumberOfCreaturesInWindow = 0 ;
		if (creaturesDiscovered != null)
		{
			NumberOfCreaturesInWindow = Math.min(NumberOfCreaturesPerWindow, creaturesDiscovered.length - NumberOfCreaturesPerWindow*window) ;
		}
		int L = (int)(0.6*screenSize.x), H = (int)(0.6*screenSize.y) ;
		float offset = 5 ;	// Offset from the edges
		float Sx = offset, Sy = offset ;
		Size size = new Size((int) ((L - 2*offset - (NumCreatureWindows[0] - 1)*Sx)/NumCreatureWindows[0]), (int) (H - 2*offset - (NumCreatureWindows[1] - 1)*Sy)/NumCreatureWindows[1]) ;
		int thickness = 2 ;
		//DF.DrawMenuWindow(Pos, L, H, AllText[AllTextCat[1]][1], 0, ColorPalette[7], ColorPalette[2]) ;
		//DF.DrawWindowArray(NumCreatureWindows, new Point((int) (Pos.x + offset), (int) (Pos.y - H + h + offset)), "BotLeft", l, h, Sx, Sy, thickness, new Color[] {ColorPalette[7], ColorPalette[0]}, NumberOfCreaturesInWindow) ;
		
		if (creaturesDiscovered != null)
		{
			int SelectedCreature = -1 ;
			for (int cx = 0 ; cx <= NumCreatureWindows[0] - 1 ; cx += 1)
			{
				for (int cy = 0 ; cy <= NumCreatureWindows[1] - 1 ; cy += 1)
				{
					if (cx*NumCreatureWindows[0] + cy + NumberOfCreaturesPerWindow*window < creaturesDiscovered.length)
					{
						int CreatureID = creaturesDiscovered[cx*NumCreatureWindows[0] + cy + NumberOfCreaturesPerWindow*window] ;
						Point InitPos = new Point((int) (Pos.x + offset + cx * (size.x + Sx)), (int) (Pos.y - H + size.y + offset + cy * (size.y + Sy))) ;
						if (Utg.isInside(MousePos, InitPos, size))
						{
							SelectedCreature = CreatureID ;
						}
						//DF.DrawCreature(new Point((int) (InitPos.x + 0.5*l), (int) (InitPos.y - 0.5*h)), new int[] {creatureTypes[CreatureID].getSize()[0]/2, creatureTypes[CreatureID].getSize()[1]/2}, creatureTypes[CreatureID].getimage(), creatureTypes[CreatureID].getColor()) ;
					}
				}
			}
			if (-1 < SelectedCreature)
			{
				//DF.DrawCreatureInfoWindow(new Point(Pos.x + L, Pos.y), creatureTypes[SelectedCreature], items) ;
			}
		}
	}
}
