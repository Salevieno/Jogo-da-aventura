package NPC;

import java.awt.Color;

import main.Palette;

public enum NPCJobs
{
	alchemist (Palette.colors[4]),
	banker (Palette.colors[13]),
	citizen0 (Palette.colors[5]),
	citizen1 (Palette.colors[5]),
	citizen2 (Palette.colors[5]),
	citizen3 (Palette.colors[5]),
	citizen4 (Palette.colors[5]),
	citizen5 (Palette.colors[5]),
	citizen6 (Palette.colors[5]),
	citizen7 (Palette.colors[5]),
	citizen8 (Palette.colors[5]),
	citizen9 (Palette.colors[5]),
	citizen10 (Palette.colors[5]),
	citizen11 (Palette.colors[5]),
	citizen12 (Palette.colors[5]),
	citizen13 (Palette.colors[5]),
	citizen14 (Palette.colors[5]),
	citizen15 (Palette.colors[5]),
	citizen16 (Palette.colors[5]),
	citizen17 (Palette.colors[5]),
	citizen18 (Palette.colors[5]),
	citizen19 (Palette.colors[5]),
	crafter (Palette.colors[21]),
	doctor (Palette.colors[3]),
	elemental (Palette.colors[3]),
	equipsSeller (Palette.colors[20]),
	itemsSeller (Palette.colors[12]),
	smuggleSeller (Palette.colors[17]),
	master (Palette.colors[5]),
	woodcrafter (Palette.colors[8]),
	forger (Palette.colors[21]),
	saver (Palette.colors[19]),
	sailorToIsland (Palette.colors[3]),
	sailorToForest (Palette.colors[3]),
	caveEntry (Palette.colors[5]),
	caveExit (Palette.colors[5]),
	questExp (Palette.colors[5]),
	questItem (Palette.colors[5]);
	
	private final Color color ;
	
	private NPCJobs(Color color)
	{
		this.color = color ;
	}
	
	public Color getColor() { return color ;}
}
