package windows;

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
	private boolean isInvested ;
	private String investmentRisk ;
	private TimeCounter investmentCounter ;
	private LiveInput liveInput ;
	
	private static final String[] investmentRiskLevels = new String[] {"low", "high"} ;
	public static final Image clock = UtilG.loadImage(Game.ImagesPath + "\\NPCs\\" + "clock.png") ;
	// TODO add avisos max deposit, withdraw and investment
	public BankWindow()
	{
		super("Banco", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Banco.png"), 1, 1, 1, 1) ;
		windowPos = Game.getScreen().getPoint(0.4, 0.2) ;
		mode = "" ;
		amountTyped = 0 ;
		balance = 0 ;
		investedAmount = 0 ;
		isInvested = false ;
		investmentCounter = new TimeCounter(0, 10000) ;
		liveInput = new LiveInput() ;
	}

	
	public int getAmountTyped() { return amountTyped ;}
	public int getBalance() { return balance;}
	public TimeCounter getInvestmentCounter() { return investmentCounter ;}
	public void setMode(String mode) { this.mode = mode ;}
	
	private boolean isReadingInput() { return mode.equals("deposit") | mode.equals("withdraw") | mode.equals("investment low risk") | mode.equals("investment hight risk") ;}
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
		isInvested = true ;
		investedAmount += amount ;
		bag.removeGold(amount) ;
	}
	

	//	public void displayInput(String message, String action, DrawingOnPanel DP)
//	{
//		Point pos = UtilG.Translate(windowPos, 50, size.height / 3) ;
//		DP.DrawText(UtilG.Translate(pos, 0, -30), Align.centerLeft, DrawingOnPanel.stdAngle, message, stdFont, Game.colorPalette[9]) ;
//		DP.DrawRoundRect(pos, Align.centerLeft, new Dimension(150, 20), 1, Game.colorPalette[7], Game.colorPalette[7], true) ;
//		DP.DrawImage(Player.CoinIcon, UtilG.Translate(pos, 5, 0), Align.centerLeft) ;
//		DP.DrawText(UtilG.Translate(pos, 20, 0), Align.centerLeft, DrawingOnPanel.stdAngle, String.valueOf(amountTyped), stdFont, Game.colorPalette[9]) ;
//	}

	public void readValue(String action)
	{
		
		if (!UtilG.isNumeric(action) & !action.equals("Backspace")) { return ;}

		liveInput.receiveInput(action) ;
//		System.out.println("reading = " + liveInput.getText()) ;
		
	}
	
	private void drawInvestmentTimer(Point pos, double timeRate, DrawingOnPanel DP)
	{
		DP.DrawImage(clock, pos, Align.center) ;
		DP.DrawArc(pos, 16, 1, 90, (int) (-360 * timeRate), Game.colorPalette[2], null) ; // TODO investment timer animation
	}
	
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, border + 10) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;

		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.colorPalette[2]) ;
		
		Point balancePos = UtilG.Translate(windowPos, border + padding + 4, (int) border + 30) ;
		Point investmentPos = UtilG.Translate(windowPos, border + padding + 4, border + 90) ;
		
		DP.DrawText(balancePos, Align.centerLeft, angle, "Saldo", stdFont, Game.colorPalette[9]) ;
		DP.DrawText(investmentPos, Align.centerLeft, angle, "Investimento", stdFont, Game.colorPalette[9]) ;
		
		drawInvestmentTimer(UtilG.Translate(investmentPos, 110, 2), investmentCounter.rate(), DP) ;

		DP.DrawImage(Player.CoinIcon, UtilG.Translate(balancePos, 0, 20), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(balancePos, 15, 20), Align.centerLeft, angle, String.valueOf(balance), stdFont, Game.colorPalette[9]) ;
		DP.DrawImage(Player.CoinIcon, UtilG.Translate(investmentPos, 0, 20), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(investmentPos, 15, 20), Align.centerLeft, angle, String.valueOf(investedAmount), stdFont, Game.colorPalette[9]) ;
		
		if (!isReadingInput()) { return ;}

		Point inputPos = UtilG.Translate(windowPos, border + padding + 4, border - 30) ;
		liveInput.displayTypingField(inputPos, DP) ;
	}

}
