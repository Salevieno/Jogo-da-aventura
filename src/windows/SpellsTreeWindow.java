package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.SpellTypes;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import liveBeings.Player;
import liveBeings.Spell;
import main.Game;
import utilities.Util;
import utilities.UtilS;

public class SpellsTreeWindow extends GameWindow
{
	private List<Spell> spells ;
	private List<Spell> playerCurrentSpells ;
	private List<Spell> spellsOnWindow ;
	private int[] spellsDistribution ;
	private int playerJob ;
	private int points ;

	private static final Point windowTopLeft = Game.getScreen().pos(0.4, 0.2) ;
	private static final Font regularFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	private static final Font largeFont = new Font(Game.MainFontName, Font.BOLD, 12) ;
	private static final Image noTabsImage = UtilS.loadImage("\\Windows\\" + "SpellsTree.png") ;
	private static final Image tab0Image = UtilS.loadImage("\\Windows\\" + "SpellsTreeTab0.png") ;
	private static final Image tab1Image = UtilS.loadImage("\\Windows\\" + "SpellsTreeTab1.png") ;
	private static final Image spellSlot = UtilS.loadImage("\\Windows\\" + "SpellSlot.png") ;
	private static final Image spellSlotSelected = UtilS.loadImage("\\Windows\\" + "SpellSlotSelected.png") ;
	private static final Image spellInactiveSlot = UtilS.loadImage("\\Windows\\" + "SpellInactiveSlot.png") ;
	private static final Image spellInfo = UtilS.loadImage("\\Windows\\" + "SpellInfo.png") ;
	private static final Image spellPoints = UtilS.loadImage("\\Windows\\" + "SpellPoints.png") ;
	
	public SpellsTreeWindow(int playerJob)
	{

		super("Árvore de magias", windowTopLeft, noTabsImage, 0, 1, 0, 1) ;
		
		this.playerJob = playerJob ;
		playerCurrentSpells = new ArrayList<>() ;
	}
		
	public void switchTo2Tabs() { numberTabs = 2 ;}
	public void setSpells(List<Spell> spells) { this.spells = spells ; updateSpellsOnWindow() ; numberItems = spellsOnWindow.size() ;}
	public void setPoints (int points) { this.points = points ;}
	public void setplayerCurrentSpells (List<Spell> playerCurrentSpells) { this.playerCurrentSpells = playerCurrentSpells ;}
	
	public boolean canAcquireSpell(int spellPoints)
	{
		return 0 < spellPoints & !spells.get(item).isMaxed() & spells.get(item).hasPreRequisitesMet(playerCurrentSpells) ;
	}
	
	public void acquireSpell(Player player)
	{
		Spell spell = spellsOnWindow.get(item) ;
		if (!spells.contains(spell)) { return ;}

		if (spell.getLevel() == 0)
		{
			player.learnSpell(spell);
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

		if (action.equals(stdMenuDown))
		{
			itemDown() ;
		}
		if (action.equals(stdMenuUp))
		{
			itemUp() ;
		}
		if (1 <= numberTabs)
		{
			if (action.equals(stdWindowUp))
			{
				item = 0 ;
				tabDown() ;
				updateSpellsOnWindow() ;
				updateSpellsDistribution() ;
				numberItems = spellsOnWindow.size() ;
			}
			if (action.equals(stdWindowDown))
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
		String action = player.getCurrentAction() ;
		
		if (canAcquireSpell(points) & actionIsForward(action))
		{
			acquireSpell(player) ;
			points += -1 ;
			player.decSpellPoints() ;
		}		
		
	}
	
	private Point calcSlotPos(int row, int col, int numberRows, int numberCols, Dimension slotSize)
	{
		Point offset = new Point(windowTopLeft.x + border + padding, windowTopLeft.y + border + padding + 20) ;
		
		double spacingX = Util.spacing(288, numberCols, slotSize.width, padding) ;
		double spacingY = Util.spacing(320, numberRows, slotSize.height, padding) ;

		Point slotPos = new Point((int) (offset.x + col * spacingX), (int) (offset.y + row * spacingY)) ;
		
		return slotPos ;
	}
	
	public void displaySpellsInfo()
	{
		if (spellsOnWindow == null) { return ;}
		if (spellsOnWindow.get(item) == null) { return ;}
		
		double angle = Draw.stdAngle ;
		Point pos = Util.Translate(windowTopLeft, 0, -66) ;
		Color textColor = Game.palette[0] ;
		Point spellNamePos = Util.Translate(windowTopLeft, spellInfo.getWidth(null) / 2, - 66 - 10) ;
		Game.DP.drawImage(spellInfo, pos, Align.topLeft) ;
		Game.DP.drawText(spellNamePos, Align.center, angle, spellsOnWindow.get(item).getName(), regularFont, textColor) ;
		pos.x += 5 ;
		pos.y += 8 ;
		for (String info : spellsOnWindow.get(item).getInfo())
		{
			Draw.fitText(pos, 16, Align.centerLeft, info, regularFont, 50, textColor) ;
			pos.y += 34 ;
		}
	}
	
	public void displaySpellPoints(int points)
	{
		double angle = Draw.stdAngle ;
		Point pointsPos = Util.Translate(windowTopLeft, size.width + 10 + 28, size.height - 6 - 40) ;
		Color color = Game.palette[21] ;
		
		Game.DP.drawImage(spellPoints, pointsPos, Align.topCenter) ;
		Game.DP.drawText(Util.Translate(pointsPos, 0, 6), Align.topCenter, angle, "Pontos", regularFont, color) ;
		Game.DP.drawText(Util.Translate(pointsPos, 0, 24), Align.topCenter, angle, String.valueOf(points), regularFont, color) ;
		
	}
	
	public void displayWindow()
	{
		double angle = Draw.stdAngle ;
		if (numberTabs <= 1)
		{
			Game.DP.drawImage(image, windowTopLeft, angle, Scale.unit, Align.topLeft) ;
			return ;
		}
		
		Point displayPos = Util.Translate(windowTopLeft, -23, 0) ;
		Point tab1Pos = Util.Translate(windowTopLeft, -10, 6 + 75/2) ;
		Point tab2Pos = Util.Translate(windowTopLeft, -10, 6 + 75 + 75/2) ;
		Image displayImage = tab == 0 ? tab0Image : tab1Image ;
		Color tabTextColor = Game.palette[21] ;
		Game.DP.drawImage(displayImage, displayPos, angle, Scale.unit, Align.topLeft) ;
		Game.DP.drawText(tab1Pos, Align.center, 90, "Basic", largeFont, tab == 0 ? selColor : tabTextColor);
		Game.DP.drawText(tab2Pos, Align.center, 90, "Pro", largeFont, tab == 1 ? selColor : tabTextColor);
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
	
	public void display(Point mousePos)
	{

		double angle = Draw.stdAngle ;
		Color selectedColor = Game.palette[3] ;
		Color hasPreReqColor = Game.palette[21] ;
		Color hasNotPreReqColor = Game.palette[21] ;
		
		displaySpellsInfo() ;
		displayWindow() ;
		
		Point titlePos = Util.Translate(windowTopLeft, size.width / 2, 6 + 9) ;
		Game.DP.drawText(titlePos, Align.center, angle, name, largeFont, Game.palette[21]);
		
		if (spells == null) { return ;}
		
		// display spells
		int initialSpell = tab == 0 ? 0 : 0 ;
		int row = 0 ;
		int col = 0 ;
		for (int i = 0 ; i <= spellsOnWindow.size() - 1 ; i += 1)
		{
			if (spellsDistribution.length <= row) { System.out.println("Tentando desenhar magias demais!") ; break ;}
			
			Spell spell = spellsOnWindow.get(i) ;
			boolean hasPreReq = spell.hasPreRequisitesMet(playerCurrentSpells) ;
			Dimension slotSize = new Dimension(spellSlot.getWidth(null), spellSlot.getHeight(null)) ;
			Color textColor = hasPreReq ? hasPreReqColor : hasNotPreReqColor ;
			Image slotImage = hasPreReq ? spellSlot : spellInactiveSlot ;
			Point slotPos = calcSlotPos(row, col, spellsDistribution.length, spellsDistribution[row], slotSize) ;
			
			checkMouseSelection(mousePos, slotPos, Align.topLeft, slotSize, initialSpell + i) ;
			if (this.item == initialSpell + i)
			{
				textColor = selectedColor ;
				slotImage = hasPreReq ? spellSlotSelected : spellInactiveSlot;
			}
			
			Point spellImagePos = Util.Translate(slotPos, slotSize.width / 2, 4 + 14) ;
			Point spellLevelPos = Util.Translate(slotPos, slotSize.width / 2, slotSize.height / 2 + 18) ;
					
			Game.DP.drawImage(slotImage, slotPos, Align.topLeft) ;
			Game.DP.drawImage(spell.getImage(), spellImagePos, Align.center) ;
			Game.DP.drawText(spellLevelPos, Align.center, angle, String.valueOf(spell.getLevel()), regularFont, textColor) ;
			col += 1 ;			
			
			if (spellsDistribution[row] <= col)
			{
				col = 0 ;
				row += 1 ;
			}
			
		}

		displaySpellPoints(points) ;
	}
	
}
