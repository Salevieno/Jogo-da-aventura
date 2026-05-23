package windows;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import main.GamePanel;
import main.ImageLoader;
import main.Log;
import main.Palette;
import main.Path;
import maps.GameMap;
import screen.Screen;
import screen.Sky;
import utilities.Util;


public class MapWindow extends GameWindow
{
	private Scale scale ;
	private Point offset ;
	private Point playerPos ;
	private Dimension mapSize ;
	private GameMap mapWithPlayer ; // TODO verificar se precisa de gamemap
	private List<GameMap> mapsDisplayed ;
	private final Point spacing ;
	
	private static final boolean FULL_MAP = false ;
	
	public MapWindow()
	{
		super("Mapa", new Point(150, 100), ImageLoader.loadImage(Path.WINDOWS_IMG + "MapWindow.png"), 0, 0, 0, 0) ;
		this.mapsDisplayed = new ArrayList<>() ;
		this.scale = new Scale(0.1, 0.1) ;
		this.spacing = new Point(6, 6) ;
	}

	public void update(Point playerPos, GameMap mapWithPlayer)
	{
		this.playerPos = playerPos ;
		this.mapWithPlayer = mapWithPlayer ;
		this.scale = new Scale(0.1, 0.1) ;
		this.mapSize = new Dimension((int) (Screen.getMe().mapSize().width * scale.x), (int) (Screen.getMe().mapSize().height * scale.y)) ;
		this.mapsDisplayed = GameMap.getAllMaps().stream().filter(map -> mapWithPlayer.getContinent().equals(map.getContinent())).toList() ;
		this.offset = switch(mapWithPlayer.getContinent())
		{
			case forest -> calcMapOffset(8, 6, scale, spacing) ;
			case cave -> calcMapOffset(2, 6, scale, spacing) ;
			case island -> calcMapOffset(2, 3, scale, spacing) ;
			case ocean -> calcMapOffset(3, 4, scale, spacing) ;
			case volcano -> calcMapOffset(4, 3, scale, spacing) ;
			case snowland -> calcMapOffset(2, 3, scale, spacing) ;
			case special -> calcMapOffset(3, 4, scale, spacing) ;
			default -> new Point() ;
		};
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
		int offsetX = (int) Util.calcOffset(numberCols, image.getWidth(null), scale.x * GameMap.width(), spacing.x) ;
		int offsetY = (int) Util.calcOffset(numberRows, image.getHeight(null), scale.y * GameMap.height(), spacing.y) ;
		return new Point(offsetX, offsetY) ;
	}
	
	public void displayPlayerLocation(Point mapPos, Dimension screenSize)
	{
		double playerRelXPos = playerPos.x / (double) Screen.getMe().mapSize().width ;
		double playerRelYPos = (playerPos.y - Sky.getHeight()) / (double) Screen.getMe().mapSize().height ;
		Point circlePos = Util.translate(mapPos, (int) (screenSize.width * playerRelXPos), (int) (-screenSize.height * (1 - playerRelYPos))) ;
		GamePanel.getDP().drawCircle(circlePos, 5, 0, Palette.colors[6], null) ;
	}
	
	public void display(Point mousePos)
	{		
		// full map = 14 x 15 mapas
		// continent maps = 6 x 8 maps (max)
		
		GamePanel.getDP().drawImage(image, topLeftPos, Align.topLeft) ;
		
		if (mapWithPlayer == null) { Log.warn("Map with player = null when displaying map") ; return ;}
		if (mapsDisplayed == null) { Log.warn("Maps displayed = null when displaying map") ; return ;}
		if (mapsDisplayed.isEmpty()) { Log.warn("Maps displayed = empty when displaying map") ; return ;}
		
		if (FULL_MAP)
		{
			mapsDisplayed = GameMap.getAllMaps() ;
			scale = new Scale(0.05, 0.05) ;
			mapSize = new Dimension((int) (Screen.getMe().mapSize().width * scale.x), (int) (Screen.getMe().mapSize().height * scale.y)) ;
			offset = calcMapOffset(15, 14, scale, spacing) ;
		}
		
		for (GameMap map : mapsDisplayed)
		{
			Point cell = FULL_MAP ? getMapRowColFullMap(map.getName()) : getMapRowCol(map.getName()) ;
	
			if (cell == null) { Log.warn("cell = null when displaying map") ; continue ;}
			
			Point mapPos = Util.translate(topLeftPos, offset.x + (mapSize.width + spacing.x) * cell.x / 2,
													size.height - offset.y - (mapSize.height + spacing.y) * cell.y / 2) ;
			map.display(mapPos, Align.bottomLeft, scale) ;

			Point mapNamePos = Util.translate(mapPos, (int) (scale.x * Screen.getMe().mapSize().width / 2),
													(int) (-scale.y * Screen.getMe().mapSize().height / 2)) ;
			GamePanel.getDP().drawText(mapNamePos, Align.center, 0, map.getName(), STD_FONT, Palette.colors[0]) ;
			
			if (!map.equals(mapWithPlayer)) { continue ;}
			if (playerPos == null) { continue ;}

			displayPlayerLocation(mapPos, mapSize) ;
		}
	}
}
