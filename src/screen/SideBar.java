package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.GameButton;
import components.IconFunction;
import components.QuestSkills;
import graphics.Draw;
import graphics.DrawPrimitives;
import liveBeings.HotKeysBar;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import liveBeings.SpellsBar;
import main.Game;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;
import windows.PetAttributesWindow;
import windows.PlayerAttributesWindow;

public abstract class SideBar
{
	
	private static final Point barPos = Game.getScreen().pos(1, 0) ;
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 10) ;
	public static final Dimension size = new Dimension(40, Game.getScreen().getSize().height) ;
	public static final Image slotImage = UtilS.loadImage("\\SideBar\\" + "Slot.png") ;
	private static final List<GameButton> buttons = new ArrayList<>() ;
	
	static
	{
		String[] iconNames = new String[] {"map", "quest", "bag", "settings"} ;
		IconFunction[] actions = new IconFunction[iconNames.length] ;
		
		Player player = Game.getPlayer() ;
		Image playerImage = player.getMovingAni().idleGif ;
		IconFunction playerAction = () -> {
			((PlayerAttributesWindow) player.getAttWindow()).setPlayer(player) ;
			((PlayerAttributesWindow) player.getAttWindow()).updateAttIncButtons(player) ;
			player.switchOpenClose(player.getAttWindow()) ;
		} ;
		actions[0] = () -> {
			if (!player.getQuestSkills().get(QuestSkills.getContinentMap(player.getMap().getContinent().name()))) { return ;}
			player.getMapWindow().setPlayerPos(player.getPos()) ;
			player.getMapWindow().setCurrentMap(player.getMap()) ;
			player.switchOpenClose(player.getMapWindow()) ;
		} ; 
		actions[1] = () -> { 
			player.getQuestWindow().setQuests(player.getQuests()) ;
			player.getQuestWindow().setBag(player.getBag()) ;
			player.switchOpenClose(player.getQuestWindow()) ;
		} ; 
		actions[2] = () -> { player.switchOpenClose(player.getBag()) ;} ; 
		actions[3] = () -> { player.switchOpenClose(player.getSettings()) ;} ;

		
		setPet(player, Game.getPet()) ;

		Point iconPos = UtilG.Translate(barPos, 20, 50) ;
		buttons.add(new GameButton(iconPos, Align.topCenter, playerImage, playerImage, playerAction)) ;
		iconPos.y += playerImage.getHeight(null) + 10 ;
		for (int i = 0; i <= iconNames.length - 1 ; i += 1)
		{
			Image iconImg = UtilS.loadImage("\\SideBar\\Icon" + "_" + iconNames[i] + ".png");
			Image iconSelectedImg = UtilS.loadImage("\\SideBar\\Icon" + "_" + iconNames[i] + "Selected.png") ;
			buttons.add(new GameButton(iconPos, Align.topCenter, iconNames[i], iconImg, iconSelectedImg, actions[i])) ;

			iconPos.y += iconImg.getHeight(null) + 10 ;
		}
		
		buttons.forEach(GameButton::activate);
	}
	
	public static void initialize()
	{
		SpellsBar.updateSpells(Game.getPlayer().getActiveSpells()) ;
	}
	
	public static void setPet(Player player, Pet pet)
	{
		if (pet == null) { return ;}
		
		Image petImage = pet.getMovingAnimations().idleGif ;
		IconFunction petAction = () -> {
			((PetAttributesWindow) pet.getAttWindow()).setPet(pet) ;
			player.switchOpenClose(pet.getAttWindow()) ;
		} ;
		buttons.add(new GameButton(UtilG.Translate(barPos, 20, 10), Align.topCenter, petImage, petImage, petAction)) ;
	}
		
	public static List<GameButton> getButtons() { return buttons ;}
		
	public static void act(String action, Point mousePos)
	{
		if (action == null) { return ;}
		
		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			if (!button.isClicked(mousePos, action)) { continue ;}
			
			button.act() ;
		}
	}
	
	private static void displayKeys(DrawPrimitives DP)
	{
		String[] keys = new String[] {null, PlayerActions.map.getKey(), PlayerActions.quest.getKey(), PlayerActions.bag.getKey(), null, null} ;
		Dimension textSize = new Dimension(12, 12) ;
		Color textColor = Game.colorPalette[0] ;
		int i = 0 ;
		for (GameButton button : buttons)
		{

			if (keys[i] == null) { i += 1 ; continue ;}
			Point rectCenter = UtilG.Translate(button.getTopLeftPos(), 5, 0) ;
			DP.drawRoundRect(rectCenter, Align.center, textSize, 1, Game.colorPalette[3], true, 2, 2) ;
			DP.drawText(rectCenter, Align.center, Draw.stdAngle, keys[i], font, textColor) ;
			i += 1 ;
		}
	}
	
	public static void display(Player player, Pet pet, Point mousePos, DrawPrimitives DP)
	{
		
		DP.drawRect(barPos, Align.topLeft, size, 1, Game.colorPalette[0], null) ;
		
		buttons.forEach(button -> button.display(Draw.stdAngle, false, mousePos, DP)) ;
		displayKeys(DP) ;
		
		SpellsBar.display(player.getMp().getCurrentValue(), mousePos, DP);
		HotKeysBar.display(player.getHotItems(), mousePos, DP) ;

	}
	
	
}
