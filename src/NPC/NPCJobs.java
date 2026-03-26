package NPC;

import java.awt.Color;

import main.Game;

public enum NPCJobs
{
	alchemist (Game.palette[4]),
	banker (Game.palette[13]),
	citizen0 (Game.palette[5]),
	citizen1 (Game.palette[5]),
	citizen2 (Game.palette[5]),
	citizen3 (Game.palette[5]),
	citizen4 (Game.palette[5]),
	citizen5 (Game.palette[5]),
	citizen6 (Game.palette[5]),
	citizen7 (Game.palette[5]),
	citizen8 (Game.palette[5]),
	citizen9 (Game.palette[5]),
	citizen10 (Game.palette[5]),
	citizen11 (Game.palette[5]),
	citizen12 (Game.palette[5]),
	citizen13 (Game.palette[5]),
	citizen14 (Game.palette[5]),
	citizen15 (Game.palette[5]),
	citizen16 (Game.palette[5]),
	citizen17 (Game.palette[5]),
	citizen18 (Game.palette[5]),
	citizen19 (Game.palette[5]),
	crafter (Game.palette[21]),
	doctor (Game.palette[3]),
	elemental (Game.palette[3]),
	equipsSeller (Game.palette[20]),
	itemsSeller (Game.palette[12]),
	smuggleSeller (Game.palette[17]),
	master (Game.palette[5]),
	woodcrafter (Game.palette[8]),
	forger (Game.palette[21]),
	saver (Game.palette[19]),
	sailorToIsland (Game.palette[3]),
	sailorToForest (Game.palette[3]),
	caveEntry (Game.palette[5]),
	caveExit (Game.palette[5]),
	questExp (Game.palette[5]),
	questItem (Game.palette[5]);
	
	private final Color color ;
	
	private NPCJobs(Color color)
	{
		this.color = color ;
	}
	
	public Color getColor() { return color ;}
}
