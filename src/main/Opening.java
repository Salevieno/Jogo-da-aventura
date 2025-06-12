package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import components.GameButton;
import components.IconFunction;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import graphics2.Gif;
import liveBeings.Buff;
import liveBeings.Player;
import liveBeings.Spell;
import screen.Screen;
import utilities.GameStates;
import utilities.LiveInput;
import utilities.Log;
import utilities.Util;
import utilities.UtilS;

public abstract class Opening
{
	public static Gif openingGif ;
	private static Image backgroundImage;
    private static List<GameButton> buttons ;
    private static List<List<GameButton>> buttonsInStep ;
    private static List<GameButton> languageButtons ;
    private static List<GameButton> loadSlotButtons ;
	private static Player[] players ;
    private static String[] stepMessage ;
    private static String[] jobDescriptionPtBr ;
    private static String[] jobDescriptionEn ;
    private static int step ;
    private static boolean newGame ;
    private static boolean isOver ;
    
    private static String chosenName ;
    private static int difficultLevel ;
    private static String chosenSex ;
    private static int chosenJob ;
	private static LiveInput liveInput ;
	

    private static final Font font ;
    private static final Font smallFont ;
	// private static final Image LoadingEnfeite ;
	private static final Image LoadingSlot ;
	private static final Image LoadingSlotSelected ;
	private static final Image generalButtonImg ;
	private static final Image generalButtonSelectedImg ;
	// private static final Clip thunderSound ;
	// private static final Clip introMusic ;
	
	
	static
	{
		font = new Font(Game.MainFontName, Font.BOLD, 16) ;
	    smallFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String path = Game.ImagesPath  + "\\Opening\\";
		backgroundImage = Util.loadImage(path + "Opening.png") ;
		openingGif = new Gif("Opening", Util.loadImage(path + "Opening.png"), 0.7, false, true) ;
		// LoadingEnfeite = UtilS.loadImage("\\Opening\\" + "LoadingEnfeite.png") ;
		LoadingSlot = Util.loadImage(path + "LoadingSlot.png") ;
		LoadingSlotSelected = Util.loadImage(path + "LoadingSlotSelected.png") ;
		generalButtonImg = Util.loadImage(path + "generalButton.png") ;
		generalButtonSelectedImg = Util.loadImage(path + "generalButtonSelected.png") ;

		// thunderSound = Music.loadMusicFile("0-Thunder.wav") ;
		// introMusic = Music.loadMusicFile("intro.wav") ;
		
		GameButton.selectedIconID = 2 ;
    	step = 0 ;
    	newGame = true ;
    	isOver = false ;
		liveInput = new LiveInput() ;
    	buttons = new ArrayList<>() ;
		buttonsInStep = new ArrayList<>() ;
    	loadSlotButtons = new ArrayList<>() ;

		IconFunction portAction = () -> { } ; // TODO switch language
		IconFunction enAction = () -> { } ;
		IconFunction newGameAction = () -> {advanceStep() ;} ;
		IconFunction loadGameAction = () -> { switchToLoadGameScreen() ;} ;
		IconFunction confirmNameAction = () -> {chosenName = liveInput.getText() ; advanceStep() ;} ;
		IconFunction maleAction = () -> { chosenSex = "M" ; advanceStep() ;} ;
		IconFunction femaleAction = () -> { chosenSex = "F" ; advanceStep() ;} ;
		IconFunction easyAction = () -> { difficultLevel = 0 ; advanceStep() ;} ;
		IconFunction mediumAction = () -> { difficultLevel = 1 ; advanceStep() ;} ;
		IconFunction hardAction = () -> { difficultLevel = 2 ; advanceStep() ;} ;
		IconFunction knightAction = () -> { chosenJob = 0 ; advanceStep() ;} ;
		IconFunction mageAction = () -> { chosenJob = 1 ; advanceStep() ;} ;
		IconFunction archerAction = () -> { chosenJob = 2 ; advanceStep() ;} ;
		IconFunction animalAction = () -> { chosenJob = 3 ; advanceStep() ;} ;
		IconFunction thiefAction = () -> { chosenJob = 4 ; advanceStep() ;} ;
		
		Screen screen = Game.getScreen() ;
		GameButton portButton = new GameButton(screen.pos(0.85, 0.05), Align.center, "Port", Util.loadImage(path + "Port.png"), Util.loadImage(path + "PortSelected.png"), portAction) ;
		GameButton enButton = new GameButton(screen.pos(0.95, 0.05), Align.center, "En", Util.loadImage(path + "En.png"), Util.loadImage(path + "EnSelected.png"), enAction) ;
		languageButtons = List.of(portButton, enButton) ;

		String[] btNames = new String[] {
				"New Game", "Load Game",
				"Confirm name",
				"Male", "Female",
				"Easy", "Medium", "Hard",
				"Knight", "Mage", "Archer", "Animal", "Thief"} ;
		Point[] btPos = new Point[] {
				screen.pos(0.4, 0.3), screen.pos(0.6, 0.3),
				screen.pos(0.51, 0.45), 
				screen.pos(0.4, 0.3), screen.pos(0.6, 0.3),
				screen.pos(0.3, 0.3), screen.pos(0.5, 0.3), screen.pos(0.7, 0.3),
				screen.pos(0.13, 0.3), screen.pos(0.33, 0.3), screen.pos(0.53, 0.3), screen.pos(0.73, 0.3), screen.pos(0.93, 0.3)} ;
		IconFunction[] btAction = new IconFunction[] {
				newGameAction, loadGameAction,
				confirmNameAction,
				maleAction, femaleAction,
				easyAction, mediumAction, hardAction,
				knightAction, mageAction, archerAction, animalAction, thiefAction} ;
		for (int i = 0 ; i <= btNames.length - 1; i += 1)
		{
			Image btImage = Util.loadImage(path + btNames[i] + ".png") ;
			Image btImageSelected = Util.loadImage(path + btNames[i] + " Selected.gif") ;
			if (btImage == null) { btImage = UtilS.loadImage("ButtonGeneral.png") ;}
			if (btImageSelected == null) { btImageSelected = Util.loadImage(path + btNames[i] + " Selected.png") ;}
			if (btImageSelected == null) { btImageSelected = UtilS.loadImage("ButtonGeneralSelected.png") ;}
			GameButton newButton = new GameButton(btPos[i], Align.center, btNames[i], generalButtonImg, generalButtonSelectedImg, btAction[i]) ;
			newButton.deactivate() ;
			buttons.add(newButton) ;		
		}

		buttonsInStep.add(List.of(buttons.get(0), buttons.get(1))) ;
		buttonsInStep.add(List.of(buttons.get(2))) ;
		buttonsInStep.add(List.of(buttons.get(3), buttons.get(4))) ;
		buttonsInStep.add(List.of(buttons.get(5), buttons.get(6), buttons.get(7))) ;
		buttonsInStep.add(List.of(buttons.get(8), buttons.get(9), buttons.get(10), buttons.get(11), buttons.get(12))) ;

    	buttons.get(0).activateAndSelect() ;	
    	buttons.get(1).activate() ;
    	
    	stepMessage = new String[] {"", "Qual o seu nome?", "", "", "", ""} ;
    	jobDescriptionEn = new String[]
		{
			"Knights are powerful melee warriors. They have great attack, power and vitality and are the strongest warriors in the realm.",
		    "Mages have the greatest magical power. They control the elements and can use supernatural powers to manipulate magic and life.",
		    "Archers are specialized in distance fighting. They use physical power combined with the power of the elements.",
		    "Animals live in harmony with nature and can enjoy its powers. They have great power over life and are incredibly agile.",
		    "Thieves are the fastest in the whole realm. They brutally attack any enemy that crosses their way, looking for power and wealth."	
		};
    	jobDescriptionPtBr = new String[]
		{
			"Cavaleiros são poderosos guerreiros corpo-a-corpo. Eles tem grande ataque, poder e vitalidade e são os guerreiros mais fortes do reino.",
		    "Magos tem o maior poder mágico. Eles controlam os elementos e podem usar poderes sobrenaturais para manipular a magia e a vida.",
		    "Arqueiros são guerreiros especializados em luta à distância. Eles usam poder físico combinado com o poder dos elementos.",
		    "Animais vivem em harmonia com a natureza e podem usufruir dos seus poderes. Eles tem grande poder sobre a vida e incrível agilidade.",
		    "Ladrões são os mais ágeis em todo o reino. Eles atacam cruelmente qualquer inimigo que cruze o seu caminho buscando poder e riqueza."	
		};

	}

	public static Player getChosenPlayer() { return new Player(chosenName, chosenSex, chosenJob) ;}
	public static int getChosenDifficultLevel() { return difficultLevel ;}
	public static Gif getOpeningGif() { return openingGif ;}

	private static void switchToLoadGameScreen()
	{		
		players = new Player[3] ;

		newGame = false ;
		buttons.get(0).deactivate() ;
		buttons.get(1).deactivate() ;
		// TODO
		Buff.loadBuffs() ;
		Buff.loadDebuffs() ;
		Spell.load(Languages.portugues, Buff.allBuffs, Buff.allDebuffs) ;
		players[0] = Player.load(1) ;
		players[1] = Player.load(2) ;
		players[2] = Player.load(3) ;
		
		Log.loadingStatus(players[0] != null, 1);
		Log.loadingStatus(players[1] != null, 2);
		Log.loadingStatus(players[2] != null, 3);

		IconFunction loadSlot1 = () -> { loadGame(players, 0) ;} ;
		IconFunction loadSlot2 = () -> { loadGame(players, 1) ;} ;
		IconFunction loadSlot3 = () -> { loadGame(players, 2) ;} ;
		
		loadSlotButtons.add(new GameButton(new Point(60, 100), Align.topLeft, "Load slot 1", LoadingSlot, LoadingSlotSelected, loadSlot1)) ;
		loadSlotButtons.add(new GameButton(new Point(260, 100), Align.topLeft, "Load slot 2", LoadingSlot, LoadingSlotSelected, loadSlot2)) ;
		loadSlotButtons.add(new GameButton(new Point(460, 100), Align.topLeft, "Load slot 3", LoadingSlot, LoadingSlotSelected, loadSlot3)) ;

		if (players[0] == null) { loadSlotButtons.get(0).deactivate() ;}
		if (players[1] == null) { loadSlotButtons.get(1).deactivate() ;}
		if (players[2] == null) { loadSlotButtons.get(2).deactivate() ;}
	}

	private static void loadGame(Player[] players, int slot)
	{
		Game.setPlayer(players[slot]) ;
		Game.setSaveSlotInUse(slot) ;
		loadSlotButtons.forEach(GameButton::deactivate) ;
		isOver = true ;
	}

	private static void navigate(String action)
	{
		if (action == null) { return ;}

		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_LEFT)) | action.equals("A"))
		{
			selectPreviousButton() ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_RIGHT)) | action.equals("D"))
		{
			selectNextButton() ;
		}
	}

	private static void selectNextButton()
	{
		List<GameButton> screenButtons = buttonsInStep.get(step) ;
		GameButton selectedButton = screenButtons.stream().filter(GameButton::isSelected).findFirst().orElse(null) ;

		if (selectedButton == null) { System.out.println("Warn: no button selected when trying to select next") ; return ;}

		int selectedButtonIndex = screenButtons.indexOf(selectedButton) ;
		int nextButtonIndex = screenButtons.size() == selectedButtonIndex + 1 ? 0 : selectedButtonIndex + 1 ;

		screenButtons.get(selectedButtonIndex).unSelect() ;
		screenButtons.get(nextButtonIndex).select() ;
	}

	private static void selectPreviousButton()
	{
		List<GameButton> screenButtons = buttonsInStep.get(step) ;
		GameButton selectedButton = screenButtons.stream().filter(GameButton::isSelected).findFirst().orElse(null) ;

		if (selectedButton == null) { System.out.println("Warn: no button selected when trying to select previous") ; return ;}

		int selectedButtonIndex = screenButtons.indexOf(selectedButton) ;
		int previousButtonIndex = 0 == selectedButtonIndex ? screenButtons.size() - 1 : selectedButtonIndex - 1 ;

		screenButtons.get(selectedButtonIndex).unSelect() ;
		screenButtons.get(previousButtonIndex).select() ;
	}

	private static void advanceStep()
	{
		if (step == 4)
		{
			buttonsInStep.get(step).forEach(GameButton::deactivate) ;
			step += 1 ;
			isOver = true ;
			return ;
		}

		buttonsInStep.get(step).forEach(GameButton::deactivate) ;
		buttonsInStep.get(step + 1).forEach(GameButton::activate) ;
		buttonsInStep.get(step + 1).get(0).activateAndSelect() ;
		step += 1 ;
	}
	
	private static void displaySlot(Point pos, int slotNumber)
	{
		
		Player player = players[slotNumber] ;
		double angle = Draw.stdAngle ;
		Color textColor = Game.palette[0] ;
		
		Point textPos = Util.Translate(pos, 75, 10) ;
		GamePanel.DP.drawText(textPos, Align.center, angle, "Slot " + (slotNumber + 1), font, textColor) ;

		Point namePos = Util.Translate(pos, 75, 30) ;
		GamePanel.DP.drawText(namePos, Align.center, angle, player.getName(), smallFont, textColor) ;
		
		Point levelPos = Util.Translate(pos, 10, 45) ;
		GamePanel.DP.drawText(levelPos, Align.centerLeft, angle, "Nível: " + player.getLevel(), smallFont, textColor) ;
		
	}
	
	private static void displayLoadingSlot(Player player, Point mousePos)
	{
		for (int i = 0 ; i <= loadSlotButtons.size() - 1 ; i += 1)
		{
			if (!loadSlotButtons.get(i).isActive()) { continue ;}

			loadSlotButtons.get(i).display(0, true, mousePos) ;
			displaySlot(new Point(60 + 200 * i, 100), i) ;
		}
	}
	
	private static void displayJobDescription()
	{
		Color textColor = Game.palette[0] ;
		Color bgColor = Game.palette[3] ;
		String[] description = Game.getLanguage() == Languages.portugues ? jobDescriptionPtBr : jobDescriptionEn ;
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			Point rectPos = Game.getScreen().pos(0.04 + i * 0.2, 0.4) ;
			Point textPos = Util.Translate(rectPos, 5, 5) ;
			GamePanel.DP.drawRoundRect(rectPos, Align.topLeft, new Dimension(110, 150), 2, bgColor, Game.palette[0], true) ;
			Draw.fitText(textPos, 10, Align.topLeft, description[i], smallFont, 18, textColor) ;
		}
	}
	
	public static void display(String action, Point mousePos)
	{
		Point textPos = Game.getScreen().pos(0.5, 0.3) ;
		Color textColor = Game.palette[0] ;
		
		GamePanel.DP.drawImage(backgroundImage, new Point(0, 0), 0, Scale.unit, Align.topLeft) ;

		for (GameButton button : languageButtons)
		{
			if (!button.isActive()) { continue ;}
			
			button.display(0, false, mousePos) ;
		}

		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			
			button.display(0, true, mousePos) ;
		}
		
		if (step == 1)
		{
			liveInput.displayTypingField(Game.getScreen().pos(0.34, 0.36), false) ;
		}
		if (step == 4)
		{
			displayJobDescription() ;
		}
		
		if (stepMessage.length - 1 <= step) { return ;}
		GamePanel.DP.drawText(textPos, Align.center, Draw.stdAngle, stepMessage[step], font, textColor) ;
	}

	public static void run(Player player, Point mousePos)
	{
		if (!openingGif.hasPlayed())
		{
			if (!openingGif.isActive() & !openingGif.isDonePlaying())
			{
//				Music.PlayMusic(thunderSound) ;
//				Music.PlayMusic(introMusic) ;
				openingGif.start(new Point(0, 0), Align.topLeft);
			}
			Gif.playAll() ;
    		return ;
		}
		
		navigate(player.getCurrentAction()) ;
		if (step == 1 && player.getCurrentAction() != null)
		{
			liveInput.receiveInput(player.getCurrentAction()) ;
		}
		// act(player.getCurrentAction(), mousePos) ;
		if (newGame)
		{
			display(player.getCurrentAction(), mousePos) ;
		}
		else
		{
			displayLoadingSlot(player, mousePos) ;
		}
		player.resetAction() ;
		
		if (isOver())
		{
			if (newGame())
			{
				Game.difficultLevel = getChosenDifficultLevel() ;
				Game.setPlayer(getChosenPlayer()) ;
			}
			Game.setState(GameStates.loading) ;
		}

	}
	
	public static boolean newGame() { return newGame ;}
	public static boolean isOver() { return isOver ;}
	
}
