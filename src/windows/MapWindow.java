package windows;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import maps.GameMap;
import screen.Sky;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class MapWindow extends GameWindow
{
	private Point windowPos = new Point(30, 30) ;
	private static final Image image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "MapWindow.png") ;
	
	public MapWindow()
	{
		super("Mapa", image, 0, 0, 0, 0) ;
	}

	public void navigate(String action)
	{
		
	}
	
	private Point getMapRowCol(String mapName)
	{
		int row = 0 ;
		int col = 0 ;
		switch (mapName)
		{
			case "City of the knights": row = 3 ; col = 0 ; break ;
			case "City of the mages": row = 0 ; col = 2 ; break ;
			case "City of the archers": row = 4 ; col = 4 ; break ;
			case "City of the animals": row = 6 ; col = 4 ; break ;
			case "City of the thieves": row = 0 ; col = 0 ; break ;
			case "Forest 1": row = 0 ; col = 1 ; break ;
			case "Forest 2": row = 1 ; col = 0 ; break ;
			case "Forest 3": row = 1 ; col = 1 ; break ;
			case "Forest 4": row = 1 ; col = 2 ; break ;
			case "Forest 5": row = 1 ; col = 3 ; break ;
			case "Forest 6": row = 2 ; col = 1 ; break ;
			case "Forest 7": row = 2 ; col = 2 ; break ;
			case "Forest 8": row = 2 ; col = 3 ; break ;
			case "Forest 9": row = 2 ; col = 4 ; break ;
			case "Forest 10": row = 3 ; col = 1 ; break ;
			case "Forest 11": row = 3 ; col = 2 ; break ;
			case "Forest 12": row = 3 ; col = 3 ; break ;
			case "Forest 13": row = 3 ; col = 4 ; break ;
			case "Forest 14": row = 4 ; col = 1 ; break ;
			case "Forest 15": row = 4 ; col = 2 ; break ;
			case "Forest 16": row = 4 ; col = 3 ; break ;
			case "Forest 17": row = 5 ; col = 1 ; break ;
			case "Forest 18": row = 5 ; col = 2 ; break ;
			case "Forest 19": row = 5 ; col = 3 ; break ;
			case "Forest 20": row = 5 ; col = 4 ; break ;
			case "Forest 21": row = 6 ; col = 1 ; break ;
			case "Forest 22": row = 6 ; col = 2 ; break ;
			case "Forest 23": row = 6 ; col = 3 ; break ;
			case "Forest 24": row = 7 ; col = 2 ; break ;
			case "Forest 25": row = 7 ; col = 3 ; break ;
			case "Cave 1": row = 2 ; col = 0 ; break ;
			case "Cave 2": row = 2 ; col = 2 ; break ;
			case "Cave 3": row = 2 ; col = 4 ; break ;
			case "Cave 4": row = 0 ; col = 0 ; break ;
			case "Cave 5": row = 0 ; col = 2 ; break ;
			case "Cave 6": row = 0 ; col = 4 ; break ;
			case "Cave 7": row = 1 ; col = 6 ; break ;
			case "Cave 8": row = 2 ; col = 8 ; break ;
			case "Cave 9": row = 0 ; col = 8 ; break ;
			case "Cave 10": row = 1 ; col = 10 ; break ;
			case "Island 1": row = 0 ; col = 0 ; break ;
			case "Island 2": row = 2 ; col = 1 ; break ;
			case "Island 3": row = 1 ; col = 1 ; break ;
			case "Island 4": row = 2 ; col = 2 ; break ;
			case "Island 5": row = 1 ; col = 2 ; break ;
			case "Volcano 1": row = 0 ; col = 2 ; break ;
			case "Volcano 2": row = 1 ; col = 0 ; break ;
			case "Volcano 3": row = 1 ; col = 1 ; break ;
			case "Volcano 4": row = 1 ; col = 2 ; break ;
			case "Volcano 5": row = 2 ; col = 0 ; break ;
			case "Volcano 6": row = 2 ; col = 1 ; break ;
			case "Volcano 7": row = 2 ; col = 2 ; break ;
			case "Volcano 8": row = 3 ; col = 0 ; break ;
			case "Volcano 9": row = 3 ; col = 1 ; break ;
			case "Volcano 10": row = 3 ; col = 2 ; break ;
			case "Snowland 1": row = 1 ; col = 2 ; break ;
			case "Snowland 2": row = 0 ; col = 2 ; break ;
			case "Snowland 3": row = 1 ; col = 1 ; break ;
			case "Snowland 4": row = 0 ; col = 1 ; break ;
			case "Snowland 5": row = 0 ; col = 0 ; break ;
			case "Ocean 1": row = 2 ; col = 0 ; break ;
			case "Ocean 2": row = 2 ; col = 1 ; break ;
			case "Ocean 3": row = 2 ; col = 2 ; break ;
			case "Ocean 4": row = 2 ; col = 3 ; break ;
			case "Ocean 5": row = 1 ; col = 1 ; break ;
			case "Ocean 6": row = 0 ; col = 1 ; break ;
			default: return null ;
		}
		
		return new Point(col, row) ;
	}
	
	private Point getMapRowColFullMap(String mapName)
	{
		int row = 0 ;
		int col = 0 ;
		switch (mapName)
		{
			case "City of the knights": row = 6 ; col = 2 ; break ;
			case "City of the mages": row = 3 ; col = 4 ; break ;
			case "City of the archers": row = 7 ; col = 6 ; break ;
			case "City of the animals": row = 9 ; col = 6 ; break ;
			case "City of the thieves": row = 3 ; col = 2 ; break ;
			case "Forest 1": row = 3 ; col = 3 ; break ;
			case "Forest 2": row = 4 ; col = 2 ; break ;
			case "Forest 3": row = 4 ; col = 3 ; break ;
			case "Forest 4": row = 4 ; col = 4 ; break ;
			case "Forest 5": row = 4 ; col = 5 ; break ;
			case "Forest 6": row = 5 ; col = 3 ; break ;
			case "Forest 7": row = 5 ; col = 4 ; break ;
			case "Forest 8": row = 5 ; col = 5 ; break ;
			case "Forest 9": row = 5 ; col = 6 ; break ;
			case "Forest 10": row = 6 ; col = 3 ; break ;
			case "Forest 11": row = 6 ; col = 4 ; break ;
			case "Forest 12": row = 6 ; col = 5 ; break ;
			case "Forest 13": row = 6 ; col = 6 ; break ;
			case "Forest 14": row = 7 ; col = 3 ; break ;
			case "Forest 15": row = 7 ; col = 4 ; break ;
			case "Forest 16": row = 7 ; col = 5 ; break ;
			case "Forest 17": row = 8 ; col = 3 ; break ;
			case "Forest 18": row = 8 ; col = 4 ; break ;
			case "Forest 19": row = 8 ; col = 5 ; break ;
			case "Forest 20": row = 8 ; col = 6 ; break ;
			case "Forest 21": row = 9 ; col = 3 ; break ;
			case "Forest 22": row = 9 ; col = 4 ; break ;
			case "Forest 23": row = 9 ; col = 5 ; break ;
			case "Forest 24": row = 10 ; col = 4 ; break ;
			case "Forest 25": row = 10 ; col = 5 ; break ;
			case "Cave 1": row = 1 ; col = 2 ; break ;
			case "Cave 2": row = 1 ; col = 3 ; break ;
			case "Cave 3": row = 1 ; col = 4 ; break ;
			case "Cave 4": row = 0 ; col = 2 ; break ;
			case "Cave 5": row = 0 ; col = 3 ; break ;
			case "Cave 6": row = 0 ; col = 4 ; break ;
			case "Cave 7": row = 1 ; col = 5 ; break ;
			case "Cave 8": row = 1 ; col = 6 ; break ;
			case "Cave 9": row = 0 ; col = 6 ; break ;
			case "Cave 10": row = 1 ; col = 7 ; break ;
			case "Island 1": row = 6 ; col = 11 ; break ;
			case "Island 2": row = 7 ; col = 12 ; break ;
			case "Island 3": row = 6 ; col = 12 ; break ;
			case "Island 4": row = 7 ; col = 13 ; break ;
			case "Island 5": row = 6 ; col = 13 ; break ;
			case "Volcano 1": row = 10 ; col = 8 ; break ;
			case "Volcano 2": row = 11 ; col = 6 ; break ;
			case "Volcano 3": row = 11 ; col = 7 ; break ;
			case "Volcano 4": row = 11 ; col = 8 ; break ;
			case "Volcano 5": row = 12 ; col = 6 ; break ;
			case "Volcano 6": row = 12 ; col = 7 ; break ;
			case "Volcano 7": row = 12 ; col = 8 ; break ;
			case "Volcano 8": row = 13 ; col = 6 ; break ;
			case "Volcano 9": row = 13 ; col = 7 ; break ;
			case "Volcano 10": row = 13 ; col = 8 ; break ;
			case "Snowland 1": row = 10 ; col = 2 ; break ;
			case "Snowland 2": row = 9 ; col = 2 ; break ;
			case "Snowland 3": row = 10 ; col = 1 ; break ;
			case "Snowland 4": row = 9 ; col = 1 ; break ;
			case "Snowland 5": row = 9 ; col = 0 ; break ;
			case "Ocean 1": row = 6 ; col = 7 ; break ;
			case "Ocean 2": row = 6 ; col = 8 ; break ;
			case "Ocean 3": row = 6 ; col = 9 ; break ;
			case "Ocean 4": row = 6 ; col = 10 ; break ;
			case "Ocean 5": row = 5 ; col = 8 ; break ;
			case "Ocean 6": row = 4 ; col = 8 ; break ;
			default: return null ;
		}
		
		return new Point(col, row) ;
	}
	
	public void display(Point playerPos, GameMap playerMap, DrawingOnPanel DP)
	{
		// full map = 14 x 14 mapas
		// continent maps = max 6 x 8 maps
		Dimension screenSize = Game.getScreen().getSize() ;
		Scale scale = new Scale(0.1, 0.1) ;
		Point offset = new Point(18 + border, 8 + border) ;
		Point spacing = new Point ((int) (640 * scale.x), (int) (480 * scale.y)) ;
		GameMap[] maps = null ;
		
		DP.DrawImage(image, windowPos, Align.topLeft) ;

		switch (playerMap.getContinent())
		{
			case forest: maps = GameMap.inForest() ; break ;
			case cave: maps = GameMap.inCave() ; break ;
			case island: maps = GameMap.inIsland() ; break ;
			case ocean: maps = GameMap.inOcean() ; break ;
			case volcano: maps = GameMap.inVolcano() ; break ;
			case snowland: maps = GameMap.inSnowland() ; break ;
			case special: maps = GameMap.inSpecial() ; break ;
			default: break ;
		}
		
		if (maps == null) { return ;}
		
//		maps = Game.getMaps() ;
//		scale = new Scale(0.05, 0.05) ;
//		spacing = new Point ((int) (640 * scale.x), (int) (480 * scale.y)) ;
		
		spacing = new Point ((int) (320 * scale.x), (int) (240 * scale.y)) ;
		
		for (GameMap map : maps)
		{
			Point cell = getMapRowCol(map.getName()) ;
			
			if (cell == null) { continue ;}
			
			Point mapPos = UtilG.Translate(windowPos, offset.x + spacing.x * cell.x, size.height - offset.y - spacing.y * cell.y) ;
			map.display(mapPos, scale, DP) ;
			Point textPos = UtilG.Translate(mapPos, (int) (scale.x * screenSize.width / 2), (int) (-scale.y * screenSize.height / 2)) ;
			DP.DrawText(textPos, Align.center, 0, map.getName(), stdFont, Game.colorPalette[9]) ;
			
			if (!map.equals(playerMap)) { continue ;}
			
			double playerRelXPos = playerPos.x / (double) Game.getScreen().getSize().width ;
			double playerRelYPos = playerPos.y / (double) (Game.getScreen().getSize().height) ;
			Point circlePos = UtilG.Translate(mapPos, (int) (scale.x * screenSize.width * playerRelXPos), (int) (-scale.y * screenSize.height * (1 - playerRelYPos))) ;
			DP.DrawCircle(circlePos, 5, 0, Game.colorPalette[6], null) ;
		}
	}
}
