package Maps;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import GameComponents.MapElements;
import Graphics.DrawPrimitives;
import LiveBeings.CreatureTypes;
import LiveBeings.Creatures;
import Main.Game;
import Screen.Screen;
import Utilities.Size;
import Utilities.UtilG;

public class FieldMap extends Maps
{
	private ArrayList<Collectible> collectibles ;
	private ArrayList<Creatures> creatures ;
	private int level ;
	private int[] collectibleDelay ;
	
	public FieldMap(String name, int continent, int[] connections, Image image, int collectibleLevel, int[] collectibleDelay, int[] creatureTypeIDs)
	{
		super(name, continent, connections, image, null, null) ;
		this.level = collectibleLevel ;
		this.collectibleDelay = collectibleDelay ;
		
		
		// add map elements
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point((int) (0.1 * screen.getSize().x), Game.getSky().height + 10) ;
		Size range = new Size(this.getimage().getWidth(null), screen.getSize().y - Game.getSky().height) ;
		Size step = new Size(1, 1) ;
		for (int me = 0 ; me <= 4 ; ++me)
		{
			Point randomPos = UtilG.RandomPos(minCoord, range, step) ;
			mapElem.add(new MapElements(me, "ForestTree", randomPos, new ImageIcon(Game.ImagesPath + "MapElem6_TreeForest.png").getImage())) ;				
		}
		
		
		// add collectbiles
		collectibles = new ArrayList<Collectible>() ;
		AddCollectibles() ;
		
		
		// add creatures
		creatures = new ArrayList<Creatures>() ;
		for (int c = 0 ; c <= creatureTypeIDs.length - 1 ; c += 1)
		{
			if (-1 < creatureTypeIDs[c])
			{
				CreatureTypes creatureType = Game.getCreatureTypes()[creatureTypeIDs[c]];
				Creatures creature = new Creatures(creatureType) ;
				creatures.add(creature) ;
			}
		}
	}

	public ArrayList<Creatures> getCreatures() {return creatures ;}
	public ArrayList<Collectible> getCollectibles() {return collectibles ;}
	public void setCreatures(ArrayList<Creatures> newValue) {creatures = newValue ;}
	
	public void IncCollectiblesCounter()
	{
		for (int m = 0 ; m <= collectibles.size() - 1 ; m += 1)
		{
			if (collectibles.get(m).getCounter() <= collectibles.get(m).getDelay())
			{
				collectibles.get(m).incCounter() ;
			}	
		}
	}
	
	public void ActivateCollectiblesCounter()
	{
		for (int m = 0 ; m <= collectibles.size() - 1 ; m += 1)
		{
			if (collectibles.get(m).getCounter() == collectibles.get(m).getDelay() - 1)
			{
				AddCollectibles() ;
			}	
		}
	}
	
	public void CreateGroundElement()
	{
		
	}
	
	public void AddCollectibles()
	{
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().y)) ;
		Size range = new Size(Game.getScreen().getSize().x, (int) ((1 - (float)(Game.getSky().height)/Game.getScreen().getSize().y) * Game.getScreen().getSize().y)) ;
		
		collectibles.add(new Collectible(0, level, UtilG.RandomPos(minCoord, range, new Size(1, 1)), 0, collectibleDelay[0])) ;
		collectibles.add(new Collectible(1, level, UtilG.RandomPos(minCoord, range, new Size(1, 1)), 0, collectibleDelay[1])) ;
		collectibles.add(new Collectible(2, level, UtilG.RandomPos(minCoord, range, new Size(1, 1)), 0, collectibleDelay[2])) ;
		collectibles.add(new Collectible(3, level, UtilG.RandomPos(minCoord, range, new Size(1, 1)), 0, collectibleDelay[3])) ;
	} 	
	
 	public void displayCollectibles(DrawPrimitives DP)
 	{
 		for (int col = 0 ; col <= collectibles.size() - 1 ; col += 1)
		{
 			collectibles.get(col).display(DP) ;
		}
 	}

	public void displayGroundType()
	{
		/*if (groundType != null)
		{
			for (int gt = 0 ; gt <= groundType.length - 1 ; gt += 1)
			{
				Object[] o = (Object[]) groundType[gt] ;
				String type = (String) o[0] ;
				Point p = (Point) o[1] ;
				if (type.equals("water"))
				{
					DP.DrawRect(new Point(p.x, p.y), "center", new Size(10, 10), 0, Color.blue, null, false);
				}
			}
		}*/
	}
	
 	
 	
}