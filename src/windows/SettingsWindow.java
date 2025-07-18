package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import graphics.Align;
import graphics2.Draw;
import liveBeings.LiveBeing;
import liveBeings.PlayerActions;
import main.Game;
import main.GamePanel;
import main.TextCategories;
import utilities.Util;
import utilities.UtilS;

public class SettingsWindow extends GameWindow
{
	private boolean musicIsOn ;
	private boolean soundEffectsAreOn ;
	private boolean showAtkRange ;
	private int attDisplay ;
	private int damageAnimation ;
	private int selectedActionKeyID ;
	
	
	private static final Point windowPos ;
	private static final Font font ;
    public final static Image imageMenu0 ;
	private static final Image imageMenu1 ;
	
	static
	{
		windowPos = Game.getScreen().pos(0.4, 0.35) ;
		font = new Font(Game.MainFontName, Font.BOLD, 13) ;
	    imageMenu0 = UtilS.loadImage("\\Windows\\" + "windowSettings.png") ;
		imageMenu1 = UtilS.loadImage("\\Windows\\" + "settingsDeeperWindow.png") ;
	}
	
	public SettingsWindow(boolean musicIsOn, boolean soundEffectsAreOn, boolean showAtkRange, int attDisplay, int damageAnimation)
	{
		super("Opções", windowPos, imageMenu0, 3, 0, 6, 0) ;
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
			if (action.equals(stdMenuDown))
			{
				itemUp() ;
			}
			if (action.equals(stdMenuUp))
			{
				itemDown() ;
			}
			if (actionIsForward(action) | action.equals(stdWindowUp))
			{
				updateSetting() ;
			}
			if (action.equals(stdExit))
			{
				close() ;
			}
		}
		else if (getMenu() == 1)
		{
			if (action.equals(stdMenuDown))
			{
				itemUp() ;
			}
			if (action.equals(stdMenuUp))
			{
				itemDown() ;
			}
			if (action.equals(stdWindowUp))
			{
				windowUp() ;
			}
			if (action.equals(stdWindowDown))
			{
				windowDown() ;
			}
			if (actionIsForward(action))
			{
				selectActionKey() ;
			}
			if (action.equals(stdExit))
			{
				menu = 0 ;
				numberItems = 6 ;
				item = 5;
			}
		}
		else if (getMenu() == 2)
		{
			if (!actionIsForward(action))
			{
				updateActionKey(action) ;
				selectedActionKeyID = -1 ;
				menu = 1 ;
			}
			if (action.equals(stdExit))
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
			LiveBeing.updateDamageAnimation(damageAnimation) ;
		}
		if (item == 5)
		{
			menu = 1 ;
			item = 0 ;
			numberItems = PlayerActions.values().length ;
		}
	}
	
	public void selectActionKey()
	{
		selectedActionKeyID = item ;
		menu = 2 ;
	}
	
	public void updateActionKey(String action)
	{
		PlayerActions.values()[item].setKey(action) ;
	}
	
	public void displayValue(Point textPos, boolean selected, String text, double angle)
	{
		if (text == null)
		{
			text = selected ? "On" : "Off" ;
		}
			
		Color textColor = selected ? Game.palette[20] : Game.palette[2] ;

		GamePanel.DP.drawText(textPos, Align.bottomCenter, angle, text, font, textColor) ;	
	}
	
	
	private void displayMenu0(Point mousePos, String[] text)
	{
		
		boolean[] keyIsOn = new boolean[] {musicIsOn, soundEffectsAreOn, showAtkRange, false, false} ;
		Point optionPos = Util.Translate(windowPos, 25, 42) ;
		double angle = Draw.stdAngle ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		
		for (int i = 0 ; i <= numberItems - 1 ; i += 1)
		{
			optionPos.y += sy ;
			Point actionKeyPos = Util.Translate(optionPos, sx, 0) ;
			checkMouseSelection(mousePos, optionPos, Align.bottomLeft, new Dimension(100, 10), i) ;
			Color textColor = getTextColor(item == i) ;
			GamePanel.DP.drawText(optionPos, Align.bottomLeft, angle, text[i], font, textColor) ;
			
			if (i == 3)
			{
				displayValue(actionKeyPos, keyIsOn[i], String.valueOf(getAttDisplay()), angle) ;
				continue ;
			}
			if (i == 4)
			{
				displayValue(actionKeyPos, keyIsOn[i], String.valueOf(getDamageAnimation()), angle) ;
				continue ;
			}

			if (i == 5)
			{
				continue ;
			}
			
			displayValue(actionKeyPos, keyIsOn[i], null, angle) ;
		}				
		
	}
	
	private void displayMenu1(Point mousePos, String[] text)
	{
		Point optionPos = Util.Translate(windowPos, 25, 42) ;
		double angle = Draw.stdAngle ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		
		for (int i = 0 ; i <= PlayerActions.values().length - 1 ; i += 1)
		{
			optionPos.y += sy ;
			Point actionKeyPos = Util.Translate(optionPos, sx, 0) ;
			checkMouseSelection(mousePos, optionPos, Align.bottomLeft, new Dimension(100, 10), i) ;
			Color textColor = getTextColor(item == i) ;
			GamePanel.DP.drawText(optionPos, Align.bottomLeft, angle, text[i + 6], font, textColor) ;
			GamePanel.DP.drawText(actionKeyPos, Align.bottomCenter, angle, PlayerActions.values()[i].getKey(), font, getTextColor(selectedActionKeyID == i)) ;			
		}
	}
	
	public void display(Point mousePos)
	{
		double angle = Draw.stdAngle ;
		Point textPos = Util.Translate(windowPos, 25, 42) ;
		Image menuImage = menu == 0 ? image : imageMenu1 ;
		String[] text = Game.allText.get(TextCategories.settings) ;
		Color[] textColor = new Color[3 + PlayerActions.values().length] ;
		Arrays.fill(textColor, Game.palette[0]) ;
		textColor[item] = Game.palette[18] ;
		
		GamePanel.DP.drawImage(menuImage, windowPos, Align.topLeft) ;
		Point titlePos = Util.Translate(textPos, image.getWidth(null) / 2 - 15, -6) ;
		GamePanel.DP.drawText(titlePos, Align.bottomCenter, angle, name, font, Game.palette[0]) ;
		if (menu == 0)
		{
			numberItems = 6 ;
			displayMenu0(mousePos, text) ;
			
			return ;
		}
		
		if (menu == 1 | menu == 2)
		{
			numberItems = PlayerActions.values().length ;
			displayMenu1(mousePos, text) ;
		}
	}
}
