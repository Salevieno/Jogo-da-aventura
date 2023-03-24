package windows;

import java.awt.Dimension;
import java.awt.Image;
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
	private int investedAmount ;
	private boolean isInvested ;
	private String investmentRisk ;
	private TimeCounter investmentCounter ;
	
	private static final String[] investmentRiskLevels = new String[] {"low", "high"} ;
	public static final Image clock = UtilG.loadImage(Game.ImagesPath + "\\NPCs\\" + "clock.png") ;
	
	public BankWindow()
	{
		super("Banco", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Banco.png"), 1, 1, 1, 1) ;
		windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		amountTyped = 0 ;
		balance = 0 ;
		investedAmount = 0 ;
		isInvested = false ;
		investmentCounter = new TimeCounter(0, 10000) ;
	}

	
	
	public int getAmountTyped() { return amountTyped ;}
	public int getBalance() { return balance;}
	public TimeCounter getInvestmentCounter() { return investmentCounter ;}

	public boolean isInvested() { return isInvested;}
	public boolean investmentIsComplete() { return investmentCounter.finished() ;}
	
	public void incInvestmentCounter() { investmentCounter.inc() ;}
	public void completeInvestment()
	{
		double rate = investmentRisk.equals(investmentRiskLevels[0]) ? Math.random() <= 0.95 ? 1.05 : 0.95 : Math.random() <= 0.6 ? 1.2 : 0.9 ;
		
		investedAmount = (int) (investedAmount * rate) ;
		balance += investedAmount ;
		investedAmount = 0 ;
		investmentCounter.reset() ;
		isInvested = false ;
	}
	
	public void navigate(String action)
	{

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
		bag
		.addGold(amount) ;
	}
	
	public void invest(BagWindow bag, int amount, boolean highRisk)
	{
		if (!bag.hasEnoughGold(amount)) { return ;}
		
		investmentRisk = highRisk ? investmentRiskLevels[1] : investmentRiskLevels[0] ;
		isInvested = true ;
		investedAmount += amount ;
		bag.removeGold(amount) ;
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
	
	private void drawInvestmentTimer(Point pos, double timeRate, DrawingOnPanel DP)
	{
		DP.DrawImage(clock, pos, Align.center) ;
		DP.DrawArc(pos, 16, 1, 90, (int) (-360 * timeRate), Game.ColorPalette[2], null) ;
	}
	
	public void display(DrawingOnPanel DP)
	{
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, border + 10) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;

		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.ColorPalette[2]) ;
		
		Point balancePos = UtilG.Translate(windowPos, border + padding + 4, (int) border + 30) ;
		Point investmentPos = UtilG.Translate(windowPos, border + padding + 4, border + 90) ;
		
		DP.DrawText(balancePos, Align.centerLeft, DrawingOnPanel.stdAngle, "Saldo", stdFont, Game.ColorPalette[9]) ;
		DP.DrawText(investmentPos, Align.centerLeft, DrawingOnPanel.stdAngle, "Investimento", stdFont, Game.ColorPalette[9]) ;
		
		drawInvestmentTimer(UtilG.Translate(investmentPos, 110, 2), investmentCounter.rate(), DP) ;

		DP.DrawImage(Player.CoinIcon, UtilG.Translate(balancePos, 0, 20), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(balancePos, 15, 20), Align.centerLeft, DrawingOnPanel.stdAngle, String.valueOf(balance), stdFont, Game.ColorPalette[9]) ;
		DP.DrawImage(Player.CoinIcon, UtilG.Translate(investmentPos, 0, 20), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(investmentPos, 15, 20), Align.centerLeft, DrawingOnPanel.stdAngle, String.valueOf(investedAmount), stdFont, Game.ColorPalette[9]) ;
		
	}

}
