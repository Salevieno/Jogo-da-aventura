package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import components.SpellTypes;
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
	private Point windowTopLeft ;
	private List<Spell> spellsOnWindow ;
	private int[] spellsDistribution ;
	private int playerJob ;
	private int points ;

	private static final Font regularFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	private static final Font largeFont = new Font(Game.MainFontName, Font.BOLD, 12) ;
	private static final Image noTabsImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellsTree.png") ;
	private static final Image tab0Image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellsTreeTab0.png") ;
	private static final Image tab1Image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellsTreeTab1.png") ;
	private static final Image spellSlot = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellSlot.png") ;
	private static final Image spellSlotSelected = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellSlotSelected.png") ;
	private static final Image spellInfo = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellInfo.png") ;
	private static final Image spellPoints = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "SpellPoints.png") ;
	
	public SpellsTreeWindow(int playerJob)
	{// TODO arrumar a árvore de magias. Spells tá se confundindo com spellsOnWindow
		super("Árvore de magias", noTabsImage, 0, 1, 0, 1) ;
		
		windowTopLeft = Game.getScreen().getPoint(0.4, 0.2) ;
		this.playerJob = playerJob ;
	}
		
	public void switchTo2Tabs() { numberTabs = 2 ;}
	public void setSpells(List<Spell> spells) { this.spells = spells ; updateSpellsOnWindow() ; numberItems = spellsOnWindow.size() ;}
	public void setPoints (int points) { this.points = points ;}
	
	public boolean canAcquireSpell(int spellPoints) { return 0 < spellPoints & spells.get(item).getLevel() < spells.get(item).getMaxLevel() & spells.get(item).hasPreRequisitesMet() ;}
	
	public void acquireSpell(Player player)
	{
		Spell spell = spellsOnWindow.get(item) ;
		if (!spells.contains(spell))
		{
			spells.add(spell) ;
			
			return ;
		}
		
		spell.incLevel(1) ;
		if (spell.getType().equals(SpellTypes.passive))
		{
			player.applyPassiveSpell(spell) ;
		}
		
	}
	
	public void updateSpellsDistribution()
	{
		if (0 < tab) { spellsDistribution = new int[] {1, 2, 2, 2, 2, 1} ; return ;}
		
		switch (playerJob)
		{
			case 0: spellsDistribution = new int[] {1, 3, 3, 3, 3, 1} ; return ;
			case 1: spellsDistribution = new int[] {3, 3, 3, 3, 3} ; return ;
			case 2: spellsDistribution = new int[] {3, 3, 3, 3, 3} ; return ;
			case 3: spellsDistribution = new int[] {2, 3, 3, 3, 3} ; return ;
			case 4: spellsDistribution = new int[] {2, 3, 3, 3, 3} ; return ;
			default: spellsDistribution = null ;
		}
	}
	
	public void navigate(String action)
	{
		if (action == null) { return ;}

		if (action.equals(Player.ActionKeys[1]))
		{
			itemDown() ;
		}
		if (action.equals(Player.ActionKeys[3]))
		{
			itemUp() ;
		}
		if (1 <= numberTabs)
		{
			if (action.equals(Player.ActionKeys[0]))
			{
				item = 0 ;
				tabDown() ;
				updateSpellsOnWindow() ;
				updateSpellsDistribution() ;
				numberItems = spellsOnWindow.size() ;
			}
			if (action.equals(Player.ActionKeys[2]))
			{
				item = 0 ;
				tabUp() ;
				updateSpellsOnWindow() ;
				updateSpellsDistribution() ;
				numberItems = spellsOnWindow.size() ;
			}
		}
		if (action.equals("Escape"))
		{
			close() ;
		}
	}
	
	public void act(Player player)
	{

		if (canAcquireSpell(points) & Player.actionIsForward(player.getCurrentAction()))
		{
			acquireSpell(player) ;
			points += -1 ;
			player.decSpellPoints() ;
		}		
		
	}
	
	private Point calcSlotPos(int row, int col, int numberRows, int numberCols, Dimension slotSize)
	{
		Point offset = new Point(windowTopLeft.x + border + padding, windowTopLeft.y + border + padding + 20) ;
		
		double spacingX = UtilG.spacing(288, numberCols, slotSize.width, padding) ;
		double spacingY = UtilG.spacing(320, numberRows, slotSize.height, padding) ;

		Point slotPos = new Point((int) (offset.x + col * spacingX), (int) (offset.y + row * spacingY)) ;
		
		return slotPos ;
	}
	
	public void displaySpellsInfo(DrawingOnPanel DP)
	{
		if (spellsOnWindow == null) { return ;}
		if (spellsOnWindow.get(item) == null) { return ;}
		
		double angle = DrawingOnPanel.stdAngle ;
		Point pos = UtilG.Translate(windowTopLeft, 0, -40) ;
		Point spellNamePos = UtilG.Translate(windowTopLeft, spellInfo.getWidth(null) / 2, - 40 - 10) ;
		DP.DrawImage(spellInfo, pos, Align.topLeft) ;
		DP.DrawText(spellNamePos, Align.center, angle, spellsOnWindow.get(item).getName(), regularFont, Game.colorPalette[9]) ;
		pos.x += 5 ;
		pos.y += 8 ;
		for (String info : spellsOnWindow.get(item).getInfo())
		{
			DP.DrawText(pos, Align.centerLeft, angle, info, regularFont, Game.colorPalette[9]) ;
			pos.y += 16 ;
		}
	}
	
	public void displaySpellPoints(int points, DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		Point pointsPos = UtilG.Translate(windowTopLeft, size.width + 10 + 28, size.height - 6 - 40) ;
		Color color = Game.colorPalette[0] ;
		
		DP.DrawImage(spellPoints, pointsPos, Align.topCenter) ;
		DP.DrawText(UtilG.Translate(pointsPos, 0, 6), Align.topCenter, angle, "Pontos", regularFont, color) ;
//		DP.DrawText(UtilG.Translate(pointsPos, 0, 13), Align.topCenter, angle, "de magia", regularFont, color) ;
		DP.DrawText(UtilG.Translate(pointsPos, 0, 24), Align.topCenter, angle, String.valueOf(points), regularFont, color) ;
		
	}
	
	public void displayWindow(DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		if (numberTabs <= 1)
		{
			DP.DrawImage(image, windowTopLeft, angle, new Scale(1, 1), Align.topLeft) ;
			return ;
		}
		
		Point displayPos = UtilG.Translate(windowTopLeft, -23, 0) ;
		Image displayImage = tab == 0 ? tab0Image : tab1Image ;
		Color tabTextColor = Game.colorPalette[9] ;
		DP.DrawImage(displayImage, displayPos, angle, new Scale(1, 1), Align.topLeft) ;
		DP.DrawText(UtilG.Translate(windowTopLeft, -10, 6 + 75/2), Align.center, 90, "Basic", largeFont, tab == 0 ? selColor : tabTextColor);
		DP.DrawText(UtilG.Translate(windowTopLeft, -10, 6 + 75 + 75/2), Align.center, 90, "Pro", largeFont, tab == 1 ? selColor : tabTextColor);
	}
	
	public List<Spell> basicSpells()
	{
		return spells.subList(0, Player.NumberOfSpellsPerJob[playerJob]) ;
	}
	
	public List<Spell> proSpells()
	{
		return spells.subList(spells.size() - 10, spells.size()) ;
	}

	public void updateSpellsOnWindow()
	{
		spellsOnWindow = tab == 0 ? basicSpells() : proSpells() ;
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{

		double angle = DrawingOnPanel.stdAngle ;
		Color selectedColor = Game.colorPalette[3] ;
		Color hasPreReqColor = Game.colorPalette[0] ;
		Color hasNotPreReqColor = Game.colorPalette[9] ;
		
		displaySpellsInfo(DP) ;
		displayWindow(DP) ;
		
		Point titlePos = UtilG.Translate(windowTopLeft, size.width / 2, 6 + 9) ;
		DP.DrawText(titlePos, Align.center, angle, name, largeFont, Game.colorPalette[9]);
		
		if (spells == null) { return ;}
		
		// display spells
		int initialSpell = tab == 0 ? 0 : 0 ;
		int row = 0 ;
		int col = 0 ;
		for (int i = 0 ; i <= spellsOnWindow.size() - 1 ; i += 1)
		{
			if (spellsDistribution.length <= row) { System.out.println("Tentando desenhar magias demais!") ; break ;}
			
			Spell spell = spellsOnWindow.get(i) ;
			Dimension slotSize = new Dimension(spellSlot.getWidth(null), spellSlot.getHeight(null)) ;
			Color textColor = this.item == initialSpell + i ? selectedColor : spell.hasPreRequisitesMet() ? hasPreReqColor : hasNotPreReqColor ;
			Image slotImage = this.item == initialSpell + i ? spellSlotSelected : spellSlot ;
			Point slotPos = calcSlotPos(row, col, spellsDistribution.length, spellsDistribution[row], slotSize) ;
			Point spellImagePos = UtilG.Translate(slotPos, slotSize.width / 2, 4 + 14) ;
			Point spellLevelPos = UtilG.Translate(slotPos, slotSize.width / 2, slotSize.height / 2 + 18) ;
					
			DP.DrawImage(slotImage, slotPos, Align.topLeft) ;
			DP.DrawImage(spell.getImage(), spellImagePos, Align.center) ;
//			DP.DrawTextUntil(spellNamePos, Align.center, angle, spells.get(i).getName(), regularFont, textColor, 8, mousePos) ;
			DP.DrawText(spellLevelPos, Align.center, angle, String.valueOf(spell.getLevel()), regularFont, textColor) ;
			col += 1 ;			
			
			if (spellsDistribution[row] <= col)
			{
				col = 0 ;
				row += 1 ;
			}
			
		}

		displaySpellPoints(points, DP) ;
	}
	
}
