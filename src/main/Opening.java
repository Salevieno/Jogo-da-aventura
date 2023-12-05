package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import components.GameButton;
import components.IconFunction;
import graphics.DrawingOnPanel;
import graphics.Gif;
import liveBeings.Player;
import screen.Screen;
import utilities.Align;
import utilities.LiveInput;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public abstract class Opening
{
	private static Image backgroundImage;
	private static Gif openingGif ;
    private static List<GameButton> buttons ;
    private static List<GameButton> loadSlotButtons ;
    private static String[] stepMessage ;
    private static String[] jobInfo ;
    private static int step ;
    private static boolean newGame ;
    private static boolean isOver ;
    
    private static String chosenName ;
    private static int difficultLevel ;
    private static String chosenSex ;
    private static int chosenJob ;
	private static LiveInput liveInput ;
	
	private static Player[] players ;

    private static final Font font ;
    private static final Font smallFont ;
	private static final Image LoadingGif ;
	private static final Image LoadingSlot ;
	private static final Image LoadingSlotSelected ;
	public static final Clip thunderSound ;
	public static final Clip introMusic ;
	
	static
	{
		font = new Font(Game.MainFontName, Font.BOLD, 14) ;
	    smallFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
		String path = Game.ImagesPath  + "\\Opening\\";
		backgroundImage = UtilG.loadImage(path + "Opening.png") ;
		openingGif = new Gif(UtilG.loadImage(path + "Opening.gif"), 230, false, true) ;
		LoadingGif = UtilG.loadImage(path + "Loading.gif") ;
		LoadingSlot = UtilG.loadImage(path + "LoadingSlot.png") ;
		LoadingSlotSelected = UtilG.loadImage(path + "LoadingSlotSelected.png") ;

		thunderSound = Music.loadMusicFile("0-Thunder.wav") ;
		introMusic = Music.loadMusicFile("1-Intro.wav") ;
		
		GameButton.selectedIconID = 2 ;
    	step = 0 ;
    	newGame = true ;
    	isOver = false ;
		liveInput = new LiveInput() ;
    	buttons = new ArrayList<>() ;
    	loadSlotButtons = new ArrayList<>() ;
    	
    	players = new Player[3] ;

		IconFunction portAction = () -> { } ;
		IconFunction enAction = () -> { } ;
		IconFunction newGameAction = () -> {} ;
		IconFunction loadSlot1 = () -> { Game.setPlayer(players[0]) ; isOver = true ;} ;
		IconFunction loadSlot2 = () -> { Game.setPlayer(players[1]) ; isOver = true ;} ;
		IconFunction loadSlot3 = () -> { Game.setPlayer(players[2]) ; isOver = true ;} ;
		IconFunction loadGameAction = () -> {
			newGame = false ;
			players[0] = Player.load(1) ;
			players[1] = Player.load(2) ;
			players[2] = Player.load(3) ;
	
	    	loadSlotButtons.add(new GameButton(new Point(60, 100), Align.topLeft, "Load slot 1", LoadingSlot, LoadingSlotSelected, loadSlot1)) ;
	    	loadSlotButtons.add(new GameButton(new Point(260, 100), Align.topLeft, "Load slot 2", LoadingSlot, LoadingSlotSelected, loadSlot2)) ;
	    	loadSlotButtons.add(new GameButton(new Point(460, 100), Align.topLeft, "Load slot 3", LoadingSlot, LoadingSlotSelected, loadSlot3)) ;
	
			if (players[0] == null) { loadSlotButtons.get(0).deactivate() ;}
			if (players[1] == null) { loadSlotButtons.get(1).deactivate() ;}
			if (players[2] == null) { loadSlotButtons.get(2).deactivate() ;}
			
		} ;
		IconFunction confirmNameAction = () -> {chosenName = liveInput.getText() ;} ;
		IconFunction maleAction = () -> { chosenSex = "M" ;} ;
		IconFunction femaleAction = () -> { chosenSex = "F" ;} ;
		IconFunction easyAction = () -> { difficultLevel = 0 ;} ;
		IconFunction mediumAction = () -> { difficultLevel = 1 ;} ;
		IconFunction hardAction = () -> { difficultLevel = 2 ;} ;
		IconFunction knightAction = () -> { chosenJob = 0 ;} ;
		IconFunction mageAction = () -> { chosenJob = 1 ;} ;
		IconFunction archerAction = () -> { chosenJob = 2 ;} ;
		IconFunction animalAction = () -> { chosenJob = 3 ;} ;
		IconFunction thiefAction = () -> { chosenJob = 4 ;} ;
		
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
				screen.pos(0.51, 0.45), 
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
			if (btImage == null) { btImage = UtilS.loadImage("ButtonGeneral.png") ;}
			if (btImageSelected == null) { btImageSelected = UtilG.loadImage(path + btNames[i] + " Selected.png") ;}
			if (btImageSelected == null) { btImageSelected = UtilS.loadImage("ButtonGeneralSelected.png") ;}
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

	public static Player getChosenPlayer() { return new Player(chosenName, chosenSex, chosenJob) ;}
	public static String getChosenName() { return chosenName ;}
	public static int getChosenDifficultLevel() { return difficultLevel ;}
	public static String getChosenSex() { return chosenSex ;}
	public static int getChosenJob() { return chosenJob ;}
	
	
	public static Gif getOpeningGif() { return openingGif ;}

	private static void act(String action, Point mousePos)
	{
		
		if (action == null) { return ;}

		boolean advanceStep = false ;
		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			if (!button.isClicked(mousePos, action)) { continue ;}
			
			button.act() ;
			if (button.getName().equals("Load Game"))
			{
				return ;
			}
			if (!button.getName().equals("Port") & !button.getName().equals("En"))
			{
				advanceStep = true ;
			}
			break ;
		}
		
		for (GameButton button : loadSlotButtons)
		{
			if (!button.isActive()) { continue ;}
			if (!button.isClicked(mousePos, action)) { continue ;}
			
			button.act() ;
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
		    	isOver = true ;
				return ;
				
			default: return ;
		}
		
	}
	
	private static void displaySlot(Point pos, int slotNumber, DrawingOnPanel DP)
	{
//		DP.DrawImage(LoadingSlot, pos, Align.topLeft) ;
		
		Player player = players[slotNumber] ;
		double angle = DrawingOnPanel.stdAngle ;
		Color textColor = Game.colorPalette[0] ;
		
		Point textPos = UtilG.Translate(pos, 75, 10) ;
		DP.DrawText(textPos, Align.center, angle, "Slot " + (slotNumber + 1), font, textColor) ;

		Point namePos = UtilG.Translate(pos, 75, 30) ;
		DP.DrawText(namePos, Align.center, angle, player.getName(), smallFont, textColor) ;
		
		Point levelPos = UtilG.Translate(pos, 10, 45) ;
		DP.DrawText(levelPos, Align.centerLeft, angle, "Nível: " + player.getLevel(), smallFont, textColor) ;
		
	}
	
	private static void displayLoadingSlot(Player player, Point mousePos, DrawingOnPanel DP)
	{
		for (int i = 0 ; i <= loadSlotButtons.size() - 1 ; i += 1)
		{
			if (!loadSlotButtons.get(i).isActive()) { continue ;}
			
			loadSlotButtons.get(i).display(0, true, mousePos, DP) ;
			displaySlot(new Point(60 + 200 * i, 100), i, DP) ;
		}
	}
	
	public static void displayLoadingScreen(DrawingOnPanel DP)
	{
		DP.DrawGif(LoadingGif, Game.getScreen().getCenter(), Align.center) ;
	}
	
	private static void displayJobInfo(DrawingOnPanel DP)
	{
		Color textColor = Game.colorPalette[0] ;
		Color bgColor = Game.colorPalette[3] ;
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			Point rectPos = Game.getScreen().pos(0.04 + i * 0.2, 0.4) ;
			Point textPos = UtilG.Translate(rectPos, 5, 5) ;
			DP.drawGradRoundRect(rectPos, Align.topLeft, new Dimension(110, 150), 2, bgColor, bgColor, true) ;
			DP.DrawFitText(textPos, 10, Align.topLeft, jobInfo[i], smallFont, 18, textColor) ;
		}
	}
	
	public static void display(String action, Point mousePos, DrawingOnPanel DP)
	{
		Point textPos = Game.getScreen().pos(0.5, 0.3) ;
		Color textColor = Game.colorPalette[0] ;
		
		DP.DrawImage(backgroundImage, new Point(0, 0), 0, Scale.unit, Align.topLeft) ;		
		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			
			button.display(0, true, mousePos, DP) ;
		}
		
		if (step == 1)
		{
			liveInput.displayTypingField(Game.getScreen().pos(0.34, 0.36), false, DP) ;
		}
		if (step == 4)
		{
			displayJobInfo(DP) ;
		}
		if (step == 5)
		{
			displayLoadingScreen(DP) ;
		}
		
		if (stepMessage.length - 1 <= step) { return ;}
		DP.DrawText(textPos, Align.center, DrawingOnPanel.stdAngle, stepMessage[step], font, textColor) ;
	}

	public static void run(Player player, Point mousePos, DrawingOnPanel DP)
	{
		if (openingGif.isStarting())
		{
			Music.PlayMusic(thunderSound) ;
			Music.PlayMusic(introMusic) ;
		}
		if (!openingGif.isDonePlaying())
    	{
			openingGif.play(new Point(0, 0), Align.topLeft, DP);
    		return ;
    	}

		act(player.getCurrentAction(), mousePos) ;
		if (newGame)
		{
			display(player.getCurrentAction(), mousePos, DP) ;
		}
		else
		{
			displayLoadingSlot(player, mousePos, DP) ;
		}
		player.resetAction() ;
	}
	
	public static boolean newGame() { return newGame ;}
	public static boolean isOver() { return isOver ;}
	
}
