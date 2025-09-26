package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.GameTimer;
import main.LiveInput;
import main.Path;
import utilities.Util;


public class BankWindow extends GameWindow
{
	private String mode ;
	private int amountTyped ;
	private int balance ;
	private int investedAmount ;
	private boolean hasInvestement ;
	private String investmentRisk ;
	private GameTimer investmentCounter ;
	private LiveInput liveInput ;

	private static final Point windowPos = Game.getScreen().pos(0.4, 0.2) ;
	private static final Image image = Game.loadImage(Path.WINDOWS_IMG + "Banco.png") ;
	private static final String[] investmentRiskLevels = new String[] {"low", "high"} ;
	public static final Image clock = Game.loadImage(Path.WINDOWS_IMG + "clock.png") ;

	public BankWindow()
	{
		super("Banco", windowPos, image, 1, 1, 1, 1) ;
		mode = "" ;
		amountTyped = 0 ;
		balance = 0 ;
		investedAmount = 0 ;
		hasInvestement = false ;
		investmentCounter = new GameTimer(50) ;
		liveInput = new LiveInput() ;
	}

	
	public int getAmountTyped() { return amountTyped ;}
	public int getBalance() { return balance;}
	public GameTimer getInvestmentCounter() { return investmentCounter ;}
	public void setMode(String mode) { this.mode = mode ;}
	
	private boolean isReadingInput() { return mode.equals("deposit") | mode.equals("withdraw") | mode.equals("investment low risk") | mode.equals("investment hight risk") ;}
	public boolean hasInvestment() { return hasInvestement;}
	public boolean investmentIsComplete() { return investmentCounter.hasFinished() ;}
	
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
			switch(mode)
			{
				case "deposit": deposit(bag, amountTyped) ; return ;
				case "withdraw": withdraw(bag, amountTyped) ; return ;
				case "investment low risk": invest(bag, amountTyped, false) ; return ;
				case "investment hight risk": invest(bag, amountTyped, true) ; return ;
				default: return ;
			}
		}
		
	}

	public void deposit(BagWindow bag, int amount)
	{
		if (!bag.hasEnoughGold(amount)) { displayNotEnoughGold() ; return ;}
		
		balance += amount ;
		bag.removeGold(amount) ;
	}

	public void withdraw(BagWindow bag, int amount)
	{
		int amountWithFee = (int) (1.01 * amount) ;
		
		if (balance < amountWithFee) { displayNotEnoughGold() ; return ;}
		
		balance += -amountWithFee ;
		bag.addGold(amount) ;
	}
	
	public void invest(BagWindow bag, int amount, boolean highRisk)
	{
		if (!bag.hasEnoughGold(amount)) { displayNotEnoughGold() ; return ;}
		
		investmentRisk = highRisk ? investmentRiskLevels[1] : investmentRiskLevels[0] ;
		hasInvestement = true ;
		investmentCounter.start() ;
		investedAmount += amount ;
		bag.removeGold(amount) ;
	}
	
	public void readValue(String action)
	{
		
		if (!Util.isNumeric(action) & !action.equals("Backspace")) { return ;}

		liveInput.receiveInput(action) ;
		
	}
	
	private void drawInvestmentTimer(Point pos, double timeRate)
	{
		GamePanel.DP.drawImage(clock, pos, Align.center) ;
		GamePanel.DP.drawArc(Util.translate(pos, 0, 2), 21, 1, 90, (int) (-360 * timeRate), Game.palette[20], null) ;
	}
	
	private void displayNotEnoughGold()
	{
		Point msgPos = Game.getScreen().pos(0.4, 0.3) ;
		String msg = "Você não tem ouro suficiente!" ;
		Color msgColor = Game.palette[0] ;
		Animation.start(AnimationTypes.message, new Object[] {msgPos, msg, msgColor}) ;
	}
	
	public void display(Point mousePos)
	{
		Point titlePos = Util.translate(windowPos, size.width / 2, border + 10) ;
		Color textColor = Game.palette[0] ;
		double angle = Draw.stdAngle ;
		
		GamePanel.DP.drawImage(image, windowPos, angle, Scale.unit, Align.topLeft) ;

		GamePanel.DP.drawText(titlePos, Align.center, angle, name, titleFont, Game.palette[0]) ;
		
		Point balancePos = Util.translate(windowPos, border + padding + 4, (int) border + 30) ;
		Point investmentPos = Util.translate(windowPos, border + padding + 4, border + 90) ;
		
		GamePanel.DP.drawText(balancePos, Align.centerLeft, angle, "Saldo", stdFont, textColor) ;
		GamePanel.DP.drawText(investmentPos, Align.centerLeft, angle, "Investimento", stdFont, textColor) ;
		
		drawInvestmentTimer(Util.translate(investmentPos, 110, 10), investmentCounter.rate()) ;

		GamePanel.DP.drawImage(Player.getCoinImg(), Util.translate(balancePos, 0, 20), Align.centerLeft) ;
		GamePanel.DP.drawText(Util.translate(balancePos, 15, 20), Align.centerLeft, angle, String.valueOf(balance), stdFont, textColor) ;
		GamePanel.DP.drawImage(Player.getCoinImg(), Util.translate(investmentPos, 0, 20), Align.centerLeft) ;
		GamePanel.DP.drawText(Util.translate(investmentPos, 15, 20), Align.centerLeft, angle, String.valueOf(investedAmount), stdFont, textColor) ;
		
		if (!isReadingInput()) { return ;}

		Point inputMessagePos = Util.translate(windowPos, 0, border + size.height + 15) ;
		GamePanel.DP.drawText(inputMessagePos, Align.centerLeft, angle, "Amount for " + mode, stdFont, textColor) ;
		
		Point inputPos = Util.translate(windowPos, 0, border + size.height + 35) ;
		liveInput.displayTypingField(inputPos, true) ;
		GamePanel.DP.drawImage(Player.getCoinImg(), Util.translate(inputPos, 5, 0), Align.centerLeft) ;
	}

}
