package main;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import NPC.NPC;
import graphics.Align;
import graphics2.Draw;
import liveBeings.LiveBeing;
import utilities.Util;

public class Settings
{
    private boolean musicIsOn ;
    private boolean soundEffectsAreOn ;
    private boolean showAtkRange ;
    private int attDisplay ;
    private int damageAnimation ;
    private String language ;

    private static final List<String> languages ;

    static
    {
        languages = Arrays.asList(Util.readcsvFile(Path.DADOS + "GameLanguages.csv").get(0)) ;
    }
    
    public Settings(boolean musicIsOn, boolean soundEffectsAreOn, boolean showAtkRange, int attDisplay, int damageAnimation)
    {
        this.musicIsOn = musicIsOn ;
        this.soundEffectsAreOn = soundEffectsAreOn ;
        this.showAtkRange = showAtkRange ;
        this.attDisplay = attDisplay ;
        this.damageAnimation = damageAnimation ;
        this.language = languages.get(0) ;
    }

    public void update(int item)
    {
		if (item == 0)
		{
			musicIsOn = !musicIsOn ;
            if (musicIsOn)
            {
                Game.getPlayer().getMap().getMusic().start() ;
            }
            else
            {
                Game.getPlayer().getMap().getMusic().stop() ;
            }
		}
		if (item == 1)
		{
			soundEffectsAreOn = !soundEffectsAreOn ;
		}
		if (item == 2)
		{
			showAtkRange = !showAtkRange ;
		}
		if (item == 3)
		{
			attDisplay = (attDisplay + 1) % 2 ;
		}
		if (item == 4)
		{
			damageAnimation = (damageAnimation + 1) % 4 ;
			LiveBeing.updateDamageAnimation(damageAnimation) ;
		}
		if (item == 5)
		{
			GameFrame.getMe().resizeWindow() ;
		}
        if (item == 6)
        {
            language = languages.get((languages.indexOf(language) + 1) % languages.size()) ;

            // TODO incluir demais textos
            NPC.updateText(language) ;
        }
    }

    public void displaySetting(int item, Point pos, Align align)
    {
        switch (item)
        {
            case 0:
                Draw.settingSwitch(pos, align, musicIsOn) ;
                return ;
            case 1:
                Draw.settingSwitch(pos, align, soundEffectsAreOn) ;
                return ;
            case 2:
                Draw.settingSwitch(pos, align, showAtkRange) ;
                return ;
            case 3:
                Draw.settingBars(pos, align, 60, attDisplay, 2) ;
                return ;
            case 4:
                Draw.settingBars(pos, align, 60, damageAnimation, 4) ;
                return ;
            case 5:
                Draw.settingSwitch(pos, align, GameFrame.fullScreen) ;
                return ;
            case 6:
                Draw.settingBars(pos, align, 60, languages.indexOf(language), languages.size()) ;
            default:
                return ;
        }
    }

    public boolean getMusicIsOn() {return musicIsOn ;}
    public boolean getSoundEffectsAreOn() {return soundEffectsAreOn ;}
    public boolean getShowAtkRange() {return showAtkRange ;}
    public int getAttDisplay() {return attDisplay ;}
    public int getDamageAnimation() {return damageAnimation ;}
    public String getLanguage() { return language ;}    
}
