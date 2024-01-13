package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.SpellTypes;
import graphics.Draw;
import graphics.DrawPrimitives;
import liveBeings.Player;
import liveBeings.Spell;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;
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
	{// TODO arrumar a árvore de magias. Spells tá se confundindo com spellsOnWindow

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
		String action = player.getCurrentAction() ;
		Spell spell = spellsOnWindow.get(item) ;
		
		if (canAcquireSpell(points) & actionIsForward(action) & spell.hasPreRequisitesMet(playerCurrentSpells))
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
	
	public void displaySpellsInfo(DrawPrimitives DP)
	{
		if (spellsOnWindow == null) { return ;}
		if (spellsOnWindow.get(item) == null) { return ;}
		
		double angle = Draw.stdAngle ;
		Point pos = UtilG.Translate(windowTopLeft, 0, -66) ;
		Color textColor = Game.colorPalette[0] ;
		Point spellNamePos = UtilG.Translate(windowTopLeft, spellInfo.getWidth(null) / 2, - 66 - 10) ;
		DP.drawImage(spellInfo, pos, Align.topLeft) ;
		DP.drawText(spellNamePos, Align.center, angle, spellsOnWindow.get(item).getName(), regularFont, textColor) ;
		pos.x += 5 ;
		pos.y += 8 ;
		for (String info : spellsOnWindow.get(item).getInfo())
		{
			Draw.fitText(pos, 16, Align.centerLeft, info, regularFont, 50, textColor) ;
			pos.y += 34 ;
		}
	}
	
	public void displaySpellPoints(int points, DrawPrimitives DP)
	{
		double angle = Draw.stdAngle ;
		Point pointsPos = UtilG.Translate(windowTopLeft, size.width + 10 + 28, size.height - 6 - 40) ;
		Color color = Game.colorPalette[21] ;
		
		DP.drawImage(spellPoints, pointsPos, Align.topCenter) ;
		DP.drawText(UtilG.Translate(pointsPos, 0, 6), Align.topCenter, angle, "Pontos", regularFont, color) ;
		DP.drawText(UtilG.Translate(pointsPos, 0, 24), Align.topCenter, angle, String.valueOf(points), regularFont, color) ;
		
	}
	
	public void displayWindow(DrawPrimitives DP)
	{
		double angle = Draw.stdAngle ;
		if (numberTabs <= 1)
		{
			DP.drawImage(image, windowTopLeft, angle, Scale.unit, Align.topLeft) ;
			return ;
		}
		
		Point displayPos = UtilG.Translate(windowTopLeft, -23, 0) ;
		Point tab1Pos = UtilG.Translate(windowTopLeft, -10, 6 + 75/2) ;
		Point tab2Pos = UtilG.Translate(windowTopLeft, -10, 6 + 75 + 75/2) ;
		Image displayImage = tab == 0 ? tab0Image : tab1Image ;
		Color tabTextColor = Game.colorPalette[21] ;
		DP.drawImage(displayImage, displayPos, angle, Scale.unit, Align.topLeft) ;
		DP.drawText(tab1Pos, Align.center, 90, "Basic", largeFont, tab == 0 ? selColor : tabTextColor);
		DP.drawText(tab2Pos, Align.center, 90, "Pro", largeFont, tab == 1 ? selColor : tabTextColor);
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
	
	public void display(Point mousePos, DrawPrimitives DP)
	{

		double angle = Draw.stdAngle ;
		Color selectedColor = Game.colorPalette[3] ;
		Color hasPreReqColor = Game.colorPalette[21] ;
		Color hasNotPreReqColor = Game.colorPalette[21] ;
		
		displaySpellsInfo(DP) ;
		displayWindow(DP) ;
		
		Point titlePos = UtilG.Translate(windowTopLeft, size.width / 2, 6 + 9) ;
		DP.drawText(titlePos, Align.center, angle, name, largeFont, Game.colorPalette[21]);
		
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
			
			Point spellImagePos = UtilG.Translate(slotPos, slotSize.width / 2, 4 + 14) ;
			Point spellLevelPos = UtilG.Translate(slotPos, slotSize.width / 2, slotSize.height / 2 + 18) ;
					
			DP.drawImage(slotImage, slotPos, Align.topLeft) ;
			DP.drawImage(spell.getImage(), spellImagePos, Align.center) ;
			DP.drawText(spellLevelPos, Align.center, angle, String.valueOf(spell.getLevel()), regularFont, textColor) ;
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
