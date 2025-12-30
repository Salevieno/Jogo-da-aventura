package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import liveBeings.Player;
import liveBeings.Spell;
import liveBeings.SpellTypes;
import main.Game;
import main.GamePanel;
import main.Path;
import utilities.Util;


public class SpellsTreeWindow extends GameWindow
{
	private List<Spell> spells ;
	private List<Spell> playerCurrentSpells ;
	private List<Spell> spellsOnWindow ;
	private int[] spellsDistribution ;
	private int playerJob ;
	private int points ;

	private static final Point windowTopLeft = Game.getScreen().pos(0.4, 0.2) ;
	private static final Font regularFont = new Font(Game.MainFontName, Font.BOLD, 12) ;
	private static final Font largeFont = new Font(Game.MainFontName, Font.BOLD, 14) ;
	private static final Image mainImage = Game.loadImage(Path.WINDOWS_IMG + "SpellsTree.png") ;
	private static final Image tab0Image = Game.loadImage(Path.WINDOWS_IMG + "SpellsTreeTab0.png") ;
	private static final Image tab1Image = Game.loadImage(Path.WINDOWS_IMG + "SpellsTreeTab1.png") ;
	private static final Image spellSlot = Game.loadImage(Path.WINDOWS_IMG + "SpellSlot.png") ;
	private static final Image spellSlotSelected = Game.loadImage(Path.WINDOWS_IMG + "SpellSlotSelected.png") ;
	private static final Image spellInactiveSlot = Game.loadImage(Path.WINDOWS_IMG + "SpellInactiveSlot.png") ;
	private static final Image spellInfo = Game.loadImage(Path.WINDOWS_IMG + "SpellInfo.png") ;

	public SpellsTreeWindow(int playerJob)
	{

		super("Árvore de magias", windowTopLeft, mainImage, 0, 1, 0, 1) ;
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
		int padding = 30 ;
		Point offset = new Point(windowTopLeft.x + border + padding, windowTopLeft.y + 22 + padding) ;
		double spacingX = Util.spacing(mainImage.getWidth(null) - border - padding, numberCols, slotSize.width, padding) ;
		double spacingY = Util.spacing(mainImage.getHeight(null) - 22 - padding, numberRows, slotSize.height, padding) ;

		Point slotPos = new Point((int) (offset.x + col * spacingX), (int) (offset.y + row * spacingY)) ;
		
		return slotPos ;
	}
	
	public void displaySpellsInfo()
	{
		if (spellsOnWindow == null) { return ;}
		if (spellsOnWindow.get(item) == null) { return ;}
	
		Point pos = Util.translate(windowTopLeft, 0, -64) ;
		Color textColor = Game.palette[0] ;
		GamePanel.DP.drawImage(spellInfo, pos, Align.topLeft) ;
		pos.x += 5 ;
		pos.y += 10 ;
		Draw.fitText(pos, 16, Align.centerLeft, spellsOnWindow.get(item).getEffect(), regularFont, 90, textColor) ;
		pos.y += 34 ;
		Draw.fitText(pos, 16, Align.centerLeft, spellsOnWindow.get(item).getDescription(), regularFont, 90, textColor) ;
	}
	
	public void displaySpellPoints(int points)
	{
		double angle = Draw.stdAngle ;
		Point pointsPos = Util.translate(windowTopLeft, border + 6, size.height - border - padding - 6) ;
		Color color = Game.palette[21] ;

		GamePanel.DP.drawText(pointsPos, Align.centerLeft, angle, "Pontos: " + String.valueOf(points), regularFont, color) ;
		
	}
	
	public void displayWindow()
	{
		double angle = Draw.stdAngle ;
		if (numberTabs <= 1)
		{
			GamePanel.DP.drawImage(image, windowTopLeft, angle, Scale.unit, Align.topLeft) ;
			return ;
		}
		
		Point displayPos = Util.translate(windowTopLeft, -23, 0) ;
		Point tab1Pos = Util.translate(windowTopLeft, -10, 6 + 75/2) ;
		Point tab2Pos = Util.translate(windowTopLeft, -10, 6 + 75 + 75/2) ;
		Image displayImage = tab == 0 ? tab0Image : tab1Image ;
		Color tabTextColor = Game.palette[21] ;
		GamePanel.DP.drawImage(displayImage, displayPos, angle, Scale.unit, Align.topLeft) ;
		GamePanel.DP.drawText(tab1Pos, Align.center, 90, "Basic", largeFont, tab == 0 ? Game.selColor : tabTextColor);
		GamePanel.DP.drawText(tab2Pos, Align.center, 90, "Pro", largeFont, tab == 1 ? Game.selColor : tabTextColor);
	}
	
	public List<Spell> basicSpells()
	{
		return spells.subList(0, Player.numberOfSpellsPerJob[playerJob]) ;
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
		Color selectedColor = Game.palette[18] ;
		Color hasPreReqColor = Game.palette[21] ;
		Color hasNotPreReqColor = Game.palette[21] ;
		
		displaySpellsInfo() ;
		displayWindow() ;
		
		Point titlePos = Util.translate(windowTopLeft, size.width / 2, 6 + 6) ;
		GamePanel.DP.drawText(titlePos, Align.center, angle, name, largeFont, Game.palette[21]);
		
		if (spells == null) { return ;}
		
		// display spells
		int initialSpell = tab == 0 ? 0 : 0 ;
		Point space = new Point(28, 23) ;
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
			
			Point spellImagePos = Util.translate(slotPos, slotSize.width / 2, 4 + space.y) ;
			Point spellLevelPos = Util.translate(slotPos, slotSize.width / 2, slotSize.height / 2 + space.y + 4) ;
			Point spellNamePos = Util.translate(slotPos, slotSize.width / 2, -5) ;
					
			GamePanel.DP.drawImage(slotImage, slotPos, Align.topLeft) ;
			GamePanel.DP.drawImage(spell.getImage(), spellImagePos, Align.center) ;			
			GamePanel.DP.drawText(spellNamePos, Align.bottomCenter, angle, spell.getName(), regularFont, textColor) ;
			GamePanel.DP.drawText(spellLevelPos, Align.center, angle, String.valueOf(spell.getLevel()), regularFont, textColor) ;
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
