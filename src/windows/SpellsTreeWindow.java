package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.GameIcon;
import graphics.DrawingOnPanel;
import liveBeings.Player;
import liveBeings.Spell;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class SpellsTreeWindow extends GameWindow
{
	private List<Spell> spells ;
//	private int spellPoints ;
	private Point windowTopLeft ;
//	private Color textColor ;
//	public List<GameIcon> spellBoxes ;
//	private Dimension windowSize ;
	
	private static final Font regularFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	private static final Font largeFont = new Font(Game.MainFontName, Font.BOLD, 12) ;
	private static final int[][] numberSpellsPerRow = new int[][] {{2, 3, 3, 3, 3}} ;
	private static final Image spellSlot = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellSlot.png") ;
	private static final Image spellInfo = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellInfo.png") ;
	private static final Image spellPoints = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellPoints.png") ;
	
	public SpellsTreeWindow()
	{
		super("Árvore de magias", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellsTree.png"), 0, 0, 0, 1);
//		this.spells = spells ;
//		this.spellPoints = spellPoints ;
		windowTopLeft = new Point((int)(0.1 * Game.getScreen().getSize().width), (int)(0.2 * Game.getScreen().getSize().height)) ;
//		windowSize = new Dimension((int)(0.7 * Game.getScreen().getSize().width), (int)(0.66 * Game.getScreen().getSize().height)) ;
//		spellBoxes = new ArrayList<>() ;
	}
		
	public void setSpells(List<Spell> spells) { this.spells = spells ; numberItems = spells.size() ;}	
//	public void setSpellPoints(int spellPoints) { this.spellPoints = spellPoints;}
	public boolean canAcquireSpell(int spellPoints) { return 0 < spellPoints & spells.get(item).getLevel() < spells.get(item).getMaxLevel() & spells.get(item).hasPreRequisitesMet() ;}
	public void acquireSpell(List<Spell> spells)
	{

		if (!spells.contains(spells.get(item)))
		{
			spells.add(spells.get(item)) ;
			
			return ;
		}
		
		spells.get(item).incLevel(1) ;
		
	}
	
	public void navigate(String action)
	{
		if (action == null) { return ;}
		
		if (action.equals(Player.ActionKeys[0]))
		{
			itemUp() ;
		}
		if (action.equals(Player.ActionKeys[2]))
		{
			itemDown() ;
		}
	}
	
	private Point calcSlotPos(int row, int col, Dimension slotSize)
	{
		Point offset = new Point(windowTopLeft.x + border + padding, windowTopLeft.y + border + padding + 20) ;
		
		Point spacing = new Point((int)UtilG.spacing(288, 3, slotSize.width, padding), (int)UtilG.spacing(320, 5, slotSize.height, padding)) ;

		Point slotPos = new Point(offset.x + col * spacing.x, offset.y + row * spacing.y) ;
		
		return slotPos ;
	}
	
	public void displaySpellsInfo(DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		Point pos = UtilG.Translate(windowTopLeft, 0, -40) ;
		DP.DrawImage(spellInfo, pos, Align.topLeft) ;
		pos.x += 5 ;
		for (String info : spells.get(item).getInfo())
		{
			pos.y += 8 ;
			DP.DrawText(pos, Align.centerLeft, angle, info, regularFont, Game.ColorPalette[9]) ;
			pos.y += 8 ;
		}
	}
	
	public void displaySpellPoints(int points, DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		Point pointsPos = UtilG.Translate(windowTopLeft, size.width + 10 + 28, size.height - 6 - 40) ;
		Color color = Game.ColorPalette[0] ;
		
		DP.DrawImage(spellPoints, pointsPos, Align.topCenter) ;
		DP.DrawText(UtilG.Translate(pointsPos, 0, 6), Align.topCenter, angle, "Pontos", regularFont, color) ;
//		DP.DrawText(UtilG.Translate(pointsPos, 0, 13), Align.topCenter, angle, "de magia", regularFont, color) ;
		DP.DrawText(UtilG.Translate(pointsPos, 0, 24), Align.topCenter, angle, String.valueOf(points), regularFont, color) ;
		
	}
	
	public void display(Point mousePos, int points, DrawingOnPanel DP)
	{

		int TabL = size.width / 20 ;
		int TabH = size.height / 3 ;
		double angle = DrawingOnPanel.stdAngle ;
		Color[] TabColor = new Color[] {Game.ColorPalette[7], Game.ColorPalette[7]} ;
		Color[] TabTextColor = new Color[] {Game.ColorPalette[5], Game.ColorPalette[5]} ;
		Color selectedColor = Game.ColorPalette[3] ;
		Color hasPreReqColor = Game.ColorPalette[0] ;
		Color hasNotPreReqColor = Game.ColorPalette[9] ;
		
		displaySpellsInfo(DP) ;

		DP.DrawImage(image, windowTopLeft, angle, new Scale(1, 1), Align.topLeft) ;
		
		Point titlePos = UtilG.Translate(windowTopLeft, size.width / 2, 6 + 9) ;
		DP.DrawText(titlePos, Align.center, angle, name, largeFont, Game.ColorPalette[9]);
		
		
		// display spells
		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			int row = i / 3 ;
			int col = i % 3 ;
			Dimension slotSize = new Dimension(spellSlot.getWidth(null), spellSlot.getHeight(null)) ;
			Color textColor = this.item == i ? selectedColor : spells.get(i).hasPreRequisitesMet() ? hasPreReqColor : hasNotPreReqColor ;
			Point slotPos = calcSlotPos(row, col, slotSize) ;
			Point spellNamePos = UtilG.Translate(slotPos, slotSize.width / 2, 10) ;
			Point spellLevelPos = UtilG.Translate(slotPos, slotSize.width / 2, slotSize.height / 2 + 18) ;
					
			DP.DrawImage(spellSlot, slotPos, Align.topLeft) ;
			DP.DrawTextUntil(spellNamePos, Align.center, angle, spells.get(i).getName(), regularFont, textColor, 8, mousePos) ;
			DP.DrawText(spellLevelPos, Align.center, angle, String.valueOf(spells.get(i).getLevel()), regularFont, textColor) ;
		}

		displaySpellPoints(points, DP) ;
	}
	
}
