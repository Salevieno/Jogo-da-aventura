package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import Graphics.DrawPrimitives;
import LiveBeings.Creature;
import LiveBeings.Player;
import Main.Game;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class BestiaryWindow extends Window
{
	private ArrayList<Creature> discoveredCreatures ;
	
	public BestiaryWindow()
	{
		super(null, 0, 0, 0, 0) ;
		discoveredCreatures = new ArrayList<>() ;
	}
	
	public ArrayList<Creature> getDiscoveredCreatures() { return discoveredCreatures ; }
	public void addDiscoveredCreature(Creature newCreature) { discoveredCreatures.add(newCreature) ; }
	
	public void navigate(String action)
	{
		int windowLimit = 0 ;
		if (discoveredCreatures != null)
		{
			windowLimit = Math.max(discoveredCreatures.size() - 1, 0)/5 ;
		}
		window = UtilS.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, window, windowLimit) ;
	}
	
	public void displayCreatureInfo(Point windowPos, Player player, Creature creature, DrawPrimitives DP)
	{
		Font namefont = new Font(Game.MainFontName, Font.BOLD, 15) ;
		Font infoFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] text = player.allText.get("* Bestiário *") ;
		Color textColor = Game.ColorPalette[9] ;
		float angle = DrawPrimitives.OverallAngle ;
		
		int H = (int) (0.5*Game.getScreen().getSize().y) ;
		int offset = 5 ;
		int sy = infoFont.getSize() ;
		
		windowPos.y += - H ;
		creature.display(new Point(windowPos.x + 40, windowPos.y + offset), new Scale(1, 1), DP) ;
		
		// draw text
		ArrayList<String> textInfo = new ArrayList<>() ;
		textInfo.add(text[2] + ": " + creature.getLevel()) ;
		textInfo.add(text[3] + ": " + (int)creature.getLife()[0]) ;
		textInfo.add(text[4] + ": " + creature.getExp()[0]) ;
		textInfo.add(text[5] + ": " + creature.getGold()) ;
		textInfo.add(text[6] + ": ") ;
		for (int i = 0 ; i <= creature.getBag().length - 1 ; i += 1)
		{
			textInfo.add(String.valueOf(creature.getBag()[i])) ;
		}
		
		DP.DrawText(new Point(windowPos.x + offset, (int) (windowPos.y + creature.getSize().y + offset)), "TopLeft", angle, creature.getName(), namefont, textColor) ;		// Name
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			DP.DrawText(new Point(windowPos.x + offset, (int) (windowPos.y + creature.getSize().y + (i + 2) * sy + offset)), "TopLeft", angle, textInfo.get(i), infoFont, textColor) ;
		}
	}
	
	public void display(Player player, Point MousePos, DrawPrimitives DP)
	{
		Point windowPos = new Point((int)(0.1 * Game.getScreen().getSize().x), (int)(0.3 * Game.getScreen().getSize().y)) ;
		Size windowSize = new Size((int)(0.6 * Game.getScreen().getSize().x), (int)(0.6 * Game.getScreen().getSize().y)) ;
		int numRows = 6, numCols = 6 ;	
		
		int offset = 12 ;
		Size slotSize = new Size(windowSize.x / (numCols + 1) - 2 * offset / numCols, windowSize.y / (numRows + 1) - 2 * offset / numRows) ;
		int sx = (int) UtilG.spacing(windowSize.x, numCols, slotSize.x, offset) ;
		int sy = (int) UtilG.spacing(windowSize.y, numRows, slotSize.y, offset) ;

		
		DP.DrawRoundRect(windowPos, "TopLeft", windowSize, 3, Game.ColorPalette[14], Game.ColorPalette[5], true) ;
		
		if (discoveredCreatures != null)
		{
			int numSlotsInWindow = Math.min(discoveredCreatures.size(), numRows * numCols) ;
			Creature selectedCreature = null ;
			for (int slot = 0 ; slot <= numSlotsInWindow - 1 ; slot += 1)
			{
				// draw slots
				Point slotPos = new Point((int) (windowPos.x + slotSize.x / 2 + (slot / numCols) * sx + offset), (int) (windowPos.y + slotSize.y / 2 + (slot % numRows) * sy + offset)) ;
				DP.DrawRoundRect(slotPos, "Center", slotSize, 2, Game.ColorPalette[20], Game.ColorPalette[7], true) ;

				// draw creatures
				Creature creature = discoveredCreatures.get(slot) ;
				double scaleFactor = Math.min((float) (slotSize.x) / creature.getSize().x, (float) (slotSize.y) / creature.getSize().y) ;
				creature.display(slotPos, new Scale(scaleFactor, scaleFactor), DP) ;
				
				// determine if a creature is selected
				if (UtilG.isInside(MousePos, slotPos, slotSize))
				{
					selectedCreature = creature ;
				}
			}
			if (selectedCreature != null)
			{
				displayCreatureInfo(new Point(windowPos.x + windowSize.x, windowPos.y + windowSize.y), player, selectedCreature, DP) ;
			}
		}
	}
}
