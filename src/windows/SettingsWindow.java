package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import graphics.Draw;
import graphics.DrawPrimitives;
import liveBeings.PlayerActions;
import main.Battle;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.UtilG;
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
			if (action.equals(stdWindowDown))
			{
				itemUp() ;
			}
			if (action.equals(stdWindowUp))
			{
				itemDown() ;
			}
			if (action.equals(stdMenuUp))
			{
				windowUp() ;
			}
			if (action.equals(stdMenuDown))
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
			Battle.updateDamageAnimation(damageAnimation) ;
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
	
	public void displayValue(Point textPos, boolean selected, String text, double angle, DrawPrimitives DP)
	{
		if (text == null)
		{
			text = selected ? "On" : "Off" ;
		}
			
		Color textColor = selected ? Game.colorPalette[20] : Game.colorPalette[2] ;

		DP.drawText(textPos, Align.bottomCenter, angle, text, font, textColor) ;	
	}
	
	
	private void displayMenu0(Point mousePos, String[] text, DrawPrimitives DP)
	{
		
		boolean[] keyIsOn = new boolean[] {musicIsOn, soundEffectsAreOn, showAtkRange, false, false} ;
		Point optionPos = UtilG.Translate(windowPos, 25, 42) ;
		double angle = Draw.stdAngle ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		
		for (int i = 0 ; i <= numberItems - 1 ; i += 1)
		{
			optionPos.y += sy ;
			Point actionKeyPos = UtilG.Translate(optionPos, sx, 0) ;
			checkMouseSelection(mousePos, optionPos, Align.bottomLeft, new Dimension(100, 10), i) ;
			Color textColor = getTextColor(item == i) ;
			DP.drawText(optionPos, Align.bottomLeft, angle, text[i], font, textColor) ;
			
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
	
	private void displayMenu1(Point mousePos, String[] text, DrawPrimitives DP)
	{
		Point optionPos = UtilG.Translate(windowPos, 25, 42) ;
		double angle = Draw.stdAngle ;
		int sx = image.getWidth(null) - 45 ;
		int sy = font.getSize() + 4 ;
		
		for (int i = 0 ; i <= PlayerActions.values().length - 1 ; i += 1)
		{
			optionPos.y += sy ;
			Point actionKeyPos = UtilG.Translate(optionPos, sx, 0) ;
			checkMouseSelection(mousePos, optionPos, Align.bottomLeft, new Dimension(100, 10), i) ;
			Color textColor = getTextColor(item == i) ;
			DP.drawText(optionPos, Align.bottomLeft, angle, text[i + 6], font, textColor) ;
			DP.drawText(actionKeyPos, Align.bottomCenter, angle, PlayerActions.values()[i].getKey(), font, Game.colorPalette[5]) ;			
		}
		if (selectedActionKeyID <= -1) { return ;}

		Point actionKeyPos = new Point(optionPos.x + sx, optionPos.y + (selectedActionKeyID + 1)*sy) ;
		DP.drawText(actionKeyPos, Align.bottomCenter, angle, PlayerActions.values()[selectedActionKeyID].getKey(), font, Game.colorPalette[3]) ;
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		double angle = Draw.stdAngle ;
		Point textPos = UtilG.Translate(windowPos, 25, 42) ;
		Image menuImage = menu == 0 ? image : imageMenu1 ;
		String[] text = Game.allText.get(TextCategories.settings) ;
		Color[] textColor = new Color[3 + PlayerActions.values().length] ;
		Arrays.fill(textColor, Game.colorPalette[0]) ;
		textColor[item] = Game.colorPalette[18] ;
		
		DP.drawImage(menuImage, windowPos, Align.topLeft) ;
		Point titlePos = UtilG.Translate(textPos, image.getWidth(null) / 2 - 15, -6) ;
		DP.drawText(titlePos, Align.bottomCenter, angle, name, font, Game.colorPalette[0]) ;
		if (menu == 0)
		{
			numberItems = 6 ;
			displayMenu0(mousePos, text, DP) ;
			
			return ;
		}
		
		if (menu == 1 | menu == 2)
		{
			numberItems = PlayerActions.values().length ;
			displayMenu1(mousePos, text, DP) ;
		}
	}
}
