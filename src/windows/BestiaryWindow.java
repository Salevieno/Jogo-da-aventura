package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.DrawingOnPanel;
import liveBeings.CreatureType;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class BestiaryWindow extends GameWindow
{
	private ArrayList<CreatureType> discoveredCreatures ;
	
	public BestiaryWindow()
	{
		super("Besti�rio", null, 0, 0, 0, 0) ;
		discoveredCreatures = new ArrayList<>() ;
	}
	
	public ArrayList<CreatureType> getDiscoveredCreatures() { return discoveredCreatures ; }
	public void addDiscoveredCreature(CreatureType newCreature) { discoveredCreatures.add(newCreature) ; }
	
	public void navigate(String action)
	{
		/*int windowLimit = 0 ;
		if (discoveredCreatures != null)
		{
			windowLimit = Math.max(discoveredCreatures.size() - 1, 0) / 5 ;
		}
		window = UtilS.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, windowLimit) ;*/
	}
	
	public void displayCreatureInfo(Point mainWindowPos, CreatureType creatureType, DrawingOnPanel DP)
	{
		Font namefont = new Font(Game.MainFontName, Font.BOLD, 15) ;
		Font infoFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] text = Game.allText.get(TextCategories.bestiary) ;
		Color textColor = Game.colorPalette[0] ;
		double angle = DrawingOnPanel.stdAngle ;
		
		int offset = 5 ;
		int sy = infoFont.getSize() ;
		
		mainWindowPos.y += - (int) (0.5 * Game.getScreen().getSize().height) ;

		Dimension windowSize = new Dimension((int)(0.2 * Game.getScreen().getSize().width), (int)(0.5 * Game.getScreen().getSize().height)) ;
		Point windowPos = UtilG.Translate(mainWindowPos, 0, -30) ;
		DP.DrawRoundRect(windowPos, Align.topLeft, windowSize, 3, Game.colorPalette[14], Game.colorPalette[5], true) ;
		
		Point creaturePos = UtilG.Translate(mainWindowPos, 40, offset) ;
		creatureType.display(creaturePos, Scale.unit, DP) ;
		
		List<String> textInfo = new ArrayList<>() ;
		textInfo.add(text[1] + ": " + creatureType.getLevel()) ;
		textInfo.add(text[2] + ": " + (int)creatureType.getPA().getLife().getCurrentValue()) ;
		textInfo.add(text[3] + ": " + creatureType.getPA().getExp().getCurrentValue()) ;
		textInfo.add(text[4] + ": " + creatureType.getGold()) ;
		textInfo.add(text[5] + ": ") ;
		creatureType.getItems().forEach(item -> textInfo.add(item.getName())) ;

		// draw text
		Point textPos = new Point(mainWindowPos.x + offset, (int) (mainWindowPos.y + creatureType.getSize().height + offset)) ;
		DP.DrawText(textPos, Align.topLeft, angle, creatureType.getName(), namefont, textColor) ;
		textPos = UtilG.Translate(textPos, 0, sy) ;
		for (int i = 0 ; i <= text.length - 1 ; i += 1)
		{
			textPos = UtilG.Translate(textPos, 0, sy) ;
			DP.DrawText(textPos, Align.topLeft, angle, textInfo.get(i), infoFont, textColor) ;
		}
	}
	
	public void display(Point MousePos, DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.1 * Game.getScreen().getSize().width), (int)(0.3 * Game.getScreen().getSize().height)) ;
		Dimension windowSize = new Dimension((int)(0.6 * Game.getScreen().getSize().width), (int)(0.6 * Game.getScreen().getSize().height)) ;
		int numRows = 6, numCols = 6 ;	
		
		int offset = 12 ;
		Dimension slotSize = new Dimension(windowSize.width / (numCols + 1) - 2 * offset / numCols, windowSize.height / (numRows + 1) - 2 * offset / numRows) ;
		int sx = (int) UtilG.spacing(windowSize.width, numCols, slotSize.width, offset) ;
		int sy = (int) UtilG.spacing(windowSize.height, numRows, slotSize.height, offset) ;

		
		// draw window
		DP.DrawRoundRect(windowPos, Align.topLeft, windowSize, 3, Game.colorPalette[14], Game.colorPalette[5], true) ;
		
		if (discoveredCreatures != null)
		{
			int numSlotsInWindow = Math.min(discoveredCreatures.size(), numRows * numCols) ;
			CreatureType selectedCreature = null ;
			for (int slot = 0 ; slot <= numSlotsInWindow - 1 ; slot += 1)
			{
				// draw slots
				Point slotTopLeft = new Point((int) (windowPos.x + (slot / numCols) * sx + offset), (int) (windowPos.y + (slot % numRows) * sy + offset)) ;
				Point slotCenter = UtilG.Translate(slotTopLeft, slotSize.width / 2, slotSize.height / 2) ;
				DP.DrawRoundRect(slotCenter, Align.center, slotSize, 2, Game.colorPalette[20], Game.colorPalette[3], true) ;

				// draw creatures
				CreatureType creatureType = discoveredCreatures.get(slot) ;
				double scaleFactor = Math.min((double) (slotSize.width - 10) / creatureType.getSize().width, (double) (slotSize.height - 10) / creatureType.getSize().height) ;
				creatureType.display(slotCenter, new Scale(scaleFactor, scaleFactor), DP) ;
				
				// determine if a creature is selected
				if (UtilG.isInside(MousePos, slotTopLeft, slotSize))
				{
					selectedCreature = creatureType ;
				}
			}
			if (selectedCreature != null)
			{
				displayCreatureInfo(new Point(windowPos.x + windowSize.width, windowPos.y + windowSize.height), selectedCreature, DP) ;
			}
		}
	}
}
