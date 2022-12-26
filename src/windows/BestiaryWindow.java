package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.CreatureTypes;
import liveBeings.Player;
import main.Game;
import utilities.AlignmentPoints;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class BestiaryWindow extends Window
{
	private ArrayList<CreatureTypes> discoveredCreatures ;
	
	public BestiaryWindow()
	{
		super(null, 0, 0, 0, 0) ;
		discoveredCreatures = new ArrayList<>() ;
	}
	
	public ArrayList<CreatureTypes> getDiscoveredCreatures() { return discoveredCreatures ; }
	public void addDiscoveredCreature(CreatureTypes newCreature) { discoveredCreatures.add(newCreature) ; }
	
	public void navigate(String action)
	{
		/*int windowLimit = 0 ;
		if (discoveredCreatures != null)
		{
			windowLimit = Math.max(discoveredCreatures.size() - 1, 0) / 5 ;
		}
		window = UtilS.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, windowLimit) ;*/
	}
	
	public void displayCreatureInfo(Point windowPos, Player player, CreatureTypes creature, DrawingOnPanel DP)
	{
		Font namefont = new Font(Game.MainFontName, Font.BOLD, 15) ;
		Font infoFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] text = player.allText.get("* Bestiário *") ;
		Color textColor = Game.ColorPalette[9] ;
		double angle = DrawingOnPanel.stdAngle ;
		
		int H = (int) (0.5*Game.getScreen().getSize().height) ;
		int offset = 5 ;
		int sy = infoFont.getSize() ;
		
		windowPos.y += - H ;
		creature.display(new Point(windowPos.x + 40, windowPos.y + offset), new Scale(1, 1), DP) ;
		
		// draw text
		ArrayList<String> textInfo = new ArrayList<>() ;
		textInfo.add(text[2] + ": " + creature.getPA().getLevel()) ;
		textInfo.add(text[3] + ": " + (int)creature.getPA().getLife()[0]) ;
		textInfo.add(text[4] + ": " + creature.getPA().getExp()[0]) ;
		textInfo.add(text[5] + ": " + creature.getGold()) ;
		textInfo.add(text[6] + ": ") ;
		for (int i = 0 ; i <= creature.getBag().length - 1 ; i += 1)
		{
			textInfo.add(String.valueOf(creature.getBag()[i])) ;
		}
		
		DP.DrawText(new Point(windowPos.x + offset, (int) (windowPos.y + creature.getPA().getSize().height + offset)), AlignmentPoints.topLeft, angle, creature.getPA().getName(), namefont, textColor) ;		// Name
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			DP.DrawText(new Point(windowPos.x + offset, (int) (windowPos.y + creature.getPA().getSize().height + (i + 2) * sy + offset)), AlignmentPoints.topLeft, angle, textInfo.get(i), infoFont, textColor) ;
		}
	}
	
	public void display(Player player, Point MousePos, DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.1 * Game.getScreen().getSize().width), (int)(0.3 * Game.getScreen().getSize().height)) ;
		Dimension windowSize = new Dimension((int)(0.6 * Game.getScreen().getSize().width), (int)(0.6 * Game.getScreen().getSize().height)) ;
		int numRows = 6, numCols = 6 ;	
		
		int offset = 12 ;
		Dimension slotSize = new Dimension(windowSize.width / (numCols + 1) - 2 * offset / numCols, windowSize.height / (numRows + 1) - 2 * offset / numRows) ;
		int sx = (int) UtilG.spacing(windowSize.width, numCols, slotSize.width, offset) ;
		int sy = (int) UtilG.spacing(windowSize.height, numRows, slotSize.height, offset) ;

		
		DP.DrawRoundRect(windowPos, AlignmentPoints.topLeft, windowSize, 3, Game.ColorPalette[14], Game.ColorPalette[5], true) ;
		
		if (discoveredCreatures != null)
		{
			int numSlotsInWindow = Math.min(discoveredCreatures.size(), numRows * numCols) ;
			CreatureTypes selectedCreature = null ;
			for (int slot = 0 ; slot <= numSlotsInWindow - 1 ; slot += 1)
			{
				// draw slots
				Point slotPos = new Point((int) (windowPos.x + slotSize.width / 2 + (slot / numCols) * sx + offset), (int) (windowPos.y + slotSize.height / 2 + (slot % numRows) * sy + offset)) ;
				DP.DrawRoundRect(slotPos, AlignmentPoints.center, slotSize, 2, Game.ColorPalette[20], Game.ColorPalette[7], true) ;

				// draw creatures
				CreatureTypes creatureType = discoveredCreatures.get(slot) ;
				double scaleFactor = Math.min((double) (slotSize.width) / creatureType.getPA().getSize().width, (double) (slotSize.height) / creatureType.getPA().getSize().height) ;
				creatureType.display(slotPos, new Scale(1, 1), DP) ;
				
				// determine if a creature is selected
				if (UtilG.isInside(MousePos, slotPos, slotSize))
				{
					selectedCreature = creatureType ;
				}
			}
			if (selectedCreature != null)
			{
				displayCreatureInfo(new Point(windowPos.x + windowSize.width, windowPos.y + windowSize.height), player, selectedCreature, DP) ;
			}
		}
	}
}
