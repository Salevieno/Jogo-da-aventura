package main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import components.GameButton;
import components.IconFunction;
import graphics.Align;
import liveBeings.Player;
import utilities.Util;

public abstract class LoadingGame
{
	
    private static int loadingStep ;
    private static GameButton startButton ;

	private static final Image LoadingGif ;
	private static final Image petImage0 ;
	
    static
    {
    	loadingStep = 0 ;
		String path = Game.ImagesPath  + "\\Opening\\";
    	Image startImage = Util.loadImage(path + "Start.png") ;
    	Image startImageSelected = Util.loadImage(path + "Start Selected.gif") ;
		IconFunction startAction = () -> { loadingStep = 12 ;} ;
    	startButton = new GameButton(Util.Translate(Game.getScreen().getCenter(), 0, 80), Align.center, "start game", startImage, startImageSelected, startAction) ;
    	startButton.deactivate() ;
    	
		LoadingGif = Util.loadImage(path + "Loading.gif") ;
		petImage0 = Util.loadImage(Game.ImagesPath + "\\Pet\\" + "PetType" + String.valueOf(0) + ".png") ;
    }
    
    
    private static boolean loadingIsOver() { return 11 <= loadingStep ;}
    
    private static boolean startGameButtonClicked() { return loadingStep == 12 ;}
	
	public static void run(Player player, Point mousePos)
	{
		display(player.getCurrentAction(), mousePos) ;
		if (!loadingIsOver())
		{
			Game.initialize(loadingStep) ;
			loadingStep += 1 ;
		}

		if (startButton.isClicked(mousePos, player.getCurrentAction()))
		{
			startButton.act() ;
		}
		
		if (loadingIsOver())
		{
			startButton.activateAndSelect() ;
		}

		if (startGameButtonClicked())
		{
			startButton.deactivate() ;			
			player.startCounters() ;
			Game.switchToMainState() ;
		}
	}
	
	private static void display(String action, Point mousePos)
	{
//			Color textColor = Game.palette[0] ;
//			Point moveInfoTopLeft = new Point(40, 60) ;
//			GamePanel.DP.drawText(Util.Translate(moveInfoTopLeft, 100, 0), Align.center, 0, "Principais ações", font, textColor) ;
//			
//			Image[] moveInfoImages = new Image[] {Game.getPlayer().getMovingAni().movingRightGif, SideBar.getIconImages()[2], Game.getPlayer().getMovingAni().idleGif, SideBar.getIconImages()[1]} ;
//			String[] moveInfoText = new String[] {"Moving: W A S D ou setas", "Mochila: B", "Janela do jogador: C", "Quests: Q"} ;
//			for (int i = 0 ; i <= moveInfoImages.length - 1; i += 1)
//			{
//				Point imageCenterLeft = Util.Translate(moveInfoTopLeft, 0, 100 + 50 * i) ;
//				GamePanel.DP.drawImage(moveInfoImages[i], imageCenterLeft, Align.center);
//				GamePanel.DP.drawText(Util.Translate(imageCenterLeft, 35, 0), Align.centerLeft, 0, moveInfoText[i], smallFont, textColor) ;
//			}
//			
//			
//			Point atkInfoTopLeft = new Point(380, 60) ;
//			GamePanel.DP.drawText(Util.Translate(atkInfoTopLeft, 120, 0), Align.center, 0, "Ações de luta", font, textColor) ;
//			
//			Image[] atkInfoImages = new Image[] {Equip.SwordImage, Equip.ShieldImage, Player.MagicBlissGif} ;
//			String[] atkInfoText = new String[] {"Attack: Y", "Defense: U", "Spells: 0, 1...F11, F12"} ;
//			for (int i = 0 ; i <= atkInfoImages.length - 1; i += 1)
//			{
//				Point imageCenterLeft = Util.Translate(atkInfoTopLeft, 0, 100 + 50 * i) ;
//				GamePanel.DP.drawImage(atkInfoImages[i], imageCenterLeft, Align.center);
//				GamePanel.DP.drawText(Util.Translate(imageCenterLeft, 35, 0), Align.centerLeft, 0, atkInfoText[i], smallFont, textColor) ;
//			}
//			
//			GamePanel.DP.drawImage(LoadingEnfeite, new Point(0, 0), Align.topLeft) ;
		
		GamePanel.DP.drawRect(new Point(0, 0), Align.topLeft, Game.getScreen().getSize(), Game.palette[0], null) ;
		GamePanel.DP.drawImage(petImage0, Game.getScreen().getCenter(), Align.center) ;
		
		if (!loadingIsOver())
		{
			Dimension loadingBarSize = new Dimension(400, 30) ;
			Point loadingTextCenter = Util.Translate(Game.getScreen().getCenter(), 0, 80) ;
			Point loadingBarCenterLeft = Util.Translate(Game.getScreen().getCenter(), -loadingBarSize.width / 2, 80) ;
			Dimension loadedBarSize = new Dimension(loadingStep * loadingBarSize.width / 11, loadingBarSize.height) ;
			GamePanel.DP.drawImage(LoadingGif, loadingTextCenter, Align.center) ;
			GamePanel.DP.drawRoundRect(loadingBarCenterLeft, Align.centerLeft, loadingBarSize, 2, null, Game.palette[0], true);
			GamePanel.DP.drawRoundRect(loadingBarCenterLeft, Align.centerLeft, loadedBarSize, 1, Game.palette[18], Game.palette[0], false);
		}

		if (startButton.isActive())
		{
			startButton.display(0, true, mousePos) ;
		}
	}
}
