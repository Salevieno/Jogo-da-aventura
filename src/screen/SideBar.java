package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import components.GameButton;
import components.IconFunction;
import components.QuestSkills;
import graphics.DrawingOnPanel;
import liveBeings.HotKeysBar;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import liveBeings.SpellsBar;
import main.Game;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;

public class SideBar
{
	private Set<GameButton> buttons ;
	private List<Spell> spells ;
	
	private static final Point barPos = Game.getScreen().pos(1, 1) ;
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 10) ;
	public static final Dimension size = new Dimension(40, Game.getScreen().getSize().height) ;
		
	public SideBar(Player player, Image playerImage, Image petImage)
	{
		
		String[] names = new String[] {"settings", "bag", "quest", "map", "book", "tent"} ; // "player", "pet"
		IconFunction[] actions = new IconFunction[names.length] ;
		actions[0] = () -> { player.switchOpenClose(player.getSettings()) ;} ; 
		actions[1] = () -> { player.switchOpenClose(player.getBag()) ;} ; 
		actions[2] = () -> { 
			player.getQuestWindow().setQuests(player.getQuests()) ;
			player.getQuestWindow().setBag(player.getBag()) ;
			player.switchOpenClose(player.getQuestWindow()) ;
		} ; 
		actions[3] = () -> {
			if (!player.getQuestSkills().get(QuestSkills.getContinentMap(player.getMap().getContinentName(player).name()))) { return ;}
			player.getMapWindow().setPlayerPos(player.getPos()) ;
			player.getMapWindow().setCurrentMap(player.getMap()) ;
			player.switchOpenClose(player.getMapWindow()) ;
		} ; 
		actions[4] = () -> { 
			player.getFabWindow().setRecipes(player.getKnownRecipes()) ;
			player.switchOpenClose(player.getFabWindow()) ;
		} ; 
		actions[5] = () -> { System.out.println("opening tent") ;} ;
		spells = player.getActiveSpells() ;
		SpellsBar.updateSpells(spells) ;

//	case "player":
//		((PlayerAttributesWindow) attWindow).setPlayer(this) ;
//		((PlayerAttributesWindow) attWindow).updateAttIncButtons(this) ;
//		switchOpenClose(attWindow) ;
//		return ;
//		
//	case "pet":
//		if (pet == null) { return ;}
//		((PetAttributesWindow) pet.getAttWindow()).setPet(pet) ;
//		switchOpenClose(pet.getAttWindow()) ; return ;

    	int offsetY = 0 ;
		buttons = new HashSet<>() ;
		for (int i = 0; i <= names.length - 1 ; i += 1)
		{			
			Image image = UtilS.loadImage("\\SideBar\\Icon" + i + "_" + names[i] + ".png") ;
			Image selImage = UtilS.loadImage("\\SideBar\\Icon" + i + "_" + names[i] + "Selected.png") ;
			Point botCenterPos = UtilG.Translate(barPos, 20, -220 - offsetY) ;
			buttons.add(new GameButton(botCenterPos, Align.bottomCenter, image, selImage, actions[i])) ;

			offsetY += image.getHeight(null) + 10 ;
		}
		
		buttons.forEach(GameButton::activate);
		
	}
	
	public Set<GameButton> getButtons() { return buttons ;}
		
	public void act(String action, Point mousePos)
	{
		if (action == null) { return ;}
		
		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			if (!button.isClicked(mousePos, action)) { continue ;}
			
			button.act() ;
		}
	}
	
	public void display(Player player, Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		
		double stdAngle = DrawingOnPanel.stdAngle ;
		String[] keys = new String[] {null, Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7], null, null, null, null} ;
		Color textColor = Game.colorPalette[0] ;
		
		DP.DrawRect(barPos, Align.bottomLeft, size, 1, Game.colorPalette[0], null) ;
		
		buttons.forEach(button -> button.display(stdAngle, false, mousePos, DP)) ;
		
		Dimension textSize = new Dimension(12, 12) ;
		int i = 0 ;
		for (GameButton button : buttons)
		{

			if (keys[i] == null) { i += 1 ; continue ;}
			Point rectCenter = UtilG.Translate(button.getTopLeftPos(), 5, 0) ;
			DP.drawRoundRect(rectCenter, Align.center, textSize, 1, Game.colorPalette[3], true, 2, 2) ;
			DP.DrawText(rectCenter, Align.center, stdAngle, keys[i], font, textColor) ;
			i += 1 ;
		}
		
		SpellsBar.display(player.getMp().getCurrentValue(), spells, mousePos, DP);
		HotKeysBar.display(player.getHotItems(), mousePos, DP) ;

	}
	
	
}
