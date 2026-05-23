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
import UI.GameTextButton;
import graphics.Align;
import graphics2.Draw;
import liveBeings.PlayerActions;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.MainGame3_4;
import main.Music;
import main.Palette;
import main.Path;
import utilities.Util;


public class PauseWindow extends GameWindow
{
    private boolean isListeningToKeyInput ;
    private final Map<Integer, List<GameButton>> menuButtons ;
    private final Map<Integer, List<Integer>> menuSettings ;
    private static final Point WINDOW_POS = new Point(20, 20) ;
    private static final Image IMAGE_BG = ImageLoader.loadImage(Path.WINDOWS_IMG + "SettingsBackground.png") ;
    private static final Clip SOUND_EFFECT_ON_HOVER = Music.loadMusicFile("PauseButtonHoverSoundEffect.wav") ;
    private static final Image TEXT_BG_IMG = ImageLoader.loadImage(Path.UI_IMG + "SettingsTextBackground.png") ;
    private static final Font LARGE_FONT = new Font(Game.getMainFontName(), Font.BOLD, 20) ;
    private static final int SETTING_COL_POS_X = WINDOW_POS.x + (2 * Util.getSize(IMAGE_BG).width) / 3 ;

    public PauseWindow()
    {
        super("Opções", WINDOW_POS, IMAGE_BG, 2, 0, 7, 0) ;
        
        Image btnImg = ImageLoader.loadImage(Path.UI_IMG + "SettingsButton.png") ;
        Image selectedBtnImg = ImageLoader.loadImage(Path.UI_IMG + "SettingsButtonSelected.png") ;
        Image btnShortImg = ImageLoader.loadImage(Path.UI_IMG + "SettingsShortButton.png") ;
        Image selectedBtnShortImg = ImageLoader.loadImage(Path.UI_IMG + "SettingsShortButtonSelected.png") ;

        GameButton returnToMainMenu = new GameTextButton(gridPos(17, 1), Align.center, "", "V O L T A R", btnImg, selectedBtnImg, () -> { switchToMenu(0) ;}, SOUND_EFFECT_ON_HOVER) ;
        returnToMainMenu.deactivate() ;

        List<GameButton> buttonsMainMenu = List.of(
            new GameTextButton(gridPos(4, 1), Align.center, "", "R E S U M E", btnImg, selectedBtnImg, () -> { 
                updateButtons() ;
                switchOpenClose() ;
            }, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(6, 1), Align.center, "", "P L A Y E R", btnImg, selectedBtnImg, () -> { switchToMenu(1) ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(8, 1), Align.center, "", "A U D I O", btnImg, selectedBtnImg, () -> { switchToMenu(2) ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(10, 1), Align.center, "", "V I D E O", btnImg, selectedBtnImg, () -> { switchToMenu(3) ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(12, 1), Align.center, "", "C O N T R O L S", btnImg, selectedBtnImg, () -> { switchToMenu(4) ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(14, 1), Align.center, "", "L A N G U A G E", btnImg, selectedBtnImg, () -> { switchToMenu(5); ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(16, 1), Align.center, "", "E X I T", btnImg, selectedBtnImg, () -> { MainGame3_4.closeGame() ;}, SOUND_EFFECT_ON_HOVER)
        ) ;
        buttonsMainMenu.forEach(GameButton::deactivate) ;
        buttonsMainMenu.get(0).select() ;

        List<GameButton> buttonsPlayerMenu = List.of(
            new GameTextButton(gridPos(6, 1), Align.center, "", "R A N G E D I S P L A Y", btnImg, selectedBtnImg, () -> {Game.getSettings().update(2) ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(8, 1), Align.center, "", "A T T D I S P L A Y", btnImg, selectedBtnImg, () -> {Game.getSettings().update(3) ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(10, 1), Align.center, "", "D A M A G E S T Y L E", btnImg, selectedBtnImg, () -> {Game.getSettings().update(4) ;}, SOUND_EFFECT_ON_HOVER),
            returnToMainMenu
        ) ;
        buttonsPlayerMenu.forEach(GameButton::deactivate) ;

        List<GameButton> buttonsAudioMenu = List.of(
            new GameTextButton(gridPos(6, 1), Align.center, "", "M U S I C", btnImg, selectedBtnImg, () -> {Game.getSettings().update(0) ;}, SOUND_EFFECT_ON_HOVER),
            new GameTextButton(gridPos(8, 1), Align.center, "", "S O U N D E F F E C T S", btnImg, selectedBtnImg, () -> {Game.getSettings().update(1) ;}, SOUND_EFFECT_ON_HOVER),
            returnToMainMenu) ;
        buttonsAudioMenu.forEach(GameButton::deactivate) ;

        List<GameButton> buttonsVideoMenu = List.of(
            new GameTextButton(gridPos(6, 1), Align.center, "", "F U L L S C R E E N", btnImg, selectedBtnImg, () -> {Game.getSettings().update(5) ;}, SOUND_EFFECT_ON_HOVER),
            returnToMainMenu) ;
        buttonsVideoMenu.forEach(GameButton::deactivate) ;

        List<GameButton> buttonsControlsMenu = new ArrayList<>() ;
        for (int i = 0 ; i <= PlayerActions.values().length - 1 ; i += 1)
        {
            final int index = i ;
            buttonsControlsMenu.add(new GameTextButton(gridPos(i + 1, 0), Align.center, "", PlayerActions.values()[i].toString(), btnShortImg, selectedBtnShortImg, () -> { item = index ; isListeningToKeyInput = !isListeningToKeyInput ;}, SOUND_EFFECT_ON_HOVER)) ;
        }
        buttonsControlsMenu.add(returnToMainMenu) ;

        buttonsControlsMenu.forEach(GameButton::deactivate) ;

        List<GameButton> buttonsLanguageMenu = List.of(
            new GameTextButton(gridPos(6, 1), Align.center, "", "L A N G U A G E", btnShortImg, selectedBtnShortImg, () -> { Game.getSettings().update(6) ;}, SOUND_EFFECT_ON_HOVER)
        ) ;
        buttonsLanguageMenu.forEach(GameButton::deactivate) ;

        menuButtons = Map.of(
            0, buttonsMainMenu,
            1, buttonsPlayerMenu,
            2, buttonsAudioMenu,
            3, buttonsVideoMenu,
            4, buttonsControlsMenu,
            5, buttonsLanguageMenu
        ) ;

        menuSettings = new HashMap<>() ;
        menuSettings.put(0, List.of()) ;
        menuSettings.put(1, List.of(2, 3, 4)) ;
        menuSettings.put(2, List.of(0, 1)) ;
        menuSettings.put(3, List.of(5)) ;
        menuSettings.put(4, List.of()) ;
        menuSettings.put(5, List.of(6)) ;
    }

    private static Point gridPos(int row, int col)
    {
        Dimension windowSize = Util.getSize(IMAGE_BG) ;
        return Util.translate(WINDOW_POS, windowSize.width / 2 + (col - 1) * 100, row * 50) ;
    }

    public void updateButtons()
    {
        menuButtons.get(menu).forEach(!isOpen ? GameButton::activate : GameButton::deactivate) ;
    }

    private void switchToMenu(int newMenu)
    {
        if (menu == newMenu) { return ;}

        menuButtons.get(menu).forEach(GameButton::deactivate) ;
        menuButtons.get(newMenu).forEach(GameButton::activate) ;
        menu = newMenu ;
    }

    public void navigate(String action)
    {
        if (action == null) { return ;}

        switch (menu)
        {
            case 0:                
                if (action.equals(stdMenuDown))
                {
                    menuButtons.get(0).get(item).deSelect() ;
                    itemUp() ;
                    menuButtons.get(0).get(item).select() ;
                }
                if (action.equals(stdMenuUp))
                {
                    menuButtons.get(0).get(item).deSelect() ;
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
        GamePanel.getDP().drawImage(IMAGE_BG, WINDOW_POS, Align.topLeft, 0.3) ;

        for (GameButton button : menuButtons.get(menu))
        {
            if (!button.isActive()) { continue ;}
            
            button.display(0, true, mousePos, Palette.colors[3], 0.6) ;
        }
        for (int i = 0 ; i <= menuSettings.get(menu).size() - 1 ; i += 1)
        {
            int setting = menuSettings.get(menu).get(i) ;
            int posY = menuButtons.get(menu).get(i).getCenter().y ;
            Game.getSettings().displaySetting(setting, new Point(SETTING_COL_POS_X, posY), Align.bottomLeft);
        }
        if (menu == 4)
        {            
            for (int i = 0 ; i <= PlayerActions.values().length - 1 ; i += 1)
            {
                Color textColor = isListeningToKeyInput && item == i ? Palette.colors[18] : Palette.colors[3] ;
                int posY = menuButtons.get(menu).get(i).getCenter().y ;
                GamePanel.getDP().drawImage(TEXT_BG_IMG, new Point(SETTING_COL_POS_X, posY), Align.center, 0.5) ;
                GamePanel.getDP().drawText(new Point(SETTING_COL_POS_X, posY), Align.center, Draw.stdAngle, PlayerActions.values()[i].getKey(), LARGE_FONT, textColor) ;
            }
        }
    }
}
