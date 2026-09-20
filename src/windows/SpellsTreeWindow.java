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
import main.Game;
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
    private final Point titlePos ;
	private final Point spacing ;
	private final Point tabPos ;
	private final Point tab1TextPos ;
	private final Point tab2TextPos ;
	private final int textLineSpacing ;
	private final Point spellsInfoPos ;
	private final Point spellsInfoEffectPos ;
	private final Point spellsInfoDescriptionPos ;
	private final int maxSpellInfoLength ;
	private final Point pointsPos ;
	private List<Spell> spells ;
	private List<Spell> spellsOnPage ;
	private int[] spellsDistribution ;
	private int playerJob ;
	private int points ;
	private List<Point> slotPos ;
	private List<Point> spellImagePos ;
	private List<Point> spellLevelPos ;
	private List<Point> spellNamePos ;

	private static final Image WINDOW_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellsTree.png") ;
	private static final Image TAB_0_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellsTreeTab0.png") ;
	private static final Image TAB_1_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellsTreeTab1.png") ;
	private static final Image SPELL_SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellSlot.png") ;
	private static final Image SPELL_SLOT_SELECTED_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellSlotSelected.png") ;
	private static final Image SPELL_INACTIVE_SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellInactiveSlot.png") ;
	private static final Image SPELL_INFO_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "SpellInfo.png") ;
	private static final Color SELECTED_COLOR = Palette.colors[18] ;
	private static final Color HAS_PRE_REQ_COLOR = Palette.colors[21] ;
	private static final Color DOESNT_HAVE_PRE_REQ_COLOR = Palette.colors[21] ;
	private static final Dimension SLOT_SIZE = Util.getSize(SPELL_SLOT_IMAGE) ;
// TODO ajustar posições dos spells
	public SpellsTreeWindow(int playerJob)
	{
		super("Árvore de magias", Screen.getMe().pos(0.4, 0.2), WINDOW_IMAGE, 0, 1, 0, 1) ;
        this.titlePos = Util.translate(topLeftPos, size.width / 2, 6 + 6) ;
        this.spacing = new Point(28, 23) ;
        this.tabPos = Util.translate(topLeftPos, -23, 0) ;
        this.tab1TextPos = Util.translate(topLeftPos, -10, 6 + 75/2) ;
        this.tab2TextPos = Util.translate(topLeftPos, -10, 6 + 75 + 75/2) ;
        this.textLineSpacing = SUBTITLE_FONT.getSize() + 2 ;
        this.spellsInfoPos = Util.translate(topLeftPos, 0, -64) ;
        this.spellsInfoEffectPos = Util.translate(spellsInfoPos, 5, 10) ;
        this.spellsInfoDescriptionPos = Util.translate(spellsInfoPos, 5, 40) ;
        this.maxSpellInfoLength = SPELL_INFO_IMAGE.getWidth(null) - 5 ;
        this.pointsPos = Util.translate(topLeftPos, BORDER + 6, size.height - BORDER - PADDING - 6) ;
		this.playerJob = playerJob ;
		this.spells = new ArrayList<>() ;
	}

    protected void onOpen()
    {
        this.spells = Game.getPlayer().getSpells() ;
        this.points = Game.getPlayer().getSpellPoints() ;
		updateSpellsOnPage() ;
		this.numberItems = spellsOnPage.size() ;
        updateSpellsDistribution() ;
	    this.slotPos = new ArrayList<>(spellsOnPage.size()) ;
	    this.spellImagePos = new ArrayList<>(spellsOnPage.size()) ;
	    this.spellLevelPos = new ArrayList<>(spellsOnPage.size()) ;
	    this.spellNamePos = new ArrayList<>(spellsOnPage.size()) ;

		int row = 0 ;
		int col = 0 ;
        for (int i = 0 ; i <= spellsOnPage.size() - 1 ; i += 1)
		{
            Point newSlotPos = calcSlotPos(row, col, spellsDistribution.length, spellsDistribution[row], SLOT_SIZE) ;
            this.slotPos.add(newSlotPos) ;
            this.spellImagePos.add(Util.translate(newSlotPos, SLOT_SIZE.width / 2, 4 + spacing.y)) ;
            this.spellLevelPos.add(Util.translate(newSlotPos, SLOT_SIZE.width / 2, SLOT_SIZE.height / 2 + spacing.y + 4)) ;
            this.spellNamePos.add(Util.translate(newSlotPos, SLOT_SIZE.width / 2, -5)) ;

			col += 1 ;			
			
			if (spellsDistribution[row] <= col)
			{
				col = 0 ;
				row += 1 ;
			}
        }
    }
	
	public void act(Player player, Point mousePos)
	{
		String action = player.getCurrentAction() ;
		
		if (canAcquireSpell(points) && actionIsForward(action))
		{
			acquireSpell(player) ;
			points += -1 ;
			player.decSpellPoints() ;
		}
	}
		
	public void enableTab2() { numberTabs = 2 ;}

    // proTODO verificar se funciona com duas abas
	private boolean canAcquireSpell(int spellPoints) { return 0 < spellPoints && !spells.get(item).isMaxed() && spells.get(item).hasPreRequisitesMet(spells) ;}
	
	private void acquireSpell(Player player)
	{
		Spell spell = spellsOnPage.get(item) ;
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
	
	private void updateSpellsDistribution()
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
			if (action.equals(stdPageUp))
			{
				tabDown() ;
                updateTab() ;
			}
			if (action.equals(stdPageDown))
			{
				tabUp() ;
                updateTab() ;
			}
		}
		if (action.equals("Escape"))
		{
			close() ;
		}
	}
    protected int itemHoveredID(Point mousePos)
    {
		for (int i = 0 ; i <= spellsOnPage.size() - 1 ; i += 1)
		{
            if (itemIsHovered(mousePos, spellImagePos.get(i), Align.center, SLOT_SIZE)) { return i ;}
        }
        return -1 ;
    }

    private void updateTab()
    {
        /*
        proTODO
         *  atualiza spellsOnPage e spellsDistribution, mas não atualiza:
            slotPos
            spellImagePos
            spellLevelPos
            spellNamePos
            As posições calculadas em onOpen() podem ficar incompatíveis com a nova aba.
            O cálculo dos slots deveria ser extraído para um método, por exemplo updateSpellPositions(), e chamado em onOpen() e updateTab().
         */
        this.item = 0 ;
        updateSpellsOnPage() ;
        updateSpellsDistribution() ;
        this.numberItems = spellsOnPage.size() ;
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
	
	private void displaySpellsInfo()
	{
		if (spellsOnPage == null) { return ;}
		if (spellsOnPage.isEmpty()) { return ;}
		if (spellsOnPage.size() <= item) { Log.warn("Tentando obter spellOnPage além do tamanho da lista") ; return ;}
		if (spellsOnPage.get(item) == null) { return ;}

        Spell spell = spellsOnPage.get(item) ;
		GamePanel.getDP().drawImage(SPELL_INFO_IMAGE, spellsInfoPos, Align.topLeft) ;
		Draw.fitText(spellsInfoEffectPos, textLineSpacing, Align.centerLeft, spell.getEffect(), SUBTITLE_FONT, maxSpellInfoLength, Palette.colors[0]) ;
		Draw.fitText(spellsInfoDescriptionPos, textLineSpacing, Align.centerLeft, spell.getDescription(), SUBTITLE_FONT, maxSpellInfoLength, Palette.colors[0]) ;
	}
	
	private void displayWindow()
	{
		if (numberTabs <= 1)
		{
			GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft) ;
			return ;
		}
	
		Image displayImage = tab == 0 ? TAB_0_IMAGE : TAB_1_IMAGE ;
		GamePanel.getDP().drawImage(displayImage, tabPos, Scale.unit, Align.topLeft) ;
		GamePanel.getDP().drawText(tab1TextPos, Align.center, 90, "Basic", TITLE_FONT, tab == 0 ? SELECTED_COLOR : Palette.colors[21]);
		GamePanel.getDP().drawText(tab2TextPos, Align.center, 90, "Pro", TITLE_FONT, tab == 1 ? SELECTED_COLOR : Palette.colors[21]);
	}
	
	private List<Spell> basicSpells() { return spells.subList(0, Player.QTD_SPELLS_PER_JOB[playerJob]) ;}
	private List<Spell> proSpells() { return spells.subList(spells.size() - 10, spells.size()) ;}
	private void updateSpellsOnPage() { spellsOnPage = tab == 0 ? basicSpells() : proSpells() ;}

	public void display(Point mousePos)
	{
		displaySpellsInfo() ;
		displayWindow() ;
		
		GamePanel.getDP().drawText(titlePos, Align.center, name, TITLE_FONT, Palette.colors[21]);
		
		if (spells == null) { return ;}
		
		// display spells
		int initialSpell = tab == 0 ? 0 : 0 ;
		for (int i = 0 ; i <= spellsOnPage.size() - 1 ; i += 1)
		{			
			Spell spell = spellsOnPage.get(i) ;
			boolean hasPreReq = spell.hasPreRequisitesMet(spells) ;
			Color textColor = hasPreReq ? HAS_PRE_REQ_COLOR : DOESNT_HAVE_PRE_REQ_COLOR ;
			Image slotImage = hasPreReq ? SPELL_SLOT_IMAGE : SPELL_INACTIVE_SLOT_IMAGE ;
			if (this.item == initialSpell + i)
			{
				textColor = SELECTED_COLOR ;
				slotImage = hasPreReq ? SPELL_SLOT_SELECTED_IMAGE : SPELL_INACTIVE_SLOT_IMAGE;
			}
					
			GamePanel.getDP().drawImage(slotImage, slotPos.get(i), Align.topLeft) ;
			GamePanel.getDP().drawImage(spell.getImage(), spellImagePos.get(i), Align.center) ;			
			GamePanel.getDP().drawText(spellNamePos.get(i), Align.bottomCenter, spell.getName(), SUBTITLE_FONT, textColor) ;
			GamePanel.getDP().drawText(spellLevelPos.get(i), Align.center, String.valueOf(spell.getLevel()), SUBTITLE_FONT, textColor) ;
		}

		GamePanel.getDP().drawText(pointsPos, Align.centerLeft, "Pontos: " + points, SUBTITLE_FONT, Palette.colors[21]) ;
	}

	protected void onClose() { }
}