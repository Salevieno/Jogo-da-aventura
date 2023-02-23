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
	private Image deeperMenuImage ;
	private boolean musicIsOn ;
	private boolean soundEffectsAreOn ;
	private boolean showPlayerRange ;
	private int attDisplay ;
	private int damageAnimation ;
	private String[] actionKeys ;
	private int selectedActionKeyID ;
	
	public SettingsWindow(Image image, boolean musicIsOn, boolean soundEffectsAreOn, boolean showPlayerRange, int attDisplay, int damageAnimation)
	{
		super("Opções", image, 3, 0, 6, 0) ;
		deeperMenuImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "settingsDeeperWindow.png") ;
		this.musicIsOn = musicIsOn ;
		this.soundEffectsAreOn = soundEffectsAreOn ;
		this.showPlayerRange = showPlayerRange ;
		this.attDisplay = attDisplay ;
		this.damageAnimation = damageAnimation ;
		selectedActionKeyID = - 1;
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
		if (getMenu() == 0)
		{
			if (action.equals(Player.ActionKeys[2]))
			{
				itemUp() ;
			}
			if (action.equals(Player.ActionKeys[0]))
			{
				itemDown() ;
			}
			if (action.equals("Enter") | action.equals(Player.ActionKeys[3]))
			{
				updateSetting() ;
			}
			if (action.equals("Escape"))
			{
				close() ;
			}
		}
		else if (getMenu() == 1)
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
			if (action.equals("Enter"))
			{
				selectActionKey() ;
			}
			if (action.equals("Escape"))
			{
				menu = 0 ;
				numberItems = 6 ;
				item = 5;
			}
		}
		else if (getMenu() == 2)
		{
			if (!action.equals("Escape") & !action.equals("Enter"))
			{
				updateActionKey(action) ;
				selectedActionKeyID = -1 ;
				menu = 1 ;
			}
			if (action.equals("Escape"))
			{
				selectedActionKeyID = -1 ;
				menu = 1 ;
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
			numberItems = Player.ActionKeys.length ;
		}
	}
	
	public void selectActionKey()
	{
		selectedActionKeyID = item ;
		menu = 2 ;
	}
	
	public void updateActionKey(String action)
	{
		Player.ActionKeys[item] = action;
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
	public void display(String[] text, DrawingOnPanel DP)
	{
		Dimension screenSize = Game.getScreen().getSize() ;
		double stdAngle = DrawingOnPanel.stdAngle ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Point pos = new Point((int)(0.4*screenSize.width), (int)(0.35*screenSize.height)) ;
		Point textPos = new Point(pos.x + 25, pos.y  + 42) ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		Color[] textColor = new Color[3 + Player.ActionKeys.length] ;
		Arrays.fill(textColor, Game.ColorPalette[5]) ;
		textColor[item] = Game.ColorPalette[3] ;
		
		if (menu == 0)
		{
			DP.DrawImage(image, pos, Align.topLeft) ;
		}
		else
		{
			DP.DrawImage(deeperMenuImage, pos, Align.topLeft) ;			
		}
		DP.DrawText(UtilG.Translate(textPos, image.getWidth(null) / 2 - 15, -6), Align.bottomCenter, stdAngle, "OpÃ§Ãµes", font, Game.ColorPalette[5]) ;
		if (menu == 0)
		{
			for (int i = 0 ; i <= numberItems - 1 ; i += 1)
			{
				DP.DrawText(new Point(textPos.x, textPos.y + (i + 1)*sy), Align.bottomLeft, stdAngle, text[i + 1], font, textColor[i]) ;
			}
			displayValue(musicIsOn, new Point(textPos.x + sx, textPos.y + sy), stdAngle, font, DP) ;
			displayValue(soundEffectsAreOn, new Point(textPos.x + sx, textPos.y + 2 * sy), stdAngle, font, DP) ;
			displayValue(showPlayerRange, new Point(textPos.x + sx, textPos.y + 3 * sy), stdAngle, font, DP) ;
			DP.DrawText(new Point(textPos.x + sx, textPos.y + (3 + 1)*sy), Align.bottomCenter, stdAngle, String.valueOf(getAttDisplay()), font, textColor[3]) ;
			DP.DrawText(new Point(textPos.x + sx, textPos.y + (4 + 1)*sy), Align.bottomCenter, stdAngle, String.valueOf(getDamageAnimation()), font, textColor[4]) ;				
		}
		else if (menu == 1 | menu == 2)
		{
			for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(textPos.x, textPos.y + (i + 1)*sy), Align.bottomLeft, stdAngle, text[i + 7], font, textColor[i]) ;
				DP.DrawText(new Point(textPos.x + sx, textPos.y + (i + 1)*sy), Align.bottomCenter, stdAngle, Player.ActionKeys[i], font, Game.ColorPalette[5]) ;			
			}
			if (-1 < selectedActionKeyID)
			{
				DP.DrawText(new Point(textPos.x + sx, textPos.y + (selectedActionKeyID + 1)*sy), Align.bottomCenter, stdAngle, Player.ActionKeys[selectedActionKeyID], font, Game.ColorPalette[3]) ;
			}
		}
	}
}
