package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import UI.ButtonFunction;
import UI.GameButton;
import UI.GameIconButton;
import UI.GameTextButton;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import graphics2.SpriteAnimation;
import liveBeings.Player;
import music.GameMusic;
import music.GameSound;
import music.MusicManager;
import screen.Screen;
import spells.BuffData;
import spells.SpellData;
import utilities.Util;


public abstract class Opening
{
    private static List<GameButton> buttons = new ArrayList<>() ;
    private static List<List<GameButton>> buttonsInStep = new ArrayList<>() ;
    private static List<GameButton> languageButtons = new ArrayList<>() ;
    private static List<LoadingSlot> loadSlots = new ArrayList<>() ;
	private static List<Player> players ;
    private static String[] stepMessage ;
    private static String[] jobDescription ;
    private static int step = 0;
    private static boolean newGame = true ;
    private static boolean isOver = false ;
    
    private static String chosenName ;
    private static double difficultLevel ;
    private static String chosenSex ;
    private static int chosenJob ;
	private static LiveInput liveInput = new LiveInput() ;	

    private static final Font FONT = new Font(Game.getMainFontName(), Font.BOLD, 16) ;

	private static final SpriteAnimation OPENING_ANI = new SpriteAnimation(Path.OPENING_IMG + "Opening.png", new Point(), Align.topLeft, 12, 0.05) ;
	
	private static final Image BACKGROUND_IMAGE = ImageLoader.loadImage(Path.OPENING_IMG + "Opening.png") ;
	private static final Image JOB_DESCRIPTION_BACKGROUND = ImageLoader.loadImage(Path.OPENING_IMG + "JobDescriptionBackground.png") ;
	// private static final Image LoadingEnfeite ;
	private static final GameSound THUNDER_SOUND = new GameSound("Thunder.wav") ;
	private static final Clip INTRO_MUSIC = GameMusic.load("intro.wav") ;
    private static final int QTD_LOAD_SLOTS = 3 ;
	
	
	static
	{
		// LoadingEnfeite = ImageLoader.loadImage("\\Opening\\" + "LoadingEnfeite.png") ;
		ButtonFunction portAction = () -> { } ; // TODO switch language
		ButtonFunction enAction = () -> { } ;
		ButtonFunction newGameAction = () -> {advanceStep() ;} ;
		ButtonFunction loadGameAction = () -> { switchToLoadGameScreen() ;} ;
		ButtonFunction confirmNameAction = () -> {chosenName = liveInput.getText() ; advanceStep() ;} ;
		ButtonFunction maleAction = () -> { chosenSex = "M" ; advanceStep() ;} ;
		ButtonFunction femaleAction = () -> { chosenSex = "F" ; advanceStep() ;} ;
		ButtonFunction easyAction = () -> { difficultLevel = 0.3 ; advanceStep() ;} ;
		ButtonFunction mediumAction = () -> { difficultLevel = 0.7 ; advanceStep() ;} ;
		ButtonFunction hardAction = () -> { difficultLevel = 1.0 ; advanceStep() ;} ;
		ButtonFunction knightAction = () -> { chosenJob = 0 ; advanceStep() ;} ;
		ButtonFunction mageAction = () -> { chosenJob = 1 ; advanceStep() ;} ;
		ButtonFunction archerAction = () -> { chosenJob = 2 ; advanceStep() ;} ;
		ButtonFunction animalAction = () -> { chosenJob = 3 ; advanceStep() ;} ;
		ButtonFunction thiefAction = () -> { chosenJob = 4 ; advanceStep() ;} ;
		
		Screen screen = Screen.getMe() ;
		GameButton portButton = new GameIconButton(screen.pos(0.85, 0.05), Align.center, ImageLoader.loadImage(Path.OPENING_IMG + "Port.png"), ImageLoader.loadImage(Path.OPENING_IMG + "PortSelected.png"), portAction) ;
		GameButton enButton = new GameIconButton(screen.pos(0.95, 0.05), Align.center, ImageLoader.loadImage(Path.OPENING_IMG + "En.png"), ImageLoader.loadImage(Path.OPENING_IMG + "EnSelected.png"), enAction) ;
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
				screen.pos(0.1, 0.3), screen.pos(0.3, 0.3), screen.pos(0.5, 0.3), screen.pos(0.7, 0.3), screen.pos(0.9, 0.3)} ;
		ButtonFunction[] btAction = new ButtonFunction[] {
				newGameAction, loadGameAction,
				confirmNameAction,
				maleAction, femaleAction,
				easyAction, mediumAction, hardAction,
				knightAction, mageAction, archerAction, animalAction, thiefAction} ;
		for (int i = 0 ; i <= btNames.length - 1; i += 1)
		{
			Image btImage = ImageLoader.loadImage(Path.OPENING_IMG + btNames[i] + ".png") ;
			Image btImageSelected = ImageLoader.loadImage(Path.OPENING_IMG + btNames[i] + " Selected.png") ;
			if (btImage == null) { btImage = ImageLoader.loadImage("ButtonGeneral.png") ;}
			if (btImageSelected == null) { btImageSelected = ImageLoader.loadImage(Path.OPENING_IMG + btNames[i] + " Selected.png") ;}
			if (btImageSelected == null) { btImageSelected = ImageLoader.loadImage("ButtonGeneralSelected.png") ;}
			GameButton newButton = new GameTextButton(btPos[i], Align.center, btNames[i], btNames[i], btAction[i]) ;
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
    	// jobDescriptionEn = new String[]
		// {
		// 	"Knights are powerful melee warriors. They have great attack, power and vitality and are the strongest warriors in the realm.",
		//     "Mages have the greatest magical power. They control the elements and can use supernatural powers to manipulate magic and life.",
		//     "Archers are specialized in distance fighting. They use physical power combined with the power of the elements.",
		//     "Animals live in harmony with nature and can enjoy its powers. They have great power over life and are incredibly agile.",
		//     "Thieves are the fastest in the whole realm. They brutally attack any enemy that crosses their way, looking for power and wealth."	
		// };
    	jobDescription = new String[]
		{
			"Cavaleiros são poderosos guerreiros corpo-a-corpo. Eles tem grande ataque, poder e vitalidade e são os guerreiros mais fortes do reino.",
		    "Magos tem o maior poder mágico. Eles controlam os elementos e podem usar poderes sobrenaturais para manipular a magia e a vida.",
		    "Arqueiros são guerreiros especializados em luta à distância. Eles usam poder físico combinado com o poder dos elementos.",
		    "Animais vivem em harmonia com a natureza e podem usufruir dos seus poderes. Eles tem grande poder sobre a vida e incrível agilidade.",
		    "Ladrões são os mais ágeis em todo o reino. Eles atacam cruelmente qualquer inimigo que cruze o seu caminho buscando poder e riqueza."	
		};

	}

	public static Player getChosenPlayer() { return new Player(chosenName, chosenSex, chosenJob) ;}
	public static double getChosenDifficultLevel() { return difficultLevel ;}
	public static SpriteAnimation getOpeningGif() { return OPENING_ANI ;}

	private static void switchToLoadGameScreen()
	{		
		players = new ArrayList<>(QTD_LOAD_SLOTS) ;

		newGame = false ;
		buttons.get(0).deactivate() ;
		buttons.get(1).deactivate() ;
		BuffData.createBuffs() ;
		BuffData.createNerfs() ;
		SpellData.createSpells() ;
		// Spell.load("portugues", Buff.getAllBuffs(), Buff.getAllNerfs()) ;
        for (int i = 0 ; i <= QTD_LOAD_SLOTS - 1 ; i += 1)
        {
            Player playerLoaded = Player.load(i + 1) ;
            if (playerLoaded == null)
            {
                Log.warn("Save " + (i + 1) + " was not found or did not load corretly!") ;
                continue ;
            }

            players.add(playerLoaded) ;
            loadSlots.add(new LoadingSlot("Load slot " + (i + 1), new Point(96 + 384 * i, 240), playerLoaded)) ;
        }
	}

    protected static void finish()
    {
		loadSlots.forEach(LoadingSlot::deactivateButton) ;
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

		if (selectedButton == null) { Log.warn("No button selected when trying to select next") ; return ;}

		int selectedButtonIndex = screenButtons.indexOf(selectedButton) ;
		int nextButtonIndex = screenButtons.size() == selectedButtonIndex + 1 ? 0 : selectedButtonIndex + 1 ;

		screenButtons.get(selectedButtonIndex).deSelect() ;
		screenButtons.get(nextButtonIndex).select() ;
	}

	private static void selectPreviousButton()
	{
		List<GameButton> screenButtons = buttonsInStep.get(step) ;
		GameButton selectedButton = screenButtons.stream().filter(GameButton::isSelected).findFirst().orElse(null) ;

		if (selectedButton == null) { Log.warn("No button selected when trying to select previous") ; return ;}

		int selectedButtonIndex = screenButtons.indexOf(selectedButton) ;
		int previousButtonIndex = 0 == selectedButtonIndex ? screenButtons.size() - 1 : selectedButtonIndex - 1 ;

		screenButtons.get(selectedButtonIndex).deSelect() ;
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

	private static void displayLoadingSlot(Point mousePos)
	{
        loadSlots.forEach(slot -> slot.display(mousePos)) ;
	}
	
	private static void displayJobDescription()
	{
		int padding = 10 ;
		int maxLength = JOB_DESCRIPTION_BACKGROUND.getWidth(null) - padding ;
		int sy = FONT.getSize() + 6 ;
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			Point rectPos = Screen.getMe().pos(0.02 + i * 0.2, 0.4) ;
			Point textPos = Util.translate(rectPos, padding, padding) ;
			GamePanel.getDP().drawImage(JOB_DESCRIPTION_BACKGROUND, rectPos, Align.topLeft) ;
			Draw.fitText(textPos, sy, Align.topLeft, jobDescription[i], FONT, maxLength, Palette.colors[0]) ;
		}
	}
	
	public static void display(String action, Point mousePos)
	{
		Point textPos = Screen.getMe().pos(0.5, 0.3) ;
		Color textColor = Palette.colors[0] ;
		
		GamePanel.getDP().drawImage(BACKGROUND_IMAGE, new Point(0, 0), 0, Scale.unit, Align.topLeft) ;

		for (GameButton button : languageButtons)
		{
			if (!button.isActive()) { continue ;}
			
			button.display(false, mousePos) ;
		}

		for (GameButton button : buttons)
		{
			if (!button.isActive()) { continue ;}
			
			button.display(true, mousePos) ;
		}
		
		if (step == 1)
		{
			liveInput.displayTypingField(Screen.getMe().pos(0.34, 0.36), false) ;
		}
		if (step == 4)
		{
			displayJobDescription() ;
		}
		
		if (stepMessage.length - 1 <= step) { return ;}
		GamePanel.getDP().drawText(textPos, Align.center, stepMessage[step], FONT, textColor) ;
	}

	public static void run(Player player, Point mousePos)
	{
		if (!OPENING_ANI.hasFinished())
		{
			if (!OPENING_ANI.isActive() && !OPENING_ANI.hasFinished())
			{
				THUNDER_SOUND.play() ;
				MusicManager.playMusic(INTRO_MUSIC) ;
				OPENING_ANI.activate() ;
			}
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
			displayLoadingSlot(mousePos) ;
		}
		player.resetAction() ;
		
		if (isOver())
		{
			MusicManager.stopMusic(INTRO_MUSIC) ;
			OPENING_ANI.deactivate();
			if (newGame())
			{
				Game.setDifficultLevel(getChosenDifficultLevel()) ;
				Game.setPlayer(getChosenPlayer()) ;
			}
			Game.setState(GameStates.loading) ;
		}

	}
	
	public static boolean newGame() { return newGame ;}
	public static boolean isOver() { return isOver ;}
	
}
