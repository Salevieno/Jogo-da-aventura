package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import liveBeings.Player;
import main.GamePanel;
import main.ImageLoader;
import main.Log;
import main.Palette;
import main.Path;
import screen.Screen;
import spells.Spell;
import spells.SpellTypes;
import utilities.Util;


public class SpellsTreeWindow extends GameWindow
{
	private List<Spell> spells ;
	private List<Spell> playerCurrentSpells ;
	private List<Spell> spellsOnWindow ;
	private int[] spellsDistribution ;
	private int playerJob ;
	private int points ;

	private static final Image WINDOW_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellsTree.png") ;
	private static final Image TAB_0_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellsTreeTab0.png") ;
	private static final Image TAB_1_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellsTreeTab1.png") ;
	private static final Image SPELL_SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellSlot.png") ;
	private static final Image SPELL_SLOT_SELECTED_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellSlotSelected.png") ;
	private static final Image SPELL_INACTIVE_SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellInactiveSlot.png") ;
	private static final Image SPELL_INFO_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellInfo.png") ;

	public SpellsTreeWindow(int playerJob)
	{
		super("Árvore de magias", Screen.getMe().pos(0.4, 0.2), WINDOW_IMAGE, 0, 1, 0, 1) ;
		this.playerJob = playerJob ;
		this.spells = new ArrayList<>() ;
		this.playerCurrentSpells = new ArrayList<>() ;
	}
		
	public void switchTo2Tabs() { numberTabs = 2 ;}

	public void setSpells(List<Spell> spells)
	{
		this.spells = spells ;
		updateSpellsOnWindow() ;
		numberItems = spellsOnWindow.size() ;
	}

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
		Point offset = new Point(topLeftPos.x + BORDER + padding, topLeftPos.y + 22 + padding) ;
		double spacingX = Util.spacing(WINDOW_IMAGE.getWidth(null) - BORDER - padding, numberCols, slotSize.width, padding) ;
		double spacingY = Util.spacing(WINDOW_IMAGE.getHeight(null) - 22 - padding, numberRows, slotSize.height, padding) ;

		Point slotPos = new Point((int) (offset.x + col * spacingX), (int) (offset.y + row * spacingY)) ;
		
		return slotPos ;
	}
	
	public void displaySpellsInfo()
	{
		if (spellsOnWindow == null) { return ;}
		if (spellsOnWindow.get(item) == null) { return ;}
	
		Point pos = Util.translate(topLeftPos, 0, -64) ;
		int padding = 5 ;
		int sy = SUBTITLE_FONT.getSize() + 2 ;
		Point effectPos = Util.translate(pos, padding, 10) ;
		Point descriptionPos = Util.translate(pos, padding, 40) ;
		int maxTextLength = SPELL_INFO_IMAGE.getWidth(null) - padding ;
		Color textColor = Palette.colors[0] ;
		GamePanel.getDP().drawImage(SPELL_INFO_IMAGE, pos, Align.topLeft) ;
		Draw.fitText(effectPos, sy, Align.centerLeft, spellsOnWindow.get(item).getEffect(), SUBTITLE_FONT, maxTextLength, textColor) ;
		Draw.fitText(descriptionPos, sy, Align.centerLeft, spellsOnWindow.get(item).getDescription(), SUBTITLE_FONT, maxTextLength, textColor) ;
	}
	
	public void displaySpellPoints(int points)
	{
		double angle = Draw.stdAngle ;
		Point pointsPos = Util.translate(topLeftPos, BORDER + 6, size.height - BORDER - PADDING - 6) ;
		Color color = Palette.colors[21] ;

		GamePanel.getDP().drawText(pointsPos, Align.centerLeft, angle, "Pontos: " + String.valueOf(points), SUBTITLE_FONT, color) ;
		
	}
	
	public void displayWindow()
	{
		double angle = Draw.stdAngle ;
		if (numberTabs <= 1)
		{
			GamePanel.getDP().drawImage(image, topLeftPos, angle, Scale.unit, Align.topLeft) ;
			return ;
		}
		
		Point displayPos = Util.translate(topLeftPos, -23, 0) ;
		Point tab1Pos = Util.translate(topLeftPos, -10, 6 + 75/2) ;
		Point tab2Pos = Util.translate(topLeftPos, -10, 6 + 75 + 75/2) ;
		Image displayImage = tab == 0 ? TAB_0_IMAGE : TAB_1_IMAGE ;
		Color tabTextColor = Palette.colors[21] ;
		GamePanel.getDP().drawImage(displayImage, displayPos, angle, Scale.unit, Align.topLeft) ;
		GamePanel.getDP().drawText(tab1Pos, Align.center, 90, "Basic", TITLE_FONT, tab == 0 ? SELECTED_COLOR : tabTextColor);
		GamePanel.getDP().drawText(tab2Pos, Align.center, 90, "Pro", TITLE_FONT, tab == 1 ? SELECTED_COLOR : tabTextColor);
	}
	
	public List<Spell> basicSpells()
	{
		return spells.subList(0, Player.QTD_SPELLS_PER_JOB[playerJob]) ;
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
		Color selectedColor = Palette.colors[18] ;
		Color hasPreReqColor = Palette.colors[21] ;
		Color hasNotPreReqColor = Palette.colors[21] ;
		
		displaySpellsInfo() ;
		displayWindow() ;
		
		Point titlePos = Util.translate(topLeftPos, size.width / 2, 6 + 6) ;
		GamePanel.getDP().drawText(titlePos, Align.center, angle, name, TITLE_FONT, Palette.colors[21]);
		
		if (spells == null) { return ;}
		
		// display spells
		int initialSpell = tab == 0 ? 0 : 0 ;
		Point space = new Point(28, 23) ;
		int row = 0 ;
		int col = 0 ;
		for (int i = 0 ; i <= spellsOnWindow.size() - 1 ; i += 1)
		{
			if (spellsDistribution.length <= row) { Log.warn("Tentando desenhar magias demais!") ; break ;}
			
			Spell spell = spellsOnWindow.get(i) ;
			boolean hasPreReq = spell.hasPreRequisitesMet(playerCurrentSpells) ;
			Dimension slotSize = new Dimension(SPELL_SLOT_IMAGE.getWidth(null), SPELL_SLOT_IMAGE.getHeight(null)) ;
			Color textColor = hasPreReq ? hasPreReqColor : hasNotPreReqColor ;
			Image slotImage = hasPreReq ? SPELL_SLOT_IMAGE : SPELL_INACTIVE_SLOT_IMAGE ;
			Point slotPos = calcSlotPos(row, col, spellsDistribution.length, spellsDistribution[row], slotSize) ;
			
			checkMouseSelection(mousePos, slotPos, Align.topLeft, slotSize, initialSpell + i) ;
			if (this.item == initialSpell + i)
			{
				textColor = selectedColor ;
				slotImage = hasPreReq ? SPELL_SLOT_SELECTED_IMAGE : SPELL_INACTIVE_SLOT_IMAGE;
			}
			
			Point spellImagePos = Util.translate(slotPos, slotSize.width / 2, 4 + space.y) ;
			Point spellLevelPos = Util.translate(slotPos, slotSize.width / 2, slotSize.height / 2 + space.y + 4) ;
			Point spellNamePos = Util.translate(slotPos, slotSize.width / 2, -5) ;
					
			GamePanel.getDP().drawImage(slotImage, slotPos, Align.topLeft) ;
			GamePanel.getDP().drawImage(spell.getImage(), spellImagePos, Align.center) ;			
			GamePanel.getDP().drawText(spellNamePos, Align.bottomCenter, angle, spell.getName(), SUBTITLE_FONT, textColor) ;
			GamePanel.getDP().drawText(spellLevelPos, Align.center, angle, String.valueOf(spell.getLevel()), SUBTITLE_FONT, textColor) ;
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
