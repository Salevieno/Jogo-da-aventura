package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import components.GameButton;
import components.IconFunction;
import graphics.DrawingOnPanel;
import liveBeings.HotKeysBar;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.SpellsBar;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class SideBar
{
	private Set<GameButton> buttons ;
	
	private Point barPos = new Point(Game.getScreen().getSize().width, Game.getScreen().getSize().height);	
	public static final Dimension size = new Dimension(40, Game.getScreen().getSize().height) ;
		
	public SideBar(Image playerImage, Image petImage)
	{
		
		Dimension screenSize = Game.getScreen().getSize() ;
		Point topLeftPos = new Point(screenSize.width + 20, screenSize.height - 220) ;
		buttons = new HashSet<>() ;
		String[] names = new String[] {"settings", "bag", "quest", "map", "book", "tent"} ; // "player", "pet"
		IconFunction[] actions = new IconFunction[names.length] ;
		actions[0] = () -> { } ; 
		actions[1] = () -> { } ; 
		actions[2] = () -> { } ; 
		actions[3] = () -> { } ; 
		actions[4] = () -> { } ; 
		actions[5] = () -> { } ; 
		
    	String path = Game.ImagesPath + "\\SideBar\\";
    	int offsetY = 0 ;
		for (int i = 0; i <= names.length - 1 ; i += 1)
		{			
			Image image = UtilG.loadImage(path + "Icon" + i + "_" + names[i] + ".png") ;
			Image selImage = UtilG.loadImage(path + "Icon" + i + "_" + names[i] + "Selected.png") ;
			Point botCenterPos = UtilG.Translate(topLeftPos, -20, - offsetY) ;
			buttons.add(new GameButton(i, names[i], botCenterPos, "", image, selImage)) ;
//			buttons.add(new GameButton(botCenterPos, Align.center, image, selImage, actions[i])) ;

			offsetY += image.getHeight(null) + 10 ;
		}
		
		buttons.forEach(GameButton::activate);
		
	}
	
//	public void aa()
//	{
//		case "settings": switchOpenClose(settings) ; return ;
//		case "bag": switchOpenClose(bag) ; return ;
//		case "quest": 
//			questWindow.setQuests(quests) ;
//			questWindow.setBag(bag) ;
//			switchOpenClose(questWindow) ; return ;
//			
//		case "map":
//			if (!questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name()))) { return ;}
//			mapWindow.setPlayerPos(pos) ;
//			mapWindow.setCurrentMap(map) ;
//			switchOpenClose(mapWindow) ; return ;
//			
//		case "book": fabWindow.setRecipes(knownRecipes) ; switchOpenClose(fabWindow) ; return ;
//		case "player":
//			((PlayerAttributesWindow) attWindow).setPlayer(this) ;
//			((PlayerAttributesWindow) attWindow).updateAttIncButtons(this) ;
//			switchOpenClose(attWindow) ;
//			return ;
//			
//		case "pet":
//			if (pet == null) { return ;}
//			((PetAttributesWindow) pet.getAttWindow()).setPet(pet) ;
//			switchOpenClose(pet.getAttWindow()) ; return ;
//	}
//	
	public Set<GameButton> getButtons() { return buttons ;}
	public void setButtons(Set<GameButton> buttons) { this.buttons = buttons ;}
		
	public void display(Player player, Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		
		double stdAngle = DrawingOnPanel.stdAngle ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] IconKey = new String[] {null, Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7], null, null, null, null} ;
		Color textColor = Game.colorPalette[7] ;
		
		DP.DrawRect(barPos, Align.bottomLeft, size, 1, Game.colorPalette[9], null) ;
		
		buttons.forEach(button -> button.display(stdAngle, false, mousePos, DP)) ;
		buttons.forEach(button -> DP.DrawText(button.getTopLeftPos(), Align.topLeft, stdAngle, IconKey[button.getid()] != null ? IconKey[button.getid()] : "", font, textColor)) ;
		
		SpellsBar.display(player.getMp().getCurrentValue(), player.getActiveSpells(), mousePos, DP);
		HotKeysBar.display(player.getHotItems(), mousePos, DP) ;

	}
	
	
}
