package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import Graphics.DrawPrimitives;

public class Settings
{
	private boolean musicIsOn ;
	private boolean soundEffectsAreOn ;
	private boolean showPlayerRange ;
	private int attDisplay ;
	private int damageAnimation ;
	private String[] actionKeys ;
	
	public Settings(boolean musicIsOn, boolean soundEffectsAreOn, boolean showPlayerRange, int attDisplay, int damageAnimation)
	{
		this.musicIsOn = musicIsOn ;
		this.soundEffectsAreOn = soundEffectsAreOn ;
		this.showPlayerRange = showPlayerRange ;
		this.attDisplay = attDisplay ;
		this.damageAnimation = damageAnimation ;
	}
	

	public boolean getMusicIsOn() {return musicIsOn ;}
	public boolean getSoundEffectsAreOn() {return soundEffectsAreOn ;}
	public boolean getShowPlayerRange() {return showPlayerRange ;}
	public int getAttDisplay() {return attDisplay ;}
	public int getDamageAnimation() {return damageAnimation ;}
	public void setMusicIsOn(boolean newValue) {musicIsOn = newValue ;}
	public void setSoundEffectsAreOn(boolean newValue) {soundEffectsAreOn = newValue ;}
	public void setShowPlayerRange(boolean newValue) {showPlayerRange = newValue ;}
	public void setAttDisplay(int newValue) {attDisplay = newValue ;}
	public void setDamageAnimation(int newValue) {damageAnimation = newValue ;}
	
	public void displayValue(boolean value, Point TextPos, float OverallAngle, Font font, Color[] ColorPalette, DrawPrimitives DP)
	{
		if (value)
		{
			DP.DrawText(TextPos, "BotCenter", OverallAngle, "On", font, ColorPalette[5]) ;							
		}
		else
		{
			DP.DrawText(TextPos, "BotCenter", OverallAngle, "Off", font, ColorPalette[4]) ;							
		}
	}
}
