package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class SettingsWindow extends GameWindow
{
	private boolean musicIsOn ;
	private boolean soundEffectsAreOn ;
	private boolean showPlayerRange ;
	private int attDisplay ;
	private int damageAnimation ;
	private String[] actionKeys ;
	
	public SettingsWindow(Image image, boolean musicIsOn, boolean soundEffectsAreOn, boolean showPlayerRange, int attDisplay, int damageAnimation)
	{
		super(image, 3, 0, 6, 0) ;
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
	
	public void navigate(String action)
	{
		if (getTab() == 0)
		{
			if (action.equals(Player.ActionKeys[2]))
			{
				itemUp() ;
			}
			if (action.equals(Player.ActionKeys[0]))
			{
				itemDown() ;
			}
			if (action.equals("Enter") | action.equals("MouseLeftClick"))
			{
				updateSetting() ;
			}
		}
		if (getTab() == 1)
		{
			if (action.equals(Player.ActionKeys[2]))
			{
				itemUp() ;
			}
			if (action.equals(Player.ActionKeys[0]))
			{
				itemDown() ;
			}
			if (action.equals(Player.ActionKeys[3]))
			{
				windowUp() ;
			}
			if (action.equals(Player.ActionKeys[1]))
			{
				windowDown() ;
			}
		}
	}
	
	public void updateSetting()
	{
		if (item == 0)
		{
			musicIsOn = !musicIsOn ;
		}
		if (item == 1)
		{
			soundEffectsAreOn = !soundEffectsAreOn ;
		}
		if (item == 2)
		{
			showPlayerRange = !showPlayerRange ;
		}
		if (item == 3)
		{
			attDisplay = (attDisplay + 1) % 2 ;
		}
		if (item == 4)
		{
			damageAnimation = (damageAnimation + 1) % 4 ;
		}
		if (item == 5)
		{
			menu = 1 ;
			item = 0 ;
		}
	}
	
	public void displayValue(boolean value, Point TextPos, double OverallAngle, Font font, DrawingOnPanel DP)
	{
		Color[] ColorPalette = Game.ColorPalette ;
		if (value)
		{
			DP.DrawText(TextPos, Align.bottomCenter, OverallAngle, "On", font, ColorPalette[5]) ;							
		}
		else
		{
			DP.DrawText(TextPos, Align.bottomCenter, OverallAngle, "Off", font, ColorPalette[4]) ;							
		}
	}
	public void display(String[] SettingsText, DrawingOnPanel DP)
	{
		Dimension screenSize = Game.getScreen().getSize() ;
		Color[] colorPalette = Game.ColorPalette ;
		double overallAngle = DrawingOnPanel.stdAngle ;
		Dimension size = new Dimension((int)(0.5*screenSize.width), (int)Math.max(0.34*screenSize.height, Player.ActionKeys.length*(UtilG.TextH(screenSize.width / 40 + 2) + 4) + 8)) ;
		Font font = new Font("SansSerif", Font.BOLD, size.width / 20 + 2) ;
		Point pos = new Point((int)(0.4*screenSize.width), (int)(0.4*screenSize.height)) ;
		Point TextPos = new Point(pos.x + 5, pos.y + 5) ;
		int TextH = UtilG.TextH(font.getSize()) ;
		int Sx = 7 * size.width / 8, Sy = TextH + 4 ;
		Color[] TextColor = new Color[3 + Player.ActionKeys.length] ;
		Arrays.fill(TextColor, colorPalette[5]) ;
		TextColor[item] = colorPalette[3] ;
		DP.DrawImage(image, pos, Align.center) ;
		if (menu == 0)
		{
			for (int i = 0 ; i <= numberItems - 1 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), Align.bottomLeft, overallAngle, SettingsText[i + 1], font, TextColor[i]) ;
			}
			displayValue(getMusicIsOn(), new Point(TextPos.x + Sx, TextPos.y + Sy), overallAngle, font, DP) ;
			displayValue(getSoundEffectsAreOn(), new Point(TextPos.x + Sx, TextPos.y + 2 * Sy), overallAngle, font, DP) ;
			displayValue(getShowPlayerRange(), new Point(TextPos.x + Sx, TextPos.y + 3 * Sy), overallAngle, font, DP) ;
			DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (3 + 1)*Sy), Align.bottomCenter, overallAngle, String.valueOf(getAttDisplay()), font, TextColor[3]) ;
			DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (4 + 1)*Sy), Align.bottomCenter, overallAngle, String.valueOf(getDamageAnimation()), font, TextColor[4]) ;				
		}
		else if (menu == 1)
		{
			for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), Align.bottomLeft, overallAngle, SettingsText[i + 7], font, TextColor[i]) ;
				DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (i + 1)*Sy - TextH/2), Align.center, overallAngle, Player.ActionKeys[i], font, colorPalette[5]) ;			
			}
		}
		else if (menu == 2)
		{
			//DrawMenuWindow(Pos, L, (int)Math.max(0.34*screenDim[1], Player.ActionKeys.length*(Utg.TextH(L / 20 + 2) + 4) + 8), null, 0, ColorPalette[8], ColorPalette[7]) ;
			for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), Align.bottomLeft, overallAngle, SettingsText[i + 7], font, TextColor[i]) ;
				DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (i + 1)*Sy - TextH/2), Align.center, overallAngle, Player.ActionKeys[i], font, TextColor[i]) ;			
			}
		}
	}
}