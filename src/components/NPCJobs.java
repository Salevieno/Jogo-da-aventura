package components;

import java.awt.Color;

import main.Game;

public enum NPCJobs
{	
	// TODO cores
	alchemist ("alchemist", Game.ColorPalette[23]),
	banker ("banker", Game.ColorPalette[2]),
	citizen ("citizen", Game.ColorPalette[3]),
	crafter ("crafter", Game.ColorPalette[25]),
	doctor ("doctor", Game.ColorPalette[6]),
	elemental ("elemental", Game.ColorPalette[7]),
	equipsSeller ("equips seller", Game.ColorPalette[0]),
	itemsSeller ("items seller", Game.ColorPalette[0]),
	smuggleSeller ("smuggle seller", Game.ColorPalette[0]),
	master ("master", Game.ColorPalette[0]),
	woodcrafter ("woodcrafter", Game.ColorPalette[0]),
	forger ("forger", Game.ColorPalette[0]),
	saver ("saver", Game.ColorPalette[0]),
	sailorToIsland ("sailorToIsland", Game.ColorPalette[0]),
	sailorToForest ("sailorToForest", Game.ColorPalette[0]),
	caveEntry ("caveEntry", Game.ColorPalette[0]),
	caveExit ("caveExit", Game.ColorPalette[0]),
	quest ("quest", Game.ColorPalette[0]);
	
	private Color color ;
	
	private NPCJobs(String jobName, Color color)
	{
		this.color = color ;
	}
	
	public Color getColor() { return color ;}
}
