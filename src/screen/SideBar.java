package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import UI.ButtonFunction;
import UI.GameButton;
import components.QuestSkills;
import graphics.Align;
import graphics2.Draw;
import liveBeings.HotKeysBar;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import liveBeings.SpellsBar;
import main.Game;
import main.GamePanel;
import main.MainGame3_4;
import utilities.Util;
import utilities.UtilS;
import windows.PetAttributesWindow;
import windows.PlayerAttributesWindow;

public abstract class SideBar
{
	
	private static final Point barPos = Game.getScreen().posInMap(1, 0) ;
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 10) ;
	private static final String[] iconNames ;
	private static final Image[] iconImages ;
	private static final Image[] iconSelectedImages ;
	private static final List<GameButton> buttons = new ArrayList<>() ;
	private static final Color bgColor = Game.palette[1] ;
	
	private static final Dimension size = new Dimension(60, Game.getScreen().getSize().height) ;
	public static final Image slotImage = UtilS.loadImage("\\SideBar\\" + "Slot.png") ;
	public static final int sy = 10 ;
	
	static
	{
		iconNames = new String[] {"map", "quest", "bag", "settings", "exit"} ;
		iconImages = new Image[iconNames.length] ;
		iconSelectedImages = new Image[iconNames.length] ;
		for (int i = 0; i <= iconNames.length - 1 ; i += 1)
		{
			iconImages[i] = UtilS.loadImage("\\SideBar\\Icon" + "_" + iconNames[i] + ".png");
			iconSelectedImages[i] = UtilS.loadImage("\\SideBar\\Icon" + "_" + iconNames[i] + "Selected.png") ;
		}
	}
	
	public static void initialize()
	{
		Player player = Game.getPlayer() ;
		Image playerImage = player.getMovingAni().idleGif ;
		ButtonFunction playerAction = () -> {
			((PlayerAttributesWindow) player.getAttWindow()).setPlayer(player) ;
			((PlayerAttributesWindow) player.getAttWindow()).updateAttIncButtons(player) ;
			player.switchOpenClose(player.getAttWindow()) ;
		} ;
		ButtonFunction[] actions = new ButtonFunction[iconNames.length] ;
		actions[0] = () -> {
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
		actions[4] = () -> { MainGame3_4.closeGame() ;} ;

		SpellsBar.updateSpells(player.getActiveSpells()) ;
		addPetButton(player, Game.getPet()) ;
		

		Point iconPos = Util.Translate(barPos, size.width / 2, 45) ;
		buttons.add(new GameButton(iconPos, Align.topCenter, playerImage, playerImage, playerAction)) ;
		iconPos.y += playerImage.getHeight(null) + 10 ;
		for (int i = 0; i <= iconNames.length - 1 ; i += 1)
		{
			buttons.add(new GameButton(iconPos, Align.topCenter, iconNames[i], iconImages[i], iconSelectedImages[i], actions[i])) ;

			iconPos.y += iconImages[i].getHeight(null) + 10 ;
		}
		
		buttons.forEach(GameButton::activate) ;
		if (!player.getQuestSkills().get(QuestSkills.getContinentMap(player.getMap().getContinent().name())))
		{
			buttons.get(1).deactivate() ;
		}
	}
	
	public static void addPetButton(Player player, Pet pet)
	{
		if (pet == null) { return ;}
		
		Image petImage = pet.getMovingAnimations().idleGif ;
		ButtonFunction petAction = () -> {
			((PetAttributesWindow) pet.getAttWindow()).setPet(pet) ;
			player.switchOpenClose(pet.getAttWindow()) ;
		} ;
		buttons.add(new GameButton(Util.Translate(barPos, size.width / 2, 10), Align.topCenter, petImage, petImage, petAction)) ;
	}
	
	private static void displayKeys()
	{
		String[] keys = new String[] {PlayerActions.attWindow.getKey(), PlayerActions.map.getKey(), PlayerActions.quest.getKey(), PlayerActions.bag.getKey(), null, null, null} ;
		Dimension textSize = new Dimension(12, 12) ;
		Color textColor = Game.palette[0] ;
		int i = 0 ;
		for (GameButton button : buttons)
		{

			if (keys[i] == null | !button.isActive()) { i += 1 ; continue ;}

			Point rectCenter = Util.Translate(button.getTopLeftPos(), 5, 0) ;
			GamePanel.DP.drawRoundRect(rectCenter, Align.center, textSize, 1, Game.palette[3], Game.palette[0], true, 2, 2) ;
			GamePanel.DP.drawText(rectCenter, Align.center, Draw.stdAngle, keys[i], font, textColor) ;
			i += 1 ;
		}
	}
	
	public static void display(Player player, Pet pet, Point mousePos)
	{
		
		GamePanel.DP.drawRect(barPos, Align.topLeft, size, bgColor, null) ;
//		GamePanel.DP.drawLine(Util.Translate(barPos, size.width, 0), Util.Translate(barPos, size.width, size.height), Game.palette[1]) ;
		
		buttons.forEach(button -> button.display(Draw.stdAngle, false, mousePos)) ;
		displayKeys() ;
		
		SpellsBar.display(player.getMp().getCurrentValue(), mousePos);
		HotKeysBar.display(player.getHotItems(), mousePos) ;

	}
	
	
}
