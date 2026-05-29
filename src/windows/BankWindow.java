package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import animations.MessageAnimation;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import liveBeings.Player;
import main.GamePanel;
import main.GameTimer;
import main.ImageLoader;
import main.LiveInput;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class BankWindow extends GameWindow
{
	private BankAction mode ;
	private int amountTyped ;
	private int balance ;
	private int investedAmount ;
	private boolean hasInvestement ;
	private String investmentRisk ;
	private GameTimer investmentCounter ;
	private LiveInput liveInput ;

	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Banco.png") ;
	private static final String[] INVESTMENT_RISK_LEVELS = new String[] {"low", "high"} ;
	private static final Image CLOCK_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "clock.png") ;
// TODO investment
	public BankWindow()
	{
		super("Banco", Screen.getMe().pos(0.4, 0.2), IMAGE, 1, 1, 1, 1) ;
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
	public void setMode(BankAction mode) { this.mode = mode ;}
	
	private boolean isReadingInput() { return mode.equals(BankAction.deposit) || mode.equals(BankAction.withdraw) || mode.equals(BankAction.investmentLowRisk) || mode.equals(BankAction.investmentHighRisk) ;}
	public boolean hasInvestment() { return hasInvestement;}
	public boolean investmentIsComplete() { return investmentCounter.hasFinished() ;}
	
	public void completeInvestment()
	{
		double rate = investmentRisk.equals(INVESTMENT_RISK_LEVELS[0]) ? Math.random() <= 0.95 ? 1.05 : 0.95 : Math.random() <= 0.6 ? 1.2 : 0.9 ;
		
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
				case deposit: deposit(bag, amountTyped) ; return ;
				case withdraw: withdraw(bag, amountTyped) ; return ;
				case investmentLowRisk: invest(bag, amountTyped, false) ; return ;
				case investmentHighRisk: invest(bag, amountTyped, true) ; return ;
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
		
		investmentRisk = highRisk ? INVESTMENT_RISK_LEVELS[1] : INVESTMENT_RISK_LEVELS[0] ;
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
		GamePanel.getDP().drawImage(CLOCK_IMAGE, pos, Align.center) ;
		GamePanel.getDP().drawArc(Util.translate(pos, 0, 2), 21, 1, 90, (int) (-360 * timeRate), Palette.colors[20], null) ;
	}
	
	private void displayNotEnoughGold()
	{
		Point msgPos = Screen.getMe().pos(0.4, 0.3) ;
		String msg = "Você não tem ouro suficiente!" ;
		Color msgColor = Palette.colors[0] ;
		MessageAnimation.start(msgPos, msg, msgColor) ;
	}
	
	public void display(Point mousePos)
	{
		Point titlePos = Util.translate(topLeftPos, size.width / 2, BORDER + 10) ;
		Color textColor = Palette.colors[0] ;
		double angle = Draw.stdAngle ;
		
		GamePanel.getDP().drawImage(image, topLeftPos, angle, Scale.unit, Align.topLeft) ;

		GamePanel.getDP().drawText(titlePos, Align.center, angle, name, TITLE_FONT, Palette.colors[0]) ;
		
		Point balancePos = Util.translate(topLeftPos, BORDER + PADDING + 4, (int) BORDER + 30) ;
		Point investmentPos = Util.translate(topLeftPos, BORDER + PADDING + 4, BORDER + 90) ;
		
		GamePanel.getDP().drawText(balancePos, Align.centerLeft, angle, "Saldo", STD_FONT, textColor) ;
		GamePanel.getDP().drawText(investmentPos, Align.centerLeft, angle, "Investimento", STD_FONT, textColor) ;
		
		drawInvestmentTimer(Util.translate(investmentPos, 110, 10), investmentCounter.rate()) ;

		GamePanel.getDP().drawImage(Player.getCoinImg(), Util.translate(balancePos, 0, 20), Align.centerLeft) ;
		GamePanel.getDP().drawText(Util.translate(balancePos, 15, 20), Align.centerLeft, angle, String.valueOf(balance), STD_FONT, textColor) ;
		GamePanel.getDP().drawImage(Player.getCoinImg(), Util.translate(investmentPos, 0, 20), Align.centerLeft) ;
		GamePanel.getDP().drawText(Util.translate(investmentPos, 15, 20), Align.centerLeft, angle, String.valueOf(investedAmount), STD_FONT, textColor) ;
		
		if (!isReadingInput()) { return ;}

		Point inputMessagePos = Util.translate(topLeftPos, 0, BORDER + size.height + 15) ;
		GamePanel.getDP().drawText(inputMessagePos, Align.centerLeft, angle, "Amount for " + mode, STD_FONT, textColor) ;
		
		Point inputPos = Util.translate(topLeftPos, 0, BORDER + size.height + 35) ;
		liveInput.displayTypingField(inputPos, true) ;
		GamePanel.getDP().drawImage(Player.getCoinImg(), Util.translate(inputPos, 5, 0), Align.centerLeft) ;
	}

}
