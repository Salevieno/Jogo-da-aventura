package Maps;

import java.awt.Image;
import java.util.ArrayList;

import LiveBeings.CreatureTypes;
import LiveBeings.Creatures;
import Main.Game;
import Screen.Screen;

public class FieldMap extends Maps
{
	private Collectible[] collectible ;
	private ArrayList<Creatures> creatures ;
	
	public FieldMap(String name, int continent, int[] connections, Image image, int collectibleLevel, int[] collectibleDelay, int[] creatureTypeIDs)
	{
		super(name, continent, connections, image) ;
		collectible = new Collectible[4] ;
		collectible[0] = new Collectible(collectibleLevel, 0, collectibleDelay[0]) ;
		collectible[1] = new Collectible(collectibleLevel, 0, collectibleDelay[1]) ;
		collectible[2] = new Collectible(collectibleLevel, 0, collectibleDelay[2]) ;
		collectible[3] = new Collectible(collectibleLevel, 0, collectibleDelay[3]) ;
		
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
		for (int m = 0 ; m <= collectible.length - 1 ; m += 1)
		{
			if (collectible[m].getCounter() < collectible[m].getDelay())
			{
				collectible[m].incCounter() ;
			}	
		}
	}
	
	public void CreateCollectible(int MapID, int CollectibleID)
	{
		Screen screen = Game.getScreen() ;
		float MinX = (float) (0.1), MinY = (float) ((float) screen.getSize().y / (screen.getBorders()[1] - screen.getBorders()[3]) + 0.1) ; 
    	float RangeX = (float) (0.8), RangeY = (float) (1 - MinY) ;
    	if (MapID == 13 | MapID == 17)
		{ 
			RangeX = (float) (0.6) ;
		}
    	//String[] CollectibleNames = new String[] {"Berry", "Herb", "Wood", "Metal"} ;
    	//int step = 1 ;
    	if (-1 < CollectibleID)
    	{ 	
    		//int[] randomPos = new int[] {Utg.RandomCoord1D(screen.getDimensions()[0], MinX, RangeX, 1), Utg.RandomCoord1D(screen.getDimensions()[1], MinY, RangeY, 1)} ;
			//invisible_wall = Utg.AddElem(invisible_wall, new Point(randomPos[0], randomPos[1])) ;	// collider
    	}
	}
}
