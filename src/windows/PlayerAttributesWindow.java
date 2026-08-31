package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UI.ButtonFunction;
import UI.GameButton;
import UI.GameIconButton;
import attributes.Attributes;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import items.Equip;
import liveBeings.Player;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import shared.SharedImages;
import utilities.Util;


public class PlayerAttributesWindow extends AttributesWindow
{	
	private final Point topLeftPos ;

	private final Point tabsTextPos ;

	private final Point playerNamePos ;

    private final Point[] eqSlotCenter ;
	private final Point[] eqTextPos ;
	private final Point[] elemPos ;
    
    private final String[] attText ;
    private final Point lifePos ;
    private final Point mpPos ;
    private final int attTextImgOffset ;
    private final int attSpacingY ;
    private final Point battleAttCenterLeft ;
	private final Point[] attValuePos ;
	private final Point[] attImagePos ;
    private final Point collectImgCenter ;
    private final Point goldImgCenter ;
    private final Point critImgCenter ;
    private final Point critPos ;
    private final Color[] collectColors ;
	private final Point goldTextPos ;
	private final Font attFont ;

	private final Point playerImgPos ;
	private final Point superElemPos ;
	private final Point powerPos ;
	private final Point levelPos ;
	private final Point jobTextPos ;

    
	private Player player ;
    private String levelText ;
	private String jobText ;
	private Map<Attributes, GameButton> incAttButtons ;
	
    private static final List<Image> TAB_IMAGES = List.of(
        ImageLoader.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow1.png"),
        ImageLoader.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow2.png"),
        ImageLoader.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow3.png")
    ) ;
	private static final Image PLUS_SIGN_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PlusSign.png") ;
	private static final Image PLUS_SELECTED_SIGN_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PlusSignShining.png") ;
    
    private static final Font FONT_SMALL = new Font(Game.getMainFontName(), Font.BOLD, 9) ;
    private static final Font FONT_STD = new Font(Game.getMainFontName(), Font.BOLD, 11) ;

	public PlayerAttributesWindow()
	{		
		super(TAB_IMAGES.get(0), 3) ;
		this.topLeftPos = Screen.getMe().pos(0.01, 0.25) ;

        this.tabsTextPos = Util.translate(topLeftPos, 14, 56) ;

        this.playerNamePos = Util.translate(topLeftPos, size.width / 2, 11) ;

        this.eqSlotCenter = new Point[] {
				Util.translate(topLeftPos, 110, 156),
				Util.translate(topLeftPos, 397, 106),
				Util.translate(topLeftPos, 397, 205),
				Util.translate(topLeftPos, 140, 164)} ;
                
		int eqSlotSize = 51 ;
		this.eqTextPos = new Point[] {
            Util.translate(eqSlotCenter[0], -eqSlotSize / 2, eqSlotSize / 2 + 13),
            Util.translate(eqSlotCenter[1], -eqSlotSize / 2, eqSlotSize / 2 + 13),
            Util.translate(eqSlotCenter[2], -eqSlotSize / 2, eqSlotSize / 2 + 13),
            Util.translate(eqSlotCenter[3], -eqSlotSize / 2, eqSlotSize / 2 + 13)
        } ;

        this.elemPos = new Point[] {
            Util.translate(eqSlotCenter[0], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12),
            Util.translate(eqSlotCenter[1], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12),
            Util.translate(eqSlotCenter[2], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12),
            Util.translate(eqSlotCenter[3], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12)
        } ;

		this.attText = new String[] {"Nível", "Vida", "Mana", "Atq Fis", "Atq Mag", "Def Fis", "Def Mag", "Destreza", "Agilidade", "Crítico", "Atordoamento", "Bloqueio", "Sangramento", "Envenenamento", "Silêncio", "Elemento", "Coleta", "Exp", "Ouro", "Saciedade", "Água"} ;
		this.lifePos = Util.translate(topLeftPos, 37, 49) ;
		this.mpPos = Util.translate(lifePos, 0, 27) ;
		this.attTextImgOffset = 12 + 4 ;
		this.attSpacingY = 27 ;
		this.battleAttCenterLeft = Util.translate(topLeftPos, 35 + 18, 289) ;
        int qtdAttributes = 8 ;
        this.attValuePos = new Point[qtdAttributes] ;
        this.attImagePos = new Point[qtdAttributes] ;
        for (int i = 0 ; i <= qtdAttributes - 1 ; i += 1)
        {
            this.attValuePos[i] = Util.translate(battleAttCenterLeft, attTextImgOffset, i * attSpacingY) ;
            this.attImagePos[i] = Util.translate(topLeftPos, 35 + 18, 289 + i * attSpacingY) ;
        }
		this.collectImgCenter = Util.translate(topLeftPos, 324, 401) ;
		this.goldImgCenter = Util.translate(collectImgCenter, 0, 80) ;
		this.critImgCenter = Util.translate(topLeftPos, 35 + 18, 477) ;
		this.critPos = Util.translate(critImgCenter, attTextImgOffset, 0) ;
        this.collectColors = new Color[] {Palette.colors[4], Palette.colors[8], Palette.colors[1]} ;
        this.goldTextPos = Util.translate(goldImgCenter, attTextImgOffset, 0) ;
        this.attFont = SUBTITLE_FONT ;

	    this.playerImgPos = Util.translate(topLeftPos, size.width / 2, 156) ;
	    this.superElemPos = Util.translate(playerImgPos, 0, 35) ;
	    this.powerPos = Util.translate(topLeftPos, 430, 490) ;
	    this.levelPos = Util.translate(topLeftPos, size.width / 2, 56) ;
	    this.jobTextPos = Util.translate(topLeftPos, size.width / 2, 38) ;

		this.incAttButtons = new HashMap<>() ;
	}
	
    public void update(Player player)
    {
        this.player = player ;

        this.levelText = "Level: " + player.getLevel() ;
		String[] classesText = Game.getAllText().get(TextCategories.classes) ;
		String[] proClassesText = Game.getAllText().get(TextCategories.proclasses) ;
        this.jobText = player.getProJob() == 0 ? classesText[player.getJob()] : proClassesText[2 * player.getJob() + player.getProJob() - 1] ;
        
        if (this.incAttButtons.isEmpty())
        {
            Point pos = Util.translate(topLeftPos, 27, 280) ;
            for (Attributes att : Arrays.asList(Attributes.getIncrementable()))
            {
                ButtonFunction method = () -> {
                    player.getBA().mapAttributes(att).incBaseValue(1) ;
                    player.decAttPoints(1) ;
                    updateAttIncButtons() ;
                } ;
                GameButton newAttButton = new GameIconButton(pos, Align.center, PLUS_SIGN_IMAGE, PLUS_SELECTED_SIGN_IMAGE, method) ;
                newAttButton.deactivate() ;
                incAttButtons.put(att, newAttButton) ;
                pos.y += 27 ;
            }
        }

        updateAttIncButtons() ;
    }

    private void updateAttIncButtons()
    {
		if (player.getAttPoints() <= 0)
		{
			incAttButtons.values().forEach(GameButton::deactivate) ;
			return ;
		}
		
		incAttButtons.values().forEach(GameButton::activate) ;
    }

	public void navigate(String action)
	{
		if (action.equals(stdMenuDown))
		{
			tabUp() ;
		}
		if (action.equals(stdMenuUp))
		{
			tabDown() ;
		}
	}
	
	private void displayEquips(Point mousePos, Color textColor)
	{
		Equip[] equips = player.getEquips() ;

		if (equips == null) { return ;}
		
		for (int i = 0 ; i <= equips.length - 1 ; i += 1)
		{
			if (equips[i] == null) { continue ;}
			
			Equip equip = equips[i] ;

            String equipText = (0 < equip.getForgeLevel() ? "+ " + equip.getForgeLevel() : "") + " " + equip.getName() ;
			GamePanel.getDP().drawImage(equip.fullSizeImage(), eqSlotCenter[i], Align.center) ;
			Draw.textUntil(eqTextPos[i], Align.bottomLeft, equipText, FONT_SMALL, textColor, 14, mousePos) ;

			Elements eqElem = player.getEquips()[i + 1] != null ? player.getEquips()[i + 1].getElem() : null ;

			if (eqElem == null || i == 3) { continue ;}

			GamePanel.getDP().drawImage(eqElem.image, elemPos[i], new Scale(0.5, 0.5), Align.center) ;
		}

		// Arrow
		if (player.getEquippedArrow() != null)
		{
			GamePanel.getDP().drawImage(player.getEquippedArrow().fullSizeImage(), Util.translate(topLeftPos, 100, 133), Align.bottomCenter) ;
		}
	}
	
	private void displayAttributes()
	{
        // Vida e mana
		String lifeText = attText[1] + ": " + Util.round(player.getPA().getLife().getTotalValue(), 1) ;
		String mpText = attText[2] + ": " + Util.round(player.getPA().getMp().getTotalValue(), 1) ;
		GamePanel.getDP().drawText(lifePos, Align.centerLeft, lifeText, attFont, Palette.colors[7]) ;
		GamePanel.getDP().drawText(mpPos, Align.centerLeft, mpText, attFont, Palette.colors[19]) ;
		
        // Basic attributes
		String[] attText = player.getBA().basicAttributesText() ;
		for (int i = 0; i <= attText.length - 3; i += 1)
		{
			GamePanel.getDP().drawImage(ATT_ICONS[i], attImagePos[i], Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(attValuePos[i], Align.centerLeft, attText[i], attFont, Palette.colors[0]) ;
		}

        // Crit
		String critText = Util.round(100 * player.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.getDP().drawImage(CRIT_ICON, critImgCenter, Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(critPos, Align.centerLeft, critText, attFont, Palette.colors[6]) ;
		
		//	Collecting
        for (int i = 0 ; i <= COLLECT_ICONS.length - 1 ; i += 1)
        {
            String collectText = String.valueOf(Util.round(player.getCollect().get(i), 1)) ;
		    GamePanel.getDP().drawImage(COLLECT_ICONS[i], Util.translate(collectImgCenter, 0, i * attSpacingY), Scale.unit, Align.center) ;
		    GamePanel.getDP().drawText(Util.translate(collectImgCenter, attTextImgOffset, i * attSpacingY), Align.centerLeft, collectText, attFont, collectColors[i]) ;
        }

		//	Gold
		String goldText = String.valueOf(Util.round(player.getBag().getGold(), 1)) ;
		GamePanel.getDP().drawImage(SharedImages.getCoinImg(), goldImgCenter, Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(goldTextPos, Align.centerLeft, goldText, attFont, Palette.colors[13]) ;	
	}
	
	public void displayTab0(Point mousePos)
	{
		player.getMovingAni().spriteIdle.display(GamePanel.getDP(), playerImgPos, Align.center) ;
	
		GamePanel.getDP().drawText(levelPos, Align.center, levelText, attFont, Palette.colors[7]) ;
		GamePanel.getDP().drawText(jobTextPos, Align.center, jobText, attFont, Palette.colors[0]) ;
		
		displayEquips(mousePos, Palette.colors[0]) ;		

		if (player.hasSuperElement())
		{
			GamePanel.getDP().drawImage(player.getSuperElem().image, superElemPos, new Scale(0.3, 0.3), Align.center) ;
		}

		displayAttributes() ;
		player.displayPowerBar(powerPos) ;
		
		incAttButtons.values().forEach(button -> button.display(false, mousePos)) ;
	}
	
	public void displayTab1(Player player)
	{
		
		Color textColor = Palette.colors[0] ;
		String[] attText = Game.getAllText().get(TextCategories.attributes) ;
		
		int leftColX = 44 + 4 ;
		int rightColX = 197 + 4 ;
		int topRowY = 35 ;
		int secondRowY = 35 + 110 ;
		int bottomRowY = 35 + 261 ;
		
		// Titles
		GamePanel.getDP().drawText(Util.translate(topLeftPos, leftColX, topRowY), Align.centerLeft, attText[10], FONT_STD, textColor) ;
		GamePanel.getDP().drawText(Util.translate(topLeftPos, rightColX, topRowY), Align.centerLeft, attText[11], FONT_STD, textColor) ;
		GamePanel.getDP().drawText(Util.translate(topLeftPos, leftColX, secondRowY), Align.centerLeft, attText[12], FONT_STD, textColor) ;
		GamePanel.getDP().drawText(Util.translate(topLeftPos, rightColX, secondRowY), Align.centerLeft, attText[13], FONT_STD, textColor) ;
		GamePanel.getDP().drawText(Util.translate(topLeftPos, leftColX, bottomRowY), Align.centerLeft, attText[14], FONT_STD, textColor) ;

		// att values
		Point stunValuesPos = Util.translate(topLeftPos, leftColX, 56) ;
		Point blockValuesPos = Util.translate(topLeftPos, rightColX, 56) ;
		Point bloodValuesPos = Util.translate(topLeftPos, leftColX, 56 + 110) ;
		Point poisonValuesPos = Util.translate(topLeftPos, rightColX, 56 + 110) ;
		Point silenceValuesPos = Util.translate(topLeftPos, leftColX, 56 + 261) ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)
		{
			GamePanel.getDP().drawText(stunValuesPos, Align.centerLeft, player.getBA().getStun().texts()[i], FONT_STD, textColor) ;
			GamePanel.getDP().drawText(blockValuesPos, Align.centerLeft, player.getBA().getBlock().texts()[i], FONT_STD, textColor) ;
			GamePanel.getDP().drawText(silenceValuesPos, Align.centerLeft, player.getBA().getSilence().texts()[i], FONT_STD, textColor) ;
			
			stunValuesPos.y += 22 ;
			blockValuesPos.y += 22 ;
			silenceValuesPos.x += 77 ;
		}
		for (int i = 0 ; i <= 5 - 1 ; ++i)
		{
			GamePanel.getDP().drawText(bloodValuesPos, Align.centerLeft, player.getBA().getBlood().texts()[i], FONT_STD, textColor) ;
			GamePanel.getDP().drawText(poisonValuesPos, Align.centerLeft, player.getBA().getPoison().texts()[i], FONT_STD, textColor) ;
			
			bloodValuesPos.y += 22 ;
			poisonValuesPos.y += 22 ;
		}
	}
	
	public void displayTab2(Player player)
	{
		String title = "Totais" ;
		List<String> subTitles = List.of("Causados", "Recebidos", "Defendidos") ;
		Color textColor = Palette.colors[0] ;
		
		Map<String, Integer> numberStats = player.getStatistics().numberStats() ;
		Map<String, Double> damageStats = player.getStatistics().damageStats() ;
		Map<String, Double> maxStats = player.getStatistics().maxStats() ;
		Point titlesPos = Util.translate(topLeftPos, size.width / 2, 35 + 12 + 6 - 2) ;
		Point subTitlesPos = Util.translate(topLeftPos, 21 + 48, 195 + 25 + 6) ;
		Point topLeft1 = Util.translate(topLeftPos, 35 + 16, 35 + 31 + 10) ;
		Point topLeft2 = Util.translate(topLeftPos, 32 + 6, 193 + 12) ;
		Point topLeft3 = Util.translate(topLeftPos, 32 + 16, 195 + 25 + 27) ;
		
		// Titles
		GamePanel.getDP().drawText(titlesPos, Align.bottomCenter, title, FONT_STD, textColor) ;

		// subtitles
		subTitles.forEach(sub -> {
			GamePanel.getDP().drawText(subTitlesPos, Align.center, sub, FONT_STD, textColor) ;
			subTitlesPos.x += 96 ;
		}) ;
		
		// number stats
		int i = 0 ;
		for (String key : numberStats.keySet())
		{
			String text = String.valueOf(numberStats.get(key)) ;
			Point textPos = Util.translate(topLeft1, (i / 6) * 140, (i % 6) * 18) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, text, FONT_STD, textColor) ;
			i += 1 ;
		}
		
		// damage stats
		i = 0 ;
		for (String key : damageStats.keySet())
		{
			String text = String.valueOf(Util.round((double) damageStats.get(key), 1)) ;
			Point textPos = Util.translate(topLeft3, (i % 3) * 96, (i / 3) * 18) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, text, FONT_STD, textColor) ;
			i += 1 ;
		}
		
		// max stats
		i = 0 ;
		for (String key : maxStats.keySet())
		{
			String text = "dano " + key + " máx: " + String.valueOf(Util.round((double) maxStats.get(key), 1)) ;
			Point textPos = Util.translate(topLeft2, i * 126, 0) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, text, FONT_STD, textColor) ;
			i += 1 ;
		}
		
	}

	public void display(Point mousePos)
	{		
        Image windowImage = TAB_IMAGES.get(tab) ;
		
		// Main window
		GamePanel.getDP().drawImage(windowImage, topLeftPos, Align.topLeft) ;
        
        // tab names
		String[] tabsText = Game.getAllText().get(TextCategories.playerWindow) ;
		for (int i = 0 ; i <= tabsText.length - 1 ; i += 1)
		{
			GamePanel.getDP().drawText(Util.translate(tabsTextPos, 0, 90 * i), Align.center, 90, tabsText[i], TITLE_FONT, Palette.colors[0]) ;
		}
			
		switch (tab)
		{
			case 0: displayTab0(mousePos) ; break ;
			case 1: displayTab1(player) ; break ;
			case 2: displayTab2(player) ; break ;
		}		

		GamePanel.getDP().drawText(playerNamePos, Align.center, player.getName(), TITLE_FONT, Palette.colors[0]) ;		
	}	
}
