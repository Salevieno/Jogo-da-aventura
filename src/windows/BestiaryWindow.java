package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Draw;
import graphics.DrawPrimitives;
import liveBeings.CreatureType;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class BestiaryWindow extends GameWindow
{
	private List<CreatureType> discoveredCreatures ;

	private static final Point windowPos = Game.getScreen().pos(0.1, 0.3) ;
	private Dimension windowSize = new Dimension(384, 288) ;
	
	public BestiaryWindow()
	{
		super("Bestiário", windowPos, null, 0, 0, 0, 0) ;
		discoveredCreatures = new ArrayList<>() ;
	}
	
	public List<CreatureType> getDiscoveredCreatures() { return discoveredCreatures ; }
	public void addDiscoveredCreature(CreatureType newCreature) { discoveredCreatures.add(newCreature) ; }
	
	public void navigate(String action)
	{
	}
	
	public void displayCreatureInfo(Point mainWindowPos, CreatureType creatureType, DrawPrimitives DP)
	{
		Font namefont = new Font(Game.MainFontName, Font.BOLD, 15) ;
		Font infoFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] text = Game.allText.get(TextCategories.bestiary) ;
		Color textColor = Game.colorPalette[0] ;
		double angle = Draw.stdAngle ;
		
		int offset = 5 ;
		int sy = infoFont.getSize() ;
		
		mainWindowPos.y += - (int) (0.5 * Game.getScreen().getSize().height) ;

		Dimension windowSize = new Dimension(128, 240) ;
		Point windowPos = UtilG.Translate(mainWindowPos, 0, -30) ;
		DP.drawGradRoundRect(windowPos, Align.topLeft, windowSize, 3, Game.colorPalette[14], Game.colorPalette[5], true) ;
		
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
		Point textPos = UtilG.Translate(mainWindowPos, offset, creatureType.getSize().height + offset) ;
		DP.drawText(textPos, Align.topLeft, angle, creatureType.getName(), namefont, textColor) ;
		textPos = UtilG.Translate(textPos, 0, sy) ;
		for (int i = 0 ; i <= text.length - 1 ; i += 1)
		{
			textPos = UtilG.Translate(textPos, 0, sy) ;
			DP.drawText(textPos, Align.topLeft, angle, textInfo.get(i), infoFont, textColor) ;
		}
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		int numRows = 6 ;
		int numCols = 6 ;
		
		int offset = 12 ;
		Dimension slotSize = new Dimension(windowSize.width / (numCols + 1) - 2 * offset / numCols, windowSize.height / (numRows + 1) - 2 * offset / numRows) ;
		int sx = (int) UtilG.spacing(windowSize.width, numCols, slotSize.width, offset) ;
		int sy = (int) UtilG.spacing(windowSize.height, numRows, slotSize.height, offset) ;

		
		// draw window
		DP.drawGradRoundRect(windowPos, Align.topLeft, windowSize, 3, Game.colorPalette[14], Game.colorPalette[5], true) ;
		
		if (discoveredCreatures == null) { return ;}
		
		int numSlotsInWindow = Math.min(discoveredCreatures.size(), numRows * numCols) ;
		item = -1 ;
		for (int slot = 0 ; slot <= numSlotsInWindow - 1 ; slot += 1)
		{
			// draw slots
			Point slotTopLeft = UtilG.Translate(windowPos, (slot / numCols) * sx + offset, (slot % numRows) * sy + offset) ;
			Point slotCenter = UtilG.Translate(slotTopLeft, slotSize.width / 2, slotSize.height / 2) ;
			DP.drawGradRoundRect(slotCenter, Align.center, slotSize, 2, Game.colorPalette[20], Game.colorPalette[3], true) ;

			// draw creatures
			CreatureType creatureType = discoveredCreatures.get(slot) ;
			double scaleFactor = Math.min((double) (slotSize.width - 10) / creatureType.getSize().width,
					(double) (slotSize.height - 10) / creatureType.getSize().height) ;
			checkMouseSelection(mousePos, slotTopLeft, Align.topLeft, slotSize, slot) ;
			creatureType.display(slotCenter, new Scale(scaleFactor, scaleFactor), DP) ;
		}

		if (discoveredCreatures.isEmpty()) { return ;}
		if (item < 0) { return ;}
		
		CreatureType selectedCreature = discoveredCreatures.get(item) ;
		Point creatureInfoPos = UtilG.Translate(windowPos, windowSize.width, windowSize.height) ;
		displayCreatureInfo(creatureInfoPos, selectedCreature, DP) ;
	}
}
