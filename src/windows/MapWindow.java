package windows;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import maps.GameMap;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class MapWindow extends GameWindow
{
	private Point playerPos ;
	private GameMap currentMap ;
	
	private static final boolean displayFull = false ;
	private static final Point windowPos = new Point(30, 30) ;
	private static final Image image = UtilS.loadImage("\\Windows\\" + "MapWindow.png") ;
	
	public MapWindow()
	{
		super("Mapa", windowPos, image, 0, 0, 0, 0) ;
	}
	
	public void setPlayerPos(Point playerPos) { this.playerPos = playerPos ;}
	public void setCurrentMap(GameMap currentMap) { this.currentMap = currentMap ;}

	public void navigate(String action)
	{
		
	}
	
	private Point getMapRowCol(String mapName)
	{
		int row = 0 ;
		int col = 0 ;
		switch (mapName)
		{
			case "City of the knights": row = 6 ; col = 0 ; break ;
			case "City of the mages": row = 0 ; col = 4 ; break ;
			case "City of the archers": row = 8 ; col = 8 ; break ;
			case "City of the animals": row = 12 ; col = 8 ; break ;
			case "City of the thieves": row = 0 ; col = 0 ; break ;
			case "Forest 1": row = 0 ; col = 2 ; break ;
			case "Forest 2": row = 2 ; col = 0 ; break ;
			case "Forest 3": row = 2 ; col = 2 ; break ;
			case "Forest 4": row = 2 ; col = 4 ; break ;
			case "Forest 5": row = 2 ; col = 6 ; break ;
			case "Forest 6": row = 4 ; col = 2 ; break ;
			case "Forest 7": row = 4 ; col = 4 ; break ;
			case "Forest 8": row = 4 ; col = 6 ; break ;
			case "Forest 9": row = 4 ; col = 8 ; break ;
			case "Forest 10": row = 6 ; col = 2 ; break ;
			case "Forest 11": row = 6 ; col = 4 ; break ;
			case "Forest 12": row = 6 ; col = 6 ; break ;
			case "Forest 13": row = 6 ; col = 8 ; break ;
			case "Forest 14": row = 8 ; col = 2 ; break ;
			case "Forest 15": row = 8 ; col = 4 ; break ;
			case "Forest 16": row = 8 ; col = 6 ; break ;
			case "Forest 17": row = 10 ; col = 2 ; break ;
			case "Forest 18": row = 10 ; col = 4 ; break ;
			case "Forest 19": row = 10 ; col = 6 ; break ;
			case "Forest 20": row = 10 ; col = 8 ; break ;
			case "Forest 21": row = 12 ; col = 2 ; break ;
			case "Forest 22": row = 12 ; col = 4 ; break ;
			case "Forest 23": row = 12 ; col = 6 ; break ;
			case "Forest 24": row = 14 ; col = 4 ; break ;
			case "Forest 25": row = 14 ; col = 6 ; break ;
			case "Cave 1": row = 2 ; col = 0 ; break ;
			case "Cave 2": row = 2 ; col = 2 ; break ;
			case "Cave 3": row = 2 ; col = 4 ; break ;
			case "Cave 4": row = 0 ; col = 0 ; break ;
			case "Cave 5": row = 0 ; col = 2 ; break ;
			case "Cave 6": row = 0 ; col = 4 ; break ;
			case "Cave 7": row = 1 ; col = 6 ; break ;
			case "Cave 8": row = 2 ; col = 8 ; break ;
			case "Cave 9": row = 0 ; col = 8 ; break ;
			case "Cave 10": row = 2 ; col = 10 ; break ;
			case "Island 1": row = 1 ; col = 0 ; break ;
			case "Island 2": row = 2 ; col = 2 ; break ;
			case "Island 3": row = 0 ; col = 2 ; break ;
			case "Island 4": row = 2 ; col = 4 ; break ;
			case "Island 5": row = 0 ; col = 4 ; break ;
			case "Volcano 1": row = 0 ; col = 4 ; break ;
			case "Volcano 2": row = 2 ; col = 0 ; break ;
			case "Volcano 3": row = 2 ; col = 2 ; break ;
			case "Volcano 4": row = 2 ; col = 4 ; break ;
			case "Volcano 5": row = 4 ; col = 0 ; break ;
			case "Volcano 6": row = 4 ; col = 2 ; break ;
			case "Volcano 7": row = 4 ; col = 4 ; break ;
			case "Volcano 8": row = 6 ; col = 0 ; break ;
			case "Volcano 9": row = 6 ; col = 2 ; break ;
			case "Volcano 10": row = 6 ; col = 4 ; break ;
			case "Snowland 1": row = 2 ; col = 4 ; break ;
			case "Snowland 2": row = 0 ; col = 4 ; break ;
			case "Snowland 3": row = 2 ; col = 2 ; break ;
			case "Snowland 4": row = 0 ; col = 2 ; break ;
			case "Snowland 5": row = 0 ; col = 0 ; break ;
			case "Ocean 1": row = 4 ; col = 0 ; break ;
			case "Ocean 2": row = 4 ; col = 2 ; break ;
			case "Ocean 3": row = 4 ; col = 4 ; break ;
			case "Ocean 4": row = 4 ; col = 6 ; break ;
			case "Ocean 5": row = 2 ; col = 2 ; break ;
			case "Ocean 6": row = 0 ; col = 2 ; break ;
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
		
		return new Point(2*col, 2*row) ;
	}
	
	private Point calcMapOffset(int numberRows, int numberCols, Scale scale, Point spacing)
	{		
		int offsetX = (int) UtilG.calcOffset(numberCols, image.getWidth(null), scale.x * 600, spacing.x) ;
		int offsetY = (int) UtilG.calcOffset(numberRows, image.getHeight(null), scale.y * 384, spacing.y) ;
		return new Point(offsetX, offsetY) ;
	}
	
	public void displayPlayer(Point mapPos, Dimension screenSize, Scale scale, DrawingOnPanel DP)
	{

		double playerRelXPos = playerPos.x / (double) Game.getScreen().getSize().width ;
		double playerRelYPos = playerPos.y / (double) (Game.getScreen().getSize().height) ;
		Point circlePos = UtilG.Translate(mapPos, (int) (scale.x * screenSize.width * playerRelXPos),
				(int) (-scale.y * screenSize.height * (1 - playerRelYPos))) ;
		DP.DrawCircle(circlePos, 5, 0, Game.colorPalette[6], null) ;
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		if (currentMap == null) { return ;}
		
		// full map = 14 x 15 mapas
		// continent maps = max 6 x 8 maps
		Dimension screenSize = Game.getScreen().getSize() ;
		Scale scale = new Scale(0.1, 0.1) ;
		Point offset = new Point(padding + border, padding + border) ;
		Dimension mapSize = new Dimension((int) (600 * scale.x), (int) (384 * scale.y)) ;
		Point spacing = new Point((int) ((640 * scale.x - mapSize.width)), (int) ((480 * scale.y - mapSize.height))) ;
		GameMap[] maps = null ;

		DP.DrawImage(image, windowPos, Align.topLeft) ;

		switch (currentMap.getContinent())
		{
			case forest: maps = GameMap.inForest() ; offset = calcMapOffset(8, 6, scale, spacing) ; break ;
			case cave: maps = GameMap.inCave() ; offset = calcMapOffset(2, 6, scale, spacing) ; break ;
			case island: maps = GameMap.inIsland() ; offset = calcMapOffset(2, 3, scale, spacing) ; break ;
			case ocean: maps = GameMap.inOcean() ; offset = calcMapOffset(3, 4, scale, spacing) ; break ;
			case volcano: maps = GameMap.inVolcano() ; offset = calcMapOffset(4, 3, scale, spacing) ; break ;
			case snowland: maps = GameMap.inSnowland() ; offset = calcMapOffset(2, 3, scale, spacing) ; break ;
//			case special: maps = GameMap.inSpecial() ; offset = calcMapOffset(3, 4, scale, spacing) ; break ;
			default: break ;
		}
		
		if (maps == null) { return ;}
		
		if (displayFull)
		{
			maps = Game.getMaps() ;
			scale = new Scale(0.05, 0.05) ;
			mapSize = new Dimension((int) (600 * scale.x), (int) (384 * scale.y)) ;
			spacing = new Point((int) ((640 * scale.x - mapSize.width)), (int) ((480 * scale.y - mapSize.height))) ;
			offset = calcMapOffset(15, 14, scale, spacing) ;
		}
		
		for (GameMap map : maps)
		{
			Point cell = displayFull ? getMapRowColFullMap(map.getName()): getMapRowCol(map.getName()) ;
			
			if (cell == null) { continue ;}
			
			Point mapPos = UtilG.Translate(windowPos, offset.x + (mapSize.width + spacing.x) * cell.x / 2,
					size.height - offset.y - (mapSize.height + spacing.y) * cell.y / 2) ;
			map.display(mapPos, scale, DP) ;
			Point textPos = UtilG.Translate(mapPos, (int) (scale.x * screenSize.width / 2),
					(int) (-scale.y * screenSize.height / 2)) ;
			DP.DrawText(textPos, Align.center, 0, map.getName(), stdFont, Game.colorPalette[0]) ;
			
			if (!map.equals(currentMap)) { continue ;}
			if (playerPos == null) { continue ;}
			
			displayPlayer(mapPos, screenSize, scale, DP) ;
		}
	}
}
