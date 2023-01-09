package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import components.Icon;
import graphics.DrawingOnPanel;
import liveBeings.Spell;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class SpellsTreeWindow extends GameWindow
{
	private Spell[] spells ;
	private int spellPoints ;
	private Color textColor ;
	public List<Icon> spellBoxes ;
	private Point windowBotLeft ;
	private Dimension windowSize ;
	
	private static final Font regularFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	private static final Font largeFont = new Font(Game.MainFontName, Font.BOLD, 12) ;
	private static final int[][] numberSpellsPerRow = new int[][] {{2, 3, 3, 3, 3}} ;
	
	public SpellsTreeWindow(Spell[] spells, int spellPoints, Color textColor)
	{
		super(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellsTreeIcon.png"), 0, 0, 0, 1);
		this.spells = spells ;
		this.spellPoints = spellPoints ;
		this.textColor = textColor ;
		windowBotLeft = new Point((int)(0.1 * Game.getScreen().getSize().width), (int)(0.9 * Game.getScreen().getSize().height)) ;
		windowSize = new Dimension((int)(0.7 * Game.getScreen().getSize().width), (int)(0.66 * Game.getScreen().getSize().height)) ;
		spellBoxes = new ArrayList<>();
	}
		
	public void setSpells(Spell[] spells) {this.spells = spells ; setSpellBoxes() ;}
	public void setSpellBoxes()
	{
		// creating of spellboxes as icons at pos (0, 0)
		for (int spell = 0 ; spell <= spells.length - 1 ; spell += 1)
		{
			Icon newIcon = new Icon(spell, spells[spell].getName(), new Point(0, 0), spells[spell].getInfo()[0], image, image) ;
			newIcon.activate();
			spellBoxes.add(newIcon) ;
		}
		
		// arranging the boxes in rows
		int cont = 0 ;
		int numberRows = numberSpellsPerRow[0].length ;
		int sy = (int) UtilG.spacing(windowSize.height, numberRows, spellBoxes.get(cont).getImage().getHeight(null), 0) ;
		for (int row = 0 ; row <= numberRows - 1 ; row += 1)
		{
			int numberCol = numberSpellsPerRow[0][row] ;
			int sx = (int) UtilG.spacing(windowSize.width, numberCol, spellBoxes.get(cont).getImage().getWidth(null), 0) ;
			for (int col = 0 ; col <= numberCol - 1 ; col += 1)
			{
				Point pos = new Point(windowBotLeft.x + col * sx, windowBotLeft.y - windowSize.height + row * sy) ;
				spellBoxes.get(cont).setPos(pos) ;
				cont += 1;
			}
		}
	}
		
	public void display(Point MousePos, int SelectedSpell, DrawingOnPanel DP)
	{
		//System.out.println(Game.getAllSpellTypes().length);
		//System.out.println(Arrays.toString(knightSpells));
		//int[] Sequence = GetSpellSequence() ;
		//int[] ProSequence = GetProSpellSequence() ;
		//int NumberOfSpells = GetNumberOfSpells() ;
		//int NumberOfProSpells = 0 ;
		int TabL = windowSize.width / 20 ;
		int TabH = windowSize.height / 3 ;
		//Dimension size = new Dimension((int)(0.2*screen.getSize().width), (int)(0.1*screen.getSize().height)) ;
		//int Sx = size.width / 10, Sy = size.height / 10 ;
		//Color[] SpellsColors = new Color[spells.size() + NumberOfProSpells] ;
		Color[] TabColor = new Color[] {Game.ColorPalette[7], Game.ColorPalette[7]} ;
		Color[] TabTextColor = new Color[] {Game.ColorPalette[5], Game.ColorPalette[5]} ;
		/*int tab = 0 ;
		if (NumberOfSpells - 1 < SelectedSpell)
		{
			tab = 1 ;
			NumberOfProSpells = GetNumberOfProSpells() ;
			Sequence = ProSequence ;
		}*/

		Color[] spellColors = new Color[spells.length] ;
		for (int i = 0 ; i <= spells.length - 1 ; i += 1)
		{
			spellColors[i] = Game.ColorPalette[4] ;
			if (spells[i].hasPreRequisitesMet())
			{
				spellColors[i] = Game.ColorPalette[5] ;
			}
		}
		
		
		// spell tree window
		DP.DrawRoundRect(windowBotLeft, Align.bottomLeft, windowSize, 1, Game.ColorPalette[20], Game.ColorPalette[20], true) ;
		TabColor[tab] = Game.ColorPalette[20] ;
		TabTextColor[tab] = Game.ColorPalette[3] ;
		DP.DrawRoundRect(new Point(windowBotLeft.x, windowBotLeft.y - 2*TabH), Align.bottomRight, new Dimension(TabL, TabH), 1, TabColor[0], Game.ColorPalette[8], true) ;
		//DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 2*TabH - TabH/2), AlignmentPoints.center, 90, allText.get("* Classes *")[getJob() + 1], Largefont, TabTextColor[0]) ;
		//if (0 < getProJob())
		//{
		//	DP.DrawRoundRect(new Point(Pos.x, Pos.y - TabH), AlignmentPoints.bottomRight, new Dimension(TabL, TabH), 1, TabColor[1], Game.ColorPalette[8], true) ;	
		//	DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 3*TabH/2), AlignmentPoints.center, 90, allText.get("* ProClasses *")[getProJob() + 2*getJob()], Largefont, TabTextColor[1]) ;
		//}
		
		
		// spell description and effect displayed on top of the window
		int textMaxWidth = windowSize.width / 5 ;
		int sx = 10 ;
		int sy = regularFont.getSize() + 2 ;
		String description = spells[SelectedSpell].getInfo()[0] ;
		String effect = spells[SelectedSpell].getInfo()[1] ;
		DP.DrawRoundRect(new Point(windowBotLeft.x, windowBotLeft.y - windowSize.height), Align.bottomLeft, new Dimension(windowSize.width, windowSize.height / 4), 1, Game.ColorPalette[7], Game.ColorPalette[7], true) ;
		DP.DrawFitText(new Point(windowBotLeft.x + sx, windowBotLeft.y - windowSize.height - windowSize.height / 5), sy, Align.bottomLeft, effect, regularFont, textMaxWidth, textColor) ;
		DP.DrawFitText(new Point(windowBotLeft.x + sx, windowBotLeft.y - windowSize.height - windowSize.height / 10), sy, Align.bottomLeft, description, regularFont, textMaxWidth - 6, textColor) ;		
		
		
		// spell points
		DP.DrawText(new Point(windowBotLeft.x + windowSize.width - 5, windowBotLeft.y - 5), Align.bottomRight, DrawingOnPanel.stdAngle, "Pontos: " +  spellPoints, largeFont, textColor) ;
	
		
		// spell boxes
		for (int spell = 0 ; spell <= spells.length - 1 ; spell += 1)
		{
			//SpellNames[0] = UtilG.AddElem(SpellNames[0], spells[spell].getName()) ;
			//SpellNames[1] = UtilG.AddElem(SpellNames[1], spells[spell].getType()) ;
			//SpellLevels = UtilG.AddElem(SpellLevels, String.valueOf(player.getSpell()[spell])) ;
			//SpellsColors[spell] = color[spell] ;
			spellBoxes.get(spell).display(DrawingOnPanel.stdAngle, Align.topLeft, MousePos, DP) ;
		}
		
		//DrawOrganogram(Sequence, new Point(Pos.x, Pos.y - Size.y), Sx, Sy, size, SpellNames, SpellLevels, SpellsTreeIcon, font, SpellsColors, MousePos) ;
	}
	
}