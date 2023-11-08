package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Battle;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;

public class SettingsWindow extends GameWindow
{
	private Image deeperMenuImage ;
	private boolean musicIsOn ;
	private boolean soundEffectsAreOn ;
	private boolean showAtkRange ;
	private int attDisplay ;
	private int damageAnimation ;
//	private String[] actionKeys ;
	private int selectedActionKeyID ;
	
	
	private static final Point windowPos = Game.getScreen().pos(0.4, 0.35) ;
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
	
	public SettingsWindow(Image image, boolean musicIsOn, boolean soundEffectsAreOn, boolean showAtkRange, int attDisplay, int damageAnimation)
	{
		super("Opções", image, 3, 0, 6, 0) ;
		deeperMenuImage = UtilS.loadImage("\\Windows\\" + "settingsDeeperWindow.png") ;
		this.musicIsOn = musicIsOn ;
		this.soundEffectsAreOn = soundEffectsAreOn ;
		this.showAtkRange = showAtkRange ;
		this.attDisplay = attDisplay ;
		this.damageAnimation = damageAnimation ;
		selectedActionKeyID = - 1;
	}
	

	public boolean getMusicIsOn() {return musicIsOn ;}
	public boolean getSoundEffectsAreOn() {return soundEffectsAreOn ;}
	public boolean getShowAtkRange() {return showAtkRange ;}
	public int getAttDisplay() {return attDisplay ;}
	public int getDamageAnimation() {return damageAnimation ;}
	public void setMusicIsOn(boolean newValue) {musicIsOn = newValue ;}
	public void setSoundEffectsAreOn(boolean newValue) {soundEffectsAreOn = newValue ;}
	public void setShowPlayerRange(boolean newValue) {showAtkRange = newValue ;}
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
			if (action.equals("Enter") | action.equals("LeftClick") | action.equals(Player.ActionKeys[3]))
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
			if (action.equals("Enter") | action.equals("LeftClick"))
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
			showAtkRange = !showAtkRange ;
		}
		if (item == 3)
		{
			attDisplay = (attDisplay + 1) % 2 ;
		}
		if (item == 4)
		{
			damageAnimation = (damageAnimation + 1) % 4 ;
			Battle.updateDamageAnimation(damageAnimation) ;
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
	
	public void displayValue(Point textPos, boolean selected, String text, double angle, DrawingOnPanel DP)
	{
		if (text == null)
		{
			text = selected ? "On" : "Off" ;
		}
			
		Color textColor = selected ? Game.colorPalette[20] : Game.colorPalette[2] ;

		DP.DrawText(textPos, Align.bottomCenter, angle, text, font, textColor) ;	
	}
	
	
	private void displayMenu0(Point mousePos, String[] text, DrawingOnPanel DP)
	{
		
		boolean[] keyIsOn = new boolean[] {musicIsOn, soundEffectsAreOn, showAtkRange, false, false} ;
		Point optionPos = UtilG.Translate(windowPos, 25, 42) ;
		double angle = DrawingOnPanel.stdAngle ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		
		for (int i = 0 ; i <= numberItems - 1 ; i += 1)
		{
			optionPos.y += sy ;
			Point actionKeyPos = UtilG.Translate(optionPos, sx, 0) ;
			checkMouseSelection(mousePos, optionPos, Align.bottomLeft, new Dimension(100, 10), i) ;
			Color textColor = getTextColor(item == i) ;
			DP.DrawText(optionPos, Align.bottomLeft, angle, text[i], font, textColor) ;
			
			if (i == 3)
			{
				displayValue(actionKeyPos, keyIsOn[i], String.valueOf(getAttDisplay()), angle, DP) ;
				continue ;
			}
			if (i == 4)
			{
				displayValue(actionKeyPos, keyIsOn[i], String.valueOf(getDamageAnimation()), angle, DP) ;
				continue ;
			}

			if (i == 5)
			{
				continue ;
			}
			
			displayValue(actionKeyPos, keyIsOn[i], null, angle, DP) ;
		}				
		
	}
	
	private void displayMenu1(Point mousePos, String[] text, DrawingOnPanel DP)
	{
		Point optionPos = UtilG.Translate(windowPos, 25, 42) ;
		double angle = DrawingOnPanel.stdAngle ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		
		for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
		{
			optionPos.y += sy ;
			Point actionKeyPos = UtilG.Translate(optionPos, sx, 0) ;
			checkMouseSelection(mousePos, optionPos, Align.bottomLeft, new Dimension(100, 10), i) ;
			Color textColor = getTextColor(item == i) ;
			DP.DrawText(optionPos, Align.bottomLeft, angle, text[i + 6], font, textColor) ;
			DP.DrawText(actionKeyPos, Align.bottomCenter, angle, Player.ActionKeys[i], font, Game.colorPalette[5]) ;			
		}
		if (selectedActionKeyID <= -1) { return ;}

		Point actionKeyPos = new Point(optionPos.x + sx, optionPos.y + (selectedActionKeyID + 1)*sy) ;
		DP.DrawText(actionKeyPos, Align.bottomCenter, angle, Player.ActionKeys[selectedActionKeyID], font, Game.colorPalette[3]) ;
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		Point textPos = UtilG.Translate(windowPos, 25, 42) ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		Image menuImage = menu == 0 ? image : deeperMenuImage ;
		String[] text = Game.allText.get(TextCategories.settings) ;
		Color[] textColor = new Color[3 + Player.ActionKeys.length] ;
		Arrays.fill(textColor, Game.colorPalette[0]) ;
		textColor[item] = Game.colorPalette[18] ;
		
		DP.DrawImage(menuImage, windowPos, Align.topLeft) ;
		Point titlePos = UtilG.Translate(textPos, image.getWidth(null) / 2 - 15, -6) ;
		DP.DrawText(titlePos, Align.bottomCenter, angle, "Opções", font, Game.colorPalette[0]) ;
		if (menu == 0)
		{
			displayMenu0(mousePos, text, DP) ;
			
			return ;
		}
		
		if (menu == 1 | menu == 2)
		{
			displayMenu1(mousePos, text, DP) ;
		}
	}
}
