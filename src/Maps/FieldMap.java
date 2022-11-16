package Maps;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import GameComponents.MapElements;
import LiveBeings.CreatureTypes;
import LiveBeings.Creatures;
import Main.Game;
import Screen.Screen;
import Utilities.Size;
import Utilities.UtilG;

public class FieldMap extends Maps
{
	private ArrayList<Collectible> collectible ;
	private ArrayList<Creatures> creatures ;
	
	public FieldMap(String name, int continent, int[] connections, Image image, int collectibleLevel, int[] collectibleDelay, int[] creatureTypeIDs)
	{
		super(name, continent, connections, image) ;


		Screen screen = Game.getScreen() ;
		Point minCoord = new Point((int) (0.1 * screen.getSize().x), Game.getSky().height + 10) ;
		Size range = new Size(this.getimage().getWidth(null), screen.getSize().y - Game.getSky().height) ;
		Size step = new Size(1, 1) ;
		for (int me = 0 ; me <= 4 ; ++me)
		{
			Point randomPos = UtilG.RandomPos(minCoord, range, step) ;
			mapElem.add(new MapElements(me, "ForestTree", randomPos, new ImageIcon(Game.ImagesPath + "MapElem6_TreeForest.png").getImage())) ;				
		}
		
		collectible = new ArrayList<Collectible>() ;
		collectible.add(new Collectible(collectibleLevel, 0, collectibleDelay[0])) ;
		collectible.add(new Collectible(collectibleLevel, 0, collectibleDelay[1])) ;
		collectible.add(new Collectible(collectibleLevel, 0, collectibleDelay[2])) ;
		collectible.add(new Collectible(collectibleLevel, 0, collectibleDelay[3])) ;
		
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
	public void setCreatures(ArrayList<Creatures> newValue) {creatures = newValue ;}
	
	public void IncCollectiblesCounter()
	{
		for (int m = 0 ; m <= collectible.size() - 1 ; m += 1)
		{
			if (collectible.get(m).getCounter() < collectible.get(m).getDelay())
			{
				collectible.get(m).incCounter() ;
			}	
		}
	}
	
	public void CreateGroundElement()
	{
		
	}
	
	public void CreateCollectible(int MapID, int CollectibleID)
	{
		Screen screen = Game.getScreen() ;
    	/*if (MapID == 13 | MapID == 17) // shore
		{ 
			RangeX = (float) (0.6) ;
		}*/
    	if (-1 < CollectibleID)
    	{ 	
    		Point minCoord = new Point((int) (0.1 * screen.getSize().x), Game.getSky().height + 10) ;
    		Size range = new Size(this.getimage().getWidth(null), screen.getSize().y - Game.getSky().height) ;
    		Size step = new Size(1, 1) ;
    		
    		Point randomPos = UtilG.RandomPos(minCoord, range, step) ;
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
