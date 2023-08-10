package maps;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.Clip;

import components.NPCs;
import graphics.DrawingOnPanel;
import items.Item;
import liveBeings.Creature;
import liveBeings.CreatureType;
import main.Game;
import screen.Screen;
import utilities.Align;
import utilities.UtilG;

public class FieldMap extends GameMap
{
	private List<Collectible> collectibles ;
	private List<Creature> creatures ;
	private int level ;
	private int[] collectibleDelay ;
	
	public FieldMap(String name, Continents continent, int[] connections, Image image, Clip music, int collectibleLevel, int[] collectibleDelay, int[] creatureTypeIDs, List<NPCs> npcs)
	{
		super(name, continent, connections, image, music, null, npcs) ;
		this.level = collectibleLevel ;
		this.collectibleDelay = collectibleDelay ;
		
		
		// add map elements
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point(20, Game.getSky().height + 20) ;
		Dimension range = new Dimension(screen.getSize().width - 40, screen.getSize().height - Game.getSky().height - 40) ;
		Dimension step = new Dimension(1, 1) ;
		
		int numberTrees = 5 ;
		int numberGrass = 20 ;
		Image treeImage = UtilG.loadImage(Game.ImagesPath + "\\MapElements\\" + "MapElem6_TreeForest.png") ;
		Image grassImage = UtilG.loadImage(Game.ImagesPath + "\\MapElements\\" + "MapElem8_Grass.png") ;

		for (int me = 0 ; me <= numberTrees - 1 ; me += 1)
		{
			Point randomPos = UtilG.RandomPos(minCoord, range, step) ;
			mapElems.add(new MapElements(me, "ForestTree", randomPos, treeImage)) ;				
		}
		for (int me = 0 ; me <= numberGrass - 1 ; me += 1)
		{
			Point randomPos = UtilG.RandomPos(minCoord, range, step) ;
			mapElems.add(new MapElements(me, "grass", randomPos, grassImage)) ;				
		}
		
		
		// add collectbiles
		collectibles = new ArrayList<Collectible>() ;
		AddCollectibles() ;
		
		
		// add creatures
		creatures = new ArrayList<Creature>() ;			
		for (int creatureTypeID : creatureTypeIDs)
		{
			if (-1 < creatureTypeID)
			{
				CreatureType creatureType = Game.getCreatureTypes()[creatureTypeID];
				Creature creature = new Creature(creatureType) ;
				creatures.add(creature) ;
			}
		}
		
		// TODO incluir os digging items para as cidades e mapas especiais
		for (Item item : allDiggingItems.keySet())
		{
			if (!containsItem(item)) { continue ;}
			
			diggingItems.put(item, allDiggingItems.get(item)) ;
		}
		calcDigItemChances() ;
//		System.out.println(name);
//		System.out.println(diggingItems);
		
		// add npcs
		this.npcs = npcs ;
	}

	public List<Creature> getCreatures() {return creatures ;}
	public List<Collectible> getCollectibles() {return collectibles ;}
	public void setCreatures(List<Creature> newValue) {creatures = newValue ;}
	
	public boolean hasCreatures() { return creatures != null ;}
	
	public void IncCollectiblesCounter()
	{
		for (Collectible collectible : collectibles)
		{
			collectible.getCounter().inc() ;
//			if (collectible.getCounter() <= collectible.getDelay()) { }
		}
	}
	
	public void ActivateCollectiblesCounter()
	{
		int numberCollectibles = collectibles.size() ;
		for (int i = 0 ; i <= numberCollectibles - 1 ; i += 1)
		{
			if (collectibles.get(i).getCounter().finished()) { AddCollectibles() ; collectibles.get(i).getCounter().reset() ;}
		}
	}
	
	public void CreateGroundElement()
	{
		
	}
	
	public void AddCollectibles()
	{
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().height)) ;
		Dimension range = new Dimension(Game.getScreen().getSize().width, (int) ((1 - (float)(Game.getSky().height)/Game.getScreen().getSize().height) * Game.getScreen().getSize().height)) ;
		
		collectibles.add(new Collectible(220, level, UtilG.RandomPos(minCoord, range, new Dimension(1, 1)), collectibleDelay[0])) ;
		collectibles.add(new Collectible(60 + 3 * level, level, UtilG.RandomPos(minCoord, range, new Dimension(1, 1)), collectibleDelay[1])) ;
		collectibles.add(new Collectible(61 + 3 * level, level, UtilG.RandomPos(minCoord, range, new Dimension(1, 1)), collectibleDelay[2])) ;
		collectibles.add(new Collectible(62 + 3 * level, level, UtilG.RandomPos(minCoord, range, new Dimension(1, 1)), collectibleDelay[3])) ;
	} 	
	
 	public void displayCollectibles(DrawingOnPanel DP)
 	{
 		for (Collectible collectible : collectibles)
		{
 			collectible.display(DP) ;
		}
 	}

 	public boolean containsItem(Item item)
 	{
 		for (Creature creature : creatures)
 		{
 			if (creature.getBag().contains(item))
 			{
 				return true ;
 			}
 		}
 		
 		return false ;
 	} 	

 	@Override
	public void displayItems(DrawingOnPanel DP)
	{

		Point pos = new Point(500, 190) ;
		Dimension size = new Dimension(140, 200) ;
		Point textPos = UtilG.Translate(pos, -size.width / 2 + 5, -size.height / 2) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Set<Item> itemsAlreadyDisplayed = new HashSet<>() ;
		
		DP.DrawRoundRect(pos, Align.center, size, 1, Game.colorPalette[8], Game.colorPalette[8], true);
		
		creatures.forEach(creature -> {
			for (Item item : creature.getBag())
			{
				if (itemsAlreadyDisplayed.contains(item)) { continue ;}
				
				itemsAlreadyDisplayed.add(item) ;
				DP.DrawText(textPos, Align.centerLeft, 0, item.getName(), font, Game.colorPalette[9]) ;
				textPos.y += 10 ;
			}
		}) ;

	}
 	
 	public void printItems()
 	{
 		System.out.println(name);
 		creatures.forEach(creature -> creature.getBag().forEach(System.out::println));
 		System.out.println();
 	}
 	
//	public void displayGroundType()
//	{
//		if (groundType != null)
//		{
//			for (int gt = 0 ; gt <= groundType.length - 1 ; gt += 1)
//			{
//				Object[] o = (Object[]) groundType[gt] ;
//				String type = (String) o[0] ;
//				Point p = (Point) o[1] ;
//				if (type.equals("water"))
//				{
//					DP.DrawRect(new Point(p.x, p.y), "center", new Size(10, 10), 0, Color.blue, null, false);
//				}
//			}
//		}
//	}
	 	
}