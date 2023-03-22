package windows;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.Typing;
import utilities.UtilG;

public class BankWindow extends GameWindow
{
	private Point windowPos ;
	private int amountTyped ;
	private int balance ;
	private boolean isInvested ;
	private TimeCounter investmentCounter ;
	
	public BankWindow()
	{
		super("Bank", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Bank.png"), 1, 1, 1, 1) ;
		windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		amountTyped = 0 ;
		balance = 0 ;
		isInvested = false ;
		investmentCounter = new TimeCounter(0, 1000) ;	// TODO where to inc this counter?
	}

	
	
	public int getAmountTyped() { return amountTyped ;}
	public int getBalance() { return balance;}
	public boolean isInvested() { return isInvested;}
	public boolean investmentIsComplete() { return investmentCounter.finished() ;}
	
	public void restartInvestment() { investmentCounter.reset() ;}


	public void navigate(String action)
	{

	}
	
	public void updateValueInvested()
	{
		double rate = Math.random() <= 0.8 ? 1.05 : 0.95 ;
		balance = (int) (balance * rate) ;
	}

	public void deposit(BagWindow bag, int amount)
	{
		if (!bag.hasEnoughGold(amount)) { return ;}
		
		balance += amount ;
		bag.removeGold(amount) ;
	}

	public void withdraw(BagWindow bag, int amount)
	{
		int amountWithFee = (int) (1.01 * amount) ;
		
		if (balance < amountWithFee) { return ;}
		
		balance += -amountWithFee ;
		bag.addGold(amount) ;
	}
	
	public void displayInput(String message, String action, DrawingOnPanel DP)
	{
		Point pos = UtilG.Translate(windowPos, 50, size.height / 3) ;
		DP.DrawText(UtilG.Translate(pos, 0, -30), Align.centerLeft, DrawingOnPanel.stdAngle, message, stdFont, Game.ColorPalette[9]) ;
		DP.DrawRoundRect(pos, Align.centerLeft, new Dimension(150, 20), 1, Game.ColorPalette[7], Game.ColorPalette[7], true) ;
		DP.DrawImage(Player.CoinIcon, UtilG.Translate(pos, 5, 0), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(pos, 20, 0), Align.centerLeft, DrawingOnPanel.stdAngle, String.valueOf(amountTyped), stdFont, Game.ColorPalette[9]) ;
	}

	public int readValue(String action, DrawingOnPanel DP)
	{
		Point pos = UtilG.Translate(windowPos, 50 + 20, size.height / 3) ;
		String text = Typing.LiveTyping(pos, DrawingOnPanel.stdAngle, action, stdFont, Game.ColorPalette[5], DP) ;
		
		if (!UtilG.isNumeric(text)) { return 0 ;}
		
		if (1 <= text.length()) { amountTyped = Integer.parseInt(text) ; return amountTyped ;}
		
		return 0 ;
	}
	
	public void display(DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		Point balancePos = UtilG.Translate(windowPos, 50, (int) (0.8 * size.height)) ;
		DP.DrawImage(Player.CoinIcon, balancePos, Align.center) ;
		DP.DrawText(balancePos, Align.centerLeft, DrawingOnPanel.stdAngle, String.valueOf(balance), stdFont, Game.ColorPalette[9]) ;
	}

}
