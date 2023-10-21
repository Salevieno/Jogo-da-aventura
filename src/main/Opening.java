package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.GameButton;
import components.IconFunction;
import graphics.DrawingOnPanel;
import graphics.Gif;
import liveBeings.Player;
import screen.Screen;
import utilities.Align;
import utilities.GameStates;
import utilities.LiveInput;
import utilities.Scale;
import utilities.UtilG;

public abstract class Opening
{
	private static Image backgroundImage;
	private static Gif openingGif ;
    private static List<GameButton> buttons ;
    private static String[] stepMessage ;
    private static String[] jobInfo ;
    private static int step ;
    private static String[] playerInfo ;	// Chosen name, difficult level, sex and class
	private static LiveInput liveInput ;

    private static final Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
    private static final Font smallFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	
	static
	{
		String path = Game.ImagesPath  + "\\Opening\\";
		backgroundImage = UtilG.loadImage(path + "Opening.png") ;
		openingGif = new Gif(UtilG.loadImage(path + "Opening.gif"), 200, false, true) ;
		GameButton.selectedIconID = 2 ;
    	step = 0 ;
		liveInput = new LiveInput() ;
    	playerInfo = new String[4] ;

    	
    	buttons = new ArrayList<>() ;

		IconFunction portAction = () -> { } ;
		IconFunction enAction = () -> { } ;
		IconFunction newGameAction = () -> {} ;
		IconFunction loadGameAction = () -> {} ;
		IconFunction confirmNameAction = () -> {playerInfo[0] = liveInput.getText() ; System.out.println(playerInfo[0]);} ;
		IconFunction maleAction = () -> { playerInfo[1] = "M" ;} ;
		IconFunction femaleAction = () -> { playerInfo[1] = "F" ;} ;
		IconFunction easyAction = () -> { playerInfo[2] = "0" ;} ;
		IconFunction mediumAction = () -> { playerInfo[2] = "1" ;} ;
		IconFunction hardAction = () -> { playerInfo[2] = "2" ;} ;
		IconFunction knightAction = () -> { playerInfo[3] = "0" ;} ;
		IconFunction mageAction = () -> { playerInfo[3] = "1" ;} ;
		IconFunction archerAction = () -> { playerInfo[3] = "2" ;} ;
		IconFunction animalAction = () -> { playerInfo[3] = "3" ;} ;
		IconFunction thiefAction = () -> { playerInfo[3] = "4" ;} ;
		
		Screen screen = Game.getScreen() ;
		String[] btNames = new String[] {
				"Port", "En",
				"New Game", "Load Game",
				"Confirm name",
				"Male", "Female",
				"Easy", "Medium", "Hard",
				"Knight", "Mage", "Archer", "Animal", "Thief"} ;
		Point[] btPos = new Point[] {
				screen.pos(0.85, 0.05), screen.pos(0.95, 0.05),
				screen.pos(0.45, 0.3), screen.pos(0.65, 0.3),
				screen.pos(0.48, 0.45), 
				screen.pos(0.4, 0.3), screen.pos(0.6, 0.3),
				screen.pos(0.3, 0.3), screen.pos(0.5, 0.3), screen.pos(0.7, 0.3),
				screen.pos(0.13, 0.3), screen.pos(0.33, 0.3), screen.pos(0.53, 0.3), screen.pos(0.73, 0.3), screen.pos(0.93, 0.3)} ;
		IconFunction[] btAction = new IconFunction[] {
				portAction, enAction,
				newGameAction, loadGameAction,
				confirmNameAction,
				maleAction, femaleAction,
				easyAction, mediumAction, hardAction,
				knightAction, mageAction, archerAction, animalAction, thiefAction} ;
		for (int i = 0 ; i <= btNames.length - 1; i += 1)
		{
			Image btImage = UtilG.loadImage(path + btNames[i] + ".png") ;
			Image btImageSelected = UtilG.loadImage(path + btNames[i] + " Selected.gif") ;
			if (btImage == null) { btImage = UtilG.loadImage(Game.ImagesPath + "ButtonGeneral.png") ;}
			if (btImageSelected == null) { btImageSelected = UtilG.loadImage(path + btNames[i] + " Selected.png") ;}
			if (btImageSelected == null) { btImageSelected = UtilG.loadImage(Game.ImagesPath + "ButtonGeneralSelected.png") ;}
			buttons.add(new GameButton(btPos[i], Align.center, btNames[i], btImage, btImageSelected, btAction[i])) ;
			buttons.get(buttons.size() - 1).deactivate() ;
		
		}
		
    	for (GameButton button : buttons)
    	{
     		GameButton.addToAllIconsList(button) ;
    	}
    	buttons.get(0).activate() ;
    	buttons.get(1).activate() ;
    	buttons.get(2).activate() ;
    	buttons.get(3).activate() ;

    	// TODO get text from json. AllText is not loaded here yet Game.allText.get(TextCategories.newGame)
    	stepMessage = new String[] {"", "Qual o seu nome?", "", "", "", ""} ;
    	jobInfo = new String[]
		{
			"Cavaleiros são poderosos guerreiros corpo-a-corpo. Eles tem grande ataque, poder e vitalidade e são os guerreiros mais fortes do reino.",
		    "Magos tem o maior poder mágico. Eles controlam os elementos e podem usar poderes sobrenaturais para manipular a magia e a vida.",
		    "Arqueiros são guerreiros especializados em luta à distância. Eles usam poder físico combinado com o controle dos elementos.",
		    "Animais vivem em harmonia com a natureza e podem usufruir dos seus poderes. Eles tem grande poder sobre a vida e incrível agilidade.",
		    "Ladrões são os mais ágeis em todo o reino. Eles atacam cruelmente qualquer inimigo que cruze o seu caminho buscando poder e riqueza."	
		};

	}

	public static Gif getOpeningGif() { return openingGif ;}

	public static void act(String action, Point mousePos)
	{
		
		if (action == null) { return ;}

		boolean advanceStep = false ;
		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			if (!button.isClicked(mousePos, action)) { continue ;}
			
			button.act() ;
			if (!button.getName().equals("Port") & !button.getName().equals("En"))
			{
				advanceStep = true ;
			}
			break ;
		}
		
		if (step == 1)
		{
			liveInput.receiveInput(action) ;
		}
		
		if (!advanceStep) { return ;}
		
		step += 1 ;
		switch(step)
		{
			case 1:
				buttons.get(2).deactivate() ;
		    	buttons.get(3).deactivate() ;
		    	buttons.get(4).activate() ;
				return ;
				
			case 2:
		    	buttons.get(4).deactivate() ;
		    	buttons.get(5).activate() ;
		    	buttons.get(6).activate() ;
				return ;
				
			case 3:
				buttons.get(5).deactivate() ;
		    	buttons.get(6).deactivate() ;
		    	buttons.get(7).activate() ;
		    	buttons.get(8).activate() ;
		    	buttons.get(9).activate() ;
				return ;
				
			case 4:
		    	buttons.get(7).deactivate() ;
		    	buttons.get(8).deactivate() ;
		    	buttons.get(9).deactivate() ;
		    	buttons.get(10).activate() ;
		    	buttons.get(11).activate() ;
		    	buttons.get(12).activate() ;
		    	buttons.get(13).activate() ;
		    	buttons.get(14).activate() ;
				return ;
				
			case 5:
				buttons.get(10).deactivate() ;
		    	buttons.get(11).deactivate() ;
		    	buttons.get(12).deactivate() ;
		    	buttons.get(13).deactivate() ;
		    	buttons.get(14).deactivate() ;
				return ;				
				
			default: return ;
		}
		
	}

	public static void displayJobInfo(DrawingOnPanel DP)
	{
		Color textColor = Game.colorPalette[0] ;
		Color bgColor = Game.colorPalette[3] ;
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			Point rectPos = Game.getScreen().pos(0.04 + i * 0.2, 0.4) ;
			Point textPos = UtilG.Translate(rectPos, 5, 5) ;
			DP.DrawRoundRect(rectPos, Align.topLeft, new Dimension(110, 150), 2, bgColor, bgColor, true) ;
			DP.DrawFitText(textPos, 10, Align.topLeft, jobInfo[i], smallFont, 18, textColor) ;
		}
	}
	
	public static void display(String action, Point mousePos, DrawingOnPanel DP)
	{
		Point textPos = Game.getScreen().pos(0.5, 0.3) ;
		Color textColor = Game.colorPalette[5] ;
		
		DP.DrawImage(backgroundImage, new Point(0, 0), 0, new Scale(1, 1), Align.topLeft) ;		
		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			
			button.display(0, true, mousePos, DP) ;
		}
		
		if (step == 1)
		{
			DP.DrawText(Game.getScreen().pos(0.3, 0.4), Align.topLeft, DrawingOnPanel.stdAngle, liveInput.getText(), font, textColor) ;
		}
		if (step == 4)
		{
			displayJobInfo(DP) ;
		}
		
		if (stepMessage.length - 1 <= step) { return ;}
		DP.DrawText(textPos, Align.center, DrawingOnPanel.stdAngle, stepMessage[step], font, textColor) ;
	}

	public static void run(Player player, Point mousePos, DrawingOnPanel DP)
	{
		if (!Opening.getOpeningGif().isDonePlaying())
    	{
			openingGif.play(new Point(0, 0), Align.topLeft, DP);
    		return ;
    	}

		Music.PlayMusic(Music.intro) ;
		display(player.getCurrentAction(), mousePos, DP) ;
		act(player.getCurrentAction(), mousePos) ;
		player.resetAction() ;
	}
	
	public static String[] getPlayerInfo() { return playerInfo ;}

	public static boolean isOver() { return (step == 5) ;}
	
}
