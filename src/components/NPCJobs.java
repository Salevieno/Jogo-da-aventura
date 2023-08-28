package components;

import java.awt.Color;

import main.Game;

public enum NPCJobs
{
	alchemist ("alchemist", Game.colorPalette[4]),
	banker ("banker", Game.colorPalette[13]),
	citizen ("citizen", Game.colorPalette[0]),
	crafter ("crafter", Game.colorPalette[18]),
	doctor ("doctor", Game.colorPalette[7]),
	elemental ("elemental", Game.colorPalette[3]),
	equipsSeller ("equips seller", Game.colorPalette[20]),
	itemsSeller ("items seller", Game.colorPalette[12]),
	smuggleSeller ("smuggle seller", Game.colorPalette[17]),
	master ("master", Game.colorPalette[0]),
	woodcrafter ("woodcrafter", Game.colorPalette[8]),
	forger ("forger", Game.colorPalette[21]),
	saver ("saver", Game.colorPalette[19]),
	sailorToIsland ("sailorToIsland", Game.colorPalette[15]),
	sailorToForest ("sailorToForest", Game.colorPalette[15]),
	caveEntry ("caveEntry", Game.colorPalette[0]),
	caveExit ("caveExit", Game.colorPalette[0]),
	quest ("quest", Game.colorPalette[5]);
	
	private Color color ;
	
	private NPCJobs(String jobName, Color color)
	{
		this.color = color ;
	}
	
	public Color getColor() { return color ;}
}
