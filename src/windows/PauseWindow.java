package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Clip;

import UI.GameButton;
import graphics.Align;
import graphics2.Draw;
import liveBeings.PlayerActions;
import main.Game;
import main.GamePanel;
import main.MainGame3_4;
import main.Music;
import main.Path;
import utilities.Util;


public class PauseWindow extends GameWindow
{

    private boolean isListeningToKeyInput ;
	private static final Point windowPos ;
    private static final Image imageBg ;
    private static final Clip soundEffectOnHover ;
    private static final Image textBgImg ;
    private static final Font largeFont ;
    private static final int settingColPosX ;
    private final Map<Integer, List<GameButton>> menuButtons ;
    private final Map<Integer, List<Integer>> menuSettings ;

    static
    {
        windowPos = new Point(20, 20) ;
	    imageBg = Game.loadImage(Path.WINDOWS_IMG + "SettingsBackground.png") ;
        soundEffectOnHover = Music.loadMusicFile("PauseButtonHoverSoundEffect.wav") ;
        textBgImg = Game.loadImage(Path.UI_IMG + "SettingsTextBackground.png") ;
        largeFont = new Font(Game.MainFontName, Font.BOLD, 20) ;
        Dimension windowSize = Util.getSize(imageBg) ; 
        settingColPosX = windowPos.x + (2 * windowSize.width) / 3 ;
    }

    public PauseWindow()
    {
        super("Opções", windowPos, imageBg, 2, 0, 5, 0) ;
        
        Dimension windowSize = Util.getSize(imageBg) ;
        Image btnImg = Game.loadImage(Path.UI_IMG + "SettingsButton.png") ;
        Image selectedBtnImg = Game.loadImage(Path.UI_IMG + "SettingsButtonSelected.png") ;
        Image btnShortImg = Game.loadImage(Path.UI_IMG + "SettingsShortButton.png") ;
        Image selectedBtnShortImg = Game.loadImage(Path.UI_IMG + "SettingsShortButtonSelected.png") ;

        GameButton returnToMainMenu = new GameButton(Util.translate(windowPos, windowSize.width / 2, 850), Align.center, "V O L T A R", btnImg, selectedBtnImg, () -> { switchToMenu(0) ;}, soundEffectOnHover) ;
        returnToMainMenu.deactivate() ;

        List<GameButton> buttonsMainMenu = List.of(
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 200), Align.center, "R E S U M E", btnImg, selectedBtnImg, () -> { 
			updateButtons() ;
			switchOpenClose() ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 300), Align.center, "P L A Y E R", btnImg, selectedBtnImg, () -> { switchToMenu(1) ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 400), Align.center, "A U D I O", btnImg, selectedBtnImg, () -> { switchToMenu(2) ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 500), Align.center, "V I D E O", btnImg, selectedBtnImg, () -> { switchToMenu(3) ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 600), Align.center, "C O N T R O L S", btnImg, selectedBtnImg, () -> { switchToMenu(4) ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 700), Align.center, "E X I T", btnImg, selectedBtnImg, () -> { MainGame3_4.closeGame() ;}, soundEffectOnHover)
        ) ;
        buttonsMainMenu.forEach(GameButton::deactivate) ;
        buttonsMainMenu.get(0).select() ;

        List<GameButton> buttonsPlayerMenu = List.of(
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 300), Align.center, "A T T D I S P L A Y", btnImg, selectedBtnImg, () -> {Game.getSettings().update(2) ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 400), Align.center, "A T K D I S P L A Y", btnImg, selectedBtnImg, () -> {Game.getSettings().update(3) ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 500), Align.center, "D A M A G E S T Y L E", btnImg, selectedBtnImg, () -> {Game.getSettings().update(4) ;}, soundEffectOnHover),
            returnToMainMenu
        ) ;
        buttonsPlayerMenu.forEach(GameButton::deactivate) ;

        List<GameButton> buttonsAudioMenu = List.of(
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 300), Align.center, "M U S I C", btnImg, selectedBtnImg, () -> {Game.getSettings().update(0) ;}, soundEffectOnHover),
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 400), Align.center, "S O U N D E F F E C T S", btnImg, selectedBtnImg, () -> {Game.getSettings().update(1) ;}, soundEffectOnHover),
            returnToMainMenu) ;
        buttonsAudioMenu.forEach(GameButton::deactivate) ;

        List<GameButton> buttonsVideoMenu = List.of(
            new GameButton(Util.translate(windowPos, windowSize.width / 2, 300), Align.center, "F U L L S C R E E N", btnImg, selectedBtnImg, () -> {Game.getSettings().update(5) ;}, soundEffectOnHover),
            returnToMainMenu) ;
        buttonsVideoMenu.forEach(GameButton::deactivate) ;

        List<GameButton> buttonsControlsMenu = new ArrayList<>() ;
        System.out.println(PlayerActions.values().length) ;
        for (int i = 0 ; i <= PlayerActions.values().length - 1 ; i += 1)
        {
            final int index = i ;
            buttonsControlsMenu.add(new GameButton(Util.translate(windowPos, windowSize.width / 2 - 100, 50 + i * 50), Align.center, PlayerActions.values()[i].toString(), btnShortImg, selectedBtnShortImg, () -> { item = index ; isListeningToKeyInput = !isListeningToKeyInput ;}, soundEffectOnHover)) ;
        }
        buttonsControlsMenu.add(returnToMainMenu) ;

        buttonsControlsMenu.forEach(GameButton::deactivate) ;

        menuButtons = Map.of(
            0, buttonsMainMenu,
            1, buttonsPlayerMenu,
            2, buttonsAudioMenu,
            3, buttonsVideoMenu,
            4, buttonsControlsMenu
        ) ;

        menuSettings = new HashMap<>() ;
        menuSettings.put(0, List.of()) ;
        menuSettings.put(1, List.of(2, 3, 4)) ;
        menuSettings.put(2, List.of(0, 1)) ;
        menuSettings.put(3, List.of(5)) ;
        menuSettings.put(4, List.of()) ;
    }

    public void updateButtons()
    {
        menuButtons.get(menu).forEach(!isOpen ? GameButton::activate : GameButton::deactivate) ;
    }

    private void switchToMenu(int newMenu)
    {
        if (menu == newMenu) { return ;}

        menuButtons.get(menu).forEach(GameButton::deactivate) ;
        menu = newMenu ;
        menuButtons.get(menu).forEach(GameButton::activate) ;
    }

    public void navigate(String action)
    {
        if (action == null) { return ;}

        switch (menu)
        {
            case 0:                
                if (action.equals(stdMenuDown))
                {
                    itemUp() ;
                    menuButtons.get(0).get(item).select() ;
                }
                if (action.equals(stdMenuUp))
                {
                    itemDown() ;
                    menuButtons.get(0).get(item).select() ;
                }
                return ;

            case 1:
                if (action.equals(stdMenuDown))
                {
                    itemUp() ;
                }
                if (action.equals(stdMenuUp))
                {
                    itemDown() ;
                }
                if (action.equals(stdExit))
                {
                    menu = 0;
                }

            case 4:
                if (!isListeningToKeyInput) { return ;}
                if (action.equals("LeftClick")) { return ;}

                PlayerActions.values()[item].setKey(action) ;
                isListeningToKeyInput = false ;
                return ;

            default:
                return ;
        }
    }

    public void display(Point mousePos)
    {
        GamePanel.DP.drawImage(imageBg, windowPos, Align.topLeft, 0.3) ;

        for (GameButton button : menuButtons.get(menu))
        {
            if (!button.isActive()) { continue ;}
            
            button.display(0, true, mousePos, Game.palette[3], 0.6) ;
        }
        for (int i = 0 ; i <= menuSettings.get(menu).size() - 1 ; i += 1)
        {
            int setting = menuSettings.get(menu).get(i) ;
            int posY = menuButtons.get(menu).get(i).getCenter().y ;
            Game.getSettings().displaySetting(setting, new Point(settingColPosX, posY), Align.bottomLeft);
        }
        if (menu == 4)
        {            
            for (int i = 0 ; i <= PlayerActions.values().length - 1 ; i += 1)
            {
                Color textColor = isListeningToKeyInput && item == i ? Game.palette[18] : Game.palette[3] ;
                int posY = menuButtons.get(menu).get(i).getCenter().y ;
                GamePanel.DP.drawImage(textBgImg, new Point(settingColPosX, posY), Align.center, 0.5) ;
                GamePanel.DP.drawText(new Point(settingColPosX, posY), Align.center, Draw.stdAngle, PlayerActions.values()[i].getKey(), largeFont, textColor) ;
            }
        }
    }
}
