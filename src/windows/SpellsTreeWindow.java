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
		Point offset = new Point(windowTopLeft.x + 6 + 14, windowTopLeft.y + 6 + 46 + 6) ;
		Point spacing = new Point(70 + slotSize.width, 20 + slotSize.height) ;

		Point slotPos = new Point(offset.x + col * spacing.x, offset.y + row * spacing.y) ;
		
		return slotPos ;
	}
	
	public void display(Point mousePos, int spellPoints, DrawingOnPanel DP)
	{

		int TabL = size.width / 20 ;
		int TabH = size.height / 3 ;
		double angle = DrawingOnPanel.stdAngle ;
		Color[] TabColor = new Color[] {Game.ColorPalette[7], Game.ColorPalette[7]} ;
		Color[] TabTextColor = new Color[] {Game.ColorPalette[5], Game.ColorPalette[5]} ;
		

		DP.DrawImage(image, windowTopLeft, angle, new Scale(1, 1), Align.topLeft) ;
		
		Point titlePos = UtilG.Translate(windowTopLeft, size.width / 2, 6 + 9) ;
		DP.DrawText(titlePos, Align.center, angle, name, largeFont, Game.ColorPalette[9]);
		
//		DP.DrawRoundRect(windowBotLeft, Align.bottomLeft, windowSize, 1, Game.ColorPalette[20], Game.ColorPalette[20], true) ;
//		TabColor[tab] = Game.ColorPalette[20] ;
//		TabTextColor[tab] = Game.ColorPalette[3] ;
//		DP.DrawRoundRect(new Point(windowBotLeft.x, windowBotLeft.y - 2*TabH), Align.bottomRight, new Dimension(TabL, TabH), 1, TabColor[0], Game.ColorPalette[8], true) ;

		
		// spell description and effect displayed on top of the window
//		int textMaxWidth = windowSize.width / 5 ;
//		int sx = 10 ;
//		int sy = regularFont.getSize() + 2 ;
//		String description = spells.get(item).getInfo()[0] ;
//		String effect = spells.get(item).getInfo()[1] ;
//		Color textColor = Game.ColorPalette[9] ; // this.item == itemsOnWindow.indexOf(item) ? Game.ColorPalette[6] : 
//		DP.DrawRoundRect(new Point(windowBotLeft.x, windowBotLeft.y - windowSize.height), Align.bottomLeft, new Dimension(windowSize.width, windowSize.height / 4), 1, Game.ColorPalette[7], Game.ColorPalette[7], true) ;
//		DP.DrawFitText(new Point(windowBotLeft.x + sx, windowBotLeft.y - windowSize.height - windowSize.height / 5), sy, Align.bottomLeft, effect, regularFont, textMaxWidth, textColor) ;
//		DP.DrawFitText(new Point(windowBotLeft.x + sx, windowBotLeft.y - windowSize.height - windowSize.height / 10), sy, Align.bottomLeft, description, regularFont, textMaxWidth - 6, textColor) ;		
		
		
		// spell points
//		DP.DrawText(new Point(windowBotLeft.x + windowSize.width - 5, windowBotLeft.y - 5), Align.bottomRight, DrawingOnPanel.stdAngle, "Pontos: " +  spellPoints, largeFont, textColor) ;
	
		
		// display spells
		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
//			spellBoxes.get(spell).display(DrawingOnPanel.stdAngle, Align.topLeft, mousePos, DP) ;
			
			int row = i / 3 ;
			int col = i % 3 ;
			Dimension slotSize = new Dimension(spellSlot.getWidth(null), spellSlot.getHeight(null)) ;
			Color textColor = this.item == i ? Game.ColorPalette[6] : spells.get(i).hasPreRequisitesMet() ? Game.ColorPalette[0] : Game.ColorPalette[9] ;			
			Point slotPos = calcSlotPos(row, col, slotSize) ;
			Point spellNamePos = UtilG.Translate(slotPos, slotSize.width / 2, 15) ;
			Point spellLevelPos = UtilG.Translate(slotPos, slotSize.width / 2, slotSize.height / 2 + 10) ;
					
			DP.DrawImage(spellSlot, slotPos, Align.topLeft) ;
			DP.DrawText(spellNamePos, Align.center, angle, spells.get(i).getName(), regularFont, textColor) ;
			DP.DrawText(spellLevelPos, Align.center, angle, String.valueOf(spells.get(i).getLevel()), regularFont, textColor) ;
		}

		Point spellPointsPos = UtilG.Translate(windowTopLeft, 0, -20) ;
		DP.DrawText(spellPointsPos, Align.centerLeft, angle, "Pontos de magia = " + spellPoints, largeFont, Game.ColorPalette[9]) ;
		
		
	}
	
}





//System.out.println(Game.getAllSpellTypes().length);
//System.out.println(Arrays.toString(knightSpells));
//int[] Sequence = GetSpellSequence() ;
//int[] ProSequence = GetProSpellSequence() ;
//int NumberOfSpells = GetNumberOfSpells() ;
//int NumberOfProSpells = 0 ;
//Dimension size = new Dimension((int)(0.2*screen.getSize().width), (int)(0.1*screen.getSize().height)) ;
//int Sx = size.width / 10, Sy = size.height / 10 ;
//Color[] SpellsColors = new Color[spells.size() + NumberOfProSpells] ;
/*int tab = 0 ;
if (NumberOfSpells - 1 < SelectedSpell)
{
	tab = 1 ;
	NumberOfProSpells = GetNumberOfProSpells() ;
	Sequence = ProSequence ;
}*/



// spell tree window
//DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 2*TabH - TabH/2), AlignmentPoints.center, 90, allText.get("* Classes *")[getJob() + 1], Largefont, TabTextColor[0]) ;
//if (0 < getProJob())
//{
//	DP.DrawRoundRect(new Point(Pos.x, Pos.y - TabH), AlignmentPoints.bottomRight, new Dimension(TabL, TabH), 1, TabColor[1], Game.ColorPalette[8], true) ;	
//	DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 3*TabH/2), AlignmentPoints.center, 90, allText.get("* ProClasses *")[getProJob() + 2*getJob()], Largefont, TabTextColor[1]) ;
//}


//DrawOrganogram(Sequence, new Point(Pos.x, Pos.y - Size.y), Sx, Sy, size, SpellNames, SpellLevels, SpellsTreeIcon, font, SpellsColors, MousePos) ;
