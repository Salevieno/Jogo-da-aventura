package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.LiveInput;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;

public class BankWindow extends GameWindow
{
	private Point windowPos ;
	private String mode ;
	private int amountTyped ;
	private int balance ;
	private int investedAmount ;
	private boolean hasInvestement ;
	private String investmentRisk ;
	private TimeCounter investmentCounter ;
	private LiveInput liveInput ;
	
	private static final String[] investmentRiskLevels = new String[] {"low", "high"} ;
	public static final Image clock = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "clock.png") ;
	// TODO add avisos max deposit, withdraw and investment
	public BankWindow()
	{
		super("Banco", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Banco.png"), 1, 1, 1, 1) ;
		windowPos = Game.getScreen().pos(0.4, 0.2) ;
		mode = "" ;
		amountTyped = 0 ;
		balance = 0 ;
		investedAmount = 0 ;
		hasInvestement = false ;
		investmentCounter = new TimeCounter(0, 10000) ;
		liveInput = new LiveInput() ;
	}

	
	public int getAmountTyped() { return amountTyped ;}
	public int getBalance() { return balance;}
	public TimeCounter getInvestmentCounter() { return investmentCounter ;}
	public void setMode(String mode) { this.mode = mode ;}
	
	private boolean isReadingInput() { return mode.equals("deposit") | mode.equals("withdraw") | mode.equals("investment low risk") | mode.equals("investment hight risk") ;}
	public boolean hasInvestment() { return hasInvestement;}
	public boolean investmentIsComplete() { return investmentCounter.finished() ;}
	
	public void incInvestmentCounter() { investmentCounter.inc() ;}
	public void completeInvestment()
	{
		double rate = investmentRisk.equals(investmentRiskLevels[0]) ? Math.random() <= 0.95 ? 1.05 : 0.95 : Math.random() <= 0.6 ? 1.2 : 0.9 ;
		
		investedAmount = (int) (investedAmount * rate) ;
		balance += investedAmount ;
		investedAmount = 0 ;
		investmentCounter.reset() ;
		hasInvestement = false ;
	}
	
	public void navigate(String action)
	{

	}
	
	public void act(BagWindow bag, String action)
	{
		
		if (action == null) { return ;}
		
		if (isReadingInput())
		{
			readValue(action) ;
		}
		if (action.equals("Enter") & !liveInput.getText().isEmpty())
		{
			amountTyped = Integer.parseInt(liveInput.getText()) ;
			liveInput.clearText() ;
			if (mode.equals("deposit"))
			{
				deposit(bag, amountTyped) ;
			}
			if (mode.equals("withdraw"))
			{
				withdraw(bag, amountTyped) ;
			}
			if (mode.equals("investment low risk"))
			{
				invest(bag, amountTyped, false) ;
			}
			if (mode.equals("investment hight risk"))
			{
				invest(bag, amountTyped, true) ;
			}
		}
		
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
	
	public void invest(BagWindow bag, int amount, boolean highRisk)
	{
		if (!bag.hasEnoughGold(amount)) { return ;}
		
		investmentRisk = highRisk ? investmentRiskLevels[1] : investmentRiskLevels[0] ;
		hasInvestement = true ;
		investedAmount += amount ;
		bag.removeGold(amount) ;
	}
	
	public void readValue(String action)
	{
		
		if (!UtilG.isNumeric(action) & !action.equals("Backspace")) { return ;}

		liveInput.receiveInput(action) ;
		
	}
	
	private void drawInvestmentTimer(Point pos, double timeRate, DrawingOnPanel DP)
	{
		DP.DrawImage(clock, pos, Align.center) ;
		DP.DrawArc(UtilG.Translate(pos, 0, 2), 21, 1, 90, (int) (-360 * timeRate), Game.colorPalette[20], null) ;
	}
	
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, border + 10) ;
		Color textColor = Game.colorPalette[0] ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, angle, Scale.unit, Align.topLeft) ;

		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.colorPalette[0]) ;
		
		Point balancePos = UtilG.Translate(windowPos, border + padding + 4, (int) border + 30) ;
		Point investmentPos = UtilG.Translate(windowPos, border + padding + 4, border + 90) ;
		
		DP.DrawText(balancePos, Align.centerLeft, angle, "Saldo", stdFont, textColor) ;
		DP.DrawText(investmentPos, Align.centerLeft, angle, "Investimento", stdFont, textColor) ;
		
		drawInvestmentTimer(UtilG.Translate(investmentPos, 110, 10), investmentCounter.rate(), DP) ;

		DP.DrawImage(Player.CoinIcon, UtilG.Translate(balancePos, 0, 20), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(balancePos, 15, 20), Align.centerLeft, angle, String.valueOf(balance), stdFont, textColor) ;
		DP.DrawImage(Player.CoinIcon, UtilG.Translate(investmentPos, 0, 20), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(investmentPos, 15, 20), Align.centerLeft, angle, String.valueOf(investedAmount), stdFont, textColor) ;
		
		if (!isReadingInput()) { return ;}

		Point inputMessagePos = UtilG.Translate(windowPos, 0, border + size.height + 15) ;
		DP.DrawText(inputMessagePos, Align.centerLeft, angle, "Amount for " + mode, stdFont, textColor) ;
		
		Point inputPos = UtilG.Translate(windowPos, 0, border + size.height + 35) ;
		liveInput.displayTypingField(inputPos, true, DP) ;
		DP.DrawImage(Player.CoinIcon, UtilG.Translate(inputPos, 5, 0), Align.centerLeft) ;
	}

}
