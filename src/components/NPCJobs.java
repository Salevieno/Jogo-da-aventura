package components;

import java.awt.Color;

import main.Game;

public enum NPCJobs
{	
	// TODO cores
	alchemist ("alchemist", Game.colorPalette[23]),
	banker ("banker", Game.colorPalette[2]),
	citizen ("citizen", Game.colorPalette[3]),
	crafter ("crafter", Game.colorPalette[25]),
	doctor ("doctor", Game.colorPalette[6]),
	elemental ("elemental", Game.colorPalette[7]),
	equipsSeller ("equips seller", Game.colorPalette[0]),
	itemsSeller ("items seller", Game.colorPalette[0]),
	smuggleSeller ("smuggle seller", Game.colorPalette[0]),
	master ("master", Game.colorPalette[0]),
	woodcrafter ("woodcrafter", Game.colorPalette[0]),
	forger ("forger", Game.colorPalette[0]),
	saver ("saver", Game.colorPalette[0]),
	sailorToIsland ("sailorToIsland", Game.colorPalette[0]),
	sailorToForest ("sailorToForest", Game.colorPalette[0]),
	caveEntry ("caveEntry", Game.colorPalette[0]),
	caveExit ("caveExit", Game.colorPalette[0]),
	quest ("quest", Game.colorPalette[0]);
	
	private Color color ;
	
	private NPCJobs(String jobName, Color color)
	{
		this.color = color ;
	}
	
	public Color getColor() { return color ;}
}
