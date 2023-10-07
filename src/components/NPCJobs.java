package components;

import java.awt.Color;

import main.Game;

public enum NPCJobs
{
	alchemist ("alchemist", Game.colorPalette[4]),
	banker ("banker", Game.colorPalette[13]),
	citizen0 ("citizen0", Game.colorPalette[1]),
	citizen1 ("citizen1", Game.colorPalette[1]),
	citizen2 ("citizen2", Game.colorPalette[1]),
	citizen3 ("citizen3", Game.colorPalette[1]),
	citizen4 ("citizen4", Game.colorPalette[1]),
	citizen5 ("citizen5", Game.colorPalette[1]),
	citizen6 ("citizen6", Game.colorPalette[1]),
	citizen7 ("citizen7", Game.colorPalette[1]),
	citizen8 ("citizen8", Game.colorPalette[1]),
	citizen9 ("citizen9", Game.colorPalette[1]),
	citizen10 ("citizen10", Game.colorPalette[1]),
	citizen11 ("citizen11", Game.colorPalette[1]),
	citizen12 ("citizen12", Game.colorPalette[1]),
	citizen13 ("citizen13", Game.colorPalette[1]),
	citizen14 ("citizen14", Game.colorPalette[1]),
	citizen15 ("citizen15", Game.colorPalette[1]),
	citizen16 ("citizen16", Game.colorPalette[1]),
	citizen17 ("citizen17", Game.colorPalette[1]),
	citizen18 ("citizen18", Game.colorPalette[1]),
	citizen19 ("citizen19", Game.colorPalette[1]),
	crafter ("crafter", Game.colorPalette[18]),
	doctor ("doctor", Game.colorPalette[7]),
	elemental ("elemental", Game.colorPalette[3]),
	equipsSeller ("equips seller", Game.colorPalette[20]),
	itemsSeller ("items seller", Game.colorPalette[12]),
	smuggleSeller ("smuggle seller", Game.colorPalette[17]),
	master ("master", Game.colorPalette[1]),
	woodcrafter ("woodcrafter", Game.colorPalette[8]),
	forger ("forger", Game.colorPalette[21]),
	saver ("saver", Game.colorPalette[19]),
	sailorToIsland ("sailorToIsland", Game.colorPalette[15]),
	sailorToForest ("sailorToForest", Game.colorPalette[15]),
	caveEntry ("caveEntry", Game.colorPalette[1]),
	caveExit ("caveExit", Game.colorPalette[1]),
	questExp ("quest exp", Game.colorPalette[5]),
	questItem ("quest item", Game.colorPalette[5]);
	
	private Color color ;
	
	private NPCJobs(String jobName, Color color)
	{
		this.color = color ;
	}
	
	public Color getColor() { return color ;}
}
