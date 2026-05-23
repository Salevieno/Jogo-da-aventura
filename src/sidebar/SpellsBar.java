package sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import liveBeings.LiveBeing;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import spells.Spell;
import utilities.Util;


public abstract class SpellsBar
{   
    private static final int MAX_NUMBER_ROWS ;
    private static final Font LARGE_FONT ;
    private static final Color TEXT_COLOR ;

    private static final Point BAR_POS ;
    private static final Image BAR_IMAGE ;
    private static final Dimension BAR_SIZE ;
    private static final Image SLOT_IMAGE_NO_MP ;
    private static final Dimension SLOT_SIZE ;
    private static final Point SLOTS_OFFSET ;
    private static final Image COOLDOWN_IMAGE ;
	
	private static List<Spell> spells ;
	private static int nRows ;
	private static int nCols ;
	
	static
	{
		MAX_NUMBER_ROWS = 8 ;
		LARGE_FONT = new Font("SansSerif", Font.BOLD, 14) ;
		TEXT_COLOR = Palette.colors[4] ;

		BAR_POS = new Point(Screen.getMe().mapSize().width + 2, HotKeysBar.topLeft().y - SideBar.SY) ;
		BAR_IMAGE = ImageLoader.loadImage(Path.SIDEBAR_IMG + "SpellsBar.png") ;
		BAR_SIZE = Util.getSize(BAR_IMAGE) ;
		SLOT_IMAGE_NO_MP = ImageLoader.loadImage(Path.SIDEBAR_IMG + "SlotNoMP.png") ;
		SLOT_SIZE = Util.getSize(SideBar.SLOT_IMAGE) ;
		SLOTS_OFFSET = new Point(3, 4) ;
		COOLDOWN_IMAGE = ImageLoader.loadImage(Path.SIDEBAR_IMG + "Cooldown.png") ;
	}
	
	public static void updateSpells(List<Spell> newSpells)
	{
		spells = newSpells ;
		nCols = Util.calcGridNumberColumns(spells.size(), MAX_NUMBER_ROWS) ;
		nRows = MAX_NUMBER_ROWS ;
	}
	
	private static void displayCooldown(Point slotCenter, Spell spell)
	{

		if (spell.getCooldownCounter().hasFinished()) { return ;}
		
		Dimension imgSize = Util.getSize(COOLDOWN_IMAGE) ;
		Scale scale = new Scale(1, 1 - spell.getCooldownCounter().rate()) ;
		Point imgPos = new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2);
		GamePanel.getDP().drawImage(COOLDOWN_IMAGE, imgPos, Draw.stdAngle, scale, Align.bottomLeft, 0.5) ;
	
	}

	private static void displaySpellName(int slotCenterY, String spellName)
	{
		Point textPos = new Point(BAR_POS.x - 5, slotCenterY) ;
		Draw.bufferedText(textPos, Align.centerRight, 0.0, spellName, LARGE_FONT, TEXT_COLOR, Palette.colors[3], 2) ;
	}
	
	public static void display(int userMP, Point mousePos)
	{
		int sx = (int) Util.spacing(BAR_SIZE.width, nCols, SLOT_SIZE.width, SLOTS_OFFSET.x) ;
		int sy = (int) Util.spacing(BAR_SIZE.height, nRows, SLOT_SIZE.height, SLOTS_OFFSET.y) ;

		GamePanel.getDP().drawImage(BAR_IMAGE, BAR_POS, Align.bottomLeft) ;

		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			Spell spell = spells.get(i) ;
			if (spell.getLevel() <= 0) { continue ;}
			
				int row = i / nCols ;
			int col = i % nCols ;
			Point slotCenter = Util.translate(BAR_POS, SLOTS_OFFSET.x + SLOT_SIZE.width / 2 + col * sx, - BAR_SIZE.height + SLOT_SIZE.height / 2 + SLOTS_OFFSET.y + row * sy) ;
			Point slotTopLeft = UtilAlignment.getTopLeft(slotCenter, Align.center, SLOT_SIZE) ;
			Image image = spell.getMpCost() < userMP ? SideBar.SLOT_IMAGE : SLOT_IMAGE_NO_MP ;
			GamePanel.getDP().drawImage(image, slotCenter, Align.center, 0.8) ;
			GamePanel.getDP().drawImage(spell.getImage(), slotCenter, Align.center) ;
			Draw.keyboardKey(slotTopLeft, LiveBeing.getSpellKeys().get(i), Palette.colors[0]);
			
			displayCooldown(slotCenter, spell) ;

			if (!Util.isInside(mousePos, slotTopLeft, SLOT_SIZE)) { continue ;}
			
			displaySpellName(slotCenter.y, spell.getName()) ;		
		}
	}
}