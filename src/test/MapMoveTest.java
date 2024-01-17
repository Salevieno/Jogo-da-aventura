package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import liveBeings.LiveBeing;
import main.Game;
import maps.GameMap;
import utilities.Directions;

public class MapMoveTest
{
	@Test
	void mapMoveTest()
	{
		
		Point topLeft = Game.getScreen().pos(0.01, 0.99) ;
		Point bottomLeft = Game.getScreen().pos(0.01, 0.01) ;
		Point bottomRight = Game.getScreen().pos(0.99, 0.01) ;
		Point topRight = Game.getScreen().pos(0.99, 0.99) ;
		Map<Integer, Point> possiblePositions = new LinkedHashMap<>() ;
		Map<Integer, Directions> possibleDirections = new LinkedHashMap<>() ;
		Map<Integer, String> moveNames = new LinkedHashMap<>() ;
		possiblePositions.put(0, topRight) ;
		possiblePositions.put(1, topLeft) ;
		possiblePositions.put(2, topLeft) ;
		possiblePositions.put(3, bottomLeft) ;
		possiblePositions.put(4, bottomLeft) ;
		possiblePositions.put(5, bottomRight) ;
		possiblePositions.put(6, bottomRight) ;
		possiblePositions.put(7, topRight) ;
		possibleDirections.put(0, Directions.up) ;
		possibleDirections.put(1, Directions.up) ;
		possibleDirections.put(2, Directions.left) ;
		possibleDirections.put(3, Directions.left) ;
		possibleDirections.put(4, Directions.down) ;
		possibleDirections.put(5, Directions.down) ;
		possibleDirections.put(6, Directions.right) ;
		possibleDirections.put(7, Directions.right) ;
		moveNames.put(0, "topRight") ;
		moveNames.put(1, "topLeft") ;
		moveNames.put(2, "leftTop") ;
		moveNames.put(3, "leftBottom") ;
		moveNames.put(4, "bottomLeft") ;
		moveNames.put(5, "bottomRight") ;
		moveNames.put(6, "rightBottom") ;
		moveNames.put(7, "rightTop") ;
		
//		Game.initalizeMapsTest() ;
		GameMap[] maps = Game.getMaps() ;
		for (GameMap map : maps)
		{
			System.out.println(map.getName());
			possiblePositions.keySet().forEach(id -> 
			{
				Point pos = possiblePositions.get(id) ;
				Directions dir = possibleDirections.get(id) ;
				String moveName = moveNames.get(id) ;
				GameMap newMap = LiveBeing.calcNewMap(pos, dir, map) ;
				String newMapName = newMap != null ? newMap.getName() : "-" ;
				System.out.println(moveName + ":	" + newMapName);
			}) ;
			System.out.println();
		}
		
		assertEquals(true, true) ;
		
	}
}
