package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Arrays;

import GameComponents.Size;
import Graphics.DrawPrimitives;
import LiveBeings.Player;
import Main.Game;
import Main.Utg;

public class Settings extends Window
{
	private boolean musicIsOn ;
	private boolean soundEffectsAreOn ;
	private boolean showPlayerRange ;
	private int attDisplay ;
	private int damageAnimation ;
	private String[] actionKeys ;
	
	public Settings(boolean musicIsOn, boolean soundEffectsAreOn, boolean showPlayerRange, int attDisplay, int damageAnimation)
	{
		super(null, 0, 0, 0, 0) ;
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
	
	public void displayValue(boolean value, Point TextPos, float OverallAngle, Font font, DrawPrimitives DP)
	{
		Color[] ColorPalette = Game.ColorPalette ;
		if (value)
		{
			DP.DrawText(TextPos, "BotCenter", OverallAngle, "On", font, ColorPalette[5]) ;							
		}
		else
		{
			DP.DrawText(TextPos, "BotCenter", OverallAngle, "Off", font, ColorPalette[4]) ;							
		}
	}
	public void display(int[] AllTextCat, String[][] AllText, DrawPrimitives DP)
	{
		int TextCat = AllTextCat[36] ;
		Size screenSize = Game.getScreen().getSize() ;
		Color[] ColorPalette = Game.ColorPalette ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		int L = (int)(0.5*screenSize.x), H = (int)Math.max(0.34*screenSize.y, Player.ActionKeys.length*(Utg.TextH(L / 20 + 2) + 4) + 8) ;
		Font font = new Font("SansSerif", Font.BOLD, L / 20 + 2) ;
		Color[] TextColor = new Color[3 + Player.ActionKeys.length] ;
		Point Pos = new Point((int)(0.4*screenSize.x), (int)(0.4*screenSize.y)) ;
		Point TextPos = new Point(Pos.x + 5, Pos.y + 5) ;
		int TextH = Utg.TextH(font.getSize()) ;
		int Sx = 7*L/8, Sy = TextH + 4 ;
		Arrays.fill(TextColor, ColorPalette[5]) ;
		TextColor[item] = ColorPalette[3] ;
		//DrawMenuWindow(Pos, L, H, null, 0, ColorPalette[8], ColorPalette[7]) ;
		if (menu == 0)
		{
			for (int i = 0 ; i <= 5 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), "BotLeft", OverallAngle, AllText[TextCat][i + 1], font, TextColor[i]) ;
			}
			displayValue(getMusicIsOn(), new Point(TextPos.x + Sx, TextPos.y + Sy), OverallAngle, font, DP) ;
			displayValue(getSoundEffectsAreOn(), new Point(TextPos.x + Sx, TextPos.y + 2 * Sy), OverallAngle, font, DP) ;
			displayValue(getShowPlayerRange(), new Point(TextPos.x + Sx, TextPos.y + 3 * Sy), OverallAngle, font, DP) ;
			DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (3 + 1)*Sy), "BotCenter", OverallAngle, String.valueOf(getAttDisplay()), font, TextColor[3]) ;
			DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (4 + 1)*Sy), "BotCenter", OverallAngle, String.valueOf(getDamageAnimation()), font, TextColor[4]) ;				
		}
		else if (menu == 1)
		{
			for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), "BotLeft", OverallAngle, AllText[TextCat][i + 7], font, TextColor[i]) ;
				DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (i + 1)*Sy - TextH/2), "Center", OverallAngle, Player.ActionKeys[i], font, ColorPalette[5]) ;			
			}
		}
		else if (menu == 2)
		{
			//DrawMenuWindow(Pos, L, (int)Math.max(0.34*screenDim[1], Player.ActionKeys.length*(Utg.TextH(L / 20 + 2) + 4) + 8), null, 0, ColorPalette[8], ColorPalette[7]) ;
			for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), "BotLeft", OverallAngle, AllText[TextCat][i + 7], font, TextColor[i]) ;
				DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (i + 1)*Sy - TextH/2), "Center", OverallAngle, Player.ActionKeys[i], font, TextColor[i]) ;			
			}
		}
	}
}
