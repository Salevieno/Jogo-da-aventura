package NPC;

import java.awt.Color;

import main.Game;

public enum NPCJobs
{
	alchemist ("alchemist", Game.palette[4]),
	banker ("banker", Game.palette[13]),
	citizen0 ("citizen0", Game.palette[5]),
	citizen1 ("citizen1", Game.palette[5]),
	citizen2 ("citizen2", Game.palette[5]),
	citizen3 ("citizen3", Game.palette[5]),
	citizen4 ("citizen4", Game.palette[5]),
	citizen5 ("citizen5", Game.palette[5]),
	citizen6 ("citizen6", Game.palette[5]),
	citizen7 ("citizen7", Game.palette[5]),
	citizen8 ("citizen8", Game.palette[5]),
	citizen9 ("citizen9", Game.palette[5]),
	citizen10 ("citizen10", Game.palette[5]),
	citizen11 ("citizen11", Game.palette[5]),
	citizen12 ("citizen12", Game.palette[5]),
	citizen13 ("citizen13", Game.palette[5]),
	citizen14 ("citizen14", Game.palette[5]),
	citizen15 ("citizen15", Game.palette[5]),
	citizen16 ("citizen16", Game.palette[5]),
	citizen17 ("citizen17", Game.palette[5]),
	citizen18 ("citizen18", Game.palette[5]),
	citizen19 ("citizen19", Game.palette[5]),
	crafter ("crafter", Game.palette[21]),
	doctor ("doctor", Game.palette[3]),
	elemental ("elemental", Game.palette[3]),
	equipsSeller ("equips seller", Game.palette[20]),
	itemsSeller ("items seller", Game.palette[12]),
	smuggleSeller ("smuggle seller", Game.palette[17]),
	master ("master", Game.palette[5]),
	woodcrafter ("woodcrafter", Game.palette[8]),
	forger ("forger", Game.palette[21]),
	saver ("saver", Game.palette[19]),
	sailorToIsland ("sailorToIsland", Game.palette[3]),
	sailorToForest ("sailorToForest", Game.palette[3]),
	caveEntry ("caveEntry", Game.palette[5]),
	caveExit ("caveExit", Game.palette[5]),
	questExp ("quest exp", Game.palette[5]),
	questItem ("quest item", Game.palette[5]);
	
	private Color color ;
	
	private NPCJobs(String jobName, Color color)
	{
		this.color = color ;
	}
	
	public Color getColor() { return color ;}
}
